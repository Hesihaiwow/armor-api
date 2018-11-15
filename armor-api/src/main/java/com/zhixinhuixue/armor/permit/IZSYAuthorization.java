package com.zhixinhuixue.armor.permit;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Akuma on 2018/4/18.
 */
public interface IZSYAuthorization {

    /**
     * 是否许可访问资源
     * @param request
     * @return
     */
    boolean isPermit(HttpServletRequest request);

    /**
     * 是否许可访问资源(basic)
     * @param request
     * @return
     */
    BasicAuth isBasicPermit(HttpServletRequest request);

}
