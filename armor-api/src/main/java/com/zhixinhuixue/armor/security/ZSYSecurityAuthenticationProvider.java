package com.zhixinhuixue.armor.security;

import com.zhixinhuixue.armor.exception.ZSYAuthException;
import com.zhixinhuixue.armor.model.dto.request.UserLoginReqDTO;
import com.zhixinhuixue.armor.service.impl.ZSYUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Created by Akuma on 2018/3/7.
 */
public class ZSYSecurityAuthenticationProvider implements AuthenticationProvider {

    private ZSYUserService userService;

    public ZSYSecurityAuthenticationProvider(ZSYUserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            UserLoginReqDTO reqDTO = new UserLoginReqDTO();
            reqDTO.setPassword(password);
            reqDTO.setAccount(name);

            userService.userLogin(reqDTO);
        } catch (ZSYAuthException exception) {
            throw new BadCredentialsException(exception.getMessage());
        }
        return new UsernamePasswordAuthenticationToken(name, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
