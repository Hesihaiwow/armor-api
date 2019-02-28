package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugManageAddReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugReqDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineBugDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineBugNumResDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineBugResDTO;
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
     * 更新bug处理
     * @return
     */
    @ApiOperation("更新bug处理")
    @PutMapping(value = "/update/{id}")
    public String update(@Valid @RequestBody BugReqDTO bugReqDTO, @PathVariable("id") Long id){
        bugService.updateBug(id, bugReqDTO);
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

    /**
     * 删除Bug处理记录
     * @param id
     * @return
     */
    @ApiOperation("删除Bug处理记录")
    @DeleteMapping(value = "/{id}")
    public String deleteProject( @PathVariable("id") Long id){
        bugService.deleteBug(id);
        return ZSYResult.success().build();
    }

    // sch --

    @ApiOperation("添加bug处理")
    @PostMapping(value = "/add-bug")
    public String addNewBug(@Valid @RequestBody BugManageAddReqDTO reqDTO){
        bugService.addNewBug(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("分页查询线上bug")
    @PostMapping(value = "/page")
    public String getBugManagePage(@RequestBody BugListReqDTO bugListReqDTO){
        PageInfo<OnlineBugResDTO> pageInfo = bugService.getBugManagePage(bugListReqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }

    @ApiOperation("bug处理详情")
    @GetMapping(value = "/detail/{id}")
    public String getOnlineBugDetail(@PathVariable("id") Long id){
        OnlineBugDetailResDTO resDTO = bugService.getOnlineBugDetail(id);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("更新bug处理")
    @PutMapping(value = "/update-bug/{id}")
    public String updateOnlineBug(@Valid @RequestBody BugManageAddReqDTO reqDTO,@PathVariable Long id){
        bugService.updateOnlineBug(reqDTO,id);
        return ZSYResult.success().build();
    }

    @ApiOperation("查询各个分类的线上bug数量")
    @GetMapping(value = "/num/type")
    public String getDiffTypeBugNum(){
        OnlineBugNumResDTO resDTO = bugService.getDiffTypeBugNum();
        return ZSYResult.success().data(resDTO).build();
    }
    // -- sch

}
