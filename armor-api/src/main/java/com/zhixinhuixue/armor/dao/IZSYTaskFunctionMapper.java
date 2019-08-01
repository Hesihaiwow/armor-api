package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.TaskFunctionBO;
import com.zhixinhuixue.armor.model.pojo.TaskFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/7/29 10:46
 */
public interface IZSYTaskFunctionMapper {

    /**
     * 批量插入任务功能点
     * @author sch
     * @param functions
     */
    void insertBatch(@Param("functions") List<TaskFunction> functions);

    /**
     * 根据taskId查询任务功能点
     * @author sch
     * @param taskId
     * @return
     */
    List<TaskFunction> selectListByTaskId(@Param("taskId")Long taskId);

    /**
     * 批量修改
     * @author sch
     * @param functions
     */
    void updateBatch(@Param("functions")List<TaskFunction> functions);

    /**
     * 根据taskId查询任务功能点
     * @author sch
     * @param taskId
     * @return
     */
    List<TaskFunctionBO> selectBOListByTaskId(@Param("taskId")Long taskId);

    /**
     * 根据任务id删除所关联的功能点
     * @author sch
     * @param taskId
     */
    void deleteByTaskId(@Param("taskId")Long taskId);
}
