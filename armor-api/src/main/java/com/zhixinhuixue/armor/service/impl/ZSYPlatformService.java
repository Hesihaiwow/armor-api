package com.zhixinhuixue.armor.service.impl;

import com.google.common.base.Strings;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYPlatformMapper;
import com.zhixinhuixue.armor.dao.IZSYWeekPublishPlanPlatformMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.AddPlatformReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditPlatformReqDTO;
import com.zhixinhuixue.armor.model.dto.response.PlatformResDTO;
import com.zhixinhuixue.armor.model.pojo.Platform;
import com.zhixinhuixue.armor.service.IZSYPlatformService;
import com.zhixinhuixue.armor.source.enums.PlatformGroupEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author sch
 * @DATE 2019/6/10 16:28
 */
@Service
public class ZSYPlatformService implements IZSYPlatformService {

    @Autowired
    private IZSYPlatformMapper platformMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYWeekPublishPlanPlatformMapper planPlatformMapper;

    /**
     * 新增
     * author sch
     * @param reqDTO 参数
     */
    @Override
    @Transactional
    public void add(AddPlatformReqDTO reqDTO) {
        //校验是否存在
        Platform existPlatform = platformMapper.selectByName(reqDTO.getName().trim());
        if (existPlatform != null){
            throw new ZSYServiceException("当前名称已存在");
        }

        Platform platform = new Platform();
        platform.setId(snowFlakeIDHelper.nextId());
        platform.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        platform.setCreateTime(new Date());
        platform.setGroupMark(reqDTO.getGroupMark());
        platform.setName(reqDTO.getName().trim());


        if (platformMapper.insert(platform) == 0){
            throw new ZSYServiceException("新增平台失败");
        }
    }

    /**
     * 编辑
     * @param reqDTO 参数
     * @param id id
     */
    @Override
    @Transactional
    public void editPlatform(EditPlatformReqDTO reqDTO, Long id) {
        Platform existPlatform = platformMapper.selectById(id);
        if (existPlatform == null){
            throw new ZSYServiceException("当前发布平台不存在,请检查");
        }
        Platform sameNamePlatform = platformMapper.selectByName(reqDTO.getName().trim());
        if (sameNamePlatform != null && !sameNamePlatform.getId().equals(id)){
            throw new ZSYServiceException("当前名称已存在");
        }

        Platform platform = new Platform();
        platform.setId(id);
        platform.setGroupMark(reqDTO.getGroupMark());
        platform.setName(reqDTO.getName().trim());

        platformMapper.updateById(platform);
    }

    /**
     * 列表展示
     * @author  sch
     * @return
     */
    @Override
    public List<PlatformResDTO> list(String groupMark) {
        Integer group = null;
        if (!Strings.isNullOrEmpty(groupMark)){
            group = Integer.valueOf(groupMark);
        }
        List<Platform> platforms = platformMapper.selectList(group);
        List<PlatformResDTO> platformResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(platforms)){
            platforms.forEach(platform -> {
                PlatformResDTO res = new PlatformResDTO();
                res.setId(platform.getId());
                res.setName(platform.getName());
                Random random = new Random();
                int i = random.nextInt(8);
                res.setColor(i);
                res.setGroupMark(platform.getGroupMark());
                res.setGroupMarkText(PlatformGroupEnum.getName(platform.getGroupMark()));
                platformResDTOList.add(res);
            });
        }
        return platformResDTOList;
    }

    /**
     * 删除
     * @param id 平台id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Integer count = planPlatformMapper.checkPlatformUse(id);
        if (count>0){
            throw new ZSYServiceException("平台正在使用中,无法删除");
        }
        platformMapper.deleteById(id);
    }
}
