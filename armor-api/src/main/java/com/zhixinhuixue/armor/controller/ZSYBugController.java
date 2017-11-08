package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugReqDTO;
import com.zhixinhuixue.armor.service.IZSYBugService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Api(value = "Bug处理接口", description = "线上bug相关操作接口", tags = "/bug")
@RequestMapping("/api/bug")
@RestController
public class ZSYBugController extends ZSYController{

    @Autowired
    private IZSYBugService bugService;

    /**
     * bug处理结果表
     * @return
     */
    @ApiOperation("bug处理表")
    @GetMapping(value = "/list")
    public String getBug(BugListReqDTO bugListReqDTO){
        return ZSYResult.success().data(bugService.getBugList(bugListReqDTO)).build();
    }

    /**
     * 添加bug处理
     * @return
     */
    @ApiOperation("添加bug处理")
    @PostMapping(value = "/add")
    public String addBug(@Valid @RequestBody BugReqDTO bugReqDTO){
        bugService.addBug(bugReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * bug处理详情
     * @return
     */
    @ApiOperation("bug处理详情")
    @GetMapping(value = "/{id}")
    public String getBugDetail(@PathVariable("id") Long id){
        return bugService.getBugDetail(id).build();
    }


}
