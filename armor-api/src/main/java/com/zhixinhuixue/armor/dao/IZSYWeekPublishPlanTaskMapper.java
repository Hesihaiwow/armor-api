package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.WeekPublishPlanTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @time 2020/9/1 14:15
 */
public interface IZSYWeekPublishPlanTaskMapper {

    /**
     * 批量新增
     * @param list 集合
     */
    void insertBatch(@Param("list") List<WeekPublishPlanTask> list);

    /**
     * 根据计划id删除关联任务
     * @param wppId 计划id
     */
    void deleteByWppId(@Param("wppId") Long wppId);

    /**
     * 根据计划id查询
     * @param wppId 计划id
     * @return List<WeekPublishPlanTask>
     */
    List<WeekPublishPlanTask> selectByWppId(@Param("wppId") Long wppId);
}
