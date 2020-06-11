package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.UserGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @time 2020/6/10 14:13
 */
public interface IZSYUserGroupMapper {

    /**
     * 新增
     * @param userGroup 内容
     */
    void insert(UserGroup userGroup);

    /**
     * 删除团队_成员关联
     * @param groupId 团队id
     */
    void deleteByGroup(@Param("groupId") Long groupId);

    /**
     * 批量新增
     * @param list
     */
    void insertBatch(@Param("list") List<UserGroup> list);
}
