package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.TaskTempFunctionBO;
import com.zhixinhuixue.armor.model.bo.UserAndLevelBO;
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

    /**
     * 查询用户对应功能点等级
     * @param id
     * @return
     */
    List<UserAndLevelBO> selectUserAndLevelByFunction(@Param("id")Long id);

    /**
     * 根据任务和用户查询功能点
     * @author sch
     * @param taskId 任务
     * @param userId 用户
     */
    List<TaskTempFunction> selectListByTaskAndUser(@Param("taskId")Long taskId, @Param("userId")Long userId);
}
