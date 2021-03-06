package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserBo;
import com.zhixinhuixue.armor.model.bo.UserCheckPeopleBO;
import com.zhixinhuixue.armor.model.dto.request.QueryUserPageReqDTO;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.UserCheckPeople;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface IZSYUserMapper {

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    void insertUser(User user);

    /**
     * 查询用户
     *
     * @param account 账户
     * @return
     */
    List<User> selectByAccount(@Param("account") String account);

    /**
     * 查询邮箱
     *
     * @param email
     * @return
     */
    int selectByEmail(@Param("email")String email);

    /**
     * 查询用户
     *
     * @param account  账号
     * @param password 密码
     * @return
     */
    User selectByAccountAndPassword(@Param("account") String account, @Param("password") String password);

    /**
     * 查询有效用户
     *
     * @return
     */
    List<User> selectEffectiveUsers(@Param("id")Long id);

    /**
     * 删除用户(逻辑删除)
     *
     * @param userId 用户ID
     * @return
     */
    int deleteById(Long userId);

    /**
     * 查询用户
     *
     * @param userId 用户ID
     * @return
     */
    User selectById(Long userId);

    /**
     * 更新用户
     *
     * @param user
     * @return
     */
    int updateSelectiveById(User user);

    /**
     * 用户分页查询
     *
     * @param deptIds 部门ID
     * @return
     */
    Page<UserBo> selectPage(@Param("deptIds") List<Long> deptIds, @Param("reqDTO") QueryUserPageReqDTO reqDTO,@Param("orgId")Long orgId);


    /**
     * 查询组织id下是否存在用户
     *
     * @return
     */
    int countByDepartmentId(long id);

    // sch --

    /**
     * 根据taskid查询超时人员信息
     *
     * @param taskId
     * @return
     */
    User selectByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据ewId查询用户
     *
     * @param ewId
     * @return
     */
    User selectByEwId(@Param("ewId") Long ewId);

    /**
     * 批量插入用户创建任务审核人
     *
     * @param list
     * @return
     * @author sch
     */
    int insertUserCheckPeopleBatch(@Param("list") List<UserCheckPeople> list);

    /**
     * 根据userId查询  任务审核人集合
     *
     * @param userId
     * @return
     * @author sch
     */
    List<UserCheckPeopleBO> selectUserCheckPeopleByUserId(@Param("userId") Long userId);

    /**
     * 删除用户  任务审核人集合
     *
     * @param userId
     * @return
     * @author sch
     */
    int deleteUserCheckPeople(@Param("userId") Long userId);

    /**
     * 查看当前用户管制下的用户
     *
     * @param checkUserId
     * @return
     * @author sch
     */
    List<UserCheckPeopleBO> selectUserByCheckUserId(@Param("checkUserId") Long checkUserId);

    /**
     * 查询用户最后一级审核人
     *
     * @param userId
     * @return
     */
    Long selectUserLastCheckUser(@Param("userId") Long userId);

    /**
     * todo
     * 根据姓名查询
     *
     * @param name
     * @return
     * @author sch
     */
    User selectByName(@Param("name") String name,@Param("orgId")Long orgId);

    /**
     * 查询任务人员信息
     *
     * @param taskId 任务id
     * @return
     * @author sch
     */
    List<UserBo> selectUsersByTask(@Param("taskId") Long taskId);

    /**
     * 根据id查询
     *
     * @param userId 用户id
     * @return
     * @author sch
     */
    UserBo selectUserBOById(@Param("userId") Long userId);

    /**
     * 查询项目管理者
     *
     * @return
     * @author sch
     */
    List<User> selectManagers(@Param("orgId")Long orgId);

    /**
     * 查询用户剩余调休
     *
     * @param jobRole 角色
     * @param userId  用户id
     */
    List<User> selectUserRestHours(@Param("jobRole") Integer jobRole, @Param("userId") Long userId,@Param("orgId")Long orgId);

    /**
     * 根据任务id,查询相关人员
     *
     * @param taskId 任务id
     * @author sch
     */
    List<User> selectTaskUsers(@Param("taskId") Long taskId);

    /**
     * 查询bug报告员
     *
     * @author sch
     */
    List<User> selectBugReporters(@Param("orgId")Long orgId);

    /**
     * 查询bug分派员
     *
     * @author sch
     */
    List<User> selectBugHandlers(@Param("orgId")Long orgId);

    /**
     * 根据工作组id查询用户
     *
     * @param groupId 工作组id
     * @author sch
     */
    List<User> selectUsersByGroup(@Param("groupId") Long groupId,@Param("orgId")Long orgId);

    List<User> selectAll(@Param("orgId")Long orgId);

    /**
     * 查询所有未删除的用户
     *
     * @return List<User>
     * @author sch
     */
    List<User> selectNotDeleteUsers(@Param("orgId")Long orgId);

    /**
     * 查询机构下人员
     *
     * @param orgId
     * @return
     */
    List<User> selectByOrdId(@Param("orgId") Long orgId);
    // -- sch
}