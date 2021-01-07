package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserResDTO;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by Akuma on 2017/8/7.
 */
@RestController
@RequestMapping("/api/user")
public class ZSYUserController extends ZSYController {

    @Resource
    private IZSYUserService userService;


    /**
     * 用户登陆
     *
     * @param userLoginReqDTO 参数
     */
    @PostMapping(value = "/login")
    public String login(@Valid @RequestBody UserLoginReqDTO userLoginReqDTO) {
        return userService.userLogin(userLoginReqDTO).build();
    }

    /**
     * 用户登陆
     *
     * @param userLoginReqDTO 参数
     */
    @PostMapping(value = "/admin/login")
    public String adminLogin(@Valid @RequestBody UserLoginReqDTO userLoginReqDTO) {
        return userService.adminLogin(userLoginReqDTO).build();
    }

    /**
     * 用户注册
     *
     * @param userReqDTO 参数
     */
    @PostMapping(value = "/register")
    public String register(@Valid @RequestBody UserReqDTO userReqDTO) {
        userService.registerUser(userReqDTO);
        return ZSYResult.success().build();
    }


    /**
     * 用户注销
     */
    @PostMapping(value = "/logout")
    public String login() {
        userService.userLogout();
        return ZSYResult.success().build();
    }


    /**
     * 用户列表(分页)
     *
     * @param reqDTO 参数
     */
    @PostMapping("/page")
    public String paging(@RequestBody QueryUserPageReqDTO reqDTO) {
        PageInfo<UserPageResDTO> pageInfo = userService.userPage(reqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }


    /**
     * 用户添加
     *
     * @param userReqDTO 参数
     */
    @PostMapping("/add")
    public String add(@Valid @RequestBody UserReqDTO userReqDTO) {
        userService.addUser(userReqDTO);
        return ZSYResult.success().build();
    }


    /**
     * 查询用户
     *
     * @param userId 用户id
     */
    @GetMapping("/{userId}")
    public String getUser(@PathVariable("userId") Long userId) {
        UserResDTO userResDTO = userService.getUserById(userId);
        return ZSYResult.success().data(userResDTO).build();
    }


    /**
     * 有效用户列表
     */
    @GetMapping("/effective")
    public String effectiveUsers() {
        List<EffectUserResDTO> users = userService.getEffectiveUsers();
        return ZSYResult.success().data(users).build();
    }

    /**
     * 管理设计用户列表
     */
    @GetMapping("/manager")
    public String manageUsers() {
        List<EffectUserResDTO> users = userService.getManageUsers();
        return ZSYResult.success().data(users).build();
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return ZSYResult.success().build();
    }

    /**
     * 修改密码
     *
     * @param userPwdReqDTO 参数
     */
    @PutMapping("/password")
    public String modifyUserPwd(@Valid @RequestBody UserPwdReqDTO userPwdReqDTO) {
        userService.modifyUserPassword(userPwdReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 重置密码
     *
     * @param userId 用户id
     */
    @PutMapping("/repwd/{userId}")
    public String resetUserPwd(@PathVariable("userId") Long userId) {
        userService.resetUserPassword(userId);
        return ZSYResult.success().build();
    }

    /**
     * 修改用户
     *
     * @param userReqDTO 参数
     * @param userId     用户id
     */
    @PutMapping("/{userId}")
    public String modifyUser(@Valid @RequestBody UserReqDTO userReqDTO,
                             @PathVariable("userId") Long userId) {
        userReqDTO.setUserId(userId);
        userService.modifyUser(userReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改头像
     *
     * @param uploadAvatarReqDTO 参数
     */
    @PutMapping("/modifyAvatar")
    public String modifyAvatar(@Valid @RequestBody UploadAvatarReqDTO uploadAvatarReqDTO) {
        userService.modifyUserAvatar(uploadAvatarReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改组织
     *
     * @param id id
     */
    @PutMapping("/modifyDept/{id}")
    public String modifyAvatar(@Valid @PathVariable("id") Long id) {
        return ZSYResult.success().data(userService.modifyDept(id)).build();
    }

    /**
     * 获取我的资料
     */
    @GetMapping("/myProfile")
    public String getUser() {
        UserResDTO userResDTO = userService.getUserById(ZSYTokenRequestContext.get().getUserId());
        return ZSYResult.success().data(userResDTO).build();
    }

    /**
     * 验证邮件
     *
     * @param validateEmail 参数
     */
    @GetMapping("/validateEmail/{validateEmail}")
    public String activeEmail(@PathVariable("validateEmail") String validateEmail) {
        userService.validateEmail(validateEmail);
        return ZSYResult.success().data("邮箱验证成功").build();
    }

    /**
     * 重新发送邮件
     */
    @GetMapping("/sendEmail")
    public String sendEmail() {
        userService.sendEmail();
        return ZSYResult.success().build();
    }

    // sch --

    /**
     * 用户修改个人基本信息
     *
     * @param userInfoReqDTO 参数
     * @author sch
     */
    @PutMapping("/modify/info")
    public String modifyBasicInfo(@Valid @RequestBody UserInfoReqDTO userInfoReqDTO) {
        userService.modifyBasicInfo(userInfoReqDTO);
        return ZSYResult.success().build();
    }


    /**
     * 查询当前用户是否其他用户的审核人
     *
     * @param checkUserId 审核人id
     * @author sch
     */
    @GetMapping("/controlled-people/{checkUserId}")
    public String getControlledPeopleList(@PathVariable("checkUserId") Long checkUserId) {
        return ZSYResult.success().data(userService.getControlledPeopleList(checkUserId)).build();
    }

    /**
     * 查询项目管理者
     *
     * @author sch
     */
    @GetMapping("/managers")
    public String getProductManagers() {
        return ZSYResult.success().data(userService.getProductManagers()).build();
    }

    /**
     * 修改用户调休时间
     *
     * @param reqDTO 参数
     * @author sch
     */
    @PostMapping("/rest-hours/update")
    public String updateUserRestHours(@Valid @RequestBody EditUserRestHoursReqDTO reqDTO) {
        userService.updateUserRestHours(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 查询未删除的用户
     *
     * @author sch
     */
    @GetMapping("/not-delete-lists")
    public String getNotDeleteUserList() {
        return ZSYResult.success().data(userService.getNotDeleteUserList()).build();
    }
    // -- sch

    /***
     *  查询是否是组内的leader
     *
     * @return
     * @author hsh
     * @create 2020/11/5-15:53
     */
    @GetMapping("/group-leader")
    public String getIsGroupLeader(){
        return ZSYResult.success().data(userService.getIsGroupLeader()).build();
    }
}
