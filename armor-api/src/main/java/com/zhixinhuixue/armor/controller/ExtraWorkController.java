package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.AddExtraWorkReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkResDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.service.IZSYExtraWorkService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author SCH
 * @date 2019/2/14 16:15
 */
@RestController
@RequestMapping("/api/extra-work")
@Api(value = "加班申请相关接口", description = "任务管理系统加班申请相关接口", tags = "/extra-work")
public class ExtraWorkController {
    @Autowired
    private IZSYExtraWorkService extraWorkService;

    @ApiOperation("新增加班申请")
    @PostMapping("/add")
    public String addExtraWork(@Valid @RequestBody AddExtraWorkReqDTO reqDTO){
        extraWorkService.addExtraWork(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改加班申请")
    @PutMapping("/update/{ewId}")
    public String updateExtraWork(@Valid @RequestBody AddExtraWorkReqDTO reqDTO,@PathVariable("ewId")Long ewId){
        extraWorkService.updateExtraWork(reqDTO,ewId);
        return ZSYResult.success().build();
    }

    @ApiOperation("审核通过")
    @PutMapping("/check/{ewId}")
    public String checkExtraWork(@PathVariable("ewId")Long ewId){
        extraWorkService.checkExtraWork(ewId);
        return ZSYResult.success().build();
    }

    @ApiOperation("删除加班申请")
    @PutMapping("/delete/{ewId}")
    public String deleteExtraWork(@PathVariable("ewId")Long ewId){
        extraWorkService.deleteExtraWork(ewId);
        return ZSYResult.success().build();
    }

    @ApiOperation("查询我的加班申请(待审核)")
    @GetMapping("/page/wait/{pageNum}")
    public String getWaitExtraWorkByPage(@PathVariable("pageNum")Integer pageNum){
        PageInfo<ExtraWorkResDTO> pageInfo = extraWorkService.getWaitExtraWorkByPage(pageNum);
        return ZSYResult.success().data(pageInfo).build();
    }

    @ApiOperation("查询我的加班申请(审核通过)")
    @GetMapping("/page/access/{pageNum}")
    public String getAccessExtraWorkByPage(@PathVariable("pageNum")Integer pageNum){
        PageInfo<ExtraWorkResDTO> pageInfo = extraWorkService.getAccessExtraWorkByPage(pageNum);
        return ZSYResult.success().data(pageInfo).build();
    }

    @ApiOperation("查询审核中的加班申请")
    @GetMapping("/page/checking/{pageNum}")
    public String getCheckingExtraWorkByPage(@PathVariable("pageNum")Integer pageNum){
        PageInfo<ExtraWorkResDTO> pageInfo = extraWorkService.getCheckingExtraWorkByPage(pageNum);
        return ZSYResult.success().data(pageInfo).build();
    }

    @ApiOperation("查询审核通过的加班申请")
    @GetMapping("/page/checked/{pageNum}")
    public String getCheckedExtraWorkByPage(@PathVariable("pageNum")Integer pageNum){
        PageInfo<ExtraWorkResDTO> pageInfo = extraWorkService.getCheckedExtraWorkByPage(pageNum);
        return ZSYResult.success().data(pageInfo).build();
    }

    @ApiOperation("查询我的未完成任务")
    @GetMapping("/task-running")
    public String getMyRunningTask(){
        List<Task> list = extraWorkService.getMyRunningTaskList();
        return ZSYResult.success().data(list).build();
    }
}
