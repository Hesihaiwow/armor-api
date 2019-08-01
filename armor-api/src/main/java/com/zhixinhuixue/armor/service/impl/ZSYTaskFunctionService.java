package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYTaskFunctionMapper;
import com.zhixinhuixue.armor.model.bo.TaskFunctionBO;
import com.zhixinhuixue.armor.model.dto.response.TaskFunctionListResDTO;
import com.zhixinhuixue.armor.model.pojo.TaskFunction;
import com.zhixinhuixue.armor.service.IZSYTaskFunctionService;
import com.zhixinhuixue.armor.source.enums.FunctionAction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/7/31 17:36
 */
@Service
public class ZSYTaskFunctionService implements IZSYTaskFunctionService {
    @Autowired
    private IZSYTaskFunctionMapper functionMapper;

    @Override
    public List<TaskFunctionListResDTO> getFunctionListByTask(Long taskId) {
        List<TaskFunctionBO> functions = functionMapper.selectBOListByTaskId(taskId);
        List<TaskFunctionListResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(functions)){
            functions.forEach(function->{
                TaskFunctionListResDTO resDTO = new TaskFunctionListResDTO();
                BeanUtils.copyProperties(function,resDTO);
                String actionName = FunctionAction.getName(function.getAction());
                String functionStr = "【"+actionName+"】"+function.getFunction()+"【"+function.getModuleName()+"】";
                resDTO.setFunctionStr(functionStr);
                list.add(resDTO);
            });
        }
        return list;
    }
}
