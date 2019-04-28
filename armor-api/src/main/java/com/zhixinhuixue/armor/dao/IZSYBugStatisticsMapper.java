package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.pojo.MantisBugStatistics;
import com.zhixinhuixue.armor.model.pojo.MantisCategory;
import com.zhixinhuixue.armor.model.pojo.MantisUser;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by sch on 2019/04/17
 */
public interface IZSYBugStatisticsMapper {

    /**
     * 查询上次导入时最后一天记录的bugId
     * @return
     */
    Integer selectLastImportBugId();

    /**
     * 批量导入
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<MantisBugStatistics> list);

    /**
     * 查询已存在的bugId集合
     * @return
     */
    List<Integer> selectBugIdList();

    /**
     * 清空原来的mantis_user
     */
    void deleteAllUsers();

    /**
     * 批量导入用户
     * @param userList
     * @return
     */
    int insertUserBatch(@Param("userList")List<MantisUser> userList);

    /**
     * 清空原来的mantis_category
     */
    void deleteAllCategory();

    /**
     * 批量导入分类
     * @param categoryList
     * @return
     */
    int insertCategoryBatch(@Param("categoryList")List<MantisCategory> categoryList);

    /**
     * 查询反馈系统(分类)
     * @author sch
     * @return
     */
    List<MantisCategory> selectCategoryList();

    /**
     * 按年月查询测试人员bug统计情况
     * @author sch
     * @return
     */
    List<MantisBugStatisticsBO> selectBugStatsGroupByUser(@Param("beginTime")Long beginTime,@Param("endTime")Long endTime);

    /**
     * 查询测试人员周bug数量图表
     * @param beginTime
     * @param endTime
     * @return
     */
    List<MantisBugUserWeekBO> selectBugWeekGroupByUser(@Param("beginTime")Long beginTime,@Param("endTime")Long endTime);

    /**
     * 查询线上bug分类数量
     * @param sysUserId
     * @param beginTime
     * @param endTime
     * @return
     */
    List<OnlineBugCategoryNumBO> selectBugCategoryNum(@Param("sysUserId")Long sysUserId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 查询测试人员线上bug月数量图表
     * @param beginTime
     * @param endTime
     * @return
     */
    List<OnlineBugUserMonthBO> selectBugMonthGroupByUser(@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 按任务统计bug
     * @param beginTime
     * @param endTime
     * @return
     */
    Page<MantisBugGroupByTaskBO> selectBugStatsGroupByTask(@Param("beginTime")Long beginTime, @Param("endTime")Long endTime);

    /**
     * 清空原来的bug数据
     */
    void deleteAllBugStats();
}
