package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskEvaluation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/22 11:00
 */
public interface IZSYTaskEvaluationMapper {

    /**
     * 根据任务查询已经评价的用户
     * @author sch
     * @param taskId
     * @return
     */
    List<Long> selectEvaluatedUsersByTask(@Param("taskId") Long taskId);

    /**
     * 批量插入任务评价
     * @author sch
     * @param list
     * @return
     */
    int insertBatch(List<TaskEvaluation> list);

    /**
     * 根据任务和用户查询评价
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    List<TaskEvaluation> selectByTaskAndUser(@Param("taskId") Long taskId,@Param("userId")  Long userId);
}
