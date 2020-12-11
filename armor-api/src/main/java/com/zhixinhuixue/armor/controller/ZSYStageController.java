package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.response.StageResDTO;
import com.zhixinhuixue.armor.service.IZSYStageService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 阶段接口
 * Created by Lang on 2017/8/9 0007.
 */
@RequestMapping("/api/stage")
@RestController
public class ZSYStageController extends ZSYController {

    @Resource
    private IZSYStageService stageService;

    /**
     * 获取阶段列表
     *
     * @return
     */
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
    @DeleteMapping(value = "/{stageId}")
    public String deleteStage(@PathVariable Long stageId) {
        stageService.deleteStage(stageId);
        return ZSYResult.success().build();
    }

    /**
     * 移动阶段
     *
     * @param id
     * @param sort
     * @return
     */
    @PutMapping(value = "/move/{id}/{sort}")
    public String moveStage(@PathVariable("id") Long id, @PathVariable("sort") Integer sort) {
        return stageService.moveStage(id, sort).build();
    }

    /**
     * 移动阶段
     *
     * @param stageResDTO
     * @return
     */
    @PutMapping(value = "/move")
    public String moveStage(@Valid @RequestBody StageResDTO stageResDTO) {
        stageService.moveStage(stageResDTO);
        return ZSYResult.success().build();
    }

}
