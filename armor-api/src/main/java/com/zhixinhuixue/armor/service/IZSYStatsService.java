package com.zhixinhuixue.armor.service;


import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;

import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
public interface IZSYStatsService {

    /**
     * 获取任务统计信息
     *
     * @return
     */
    List<StatsPageResDTO> getStats();
}
