package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.github.pagehelper.page.PageMethod.startPage;

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
     *
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult add(OnlineQuestionReqDTO reqDTO) {
        OnlineQuestion onlineQuestion = new OnlineQuestion();
        BeanUtils.copyProperties(reqDTO, onlineQuestion);
        onlineQuestion.setOqrId(snowFlakeIDHelper.nextId());
        onlineQuestion.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        onlineQuestion.setCreateTime(new Date());
        onlineQuestion.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        onlineQuestion.setStatus(ZSYTaskStatus.DOING.getValue());
        onlineQuestion.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
        onlineQuestion.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        if (onlineQuestionMapper.insertQuestion(onlineQuestion) == 0) {
            throw new ZSYServiceException("新增线上问题失败");
        }
        if (!CollectionUtils.isEmpty(reqDTO.getUrlList())) {
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
            if (!list.isEmpty()) {
                onlineQuestionMapper.insertUrls(list);
            }
        }
        return ZSYResult.success().data(onlineQuestion.getOqrId());
    }

    /**
     * 根据状态分页查看我的线上问题
     *
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<OnlineQuestionResDTO> selectMyQuestionPage(MyQuestionReqDTO reqDTO) {
        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<OnlineQuestionBO> onlineQuestionBOs = onlineQuestionMapper.selectOnlineQuestionPageByStatus(reqDTO, ZSYTokenRequestContext.get().getUserId());
        Page<OnlineQuestionResDTO> onlineQuestionResDTOS = new Page<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOs)) {
            BeanUtils.copyProperties(onlineQuestionBOs, onlineQuestionResDTOS);
            onlineQuestionBOs.forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO onlineQuestionResDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO, onlineQuestionResDTO);
                onlineQuestionResDTOS.add(onlineQuestionResDTO);
            });
        }
        return new PageInfo<>(onlineQuestionResDTOS);
    }

    /**
     * 查看进行中线上问题
     *
     * @return
     */
    @Override
    public List<OnlineQuestionResDTO> selectRunningQuestion() {
        List<OnlineQuestionBO> onlineQuestionBOList = onlineQuestionMapper.selectRunningQuestion(ZSYTokenRequestContext.get().getUserId());
        List<OnlineQuestionResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOList)) {
            BeanUtils.copyProperties(onlineQuestionBOList, list);
            onlineQuestionBOList.forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO resDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO, resDTO);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String endDate = sdf.format(onlineQuestionBO.getEndTime());
                String today = sdf.format(new Date());
                try {
                    Date endTime = sdf.parse(endDate);
                    Date nowTime = sdf.parse(today);
                    if (endTime.before(nowTime)) {
                        resDTO.setIsToday(-1);
                    } else if (endTime.after(nowTime)) {
                        resDTO.setIsToday(1);
                    } else {
                        resDTO.setIsToday(0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<String> urls = onlineQuestionMapper.selectUrlsById(onlineQuestionBO.getOqrId());
                if (!CollectionUtils.isEmpty(urls)) {
                    resDTO.setUrlList(urls);
                } else {
                    resDTO.setUrlList(null);
                }
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 查看已完成线上问题
     *
     * @return
     */
    @Override
    public PageInfo<OnlineQuestionResDTO> selectCompletedQuestion(Integer pageNum) {
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<OnlineQuestionBO> onlineQuestionBOList = onlineQuestionMapper.selectCompletedQuestion(ZSYTokenRequestContext.get().getUserId());
        Page<OnlineQuestionResDTO> page = new Page<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOList)) {
            BeanUtils.copyProperties(onlineQuestionBOList, page);
            onlineQuestionBOList.forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO resDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO, resDTO);
                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 查看待审核线上问题
     *
     * @return
     */
    @Override
    public PageInfo<OnlineQuestionResDTO> selectWaitQuestion(Integer pageNum) {
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<OnlineQuestionBO> onlineQuestionBOList = onlineQuestionMapper.selectWaitQuestion(ZSYTokenRequestContext.get().getOrgId());
        Page<OnlineQuestionResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOList)) {
            BeanUtils.copyProperties(onlineQuestionBOList, list);
            onlineQuestionBOList.forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO resDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO, resDTO);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String endDate = sdf.format(onlineQuestionBO.getEndTime());
                String today = sdf.format(new Date());
                try {
                    Date endTime = sdf.parse(endDate);
                    Date nowTime = sdf.parse(today);
                    if (endTime.before(nowTime)) {
                        resDTO.setIsToday(-1);
                    } else if (endTime.after(nowTime)) {
                        resDTO.setIsToday(1);
                    } else {
                        resDTO.setIsToday(0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<String> urls = onlineQuestionMapper.selectUrlsById(onlineQuestionBO.getOqrId());
                if (!CollectionUtils.isEmpty(urls)) {
                    resDTO.setUrlList(urls);
                } else {
                    resDTO.setUrlList(null);
                }
                list.add(resDTO);
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * 查看审核通过线上问题
     *
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<OnlineQuestionResDTO> selectAcceptedQuestion(Integer pageNum) {
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<OnlineQuestionBO> onlineQuestionBOS = onlineQuestionMapper.selectAcceptedQuestion(ZSYTokenRequestContext.get().getOrgId());
        Page<OnlineQuestionResDTO> page = new Page<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOS)) {
            BeanUtils.copyProperties(onlineQuestionBOS, page);
            onlineQuestionBOS.forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO resDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO, resDTO);
                List<String> urls = onlineQuestionMapper.selectUrlsById(onlineQuestionBO.getOqrId());
                if (!CollectionUtils.isEmpty(urls)) {
                    resDTO.setUrlList(urls);
                } else {
                    resDTO.setUrlList(null);
                }
                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }


    /**
     * 查看审核中(审核通过)线上问题
     *
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<OnlineQuestionResDTO> selectCheckQuestionPage(MyQuestionReqDTO reqDTO) {
        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        reqDTO.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        Page<OnlineQuestionBO> onlineQuestionBOS = onlineQuestionMapper.selectCheckQuestionPageByReviewStatus(reqDTO);
        Page<OnlineQuestionResDTO> onlineQuestionResDTOS = new Page<>();
        if (!CollectionUtils.isEmpty(onlineQuestionBOS)) {
            BeanUtils.copyProperties(onlineQuestionBOS, onlineQuestionResDTOS);
            onlineQuestionBOS.forEach(onlineQuestionBO -> {
                OnlineQuestionResDTO onlineQuestionResDTO = new OnlineQuestionResDTO();
                BeanUtils.copyProperties(onlineQuestionBO, onlineQuestionResDTO);
                onlineQuestionResDTOS.add(onlineQuestionResDTO);
            });
        }
        return new PageInfo<>(onlineQuestionResDTOS);
    }

    /**
     * 审核线上问题通过
     *
     * @param oqrId
     * @return
     */
    @Override
    @Transactional
    public ZSYResult acceptQuestion(Long oqrId) {
        OnlineQuestion onlineQuestion = onlineQuestionMapper.selectById(oqrId);
        if (onlineQuestion == null || onlineQuestion.getIsDelete() == 1) {
            throw new ZSYServiceException("该线上问题不存在");
        }
        onlineQuestion.setReviewStatus(3);
        if (onlineQuestionMapper.updateById(onlineQuestion) == 0) {
            throw new ZSYServiceException("审核失败");
        }
        return ZSYResult.success().data(onlineQuestion.getOqrId());
    }

    /**
     * 删除线上问题
     *
     * @param oqrId
     * @return
     */
    @Override
    @Transactional
    public ZSYResult deleteQuestion(Long oqrId) {
        OnlineQuestion onlineQuestion = onlineQuestionMapper.selectById(oqrId);
        if (onlineQuestion == null || onlineQuestion.getIsDelete() == 1) {
            throw new ZSYServiceException("该线上问题不存在");
        }
        onlineQuestion.setIsDelete(1);
        onlineQuestion.setStatus(3);
        onlineQuestion.setReviewStatus(2);
        if (onlineQuestionMapper.updateById(onlineQuestion) == 0) {
            throw new ZSYServiceException("删除失败");
        }
        List<String> urls = onlineQuestionMapper.selectUrlsById(onlineQuestion.getOqrId());
        if (!CollectionUtils.isEmpty(urls) && onlineQuestionMapper.deleteUrlByOqrId(onlineQuestion.getOqrId()) == 0) {
            throw new ZSYServiceException("修改失败");
        }
        return ZSYResult.success().data(onlineQuestion.getOqrId());
    }


    /**
     * 个人完成线上问题
     *
     * @param oqrId
     * @return
     */
    @Override
    @Transactional
    public ZSYResult finishQuestion(Long oqrId) {
        OnlineQuestion onlineQuestion = onlineQuestionMapper.selectById(oqrId);
        if (onlineQuestion == null || onlineQuestion.getIsDelete() == 1) {
            throw new ZSYServiceException("该线上问题不存在");
        }
        onlineQuestion.setStatus(2);
        onlineQuestion.setReviewStatus(3);
        onlineQuestion.setCompleteTime(new Date());
        if (onlineQuestionMapper.updateById(onlineQuestion) == 0) {
            throw new ZSYServiceException("线上问题完成失败");
        }
        return ZSYResult.success().data(onlineQuestion.getOqrId());
    }

    /**
     * 修改线上问题
     *
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult updateQuestion(UpdateQuestionReqDTO reqDTO) {
        OnlineQuestion onlineQuestion = onlineQuestionMapper.selectById(reqDTO.getOqrId());
        if (onlineQuestion == null || onlineQuestion.getIsDelete() == 1) {
            throw new ZSYServiceException("该线上问题不存在");
        }
        BeanUtils.copyProperties(reqDTO, onlineQuestion);
        if (onlineQuestionMapper.updateById(onlineQuestion) == 0) {
            throw new ZSYServiceException("修改失败");
        }
        List<String> urls = onlineQuestionMapper.selectUrlsById(reqDTO.getOqrId());
        if (!CollectionUtils.isEmpty(urls)) {
            //删除原来的图片地址
            onlineQuestionMapper.deleteUrlByOqrId(onlineQuestion.getOqrId());
        }
        if (!CollectionUtils.isEmpty(reqDTO.getUrlList())) {
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
            if (!list.isEmpty()) {
                onlineQuestionMapper.insertUrls(list);
            }
        }
        return ZSYResult.success().data(onlineQuestion.getOqrId());
    }

}
