package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Stage;

import java.util.List;

public interface IZSYStageMapper {

    /**
     * 阶段列表
     * @return
     */
    List<Stage> selectStage();

    /**
     * 添加阶段
     * @param record 阶段记录
     * @return
     */
    void insert(Stage record);

    /**
     * 删除阶段
     * @param id
     * @return
     */
    int deleteStage(Long id);

    /**
     * 验证阶段名
     * @param name
     * @return
     */
    int validateStage(String name);


}