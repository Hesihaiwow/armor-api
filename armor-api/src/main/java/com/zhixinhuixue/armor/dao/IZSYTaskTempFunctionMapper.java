package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.TaskTempFunctionBO;
import com.zhixinhuixue.armor.model.pojo.TaskTempFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/7/29 10:46
 */
public interface IZSYTaskTempFunctionMapper {

    /**
     * 批量新增
     * @author sch
     * @param functionList
     */
    void insertBatch(@Param("functionList") List<TaskTempFunction> functionList);

    /**
     * 根据ttId删除
     * @author sch
     * @param ttId
     */
    void deleteByTtId(@Param("ttId")Long ttId);

    /**
     * 根据ttId查询
     * @author sch
     * @param ttId
     * @return
     */
    List<TaskTempFunctionBO> selectListByTtId(@Param("ttId")Long ttId);
}
