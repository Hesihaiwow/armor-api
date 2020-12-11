package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.AddExtraWorkReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkResDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.service.IZSYExtraWorkService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 加班申请相关接口
 *
 * @author SCH
 * @date 2019/2/14 16:15
 */
@RestController
@RequestMapping("/api/extra-work")
public class ExtraWorkController {
    @Resource
    private IZSYExtraWorkService extraWorkService;

    /**
     * 新增加班申请
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public String addExtraWork(@Valid @RequestBody AddExtraWorkReqDTO reqDTO) {
        extraWorkService.addExtraWork(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改加班申请
     *
     * @param reqDTO
     * @param ewId
     * @return
     */
    @PutMapping("/update/{ewId}")
    public String updateExtraWork(@Valid @RequestBody AddExtraWorkReqDTO reqDTO, @PathVariable("ewId") Long ewId) {
        extraWorkService.updateExtraWork(reqDTO, ewId);
        return ZSYResult.success().build();
    }

    /**
     * 审核通过
     *
     * @param ewId
     * @return
     */
    @PutMapping("/check/{ewId}")
    public String checkExtraWork(@PathVariable("ewId") Long ewId) {
        extraWorkService.checkExtraWork(ewId);
        return ZSYResult.success().build();
    }

    /**
     * 删除加班申请
     *
     * @param ewId
     * @return
     */
    @PutMapping("/delete/{ewId}")
    public String deleteExtraWork(@PathVariable("ewId") Long ewId) {
        extraWorkService.deleteExtraWork(ewId);
        return ZSYResult.success().build();
    }

    /**
     * 查询我的加班申请(待审核)
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/page/wait/{pageNum}")
    public String getWaitExtraWorkByPage(@PathVariable("pageNum") Integer pageNum) {
        PageInfo<ExtraWorkResDTO> pageInfo = extraWorkService.getWaitExtraWorkByPage(pageNum);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * 查询我的加班申请(审核通过)
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/page/access/{pageNum}")
    public String getAccessExtraWorkByPage(@PathVariable("pageNum") Integer pageNum) {
        PageInfo<ExtraWorkResDTO> pageInfo = extraWorkService.getAccessExtraWorkByPage(pageNum);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * 查询审核中的加班申请
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/page/checking/{pageNum}")
    public String getCheckingExtraWorkByPage(@PathVariable("pageNum") Integer pageNum) {
        PageInfo<ExtraWorkResDTO> pageInfo = extraWorkService.getCheckingExtraWorkByPage(pageNum);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * 查询审核通过的加班申请
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/page/checked/{pageNum}")
    public String getCheckedExtraWorkByPage(@PathVariable("pageNum") Integer pageNum) {
        PageInfo<ExtraWorkResDTO> pageInfo = extraWorkService.getCheckedExtraWorkByPage(pageNum);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * 查询加班申请详情
     *
     * @param ewId
     * @return
     */
    @GetMapping("/detail/{ewId}")
    public String getEWorkDetail(@PathVariable("ewId") Long ewId) {
        ExtraWorkDetailResDTO resDTO = extraWorkService.getEWorkDetail(ewId);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 查询我的未完成任务
     *
     * @param userId
     * @return
     */
    @GetMapping("/task-running/{userId}")
    public String getMyRunningTask(@PathVariable("userId") Long userId) {
        List<Task> list = extraWorkService.getMyRunningTaskList(userId);
        return ZSYResult.success().data(list).build();
    }
}
