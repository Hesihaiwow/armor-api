package com.zhixinhuixue.armor.permit;

import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.model.dto.request.BasicUserReqDTO;
import org.springframework.security.core.Authentication;

/**
 * Created by Akuma on 2018/4/18.
 */
public class ZSYAuthorizationFactory {

    private ZSYAuthorizationFactory(){}

    /**
     * 获取许可证处理对象
     * @param authentication
     * @return
     */
    public static IZSYAuthorization createAuthorization(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (principal instanceof BasicUserReqDTO){
            return new ZSYBasicAuthorization(authentication);
        } else {
            throw new ZSYServiceException("无效的请求验证类型");
        }
    }

}
