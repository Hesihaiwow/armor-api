package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserIntegralHistoryBO;
import com.zhixinhuixue.armor.model.bo.UserIntegralInfoBO;
import com.zhixinhuixue.armor.model.pojo.UserIntegral;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IZSYUserIntegralMapper {

    /**
     * 积分列表
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<UserIntegralInfoBO> getIntegralPage(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 查询用户积分
     * @param startTime
     * @param endTime
     * @param id
     * @return
     */
    int getUserIntegral(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("id") Long id);

    /**
     * 插入用户积分
     *
     * @param userIntegral
     * @return
     */
    int insert(UserIntegral userIntegral);

    /**
     * 查询用户排名
     * @param startTime
     * @param endTime
     * @param id
     * @return
     */
    Integer getRank(@Param("startTime")String startTime, @Param("endTime") String endTime,@Param("userId")Long id);


    /**
     * 用户积分历史
     * @param id
     * @param startTime
     * @param endTime
     * @return
     */
    Page<UserIntegralHistoryBO> getIntegralHistory(@Param("id") Long id, @Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 删除用户任务积分
     * @param taskId
     * @param userId
     * @return
     */
    int deleteUserIntegral(@Param("taskId") Long taskId, @Param("userId")Long userId);

}