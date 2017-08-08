package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Akuma on 2017/8/7.
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户服务",description="任务管理系统用户相关接口",tags = "/user")
public class ZSYUserController extends ZSYController {

    private static final Logger logger = LoggerFactory.getLogger(ZSYUserController.class);


    @Autowired
    private IZSYUserService userService;


    @ApiOperation("用户登陆")
    @PostMapping(value = "/login")
    public String login(@Valid @RequestBody UserLoginReqDTO userLoginReqDTO){
        return userService.userLogin(userLoginReqDTO).build();
    }


    @ApiOperation("用户添加")
    @PostMapping("/add")
    public String add(@Valid @RequestBody UserReqDTO userReqDTO){
        userService.addUser(userReqDTO);
        return ZSYResult.success().build();
    }


    @ApiOperation("有效用户列表")
    @GetMapping("/effective")
    public String effectiveUsers(){
        List<EffectUserResDTO> users = userService.getEffectiveUsers();
        return ZSYResult.success().data(users).build();
    }

}
