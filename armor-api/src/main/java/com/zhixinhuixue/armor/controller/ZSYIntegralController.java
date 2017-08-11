package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.response.IntegralPageDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralDTO;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Api(value = "积分信息",description="积分列表与相关信息接口",tags = "/integral")
@RequestMapping("/integral")
@RestController
public class ZSYIntegralController extends ZSYController{

    @Autowired
    private IZSYIntegralService integraService;


    /**
     * 获取积分列表,按时间区分
     * @return
     */
    @ApiOperation("获取积分列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime",value = "开始时间",required = false,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "endTime",value = "结束时间",required = false,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "pageIndex",value = "页码",required = true,paramType = "path",dataType = "int")
    })
    @GetMapping("/{pageIndex}")
    public String getIntegralPage(@PathVariable int pageIndex,@PathParam("startTime")String startTime,@PathParam("endTime")String endTime){
        PageInfo<IntegralPageDTO> pageInfo = integraService.getIntegralPage(pageIndex,startTime,endTime);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * 用户积分记录
     * @return
     */
    @ApiOperation("获取用户积分记录")
    @GetMapping("/user")
    public String getUserIntegral(){
        UserIntegralDTO userIntegralDTO = integraService.getUserIntegral();
        return ZSYResult.success().data(userIntegralDTO).build();
    }

}
