package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.AddEvaluationReqDTO;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author sch
 * @DATE 2019/5/22 10:59
 */
public interface IZSYTaskEvaluationService {
    /**
     * 评价
     * @author sch
     * @param reqDTO
     * @return
     */
    ZSYResult evaluate(AddEvaluationReqDTO reqDTO);

    /**
     * 完成任务
     * @author sch
     * @param taskId
     */
    void finishTask(Long taskId);
}
