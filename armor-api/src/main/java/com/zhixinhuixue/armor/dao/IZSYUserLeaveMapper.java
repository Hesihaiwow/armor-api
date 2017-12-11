package com.zhixinhuixue.armor.dao;


import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserLeaveBO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveListReqDTO;
import com.zhixinhuixue.armor.model.pojo.UserLeave;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public interface IZSYUserLeaveMapper {

    int insertLeave(UserLeave userLeave);

    UserLeaveBO getUserLeaveById(Long id);

    Page<UserLeaveBO> getLeaveByReviewStatus(@Param("userId")Long userId, @Param("reviewStatus")int reviewStatus);

    int updateLeave(UserLeave userLeave);

    int deleteById(Long id);

    Page<UserLeaveBO> getLeaveList(UserLeaveListReqDTO userLeaveListReqDTO);

}
