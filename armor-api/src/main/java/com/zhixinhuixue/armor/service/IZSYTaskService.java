package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.CommentReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskCompleteReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskListResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskLogDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskResDTO;
import com.zhixinhuixue.armor.source.ZSYResult;

import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
public interface IZSYTaskService {

    /**
     * 添加任务
     *
     * @param taskReqDTO
     * @return
     */
    ZSYResult addTask(TaskReqDTO taskReqDTO);

    /***
     * 修改任务
     * @param taskId
     * @param taskReqDTO
     * @return
     */
    ZSYResult modifyTask(Long taskId, TaskReqDTO taskReqDTO);

    /**
     * 审核任务
     *
     * @param taskId
     * @return
     */
    ZSYResult auditTask(Long taskId, Integer auditStatus);

    /**
     * 完成个人任务
     *
     * @param taskCompleteReqDTO
     * @return
     */
    ZSYResult completePrivateTask(TaskCompleteReqDTO taskCompleteReqDTO);

    /**
     * 完成多人任务中我的任务
     *
     * @param taskCompleteReqDTO
     * @return
     */
    ZSYResult completePublicTask(TaskCompleteReqDTO taskCompleteReqDTO);

    /**
     * 获取任务详情
     *
     * @param taskId
     * @return
     */
    ZSYResult<TaskDetailResDTO> getTaskDetail(Long taskId);

    /**
     * 按状态查询任务
     *
     * @return
     */
    ZSYResult<List<TaskResDTO>> getTaskByStatus(Integer status, Integer reviewStatus, Integer taskUserStatus, Long userId);

    /**
     * 获取已完成&已评价的任务，包含积分
     *
     * @return
     */
    ZSYResult<List<TaskResDTO>> getFinishedTask();

    /**
     * 完成主任务
     *
     * @param taskId
     * @return
     */
    ZSYResult completeMasterTask(Long taskId);


    /**
     * 添加评价
     *
     * @param commentReqDTO
     * @return
     */
    ZSYResult addComment(CommentReqDTO commentReqDTO);

    /**
     * 主任务完成，结算积分
     */
    void finishTask(Long taskId);

    /**
     * 分页查询任务
     *
     * @param taskListReqDTO
     * @return
     */
    PageInfo<TaskListResDTO> getTaskListPage(TaskListReqDTO taskListReqDTO);

    /**
     * 获取所有待审核状态的任务
     *
     * @return
     */
    ZSYResult<List<TaskResDTO>> getAllWaitAudit();

    /**
     * 获取所有待评价的任务
     *
     * @return
     */
    ZSYResult<List<TaskResDTO>> getAllWaitComment(Long userId);

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    ZSYResult deleteTaskById(Long taskId);

    /**
     * 获取任务日志
     *
     * @param taskId
     * @return
     */
    PageInfo<TaskLogDTO> getTaskLog(Long taskId, int pageNum);
}
