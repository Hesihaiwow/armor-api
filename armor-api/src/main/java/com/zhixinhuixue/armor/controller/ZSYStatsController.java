package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.CalculateResDTO;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Api(value = "统计接口", description = "统计相关操作接口", tags = "/stats")
@RequestMapping("/api/stats")
@RestController
public class ZSYStatsController extends ZSYController{

    @Autowired
    private IZSYStatsService statsService;

    /**
     * 用户积分统计信息
     * @return
     */
    @ApiOperation("任务统计")
    @GetMapping(value = "/list")
    public String getStats(){
        List<StatsPageResDTO> statsPageResDTOList = statsService.getStats();
        return ZSYResult.success().data(statsPageResDTOList).build();
    }

    /**
     * 个人任务统计
     * @return
     */
    @ApiOperation("个人任务统计")
    @GetMapping(value = "/personTaskList")
    public String getPersonalList(PersonalTaskListReqDTO personalTaskListReqDTO){
        return ZSYResult.success().data(statsService.getPersonalList(personalTaskListReqDTO)).build();
    }

    /**
     * 周工作统计
     * @return
     */
    @ApiOperation("周工作统计")
    @PostMapping(value = "/weekStats")
    public String getWeekStats(@RequestBody UserWeekStatsReqDTO userWeek){
        return ZSYResult.success().data(statsService.getWeekStats(userWeek)).build();
    }

    /**
     * 积分奖金计算
     * @return
     */
    @ApiOperation("积分奖金计算")
    @PostMapping(value = "/calculate")
    public String calculate(@Valid @RequestBody CalculateReqDTO calculateReqDTO){
        List<CalculateResDTO> calculateResDTOS = statsService.calculate(calculateReqDTO);
        return ZSYResult.success().data(calculateResDTOS).build();
    }

    /**
     * 分页用户评论记录
     * @return
     */
    @ApiOperation("分页用户评论记录")
    @GetMapping(value = "/userComments")
    public String getUserComments(@ModelAttribute UserCommentsReqDTO userCommentsReqDTO){
        return ZSYResult.success().data(statsService.findByPage(
                userCommentsReqDTO.getPageNum(),
                userCommentsReqDTO.getUserId(),
                userCommentsReqDTO.getGrade())).build();
    }

    /**
     * 加班统计
     * @author sch
     */
    @ApiOperation("加班统计")
    @PostMapping("/extra-work/page")
    public String getExtraWorkStats(@RequestBody ExtraWorkStatsReqDTO reqDTO){
        return ZSYResult.success().data(statsService.getExtraWorkStats(reqDTO)).build();
    }

    /**
     * 周人员投入表
     */
    @ApiOperation("周人员投入表")
    @PostMapping("/week-user-cost")
    public String getWeekUserCost(@Valid @RequestBody QueryUserCostReqDTO reqDTO){
        return ZSYResult.success().data(statsService.getWeekUserCost(reqDTO)).build();
    }

    /**
     * 周人员投入表
     */
    @ApiOperation("周人员投入表")
    @PostMapping("/week-user-cost/v2")
    public String getWeekUserCostV2(@Valid @RequestBody QueryUserCostReqDTO reqDTO){
        return ZSYResult.success().data(statsService.getWeekUserCostV2(reqDTO)).build();
    }
}
