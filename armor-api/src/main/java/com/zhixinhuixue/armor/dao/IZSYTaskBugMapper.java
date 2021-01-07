package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.QueryTaskBugPageReqDTO;
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
     *
     * @param reqDTO 条件
     * @param userId 用户id
     * @author sch
     */
    Page<TaskBugBO> selectPage(@Param("reqDTO") QueryTaskBugPageReqDTO reqDTO, @Param("userId") Long userId);

    /**
     * 新增任务bug
     *
     * @param taskBug bug
     * @author sch
     */
    int insert(TaskBug taskBug);

    /**
     * 根据主键查询
     *
     * @param tbId bugId
     * @author sch
     */
    TaskBug selectById(@Param("tbId") Long tbId);

    /**
     * 更新
     *
     * @param taskBug bug
     * @author sch
     */
    int updateById(TaskBug taskBug);

    /**
     * 查看详情
     *
     * @param tbId bugId
     * @author sch
     */
    TaskBugBO selectDetailById(@Param("tbId") Long tbId);

    /**
     * 查询bug数量
     *
     * @param status 状态
     * @param userId 用户id
     * @author sch
     */
    Integer selectTaskBugNum(@Param("status") Integer status, @Param("userId") Long userId, @Param("taskId") Long taskId, @Param("orgId")Long orgId);

    /**
     * 查询最后一个bug编号
     *
     * @author sch
     */
    Integer selectLastBugNo(@Param("orgId")Long orgId);

    /**
     * 根据条件查询
     *
     * @param status    状态
     * @param handlerId 分派人
     * @param createBy  创建人
     * @author sch
     */
    List<TaskBugBO> selectMyBugList(@Param("status") Integer status, @Param("handlerId") Long handlerId, @Param("createBy") Long createBy);

    /**
     * 查询我的已分派未解决bug数量
     *
     * @param handlerId 分派人
     * @param status    状态
     * @param createBy  创建人
     * @author sch
     */
    Integer selectTaskBugNumByStatus(@Param("handlerId") Long handlerId, @Param("status") Integer status, @Param("createBy") Long createBy);

    /**
     * 查询已分配的或者已解决的
     *
     * @param userId 用户
     * @param status 状态(1: 查询已分配的bug   2:查询已解决的bug)
     * @author sch
     */
    Page<TaskBugBO> selectAssignedOrSolved(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 查询我报告的bug
     *
     * @param userId 用户id
     * @author sch
     */
    Page<TaskBugBO> selectMyReportBug(@Param("userId") Long userId);

    /**
     * bug按状态分类
     *
     * @return List<TaskBugStatusPieBO>
     * @author sch
     */
    List<TaskBugStatusPieBO> selectBugStatusPie(@Param("yearMonth") String yearMonth,@Param("orgId")Long orgId);

    /**
     * bug按类型分类
     *
     * @return List<TaskBugTypePieBO>
     * @author sch
     */
    List<TaskBugTypePieBO> selectBugTypePie(@Param("yearMonth") String yearMonth,@Param("orgId")Long orgId);

    /**
     * 查询分配人的bug统计
     *
     * @param yearMonth 年份
     * @param groupId   业务组id
     * @return List<TaskBugUserHistogramBO>
     * @author sch
     */
    List<TaskBugUserHistogramBO> selectBugHandlerHistogram(@Param("yearMonth") String yearMonth, @Param("groupId") Long groupId);

    /**
     * 查询创建人的bug统计
     *
     * @param yearMonth 年月
     * @return List<TaskBugUserHistogramBO>
     * @author sch
     */
    List<TaskBugUserHistogramBO> selectBugCreatorHistogram(@Param("yearMonth") String yearMonth,@Param("orgId")Long orgId);

    /**
     * 查询任务bug
     *
     * @param yearMonth 年月
     * @param taskName  任务名称
     * @return List<TaskBugBO>
     */
    Page<TaskBugBO> selectTaskStat(@Param("yearMonth") String yearMonth, @Param("taskName") String taskName,@Param("orgId")Long orgId);

    /**
     * 测试阶段相关任务,按当前登录人是否参与排序
     *
     * @param userId 用户id
     * @return List<BugTestTaskBO>
     */
    List<BugTestTaskBO> selectBugTestTaskList(@Param("userId") Long userId,@Param("orgId")Long orgId);
}
