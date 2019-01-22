package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.PersonVacationReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.dto.response.AnnualFeedbackInTypeResDTO;
import com.zhixinhuixue.armor.model.dto.response.ProjectTaskResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTotalHoursResDTO;
import com.zhixinhuixue.armor.model.dto.request.YearReqDTO;
import com.zhixinhuixue.armor.service.IZSYDataService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author SCH
 * @date 2019/1/10 13:00
 */
@RestController
@RequestMapping("/api/data")
@Api(value = "数据统计相关接口", description = "任务管理系统数据统计相关接口", tags = "/data")
public class ZSYDataController {

    @Autowired
    private IZSYDataService zsyDataService;

    @ApiOperation("年度需求总数(学管端,其他)")
    @PostMapping("/annual/feedback-num")
    public String getAnnualFeedbackData(@RequestBody YearReqDTO reqDTO){
        AnnualFeedbackResDTO resDTO = zsyDataService.getAnnualFeedbackData(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度每月需求总数")
    @PostMapping("/annual/feedback/month")
    public String getEveryMonthFeedback(@RequestBody YearReqDTO reqDTO){
        List<Integer> list = zsyDataService.getEveryMonthFeedback(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    @ApiOperation("年度每月任务完成总数")
    @PostMapping("/annual/task/month")
    public String getEveryMonthTask(@RequestBody YearReqDTO reqDTO){
        List<Integer> list = zsyDataService.getEveryMonthTask(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    @ApiOperation("年度任务总数(多人任务,个人任务)")
    @PostMapping("/annual/task-num")
    public String getAnnualTaskData(@RequestBody YearReqDTO reqDTO){
        AnnualTaskResDTO resDTO = zsyDataService.getAnnualTaskData(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度需求各个分类数量")
    @PostMapping("/annual/feedback-type")
    public String getAnnualFbTypeNum(@RequestBody YearReqDTO reqDTO){
        AnnualFeedbackInTypeResDTO resDTO = zsyDataService.getAnnualFbTypeNum(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度需求最多和最少的月份")
    @PostMapping("/annual/feedback/max-min")
    public String getAnnualFbMaxAndMinMonth(@RequestBody YearReqDTO reqDTO){
        MaxAndMinFbMonthResDTO resDTO = zsyDataService.getAnnualFbMaxAndMinMonth(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度任务总耗时,耗时最长(最短)任务")
    @PostMapping("/annual/task/total-hours")
    public String getAnnualTaskTotalHours(@RequestBody YearReqDTO reqDTO){
        TaskTotalHoursResDTO resDTO = zsyDataService.getAnnualTaskTotalHours(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度各个项目任务数")
    @PostMapping("/annual/task/project-task")
    public String getAnnualProjectTaskNum(@RequestBody YearReqDTO reqDTO){
        List<ProjectTaskResDTO> list = zsyDataService.getAnnualProjectTaskNum(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    @ApiOperation("年度任务数(优先级)")
    @PostMapping("/annual/task/priority")
    public String getAnnualTaskByPriority(@RequestBody YearReqDTO reqDTO){
        DiffPriorityTaskResDTO resDTO = zsyDataService.getAnnualTaskByPriority(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度请假次数,总时长")
    @PostMapping("/annual/vacation")
    public String getAnnualVacationCount(@RequestBody YearReqDTO reqDTO){
        AnnualVacationResDTO resDTO = zsyDataService.getAnnualVacationCount(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度请假每月次数及时长")
    @PostMapping("/annual/vacation/month")
    public String getEveryMonthVacation(@RequestBody YearReqDTO reqDTO){
        EveryMonthVacationResDTO resDTO = zsyDataService.getEveryMonthVacation(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度个人数据")
    @PostMapping("/annual/person-data")
    public String getPersonData(@RequestBody YearReqDTO reqDTO){
        PersonDataResDTO resDTO = zsyDataService.getPersonData(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("查看个人年度数据")
    @PostMapping("/annual/person-data/{userId}")
    public String getPersonDataByUserId(@RequestBody YearReqDTO reqDTO, @PathVariable Long userId){
        PersonDataResDTO resDTO = zsyDataService.getPersonDataByUserId(reqDTO,userId);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("查看个人请假情况")
    @PostMapping("/annual/person-vacation")
    public String getPersonVacation(@RequestBody PersonVacationReqDTO reqDTO){
        List<PersonVacationResDTO> resDTO = zsyDataService.getPersonVacation(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("个人年度每月请假情况")
    @PostMapping("/annual/person-vacation/month")
    public String getPersonEveryMonthVacation(@RequestBody YearReqDTO reqDTO){
        EveryMonthVacationResDTO resDTO = zsyDataService.getPersonEveryMonthVacation(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

}
