package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddUserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.service.IZSYUserTaskIntegralService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户任务积分接口
 * @author sch
 * @DATE 2019/9/9 9:44
 */
@RequestMapping("/api/user-task-integral")
@RestController
public class UserTaskIntegralController {
    @Resource
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

    /**
     * 个人积分信息
     * @return
     */
    @GetMapping("/personal")
    public String getPersonalIntegral(){
        return ZSYResult.success().data(userTaskIntegralService.getPersonalIntegral()).build();
    }

    /**
     * 查看积分列表
     * @param reqDTO
     * @return
     */
    @PostMapping("/rank")
    public String getIntegralRank(@RequestBody UserTaskIntegralReqDTO reqDTO){
        return ZSYResult.success().data(userTaskIntegralService.getIntegralRank(reqDTO)).build();
    }

    /**
     * 获取积分列数
     * @param reqDTO
     * @return
     */
    @PostMapping("/count")
    public String getIntegralCount(@RequestBody UserTaskIntegralReqDTO reqDTO){
        return ZSYResult.success().data(userTaskIntegralService.getIntegralCount(reqDTO)).build();
    }

    /**
     * 获取用户积分历史
     * @param reqDTO
     * @return
     */
    @PostMapping("/history")
    public String getIntegralHistoryPage(@RequestBody UserTaskIntegralReqDTO reqDTO){
        return ZSYResult.success().data(userTaskIntegralService.getIntegralHistoryPage(reqDTO)).build();
    }

    /**
     * 添加积分
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public String addIntegral(@Valid @RequestBody AddUserTaskIntegralReqDTO reqDTO){
        userTaskIntegralService.addIntegral(reqDTO);
        return ZSYResult.success().build();
    }


    /**
     * 更新7月之后,没有任务级别的个人任务,新增级别
     * @return
     */
    @PutMapping("/private-task/add-level")
    public String privateTaskAddLevel(){
        userTaskIntegralService.privateTaskAddLevel();
        return ZSYResult.success().build();
    }

    /**
     * 更新7月之后,没有任务级别的多人任务,根据工时新增级别
     * @return
     */
    @PutMapping("/multi-task/add-level")
    public String multiTaskAddLevel(){
        userTaskIntegralService.multiTaskAddLevel();
        return ZSYResult.success().build();
    }

    /**
     * 计算7月之后完成的个人任务积分
     * @return
     */
    @PutMapping("/private-task/finished/count")
    public String countPrivateIntegral(){
        userTaskIntegralService.countPrivateIntegral();
        return ZSYResult.success().build();
    }

    /**
     * 计算7月之后结束的多人任务积分
     * @return
     */
    @PutMapping("/multi-task/finished/count")
    public String countMultiIntegral(){
        userTaskIntegralService.countMultiIntegral();
        return ZSYResult.success().build();
    }

    /**
     * 计算7月之后完成的未结束的多人任务积分
     * @return
     */
    @PutMapping("/multi-task/completed/count")
    public String countMultiCompletedIntegral(){
        userTaskIntegralService.countMultiCompletedIntegral();
        return ZSYResult.success().build();
    }
}
