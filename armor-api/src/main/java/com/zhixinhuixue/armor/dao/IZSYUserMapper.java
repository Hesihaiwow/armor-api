package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IZSYUserMapper {

    /**
     * 添加用户
     * @param user
     * @return
     */
    void insertUser(User user);

    /**
     * 查询用户
     * @param account 账户
     * @return
     */
    List<User> selectByAccount(String account);

    /**
     * 查询用户
     * @param account 账号
     * @param password 密码
     * @return
     */
    User selectByAccountAndPassword(@Param("account") String account, @Param("password") String password);

    /**
     * 查询有效用户
     * @return
     */
    List<User> selectEffectiveUsers();

    /**
     * 删除用户(逻辑删除)
     * @param userId 用户ID
     * @return
     */
    int deleteById(Long userId);

    /**
     * 查询用户
     * @param userId 用户ID
     * @return
     */
    User selectById(Long userId);

    /**
     * 更新用户
     * @param user
     * @return
     */
    int updateSelectiveById(User user);

}