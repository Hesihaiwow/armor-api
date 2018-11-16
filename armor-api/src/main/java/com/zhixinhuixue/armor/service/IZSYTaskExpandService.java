package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReviewReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskExpandResDTO;

import java.util.List;

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

    List<TaskExpandResDTO> getExpandDoing(Integer reqDTO);

    void passExpand(TaskExpandReviewReqDTO reviewReqDTO);

}
