package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.response.StageDTO;
import com.zhixinhuixue.armor.service.IZSYStageService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Lang on 2017/8/9 0007.
 */
@Api(value = "阶段接口", description = "阶段相关操作接口", tags = "/stage")
@RequestMapping("/stage")
@RestController
public class ZSYStageController extends ZSYController {

    @Autowired
    private IZSYStageService stageService;

    /**
     * 获取标签列表
     *
     * @return
     */
    @ApiOperation("阶段列表")
    @GetMapping(value = "/list")
    public String getStage() {
        List<StageDTO> stageDTOS = stageService.getStage();
        return ZSYResult.success().data(stageDTOS).build();
    }

    /**
     * 添加标签
     *
     * @return
     */
    @ApiOperation("添加阶段")
    @PostMapping(value = "/add")
    public String addStage(@RequestParam String name) {
        stageService.addStage(name);
        return ZSYResult.success().build();
    }

    /**
     * 添加标签
     *
     * @return
     */
    @ApiOperation("删除阶段")
    @DeleteMapping(value = "/{stageId}")
    public String deleteStage(@PathVariable long stageId) {
        stageService.deleteStage(stageId);
        return ZSYResult.success().build();
    }

}
