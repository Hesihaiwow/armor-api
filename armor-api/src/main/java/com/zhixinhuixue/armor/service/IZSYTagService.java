package com.zhixinhuixue.armor.service;


import com.zhixinhuixue.armor.model.dto.response.TagDTO;

import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYTagService {

    /**
     * 获取标签列表
     * @return
     */
    List<TagDTO> getTag();

    /**
     * 添加标签
     * @param name
     * @return
     */
    void addTag(String name);

    /**
     * 删除标签
     * @param id
     */
    void deleteTag(long id);

}
