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

    UserLeaveBO getUserLeaveById(Long id);

    Page<UserLeaveBO> getLeaveByReviewStatus(@Param("userId")Long userId, @Param("reviewStatus")int reviewStatus,@Param("departmentId")Long departmentId);

    int updateLeave(UserLeave userLeave);

    int deleteById(Long id);

    Page<UserLeaveBO> getLeaveList(UserLeaveListReqDTO userLeaveListReqDTO);

    /**
     * 根据用户和起止时间查询请假
     * @author sch
     * @param userId
     * @param today0
     * @param today23
     * @return
     */
    UserLeave selectByUserAndTime(@Param("userId")Long userId, @Param("today0")Date today0, @Param("today23")Date today23);

    /**
     * 根据用户和起止时间查询周请假时长
     * @author sch
     * @param userId
     * @param firstDay
     * @param lastDay
     * @return
     */
    Double selectWeekLeaveHoursByUser(@Param("userId")Long userId, @Param("firstDay")Date firstDay, @Param("lastDay")Date lastDay);
}
