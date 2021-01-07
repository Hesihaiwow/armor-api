package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTagMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.response.TagResDTO;
import com.zhixinhuixue.armor.model.pojo.Tag;
import com.zhixinhuixue.armor.service.IZSYTagService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYTagService implements IZSYTagService {

    @Resource
    private IZSYTagMapper tagMapper;

    @Resource
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 获取标签列表
     *
     * @return
     */
    @Override
    public List<TagResDTO> getTag() {
        List<Tag> tags = tagMapper.selectTag(ZSYTokenRequestContext.get().getDepartmentId());

        List<TagResDTO> tagResDTOS = new ArrayList<>();

        tags.stream().forEach(tag -> {
            TagResDTO tagResDTO = new TagResDTO();
            BeanUtils.copyProperties(tag, tagResDTO);
            tagResDTOS.add(tagResDTO);
        });

        return tagResDTOS;
    }

    /**
     * 添加标签
     *
     * @param name
     */
    @Override
    public Long addTag(String name) {
        if (tagMapper.validateTag(name.replace(" ", ""), ZSYTokenRequestContext.get().getDepartmentId()) > 0) {
            throw new ZSYServiceException("标签名称已存在");
        }
        Tag tag = new Tag();
        tag.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        tag.setCreateTime(new Date());
        tag.setId(snowFlakeIDHelper.nextId());
        tag.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
        int count = tagMapper.countTag(null,ZSYTokenRequestContext.get().getOrgId());
        if (count < 8) {
            tag.setColor("" + (count + 1));
        } else {
            tag.setColor("7");
        }
        tag.setName(name);
        tag.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        tagMapper.insert(tag);
        return tag.getId();
    }

    /**
     * 删除标签
     *
     * @param id
     */
    @Override
    public void deleteTag(Long id) {
        if (tagMapper.countTag(id,ZSYTokenRequestContext.get().getOrgId()) != 0) {
            throw new ZSYServiceException("标签正在使用中，请修改或停止使用后删除");
        }
        if (tagMapper.deleteTag(id) == 0) {
            throw new ZSYServiceException("删除失败");
        }
    }
}
