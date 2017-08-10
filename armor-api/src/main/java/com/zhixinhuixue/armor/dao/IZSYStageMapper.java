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
     * @param record
     * @return
     */
    int insert(Stage record);

    /**
     * 删除阶段
     * @param id
     * @return
     */
    int deleteStage(long id);


}