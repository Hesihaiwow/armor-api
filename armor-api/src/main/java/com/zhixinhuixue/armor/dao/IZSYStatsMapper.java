package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;

import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
public interface IZSYStatsMapper {

    /**
     * 获取任务统计
     *
     * @return
     */
    List<StatsPageResDTO> getStats();
}
