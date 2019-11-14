package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddUserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.service.IZSYUserTaskIntegralService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYRestHoursType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author sch
 * @DATE 2019/9/9 9:44
 */
@Api(value = "用户任务积分接口",description="用户任务积分接口",tags = "/user_task_integral")
@RequestMapping("/api/user-task-integral")
@RestController
public class UserTaskIntegralController {
    @Autowired
    private IZSYUserTaskIntegralService userTaskIntegralService;

//    @ApiOperation("统计7月之后的没有功能点的多人任务积分")
//    @PostMapping("/multi-task/update")
    public String updateAfterJuly(){
        userTaskIntegralService.updateAfterJuly();
        return ZSYResult.success().build();
    }

//    @ApiOperation("统计7月之后的没有任务级别的个人任务积分")
//    @PostMapping("/private-task/update")
    public String updateAfterJulyPrivate(){
        userTaskIntegralService.updateAfterJulyPrivate();
        return ZSYResult.success().build();
    }

//    @ApiOperation("统计7月之后的有功能点的且已评价多人任务积分(已完成,没结束)")
//    @PostMapping("/multi-task/with-evaluation/update")
    public String updateAfterJulyWithEvaluation(){
        userTaskIntegralService.updateAfterJulyWithEvaluation();
        return ZSYResult.success().build();
    }

//    @ApiOperation("统计7月之后的没有功能点的且已评价多人任务积分(已完成,没结束)")
//    @PostMapping("/multi-task/without-evaluation/update")
    public String updateAfterJulyWithoutEvaluation(){
        userTaskIntegralService.updateAfterJulyWithoutEvaluation();
        return ZSYResult.success().build();
    }


//    @ApiOperation("统计7月之后的有功能点的已完成的多人任务积分")
//    @PostMapping("/multi-task/finished/update")
    public String updateAfterJulyFinished(){
        userTaskIntegralService.updateAfterJulyFinished();
        return ZSYResult.success().build();
    }

//    @ApiOperation("统计7月之后的有任务级别的而且已经完成的个人任务积分")
//    @PostMapping("/private-task/finished/update")
    public String updateAfterJulyPrivateFinished(){
        userTaskIntegralService.updateAfterJulyPrivateFinished();
        return ZSYResult.success().build();
    }

    @ApiOperation("个人积分信息")
    @GetMapping("/personal")
    public String getPersonalIntegral(){
        return ZSYResult.success().data(userTaskIntegralService.getPersonalIntegral()).build();
    }

    @ApiOperation("查看积分列表")
    @PostMapping("/rank")
    public String getIntegralRank(@RequestBody UserTaskIntegralReqDTO reqDTO){
        return ZSYResult.success().data(userTaskIntegralService.getIntegralRank(reqDTO)).build();
    }

    @ApiOperation("获取积分列数")
    @PostMapping("/count")
    public String getIntegralCount(@RequestBody UserTaskIntegralReqDTO reqDTO){
        return ZSYResult.success().data(userTaskIntegralService.getIntegralCount(reqDTO)).build();
    }

    @ApiOperation("获取用户积分历史")
    @PostMapping("/history")
    public String getIntegralHistoryPage(@RequestBody UserTaskIntegralReqDTO reqDTO){
        return ZSYResult.success().data(userTaskIntegralService.getIntegralHistoryPage(reqDTO)).build();
    }

    @ApiOperation("添加积分")
    @PostMapping("/add")
    public String addIntegral(@Valid @RequestBody AddUserTaskIntegralReqDTO reqDTO){
        userTaskIntegralService.addIntegral(reqDTO);
        return ZSYResult.success().build();
    }


    @ApiOperation("更新7月之后,没有任务级别的个人任务,新增级别")
    @PutMapping("/private-task/add-level")
    public String privateTaskAddLevel(){
        userTaskIntegralService.privateTaskAddLevel();
        return ZSYResult.success().build();
    }

    @ApiOperation("更新7月之后,没有任务级别的多人任务,根据工时新增级别")
    @PutMapping("/multi-task/add-level")
    public String multiTaskAddLevel(){
        userTaskIntegralService.multiTaskAddLevel();
        return ZSYResult.success().build();
    }

    @ApiOperation("计算7月之后完成的个人任务积分")
    @PutMapping("/private-task/finished/count")
    public String countPrivateIntegral(){
        userTaskIntegralService.countPrivateIntegral();
        return ZSYResult.success().build();
    }

    @ApiOperation("计算7月之后结束的多人任务积分")
    @PutMapping("/multi-task/finished/count")
    public String countMultiIntegral(){
        userTaskIntegralService.countMultiIntegral();
        return ZSYResult.success().build();
    }

    @ApiOperation("计算7月之后完成的未结束的多人任务积分")
    @PutMapping("/multi-task/completed/count")
    public String countMultiCompletedIntegral(){
        userTaskIntegralService.countMultiCompletedIntegral();
        return ZSYResult.success().build();
    }
}
