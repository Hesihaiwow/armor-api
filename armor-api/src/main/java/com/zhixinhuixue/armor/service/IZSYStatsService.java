package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.CalculateReqDTO;
import com.zhixinhuixue.armor.model.dto.request.PersonalTaskListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserWeekStatsReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.dto.response.UserCommentsPageResDTO;

import java.util.Date;
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

    List<PersonTaskResDTO> getPersonalList(PersonalTaskListReqDTO personalTaskListReqDTO);

    List<CalculateResDTO> calculate(CalculateReqDTO calculateReqDTO);

    /**
     * 统计用户评论
     * @param pageNum
     * @param userId
     * @param grade
     * @return
     */
    PageInfo<UserCommentsPageResDTO> findByPage(Integer pageNum, Long userId, String grade);

    /**
     * 周工作量统计
     * @param userWeek
     * @return
     */
    List<StatsWeekResDTO> getWeekStats(UserWeekStatsReqDTO userWeek);
}
