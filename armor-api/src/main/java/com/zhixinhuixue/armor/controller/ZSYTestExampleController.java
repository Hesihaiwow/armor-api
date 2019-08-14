package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddTestFunctionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestFunctionReqDTO;
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

    @ApiOperation("新增功能点")
    @PostMapping("/function/add")
    public String addFunction(@Valid @RequestBody AddTestFunctionReqDTO reqDTO){
        exampleService.addFunction(reqDTO);
        return ZSYResult.success().build();
    }

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
    @PutMapping("/edit")
    public String editExample(@Valid @RequestBody EditTestExampleReqDTO reqDTO){
        exampleService.editExample(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改审批状态")
    @PutMapping("/exam-status/{exampleId}/{examStatus}")
    public String editExamStatus(@PathVariable("exampleId")Long exampleId,@PathVariable("examStatus")Integer examStatus){
        exampleService.editExamStatus(exampleId,examStatus);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改状态")
    @PutMapping("/status/{exampleId}/{status}")
    public String editStatus(@PathVariable("exampleId")Long exampleId,@PathVariable("status")Integer status){
        exampleService.editStatus(exampleId,status);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改功能点名称")
    @PutMapping("/function/edit")
    public String editFunction(@Valid @RequestBody EditTestFunctionReqDTO reqDTO){
        exampleService.editFunction(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("删除功能点")
    @DeleteMapping("/function/delete/{functionId}")
    public String deleteFunction(@PathVariable("functionId")Long functionId){
        exampleService.deleteFunction(functionId);
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
