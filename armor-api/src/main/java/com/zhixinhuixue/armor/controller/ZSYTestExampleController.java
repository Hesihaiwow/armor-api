package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.service.IZSYTestExampleService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author sch
 * @DATE 2019/8/6 9:40
 */
@RestController
@RequestMapping("/api/test-example")
@Api(value = "测试用例相关接口", description = "任务管理系统测试用例相关接口", tags = "/test-example")
public class ZSYTestExampleController {

    @Autowired
    private IZSYTestExampleService exampleService;

    @ApiOperation("新增")
    @PostMapping("/add")
    public String add(@Valid @RequestBody AddTestExampleReqDTO reqDTO){
        exampleService.add(reqDTO);
        return ZSYResult.success().build();
    }
}
