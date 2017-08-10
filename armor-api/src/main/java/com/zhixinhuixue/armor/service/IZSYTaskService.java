package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.TaskCompleteReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskDetailResDTO;
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
    ZSYResult<List<TaskResDTO>> getTaskByStatus(Integer status, Integer reviewStatus);
}
