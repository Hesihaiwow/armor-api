package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.WeekPublishEditReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishQueryReqDTO;
import com.zhixinhuixue.armor.service.IZSYWeekPublishService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("周发版计划列表")
    @PostMapping("/list")
    public String list(@RequestBody WeekPublishQueryReqDTO reqDTO){
        return ZSYResult.success().data(weekPublishService.list(reqDTO)).build();
    }

    @ApiOperation("编辑")
    @PostMapping("/edit")
    public String edit(@RequestBody WeekPublishEditReqDTO reqDTO){
        weekPublishService.edit(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("按任务负责人分组查看")
    @PostMapping("/list/by-charge")
    public String getListByChargeMan(@RequestBody WeekPublishQueryReqDTO reqDTO){
        return ZSYResult.success().data(weekPublishService.getListByChargeMan(reqDTO)).build();
    }
}
