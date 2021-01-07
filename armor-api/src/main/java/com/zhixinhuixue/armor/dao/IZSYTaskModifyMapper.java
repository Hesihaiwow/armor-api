package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskModifyBO;
import com.zhixinhuixue.armor.model.bo.TaskModifyDetailBO;
import com.zhixinhuixue.armor.model.pojo.TaskModify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/9 10:39
 */
public interface IZSYTaskModifyMapper {

    /**
     * 新增任务修改申请
     * @author sch
     * @param taskModify
     * @return
     */
    int insert(TaskModify taskModify);

    /**
     * 根据主键查找申请
     * @author sch
     * @param id
     * @return
     */
    TaskModify selectById(@Param("id") Long id);


    /**
     * 删除申请
     * @author sch
     * @param id
     * @return
     */
    int deleteById(@Param("id")Long id);

    /**
     * 根据任务和用户查询是否存在审核中的任务修改申请
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    TaskModify selectTaskAndUser(@Param("taskId")Long taskId,@Param("userId")Long userId);

    /**
     * 个人查看待审核任务修改申请
     * @author sch
     * @param userId
     * @return
     */
    List<TaskModifyBO> selectListByUser(@Param("userId")Long userId);

    /**
     * 个人分页查看审核通过的任务修改申请
     * @author sch
     * @param userId
     * @return
     */
    Page<TaskModifyBO> selectPageByUser(@Param("userId")Long userId);

    /**
     * 管理员查看待审核任务修改申请
     * @author sch
     * @return
     */
    List<TaskModifyBO> selectList(@Param("orgId")Long orgId);

    /**
     * 管理员分页查看审核通过任务修改申请
     * @author sch
     * @return
     */
    Page<TaskModifyBO> selectPage(@Param("orgId")Long orgId);

    /**
     * 更新任务修改申请
     * @author sch
     * @param taskModify
     * @return
     */
    int updateById(TaskModify taskModify);

    /**
     * 查看申请详情
     * @author sch
     * @param id
     * @return
     */
    TaskModifyDetailBO selectDetailById(@Param("id") Long id);

    /**
     * 根据task和user查看任务修改申请
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    TaskModify selectByTaskAndUser(@Param("taskId")Long taskId,@Param("userId")Long userId);

    int deleteByTask(@Param("taskId")Long taskId);
}
