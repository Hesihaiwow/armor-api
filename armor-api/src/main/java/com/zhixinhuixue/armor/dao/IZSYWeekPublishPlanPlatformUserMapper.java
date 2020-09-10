package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.WeekPublishPlanPlatformUserBO;
import com.zhixinhuixue.armor.model.pojo.WeekPublishPlanPlatformUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @time 2020/9/8 17:43
 */
public interface IZSYWeekPublishPlanPlatformUserMapper {

    /**
     * 批量插入
     * @param list 集合
     */
    void insertBatch(@Param("list") List<WeekPublishPlanPlatformUser> list);

    /**
     * 根据发版计划删除
     * @param wppId 计划id
     */
    void deleteByWppId(@Param("wppId") Long wppId);

    /**
     * 根据计划查询
     * @param wppId 计划id
     * @return List<WeekPublishPlanPlatformUserBO>
     */
    List<WeekPublishPlanPlatformUserBO> selectByWppId(@Param("wppId") Long wppId);
}
