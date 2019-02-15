package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.AddExtraWorkReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkResDTO;
import com.zhixinhuixue.armor.model.pojo.Task;

import java.util.List;

/**
 * @author SCH
 * @date 2019/2/14 17:19
 */
public interface IZSYExtraWorkService {

    /**
     * 新增加班申请
     * @param reqDTO
     */
    void addExtraWork(AddExtraWorkReqDTO reqDTO);

    /**
     * 修改加班申请
     * @param reqDTO
     * @param ewId
     */
    void updateExtraWork(AddExtraWorkReqDTO reqDTO, Long ewId);

    /**
     * 删除加班申请
     * @param ewId
     */
    void deleteExtraWork(Long ewId);

    /**
     * 审核通过
     * @param ewId
     */
    void checkExtraWork(Long ewId);

    /**
     * 查询我的未完成任务
     * @return
     */
    List<Task> getMyRunningTaskList();

    /**
     * 查询我的加班申请(待审核)
     * @param pageNum
     * @return
     */
    PageInfo<ExtraWorkResDTO> getWaitExtraWorkByPage(Integer pageNum);

    /**
     * 查询我的加班申请(审核通过)
     * @param pageNum
     * @return
     */
    PageInfo<ExtraWorkResDTO> getAccessExtraWorkByPage(Integer pageNum);

    /**
     * 查询审核中的加班申请
     * @param pageNum
     * @return
     */
    PageInfo<ExtraWorkResDTO> getCheckingExtraWorkByPage(Integer pageNum);

    /**
     * 查询审核通过的加班申请
     * @param pageNum
     * @return
     */
    PageInfo<ExtraWorkResDTO> getCheckedExtraWorkByPage(Integer pageNum);

}
