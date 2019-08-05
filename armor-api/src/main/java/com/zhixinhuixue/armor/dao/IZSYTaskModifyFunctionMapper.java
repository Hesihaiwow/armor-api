package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskModifyFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/8/2 16:31
 */
public interface IZSYTaskModifyFunctionMapper {

    /**
     * 批量插入
     * @author sch
     * @param functionList
     * @return
     */
    int insertBatch(@Param("functionList") List<TaskModifyFunction> functionList);

    /**
     * 根据tmId删除 修改任务功能点
     * @author sch
     * @param tmId
     */
    void deleteByTmId(@Param("tmId") Long tmId);

    /**
     * 根据任务id删除修改任功能点
     * @param taskId
     */
    void deleteByTask(@Param("taskId")Long taskId);
}
