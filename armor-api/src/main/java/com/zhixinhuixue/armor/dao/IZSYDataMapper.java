package com.zhixinhuixue.armor.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/1/10 13:04
 */
public interface IZSYDataMapper {

    /**
     * 查询年度需求  来自学管端
     * @author sch
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectAnnualFeedbackFromCoach(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    /**
     * 查询年度需求  来自其他
     * @author sch
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectAnnualFeedbackOhter(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    /**
     * 查询年度 多人任务数
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectAnnualMultiTaskNum(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    /**
     * 查询年度 单人任务数
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer selectAnnualSingleTaskNum(@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);
}
