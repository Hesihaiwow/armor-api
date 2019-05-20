package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserBo;
import com.zhixinhuixue.armor.model.bo.UserCheckPeopleBO;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.UserCheckPeople;
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

    // sch --

    /**
     * 根据taskid查询超时人员信息
     * @param taskId
     * @return
     */
    User selectByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据ewId查询用户
     * @param ewId
     * @return
     */
    User selectByEwId(@Param("ewId") Long ewId);

    /**
     * 批量插入用户创建任务审核人
     * @author sch
     * @param list
     * @return
     */
    int insertUserCheckPeopleBatch(@Param("list")List<UserCheckPeople> list);

    /**
     * 根据userId查询  任务审核人集合
     * @author sch
     * @param userId
     * @return
     */
    List<UserCheckPeopleBO> selectUserCheckPeopleByUserId(@Param("userId")Long userId);

    /**
     * 删除用户  任务审核人集合
     * @author sch
     * @param userId
     * @return
     */
    int deleteUserCheckPeople(@Param("userId")Long userId);

    /**
     * 查看当前用户管制下的用户
     * @author sch
     * @param checkUserId
     * @return
     */
    List<UserCheckPeopleBO> selectUserByCheckUserId(@Param("checkUserId")Long checkUserId);

    /**
     * 查询用户最后一级审核人
     * @param userId
     * @return
     */
    Long selectUserLastCheckUser(@Param("userId")Long userId);
    // -- sch
}