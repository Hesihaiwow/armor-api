package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskBO;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.bo.TaskListBO;
import com.zhixinhuixue.armor.model.dto.request.TaskListReqDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskComment;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IZSYTaskMapper {

    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入任务
     *
     * @param record
     * @return
     */
    int insert(Task record);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    Task selectByPrimaryKey(Long id);


    /**
     * 根据主键更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(Task record);


    /**
     * 查询任务详情
     *
     * @param taskId
     * @return
     */
    TaskDetailBO selectTaskDetailByTaskId(Long taskId);

    TaskDetailBO selectTaskDetailByTaskId1(@Param("taskId") Long taskId,@Param("stage")Long stage,@Param("status")Integer status);


    /**
     * 根据状态查询任务
     *
     * @param status
     * @param reviewStatus
     * @return
     */
    List<TaskBO> selectTaskByStatus(@Param("status") Integer status,
                                    @Param("reviewStatus") Integer reviewStatus,
                                    @Param("taskUserStatus") Integer taskUserStatus,
                                    @Param("userId") Long userId);


    /**
     * 查询已结束的任务，包含评价
     *
     * @return
     */
    Page<TaskBO> selectFinishedTask(Long userId);

    /**
     * 分页查询任务列表
     *
     * @param taskListReqDTO
     * @return
     */
    Page<TaskListBO> selectPage(TaskListReqDTO taskListReqDTO);

    Page<TaskListBO>  selectPage1(@Param("orgId")Long orgId);

    /**
     * 查询所有待审核任务
     * @return
     */
    List<TaskBO> selectAllWaitAudit(@Param("id")Long id,@Param("orgId")Long orgId);

    /**
     * 分页查询所有审核通过的任务
     * @param
     * @return
     */
    Page<TaskBO> selectAllAuditSuccess(@Param("id")Long id,@Param("orgId")Long orgId);

    /**
     * 查询所有未评价完的任务
     * @param userId
     * @return
     */
    List<TaskDetailBO> selectAllNotClosed(Long userId);


    /**
     * 查询项目下是否存在任务
     * @param projectId
     * @return
     */
    int findTaskByProjectId(Long projectId);

    /**
     *
     * 查询任务成员评价
     * @param taskId
     * @param taskUserId
     * @return
     */
    List<TaskComment> findTaskComment(@Param("taskId") Long taskId, @Param("taskUserId") Long taskUserId);

    /**

     * 查询所有未结束已完成的任务
     * @return
     */
    List<Task> findNotFinishedTask(@Param("orgId")Long orgId);

    /**
     * 查询阶段任务
     * @param stageId
     * @return
     */
    List<TaskListBO> selectTaskByStageId(@Param("stageId") Long stageId,@Param("departmentId") Long departmentId,
                                         @Param("userId") Long userId,@Param("orgId")Long orgId);

    /**
     * 查询阶段任务
     * @param stageId
     * @return
     */
    List<TaskListBO> selectTaskByEndStageId(@Param("stageId") Long stageId,@Param("departmentId") Long departmentId,
                                            @Param("userId") Long userId,@Param("orgId")Long orgId);


    /**
     * 查询阶段任务
     * @param stageId
     * @param departmentId
     * @param time
     * @return
     */
    List<TaskListBO> selectTaskByStageTime(@Param("stageId") Long stageId, @Param("departmentId") Long departmentId, @Param("time")Date time);

    /**
     * 查询阶段任务
     * @param stageId
     * @param departmentId
     * @param time
     * @return
     */
    List<TaskListBO> selectTaskByEndStageTime(@Param("stageId") Long stageId, @Param("departmentId") Long departmentId, @Param("time")Date time);


    /**
     * 查询阶段内任务最后一个index
     * @param stageId
     * @return
     */
    Integer selectLastIndexByStageId(@Param("stageId")Long stageId,@Param("orgId")Long orgId);

    /**
     * 修改任务下标
     * @param stageId
     * @param sort
     */
    int updateIndexByStageId(@Param("stageId") Long stageId, @Param("sort") Integer sort,@Param("orgId")Long orgId);

    /**
     * 修改评审状态
     * @param taskId
     * @return
     */
    int examineTask(Long taskId);

    /**
     * 查询未关联计划任务
     * @return
     */
    List<Task> getTaskPlanList(@Param("orgId")Long orgId);

    /**
     * 查询审核通过,进行中的任务
     * @return
     */
    List<TaskListBO> selectTaskList(@Param("orgId")Long orgId);

    /**
     * 查询审核通过,进行中的子任务
     * @return
     */
    List<TaskBO> selectSonTaskList(@Param("orgId")Long orgId);

    /**
     * 通过stageId查询我的已发布任务
     * @author sch
     * @param stageId
     * @param departmentId
     * @param userId
     * @return
     */
    List<TaskListBO> selectMyTaskByStageId(@Param("stageId") Long stageId,@Param("departmentId") Long departmentId,@Param("userId") Long userId);

    /**
     * 通过stageId查询我的任务
     * @author sch
     * @param stageId
     * @param departmentId
     * @param userId
     * @return
     */
    List<TaskListBO> selectMyTaskByEndStageId(@Param("stageId") Long stageId,@Param("departmentId") Long departmentId,@Param("userId") Long userId);

    /**
     * 查询我的未完成任务
     * @param userId
     * @return
     */
    List<Task> selectMyRunningTask(@Param("userId")Long userId);

    /**
     * 查询2020-02总任务已完成的多人任务
     * @param userId
     * @return
     */
    List<Task> selectTaskDone(@Param("userId")Long userId, @Param("date")LocalDate date);


    /**
     * 查询2020-02总任务已完成的个人任务
     * @param userId 用户
     */
    List<Task> selectPrivateTaskDone(@Param("userId")Long userId, @Param("date")LocalDate date);

    /**
     * 分页查询已评价的任务
     * @param userId
     * @return
     */
    Page<TaskDetailBO> selectCommented(@Param("userId") Long userId);

    /**
     * 获取所有待审核的任务
     * @author sch
     * @param departmentId
     * @return
     */
    Page<TaskBO> selectPendingTaskPage(@Param("departmentId")Long departmentId);

    /**
     * 所有进行中的多人任务
     * @author sch
     * @return
     */
    List<Task> selectAllTaskBase(@Param("orgId")Long orgId);

    /**
     * 根据阶段查询可用的多人任务
     * @param stageId
     * @param departmentId
     * @return
     */
    List<TaskListBO> selectTaskInfoByStageId(@Param("stageId")Long stageId, @Param("departmentId")Long departmentId);

    /**
     * 查询当前负责的进行中的任务
     * @author sch
     * @param userId 用户ID
     * @return
     */
    List<Task> selectDoingListByUser(@Param("userId") Long userId);

    /**
     * 查询7月之后的任务
     * @author sch
     */
    List<Task> selectListAfterJuly(@Param("orgId")Long orgId);

    /**
     * 所有进行中的任务
     * @author sch
     */
    List<Task> selectAllDoingTasks(@Param("orgId")Long orgId);

    /**
     * 查询全部发布任务
     * @author sch
     */
    List<Task> selectAllCompleteTasks(@Param("orgId")Long orgId);

    /**
     * 查询已经产生bug的任务
     * @author sch
     */
    List<Task> selectBugTasks(@Param("orgId")Long orgId);

    /**
     * 测试相关阶段的任务
     * @author sch
     */
    List<Task> selectTestTask(@Param("orgId")Long orgId);

    /**
     * 当前用户参与的进行中的多人任务
     * @param userId 用户id
     * @return List<Task>
     */
    List<Task> selectJoinRunningTasks(@Param("userId") Long userId);

    /**
     * 获取待发布多人任务
     * @return List<Task>
     */
    List<Task> selectWaitDeployTasks(@Param("orgId")Long orgId);

    /**
     * 获取开发和测试阶段任务
     * @return List<Task>
     */
    List<Task> selectDevAndTestTasks(@Param("orgId")Long orgId);

    /**
     * 根据id集合批量查询
     * @param list id集合
     * @return List<Task>
     */
    List<Task> selectByIds(@Param("list") List<Long> list);

    /**
     * 查询发版计划关联的任务
     * @param wppId 发版计划id
     * @return List<Task>
     */
    List<Task> selectPublishPlanTask(@Param("wppId") Long wppId,@Param("orgId")Long orgId);
}