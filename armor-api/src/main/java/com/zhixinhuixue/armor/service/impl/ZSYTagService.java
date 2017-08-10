package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTagMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.response.TagDTO;
import com.zhixinhuixue.armor.model.pojo.Tag;
import com.zhixinhuixue.armor.service.IZSYTagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Override
    public List<TagDTO> getTag(){
        List<Tag> tags = tagMapper.selectTag();

        List<TagDTO> tagDTOS = new ArrayList<>();

        tags.stream().forEach(tag -> {
            TagDTO stageDTO = new TagDTO();
            BeanUtils.copyProperties(tags,stageDTO);
            stageDTO.setName(tag.getName());
            stageDTO.setId(tag.getId());
            tagDTOS.add(stageDTO);
        });

        return tagDTOS;
    }

    /**
     * 添加标签
     * @param name
     */
    @Override
    public void addTag(String name){
        if(tagMapper.validateTag(name.replace(" ", ""))>0){
            throw new ZSYServiceException("标签名称已存在");
        }else{
            Tag tag = new Tag();
            tag.setCreateBy(ZSYTokenRequestContext.get().getUserId());
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
    }

    /**
     * 删除标签
     * @param id
     */
    @Override
    public void deleteTag(String id){
        if(tagMapper.deleteTag(new Long(id))==0){
            throw new ZSYServiceException("删除失败");
        }
    }
}
