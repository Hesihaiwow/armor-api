package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishAddReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishEditReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishPlanDetailResDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskBaseResDTO;
import com.zhixinhuixue.armor.model.dto.response.WeekPublishPlanPageResDTO;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/11 9:57
 */
public interface IZSYWeekPublishService {

    /**
     * 新增发版计划
     * @param reqDTO 参数
     */
    void addPublishPlan(WeekPublishAddReqDTO reqDTO);

    /**
     * 编辑发版计划
     * @param reqDTO 参数
     * @param wppId 计划id
     */
    void editPublishPlan(WeekPublishEditReqDTO reqDTO, Long wppId);

    /**
     * 删除发版计划
     * @param wppId 计划id
     */
    void deletePublishPlan(Long wppId);

    /**
     * 获取待发布任务
     */
    List<TaskBaseResDTO> getWaitDeployTasks();

    /**
     * 获取开发和测试阶段任务
     */
    List<TaskBaseResDTO> getDevAndTestTasks();

    /**
     * 分页查询
     * @param reqDTO 参数
     */
    PageInfo<WeekPublishPlanPageResDTO> getPage(WeekPublishQueryReqDTO reqDTO);

    /**
     * 查询发版计划详情
     * @param wppId 计划id
     */
    WeekPublishPlanDetailResDTO getPublishPlanById(Long wppId);
}
