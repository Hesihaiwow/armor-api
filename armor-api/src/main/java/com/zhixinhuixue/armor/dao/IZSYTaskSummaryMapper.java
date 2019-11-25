package com.zhixinhuixue.armor.dao;

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
     * @author sch
     * @param list 集合
     */
    int insertBatch(@Param("list") List<TaskSummary> list);

    /**
     * 根据任务id删除总结
     * @author sch
     * @param taskId 任务id
     */
    void deleteByTask(@Param("taskId") Long taskId);

    /**
     * 新增
     * @author sch
     * @param summary 任务总结
     */
    int insert(TaskSummary summary);

    /**
     * 根据主键查询
     * @author sch
     * @param summaryId 总结id
     */
    TaskSummary selectById(@Param("summaryId") Long summaryId);

    /**
     * 更新
     * @author sch
     * @param summary 任务总
     */
    int update(TaskSummary summary);

    /**
     * 所有任务总结
     * @author sch
     */
    List<TaskSummary> selectAll();
}
