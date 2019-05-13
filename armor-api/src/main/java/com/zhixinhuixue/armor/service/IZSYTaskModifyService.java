package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.AddTaskModifyReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskModifyReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskModifyDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskModifyListResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskUserWeekResDTO;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/9 10:33
 */
public interface IZSYTaskModifyService {

    /**
     * 新增任务修改申请
     * @author sch
     * @param addTaskModifyReqDTO
     */
    void add(AddTaskModifyReqDTO addTaskModifyReqDTO);

    /**
     * 删除申请
     * @author sch
     * @param id
     */
    void deleteById(Long id);

    /**
     * 个人查看待审核任务修改申请
     * @author sch
     * @return
     */
    List<TaskModifyListResDTO> getPersonalTaskModifyList();

    /**
     * 个人分页查看审核通过的任务修改申请
     * @author sch
     * @param pageNum
     * @return
     */
    PageInfo<TaskModifyListResDTO> getPersonalTaskModifyPage(Integer pageNum);

    /**
     * 管理员查看待审核任务修改申请
     * @author sch
     * @return
     */
    List<TaskModifyListResDTO> getTaskModifyList();

    /**
     * 管理员分页查看审核通过任务修改申请
     * @author sch
     * @param pageNum
     * @return
     */
    PageInfo<TaskModifyListResDTO> getTaskModifyPage(Integer pageNum);

    /**
     * 审核任务修改申请
     * @author sch
     * @param editTaskModifyReqDTO
     */
    void reviewTaskModify(EditTaskModifyReqDTO editTaskModifyReqDTO);

    /**
     * 查看申请详情
     * @author sch
     * @param id
     * @return
     */
    TaskModifyDetailResDTO getTaskModifyDetail(Long id);

    /**
     * 根据任务和用户查询周工时分配
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    TaskUserWeekResDTO getBeforeTaskUserWeek(Long taskId, Long userId);

    /**
     * 修改申请
     * @author sch
     * @param editTaskModifyReqDTO
     */
    void updateModify(EditTaskModifyReqDTO editTaskModifyReqDTO);
}
