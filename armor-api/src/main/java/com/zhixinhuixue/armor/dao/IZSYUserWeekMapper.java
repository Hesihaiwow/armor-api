package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.AvgUserWeekHourBO;
import com.zhixinhuixue.armor.model.bo.StatsUserWeekBO;
import com.zhixinhuixue.armor.model.bo.UserCostBO;
import com.zhixinhuixue.armor.model.pojo.UserWeek;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public interface IZSYUserWeekMapper {

    int insertList(List<UserWeek> userWeeks);

    int deleteByTaskId(Long taskId);

    /**
     * 周工作统计
     * @param weeekNumber
     * @return
     */
    List<StatsUserWeekBO> getUserWeekStats(@Param("weekNumber") int weeekNumber, @Param("year") int year
            ,@Param("departmentId") Long departmentId,@Param("jobRole")Integer jobRole);

    Double getUserWeekHours(@Param("taskId") Long taskId, @Param("userId") Long userId,@Param("weekNumber") int weekNumber,@Param("year") int year);

    List<UserWeek> getTaskUserHours(@Param("taskId") Long taskId, @Param("userId") Long userId);

    /**
     * 延长时间，获取相同任务相同周的任务
     * @return
     */
    UserWeek getSameWeek(@Param("taskId") Long taskId, @Param("userId") Long userId,@Param("weekNumber") int weekNumber,@Param("year") int year);

    int updateHours(@Param("id") Long id, @Param("hours") Double hours);

    /**
     * 删除userWeek
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    int deleteByTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") Long userId);

    /**
     * 查询userWeek
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    List<UserWeek> selectByTaskAndUser(@Param("taskId") Long taskId, @Param("userId") Long userId);

    /**
     * 查询用户某个任务的周工时
     * @author sch
     * @param taskId
     * @param userId
     * @param weekNumber
     * @param year
     * @return
     */
    Double selectHoursByTaskAndUser(@Param("taskId") Long taskId, @Param("userId") Long userId,
                                    @Param("weekNumber") int weekNumber,@Param("year") int year);

    /**
     * 查询不同角色每周所有人工作量的平均时长
     * @author sch
     * @param jobRole
     * @param year
     * @param weekOfYear
     * @return
     */
    AvgUserWeekHourBO selectAvgWeekHour(@Param("jobRole")Integer jobRole, @Param("year")int year, @Param("weekOfYear")int weekOfYear);

    /**
     * 根据团队查询周人员投入
     * @author sch
     * @param groupId 团队id
     * @param year 年
     * @param weekNumber 周
     * @return List<UserCostBO>
     */
    List<UserCostBO> selectUserCostByGroup(@Param("groupId")Long groupId, @Param("year")int year, @Param("weekNumber")Integer weekNumber);
}
