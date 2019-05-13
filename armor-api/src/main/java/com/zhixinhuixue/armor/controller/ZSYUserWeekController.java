package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.service.IZSYUserWeekService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Lang on 2017/12/4 0001.
 */
@Api(value = "周工作量", description = "周任务操作接口", tags = "/userWeek")
@RequestMapping("/api/userWeek")
@RestController
public class ZSYUserWeekController extends ZSYController {

    @Autowired
    private IZSYUserWeekService userWeekService;

    /**
     * 获取周工作量
     *
     * @return
     */
    @ApiOperation("阶段列表")
    @GetMapping(value = "/{taskId}/{id}/{year}/{weekNumber}")
    public String getUserWeekHours(@PathVariable("taskId") Long taskId,@PathVariable("id") Long id,@PathVariable("year") int year , @PathVariable("weekNumber") int weekNumber) {
        return ZSYResult.success().data(userWeekService.getUserWeekHours(taskId, id, weekNumber, year)).build();
    }

    /**
     * 获取任务时间详情
     *
     * @return
     */
    @ApiOperation("获取任务时间详情")
    @GetMapping(value = "/task/{taskId}/{userId}")
    public String getUserWeekHours(@PathVariable("taskId") Long taskId,@PathVariable("userId") Long userId) {
        return ZSYResult.success().data(userWeekService.getTaskUserHours(taskId, userId)).build();
    }

    @ApiOperation("获取去除某个任务后的周工时分配")
    @GetMapping("/without/{taskId}/{userId}/{year}/{weekNumber}")
    public String getUserWeekHoursWithoutTask(@PathVariable("taskId") Long taskId,@PathVariable("userId") Long userId,
                                              @PathVariable("year") int year , @PathVariable("weekNumber") int weekNumber){
        return ZSYResult.success().data(userWeekService.getUserWeekHoursWithoutTask(taskId,userId,year,weekNumber)).build();
    }


}
