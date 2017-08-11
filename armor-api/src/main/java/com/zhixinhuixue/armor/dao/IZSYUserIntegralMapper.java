package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserIntegralInfoBO;
import com.zhixinhuixue.armor.model.pojo.UserIntegral;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface IZSYUserIntegralMapper {

    /**
     * 积分列表
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page<UserIntegralInfoBO> getIntegralPage(@Param("startTime") String startTime, @Param("endTime") String endTime);

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
    int getRank(@Param("startTime")String startTime, @Param("endTime") String endTime,@Param("userId")Long id);
}