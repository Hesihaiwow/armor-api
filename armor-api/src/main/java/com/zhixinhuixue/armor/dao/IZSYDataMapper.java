package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.pojo.UserLeave;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author SCH
 * @date 2019/1/10 13:04
 */
public interface IZSYDataMapper {

    /**
     * 查询年度需求  来自学管端
     * @author sch
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectAnnualFeedbackFromCoach(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 查询年度需求  来自其他
     * @author sch
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectAnnualFeedbackOhter(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 查询年度 多人任务数
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectAnnualMultiTaskNum(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 查询年度 单人任务数
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectAnnualSingleTaskNum(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 根据type查询需求数量
     * @param beginTime
     * @param endTime
     * @param type
     * @return
     */
    Integer selectDiffFbNumByType(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("type") int type,@Param("orgId")Long orgId);

    /**
     * 查询年度需求总数
     * @return
     */
    Integer selectAnnualAllFbNum(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 查询年度需求最少(最多)的月份及数量
     * @param beginTime
     * @param endTime
     * @param maxOrMin
     * @return
     */
    FeedbackMonthBO selectFbMonth(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("maxOrMin") int maxOrMin,@Param("orgId")Long orgId);

    /**
     * 查询每个月的需求
     * @param beginTime
     * @param endTime
     * @return
     */
    List<FeedbackMonthBO> selectFbMonthList(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 年度任务总耗时(小时)
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectTotalHours(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 年度耗时最长任务id
     * @param beginTime
     * @param endTime
     * @return
     */
    Long selectMostTimeTaskId(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 年度耗时最短任务id
     * @param beginTime
     * @param endTime
     * @return
     */
    Long selectLessTimeTaskId(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 年度各个项目任务数list
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProjectTaskBO> selectProjectTaskList(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 年度请假总次数
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectVacationCount(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 年度请假总时长
     * @param beginTime
     * @param endTime
     * @return
     */
    Float selectVacationTime(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 查询请假 月份-次数-时长  集合
     * @param beginTime
     * @param endTime
     * @return
     */
    List<String> getMonthAndCountAndTimeList(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 查询个人年度完成任务
     * @param beginTime
     * @param endTime
     * @param userId
     * @return
     */
    PersonCompleteTaskBO selectPersonTask(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Long userId);

    /**
     * 查询个人年度请假情况
     * @param beginTime
     * @param endTime
     * @param userId
     * @return
     */
    PersonVacationBO selectPersonVacation(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Long userId);

    /**
     * 查询任务总时长
     * @param taskId
     * @return
     */
    Float selectTaskTimeById(@Param("taskId")Long taskId);

    /**
     * 查询年度个人完成的多人任务
     * @param beginTime
     * @param endTime
     * @param userId
     * @return
     */
    Integer selectPersonMultiTaskNum(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Long userId);

    /**
     * 查询年度个人完成的耗时最长多人任务
     * @param beginTime
     * @param endTime
     * @param userId
     * @return
     */
    Long selectPersonMostTimeTaskId(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Long userId);

    /**
     * 查询年度个人完成的耗时最短多人任务
     * @param beginTime
     * @param endTime
     * @param userId
     * @return
     */
    Long selectPersonLessTimeTaskId(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("userId") Long userId);

    /**
     * 查询任务  个人耗时
     * @param taskId
     * @return
     */
    Float selectPersonTaskTimeById(@Param("taskId") Long taskId,@Param("userId") Long userId);

    /**
     * 年度任务数(优先级)
     * @param beginTime
     * @param endTime
     * @param priority
     * @return
     */
    Integer selectPriorityTaskNum(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("priority") int priority,@Param("orgId")Long orgId);

    /**
     * 查询没有需求数
     * @param beginTime
     * @param endTime
     * @return
     */
    List<String> selectMonthAndFbCountList(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 年度每月任务完成总数
     * @param beginTime
     * @param endTime
     * @return
     */
    List<String> selectMonthAndTaskCountList(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 查询请假人员id
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Long> selectUserIds(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 根据userId查询请假情况
     * @param userId
     * @return
     */
    List<UserLeave> selectUserLeaveById(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("userId") Long userId);

    /**
     * 查询个人年度每个月请假情况
     * @param beginTime
     * @param endTime
     * @param userId
     * @return
     */
    List<String> getMonthAndCountAndTimeListByUser(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("userId") Long userId);

    /**
     * 查询年度完成总多人任务数
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectTotalTaskNum(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 查询年度完成多人任务总时长
     * @param beginTime
     * @param endTime
     * @return
     */
    Float selectTaskTotalTime(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 各阶段总耗时
     * @param beginTime
     * @param endTime
     * @return
     */
    Float selectDiffStageTime(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("jobRole")Integer jobRole,@Param("orgId")Long orgId);

    /**
     * 开发阶段总耗时
     * @param beginTime 开始时间
     * @param endTime 截止时间
     */
    Float selectDevStageTime(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 各阶段参与的任务数量
     * @param beginTime
     * @param endTime
     * @param jobRole
     * @return
     */
    Integer selectDiffStageTaskNum(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("jobRole")Integer jobRole,@Param("orgId")Long orgId);

    /**
     * 开发阶段参与的任务数量
     * @param beginTime 开始时间
     * @param endTime 截止时间
     */
    Integer selectDevStageTaskNum(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 年度已完成多人任务耗时最多的前10个
     * @param beginTime
     * @param endTime
     * @return
     */
    List<Long> selectTop10Task(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("orgId")Long orgId);

    /**
     * 单个任务总耗时
     * @param taskId
     * @return
     */
    Float selectTaskHoursById(@Param("taskId") Long taskId);

    /**
     * 用户加班,调休统计表
     * @param yearMonthStr
     * @return
     */
    List<LeaveAndEworkBO> selectLeaveAndEWork(@Param("yearMonthStr") String yearMonthStr,@Param("orgId")Long orgId);
}
