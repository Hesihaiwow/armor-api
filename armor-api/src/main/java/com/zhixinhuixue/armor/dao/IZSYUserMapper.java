package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserBo;
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
     * 查询邮箱
     * @param email
     * @return
     */
    int selectByEmail(String email);

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
    List<User> selectEffectiveUsers(Long id);

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

    /**
     * 用户分页查询
     * @param deptIds 部门ID
     * @return
     */
    Page<UserBo> selectPage(List<Long> deptIds);


    /**
     * 查询组织id下是否存在用户
     * @return
     */
    int countByDepartmentId(long id);
}