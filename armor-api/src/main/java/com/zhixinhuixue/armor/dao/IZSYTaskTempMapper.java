package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskTempBO;
import com.zhixinhuixue.armor.model.pojo.TaskTemp;
import com.zhixinhuixue.armor.model.pojo.UserWeek;
import com.zhixinhuixue.armor.model.pojo.UserWeekTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/2 9:46
 */
public interface IZSYTaskTempMapper {

    /**
     * 根据任务id和userId查询 临时任务
     * @param userId
     * @param taskId
     * @return
     */
    TaskTemp selectByUserAndTask(@Param("userId") Long userId,@Param("taskId") Long taskId);

    /**
     * 新增任务
     * @param taskTemp
     * @return
     */
    int insertTaskTemp(TaskTemp taskTemp);

    /**
     * 批量插入周工作量(临时)
     * @param userWeekList
     * @return
     */
    int insertUserWeekTempBatch(@Param("userWeekList") List<UserWeekTemp> userWeekList);

    /**
     * 删除原有的userWeekTemp
     * @param userId
     * @param taskId
     * @return
     */
    int deleteUserWeekTempByUserAndTask(@Param("userId") Long userId,@Param("taskId") Long taskId);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    TaskTemp selectById(@Param("id")Long id);

    /**
     * 修改任务(临时)
     * @param existTaskTemp
     * @return
     */
    int updateTaskTemp(TaskTemp existTaskTemp);

    /**
     * 根据userId和taskId查询周工作量
     * @param userId
     * @param taskId
     * @return
     */
    List<UserWeekTemp> selectUserWeekTempByUserAndTask(@Param("userId") Long userId,@Param("taskId") Long taskId);

    /**
     * 删除周工作量
     * @param idList
     * @return
     */
    int updateUserWeekTempBatch(List<Long> idList);

    /**
     * 个人分页查看任务
     * @param userId
     * @param reviewStatus
     * @return
     */
    Page<TaskTempBO> selectPersonalTaskTempPage(@Param("userId")Long userId,@Param("reviewStatus") Integer reviewStatus);

    /**
     * 管理员分页查看任务
     * @param reviewStatus
     * @return
     */
    Page<TaskTempBO> selectTaskTempPage(@Param("reviewStatus")Integer reviewStatus);

    /**
     * 审核userWeekTemp
     * @param uwtIds
     * @return
     */
    int checkUserWeekTempBatch(List<Long> uwtIds);
}
