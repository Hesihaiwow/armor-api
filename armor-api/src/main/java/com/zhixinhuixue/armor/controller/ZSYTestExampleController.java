package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddTestFunctionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestFunctionReqDTO;
import com.zhixinhuixue.armor.service.IZSYTestExampleService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 测试用例相关接口
 *
 * @author sch
 * @DATE 2019/8/6 9:40
 */
@RestController
@RequestMapping("/api/test-example")
public class ZSYTestExampleController {

    @Resource
    private IZSYTestExampleService exampleService;

    /**
     * 新增功能点
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/function/add")
    public String addFunction(@Valid @RequestBody AddTestFunctionReqDTO reqDTO) {
        exampleService.addFunction(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 新增
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public String add(@Valid @RequestBody AddTestExampleReqDTO reqDTO) {
        exampleService.add(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 查看树
     *
     * @param taskId
     * @return
     */
    @GetMapping("/tree/{taskId}")
    public String getTree(@PathVariable("taskId") Long taskId) {
        return ZSYResult.success().data(exampleService.getTree(taskId)).build();
    }

    /**
     * 查看测试用例详情
     *
     * @param exampleId
     * @return
     */
    @GetMapping("/detail/{exampleId}")
    public String getExampleDetail(@PathVariable("exampleId") Long exampleId) {
        return ZSYResult.success().data(exampleService.getExampleDetail(exampleId)).build();
    }

    /**
     * 删除
     *
     * @param exampleId
     * @return
     */
    @DeleteMapping("/delete/{exampleId}")
    public String deleteExample(@PathVariable("exampleId") Long exampleId) {
        exampleService.deleteExample(exampleId);
        return ZSYResult.success().build();
    }

    /**
     * 编辑
     *
     * @param reqDTO
     * @return
     */
    @PutMapping("/edit")
    public String editExample(@Valid @RequestBody EditTestExampleReqDTO reqDTO) {
        exampleService.editExample(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改审批状态
     *
     * @param exampleId
     * @param examStatus
     * @return
     */
    @PutMapping("/exam-status/{exampleId}/{examStatus}")
    public String editExamStatus(@PathVariable("exampleId") Long exampleId, @PathVariable("examStatus") Integer examStatus) {
        exampleService.editExamStatus(exampleId, examStatus);
        return ZSYResult.success().build();
    }

    /**
     * 修改状态
     *
     * @param exampleId
     * @param status
     * @return
     */
    @PutMapping("/status/{exampleId}/{status}")
    public String editStatus(@PathVariable("exampleId") Long exampleId, @PathVariable("status") Integer status) {
        exampleService.editStatus(exampleId, status);
        return ZSYResult.success().build();
    }

    /**
     * 修改功能点名称
     *
     * @param reqDTO
     * @return
     */
    @PutMapping("/function/edit")
    public String editFunction(@Valid @RequestBody EditTestFunctionReqDTO reqDTO) {
        exampleService.editFunction(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除功能点
     *
     * @param functionId
     * @return
     */
    @DeleteMapping("/function/delete/{functionId}")
    public String deleteFunction(@PathVariable("functionId") Long functionId) {
        exampleService.deleteFunction(functionId);
        return ZSYResult.success().build();
    }

    /**
     * 导入
     *
     * @param taskId
     * @param uploadFile
     * @return
     */
    @PostMapping(value = "/import/{taskId}", produces = "application/json; charset=utf-8")
    public String importExample(@PathVariable("taskId") Long taskId, @RequestParam(value = "uploadFile") MultipartFile uploadFile) {
        exampleService.importExample(uploadFile, taskId);
        return ZSYResult.success().build();
    }

    /**
     * 导出
     *
     * @param taskId
     * @return
     */
    @GetMapping("/export/{taskId}")
    public String exportExample(@PathVariable("taskId") Long taskId) {
        return ZSYResult.success().data(exampleService.exportExample(taskId)).build();
    }
}
