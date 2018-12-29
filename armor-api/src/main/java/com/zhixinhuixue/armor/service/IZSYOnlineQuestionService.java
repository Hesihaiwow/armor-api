package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.MyQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.OnlineQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UpdateQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineQuestionResDTO;
import com.zhixinhuixue.armor.source.ZSYResult;

import java.util.List;

/**
 * Created by SCH on 2018-12-27
 */
public interface IZSYOnlineQuestionService {

    /**
     * 新增线上问题
     * @param reqDTO
     * @return
     */
    ZSYResult add(OnlineQuestionReqDTO reqDTO);

    /**
     * 根据状态分页查看我的线上问题
     * @param reqDTO
     * @return
     */
    PageInfo<OnlineQuestionResDTO> selectMyQuestionPage(MyQuestionReqDTO reqDTO);

    /**
     * 查看审核中(审核通过)线上问题
     * @param reqDTO
     * @return
     */
    PageInfo<OnlineQuestionResDTO> selectCheckQuestionPage(MyQuestionReqDTO reqDTO);

    /**
     * 审核线上问题通过
     * @param oqrId
     * @return
     */
    ZSYResult acceptQuestion(Long oqrId);

    /**
     *删除线上问题
     * @param oqrId
     * @return
     */
    ZSYResult deleteQuestion(Long oqrId);

    /**
     * 修改线上问题
     * @param reqDTO
     * @return
     */
    ZSYResult updateQuestion(UpdateQuestionReqDTO reqDTO);

    /**
     * 查看进行中线上问题
     * @return
     */
    List<OnlineQuestionResDTO> selectRunningQuestion();

    /**
     * 查看已完成线上问题
     * @return
     */
    PageInfo<OnlineQuestionResDTO> selectCompletedQuestion(Integer pageNum);

    /**
     * 查看待审核线上问题
     * @return
     */
    List<OnlineQuestionResDTO> selectWaitQuestion();

    /**
     * 查看审核通过线上问题
     * @param pageNum
     * @return
     */
    PageInfo<OnlineQuestionResDTO> selectAcceptedQuestion(Integer pageNum);

    /**
     * 个人完成线上问题
     * @param oqrId
     * @return
     */
    ZSYResult finishQuestion(Long oqrId);
}
