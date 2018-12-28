package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYOnlineQuestionMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.OnlineQuestionBO;
import com.zhixinhuixue.armor.model.dto.request.MyQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.OnlineQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UpdateQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineQuestionResDTO;
import com.zhixinhuixue.armor.model.pojo.OnlineQuestion;
import com.zhixinhuixue.armor.model.pojo.QuestionUrl;
import com.zhixinhuixue.armor.service.IZSYOnlineQuestionService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by SCH on 2018-12-27
 */
@Service
public class ZSYOnlineQuestionService implements IZSYOnlineQuestionService {

    @Autowired
    private IZSYOnlineQuestionMapper onlineQuestionMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 新增线上问题
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult add(OnlineQuestionReqDTO reqDTO) {
        OnlineQuestion onlineQuestion = new OnlineQuestion();
        BeanUtils.copyProperties(reqDTO,onlineQuestion);
        onlineQuestion.setOqrId(snowFlakeIDHelper.nextId());
        onlineQuestion.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        onlineQuestion.setCreateTime(new Date());
        onlineQuestion.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        onlineQuestion.setStatus(ZSYTaskStatus.DOING.getValue());
        onlineQuestion.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
        if (onlineQuestionMapper.insertQuestion(onlineQuestion) == 0){
            throw new ZSYServiceException("新增线上问题失败");
        }
        if (!CollectionUtils.isEmpty(reqDTO.getUrlList())){
            List<QuestionUrl> list = new ArrayList<>();
            reqDTO.getUrlList()
                    .stream().filter(url -> !Strings.isNullOrEmpty(url))
                    .forEach(url -> {
                        QuestionUrl questionUrl = new QuestionUrl();
                        questionUrl.setId(snowFlakeIDHelper.nextId());
                        questionUrl.setOqrId(onlineQuestion.getOqrId());
                        questionUrl.setUrl(url);
                        questionUrl.setCreateTime(new Date());
                        list.add(questionUrl);
                    });
            if (!list.isEmpty()){
                if (onlineQuestionMapper.insertUrls(list) == 0){
                    throw new ZSYServiceException("添加线上问题图片地址失败");
                }
            }
        }
        return ZSYResult.success().data(onlineQuestion.getOqrId());
    }

    /**
     * 根据状态分页查看我的线上问题
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<OnlineQuestionResDTO> selectMyQuestionPage(MyQuestionReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<OnlineQuestionBO> onlineQuestionBOs = onlineQuestionMapper.selectOnlineQuestionPageByStatus(reqDTO,ZSYTokenRequestContext.get().getUserId());
        Page<OnlineQuestionResDTO> onlineQuestionResDTOS = new Page<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOs)){
            BeanUtils.copyProperties(onlineQuestionBOs,onlineQuestionResDTOS);
            onlineQuestionBOs.stream().forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO onlineQuestionResDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO,onlineQuestionResDTO);
                onlineQuestionResDTOS.add(onlineQuestionResDTO);
            });
        }
        return new PageInfo<>(onlineQuestionResDTOS);
    }

    /**
     * 查看进行中线上问题
     * @return
     */
    @Override
    public List<OnlineQuestionResDTO> selectRunningQuestion() {
        List<OnlineQuestionBO> onlineQuestionBOList = onlineQuestionMapper.selectRunningQuestion(ZSYTokenRequestContext.get().getUserId());
        List<OnlineQuestionResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOList)){
            BeanUtils.copyProperties(onlineQuestionBOList,list);
            onlineQuestionBOList.stream().forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO resDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO,resDTO);
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 查看审核中(审核通过)线上问题
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<OnlineQuestionResDTO> selectCheckQuestionPage(MyQuestionReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE_WAIT);
        Page<OnlineQuestionBO> onlineQuestionBOS = onlineQuestionMapper.selectCheckQuestionPageByReviewStatus(reqDTO);
        Page<OnlineQuestionResDTO> onlineQuestionResDTOS = new Page<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOS)){
            BeanUtils.copyProperties(onlineQuestionBOS,onlineQuestionResDTOS);
            onlineQuestionBOS.stream().forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO onlineQuestionResDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO,onlineQuestionResDTO);
                onlineQuestionResDTOS.add(onlineQuestionResDTO);
            });
        }
        return new PageInfo<>(onlineQuestionResDTOS);
    }

    /**
     * 审核线上问题通过
     * @param oqrId
     * @return
     */
    @Override
    @Transactional
    public ZSYResult acceptQuestion(Long oqrId) {
        OnlineQuestion onlineQuestion = onlineQuestionMapper.selectById(oqrId);
        if (onlineQuestion == null || onlineQuestion.getIsDelete() == 1){
            throw new ZSYServiceException("该线上问题不存在");
        }
        onlineQuestion.setReviewStatus(3);
        if (onlineQuestionMapper.updateById(onlineQuestion) == 0){
            throw new ZSYServiceException("审核失败");
        }
        return ZSYResult.success().data(onlineQuestion.getOqrId());
    }

    /**
     * 删除线上问题
     * @param oqrId
     * @return
     */
    @Override
    @Transactional
    public ZSYResult deleteQuestion(Long oqrId) {
        OnlineQuestion onlineQuestion = onlineQuestionMapper.selectById(oqrId);
        if (onlineQuestion == null || onlineQuestion.getIsDelete() == 1){
            throw new ZSYServiceException("该线上问题不存在");
        }
        onlineQuestion.setIsDelete(1);
        onlineQuestion.setStatus(3);
        if (onlineQuestionMapper.updateById(onlineQuestion) == 0){
            throw new ZSYServiceException("删除失败");
        }
        if (onlineQuestionMapper.deleteUrlByOqrId(onlineQuestion.getOqrId()) == 0){
            throw new ZSYServiceException("修改失败");
        }
        return ZSYResult.success().data(onlineQuestion.getOqrId());
    }

    /**
     * 修改线上问题
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult updateQuestion(UpdateQuestionReqDTO reqDTO) {
        OnlineQuestion onlineQuestion = onlineQuestionMapper.selectById(reqDTO.getOqrId());
        if (onlineQuestion == null || onlineQuestion.getIsDelete() == 1){
            throw new ZSYServiceException("该线上问题不存在");
        }
        BeanUtils.copyProperties(reqDTO,onlineQuestion);
        if (onlineQuestionMapper.updateById(onlineQuestion) == 0){
            throw new ZSYServiceException("修改失败");
        }
        if (!CollectionUtils.isEmpty(reqDTO.getUrlList())){
            //删除原来的图片地址
            if (onlineQuestionMapper.deleteUrlByOqrId(onlineQuestion.getOqrId()) == 0){
                throw new ZSYServiceException("修改失败");
            }
            List<QuestionUrl> list = new ArrayList<>();
            reqDTO.getUrlList()
                    .stream().filter(url -> !Strings.isNullOrEmpty(url))
                    .forEach(url -> {
                        QuestionUrl questionUrl = new QuestionUrl();
                        questionUrl.setId(snowFlakeIDHelper.nextId());
                        questionUrl.setOqrId(onlineQuestion.getOqrId());
                        questionUrl.setUrl(url);
                        questionUrl.setCreateTime(new Date());
                        list.add(questionUrl);
                    });
            if (!list.isEmpty()){
                if (onlineQuestionMapper.insertUrls(list) == 0){
                    throw new ZSYServiceException("修改失败");
                }
            }
        }
        return null;
    }


}
