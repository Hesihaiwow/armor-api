package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserIntegralHistoryPageBO;
import com.zhixinhuixue.armor.model.bo.UserTaskIntegralListBO;
import com.zhixinhuixue.armor.model.dto.request.UserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.model.pojo.UserTaskIntegral;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/9/9 9:53
 */
public interface IZSYUserTaskIntegralMapper {
    /**
     * 根据用户和任务查询积分
     * @author sch
     * @param userId 用户id
     * @param taskId 任务id
     */
    List<UserTaskIntegral> selectByUserAndTask(@Param("userId") Long userId, @Param("taskId") Long taskId);

    /**
     * 新增
     * @author sch
     * @param integral 积分
     */
    int insert(UserTaskIntegral integral);

    /**
     * 批量插入
     * @author sch
     * @param list 集合
     */
    void insertBatch(@Param("list")List<UserTaskIntegral> list);

    /**
     * 根据用户和时间查询积分
     * @author sch
     * @param userId 用户id
     * @param beginTime 开始时间
     * @param endTime 结束时间
     */
    List<UserTaskIntegral> selectListByUserAndTime(@Param("userId")Long userId,
                                                   @Param("beginTime")Date beginTime,
                                                   @Param("endTime")Date endTime);

    /**
     * 查看积分排序
     * @author sch
     * @param reqDTO 条件
     */
    List<UserTaskIntegralListBO> selectByTimeRange(@Param("reqDTO") UserTaskIntegralReqDTO reqDTO,
                                                   @Param("deptId")Long deptId);

    /**
     * 积分列数
     * @author sch
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @param deptId 部门id
     */
    int getIntegralCount(@Param("beginTime")Date beginTime,
                         @Param("endTime")Date endTime,
                         @Param("deptId")Long deptId);

    /**
     * 获取用户积分历史
     * @author sch
     * @param userId 用户id
     * @param beginTime 开始
     * @param endTime 结束
     */
    Page<UserIntegralHistoryPageBO> getIntegralHistory(@Param("userId")Long userId,
                                                       @Param("beginTime")Date beginTime,
                                                       @Param("endTime")Date endTime);
}
