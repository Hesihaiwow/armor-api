package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;

/**
 * Created by Lang on 2017/9/4 0004.
 */
public interface IZSYStatsService {

    /**
     * 获取任务统计信息
     * @return
     */
    PageInfo<StatsPageResDTO> getStats(int pageIndex);
}
