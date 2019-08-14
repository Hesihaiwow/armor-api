package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.response.TaskFunctionListResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTempFunctionResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserAndLevelResDTO;

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

    /**
     * 根据功能点查询相关人员等级
     * @param functionId
     * @return
     */
    List<UserAndLevelResDTO> getUserAndLevel(Long functionId);
}
