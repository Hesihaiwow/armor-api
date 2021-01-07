package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.WeekPublishPlanBO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishQueryReqDTO;
import com.zhixinhuixue.armor.model.pojo.WeekPublishPlan;
import org.apache.ibatis.annotations.Param;

/**
 * @author sch
 * @DATE 2019/6/10 18:49
 */
public interface IZSYWeekPublishPlanMapper {

    /**
     * 根据名称查询
     * @param name 名称
     * @return WeekPublishPlan
     */
    WeekPublishPlan selectByName(@Param("name") String name,@Param("orgId")Long orgId);

    /**
     * 新增
     * @param weekPublishPlan 参数
     */
    void insert(WeekPublishPlan weekPublishPlan);

    /**
     * 根据id查询
     * @param wppId 主键
     * @return WeekPublishPlan
     */
    WeekPublishPlan selectById(@Param("wppId") Long wppId,@Param("orgId")Long orgId);

    /**
     * 更新
     * @param weekPublishPlan 参数
     */
    void updateById(WeekPublishPlan weekPublishPlan);

    /**
     * 分页查询
     * @param reqDTO 参数
     * @return Page<WeekPublishPlanBO>
     */
    Page<WeekPublishPlanBO> selectPage(WeekPublishQueryReqDTO reqDTO);
}
