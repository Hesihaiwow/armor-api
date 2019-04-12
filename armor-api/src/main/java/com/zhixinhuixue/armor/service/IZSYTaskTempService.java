package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.AddTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTempResDTO;

/**
 * @author sch
 * @DATE 2019/4/2 9:45
 */
public interface IZSYTaskTempService {

    /**
     * 新增任务(临时)
     * @param addTaskTempReqDTO
     */
    void addTaskTemp(AddTaskTempReqDTO addTaskTempReqDTO);

    /**
     * 修改任务(临时)
     * @param editTaskTempReqDTO
     */
    void updateTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO);

    /**
     * 删除任务
     * @param id
     */
    void deleteTaskTemp(Long id);

    /**
     * 个人分页查看任务
     * @param pageNum
     * @param reviewStatus
     * @return
     */
    PageInfo<TaskTempResDTO> getPersonalTaskTempPage(Integer pageNum, Integer reviewStatus);

    /**
     * 管理员分页查看任务
     * @param pageNum
     * @param reviewStatus
     * @return
     */
    PageInfo<TaskTempResDTO> getTaskTempPage(Integer pageNum, Integer reviewStatus);

    /**
     * 管理员审核任务
     * @param
     */
    void accessTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO);
}
