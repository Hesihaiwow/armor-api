package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.source.ZSYResult;

/**
 * Created by Tate on 2017/8/7.
 */
public interface ITaskService {

    /**
     * 添加任务
     *
     * @param taskReqDTO
     * @param loginUserId
     * @return
     */
    ZSYResult addTask(TaskReqDTO taskReqDTO);
}
