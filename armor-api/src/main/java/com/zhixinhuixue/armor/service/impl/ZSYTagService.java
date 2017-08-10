package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTagMapper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.pojo.Tag;
import com.zhixinhuixue.armor.service.IZSYTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYTagService implements IZSYTagService {

    @Autowired
    private IZSYTagMapper tagMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 获取标签列表
     * @return
     */
    public List<Tag> getTag(){
        List<Tag> tags = tagMapper.selectTag();
        return tags;
    }

    /**
     * 添加标签
     * @param name
     */
    public void addTag(String name){
        Tag tag = new Tag();
        tag.setCreateBy(ZSYTokenRequestContext.get().getUserId());
//        project.setCreateBy(1231231221l);
        tag.setCreateTime(new Date());
        tag.setId(snowFlakeIDHelper.nextId());
        int count = tagMapper.countTag();
        if(count<8){
            tag.setColor(""+(count+1));
        }else{
            tag.setColor("7");
        }
        tag.setName(name);
        tagMapper.insert(tag);
    }

    /**
     * 删除标签
     * @param id
     */
    public void deleteTag(String id){
        tagMapper.deleteTag(new Long(id));
    }
}
