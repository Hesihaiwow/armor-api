package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.dto.request.AddEvaluationReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EvaluationPageQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.response.PersonEvaluationResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskEvaluationPageResDTO;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    /**
     * 查询待评价任务
     * @author sch
     * @return
     */
    List<TaskDetailBO> getWaitEvaluated();

    /**
     * 分页查看已经评价的任务
     * @author sch
     * @param pageNum
     * @return
     */
    PageInfo<TaskDetailBO> getEvaluated(Integer pageNum);

    /**
     * 管理员分页查看用户所有任务综合评价
     * @author sch
     * @param reqDTO
     * @return
     */
    PageInfo<TaskEvaluationPageResDTO> getUserAvgEvaluation(EvaluationPageQueryReqDTO reqDTO);

    /**
     * 个人主页查看周,月,年的综合评价
     * @author sch
     * @return
     */
    PersonEvaluationResDTO getPersonalAverageEva();
}
