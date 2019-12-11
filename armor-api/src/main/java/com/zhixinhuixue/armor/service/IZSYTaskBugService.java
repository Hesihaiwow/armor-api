package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.AddTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.QueryTaskBugPageReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author sch
 * @time 2019/10/14 10:22
 */
public interface IZSYTaskBugService {

    /**
     * 分页查看任务bug
     * @param reqDTO 条件
     */
    PageInfo<TaskBugPageResDTO> getTaskBugPage(QueryTaskBugPageReqDTO reqDTO);

    /**
     * 新增
     * @param reqDTO 条件
     */
    void addTaskBug(AddTaskBugReqDTO reqDTO);

    /**
     * 修改
     * @param reqDTO 条件
     */
    void updateTaskBug(EditTaskBugReqDTO reqDTO);

    /**
     * 删除
     * @param tbId  bugId
     */
    void deleteTaskBug(Long tbId);

    /**
     * 查看详情
     * @param tbId bugId
     */
    TaskBugDetailResDTO getTaskBugDetail(Long tbId);

    /**
     * 查询任务人员
     * @param taskId 任务id
     */
    List<EffectUserResDTO> getTaskUsers(Long taskId);

    /**
     * 查询任务数量
     * @param reqDTO 条件
     */
    TaskBugNumResDTO getTaskBugNum(QueryTaskBugPageReqDTO reqDTO);

    /**
     * 个人主页显示bug分页
     * @param pageNum 页码
     */
    PageInfo<TaskBugPageResDTO> getPersonalBugPage(Integer pageNum);

    /**
     * 查询已经产生bug的任务
     * @author sch
     */
    List<TaskBaseResDTO> getReadyTasks();

    /**
     * 我的bug视图
     * @author sch
     */
    MyBugResDTO getMyBugList();

    /**
     * 测试相关阶段的任务
     * @author sch
     */
    List<TaskBaseResDTO> getTaskTesting();

    /**
     * 查询bug报告员
     * @author sch
     */
    List<EffectUserResDTO> getBugReporters();

    /**
     * 查询bug分派员
     * @author sch
     */
    List<EffectUserResDTO> getBugHandlers();
}
