package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.FeedbackBo;
import com.zhixinhuixue.armor.model.dto.request.FeedbackListReqDTO;
import com.zhixinhuixue.armor.model.pojo.Feedback;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;

import java.util.List;

public interface IZSYFeedbackMapper {

    Feedback selectById(Long id);

    /**
     * 添加需求
     * @param feedback
     * @return
     */
    int insertFeedback(Feedback feedback);

    /**
     * 查询需求列表
     * @param feedbackListReqDTO
     * @return
     */
    Page<FeedbackBo> selectFeedbackPage(FeedbackListReqDTO feedbackListReqDTO);

    /**
     * 修改需求
     * @param feedback
     * @return
     */
    int updateByFeedbackId(Feedback feedback);

    List<String> getOrigin();


}