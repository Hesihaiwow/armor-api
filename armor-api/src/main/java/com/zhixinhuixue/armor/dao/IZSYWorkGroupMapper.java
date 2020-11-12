package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.WorkGroupBO;
import com.zhixinhuixue.armor.model.pojo.WorkGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @time 2020/1/8 9:35
 */
public interface IZSYWorkGroupMapper {

    /**
     * 根据名称查询
     * @author sch
     * @param name 名称
     */
    WorkGroup selectByName(@Param("name") String name);

    /**
     * 新增团队
     * @author sch
     * @param workGroup 参数
     */
    int insertGroup(WorkGroup workGroup);

    /**
     * 根据主键查询
     * @author sch
     * @param id 主键
     */
    WorkGroup selectById(@Param("id") Long id);

    /**
     * 更新团队
     * @author sch
     * @param workGroup 参数
     */
    int updateById(WorkGroup workGroup);

    /**
     * 查询团队列表
     * @author sch
     */
    List<WorkGroup> selectList();

    /**
     * 查询团队详情
     * @author sch
     * @param id id
     */
    WorkGroupBO selectDetailById(@Param("id") Long id);

    /**
     *  根据leaderId 获取 组
     *
     * @param userId
     * @return
     * @author hsh
     * @create 2020/11/6-17:25
     */
    List<WorkGroup> selectByLeaderId(@Param("userId")Long userId);


    /**
     *  id 删除团队
     *
     * @param id
     * @return
     * @author hsh
     * @create 2020/11/6-17:25
     */
    int deleteById(@Param("id")Long id);
}
