package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskModifyUserWeek;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/9 11:01
 */
public interface IZSYTaskModifyUserWeekMapper {

    /**
     * 新增任务修改周工时分配
     * @author sch
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<TaskModifyUserWeek> list);

    /**
     * 根据tmId删除周工作量分配
     * @param id
     * @return
     */
    int deleteByTmId(@Param("id")Long id);

    /**
     * 根据tmID查询周工时分配
     * @param tmId
     * @return
     */
    List<TaskModifyUserWeek> selectByTmId(@Param("tmId")Long tmId);

    int deleteByTask(@Param("taskId")Long taskId);
}
