package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.AvgUserWeekHourBO;
import com.zhixinhuixue.armor.model.bo.StatsUserWeekBO;
import com.zhixinhuixue.armor.model.bo.UserCostBO;
import com.zhixinhuixue.armor.model.bo.UserWeekHourBO;
import com.zhixinhuixue.armor.model.pojo.UserWeek;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public interface IZSYUserWeekMapper {

    int insertList(List<UserWeek> userWeeks);

    int deleteByTaskId(@Param("taskId") Long taskId,@Param("orgId")Long orgId);

    /**
     * 周工作统计
     *
     * @param weeekNumber
     * @return
     */
    List<StatsUserWeekBO> getUserWeekStats(@Param("weekNumber") int weeekNumber, @Param("year") int year
            , @Param("departmentId") Long departmentId, @Param("jobRole") Integer jobRole,@Param("orgId")Long orgId);

    Double getUserWeekHours(@Param("taskId") Long taskId, @Param("userId") Long userId, @Param("weekNumber") int weekNumber, @Param("year") int year,@Param("orgId")Long orgId);

    List<UserWeek> getTaskUserHours(@Param("taskId") Long taskId, @Param("userId") Long userId,@Param("orgId")Long orgId);

    /**
     * 延长时间，获取相同任务相同周的任务
     *
     * @return
     */
    UserWeek getSameWeek(@Param("taskId") Long taskId, @Param("userId") Long userId, @Param("weekNumber") int weekNumber, @Param("year") int year,@Param("orgId")Long orgId);

    int updateHours(@Param("id") Long id, @Param("hours") Double hours);

    /**
     * 删除userWeek
     *
     * @param taskId
     * @param userId
     * @return
     * @author sch
     */
    int deleteByTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") Long userId,@Param("orgId")Long orgId);

    /**
     * 查询userWeek
     *
     * @param taskId
     * @param userId
     * @return
     * @author sch
     */
    List<UserWeek> selectByTaskAndUser(@Param("taskId") Long taskId, @Param("userId") Long userId);

    /**
     * 查询用户某个任务的周工时
     *
     * @param taskId
     * @param userId
     * @param weekNumber
     * @param year
     * @return
     * @author sch
     */
    Double selectHoursByTaskAndUser(@Param("taskId") Long taskId, @Param("userId") Long userId,
                                    @Param("weekNumber") int weekNumber, @Param("year") int year);

    /**
     * 查询不同角色每周所有人工作量的平均时长
     *
     * @param jobRole
     * @param year
     * @param weekOfYear
     * @return
     * @author sch
     */
    AvgUserWeekHourBO selectAvgWeekHour(@Param("jobRole") Integer jobRole, @Param("year") int year, @Param("weekOfYear") int weekOfYear,@Param("orgId")Long orgId);

    /**
     * 根据团队查询周人员投入
     *
     * @param groupId    团队id
     * @param year       年
     * @param weekNumber 周
     * @return List<UserCostBO>
     * @author sch
     */
    List<UserCostBO> selectUserCostByGroup(@Param("groupId") Long groupId, @Param("year") int year, @Param("weekNumber") Integer weekNumber,@Param("orgId")Long orgId);

    /**
     * 查询当前任务之外的周工作量
     *
     * @param userId     用户id
     * @param year       年
     * @param weekNumber 周
     * @param taskId     任务id
     * @author sch
     */
    Double selectWithoutTask(@Param("userId") Long userId, @Param("year") int year,
                             @Param("weekNumber") int weekNumber, @Param("taskId") Long taskId);

    /**
     * 查询用户周工作量
     *
     * @param year    年份
     * @param weeks   星期集合
     * @param jobRole 角色
     * @return List<UserWeekHourBO>
     * @author sch
     */
    List<UserWeekHourBO> selectUserWeekStats(@Param("year") int year, @Param("weeks") List<Integer> weeks, @Param("jobRole") Integer jobRole,@Param("orgId")Long orgId);
}
