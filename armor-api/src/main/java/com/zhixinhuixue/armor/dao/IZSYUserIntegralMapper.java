package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskIntegralBO;
import com.zhixinhuixue.armor.model.bo.UserIntegralHistoryBO;
import com.zhixinhuixue.armor.model.bo.UserIntegralInfoBO;
import com.zhixinhuixue.armor.model.pojo.UserIntegral;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IZSYUserIntegralMapper {

    /**
     * 积分列表
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<UserIntegralInfoBO> getIntegralPage(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("departmentId")Long departmentId);

    /**
     * 积分奖金计算
     * @param startTime
     * @param endTime
     * @param integral
     * @param departmentId
     * @return
     */
    List<UserIntegralInfoBO> getCalculate(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("integral") BigDecimal integral, @Param("departmentId")Long departmentId);

    /**
     * 积分列数
     * @param startTime
     * @param endTime
     * @return
     */
    int getIntegralCount(@Param("startTime") String startTime, @Param("endTime") String endTime,@Param("departmentId") Long departmentId);

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
    Integer getRank(@Param("startTime")String startTime, @Param("endTime") String endTime,@Param("userId")Long id, @Param("departmentId")Long departmentId);


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

    /**
     * 根据审核状态查询我的转移积分
     * @param userId
     * @param status
     * @return
     */
    Page<UserIntegralInfoBO> getIntegralByReviewStatus(@Param("userId") Long userId, @Param("status")int status, @Param("departmentId")Long departmentId);

    /**
     * 根据审核状态查询所有转移积分
     * @param status
     * @return
     */
    Page<UserIntegralInfoBO> getAllIntegralByReviewStatus(int status);

    /**
     *  获取积分求助转移详情
     * @param id
     * @return
     */
    UserIntegral getHelpDetail(Long id);

    /**
     * 删除积分求助转移
     * @param id
     * @return
     */
    int deleteIntegralById(Long id);

    /**
     * 编辑积分求助转移
     * @param userIntegral
     * @return
     */
    int updateIntegralById(UserIntegral userIntegral);

    /**
     * 审核积分通过
     * @param id
     * @return
     */
    int updateReview(Long id);


    /**
     * 根据任务Id查询积分
     * @param id
     * @return
     */
    List<UserIntegral> getUserIntegralByTaskId(@Param("id") Long id);

    /**
     * 查询用户任务功能点结合,评价
     * @param userId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<TaskIntegralBO> selectTaskIntegralByUser(@Param("userId")Long userId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
}