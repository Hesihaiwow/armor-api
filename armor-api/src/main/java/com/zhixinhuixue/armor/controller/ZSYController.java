package com.zhixinhuixue.armor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Akuma on 2017/8/7.
 */
public class ZSYController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    /**
     * 处理请求日期参数
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));//true允许空值
    }

    /**
     * 返回request
     * @return HttpServletRequest
     */
    public HttpServletRequest getRequest(){
        return request;
    }

    /**
     * 返回response
     * @return
     */
    public HttpServletResponse getResponse() {
        return response;
    }


}
