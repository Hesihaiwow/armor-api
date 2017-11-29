package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.StatsUserWeekBO;
import com.zhixinhuixue.armor.model.dto.request.UserWeekStatsReqDTO;
import com.zhixinhuixue.armor.model.pojo.UserWeek;

import java.util.List;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public interface IZSYUserWeekMapper {

    int insertList(List<UserWeek> userWeeks);

    int deleteByTaskId(Long taskId);

    /**
     * 周工作统计
     * @param userWeek
     * @return
     */
    List<StatsUserWeekBO> getUserWeekStats(UserWeekStatsReqDTO userWeek);
}
