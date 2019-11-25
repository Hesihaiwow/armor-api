package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddEvaluationReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EvaluationPageQueryReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskEvaluationService;
import com.zhixinhuixue.armor.service.IZSYTaskExpandService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author sch
 * @DATE 2019/5/22 10:57
 */
@RestController
@RequestMapping("/api/evaluation")
@Api(value = "任务评价相关接口", description = "任务管理系统任务评价相关接口", tags = "/evaluation")
public class ZSYTaskEvaluationController {
    @Autowired
    private IZSYTaskEvaluationService taskEvaluationService;

    @ApiOperation("新增评价")
    @PostMapping("/add")
    public String evaluate(@Valid @RequestBody AddEvaluationReqDTO reqDTO){
        ZSYResult zsyResult = taskEvaluationService.evaluate(reqDTO);
//        if (zsyResult.getErrCode().equals("00")) {
//            taskEvaluationService.finishTask(reqDTO.getTaskId());
//        }
        return zsyResult.build();
    }

    @ApiOperation("查询待评价任务")
    @GetMapping("/task/wait")
    public String getWaitEvaluated(){
        return ZSYResult.success().data(taskEvaluationService.getWaitEvaluated()).build();
    }

    @ApiOperation("分页查看已评价的任务")
    @GetMapping("/task/evaluated/{pageNum}")
    public String getEvaluated(@PathVariable("pageNum")Integer pageNum){
        return ZSYResult.success().data(taskEvaluationService.getEvaluated(pageNum)).build();
    }

    @ApiOperation("管理员分页查看用户所有任务综合评价")
    @PostMapping("/average/user/page")
    public String getUserAvgEvaluation(@RequestBody EvaluationPageQueryReqDTO reqDTO){
        return ZSYResult.success().data(taskEvaluationService.getUserAvgEvaluation(reqDTO)).build();
    }

    @ApiOperation("个人主页查看周,月,年的综合评价")
    @GetMapping("/average/personal")
    public String getPersonalAverageEva(){
        return ZSYResult.success().data(taskEvaluationService.getPersonalAverageEva()).build();
    }
}
