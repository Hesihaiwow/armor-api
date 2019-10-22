package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

/**
 * @author SCH
 * @date 2019/2/14 17:21
 */
public interface IZSYExtraWorkMapper {

    /**
     * 新增加班申请
     * @param extraWork
     * @return
     */
    int addExtraWork(@Param("extraWork") ExtraWork extraWork);

    /**
     * 新增加班关联任务
     * @param list
     * @return
     */
    int addExtraWorkTaskByList(@Param("list") List<ExtraWorkTask> list);

    /**
     * 查询我的加班申请(待审核)
     * @param userId
     * @return
     */
    Page<ExtraWork> selectWaitExtraWorkByPage(@Param("userId") Long userId);

    /**
     * 根据ewId  查询关联任务集合
     * @param ewId
     * @return
     */
    List<Task> selectTasksByEwId(@Param("ewId") Long ewId);

    /**
     * 查询我的加班申请(审核通过)
     * @param userId
     * @return
     */
    Page<ExtraWork> selectAccessExtraWorkByPage(@Param("userId") Long userId);

    /**
     * 查询审核中的加班申请
     * @return
     */
    Page<ExtraWork> selectCheckingExtraWorkByPage();

    /**
     * 查询审核通过的加班申请
     * @return
     */
    Page<ExtraWork> selectCheckedExtraWorkByPage();

    /**
     * 根据id查加班申请
     * @param ewId
     * @return
     */
    ExtraWork selectById(@Param("ewId") Long ewId);

    /**
     * 更新加班申请
     * @param ewId
     * @return
     */
    int updateExtraWorkById(@Param("extraWork") ExtraWork extraWork, @Param("ewId") Long ewId);

    /**
     * 根据ewId删除加班关联任务
     * @param ewId
     * @return
     */
    int deleteExtraWorkTaskByEwId(@Param("ewId") Long ewId);

    /**
     * 根据用户和时间段查询加班申请
     * @param userId
     * @param today0
     * @param today23
     * @return
     */
    ExtraWork selectByUserAndTimeRange(@Param("userId") Long userId, @Param("today0") Date today0, @Param("today23") Date today23);

    /**
     * 查询某天某人的加班申请时间
     * @param userId
     * @param today0
     * @param today23
     * @return
     */
    Float selectEWorkHours(@Param("userId") Long userId, @Param("today0") Date today0, @Param("today23") Date today23);

    /**
     * 查询10月份之间  审核通过的加班申请
     * @param beginStr
     * @param endStr
     * @return
     */
    List<ExtraWork> selectListByTime(@Param("beginStr") String beginStr,@Param("endStr")  String endStr);
}
