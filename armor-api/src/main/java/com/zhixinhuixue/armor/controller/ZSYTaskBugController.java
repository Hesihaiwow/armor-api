package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugStatReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.QueryTaskBugPageReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskBugService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 任务bug相关接口
 * @author sch
 * @time 2019/10/14 10:21
 */
@RestController
@RequestMapping("/api/task-bug")
public class ZSYTaskBugController {
    @Resource
    private IZSYTaskBugService taskBugService;

    /**
     * 分页查看任务bug
     * @param reqDTO
     * @return
     */
    @PostMapping("/page")
    public String getTaskBugPage(@Valid @RequestBody QueryTaskBugPageReqDTO reqDTO){
        return ZSYResult.success().data(taskBugService.getTaskBugPage(reqDTO)).build();
    }

    /**
     * 新增任务bug
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public String addTaskBug(@Valid @RequestBody AddTaskBugReqDTO reqDTO){
        taskBugService.addTaskBug(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改
     * @param reqDTO
     * @return
     */
    @PutMapping("/edit")
    public String updateTaskBug(@Valid @RequestBody EditTaskBugReqDTO reqDTO){
        taskBugService.updateTaskBug(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除
     * @param tbId
     * @return
     */
    @DeleteMapping("/delete/{tbId}")
    public String deleteTaskBug(@PathVariable("tbId")Long tbId){
        taskBugService.deleteTaskBug(tbId);
        return ZSYResult.success().build();
    }

    /**
     * 查看详情
     * @param tbId
     * @return
     */
    @GetMapping("/detail/{tbId}")
    public String getTaskBugDetail(@PathVariable("tbId")Long tbId){
        return ZSYResult.success().data(taskBugService.getTaskBugDetail(tbId)).build();
    }

    /**
     * 查询任务人员
     * @param taskId
     * @return
     */
    @GetMapping("/users/{taskId}")
    public String getTaskUsers(@PathVariable("taskId")Long taskId){
        return ZSYResult.success().data(taskBugService.getTaskUsers(taskId)).build();
    }

    /**
     * 查询bug报告员
     * @return
     */
    @GetMapping("/users/report")
    public String getBugReporters(){
        return ZSYResult.success().data(taskBugService.getBugReporters()).build();
    }

    /**
     * 查询bug分派员
     * @return
     */
    @GetMapping("/users/handle")
    public String getBugHandlers(){
        return ZSYResult.success().data(taskBugService.getBugHandlers()).build();
    }

    /**
     * 查询任务数量
     * @param reqDTO
     * @return
     */
    @PostMapping("/num")
    public String getTaskBugNum(@Valid @RequestBody QueryTaskBugPageReqDTO reqDTO){
        return ZSYResult.success().data(taskBugService.getTaskBugNum(reqDTO)).build();
    }

    /**
     * 个人主页显示bug分页
     * @param pageNum
     * @return
     */
    @GetMapping("/personal/page/{pageNum}")
    public String getPersonalBugPage(@PathVariable("pageNum")Integer pageNum){
        return ZSYResult.success().data(taskBugService.getPersonalBugPage(pageNum)).build();
    }

    /**
     * 查询已经产生bug的任务
     * @return
     */
    @GetMapping("/task/ready")
    public String getReadyTasks(){
        return ZSYResult.success().data(taskBugService.getReadyTasks()).build();
    }

    /**
     * 测试相关阶段的任务
     * @return
     */
    @GetMapping("/task/testing")
    public String getTaskTesting(){
        return ZSYResult.success().data(taskBugService.getTaskTesting()).build();
    }


    /**
     * 我的bug视图
     * @return
     */
    @GetMapping("/my-list")
    public String getMyBugList(){
        return ZSYResult.success().data(taskBugService.getMyBugList()).build();
    }

    /**
     * 查看bug列表
     * @param userId
     * @param status
     * @param pageNum
     * @return
     */
    @GetMapping("/page/{userId}/{status}/{pageNum}")
    public String getCustomizedPage(@PathVariable("userId")Long userId,@PathVariable("status")Integer status,
                                    @PathVariable("pageNum")Integer pageNum){
        return ZSYResult.success().data(taskBugService.getCustomizedPage(userId,status,pageNum)).build();
    }

    /**
     * bug状态分类饼形图
     * @param reqDTO 查询条件
     */
    @PostMapping("/status-pie")
    public String getBugStatusPie(@RequestBody BugStatReqDTO reqDTO){
        return ZSYResult.success().data(taskBugService.getBugStatusPie(reqDTO)).build();
    }

    /**
     * bug类型分类饼形图
     * @param reqDTO 查询条件
     */
    @PostMapping("/type-pie")
    public String getBugTypePie(@RequestBody BugStatReqDTO reqDTO){
        return ZSYResult.success().data(taskBugService.getBugTypePie(reqDTO)).build();
    }

    /**
     * 用户bug柱状图
     * @param reqDTO 查询条件
     */
    @PostMapping("/user-histogram")
    public String getBugUserHistogram(@RequestBody BugStatReqDTO reqDTO){
        return ZSYResult.success().data(taskBugService.getBugUserHistogram(reqDTO)).build();
    }

    /**
     * 任务bug表
     * @param reqDTO 参数
     */
    @PostMapping("/task-stat")
    public String getTaskBugStat(@RequestBody BugStatReqDTO reqDTO){
        return ZSYResult.success().data(taskBugService.getTaskBugStat(reqDTO)).build();
    }
}
