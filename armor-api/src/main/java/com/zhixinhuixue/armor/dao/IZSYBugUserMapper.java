package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.UserBugTypeBO;
import com.zhixinhuixue.armor.model.pojo.BugUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/11/7 0008.
 */
public interface IZSYBugUserMapper {

    /**
     * 批量插入bug用户处理结果
     * @param bugUsers
     * @return
     */
    int insertList(List<BugUser> bugUsers);


    int deleteById(Long id);

    /**
     * 查询时间段内bug人员
     * @param startTime 开始
     * @param endTime 截止
     */
    List<Long> selectBugUsersByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("year")Integer year, @Param("groupId")Long groupId,@Param("orgId")Long orgId);

    /**
     * 查询时间段内用户参与的bug
     * @param startTime 开始
     * @param endTime 截止
     */
    List<UserBugTypeBO> selectUserTypeNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("year")Integer year, @Param("groupId")Long groupId,@Param("orgId")Long orgId);
}
