package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.PageProxy;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import com.zhixinhuixue.armor.source.ZSYResult;
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
public class ZSYIntegralController {

    @Autowired
    private IZSYIntegralService integraService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取积分列表,按时间区分
     * @return
     */
    @GetMapping("/integral/{startTime}/{endTime}/{pageIndex}")
    public Object getIntegral(@PathVariable String startTime, @PathVariable String endTime, @PathVariable int pageIndex){
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


    public HttpServletRequest getRequest() {
        return request;
    }
}
