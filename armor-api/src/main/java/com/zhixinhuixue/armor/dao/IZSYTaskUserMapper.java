package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.PersonTaskBO;
import com.zhixinhuixue.armor.model.bo.TaskAndHourBO;
import com.zhixinhuixue.armor.model.bo.TaskUserHoursBO;
import com.zhixinhuixue.armor.model.dto.request.PersonalTaskListReqDTO;
import com.zhixinhuixue.armor.model.pojo.TaskUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.Date;
import java.util.List;

public interface IZSYTaskUserMapper {


    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入任务用户
     * @param record
     * @return
     */
    int insert(TaskUser record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    TaskUser selectByPrimaryKey(Long id);

    /**
     * 根据主键修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TaskUser record);

    /**
     * 批量插入taskUser
     * @param taskUserList
     * @return
     */
    int insertList(List<TaskUser> taskUserList);

    /**
     * 删除任务
     * @param taskId
     * @return
     */
    int deleteByTaskId(Long taskId);


    /**
     * 个人任务统计
     * @param personalTaskListReqDTO
     * @return
     */
    List<PersonTaskBO> getPersonalList(PersonalTaskListReqDTO personalTaskListReqDTO);


    /** sch --
     * 查询任务开发人员
     * @param id
     * @return
     */
    List<Long> selectUserByTaskId(@Param("id") Long id);

    /**
     * 删除taskUser
     * @param taskId
     * @param userId
     * @return
     */
    int deleteByTaskIdAndUserId(@Param("taskId")Long taskId, @Param("userId")Long userId);

    /**
     * 根据任务id和用户id查询taskUser
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    TaskUser selectByTaskAndUser(@Param("taskId")Long taskId, @Param("userId")Long userId);

    /**
     * 根据用户和时间查询已完成
     * @param userId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<TaskUser> selectByUserAndTime(@Param("userId")Long userId, @Param("beginTime")Date beginTime,@Param("endTime") Date endTime);

    /**
     * 查询2019-7 之后的没有功能点的多人任务
     * @author sch
     */
    List<TaskUserHoursBO> selectWithoutFunctionMultiTask(@Param("orgId")Long orgId);

    /**
     * 查询2019-7月之后的 没有任务级别的个人任务
     * @author sch
     */
    List<TaskUserHoursBO> selectWithoutTaskLevel(@Param("orgId")Long orgId);

    /**
     * 查询2019-7 之后的有功能点的而且已经结束的多人任务
     * @author sch
     */
    List<TaskUserHoursBO> selectWithFunctionMultiTask(@Param("orgId")Long orgId);

    /**
     * 查询2019-7月之后的 有任务级别的而且已经完成的个人任务
     * @author sch
     */
    List<TaskUserHoursBO> selectWithTaskLevel(@Param("orgId")Long orgId);

    /**
     * 统计7月之后的有功能点的且已评价多人任务积分(已完成,没结束)
     * @author sch
     */
    List<TaskUserHoursBO> selectWithEvaluationMultiTask(@Param("orgId")Long orgId);

    /**
     * 统计7月之后的没有功能点的且已评价多人任务积分(已完成,没结束)
     * @author sch
     */
    List<TaskUserHoursBO> selectWithoutEvaluationMultiTask(@Param("orgId")Long orgId);

    /**
     * 查询7月后没有任务级别的个人任务
     * @author sch
     */
    List<TaskUser> selectPrivateAfterJuly(@Param("orgId")Long orgId);

    /**
     * 查询7月后没有任务级别的多人任务
     * @author sch
     */
    List<TaskUser> selectMultiAfterJuly(@Param("orgId")Long orgId);

    /**
     * 查询7月之后已完成的个人任务
     * @author sch
     */
    List<TaskUser> selectPrivateAfterJulyWithLevel(@Param("orgId")Long orgId);

    /**
     * 查询7月之后已结束的多人任务
     * @author sch
     */
    List<TaskUser> selectMultiAfterJulyWithLevel(@Param("status") Integer status,@Param("orgId")Long orgId);

    /**
     * 更新状态为已结束
     * @param taskId
     */
    void updateByTaskId(@Param("taskId") Long taskId);

    /**
     * 已当前任务关联的用户的个人任务时间
     * @param taskId 任务id
     * @param userId 用户id
     */
    List<TaskAndHourBO> selectUserPrivateTaskHours(@Param("taskId") Long taskId, @Param("userId") Long userId);

    // -- sch
}