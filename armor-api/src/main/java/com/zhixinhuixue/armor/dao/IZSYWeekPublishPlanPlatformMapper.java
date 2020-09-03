package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.WeekPublishPlanPlatform;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @time 2020/9/1 14:16
 */
public interface IZSYWeekPublishPlanPlatformMapper {

    /**
     * 批量新增
     * @param list 集合
     */
    void insertBatch(@Param("list") List<WeekPublishPlanPlatform> list);

    /**
     * 根据计划id删除关联平台
     * @param wppId 计划id
     */
    void deleteByWppId(@Param("wppId") Long wppId);

    /**
     * 根据计划id查询
     * @param wppId 计划id
     * @return List<WeekPublishPlanPlatform>
     */
    List<WeekPublishPlanPlatform> selectByWppId(@Param("wppId") Long wppId);

    /**
     * 查看平台是否使用
     * @param platformId 平台id
     * @return Integer
     */
    Integer checkPlatformUse(@Param("platformId") Long platformId);
}
