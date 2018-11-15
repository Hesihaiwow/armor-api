package com.zhixinhuixue.armor.permit;

import com.zhixinhuixue.armor.model.dto.request.BasicUserReqDTO;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Akuma on 2018/4/18.
 */
public class ZSYBasicAuthorization implements IZSYAuthorization {


    private Authentication authentication;

    /**
     * Basic验证白名单
     */
    private static final List<String> PASS_URLS = Arrays.asList(
    );

    public ZSYBasicAuthorization(Authentication authentication){
        this.authentication = authentication;
    }

    @Override
    public boolean isPermit(HttpServletRequest request) {
       return false;
    }

    @Override
    public BasicAuth isBasicPermit(HttpServletRequest request) {
        //BASIC
        BasicUserReqDTO basicUser = (BasicUserReqDTO) authentication.getPrincipal();
        String url = request.getRequestURI().replaceAll("\\d+","*");
        BasicAuth basicAuth = new BasicAuth();
        basicAuth.setPermit(true);

        //查找白名单
//        basicAuth.setPermit(PASS_URLS.stream().anyMatch(url::contains));
        if (!basicAuth.isPermit()){
            basicAuth.setErrMsg("用户无权限访问该资源.");
        }
        return basicAuth;
    }

}
