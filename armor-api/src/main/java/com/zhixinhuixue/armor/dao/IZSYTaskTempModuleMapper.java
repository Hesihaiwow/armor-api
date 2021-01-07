package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskTempModule;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/7/29 10:46
 */
public interface IZSYTaskTempModuleMapper {

    /**
     * 根据名称查询
     * @author sch
     * @param name
     * @return
     */
    TaskTempModule selectByName(@Param("name") String name);

    /**
     * 新增
     * @author sch
     * @param project
     * @return
     */
    int insert(TaskTempModule project);

    /**
     * 查看列表
     * @author sch
     * @return
     */
    List<TaskTempModule> selectList(@Param("orgId")Long orgId);

    /**
     * 修改
     * @author sch
     * @param existProject
     * @return
     */
    int update(TaskTempModule existProject);

    /**
     * 查询
     * @author sch
     * @param id
     * @return
     */
    TaskTempModule selectByPrimary(@Param("id") Long id);

    /**
     * 校验是否关联任务
     * @param id
     * @return
     */
    Integer selectCountInUse(@Param("id")Long id, @Param("orgId")Long orgId);
}
