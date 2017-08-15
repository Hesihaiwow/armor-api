package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskBO;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.bo.TaskListBO;
import com.zhixinhuixue.armor.model.dto.request.TaskListReqDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import org.apache.ibatis.annotations.Param;

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


    /**
     * 根据状态查询任务
     *
     * @param status
     * @param reviewStatus
     * @return
     */
    List<TaskBO> selectTaskByStatus(@Param("status") Integer status,
                                    @Param("reviewStatus") Integer reviewStatus,
                                    @Param("userId") Long userId);

    /**
     * 查询已结束的任务，包含评价
     *
     * @return
     */
    List<TaskBO> selectFinishedTask(Long userId);

    /**
     * 分页查询任务列表
     *
     * @param taskListReqDTO
     * @return
     */
    Page<TaskListBO> selectPage(TaskListReqDTO taskListReqDTO);

}