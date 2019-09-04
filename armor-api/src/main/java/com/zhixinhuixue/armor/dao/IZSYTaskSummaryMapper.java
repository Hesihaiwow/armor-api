package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.TaskReviewBO;
import com.zhixinhuixue.armor.model.bo.TaskSummaryBO;
import com.zhixinhuixue.armor.model.pojo.TaskSummary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/9/3 14:46
 */
public interface IZSYTaskSummaryMapper {

    /**
     * 根据任务id查询
     * @author sch
     * @param taskId 任务id
     */
    List<TaskSummaryBO> selectListByTask(@Param("taskId") Long taskId);

    /**
     * 批量插入
     * @param list 集合
     */
    int insertBatch(@Param("list") List<TaskSummary> list);

    /**
     * 根据任务id删除总结
     * @param taskId 任务id
     */
    void deleteByTask(@Param("taskId") Long taskId);
}
