package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYPlatformMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.response.PlatformResDTO;
import com.zhixinhuixue.armor.model.pojo.Platform;
import com.zhixinhuixue.armor.service.IZSYPlatformService;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    /**
     * 新增
     * author sch
     * @param name
     */
    @Override
    public void add(String name) {
        //校验是否存在
        Platform existPlatform = platformMapper.selectByName(name.trim());
        if (existPlatform != null){
            throw new ZSYServiceException("当前名称已存在");
        }

        Platform platform = new Platform();
        platform.setId(snowFlakeIDHelper.nextId());
        platform.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        platform.setCreateTime(new Date());
        platform.setName(name.trim());


        if (platformMapper.insert(platform) == 0){
            throw new ZSYServiceException("新增平台失败");
        }
    }

    /**
     * 列表展示
     * @author  sch
     * @return
     */
    @Override
    public List<PlatformResDTO> list() {
        List<Platform> platforms = platformMapper.selectList();
        List<PlatformResDTO> platformResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(platforms)){
            platforms.stream().forEach(platform -> {
                PlatformResDTO res = new PlatformResDTO();
                res.setId(platform.getId());
                res.setName(platform.getName());
                Random random = new Random();
                int i = random.nextInt(8);
                res.setColor(i);
                platformResDTOList.add(res);
            });
        }
        return platformResDTOList;
    }
}
