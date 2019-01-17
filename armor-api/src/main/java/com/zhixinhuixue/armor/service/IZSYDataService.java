package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.dto.response.AnnualFeedbackInTypeResDTO;
import com.zhixinhuixue.armor.model.dto.response.ProjectTaskResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTotalHoursResDTO;
import com.zhixinhuixue.armor.model.dto.request.YearReqDTO;

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
}
