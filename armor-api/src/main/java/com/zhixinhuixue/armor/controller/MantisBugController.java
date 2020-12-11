package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.MantisBugQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.MantisBugWeekQueryReqDTO;
import com.zhixinhuixue.armor.service.IZSYMantisBugService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 测试Bug处理接口
 *
 * @author sch
 * @DATE 2019/4/17 17:36
 */
@RequestMapping("/api/mantis-bug")
@RestController
public class MantisBugController {

    @Resource
    private IZSYMantisBugService mantisBugService;

    /**
     * 查询mantis中bug信息并导入到任务系统
     *
     * @param projectId
     * @return
     */
    @PostMapping("/import/{projectId}")
    public String importMantisBug(@PathVariable("projectId") Integer projectId) {
        mantisBugService.importMantisBug(projectId);
        return ZSYResult.success().build();
    }

    /**
     * 查询反馈系统(分类)
     *
     * @return
     */
    @GetMapping("/category/list")
    public String getCategoryList() {
        return ZSYResult.success().data(mantisBugService.getCategoryList()).build();
    }

    /**
     * 按年月查询测试人员bug统计情况
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/stats/user")
    public String getBugStatsGroupByUser(@RequestBody MantisBugQueryReqDTO reqDTO) {
        return ZSYResult.success().data(mantisBugService.getBugStatsGroupByUser(reqDTO)).build();
    }

    /**
     * 查询测试人员周bug数量图表
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/bug-week/user")
    public String getBugWeekGroupByUser(@RequestBody MantisBugWeekQueryReqDTO reqDTO) {
        return ZSYResult.success().data(mantisBugService.getBugMonthGroupByUser(reqDTO)).build();
    }

    /**
     * 查询测试人员线上bug饼型图
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/online-bug/user")
    public String getOnlineBugGroupByUser(@RequestBody MantisBugWeekQueryReqDTO reqDTO) {
        return ZSYResult.success().data(mantisBugService.getOnlineBugGroupByUser(reqDTO)).build();
    }

    /**
     * 查询开发人员解决bug饼形图
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/online-bug/develop")
    public String getOnlineBugGroupByDeveloper(@RequestBody MantisBugWeekQueryReqDTO reqDTO) {
        return ZSYResult.success().data(mantisBugService.getOnlineBugGroupByDeveloper(reqDTO)).build();
    }

    /**
     * 按任务统计bug
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/stats/task")
    public String getBugStatsGroupByTask(@RequestBody MantisBugQueryReqDTO reqDTO) {
        return ZSYResult.success().data(mantisBugService.getBugStatsGroupByTask(reqDTO)).build();
    }

    /**
     * 导出数据到Excel
     *
     * @param projectId
     * @return
     */
    @GetMapping("/export/{projectId}")
    public String exportMantisBug(@PathVariable("projectId") Integer projectId) {
        return ZSYResult.success().data(mantisBugService.exportMantisBug(projectId)).build();
    }

    /**
     * 导入Excel到数据库
     *
     * @param uploadFile
     * @return
     */
    @PostMapping("/import")
    public String importExcel(MultipartFile uploadFile) {
        mantisBugService.importExcel(uploadFile);
        return ZSYResult.success().build();
    }

    /**
     * 导入user信息到数据库
     *
     * @param uploadFile
     * @return
     */
    @PostMapping("/import/user")
    public String importUser(MultipartFile uploadFile) {
        mantisBugService.importUser(uploadFile);
        return ZSYResult.success().build();
    }

    /**
     * 导入category信息到数据库
     *
     * @param uploadFile
     * @return
     */
    @PostMapping("/import/category")
    public String importCategory(MultipartFile uploadFile) {
        mantisBugService.importCategory(uploadFile);
        return ZSYResult.success().build();
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    @GetMapping("/env")
    public String getEnvironment() {
        return ZSYResult.success().data(mantisBugService.getEnvironment()).build();
    }
}
