package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.service.IZSYUserWeekService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 周工作量
 * Created by Lang on 2017/12/4 0001.
 */
@RequestMapping("/api/userWeek")
@RestController
public class ZSYUserWeekController extends ZSYController {

    @Resource
    private IZSYUserWeekService userWeekService;

    /**
     * 获取周工作量
     *
     * @return
     */
    @GetMapping(value = "/{taskId}/{id}/{year}/{weekNumber}")
    public String getUserWeekHours(@PathVariable("taskId") Long taskId, @PathVariable("id") Long id, @PathVariable("year") int year, @PathVariable("weekNumber") int weekNumber) {
        return ZSYResult.success().data(userWeekService.getUserWeekHours(taskId, id, weekNumber, year)).build();
    }

    /**
     * 获取任务时间详情
     *
     * @return
     */
    @GetMapping(value = "/task/{taskId}/{userId}")
    public String getUserWeekHours(@PathVariable("taskId") Long taskId, @PathVariable("userId") Long userId) {
        return ZSYResult.success().data(userWeekService.getTaskUserHours(taskId, userId)).build();
    }

    /**
     * 获取去除某个任务后的周工时分配
     *
     * @param taskId
     * @param userId
     * @param year
     * @param weekNumber
     * @return
     */
    @GetMapping("/without/{taskId}/{userId}/{year}/{weekNumber}")
    public String getUserWeekHoursWithoutTask(@PathVariable("taskId") Long taskId, @PathVariable("userId") Long userId,
                                              @PathVariable("year") int year, @PathVariable("weekNumber") int weekNumber) {
        return ZSYResult.success().data(userWeekService.getUserWeekHoursWithoutTask(taskId, userId, year, weekNumber)).build();
    }


}
