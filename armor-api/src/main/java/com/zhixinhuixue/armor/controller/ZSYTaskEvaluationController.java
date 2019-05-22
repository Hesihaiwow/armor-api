package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddEvaluationReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskEvaluationService;
import com.zhixinhuixue.armor.service.IZSYTaskExpandService;
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
 * @DATE 2019/5/22 10:57
 */
@RestController
@RequestMapping("/api/task/evaluation")
@Api(value = "任务评价相关接口", description = "任务管理系统任务评价相关接口", tags = "/evaluation")
public class ZSYTaskEvaluationController {
    @Autowired
    private IZSYTaskEvaluationService taskEvaluationService;

    @ApiOperation("新增评价")
    @PostMapping("/add")
    public String evaluate(@Valid @RequestBody AddEvaluationReqDTO reqDTO){
        ZSYResult zsyResult = taskEvaluationService.evaluate(reqDTO);
        if (zsyResult.getErrCode().equals("00")) {
            taskEvaluationService.finishTask(reqDTO.getTaskId());
        }
        return zsyResult.build();
    }
}
