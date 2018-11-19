package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.StatsUserWeekBO;
import com.zhixinhuixue.armor.model.bo.TaskExpandBO;
import com.zhixinhuixue.armor.model.pojo.TaskExpand;
import com.zhixinhuixue.armor.model.pojo.TaskLog;
import com.zhixinhuixue.armor.model.pojo.UserWeek;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/11/28 0028.
 */
public interface IZSYTaskExpandMapper {

    int insert(TaskExpand expand);

    TaskExpand selectById(Long teId);

    TaskExpandBO selectExpandDetail(Long teId);

    Page<TaskExpandBO> getExpandList(@Param("status") int status, @Param("userId") Long userId);

    /**
     * 审核
     * @param id
     * @return
     */
    int reviewStatus(@Param("id") Long id,@Param("status")  Integer status);

    /**
     * 查找处于延长申请的任务次数
     * @param taskId
     * @return
     */
    int findIsExpand(@Param("taskId") Long taskId,@Param("userId")  Long userId);

    /**
     * 延长任务时间
     * @param expandHours
     * @return
     */
    int updateTaskHours(@Param("expandHours") BigDecimal expandHours, @Param("endTime") Date endTime, @Param("taskId")Long taskId,@Param("userId")Long userId);
}
