package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.ExportLeaveAndEworkReqDTO;
import com.zhixinhuixue.armor.model.dto.request.PersonVacationReqDTO;
import com.zhixinhuixue.armor.model.dto.request.YearReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.service.IZSYDataService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 数据统计相关接口
 *
 * @author SCH
 * @date 2019/1/10 13:00
 */
@RestController
@RequestMapping("/api/data")
public class ZSYDataController {

    @Resource
    private IZSYDataService zsyDataService;

    /**
     * 年度需求总数(学管端,其他)
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/feedback-num")
    public String getAnnualFeedbackData(@RequestBody YearReqDTO reqDTO) {
        AnnualFeedbackResDTO resDTO = zsyDataService.getAnnualFeedbackData(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度每月需求总数
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/feedback/month")
    public String getEveryMonthFeedback(@RequestBody YearReqDTO reqDTO) {
        List<Integer> list = zsyDataService.getEveryMonthFeedback(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 年度每月任务完成总数
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/task/month")
    public String getEveryMonthTask(@RequestBody YearReqDTO reqDTO) {
        List<Integer> list = zsyDataService.getEveryMonthTask(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 年度任务总数(多人任务,个人任务)
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/task-num")
    public String getAnnualTaskData(@RequestBody YearReqDTO reqDTO) {
        AnnualTaskResDTO resDTO = zsyDataService.getAnnualTaskData(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度需求各个分类数量
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/feedback-type")
    public String getAnnualFbTypeNum(@RequestBody YearReqDTO reqDTO) {
        AnnualFeedbackInTypeResDTO resDTO = zsyDataService.getAnnualFbTypeNum(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度需求最多和最少的月份
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/feedback/max-min")
    public String getAnnualFbMaxAndMinMonth(@RequestBody YearReqDTO reqDTO) {
        MaxAndMinFbMonthResDTO resDTO = zsyDataService.getAnnualFbMaxAndMinMonth(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度任务总耗时,耗时最长(最短)任务
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/task/total-hours")
    public String getAnnualTaskTotalHours(@RequestBody YearReqDTO reqDTO) {
        TaskTotalHoursResDTO resDTO = zsyDataService.getAnnualTaskTotalHours(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度各个项目任务数
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/task/project-task")
    public String getAnnualProjectTaskNum(@RequestBody YearReqDTO reqDTO) {
        List<ProjectTaskResDTO> list = zsyDataService.getAnnualProjectTaskNum(reqDTO);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 年度任务数(优先级)
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/task/priority")
    public String getAnnualTaskByPriority(@RequestBody YearReqDTO reqDTO) {
        DiffPriorityTaskResDTO resDTO = zsyDataService.getAnnualTaskByPriority(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度请假次数,总时长
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/vacation")
    public String getAnnualVacationCount(@RequestBody YearReqDTO reqDTO) {
        AnnualVacationResDTO resDTO = zsyDataService.getAnnualVacationCount(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度请假每月次数及时长
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/vacation/month")
    public String getEveryMonthVacation(@RequestBody YearReqDTO reqDTO) {
        EveryMonthVacationResDTO resDTO = zsyDataService.getEveryMonthVacation(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度个人数据
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/person-data")
    public String getPersonData(@RequestBody YearReqDTO reqDTO) {
        PersonDataResDTO resDTO = zsyDataService.getPersonData(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 查看个人年度数据
     *
     * @param reqDTO
     * @param userId
     * @return
     */
    @PostMapping("/annual/person-data/{userId}")
    public String getPersonDataByUserId(@RequestBody YearReqDTO reqDTO, @PathVariable Long userId) {
        PersonDataResDTO resDTO = zsyDataService.getPersonDataByUserId(reqDTO, userId);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 查看个人请假情况
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/person-vacation")
    public String getPersonVacation(@RequestBody PersonVacationReqDTO reqDTO) {
        List<PersonVacationResDTO> resDTO = zsyDataService.getPersonVacation(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 个人年度每月请假情况
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/person-vacation/month")
    public String getPersonEveryMonthVacation(@RequestBody YearReqDTO reqDTO) {
        EveryMonthVacationResDTO resDTO = zsyDataService.getPersonEveryMonthVacation(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度已完成任务总耗时(设计,产品,开发,测试)
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/diff-stage/task-time")
    public String getDiffStageTaskTime(@RequestBody YearReqDTO reqDTO) {
        DiffStageTaskTimeResDTO resDTO = zsyDataService.getDiffStageTaskTime(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 年度已完成多人任务耗时最多的前10个
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/annual/most-time-task/top10")
    public String getTop10MostTimeTask(@RequestBody YearReqDTO reqDTO) {
        AnnualTop10Task resDTO = zsyDataService.getTop10MostTimeTask(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 单个任务总耗时
     *
     * @param taskId
     * @return
     */
    @GetMapping("/annual/task/time/{taskId}")
    public String getTaskTime(@PathVariable("taskId") Long taskId) {
        TaskTimeResDTO resDTO = zsyDataService.getTaskTime(taskId);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 查询近12周工作量
     *
     * @return
     */
    @GetMapping("/personal/week-hour-stats")
    public String getWeekHourStats() {
        return ZSYResult.success().data(zsyDataService.getWeekHourStats()).build();
    }

    /**
     * 管理员查看人员近12周工作量
     *
     * @param userId
     * @return
     */
    @GetMapping("/week-hour-stats/{userId}")
    public String getUserWeekHourStats(@PathVariable("userId") Long userId) {
        return ZSYResult.success().data(zsyDataService.getUserWeekHourStats(userId)).build();
    }

    /**
     * 查询任务负责人负责任务相关信息
     *
     * @return
     */
    @GetMapping("/principal-task-stats")
    public String getPrincipalTaskStats() {
        return ZSYResult.success().data(zsyDataService.getPrincipalTaskStats()).build();
    }

    /**
     * 超管查看所有负责人负责任务数
     *
     * @return
     */
    @GetMapping("/principal-task-stats/all")
    public String getAllPrincipalTaskStats() {
        return ZSYResult.success().data(zsyDataService.getAllPrincipalTaskStats()).build();
    }

    /**
     * 导出月度用户加班,调休统计表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/leave-ework/export")
    public String exportLeaveAndEwork(@Valid @RequestBody ExportLeaveAndEworkReqDTO reqDTO) {
        return ZSYResult.success().data(zsyDataService.exportLeaveAndEwork(reqDTO)).build();
    }
}
