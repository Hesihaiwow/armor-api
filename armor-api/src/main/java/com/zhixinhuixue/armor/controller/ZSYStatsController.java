package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.CalculateResDTO;
import com.zhixinhuixue.armor.model.dto.response.MonthWorkStatsResDTO;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.ZSYSwaggerResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@RequestMapping("/api/stats")
@RestController
public class ZSYStatsController extends ZSYController {

    @Resource
    private IZSYStatsService statsService;

    /**
     * 用户积分统计信息
     *
     * @return
     */
    @GetMapping(value = "/list")
    public String getStats() {
        List<StatsPageResDTO> statsPageResDTOList = statsService.getStats();
        return ZSYResult.success().data(statsPageResDTOList).build();
    }

    /**
     * 个人任务统计
     *
     * @return
     */
    @GetMapping(value = "/personTaskList")
    public String getPersonalList(PersonalTaskListReqDTO personalTaskListReqDTO) {
        return ZSYResult.success().data(statsService.getPersonalList(personalTaskListReqDTO)).build();
    }

    /**
     * 周工作统计
     *
     * @return
     */
    @PostMapping(value = "/weekStats")
    public String getWeekStats(@RequestBody UserWeekStatsReqDTO userWeek) {
        return ZSYResult.success().data(statsService.getWeekStats(userWeek)).build();
    }

    /**
     * 积分奖金计算
     *
     * @return
     */
    @PostMapping(value = "/calculate")
    public String calculate(@Valid @RequestBody CalculateReqDTO calculateReqDTO) {
        List<CalculateResDTO> calculateResDTOS = statsService.calculate(calculateReqDTO);
        return ZSYResult.success().data(calculateResDTOS).build();
    }

    /**
     * 分页用户评论记录
     *
     * @return
     */
    @GetMapping(value = "/userComments")
    public String getUserComments(@ModelAttribute UserCommentsReqDTO userCommentsReqDTO) {
        return ZSYResult.success().data(statsService.findByPage(
                userCommentsReqDTO.getPageNum(),
                userCommentsReqDTO.getUserId(),
                userCommentsReqDTO.getGrade())).build();
    }

    /**
     * 加班统计
     *
     * @author sch
     */
    @PostMapping("/extra-work/page")
    public String getExtraWorkStats(@RequestBody ExtraWorkStatsReqDTO reqDTO) {
        return ZSYResult.success().data(statsService.getExtraWorkStats(reqDTO)).build();
    }

    /**
     * 周人员投入表
     */
    @PostMapping("/week-user-cost/v2")
    public String getWeekUserCostV2(@Valid @RequestBody QueryUserCostReqDTO reqDTO) {
        return ZSYResult.success().data(statsService.getWeekUserCostV2(reqDTO)).build();
    }

    /**
     * 查询用户月工作总结
     *
     * @param reqDTO 参数
     * @author sch
     */
    @PostMapping("/month-stats")
    public ZSYSwaggerResult<List<MonthWorkStatsResDTO>> getUserMonthStats(@RequestBody MonthWorkStatsReqDTO reqDTO) {
        return new ZSYSwaggerResult<>(statsService.getUserMonthStats(reqDTO));
    }
}
