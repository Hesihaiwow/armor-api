package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.TaskReviewBO;
import com.zhixinhuixue.armor.model.pojo.TaskReview;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/9/3 14:35
 */
public interface IZSYTaskReviewMapper {

    /**
     * 根据任务id查询
     * @author sch
     * @param taskId 任务id
     * @return
     */
    List<TaskReviewBO> selectListByTask(@Param("taskId") Long taskId);

    /**
     * 批量新增
     * @param list 参数
     */
    int insertBatch(@Param("list")List<TaskReview> list);

    /**
     * 根据任务id删除评审
     * @param taskId 任务id
     */
    void deleteByTask(@Param("taskId") Long taskId);
}
