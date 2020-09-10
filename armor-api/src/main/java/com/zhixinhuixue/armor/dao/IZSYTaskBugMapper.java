package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.TaskBugBO;
import com.zhixinhuixue.armor.model.bo.TaskBugStatusPieBO;
import com.zhixinhuixue.armor.model.bo.TaskBugTypePieBO;
import com.zhixinhuixue.armor.model.bo.TaskBugUserHistogramBO;
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
     * @author sch
     * @param reqDTO 条件
     * @param userId 用户id
     */
    Page<TaskBugBO> selectPage(@Param("reqDTO") QueryTaskBugPageReqDTO reqDTO,@Param("userId") Long userId);

    /**
     * 新增任务bug
     * @author sch
     * @param taskBug bug
     */
    int insert(TaskBug taskBug);

    /**
     * 根据主键查询
     * @author sch
     * @param tbId bugId
     */
    TaskBug selectById(@Param("tbId") Long tbId);

    /**
     * 更新
     * @author sch
     * @param taskBug bug
     */
    int updateById(TaskBug taskBug);

    /**
     * 查看详情
     * @author sch
     * @param tbId bugId
     */
    TaskBugBO selectDetailById(@Param("tbId") Long tbId);

    /**
     * 查询bug数量
     * @author sch
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
     * @author sch
     * @param status 状态
     * @param handlerId 分派人
     * @param createBy 创建人
     */
    List<TaskBugBO> selectMyBugList(@Param("status") Integer status, @Param("handlerId") Long handlerId, @Param("createBy") Long createBy);

    /**
     * 查询我的已分派未解决bug数量
     * @author sch
     * @param handlerId 分派人
     * @param status 状态
     * @param createBy 创建人
     */
    Integer selectTaskBugNumByStatus(@Param("handlerId")Long handlerId, @Param("status")Integer status, @Param("createBy")Long createBy);

    /**
     * 查询已分配的或者已解决的
     * @author sch
     * @param userId 用户
     * @param status 状态(1: 查询已分配的bug   2:查询已解决的bug)
     */
    Page<TaskBugBO> selectAssignedOrSolved(@Param("userId")Long userId, @Param("status")Integer status);

    /**
     * 查询我报告的bug
     * @author sch
     * @param userId 用户id
     */
    Page<TaskBugBO> selectMyReportBug(@Param("userId")Long userId);

    /**
     * bug按状态分类
     * @author sch
     * @return List<TaskBugStatusPieBO>
     */
    List<TaskBugStatusPieBO> selectBugStatusPie(@Param("yearMonth")String yearMonth);

    /**
     * bug按类型分类
     * @author sch
     * @return List<TaskBugTypePieBO>
     */
    List<TaskBugTypePieBO> selectBugTypePie(@Param("yearMonth")String yearMonth);

    /**
     * 查询分配人的bug统计
     * @author sch
     * @param yearMonth 年份
     * @return List<TaskBugUserHistogramBO>
     */
    List<TaskBugUserHistogramBO> selectBugHandlerHistogram(@Param("yearMonth") String yearMonth);

    /**
     * 查询创建人的bug统计
     * @author sch
     * @param yearMonth 年月
     * @return List<TaskBugUserHistogramBO>
     */
    List<TaskBugUserHistogramBO> selectBugCreatorHistogram(@Param("yearMonth") String yearMonth);

    /**
     * 查询任务bug
     * @param yearMonth 年月
     * @param taskName 任务名称
     * @return List<TaskBugBO>
     */
    Page<TaskBugBO> selectTaskStat(@Param("yearMonth") String yearMonth, @Param("taskName") String taskName);
}
