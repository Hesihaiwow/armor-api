package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.response.TaskFunctionListResDTO;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/7/29 10:44
 */
public interface IZSYTaskFunctionService {

    /**
     * 根据任务查询功能点列表
     * @param taskId
     * @return
     */
    List<TaskFunctionListResDTO> getFunctionListByTask(Long taskId);
}
