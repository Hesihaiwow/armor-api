package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserCommentsPageResDTO;

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

    /**
     * 统计用户评论
     * @param pageNum
     * @param userId
     * @param grade
     * @return
     */
    PageInfo<UserCommentsPageResDTO> findByPage(Integer pageNum, Long userId, String grade);
}
