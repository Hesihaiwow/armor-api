package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskBO;
import com.zhixinhuixue.armor.model.bo.TaskCommentBO;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.bo.TaskListBO;
import com.zhixinhuixue.armor.model.dto.request.TaskListReqDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskComment;
import org.apache.ibatis.annotations.Param;

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

    Page<TaskListBO>  selectPage1();

    /**
     * 查询所有待审核任务
     * @return
     */
    List<TaskBO> selectAllWaitAudit(Long id);

    /**
     * 分页查询所有审核通过的任务
     * @param
     * @return
     */
    Page<TaskBO> selectAllAuditSuccess(Long id);

    /**
     * 查询所有未评价完的任务
     * @param userId
     * @return
     */
    Page<TaskDetailBO> selectAllNotClosed(Long userId);


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
    List<Task> findNotFinishedTask();

    /**
     * 查询阶段任务
     * @param stageId
     * @return
     */
    List<TaskListBO> selectTaskByStageId(@Param("stageId") Long stageId,@Param("departmentId") Long departmentId);

    /**
     * 查询阶段任务
     * @param stageId
     * @return
     */
    List<TaskListBO> selectTaskByEndStageId(@Param("stageId") Long stageId,@Param("departmentId") Long departmentId);


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
    Integer selectLastIndexByStageId(Long stageId);

    /**
     * 修改任务下标
     * @param stageId
     * @param sort
     */
    int updateIndexByStageId(@Param("stageId") Long stageId, @Param("sort") Integer sort);

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
    List<Task> getTaskPlanList();

    /**
     * 查询审核通过,进行中的任务
     * @return
     */
    List<TaskListBO> selectTaskList();

    /**
     * 查询审核通过,进行中的子任务
     * @return
     */
    List<TaskBO> selectSonTaskList();

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
}