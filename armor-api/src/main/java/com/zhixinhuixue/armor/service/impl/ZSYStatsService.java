package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYStatsMapper;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Service
public class ZSYStatsService implements IZSYStatsService {

    @Autowired
    private IZSYStatsMapper statsMapper;

    @Override
    public List<StatsPageResDTO> getStats(){
        List<StatsPageResDTO> userIntegralHistoryBOS = statsMapper.getStats();
        return userIntegralHistoryBOS;
    }
}
