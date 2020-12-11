package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReviewReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskExpandResDTO;

/**
 * Created by Lang on 2017/12/4 0004.
 */
public interface IZSYTaskExpandService {

    /**
     * 添加任务延长记录
     * @param expandReqDTO
     */
    void add(TaskExpandReqDTO expandReqDTO);

    TaskExpandResDTO getTaskExpandDetail(Long id);

    PageInfo<TaskExpandResDTO> getExpandList(TaskExpandListReqDTO reqDTO);

    PageInfo<TaskExpandResDTO> getExpandDoing(Integer status, Integer pageNum);

    void passExpand(TaskExpandReviewReqDTO reviewReqDTO);

    void deleteExpand(Long id);

    /**
     * 管理员分页查看延长申请
     * @author sch
     * @param reqDTO
     * @return
     */
    PageInfo<TaskExpandResDTO> getExpandPage(TaskExpandListReqDTO reqDTO);
}
