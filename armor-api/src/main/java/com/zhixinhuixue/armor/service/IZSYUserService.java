package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserPwdReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.pojo.User;
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
     * 添加用户
     * @param userReqDTO 用户信息
     */
    void addUser(UserReqDTO userReqDTO);


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

}
