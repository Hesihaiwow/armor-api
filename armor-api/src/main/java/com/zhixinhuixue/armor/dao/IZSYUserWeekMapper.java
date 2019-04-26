package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.StatsUserWeekBO;
import com.zhixinhuixue.armor.model.dto.request.UserWeekStatsReqDTO;
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
    List<StatsUserWeekBO> getUserWeekStats(@Param("weekNumber") int weeekNumber, @Param("year") int year,@Param("departmentId") Long departmentId);

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
}
