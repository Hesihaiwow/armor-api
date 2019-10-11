package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.IntegralResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralHistoryPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralReviewResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralResDTO;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Api(value = "积分信息",description="积分列表与相关信息接口",tags = "/integral")
@RequestMapping("/api/integral")
@RestController
public class ZSYIntegralController extends ZSYController{

    @Autowired
    private IZSYIntegralService integralService;


    /**
     * 获取积分列表,按时间区分
     * @return
     */
    @ApiOperation("获取积分列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime",value = "开始时间",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "endTime",value = "结束时间",required = false,paramType = "query",dataType = "string")
    })
    @GetMapping("")
    public String getIntegralPage(@PathParam("startTime")String startTime,@PathParam("endTime")String endTime){
        List<IntegralPageResDTO> integralPageResDTOList = integralService.getIntegralPage(startTime,endTime);
        return ZSYResult.success().data(integralPageResDTOList).build();
    }

    /**
     * 根据时间查看积分列数量
     * @return
     */
    @ApiOperation("获取积分列数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime",value = "开始时间",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "endTime",value = "结束时间",required = false,paramType = "query",dataType = "string")
    })
    @GetMapping("/count")
    public String getIntegralCount(@PathParam("startTime")String startTime,@PathParam("endTime")String endTime){
        return ZSYResult.success().data(integralService.getIntegralCount(startTime,endTime)).build();
    }

    /**
     * 用户积分排名信息
     * @return
     */
    @ApiOperation("获取用户积分排名信息")
    @GetMapping("/user")
    public String getUserIntegral(){
        UserIntegralResDTO userIntegralResDTO = integralService.getUserIntegral();
        return ZSYResult.success().data(userIntegralResDTO).build();
    }

    /**
     * 用户积分历史
     * @return
     */
    @ApiOperation("获取用户积分历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime",value = "开始时间",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "endTime",value = "结束时间",required = false,paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "id",value = "用户ID",required = true,paramType = "path",dataType = "long"),
            @ApiImplicitParam(name = "pageIndex",value = "页码",required = true,paramType = "path",dataType = "int")
    })
    @GetMapping("/history/{userId}/{pageIndex}")
    public String getIntegralHistory(@PathVariable Long userId,@PathVariable Integer pageIndex,@PathParam("startTime")String startTime,@PathParam("endTime")String endTime){
        PageInfo<IntegralHistoryPageResDTO> integralHistoryPageResDTOS = integralService.getIntegralHistory(userId,pageIndex,startTime,endTime);
        return ZSYResult.success().data(integralHistoryPageResDTOS).build();
    }

    /**
     * 添加积分记录
     * @param integralResDTO
     * @return
     */
    @ApiOperation("添加积分")
    @PostMapping("/add")
    public String addIntegral(@RequestBody IntegralResDTO integralResDTO){
        integralService.addIntegral(integralResDTO);
        return ZSYResult.success().build();
    }

    /**
     * 添加积分记录
     * @param integralResDTO
     * @return
     */
    @ApiOperation("添加求助转移积分")
    @PostMapping("/addHelp")
    public String addHelpIntegral(@RequestBody IntegralResDTO integralResDTO){
        integralService.addIntegral(integralResDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑积分转移
     * @return
     */
    @ApiOperation("编辑积分求助转移")
    @PostMapping("/editHelpDetail/{id}")
    public String editHelpDetail(@RequestBody IntegralResDTO integralResDTO, @PathVariable Long id){
        integralService.updateHelpDetail(integralResDTO,id);
        return ZSYResult.success().build();
    }

    /**
     * 删除积分转移
     * @return
     */
    @ApiOperation("删除积分转移")
    @DeleteMapping("/delete/{id}")
    public String deleteHelpDetail(@PathVariable Long id){
        integralService.deleteHelpDetail(id);
        return ZSYResult.success().build();
    }

    /**
     * 获取我的待审核积分转移
     * @return
     */
    @ApiOperation("获取我的待审核积分转移")
    @GetMapping("/getMyWaitList/{pageIndex}")
    public String getMyWaitList(@PathVariable Integer pageIndex){
        PageInfo<IntegralReviewResDTO>  integralReviewResDTOPageInfo = integralService.getIntegralByReviewStatus(ZSYReviewStatus.PENDING.getValue(),pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 获取所有待审核积分转移
     * @return
     */
    @ApiOperation("获取待审核积分转移")
    @GetMapping("/getHelpWaitList/{pageIndex}")
    public String getHelpWaitList(@PathVariable Integer pageIndex){
        PageInfo<IntegralReviewResDTO>  integralReviewResDTOPageInfo = integralService.getAllIntegralByReviewStatus(ZSYReviewStatus.PENDING.getValue(),pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 获取审核打回积分转移
     * @return
     */
    @ApiOperation("获取审核打回积分转移")
    @GetMapping("/getRejectList/{pageIndex}")
    public String getRejectList(@PathVariable Integer pageIndex){
        PageInfo<IntegralReviewResDTO>  integralReviewResDTOPageInfo = integralService.getIntegralByReviewStatus(ZSYReviewStatus.REJECT.getValue(),pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 获取我的审核完成积分转移
     * @return
     */
    @ApiOperation("获取我的审核完成积分转移")
    @GetMapping("/getMyReviewList/{pageIndex}")
    public String getMyReviewList(@PathVariable Integer pageIndex){
        PageInfo<IntegralReviewResDTO>  integralReviewResDTOPageInfo = integralService.getIntegralByReviewStatus(ZSYReviewStatus.ACCEPT.getValue(),pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 获取所有审核完成积分转移
     * @return
     */
    @ApiOperation("获取审核完成积分转移")
    @GetMapping("/getReviewList/{pageIndex}")
    public String getReviewList(@PathVariable Integer pageIndex){
        PageInfo<IntegralReviewResDTO>  integralReviewResDTOPageInfo = integralService.getAllIntegralByReviewStatus(ZSYReviewStatus.ACCEPT.getValue(),pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 审核通过积分转移
     * @return
     */
    @ApiOperation("审核通过积分转移")
    @GetMapping("/passReview/{id}")
    public String passReview(@PathVariable Long id){
        integralService.passReview(id);
        return ZSYResult.success().build();
    }

    // sch --
    @ApiOperation("个人积分信息")
    @GetMapping("/personal")
    public String getPersonalIntegral(){
        return ZSYResult.success().data(integralService.getPersonalIntegral()).build();
    }
    // -- sch

}
