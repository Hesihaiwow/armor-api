package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.response.UserWeekResDTO;

import java.util.List;

/**
 * Created by Lang on 2017/12/4 0004.
 */
public interface IZSYUserWeekService {

    /**
     * 获取周工作量
     * @param id
     * @param weekNumber
     * @param year
     * @return
     */
    Double getUserWeekHours(Long taskId, Long id,int weekNumber, int year);

    /**
     * 获取任务成员工资量
     * @param taskId
     * @param id
     * @return
     */
    List<UserWeekResDTO> getTaskUserHours(Long taskId, Long id);
}
