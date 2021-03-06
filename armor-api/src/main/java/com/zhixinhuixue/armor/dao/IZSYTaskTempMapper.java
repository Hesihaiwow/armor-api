package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskReviewLogBO;
import com.zhixinhuixue.armor.model.bo.TaskTempBO;
import com.zhixinhuixue.armor.model.pojo.TaskReviewLog;
import com.zhixinhuixue.armor.model.pojo.TaskTemp;
import com.zhixinhuixue.armor.model.pojo.UserWeekTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/2 9:46
 */
public interface IZSYTaskTempMapper {

    /**
     * 根据任务id和userId查询 临时任务
     *
     * @param userId
     * @param taskId
     * @return
     */
    TaskTemp selectByUserAndTask(@Param("userId") Long userId, @Param("taskId") Long taskId);

    /**
     * 新增任务
     *
     * @param taskTemp
     * @return
     */
    int insertTaskTemp(TaskTemp taskTemp);

    /**
     * 批量插入周工作量(临时)
     *
     * @param userWeekList
     * @return
     */
    int insertUserWeekTempBatch(@Param("userWeekList") List<UserWeekTemp> userWeekList);

    /**
     * 删除原有的userWeekTemp
     *
     * @param userId
     * @param taskId
     * @return
     */
    int deleteUserWeekTempByUserAndTask(@Param("userId") Long userId, @Param("taskId") Long taskId);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    TaskTemp selectById(@Param("id") Long id);

    /**
     * 修改任务(临时)
     *
     * @param existTaskTemp
     * @return
     */
    int updateTaskTemp(TaskTemp existTaskTemp);

    /**
     * 根据userId和taskId查询周工作量
     *
     * @param userId
     * @param taskId
     * @return
     */
    List<UserWeekTemp> selectUserWeekTempByUserAndTask(@Param("userId") Long userId, @Param("taskId") Long taskId);

    /**
     * 删除周工作量
     *
     * @param idList
     * @return
     */
    int updateUserWeekTempBatch(List<Long> idList);

    /**
     * 个人分页查看任务
     *
     * @param userId
     * @param reviewStatus
     * @return
     */
    Page<TaskTempBO> selectPersonalTaskTempPage(@Param("userId") Long userId, @Param("reviewStatus") Integer reviewStatus);

    /**
     * 管理员分页查看任务
     *
     * @param reviewStatus
     * @return
     */
    Page<TaskTempBO> selectTaskTempPage(@Param("reviewStatus") Integer reviewStatus,@Param("orgId")Long orgId);

    /**
     * 审核userWeekTemp
     *
     * @param uwtIds
     * @return
     */
    int checkUserWeekTempBatch(List<Long> uwtIds);

    /**
     * 个人查看待审核任务
     *
     * @return
     */
    List<TaskTempBO> selectPersonalTaskTempList(@Param("userId") Long userId);

    /**
     * 根据主键查看任务
     *
     * @param ttId
     * @return
     */
    TaskTempBO selectTaskTempBOById(@Param("ttId") Long ttId);

    /**
     * 一级待审核临时任务
     *
     * @return
     */
    List<TaskTempBO> selectTaskTempLevelOne(@Param("userId") Long userId);

    /**
     * 二级待审核临时任务
     *
     * @param userId
     * @return
     */
    List<TaskTempBO> selectTaskTempLevelTwo(@Param("userId") Long userId);

    /**
     * 查询申请人,当前临时任务审核日志
     *
     * @param id
     * @return
     */
    List<TaskReviewLogBO> selectTaskReviewLogByTaskTemp(@Param("id") Long id);

    /**
     * 插入审核日志
     *
     * @param taskReviewLog
     * @return
     */
    int insertTaskReviewLog(TaskReviewLog taskReviewLog);

    /**
     * 根据审核人id 查询审核通过多人任务
     *
     * @param checkUserId
     * @return
     */
    Page<TaskTempBO> selectAccessedByCheckUser(@Param("checkUserId") Long checkUserId);

    /**
     * 根据临时任务id   删除审核日志
     *
     * @param id
     * @return
     */
    int deleteTaskReviewLog(@Param("id") Long id);

    /**
     * 根据任务和用户删除临时任务
     *
     * @param userId
     * @param taskId
     * @return
     * @author sch
     */
    int deleteByUserAndTask(@Param("userId") Long userId, @Param("taskId") Long taskId);

    /**
     * 三级待审核临时任务
     *
     * @param userId
     * @return
     * @author sch
     */
    List<TaskTempBO> selectTaskTempLevelThree(@Param("userId") Long userId);

    /**
     * 查询任务审核日志
     *
     * @param ttId
     * @return
     */
    TaskReviewLog selectTaskReviewLogByTaskTempAndCheckUser(@Param("ttId") Long ttId, @Param("checkUserId") Long checkUserId);

    int deleteByTask(@Param("taskId") Long taskId);

    int deleteReviewLogByTask(@Param("taskId") Long taskId);

    int deleteUserWeekTempByTask(@Param("taskId") Long taskId);

    /**
     * 查询任务的所有临时任务
     *
     * @param taskId 任务id
     * @return List<TaskTemp>
     */
    List<TaskTemp> selectByTask(@Param("taskId") Long taskId);

    /**
     * 待审核临时个人任务
     *
     * @param userId 审核人
     * @param level  审核级别
     * @return List<TaskTempBO>
     */
    List<TaskTempBO> selectPriTaskTemp(@Param("userId") Long userId, @Param("level") int level);

    /**
     * 查询审核通过个人任务
     *
     * @param checkUserId 审核人
     * @return Page<TaskTempBO>
     */
    Page<TaskTempBO> selectAccessedPriTasksByCheckUser(@Param("checkUserId") Long checkUserId);

    /**
     * 待审核个人任务
     *
     * @param userId 用户id
     * @return List<TaskTempBO>
     */
    List<TaskTempBO> selectPersonalPriTaskTempList(@Param("userId") Long userId);
}
