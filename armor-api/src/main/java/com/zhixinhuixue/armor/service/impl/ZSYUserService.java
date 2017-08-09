package com.zhixinhuixue.armor.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.MD5Helper;
import com.zhixinhuixue.armor.helper.SHA1Helper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserPwdReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Akuma on 2017/8/8.
 */
@Service
public class ZSYUserService implements IZSYUserService{

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
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ZSYResult<String> userLogin(UserLoginReqDTO userLoginReqDTO) {
        //验证登录用户
        User user = userMapper.selectByAccountAndPassword(userLoginReqDTO.getAccount(),
                MD5Helper.convert(
                        String.format("%s%s", SHA1Helper.Sha1(userLoginReqDTO.getPassword()),
                                ZSYConstants.HINT_PASSWORD_KEY), 32 , false));
        if (user == null){
            throw new ZSYServiceException("账号或密码错误");
        }
        //验证通过
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(jwtSecret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String jwt = JWT.create()
                .withIssuer(jwtIssuer)
                .withExpiresAt(DateHelper.afterDate(new Date(),jwtExp))
                .withIssuedAt(new Date())
                .withClaim("userId", user.getId())
                .withClaim("userName", user.getName())
                .withArrayClaim("permissions",new String[]{"a","b","c"})
                .sign(algorithm);

        String loginKey = String.format(ZSYConstants.LOGIN_KEY,user.getId());
        stringRedisTemplate.opsForValue().set(loginKey,ZSYConstants.REDIS_DEFAULT_VALUE);
        stringRedisTemplate.expire(loginKey,ZSYConstants.LOGIN_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
        logger.info("{}({})登录成功,token:{}",user.getName(),user.getId(),jwt);
        return ZSYResult.success().data(jwt);
    }


    @Override
    public void addUser(UserReqDTO userReqDTO) {

        //校验用户账户是否存在
        List<User> existUsers = userMapper.selectByAccount(userReqDTO.getAccount());
        if (existUsers.size()>0){
            throw new ZSYServiceException(String.format("用户账户[%s]已存在",userReqDTO.getAccount()));
        }

        User user = new User();
        BeanUtils.copyProperties(userReqDTO, user);
        user.setId(snowFlakeIDHelper.nextId());
        user.setCreateTime(new Date());
        user.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        user.setStatus(ZSYUserStatus.NORMAL.getValue());
        user.setPassword(MD5Helper.convert(
                String.format("%s%s", SHA1Helper.Sha1(ZSYConstants.DEFAULT_PASSWORD),
                        ZSYConstants.HINT_PASSWORD_KEY), 32 , false));
        userMapper.insertUser(user);
    }

    @Override
    public List<EffectUserResDTO> getEffectiveUsers() {
        List<User> users = userMapper.selectEffectiveUsers();
        List<EffectUserResDTO> effectUserResDTOS = Lists.newArrayList();
        users.stream().forEach(user -> {
            EffectUserResDTO effectUserResDTO = new EffectUserResDTO();
            BeanUtils.copyProperties(user,effectUserResDTO);
            effectUserResDTOS.add(effectUserResDTO);
        });
        return effectUserResDTOS;
    }

    @Override
    public void deleteUserById(Long userId) {
        int i = userMapper.deleteById(userId);
        if (i==0){
            throw new ZSYServiceException("删除用户失败");
        }
    }

    @Override
    public void modifyUserPassword(UserPwdReqDTO userPwdReqDTO) {
        User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        Optional.ofNullable(user).orElseThrow(()->new ZSYServiceException("用户不存在"));
        //校验用户状态
        if (user.getStatus()!=0||user.getIsDelete()!=0){
            throw new ZSYServiceException("账户冻结或已删除,操作失败");
        }
        //校验原始密码
        String secretOriginalPwd = MD5Helper.convert(
                String.format("%s%s", SHA1Helper.Sha1(userPwdReqDTO.getOriginalPassword()),
                        ZSYConstants.HINT_PASSWORD_KEY), 32 , false);
        if (!secretOriginalPwd.equals(user.getPassword())){
            throw new ZSYServiceException("旧密码不正确");
        }
        //校验通过,修改密码
        User modifyUser = new User();
        modifyUser.setId(ZSYTokenRequestContext.get().getUserId());
        String secretNewPwd = MD5Helper.convert(
                String.format("%s%s", SHA1Helper.Sha1(userPwdReqDTO.getNewPassword()),
                        ZSYConstants.HINT_PASSWORD_KEY), 32 , false);
        modifyUser.setPassword(secretNewPwd);
        int i = userMapper.updateSelectiveById(modifyUser);
        if (i==0){
            throw new ZSYServiceException("更新密码失败");
        }
    }
}
