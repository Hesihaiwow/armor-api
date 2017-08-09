package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserPwdReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
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


    @ApiOperation("删除用户")
    @ApiImplicitParam(name = "userId",value = "用户ID",required = true,paramType = "path",dataType = "long")
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId")Long userId){
        userService.deleteUserById(userId);
        return ZSYResult.success().build();
    }


    @ApiOperation("修改密码")
    @PutMapping("/password")
    public String modifyUserPwd(@Valid @RequestBody UserPwdReqDTO userPwdReqDTO){
        userService.modifyUserPassword(userPwdReqDTO);
        return ZSYResult.success().build();
    }



}
