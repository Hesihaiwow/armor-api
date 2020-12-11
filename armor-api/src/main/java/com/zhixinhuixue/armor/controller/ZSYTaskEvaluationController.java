package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddEvaluationReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EvaluationPageQueryReqDTO;
import com.zhixinhuixue.armor.service.IZSYTaskEvaluationService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 任务评价相关接口
 *
 * @author sch
 * @DATE 2019/5/22 10:57
 */
@RestController
@RequestMapping("/api/evaluation")
public class ZSYTaskEvaluationController {
    @Resource
    private IZSYTaskEvaluationService taskEvaluationService;

    /**
     * 新增评价
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public String evaluate(@Valid @RequestBody AddEvaluationReqDTO reqDTO) {
        ZSYResult zsyResult = taskEvaluationService.evaluate(reqDTO);
        return zsyResult.build();
    }

    /**
     * 查询待评价任务
     *
     * @return
     */
    @GetMapping("/task/wait")
    public String getWaitEvaluated() {
        return ZSYResult.success().data(taskEvaluationService.getWaitEvaluated()).build();
    }

    /**
     * 分页查看已评价的任务
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/task/evaluated/{pageNum}")
    public String getEvaluated(@PathVariable("pageNum") Integer pageNum) {
        return ZSYResult.success().data(taskEvaluationService.getEvaluated(pageNum)).build();
    }

    /**
     * 管理员分页查看用户所有任务综合评价
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/average/user/page")
    public String getUserAvgEvaluation(@RequestBody EvaluationPageQueryReqDTO reqDTO) {
        return ZSYResult.success().data(taskEvaluationService.getUserAvgEvaluation(reqDTO)).build();
    }

    /**
     * 个人主页查看周,月,年的综合评价
     *
     * @return
     */
    @GetMapping("/average/personal")
    public String getPersonalAverageEva() {
        return ZSYResult.success().data(taskEvaluationService.getPersonalAverageEva()).build();
    }
}
