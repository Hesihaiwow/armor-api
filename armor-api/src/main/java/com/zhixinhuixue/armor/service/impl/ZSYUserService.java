package com.zhixinhuixue.armor.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYDepartmentMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYAuthException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.*;
import com.zhixinhuixue.armor.model.bo.DeptBo;
import com.zhixinhuixue.armor.model.bo.UserBo;
import com.zhixinhuixue.armor.model.dto.request.UploadAvatarReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserPwdReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserResDTO;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import com.zhixinhuixue.armor.source.enums.ZSYUserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    @Qualifier("primaryStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IZSYDepartmentMapper departmentMapper;

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
    public PageInfo<UserPageResDTO> userPage(long deptId, int pageIndex) {
        List<Long> deptIds = Lists.newArrayList();
        DeptBo deptBo = departmentMapper.selectRootDept(deptId);
        deptIds.add(deptBo.getId());
        deptIds.addAll(deepCopyDeptIds(deptBo.getChildren()));

        PageHelper.startPage(pageIndex, ZSYConstants.PAGE_SIZE);
        Page<UserBo> userBos = userMapper.selectPage(deptIds);
        Page<UserPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(userBos, page);
        userBos.stream().forEach(userBo -> {
            UserPageResDTO userPageResDTO = new UserPageResDTO();
            BeanUtils.copyProperties(userBo, userPageResDTO);
            userPageResDTO.setDeptName(userBo.getDepartment().getName());
            page.add(userPageResDTO);
        });
        PageInfo<UserPageResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }


    @Override
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
    }

    @Override
    public void modifyUser(UserReqDTO userReqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYAuthException("没有权限执行此操作");
        }

        User user = new User();
        BeanUtils.copyProperties(userReqDTO, user);
        user.setId(userReqDTO.getUserId());
        if (userMapper.updateSelectiveById(user) == 0) {
            throw new ZSYServiceException("更新用户失败");
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
    public void deleteUserById(Long userId) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYAuthException("没有权限执行此操作");
        }
        if (userMapper.deleteById(userId) == 0) {
            throw new ZSYServiceException("删除用户失败");
        }
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
        User user = userMapper.selectById(userId);
        UserResDTO userResDTO = new UserResDTO();
        if (user == null) {
            throw new ZSYServiceException(String.format("用户(%s)不存在", userId));
        }
        BeanUtils.copyProperties(user, userResDTO);
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

}
