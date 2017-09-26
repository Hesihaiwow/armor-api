package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.dto.response.StageResDTO;
import com.zhixinhuixue.armor.model.pojo.Stage;

import java.util.List;

public interface IZSYStageMapper {

    /**
     * 阶段列表
     * @return
     */
    List<StageResDTO> selectStage();

    /**
     * 添加阶段
     * @param record 阶段记录
     * @return
     */
    int insert(Stage record);

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

    /**
     * 根据ID查询阶段
     * @param id
     * @return
     */
    Stage selectById(Long id);

    /**
     * 查询阶段ID是否被使用
     */
    int countStage(Long id);

    /**
     * 编辑阶段
     * @param stage
     * @return
     */
    int update(Stage stage);


}