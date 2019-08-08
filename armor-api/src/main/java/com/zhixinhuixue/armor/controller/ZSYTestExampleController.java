package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestExampleReqDTO;
import com.zhixinhuixue.armor.service.IZSYTestExampleService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @ApiOperation("查看树")
    @GetMapping("/tree/{taskId}")
    public String getTree(@PathVariable("taskId")Long taskId){
        return ZSYResult.success().data(exampleService.getTree(taskId)).build();
    }

    @ApiOperation("查看测试用例详情")
    @GetMapping("/detail/{exampleId}")
    public String getExampleDetail(@PathVariable("exampleId")Long exampleId){
        return ZSYResult.success().data(exampleService.getExampleDetail(exampleId)).build();
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete/{exampleId}")
    public String deleteExample(@PathVariable("exampleId")Long exampleId){
        exampleService.deleteExample(exampleId);
        return ZSYResult.success().build();
    }

    @ApiOperation("编辑")
    @PostMapping("/edit")
    public String editExample(@Valid @RequestBody EditTestExampleReqDTO reqDTO){
        exampleService.editExample(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("导入")
    @PostMapping(value = "/import/{taskId}",produces = "application/json; charset=utf-8")
    public String importExample(@PathVariable("taskId")Long taskId,@RequestParam(value = "uploadFile") MultipartFile uploadFile){
        exampleService.importExample(uploadFile,taskId);
        return ZSYResult.success().build();
    }

    @ApiOperation("导出")
    @GetMapping("/export/{taskId}")
    public String exportExample(@PathVariable("taskId")Long taskId){
        return ZSYResult.success().data(exampleService.exportExample(taskId)).build();
    }
}
