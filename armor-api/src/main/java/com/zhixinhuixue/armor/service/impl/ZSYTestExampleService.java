package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTestExampleMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.pojo.TestExample;
import com.zhixinhuixue.armor.service.IZSYTestExampleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
}
