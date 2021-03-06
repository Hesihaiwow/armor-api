package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.ExportLeaveAndEworkReqDTO;
import com.zhixinhuixue.armor.model.dto.request.PersonVacationReqDTO;
import com.zhixinhuixue.armor.model.dto.request.YearReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;

import java.util.List;

/**
 * @author SCH
 * @date 2019/1/10 13:02
 */
public interface IZSYDataService {

    /**
     * 年度需求总数(学管端,其他)
     * @param reqDTO
     * @return
     * @author sch
     */
    AnnualFeedbackResDTO getAnnualFeedbackData(YearReqDTO reqDTO);

    /**
     * 年度任务总数(多人任务,个人任务)
     * @author sch
     * @param reqDTO
     * @return
     */
    AnnualTaskResDTO getAnnualTaskData(YearReqDTO reqDTO);

    /**
     * 年度需求各个分类数量
     * @author sch
     * @param reqDTO
     * @return
     */
    AnnualFeedbackInTypeResDTO getAnnualFbTypeNum(YearReqDTO reqDTO);

    /**
     * 年度需求最多和最少的月份
     * @author sch
     * @param reqDTO
     * @return
     */
    MaxAndMinFbMonthResDTO getAnnualFbMaxAndMinMonth(YearReqDTO reqDTO);

    /**
     * 年度任务总耗时,耗时最长(最短)任务
     * @author sch
     * @param reqDTO
     * @return
     */
    TaskTotalHoursResDTO getAnnualTaskTotalHours(YearReqDTO reqDTO);

    /**
     * 年度各个项目任务数
     * @author sch
     * @param reqDTO
     * @return
     */
    List<ProjectTaskResDTO> getAnnualProjectTaskNum(YearReqDTO reqDTO);

    /**
     * 年度请假次数,总时长
     * @author sch
     * @param reqDTO
     * @return
     */
    AnnualVacationResDTO getAnnualVacationCount(YearReqDTO reqDTO);

    /**
     * 年度请假每月次数及时长
     * @author sch
     * @param reqDTO
     * @return
     */
    EveryMonthVacationResDTO getEveryMonthVacation(YearReqDTO reqDTO);

    /**
     * 年度个人数据
     * @author sch
     * @param reqDTO
     * @return
     */
    PersonDataResDTO getPersonData(YearReqDTO reqDTO);

    /**
     * 查看个人年度数据
     * @param reqDTO
     * @param userId
     * @return
     */
    PersonDataResDTO getPersonDataByUserId(YearReqDTO reqDTO, Long userId);

    /**
     * 年度任务数(优先级)
     * @param reqDTO
     * @return
     */
    DiffPriorityTaskResDTO getAnnualTaskByPriority(YearReqDTO reqDTO);

    /**
     * 年度每月需求总数
     * @param reqDTO
     * @return
     */
    List<Integer> getEveryMonthFeedback(YearReqDTO reqDTO);

    /**
     * 年度每月任务完成总数
     * @param reqDTO
     * @return
     */
    List<Integer> getEveryMonthTask(YearReqDTO reqDTO);

    /**
     * 查看个人请假情况
     * @param reqDTO
     * @return
     */
    List<PersonVacationResDTO> getPersonVacation(PersonVacationReqDTO reqDTO);

    /**
     * 个人年度每月请假情况
     * @param reqDTO
     * @return
     */
    EveryMonthVacationResDTO getPersonEveryMonthVacation(YearReqDTO reqDTO);

    /**
     * 年度已完成任务总耗时(设计,产品,开发,测试)
     * @param reqDTO
     * @return
     */
    DiffStageTaskTimeResDTO getDiffStageTaskTime(YearReqDTO reqDTO);

    /**
     * 年度已完成多人任务耗时最多的前10个
     * @param reqDTO
     * @return
     */
    AnnualTop10Task getTop10MostTimeTask(YearReqDTO reqDTO);

    /**
     * 单个任务总耗时
     * @param taskId
     * @return
     */
    TaskTimeResDTO getTaskTime(Long taskId);

    /**
     * 近6周工作量
     * @author sch
     * @return
     */
    List<WeekHourStatsResDTO> getWeekHourStats();

    /**
     * 管理员查看人员近12周工作量
     * @author sch
     * @param userId
     * @return
     */
    List<WeekHourStatsResDTO> getUserWeekHourStats(Long userId);

    /**
     * 查询任务负责人负责任务相关信息
     * @author sch
     */
    List<PrincipalTaskNumResDTO> getPrincipalTaskStats();

    /**
     * 超管查看所有负责人负责任务数
     * @author sch
     */
    List<PrincipalTaskNumResDTO> getAllPrincipalTaskStats();

    /**
     * 导出月度用户加班,调休统计表
     * @author sch
     * @param reqDTO 参数
     */
    String exportLeaveAndEwork(ExportLeaveAndEworkReqDTO reqDTO);
}
