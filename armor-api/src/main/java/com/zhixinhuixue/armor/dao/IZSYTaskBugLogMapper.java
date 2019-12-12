package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskBugLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @time 2019/10/14 14:29
 */
public interface IZSYTaskBugLogMapper {

    /**
     * 新增日志
     * @param taskBugLog 日志
     */
    int insert(TaskBugLog taskBugLog);

    /**
     * 根据任务bugId查询日志
     * @param tbId 任务bugId
     */
    List<TaskBugLog> selectLogListByTbId(@Param("tbId") Long tbId);
}
