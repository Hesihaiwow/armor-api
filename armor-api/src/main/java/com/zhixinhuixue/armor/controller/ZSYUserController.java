package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserResDTO;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/user")
@Api(value = "用户服务", description = "任务管理系统用户相关接口", tags = "/user")
public class ZSYUserController extends ZSYController {

    private static final Logger logger = LoggerFactory.getLogger(ZSYUserController.class);


    @Autowired
    private IZSYUserService userService;


    @ApiOperation("用户登陆")
    @PostMapping(value = "/login")
    public String login(@Valid @RequestBody UserLoginReqDTO userLoginReqDTO) {
        return userService.userLogin(userLoginReqDTO).build();
    }

    @ApiOperation("用户注册")
    @PostMapping(value = "/register")
    public String register(@Valid @RequestBody UserReqDTO userReqDTO) {
        userService.registerUser(userReqDTO);
        return ZSYResult.success().build();
    }


    @ApiOperation("用户注销")
    @PostMapping(value = "/logout")
    public String login() {
        userService.userLogout();
        return ZSYResult.success().build();
    }


    @ApiOperation("用户列表(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门ID", required = true, paramType = "path", dataType = "long"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = true, paramType = "path", dataType = "int")
    })
    @PostMapping("/page")
    public String paging(@RequestBody QueryUserPageReqDTO reqDTO) {
        PageInfo<UserPageResDTO> pageInfo = userService.userPage(reqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }


    @ApiOperation("用户添加")
    @PostMapping("/add")
    public String add(@Valid @RequestBody UserReqDTO userReqDTO) {
        userService.addUser(userReqDTO);
        return ZSYResult.success().build();
    }


    @ApiOperation("查询用户")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "long")
    @GetMapping("/{userId}")
    public String getUser(@PathVariable("userId") Long userId) {
        UserResDTO userResDTO = userService.getUserById(userId);
        return ZSYResult.success().data(userResDTO).build();
    }


    @ApiOperation("有效用户列表")
    @GetMapping("/effective")
    public String effectiveUsers() {
        List<EffectUserResDTO> users = userService.getEffectiveUsers();
        return ZSYResult.success().data(users).build();
    }

    @ApiOperation("管理设计用户列表")
    @GetMapping("/manager")
    public String manageUsers() {
        List<EffectUserResDTO> users = userService.getManageUsers();
        return ZSYResult.success().data(users).build();
    }

    @ApiOperation("删除用户")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "long")
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return ZSYResult.success().build();
    }


    @ApiOperation("修改密码")
    @PutMapping("/password")
    public String modifyUserPwd(@Valid @RequestBody UserPwdReqDTO userPwdReqDTO) {
        userService.modifyUserPassword(userPwdReqDTO);
        return ZSYResult.success().build();
    }


    @ApiOperation("重置密码")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "long")
    @PutMapping("/repwd/{userId}")
    public String resetUserPwd(@PathVariable("userId") Long userId) {
        userService.resetUserPassword(userId);
        return ZSYResult.success().build();
    }


    @ApiOperation("修改用户")
    @PutMapping("/{userId}")
    public String modifyUser(@Valid @RequestBody UserReqDTO userReqDTO,
                             @PathVariable("userId") Long userId) {
        userReqDTO.setUserId(userId);
        userService.modifyUser(userReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改头像")
    @PutMapping("/modifyAvatar")
    public String modifyAvatar(@Valid @RequestBody UploadAvatarReqDTO uploadAvatarReqDTO) {
        userService.modifyUserAvatar(uploadAvatarReqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改组织")
    @PutMapping("/modifyDept/{id}")
    public String modifyAvatar(@Valid @PathVariable("id") Long id) {
        return ZSYResult.success().data(userService.modifyDept(id)).build();
    }

    @ApiOperation("获取我的资料")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "path", dataType = "long")
    @GetMapping("/myProfile")
    public String getUser() {
        UserResDTO userResDTO = userService.getUserById(ZSYTokenRequestContext.get().getUserId());
        return ZSYResult.success().data(userResDTO).build();
    }

    @ApiOperation("验证邮件")
    @GetMapping("/validateEmail/{validateEmail}")
    public String activeEmail(@PathVariable("validateEmail") String validateEmail) {
        userService.validateEmail(validateEmail);
        return ZSYResult.success().data("邮箱验证成功").build();
    }

    @ApiOperation("重新发送邮件")
    @GetMapping("/sendEmail")
    public String sendEmail() {
        userService.sendEmail();
        return ZSYResult.success().build();
    }

    // sch --
    @ApiOperation("用户修改个人基本信息")
    @PutMapping("/modify/info")
    public String modifyBasicInfo(@Valid @RequestBody UserInfoReqDTO userInfoReqDTO){
        userService.modifyBasicInfo(userInfoReqDTO);
        return ZSYResult.success().build();
    }


    @ApiOperation("查询当前用户是否其他用户的审核人")
    @GetMapping("/controlled-people/{checkUserId}")
    public String getControlledPeopleList(@PathVariable("checkUserId")Long checkUserId){
        return ZSYResult.success().data(userService.getControlledPeopleList(checkUserId)).build();
    }
    // -- sch
}
