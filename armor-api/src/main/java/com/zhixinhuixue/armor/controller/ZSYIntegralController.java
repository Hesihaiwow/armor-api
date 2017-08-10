package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.PageProxy;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@RestController
public class ZSYIntegralController extends ZSYController{

    @Autowired
    private IZSYIntegralService integraService;


    /**
     * 获取积分列表,按时间区分
     * @return
     */
    @ApiOperation("获取积分列表")
    @GetMapping("/integral/{startTime}/{endTime}/{pageIndex}")
    public String getIntegral(@ApiParam(value = "开始结束时间,类型:时间戳", required = true)@PathVariable String startTime, @PathVariable String endTime, @PathVariable int pageIndex){
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        PageProxy ds = new PageProxy<>(integraService, getRequest(), map);
        IZSYIntegralService integral = (IZSYIntegralService) ds.proxyInstance();
        if(endTime!="0"){
            integral.findIntegral(DateHelper.TimestampToDate(startTime), DateHelper.TimestampToDate(endTime));
        }else{
            integral.findIntegral(DateHelper.TimestampToDate(startTime), null);
        }
        return ZSYResult.success().data(map).build();
    }

}
