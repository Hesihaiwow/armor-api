package com.zhixinhuixue.armor.dao;


import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserLeaveBO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveListReqDTO;
import com.zhixinhuixue.armor.model.pojo.UserLeave;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public interface IZSYUserLeaveMapper {

    int insertLeave(UserLeave userLeave);

    UserLeaveBO getUserLeaveById(@Param("id") Long id,@Param("orgId")Long orgId);

    Page<UserLeaveBO> getLeaveByReviewStatus(@Param("userId") Long userId, @Param("reviewStatus") int reviewStatus, @Param("departmentId") Long departmentId,@Param("orgId")Long orgId);

    int updateLeave(UserLeave userLeave);

    int deleteById(Long id);

    Page<UserLeaveBO> getLeaveList(UserLeaveListReqDTO userLeaveListReqDTO);

    /**
     * 根据用户和起止时间查询请假
     *
     * @param userId
     * @param today0
     * @param today23
     * @return
     * @author sch
     */
    UserLeave selectByUserAndTime(@Param("userId") Long userId, @Param("today0") Date today0, @Param("today23") Date today23,@Param("orgId")Long orgId);

    /**
     * 根据用户和起止时间查询周请假时长
     *
     * @param userId
     * @param firstDay
     * @param lastDay
     * @return
     * @author sch
     */
    Double selectWeekLeaveHoursByUser(@Param("userId") Long userId, @Param("firstDay") Date firstDay, @Param("lastDay") Date lastDay,@Param("orgId")Long orgId);

    /**
     * 查询10月的请假
     *
     * @param beginStr 开始
     * @param endStr   截止
     */
    List<UserLeave> selectListByTime(@Param("beginStr") String beginStr, @Param("endStr") String endStr,@Param("orgId")Long orgId);

    /**
     * 根据主键查询
     *
     * @param leaveId 请假id
     */
    UserLeave selectById(@Param("leaveId") Long leaveId);

    /**
     * 查询月请假情况
     *
     * @param yearAndMonth 年月
     * @param jobRole      角色
     * @return List<UserLeaveBO>
     * @author sch
     */
    List<UserLeaveBO> selectUserLeaveMonthStats(@Param("yearAndMonth") String yearAndMonth, @Param("jobRole") Integer jobRole,@Param("orgId")Long orgId);
}
