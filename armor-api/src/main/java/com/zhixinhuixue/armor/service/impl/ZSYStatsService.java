package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.dao.IZSYStatsMapper;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Service
public class ZSYStatsService implements IZSYStatsService {

    @Autowired
    private IZSYStatsMapper statsMapper;

    @Override
    public PageInfo<StatsPageResDTO> getStats(int pageIndex){
        PageHelper.startPage(pageIndex, ZSYConstants.PAGE_SIZE);
        Page<StatsPageResDTO> userIntegralHistoryBOS = statsMapper.getStats();
        PageInfo<StatsPageResDTO> pageInfo = new PageInfo<>(userIntegralHistoryBOS);
        return pageInfo;
    }
}
