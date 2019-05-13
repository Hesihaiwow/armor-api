package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.PersonTaskBO;
import com.zhixinhuixue.armor.model.bo.TaskUserBO;
import com.zhixinhuixue.armor.model.dto.request.PersonalTaskListReqDTO;
import com.zhixinhuixue.armor.model.pojo.TaskUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IZSYTaskUserMapper {


    /**
     * 根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入任务用户
     * @param record
     * @return
     */
    int insert(TaskUser record);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    TaskUser selectByPrimaryKey(Long id);

    /**
     * 根据主键修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(TaskUser record);

    /**
     * 批量插入taskUser
     * @param taskUserList
     * @return
     */
    int insertList(List<TaskUser> taskUserList);

    /**
     * 删除任务
     * @param taskId
     * @return
     */
    int deleteByTaskId(Long taskId);


    /**
     * 个人任务统计
     * @param personalTaskListReqDTO
     * @return
     */
    List<PersonTaskBO> getPersonalList(PersonalTaskListReqDTO personalTaskListReqDTO);


    /** sch --
     * 查询任务开发人员
     * @param id
     * @return
     */
    List<Long> selectUserByTaskId(@Param("id") Long id);

    /**
     * 删除taskUser
     * @param taskId
     * @param userId
     * @return
     */
    int deleteByTaskIdAndUserId(@Param("taskId")Long taskId, @Param("userId")Long userId);

    /**
     * 根据任务id和用户id查询taskUser
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    TaskUser selectByTaskAndUser(@Param("taskId")Long taskId, @Param("userId")Long userId);


    // -- sch
}