package com.zhixinhuixue.armor.service;

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
    Double getUserWeekHours(Long id,int weekNumber, int year);
}
