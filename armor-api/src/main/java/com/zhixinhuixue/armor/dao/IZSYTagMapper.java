package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Tag;

import java.util.List;

public interface IZSYTagMapper {

    /**
     * 标签列表
     * @return
     */
    List<Tag> selectTag();

    /**
     *添加标签
     * @param record
     * @return
     */
    int insert(Tag record);

    /**
     *删除标签
     * @param id
     * @return
     */
    int deleteTag(long id);

    /**
     * 标签条数
     * @return
     */
    int countTag();

    /**
     * 验证标签是否存在
     * @param name
     * @return
     */
    int validateTag(String name);

}