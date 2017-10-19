package com.zhixinhuixue.armor.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.UploadAvatarReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserPwdReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserResDTO;
import com.zhixinhuixue.armor.source.ZSYResult;

import java.util.List;

/**
 * Created by Akuma on 2017/8/8.
 */
public interface IZSYUserService {

    /**
     * 用户登陆
     * @param userLoginReqDTO 登陆信息
     */
    ZSYResult<String> userLogin(UserLoginReqDTO userLoginReqDTO);

    /**
     * 用户注册
     * @param userReqDTO
     */
    void registerUser(UserReqDTO userReqDTO);

    /**
     * 用户注销
     */
    void userLogout();

    /**
     * 用户分页查询
     * @param deptId 部门ID
     * @param pageIndex 页码
     * @return
     */
    PageInfo<UserPageResDTO> userPage(long deptId, int pageIndex);

    /**
     * 添加用户
     * @param userReqDTO 用户信息
     */
    void addUser(UserReqDTO userReqDTO);

    /**
     * 添加用户
     * @param userReqDTO 用户信息
     */
    void modifyUser(UserReqDTO userReqDTO);

    /**
     * 修改用户头像
     */
    void modifyUserAvatar(UploadAvatarReqDTO uploadAvatarReqDTO);


    /**
     * 查询有效用户
     * @return
     */
    List<EffectUserResDTO> getEffectiveUsers();


    /**
     * 删除用户
     * @param userId 用户ID
     */
    void deleteUserById(Long userId);


    /**
     * 更新用户密码
     * @param userPwdReqDTO 用户密码信息
     */
    void modifyUserPassword(UserPwdReqDTO userPwdReqDTO);


    /**
     * 更新用户密码
     * @param userId 用户ID
     */
    void resetUserPassword(Long userId);

    /**
     * 查询用户明细
     * @param userId 用户ID
     * @return
     */
    UserResDTO getUserById(Long userId);



}
