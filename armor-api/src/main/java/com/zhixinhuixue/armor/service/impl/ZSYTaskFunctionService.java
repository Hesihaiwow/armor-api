package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYTaskFunctionMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskTempFunctionMapper;
import com.zhixinhuixue.armor.model.bo.TaskFunctionBO;
import com.zhixinhuixue.armor.model.bo.UserAndLevelBO;
import com.zhixinhuixue.armor.model.dto.response.TaskFunctionListResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserAndLevelResDTO;
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
    @Autowired
    private IZSYTaskTempFunctionMapper tempFunctionMapper;


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

    /**
     * 根据功能点查询相关人员等级
     * @param functionId
     * @return
     */
    @Override
    public List<UserAndLevelResDTO> getUserAndLevel(Long functionId) {
        List<UserAndLevelBO> userAndLevelBOS = tempFunctionMapper.selectUserAndLevelByFunction(functionId);
        List<UserAndLevelResDTO> userAndLevelResDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userAndLevelBOS)){
            userAndLevelBOS.forEach(userAndLevelBO -> {
                UserAndLevelResDTO userAndLevelResDTO = new UserAndLevelResDTO();
                BeanUtils.copyProperties(userAndLevelBO,userAndLevelResDTO);
                Integer level2 = userAndLevelBO.getLevel();
                if (level2 == 1){
                    userAndLevelResDTO.setLevelName("一级");
                }else if(level2 == 2){
                    userAndLevelResDTO.setLevelName("二级");
                }else if (level2 == 3){
                    userAndLevelResDTO.setLevelName("三级");
                }else if (level2 == 4){
                    userAndLevelResDTO.setLevelName("四级");
                }else if (level2 == 5){
                    userAndLevelResDTO.setLevelName("五级");
                }
                userAndLevelResDTOS.add(userAndLevelResDTO);
            });
        }
        return userAndLevelResDTOS;
    }
}
