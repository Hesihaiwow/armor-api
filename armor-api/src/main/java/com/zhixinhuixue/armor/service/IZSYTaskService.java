package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.source.ZSYResult;

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
     * @param taskId
     * @return
     */
    ZSYResult auditTask(Long taskId, Integer auditStatus);
}
