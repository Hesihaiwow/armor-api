package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.YearReqDTO;
import com.zhixinhuixue.armor.model.dto.response.AnnualFeedbackResDTO;
import com.zhixinhuixue.armor.model.dto.response.AnnualTaskResDTO;
import com.zhixinhuixue.armor.service.IZSYDataService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/1/10 13:00
 */
@RestController
@RequestMapping("/api/data")
@Api(value = "数据统计相关接口", description = "任务管理系统数据统计相关接口", tags = "/data")
public class ZSYDataController {

    @Autowired
    private IZSYDataService zsyDataService;

    @ApiOperation("年度需求总数(学管端,其他)")
    @PostMapping("/annual/feedback-num")
    public String getAnnualFeedbackData(@RequestBody YearReqDTO reqDTO){
        AnnualFeedbackResDTO resDTO = zsyDataService.getAnnualFeedbackData(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("年度任务总数(多人任务,个人任务)")
    @PostMapping("/annual/task-num")
    public String getAnnualTaskData(@RequestBody YearReqDTO reqDTO){
        AnnualTaskResDTO resDTO = zsyDataService.getAnnualTaskData(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }
}
