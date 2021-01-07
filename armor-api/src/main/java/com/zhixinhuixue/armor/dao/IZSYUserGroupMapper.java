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


    /**
     *  根据组id获取UserGroup集合
     *
     * @param groupIds
     * @return
     * @author hsh
     * @create 2020/11/5-17:12
     */
    List<UserGroup> selectByGroupIds(@Param("groupIds")List<Long> groupIds,@Param("orgId")Long orgId);
}
