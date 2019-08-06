package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTaskFunctionMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYTestExampleMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExampleDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTreeResDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskFunction;
import com.zhixinhuixue.armor.model.pojo.TestExample;
import com.zhixinhuixue.armor.service.IZSYTestExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/8/6 9:42
 */
@Service
public class ZSYTestExampleService implements IZSYTestExampleService {
    @Autowired
    private IZSYTestExampleMapper exampleMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskFunctionMapper functionMapper;


    /**
     * 新增
     * @param reqDTO
     */
    @Override
    public void add(AddTestExampleReqDTO reqDTO) {
        TestExample example = exampleMapper.selectByNameAndTaskAndFunction(reqDTO.getName(),reqDTO.getTaskId(),reqDTO.getFunctionId());
        if (example != null){
            throw new ZSYServiceException("当前测试用例已存在");
        }
        TestExample testExample = new TestExample();
        testExample.setId(snowFlakeIDHelper.nextId());
        testExample.setName(reqDTO.getName().trim());
        testExample.setTaskId(reqDTO.getTaskId());
        testExample.setFunctionId(reqDTO.getFunctionId());
        testExample.setCheckPoint(reqDTO.getCheckPoint().trim());
        testExample.setExpectResult(reqDTO.getExpectResult().trim());
        if (reqDTO.getRemark() != null){
            testExample.setRemark(reqDTO.getRemark().trim());
        }
        testExample.setType(reqDTO.getType());
        testExample.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        testExample.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        testExample.setCreateName(ZSYTokenRequestContext.get().getUserName());
        testExample.setUpdateName(ZSYTokenRequestContext.get().getUserName());
        testExample.setCreateTime(new Date());
        testExample.setUpdateTime(new Date());

        if (exampleMapper.insert(testExample) == 0){
            throw new ZSYServiceException("新增测试用例失败");
        }
    }

    /**
     * 查看树
     * @param taskId
     * @return
     */
    @Override
    public TaskTreeResDTO getTree(Long taskId) {
        TaskTreeResDTO taskTreeResDTO = new TaskTreeResDTO();
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task != null){
            taskTreeResDTO.setTaskId(taskId);
            taskTreeResDTO.setTaskName(task.getName());
            List<TaskFunction> functions = functionMapper.selectListByTaskId(taskId);
            List<TaskTreeResDTO.TaskFunctionTreeResDTO> functionTreeResDTOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(functions)){
                functions.forEach(function->{
                    TaskTreeResDTO.TaskFunctionTreeResDTO taskFunctionTreeResDTO = new TaskTreeResDTO.TaskFunctionTreeResDTO();
                    taskFunctionTreeResDTO.setFunctionId(function.getId());
                    taskFunctionTreeResDTO.setFunction(function.getFunction());
                    List<TaskTreeResDTO.TaskFunctionTreeResDTO.TestExampleTreeResDTO> testExampleTreeResDTOS = new ArrayList<>();
                    List<TestExample> examples = exampleMapper.selectByFunction(function.getId());
                    if (!CollectionUtils.isEmpty(examples)){
                        examples.forEach(example->{
                            TaskTreeResDTO.TaskFunctionTreeResDTO.TestExampleTreeResDTO resDTO = new TaskTreeResDTO.TaskFunctionTreeResDTO.TestExampleTreeResDTO();
                            resDTO.setId(example.getId());
                            resDTO.setName(example.getName());
                            testExampleTreeResDTOS.add(resDTO);
                        });
                    }
                    taskFunctionTreeResDTO.setExampleTreeResDTOS(testExampleTreeResDTOS);
                    functionTreeResDTOS.add(taskFunctionTreeResDTO);
                });
            }
            taskTreeResDTO.setFunctionTreeResDTOS(functionTreeResDTOS);
        }
        return taskTreeResDTO;
    }

    /**
     * 查看测试用例详情
     * @param exampleId
     * @return
     */
    @Override
    public ExampleDetailResDTO getExampleDetail(Long exampleId) {

        return null;
    }
}
