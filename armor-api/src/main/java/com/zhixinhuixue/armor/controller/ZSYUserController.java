package com.zhixinhuixue.armor.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Akuma on 2017/8/7.
 */
@RestController
@RequestMapping("/user")
public class ZSYUserController extends ZSYController {

    private static final Logger logger = LoggerFactory.getLogger(ZSYUserController.class);

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
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 用户登陆
     * @param userLoginReqDTO 登陆信息
     * @return
     */
    @PostMapping("/login")
    public ZSYResult login(@RequestBody UserLoginReqDTO userLoginReqDTO){

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
                .withClaim("userId", 1L)
                .withClaim("userName", "John Doe")
                .withArrayClaim("permissions",new String[]{"a","b","c"})
                .sign(algorithm);

        String loginKey = String.format(ZSYConstants.LOGIN_KEY,1L);
        stringRedisTemplate.opsForValue().set(loginKey,"1");
//        stringRedisTemplate.expire(loginKey,ZSYConstants.LOGIN_KEY_EXPIRE_DAYS, TimeUnit.DAYS);
        logger.info("{}({})登录成功,token:{}",1L,"John Doe",jwt);
        return ZSYResult.success().msg("登录成功").data(jwt);
    }


}
