package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.WeekPublishAddReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishEditReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishQueryReqDTO;
import com.zhixinhuixue.armor.service.IZSYWeekPublishService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author sch
 * @DATE 2019/6/11 9:55
 */
@Api(value = "周发版计划接口",description="周发版计划接口",tags = "/week-publish")
@RequestMapping("/api/week-publish")
@RestController
public class ZSYWeekPublishController {
    @Autowired
    private IZSYWeekPublishService weekPublishService;

    /**
     * 新增发版计划
     * @param reqDTO 参数
     */
    @PostMapping("")
    public String addPublishPlan(@Valid @RequestBody WeekPublishAddReqDTO reqDTO){
        weekPublishService.addPublishPlan(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑发版计划
     * @param reqDTO 参数
     * @param wppId 计划id
     */
    @PutMapping("/{wppId}")
    public String editPublishPlan(@Valid @RequestBody WeekPublishEditReqDTO reqDTO,@PathVariable("wppId")Long wppId){
        weekPublishService.editPublishPlan(reqDTO,wppId);
        return ZSYResult.success().build();
    }

    /**
     * 删除发版计划
     * @param wppId 计划id
     */
    @DeleteMapping("/{wppId}")
    public String deletePublishPlan(@PathVariable("wppId")Long wppId){
        weekPublishService.deletePublishPlan(wppId);
        return ZSYResult.success().build();
    }

    /**
     * 获取待发布任务
     */
    @GetMapping("/task-wait-deploy")
    public String getWaitDeployTasks(){
        return ZSYResult.success().data(weekPublishService.getWaitDeployTasks()).build();
    }

    /**
     * 获取开发和测试阶段任务
     */
    @GetMapping("/task-dev-test")
    public String getDevAndTestTasks(@RequestParam String wppIdStr){
        return ZSYResult.success().data(weekPublishService.getDevAndTestTasks(wppIdStr)).build();
    }

    /**
     * 分页查询
     * @param reqDTO 参数
     */
    @PostMapping("/page")
    public String getPage(@RequestBody WeekPublishQueryReqDTO reqDTO){
        return ZSYResult.success().data(weekPublishService.getPage(reqDTO)).build();
    }

    /**
     * 查询发版计划详情
     * @param wppId 计划id
     */
    @GetMapping("/{wppId}")
    public String getPublishPlanById(@PathVariable("wppId")Long wppId){
        return ZSYResult.success().data(weekPublishService.getPublishPlanById(wppId)).build();
    }

    /**
     * 查询发版计划涉及人员
     * @param wppId 计划id
     */
    @GetMapping("/publish-users/{wppId}")
    public String getPublishUsers(@PathVariable("wppId")Long wppId){
        return ZSYResult.success().data(weekPublishService.getPublishUsers(wppId)).build();
    }
}
