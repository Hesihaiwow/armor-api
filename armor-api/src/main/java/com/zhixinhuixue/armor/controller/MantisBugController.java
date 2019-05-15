package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.MantisBugQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.MantisBugWeekQueryReqDTO;
import com.zhixinhuixue.armor.service.IZSYMantisBugService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sch
 * @DATE 2019/4/17 17:36
 */
@Api(value = "测试Bug处理接口", description = "测试bug相关操作接口", tags = "/mantis-bug")
@RequestMapping("/api/mantis-bug")
@RestController
public class MantisBugController {

    @Autowired
    private IZSYMantisBugService mantisBugService;

    @ApiOperation("查询mantis中bug信息并导入到任务系统")
    @PostMapping("/import/{projectId}")
    public String importMantisBug(@PathVariable("projectId") Integer projectId){
        mantisBugService.importMantisBug(projectId);
        return ZSYResult.success().build();
    }

//    @ApiOperation("查询mantis用户信息")
//    @GetMapping("/user/list")
//    public String getMantisUserList(){
//        return ZSYResult.success().data(mantisBugService.getMantisUserList()).build();
//    }
//
//    @ApiOperation("查询mantis反馈系统信息")
//    @GetMapping("/category/list")
//    public String getMantisCategory(){
//        return ZSYResult.success().data(mantisBugService.getMantisCategory()).build();
//    }

    @ApiOperation("查询反馈系统(分类)")
    @GetMapping("/category/list")
    public String getCategoryList(){
        return ZSYResult.success().data(mantisBugService.getCategoryList()).build();
    }

    @ApiOperation("按年月查询测试人员bug统计情况")
    @PostMapping("/stats/user")
    public String getBugStatsGroupByUser(@RequestBody MantisBugQueryReqDTO reqDTO){
        return ZSYResult.success().data(mantisBugService.getBugStatsGroupByUser(reqDTO)).build();
    }

    @ApiOperation("查询测试人员周bug数量图表")
    @PostMapping("/bug-week/user")
    public String getBugWeekGroupByUser(@RequestBody MantisBugWeekQueryReqDTO reqDTO){
        return ZSYResult.success().data(mantisBugService.getBugMonthGroupByUser(reqDTO)).build();
    }

    @ApiOperation("查询测试人员线上bug饼型图")
    @PostMapping("/online-bug/user")
    public String getOnlineBugGroupByUser(@RequestBody MantisBugWeekQueryReqDTO reqDTO){
        return ZSYResult.success().data(mantisBugService.getOnlineBugGroupByUser(reqDTO)).build();
    }

    @ApiOperation("按任务统计bug")
    @PostMapping("/stats/task")
    public String getBugStatsGroupByTask(@RequestBody MantisBugQueryReqDTO reqDTO){
        return ZSYResult.success().data(mantisBugService.getBugStatsGroupByTask(reqDTO)).build();
    }

    @ApiOperation("导出数据到Excel")
    @GetMapping("/export/{projectId}")
    public String exportMantisBug(@PathVariable("projectId")Integer projectId){
        return ZSYResult.success().data(mantisBugService.exportMantisBug(projectId)).build();
    }
}
