package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import com.zhixinhuixue.armor.model.dto.request.MyQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.OnlineQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UpdateQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineQuestionResDTO;
import com.zhixinhuixue.armor.service.IZSYOnlineQuestionService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by SCH on 2018-12-27
 */
@RestController
@RequestMapping("/api/question")
@Api(value = "线上问题相关接口", description = "任务管理系统线上问题相关接口", tags = "/question")
public class ZSYOnlineQuestionController {
    @Autowired
    private IZSYOnlineQuestionService onlineQuestionService;

    @ApiOperation("新建线上问题")
    @PostMapping("/add")
    public String add(@RequestBody OnlineQuestionReqDTO reqDTO){
        return onlineQuestionService.add(reqDTO).build();
    }

    @ApiOperation("查看我的线上问题")
    @PostMapping("/mine")
    public String getMyQuestion(@RequestBody MyQuestionReqDTO reqDTO){
        PageInfo<OnlineQuestionResDTO> pageInfo = onlineQuestionService.selectMyQuestionPage(reqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }

    @ApiOperation("查看进行中线上问题")
    @GetMapping("/running")
    public String getRunningQuestion(){
        List<OnlineQuestionResDTO> list = onlineQuestionService.selectRunningQuestion();
        return ZSYResult.success().data(list).build();
    }

    @ApiOperation("查看已完成线上问题")
    @GetMapping("/completed/{pageNum}")
    public String getCompletedQuestion(@PathVariable("pageNum")Integer pageNum){
        PageInfo<OnlineQuestionResDTO> list = onlineQuestionService.selectCompletedQuestion(pageNum);
        return ZSYResult.success().data(list).build();
    }

    @ApiOperation("查看待审核线上问题")
    @GetMapping("/wait")
    public String getWaitQuestion(){
        List<OnlineQuestionResDTO> list = onlineQuestionService.selectWaitQuestion();
        return ZSYResult.success().data(list).build();
    }

    @ApiOperation("查看审核通过线上问题")
    @GetMapping("/accepted/{pageNum}")
    public String getAcceptedQuestion(@PathVariable("pageNum")Integer pageNum){
        PageInfo<OnlineQuestionResDTO> list = onlineQuestionService.selectAcceptedQuestion(pageNum);
        return ZSYResult.success().data(list).build();
    }

    @ApiOperation("查看审核中(审核通过)线上问题")
    @PostMapping("/check")
    public String getCheckQuestion(@RequestBody MyQuestionReqDTO reqDTO){
        PageInfo<OnlineQuestionResDTO> pageInfo = onlineQuestionService.selectCheckQuestionPage(reqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }

    @ApiOperation("审核线上问题通过")
    @PutMapping("/auditing/accept/{oqrId}")
    public String acceptQuestion(@PathVariable("oqrId")Long oqrId){
        return onlineQuestionService.acceptQuestion(oqrId).build();
    }

    @ApiOperation("删除线上问题")
    @PutMapping("/delete/{oqrId}")
    public String deleteQuestion(@PathVariable("oqrId")Long oqrId){
        return onlineQuestionService.deleteQuestion(oqrId).build();
    }

    @ApiOperation("个人完成线上问题")
    @PutMapping("/finish/{oqrId}")
    public String finishQuestion(@PathVariable("oqrId")Long oqrId){
        return onlineQuestionService.finishQuestion(oqrId).build();
    }

    @ApiOperation("修改线上问题")
    @PutMapping("/update")
    public String updateQuestion(@RequestBody UpdateQuestionReqDTO reqDTO){
        return onlineQuestionService.updateQuestion(reqDTO).build();
    }
}
