package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.WeekPublishPlan;
import com.zhixinhuixue.armor.model.pojo.WeekPublishPlanPlatform;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/10 18:49
 */
public interface IZSYWeekPublishPlanMapper {

    /**
     * 新增周发版任务
     * @author sch
     * @param weekPublishPlan
     * @return
     */
    int insert(@Param("weekPublishPlan") WeekPublishPlan weekPublishPlan);

    /**
     * 校验是否已经存在
     * @author  sch
     * @param taskId
     * @return
     */
    List<WeekPublishPlan> selectByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据主键查询
     * @author sch
     * @param id
     * @return
     */
    WeekPublishPlan selectById(@Param("id") Long id);

    /**
     * 删除原来的task-platform
     * @author sch
     * @param taskId
     * @return
     */
    int deletePublishPlatformByTask(@Param("taskId")Long taskId);

    /**
     * 更新
     * @author sch
     * @param exist
     * @return
     */
    int updateById(WeekPublishPlan exist);

    /**
     * 批量插入任务需要发版的平台
     * @param list
     * @return
     */
    int insertPublishPlatformBatch(List<WeekPublishPlanPlatform> list);
}
