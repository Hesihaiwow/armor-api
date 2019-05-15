package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.MantisBugQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.MantisBugWeekQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/4/17 17:38
 */
public interface IZSYMantisBugService {

    /**
     * @author sch
     * 查询mantis中bug信息并导入到任务系统
     */
    void importMantisBug(Integer projectId);

    /**
     * 查询mantis中的bug信息导出Excel
     * @author sch
     * @param projectId
     */
    List<String> exportMantisBug(Integer projectId);

    /**
     * @author sch
     * 查询mantis用户信息
     * @return
     */
    List<MantisBugUserBaseInfoResDTO> getMantisUserList();

    /**
     * 查询mantis反馈系统信息
     * @author sch
     * @return
     */
    List<MantisCategoryResDTO> getMantisCategory();

    /**
     * 查询反馈系统(分类)
     * @author sch
     * @return
     */
    List<MantisCategoryResDTO> getCategoryList();

    /**
     * 按年月查询测试人员bug统计情况
     * @author sch
     * @param reqDTO
     * @return
     */
    List<MantisBugStatisticsResDTO> getBugStatsGroupByUser(MantisBugQueryReqDTO reqDTO);

    /**
     * 查询测试人员周bug数量图表
     * @author sch
     * @return
     */
    List<MantisBugUserWeekResDTO> getBugMonthGroupByUser(MantisBugWeekQueryReqDTO reqDTO);

    /**
     * 查询测试人员线上bug月数量图表
     * @param reqDTO
     * @return
     */
    List<OnlineBugUserMonthResDTO> getOnlineBugGroupByUser(MantisBugWeekQueryReqDTO reqDTO);

    /**
     * 按任务统计bug
     * @param reqDTO
     * @return
     */
    PageInfo<MantisBugGroupByTaskResDTO> getBugStatsGroupByTask(MantisBugQueryReqDTO reqDTO);
}
