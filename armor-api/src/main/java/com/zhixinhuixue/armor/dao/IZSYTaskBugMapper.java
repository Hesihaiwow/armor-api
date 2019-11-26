package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskBugBO;
import com.zhixinhuixue.armor.model.dto.request.QueryTaskBugPageReqDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskBug;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @time 2019/10/14 10:23
 */
public interface IZSYTaskBugMapper {

    /**
     * 分页查询任务bug
     * @param reqDTO 条件
     * @param userId 用户id
     */
    Page<TaskBugBO> selectPage(@Param("reqDTO") QueryTaskBugPageReqDTO reqDTO,@Param("userId") Long userId);

    /**
     * 新增任务bug
     * @param taskBug bug
     */
    int insert(TaskBug taskBug);

    /**
     * 根据主键查询
     * @param tbId bugId
     */
    TaskBug selectById(@Param("tbId") Long tbId);

    /**
     * 更新
     * @param taskBug bug
     */
    int updateById(TaskBug taskBug);

    /**
     * 查看详情
     * @param tbId bugId
     */
    TaskBugBO selectDetailById(@Param("tbId") Long tbId);

    /**
     * 查询bug数量
     * @param status 状态
     * @param userId 用户id
     */
    Integer selectTaskBugNum(@Param("status")Integer status, @Param("userId")Long userId, @Param("taskId")Long taskId);

    /**
     * 查询最后一个bug编号
     * @author sch
     */
    Integer selectLastBugNo();

    /**
     * 根据条件查询
     * @param status
     * @param handlerId
     * @param createBy
     */
    List<TaskBugBO> selectMyBugList(@Param("status") Integer status, @Param("handlerId") Long handlerId, @Param("createBy") Long createBy);
}
