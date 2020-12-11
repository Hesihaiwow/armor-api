package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugManageAddReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugReqDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineBugDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineBugNumResDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineBugResDTO;
import com.zhixinhuixue.armor.service.IZSYBugService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Bug处理接口
 * Created by Lang on 2017/9/4 0004.
 */
@RequestMapping("/api/bug")
@RestController
public class ZSYBugController extends ZSYController {

    @Resource
    private IZSYBugService bugService;

    /**
     * bug处理结果表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public String getBug(BugListReqDTO bugListReqDTO) {
        return ZSYResult.success().data(bugService.getBugList(bugListReqDTO)).build();
    }

    /**
     * 添加bug处理
     *
     * @return
     */
    @PostMapping(value = "/add")
    public String addBug(@Valid @RequestBody BugReqDTO bugReqDTO) {
        bugService.addBug(bugReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 更新bug处理
     *
     * @return
     */
    @PutMapping(value = "/update/{id}")
    public String update(@Valid @RequestBody BugReqDTO bugReqDTO, @PathVariable("id") Long id) {
        bugService.updateBug(id, bugReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * bug处理详情
     *
     * @return
     */
    @GetMapping(value = "/{id}")
    public String getBugDetail(@PathVariable("id") Long id) {
        return bugService.getBugDetail(id).build();
    }

    /**
     * 删除Bug处理记录
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public String deleteProject(@PathVariable("id") Long id) {
        bugService.deleteBug(id);
        return ZSYResult.success().build();
    }

    // sch --

    /**
     * 添加bug处理
     *
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/add-bug")
    public String addNewBug(@Valid @RequestBody BugManageAddReqDTO reqDTO) {
        bugService.addNewBug(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 分页查询线上bug(新数据)
     *
     * @param bugListReqDTO
     * @return
     */
    @PostMapping(value = "/new/page")
    public String getBugManagePage(@RequestBody BugListReqDTO bugListReqDTO) {
        PageInfo<OnlineBugResDTO> pageInfo = bugService.getBugManagePage(bugListReqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * 分页查询线上bug(旧数据)
     *
     * @param bugListReqDTO
     * @return
     */
    @PostMapping(value = "/old/page")
    public String getOldBugManagePage(@RequestBody BugListReqDTO bugListReqDTO) {
        PageInfo<OnlineBugResDTO> pageInfo = bugService.getOldBugManagePage(bugListReqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * bug处理详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/detail/{id}")
    public String getOnlineBugDetail(@PathVariable("id") Long id) {
        OnlineBugDetailResDTO resDTO = bugService.getOnlineBugDetail(id);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 更新bug处理
     *
     * @param reqDTO
     * @param id
     * @return
     */
    @PutMapping(value = "/update-bug/{id}")
    public String updateOnlineBug(@Valid @RequestBody BugManageAddReqDTO reqDTO, @PathVariable Long id) {
        bugService.updateOnlineBug(reqDTO, id);
        return ZSYResult.success().build();
    }

    /**
     * 查询各个分类的线上bug数量
     *
     * @param bugListReqDTO
     * @return
     */
    @PostMapping(value = "/num/type")
    public String getDiffTypeBugNum(@RequestBody BugListReqDTO bugListReqDTO) {
        OnlineBugNumResDTO resDTO = bugService.getDiffTypeBugNum(bugListReqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 更新老数据状态为已解决
     *
     * @return
     */
    @PutMapping("/update/status")
    public String updateStatus() {
        bugService.updateStatus();
        return ZSYResult.success().build();
    }

    /**
     * 各个系统bug分类柱状图
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/system/histogram")
    public String getSystemHistogram(@RequestBody BugListReqDTO reqDTO) {
        return ZSYResult.success().data(bugService.getSystemHistogram(reqDTO)).build();
    }

    /**
     * 用户bug分类柱状图
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/user/histogram")
    public String getUserBugHistogram(@RequestBody BugListReqDTO reqDTO) {
        return ZSYResult.success().data(bugService.getUserBugHistogram(reqDTO)).build();
    }

    /**
     * 导入bug
     *
     * @param uploadFile
     * @return
     */
    @PostMapping("/import")
    public String importBug(@RequestParam(value = "uploadFile") MultipartFile uploadFile) {
        bugService.importBug(uploadFile);
        return ZSYResult.success().build();
    }
    // -- sch

}
