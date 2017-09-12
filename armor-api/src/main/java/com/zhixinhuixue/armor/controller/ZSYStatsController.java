package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Api(value = "统计接口", description = "任务积分统计相关操作接口", tags = "/stats")
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
}
