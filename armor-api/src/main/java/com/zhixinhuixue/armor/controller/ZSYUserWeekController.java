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
    @GetMapping(value = "/{id}/{year}/{weekNumber}")
    public String getUserWeekHours(@PathVariable("id") Long id,@PathVariable("year") int year , @PathVariable("weekNumber") int weekNumber) {
        return ZSYResult.success().data(userWeekService.getUserWeekHours(id, weekNumber, year)).build();
    }



}
