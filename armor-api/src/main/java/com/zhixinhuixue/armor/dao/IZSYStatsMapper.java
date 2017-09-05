package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;

/**
 * Created by Lang on 2017/9/4 0004.
 */
public interface IZSYStatsMapper {

    /**
     * 获取任务统计
     * @return
     */
    Page<StatsPageResDTO> getStats();
}
