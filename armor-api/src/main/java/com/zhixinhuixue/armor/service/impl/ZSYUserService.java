package com.zhixinhuixue.armor.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYAuthException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.*;
import com.zhixinhuixue.armor.model.bo.DeptBo;
import com.zhixinhuixue.armor.model.bo.UserBo;
import com.zhixinhuixue.armor.model.bo.UserCheckPeopleBO;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserCheckPeopleResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Akuma on 2017/8/8.
 */
@Service
public class ZSYUserService implements IZSYUserService {

    private static final Logger logger = LoggerFactory.getLogger(ZSYUserService.class);

    //jwt密钥
    @Value("${jwt.secret}")
    private String jwtSecret;

    //jwt发行者
    @Value("${jwt.issuer}")
    private String jwtIssuer;

    //jwt过期时间
    @Value("${jwt.exp}")
    private int jwtExp;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private IZSYWorkGroupMapper groupMapper;

    @Autowired
    @Qualifier("primaryStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IZSYDepartmentMapper departmentMapper;

    @Autowired
    private IZSYRestHoursLogMapper restHoursLogMapper;

    @Autowired
    private IZSYUserGroupMapper userGroupMapper;

    @Override
    @Transactional
    public ZSYResult<String> userLogin(UserLoginReqDTO userLoginReqDTO) {
        //验证登录用户
        User user = userMapper.selectByAccountAndPassword(userLoginReqDTO.getAccount(),
                MD5Helper.convert(
                        String.format("%s%s", SHA1Helper.Sha1(userLoginReqDTO.getPassword()),
                                ZSYConstants.HINT_PASSWORD_KEY), 32, false));
        if (user == null) {
            throw new ZSYServiceException("账号或密码错误");
        }
        if(user.getStatus()==1){
            throw new ZSYServiceException("用户已冻结使用.");
        }
        //验证通过
        //修改登录时间
        User modifyUser = new User();
        modifyUser.setId(user.getId());
        modifyUser.setLastLogin(new Date());
        if (userMapper.updateSelectiveById(modifyUser) == 0) {
            throw new ZSYServiceException("更新登录时间失败,登录异常.");
        }
        //生成Token
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(jwtSecret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Long departmentId = user.getDepartmentId();
        if(user.getDepartmentId()!=0){
            departmentId = departmentMapper.selectById(user.getDepartmentId()).getParentId();
        }
        String jwt = JWT.create()
                .withIssuer(jwtIssuer)
                .withExpiresAt(DateHelper.afterDate(new Date(), jwtExp))
                .withIssuedAt(new Date())
                .withClaim("userId", String.valueOf(user.getId()))
                .withClaim("userName", user.getName())
                .withClaim("avatarUrl", user.getAvatarUrl())
                .withClaim("userRole", user.getUserRole())
                .withClaim("departmentId", departmentId)
                .sign(algorithm);

        String loginKey = String.format(ZSYConstants.LOGIN_KEY, user.getId());
        stringRedisTemplate.opsForValue().set(loginKey, ZSYConstants.REDIS_DEFAULT_VALUE);
        stringRedisTemplate.expire(loginKey, ZSYConstants.LOGIN_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
        logger.info("{}({})登录成功,token:{}", user.getName(), user.getId(), jwt);
        return ZSYResult.success().data(jwt);
    }

    @Override
    public String createUserJwtToken(String account) {
        //验证登录用户
        List<User> users = userMapper.selectByAccount(account);
        User user = users.get(0);
        //验证通过
        //修改登录时间
        User modifyUser = new User();
        modifyUser.setId(user.getId());
        modifyUser.setLastLogin(new Date());
        if (userMapper.updateSelectiveById(modifyUser) == 0) {
            throw new ZSYServiceException("更新登录时间失败,登录异常.");
        }
        //生成Token
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(jwtSecret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Long departmentId = user.getDepartmentId();
        if(user.getDepartmentId()!=0){
            departmentId = departmentMapper.selectById(user.getDepartmentId()).getParentId();
        }
        String jwt = JWT.create()
                .withIssuer(jwtIssuer)
                .withExpiresAt(DateHelper.afterDate(new Date(), jwtExp))
                .withIssuedAt(new Date())
                .withClaim("userId", String.valueOf(user.getId()))
                .withClaim("userName", user.getName())
                .withClaim("avatarUrl", user.getAvatarUrl())
                .withClaim("userRole", user.getUserRole())
                .withClaim("departmentId", departmentId)
                .sign(algorithm);

        String loginKey = String.format(ZSYConstants.LOGIN_KEY, user.getId());
        stringRedisTemplate.opsForValue().set(loginKey, ZSYConstants.REDIS_DEFAULT_VALUE);
        stringRedisTemplate.expire(loginKey, ZSYConstants.LOGIN_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
        logger.info("{}({})登录成功,token:{}", user.getName(), user.getId(), jwt);
        return jwt;
    }

    @Override
    public void registerUser(UserReqDTO userReqDTO){
        //校验邮箱是否存在
        if (userMapper.selectByEmail(userReqDTO.getEmail()) > 0) {
            throw new ZSYServiceException(String.format("用户邮箱[%s]已存在", userReqDTO.getEmail()));
        }
        userReqDTO.setDepartmentId(ZSYConstants.NO_DEPT_ID);
        userReqDTO.setUserRole(ZSYUserRole.EMPLOYEE.getValue());
        userReqDTO.setPassword(MD5Helper.convert(
                String.format("%s%s", SHA1Helper.Sha1(userReqDTO.getPassword()),
                        ZSYConstants.HINT_PASSWORD_KEY), 32, false));
        userReqDTO.setStatus(ZSYUserStatus.ACTIVE.getValue());

        this.addUser(userReqDTO);
        MailHelper.send(userReqDTO.getEmail());
    }

    @Override
    public void userLogout() {
        String loginKey = String.format(ZSYConstants.LOGIN_KEY, ZSYTokenRequestContext.get().getUserId());
        stringRedisTemplate.delete(loginKey);
    }

    @Override
    public PageInfo<UserPageResDTO> userPage(QueryUserPageReqDTO reqDTO) {
        List<Long> deptIds = Lists.newArrayList();
        DeptBo deptBo = departmentMapper.selectRootDept(reqDTO.getDeptId());
        deptIds.add(deptBo.getId());
        deptIds.addAll(deepCopyDeptIds(deptBo.getChildren()));

        if (reqDTO.getGroupId() != null && reqDTO.getGroupId() == 1){
            reqDTO.setGroupId(null);
        }
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageIndex()).orElse(1), ZSYConstants.PAGE_SIZE);
        Page<UserBo> userBos = userMapper.selectPage(deptIds,reqDTO);
        Page<UserPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(userBos, page);
        userBos.stream().forEach(userBo -> {
            UserPageResDTO userPageResDTO = new UserPageResDTO();
            BeanUtils.copyProperties(userBo, userPageResDTO);
            userPageResDTO.setDeptName(userBo.getDepartment().getName());
            if (userBo.getLevel()!=null){
                userPageResDTO.setLevelName(ZSYUserLevel.getName(userBo.getLevel()));
            }
            List<UserCheckPeopleBO> userCheckPeopleBOS = userMapper.selectUserCheckPeopleByUserId(userBo.getId());
            List<UserCheckPeopleResDTO> checkPeopleResDTOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(userCheckPeopleBOS)){
                userCheckPeopleBOS.stream().forEach(userCheckPeopleBO -> {
                    UserCheckPeopleResDTO resDTO = new UserCheckPeopleResDTO();
                    resDTO.setId(userCheckPeopleBO.getCheckUserId());
                    resDTO.setName(userCheckPeopleBO.getCheckUserName());
                    resDTO.setLevel(userCheckPeopleBO.getLevel());
                    checkPeopleResDTOS.add(resDTO);
                });
            }
            userPageResDTO.setCheckUsers(checkPeopleResDTOS);
            page.add(userPageResDTO);
        });
        PageInfo<UserPageResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }


    @Override
    @Transactional
    public void addUser(UserReqDTO userReqDTO) {
        if(userReqDTO.getDepartmentId()==0){
            userReqDTO.setUserRole(ZSYUserRole.EMPLOYEE.getValue());
        }else{
            if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
                throw new ZSYAuthException("没有权限执行此操作");
            }
        }
        //校验邮箱是否存在
        if (userMapper.selectByEmail(userReqDTO.getEmail()) > 0) {
            throw new ZSYServiceException(String.format("用户邮箱[%s]已存在", userReqDTO.getAccount()));
        }
        if(userReqDTO.getStatus()==null){
            userReqDTO.setStatus(ZSYUserStatus.NORMAL.getValue());
        }
        //校验用户账户是否存在
        List<User> existUsers = userMapper.selectByAccount(userReqDTO.getAccount());
        if (existUsers.size() > 0) {
            throw new ZSYServiceException(String.format("用户账户[%s]已存在", userReqDTO.getAccount()));
        }

        User user = new User();
        BeanUtils.copyProperties(userReqDTO, user);
        user.setJobNumber(userReqDTO.getJobNumber().trim());
        user.setId(snowFlakeIDHelper.nextId());
        user.setCreateTime(new Date());
        user.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        user.setIntegral(new BigDecimal(ZSYConstants.DEFAULT_INTEGRAL));
        if(userReqDTO.getPassword()==null||userReqDTO.getPassword()==""){
            user.setPassword(MD5Helper.convert(
                    String.format("%s%s", SHA1Helper.Sha1(ZSYConstants.DEFAULT_PASSWORD),
                            ZSYConstants.HINT_PASSWORD_KEY), 32, false));
        }
        userMapper.insertUser(user);

        List<UserCheckPeopleReqDTO> checkUsers = userReqDTO.getCheckUserList();
        List<Long> checkUserIds = checkUsers.stream().map(UserCheckPeopleReqDTO::getId).distinct().collect(Collectors.toList());
        if (checkUserIds.size() != checkUsers.size()){
            throw new ZSYServiceException("多级审核人重复,请检查");
        }
        List<UserCheckPeople> userCheckPeopleList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(checkUsers)){
            checkUsers.stream().forEach(checkUser->{
                UserCheckPeople userCheckPeople = new UserCheckPeople();
                userCheckPeople.setId(snowFlakeIDHelper.nextId());
                userCheckPeople.setUserId(user.getId());
                userCheckPeople.setCheckUserId(checkUser.getId());
                userCheckPeople.setLevel(checkUser.getLevel());
                userCheckPeople.setStatus(0);
                userCheckPeopleList.add(userCheckPeople);
            });
            if (!CollectionUtils.isEmpty(userCheckPeopleList)){
                userMapper.insertUserCheckPeopleBatch(userCheckPeopleList);
            }
        }else {
            throw new ZSYServiceException("用户创建任务审核人不能为空");
        }
    }

    @Override
    @Transactional
    public void modifyUser(UserReqDTO userReqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYAuthException("没有权限执行此操作");
        }

        User user = new User();
        BeanUtils.copyProperties(userReqDTO, user);
        user.setId(userReqDTO.getUserId());
        user.setJobNumber(userReqDTO.getJobNumber().trim());
        if (userMapper.updateSelectiveById(user) == 0) {
            throw new ZSYServiceException("更新用户失败");
        }
        List<UserCheckPeopleReqDTO> checkUsers = userReqDTO.getCheckUserList();
        List<Long> checkUserIds = checkUsers.stream().map(UserCheckPeopleReqDTO::getId).distinct().collect(Collectors.toList());
        if (checkUserIds.size() != checkUsers.size()){
            throw new ZSYServiceException("多级审核人重复,请检查");
        }
        List<UserCheckPeople> userCheckPeopleList = new ArrayList<>();
        //当账户冻结时  设置userCheckPeople的状态为1
        if (user.getStatus() == 1){
            if (!CollectionUtils.isEmpty(checkUsers)){
                //删除原来的任务审核人
                userMapper.deleteUserCheckPeople(user.getId());
                checkUsers.stream().forEach(checkUser->{
                    UserCheckPeople userCheckPeople = new UserCheckPeople();
                    userCheckPeople.setId(snowFlakeIDHelper.nextId());
                    userCheckPeople.setUserId(user.getId());
                    userCheckPeople.setCheckUserId(checkUser.getId());
                    userCheckPeople.setLevel(checkUser.getLevel());
                    userCheckPeople.setStatus(1);
                    userCheckPeopleList.add(userCheckPeople);
                });
                if (!CollectionUtils.isEmpty(userCheckPeopleList)){
                    userMapper.insertUserCheckPeopleBatch(userCheckPeopleList);
                }
            }else {
                throw new ZSYServiceException("用户的创建任务审核人不能为空");
            }
        }else {
            if (!CollectionUtils.isEmpty(checkUsers)){
                //删除原来的任务审核人
                userMapper.deleteUserCheckPeople(user.getId());
                checkUsers.stream().forEach(checkUser->{
                    UserCheckPeople userCheckPeople = new UserCheckPeople();
                    userCheckPeople.setId(snowFlakeIDHelper.nextId());
                    userCheckPeople.setUserId(user.getId());
                    userCheckPeople.setCheckUserId(checkUser.getId());
                    userCheckPeople.setLevel(checkUser.getLevel());
                    userCheckPeople.setStatus(0);
                    userCheckPeopleList.add(userCheckPeople);
                });
                if (!CollectionUtils.isEmpty(userCheckPeopleList)){
                    userMapper.insertUserCheckPeopleBatch(userCheckPeopleList);
                }
            }else {
                throw new ZSYServiceException("用户的创建任务审核人不能为空");
            }
        }

    }

    /**
     * 修改用户头像
     */
    @Override
    public void modifyUserAvatar(UploadAvatarReqDTO uploadAvatarReqDTO) {
        User user = new User();
        user.setId(uploadAvatarReqDTO.getUserId());
        user.setAvatarUrl(uploadAvatarReqDTO.getUrl());
        userMapper.updateSelectiveById(user);
    }

    /**
     * 修改组织
     * @param id
     */
    public String modifyDept(Long id){
        User user = new User();
        user.setId(ZSYTokenRequestContext.get().getUserId());
        user.setDepartmentId(id);
        if(userMapper.countByDepartmentId(departmentMapper.selectById(id).getId())==0){//如果是组织第一名用户，自动升为超级管理员
            user.setUserRole(ZSYUserRole.ADMINISTRATOR.getValue());
        }else{
            user.setUserRole(ZSYUserRole.EMPLOYEE.getValue());
        }
        if (userMapper.updateSelectiveById(user) == 0) {
            throw new ZSYServiceException("更新组织失败，请重试");
        }

        User tokenUser = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        //生成Token
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(jwtSecret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Long departmentId = user.getDepartmentId();
        if(user.getDepartmentId()!=0){
            departmentId = departmentMapper.selectById(user.getDepartmentId()).getParentId();
        }
        String jwt = JWT.create()
                .withIssuer(jwtIssuer)
                .withExpiresAt(DateHelper.afterDate(new Date(), jwtExp))
                .withIssuedAt(new Date())
                .withClaim("userId", String.valueOf(tokenUser.getId()))
                .withClaim("userName", ZSYTokenRequestContext.get().getUserName())
                .withClaim("avatarUrl", tokenUser.getAvatarUrl())
                .withClaim("userRole", tokenUser.getUserRole())
                .withClaim("departmentId", departmentId)
                .sign(algorithm);

        String loginKey = String.format(ZSYConstants.LOGIN_KEY, tokenUser.getId());
        stringRedisTemplate.opsForValue().set(loginKey, ZSYConstants.REDIS_DEFAULT_VALUE);
        stringRedisTemplate.expire(loginKey, ZSYConstants.LOGIN_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
        logger.info("{}({})组织修改成功,token:{}", ZSYTokenRequestContext.get().getUserName(), tokenUser.getId(), jwt);
        return jwt;
    }

    @Override
    public List<EffectUserResDTO> getEffectiveUsers() {
        List<User> users = userMapper.selectEffectiveUsers(ZSYTokenRequestContext.get().getDepartmentId());
        List<EffectUserResDTO> effectUserResDTOS = Lists.newArrayList();
        users.stream().forEach(user -> {
            EffectUserResDTO effectUserResDTO = new EffectUserResDTO();
            BeanUtils.copyProperties(user, effectUserResDTO);
            effectUserResDTOS.add(effectUserResDTO);
        });
        return effectUserResDTOS;
    }

    @Override
    public List<EffectUserResDTO> getManageUsers() {
        List<User> users = userMapper.selectEffectiveUsers(ZSYTokenRequestContext.get().getDepartmentId());
        List<EffectUserResDTO> effectUserResDTOS = Lists.newArrayList();
        users.stream().forEach(user -> {
            EffectUserResDTO effectUserResDTO = new EffectUserResDTO();
            BeanUtils.copyProperties(user, effectUserResDTO);
            if(user.getUserRole()<2){
                effectUserResDTOS.add(effectUserResDTO);
            }
        });
        return effectUserResDTOS;
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYAuthException("没有权限执行此操作");
        }
        if (userMapper.deleteById(userId) == 0) {
            throw new ZSYServiceException("删除用户失败");
        }
        userMapper.deleteUserCheckPeople(userId);
    }

    @Override
    public void modifyUserPassword(UserPwdReqDTO userPwdReqDTO) {
        User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        Optional.ofNullable(user).orElseThrow(() -> new ZSYServiceException("用户不存在"));
        //校验用户状态
        if (user.getStatus() != 0 || user.getIsDelete() != 0) {
            throw new ZSYServiceException("账户冻结或已删除,操作失败");
        }
        //校验原始密码
        String secretOriginalPwd = MD5Helper.convert(
                String.format("%s%s", SHA1Helper.Sha1(userPwdReqDTO.getOriginalPassword()),
                        ZSYConstants.HINT_PASSWORD_KEY), 32, false);
        if (!secretOriginalPwd.equals(user.getPassword())) {
            throw new ZSYServiceException("旧密码不正确");
        }
        //校验通过,修改密码
        User modifyUser = new User();
        modifyUser.setId(ZSYTokenRequestContext.get().getUserId());
        String secretNewPwd = MD5Helper.convert(
                String.format("%s%s", SHA1Helper.Sha1(userPwdReqDTO.getNewPassword()),
                        ZSYConstants.HINT_PASSWORD_KEY), 32, false);
        modifyUser.setPassword(secretNewPwd);
        if (userMapper.updateSelectiveById(modifyUser) == 0) {
            throw new ZSYServiceException("更新密码失败");
        }
    }

    @Override
    public void resetUserPassword(Long userId) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYAuthException("没有权限执行此操作");
        }
        User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        Optional.ofNullable(user).orElseThrow(() -> new ZSYServiceException("用户不存在"));
        //校验用户状态
        if (user.getIsDelete() != 0) {
            throw new ZSYServiceException("用户已删除,操作失败");
        }
        User modifyUser = new User();
        modifyUser.setId(userId);
        modifyUser.setPassword(MD5Helper.convert(
                String.format("%s%s", SHA1Helper.Sha1(ZSYConstants.DEFAULT_PASSWORD),
                        ZSYConstants.HINT_PASSWORD_KEY), 32, false));
        if (userMapper.updateSelectiveById(modifyUser) == 0) {
            throw new ZSYServiceException("重置密码失败");
        }

    }

    @Override
    public UserResDTO getUserById(Long userId) {
        UserBo user = userMapper.selectUserBOById(userId);
//        User user = userMapper.selectById(userId);
        UserResDTO userResDTO = new UserResDTO();
        if (user == null) {
            throw new ZSYServiceException(String.format("用户(%s)不存在", userId));
        }
        userResDTO.setDeptName(user.getDepartment().getName());
        userResDTO.setStatusName(ZSYUserStatus.getName(user.getStatus()));
        userResDTO.setJobRoleName(ZSYJobRole.getName(user.getJobRole()));
        if (user.getLevel() != null){
            userResDTO.setLevelName(ZSYUserLevel.getName(user.getLevel()));
        }
        userResDTO.setUserRoleName(ZSYUserRole.getName(user.getUserRole()));
        List<UserCheckPeopleBO> userCheckPeopleBOS = userMapper.selectUserCheckPeopleByUserId(userId);
        List<UserCheckPeopleResDTO> checkPeopleResDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userCheckPeopleBOS)){
            userCheckPeopleBOS.stream().forEach(userCheckPeopleBO -> {
                UserCheckPeopleResDTO resDTO = new UserCheckPeopleResDTO();
                resDTO.setId(userCheckPeopleBO.getCheckUserId());
                resDTO.setName(userCheckPeopleBO.getCheckUserName());
                resDTO.setLevel(userCheckPeopleBO.getLevel());
                checkPeopleResDTOS.add(resDTO);
            });

        }
        BeanUtils.copyProperties(user, userResDTO);
        userResDTO.setCheckUsers(checkPeopleResDTOS);
        return userResDTO;
    }


    /**
     * 对象深度拷贝
     *
     * @param children 对象中的集合
     * @return
     */
    private List<Long> deepCopyDeptIds(List<DeptBo> children) {
        List<Long> ids = new ArrayList<>();
        children.stream().forEach(child -> {
            ids.add(child.getId());
            ids.addAll(deepCopyDeptIds(child.getChildren()));
        });
        return ids;
    }

    /**
     * 验证邮件
     */
    public void validateEmail(String validateEmail){
        User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
            String email =MD5Helper.convert(
                    String.format("%s%s", SHA1Helper.Sha1(user.getEmail()),
                            ZSYConstants.HINT_EMAIL_KEY), 32, false);
            if(!validateEmail.equals(email)){
                throw new ZSYServiceException("邮箱验证失败，请检查后重试");
            }

        user.setStatus(ZSYUserStatus.NORMAL.getValue());
        if (userMapper.updateSelectiveById(user) == 0) {
            throw new ZSYServiceException("更新用户信息失败,请稍后重试.");
        }
    }

    /**
     * 重新发送邮件
     */
    public void sendEmail(){
        User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        MailHelper.send(user.getEmail());
    }

    // sch --

    /**
     * 用户修改个人基本信息
     * @param userInfoReqDTO
     */
    @Override
    public void modifyBasicInfo(UserInfoReqDTO userInfoReqDTO) {
        User user = new User();
        BeanUtils.copyProperties(userInfoReqDTO,user);
        user.setId(ZSYTokenRequestContext.get().getUserId());
        if (userMapper.updateSelectiveById(user) == 0){
            throw new ZSYServiceException("更新个人基本信息失败");
        }
    }

    /**
     * 查看当前用户管制下的用户
     * @author sch
     * @param checkUserId
     * @return
     */
    @Override
    public List<EffectUserResDTO> getControlledPeopleList(Long checkUserId) {
        List<UserCheckPeopleBO> userCheckPeopleBOS = userMapper.selectUserByCheckUserId(checkUserId);
        List<EffectUserResDTO> controlledPeoples = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userCheckPeopleBOS)){
            userCheckPeopleBOS.stream().forEach(userCheckPeopleBO -> {
                EffectUserResDTO res = new EffectUserResDTO();
                res.setId(userCheckPeopleBO.getUserId());
                res.setName(userCheckPeopleBO.getCheckUserName());
                controlledPeoples.add(res);
            });
        }
        return controlledPeoples;
    }

    /**
     * 查看项目管理者
     * @author sch
     */
    @Override
    public List<EffectUserResDTO> getProductManagers() {
        List<User> users = userMapper.selectManagers();
        List<EffectUserResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(users)){
            users.forEach(user -> {
                EffectUserResDTO resDTO = new EffectUserResDTO();
                resDTO.setId(user.getId());
                resDTO.setName(user.getName());
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 修改用户调休时间
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    @Transactional
    public void updateUserRestHours(EditUserRestHoursReqDTO reqDTO) {
        User user = userMapper.selectById(reqDTO.getUserId());
        if (user == null){
            throw new ZSYServiceException("用户不存在");
        }
        user.setRestHours(reqDTO.getRestHours());
        userMapper.updateSelectiveById(user);

        //新增调休日志
        UserRestHoursLog restHoursLog = new UserRestHoursLog();
        restHoursLog.setId(snowFlakeIDHelper.nextId());
        restHoursLog.setUserId(user.getId());
        restHoursLog.setUserName(user.getName());
        restHoursLog.setRestHours(reqDTO.getRestHours());
        restHoursLog.setType(ZSYRestHoursType.MANUAL.getValue());
        restHoursLog.setContent("管理员手动重置");
        restHoursLog.setCreateTime(new Date());
        restHoursLog.setRecordTime(new Date());
        restHoursLogMapper.insert(restHoursLog);
    }

    /**
     *  查询用户是否是组内领导
     *
     * @param userId
     * @return
     * @author hsh
     * @create 2020/11/5-15:58
     */
    @Override
    public Boolean getIsGroupLeader(Long userId) {
        UserGroup userGroup = userGroupMapper.selectByUserId(userId);
        if(userGroup == null || userGroup.getGroupId() == null){
            throw new ZSYServiceException("用户组不存在");
        }
        WorkGroup workGroup = groupMapper.selectById(userGroup.getGroupId());
        if(workGroup == null){
            throw new ZSYServiceException("用户组不存在");
        }
        if(userId.longValue() == workGroup.getLeader().longValue()){
            return true;
        }
        return false;
    }

    // -- sch
}
