package com.zhixinhuixue.armor.service;



import com.zhixinhuixue.armor.model.dto.response.StageDTO;

import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYStageService {

    /**
     * 获取阶段列表
     * @return
     */
    List<StageDTO> getStage();

    /**
     * 添加阶段
     * @param name
     * @return
     */
    void addStage(String name);

    /**
     * 删除阶段
     * @param id
     */
    void deleteStage(String id);

}
