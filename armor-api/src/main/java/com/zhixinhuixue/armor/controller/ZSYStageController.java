package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.response.StageResDTO;
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
@RequestMapping("/api/stage")
@RestController
public class ZSYStageController extends ZSYController {

    @Autowired
    private IZSYStageService stageService;

    /**
     * 获取阶段列表
     *
     * @return
     */
    @ApiOperation("阶段列表")
    @GetMapping(value = "/list")
    public String getStage() {
        List<StageResDTO> stageDTOS = stageService.getStage();
        return ZSYResult.success().data(stageDTOS).build();
    }

    /**
     * 添加阶段
     *
     * @return
     */
    @ApiOperation("添加阶段")
    @PostMapping(value = "/add")
    public String addStage(@RequestBody StageResDTO stageResDTO) {
        Long id = stageService.addStage(stageResDTO);
        return ZSYResult.success().data(id).build();
    }

    /**
     * 编辑阶段
     *
     * @return
     */
    @ApiOperation("编辑阶段")
    @PostMapping(value = "/edit")
    public String editStage(@RequestBody StageResDTO stageResDTO) {
        stageService.editStage(stageResDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除阶段
     *
     * @return
     */
    @ApiOperation("删除阶段")
    @DeleteMapping(value = "/{stageId}")
    public String deleteStage(@PathVariable Long stageId) {
        stageService.deleteStage(stageId);
        return ZSYResult.success().build();
    }

}
