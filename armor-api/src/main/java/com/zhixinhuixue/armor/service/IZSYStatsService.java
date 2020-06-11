package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;

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

    /**
     * 加班统计
     * @author sch
     */
    PageInfo<ExtraWorkStatsResDTO> getExtraWorkStats(ExtraWorkStatsReqDTO reqDTO);

    /**
     * 周人员投入表
     * @param reqDTO 参数
     * @author sch
     * @return List<UserCostResDTO>
     */
    List<UserCostResDTO> getWeekUserCost(QueryUserCostReqDTO reqDTO);

    /**
     * 周人员投入表
     * @param reqDTO 参数
     * @author sch
     * @return List<WeekUserCostResDTO>
     */
    List<WeekUserCostResDTO> getWeekUserCostV2(QueryUserCostReqDTO reqDTO);
}
