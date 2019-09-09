package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.service.IZSYUserTaskIntegralService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation("统计7月之后的没有功能点的多人任务积分")
    @PostMapping("/multi-task/update")
    public String updateAfterJuly(){
        userTaskIntegralService.updateAfterJuly();
        return ZSYResult.success().build();
    }

    @ApiOperation("统计7月之后的没有任务级别的个人任务积分")
    @PostMapping("/private-task/update")
    public String updateAfterJulyPrivate(){
        userTaskIntegralService.updateAfterJulyPrivate();
        return ZSYResult.success().build();
    }

    @ApiOperation("统计7月之后的有功能点的已完成的多人任务积分")
    @PostMapping("/multi-task/finished/update")
    public String updateAfterJulyFinished(){
        userTaskIntegralService.updateAfterJulyFinished();
        return ZSYResult.success().build();
    }

    @ApiOperation("统计7月之后的有任务级别的而且已经完成的个人任务积分")
    @PostMapping("/private-task/finished/update")
    public String updateAfterJulyPrivateFinished(){
        userTaskIntegralService.updateAfterJulyPrivateFinished();
        return ZSYResult.success().build();
    }

    @ApiOperation("个人积分信息")
    @GetMapping("/personal")
    public String getPersonalIntegral(){
        return ZSYResult.success().data(userTaskIntegralService.getPersonalIntegral()).build();
    }
}
