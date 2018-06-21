package com.zhixinhuixue.armor.service;



import com.zhixinhuixue.armor.model.dto.response.StageResDTO;
import com.zhixinhuixue.armor.source.ZSYResult;

import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYStageService {

    /**
     * 获取阶段列表
     * @return
     */
    List<StageResDTO> getStage();

    /**
     * 添加阶段
     * @param stageResDTO
     * @return
     */
    Long addStage(StageResDTO stageResDTO);

    /**
     *编辑阶段
     * @param stageResDTO
     */
    void editStage(StageResDTO stageResDTO);

    /**
     * 删除阶段
     * @param id
     */
    void deleteStage(Long id);

    /**
     * 移动阶段
     * @param id
     * @param sort
     */
    ZSYResult moveStage(Long id, int sort);

    /**
     * 移动阶段
     * @param stageResDTO
     */
    void moveStage(StageResDTO stageResDTO);


}
