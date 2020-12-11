package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.AddPriTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditPriTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskTempReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskBaseResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTempResDTO;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/2 9:45
 */
public interface IZSYTaskTempService {

    /**
     * 新增任务(临时)
     *
     * @param addTaskTempReqDTO 参数
     */
    void addTaskTemp(AddTaskTempReqDTO addTaskTempReqDTO);

    /**
     * 修改任务(临时)
     *
     * @param editTaskTempReqDTO 参数
     */
    void updateTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO);

    /**
     * 删除任务
     *
     * @param id 主键
     */
    void deleteTaskTemp(Long id);

    /**
     * 个人分页查看任务
     *
     * @param pageNum      页码
     * @param reviewStatus 审核状态
     */
    PageInfo<TaskTempResDTO> getPersonalTaskTempPage(Integer pageNum, Integer reviewStatus);

    /**
     * 管理员审核任务
     */
    void accessTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO);

    /**
     * 个人查看待审核任务
     */
    List<TaskResDTO> getPersonalTaskTempList();

    /**
     * 根据主键查看任务
     *
     * @param ttId 主键
     */
    TaskTempResDTO getTaskTempById(Long ttId);


    /**
     * 根据阶段查询可用的多人任务
     *
     * @param stageId 阶段id
     */
    List<TaskBaseResDTO> getMultipleTaskByStage(Long stageId);

    /**
     * 查询待审核多人任务
     *
     * @param checkUserId 审核人
     */
    List<TaskTempResDTO> getPendingTaskTempListByCheckUser(Long checkUserId);

    /**
     * 查询审核通过多人任务
     *
     * @param checkUserId 审核人
     */
    PageInfo<TaskTempResDTO> getAccessedTaskTempListByCheckUser(Integer pageNum, Long checkUserId);

    /**
     * 新增个人任务(临时)
     *
     * @param reqDTO 参数
     */
    void addPriTaskTemp(AddPriTaskTempReqDTO reqDTO);

    /**
     * 编辑个人任务(临时)
     *
     * @param reqDTO 入参
     * @param ttId   id
     */
    void editPriTaskTemp(EditPriTaskTempReqDTO reqDTO, Long ttId);

    /**
     * 审核个人任务(临时)
     *
     * @param reqDTO 参数
     * @param ttId   id
     */
    void acceptPriTaskTemp(EditPriTaskTempReqDTO reqDTO, Long ttId);

    /**
     * 查询待审核个人任务
     *
     * @param checkUserId 审核人
     * @return List<TaskTempResDTO>
     */
    List<TaskTempResDTO> getPendingPriTaskTempListByCheckUser(Long checkUserId);

    /**
     * 查询审核通过个人任务
     *
     * @param pageNum     页码
     * @param checkUserId 审核人
     * @return PageInfo<TaskTempResDTO>
     */
    PageInfo<TaskTempResDTO> getAccessedPriTaskTempListByCheckUser(Integer pageNum, Long checkUserId);

}
