package com.zhixinhuixue.armor.helper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhixinhuixue.armor.source.ZSYPageInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by Akuma on 16/2/3.
 */
public class PageProxy<T> implements InvocationHandler {

    private Object sub;
    private Integer pageIndex;
    private Map<String, Object> map;
    private HttpServletRequest req;

    public PageProxy(Object object, HttpServletRequest req, Map<String, Object> map) {
        this.sub = object;
        Integer pageIndex = (Integer) map.get("pageIndex");
        if (pageIndex == null) {
            this.pageIndex = new Integer(1);
        } else {
            this.pageIndex = pageIndex;
        }
        this.map = map;
        this.req = req;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Integer pageSize = (Integer) map.get("pageSize");
        PageHelper.startPage(this.pageIndex, pageSize == null ? 10 : pageSize);
        Page<T> list = (Page<T>) method.invoke(sub, args);
        ZSYPageInfo page = new ZSYPageInfo(list);
        map.put("page", page);
        map.put("pageRoot", this.req.getRequestURI());
        map.put("pageParam", RequestHelper.mapToUrl(this.req.getParameterMap()));
        return null;
    }

    /**
     * 返回代理的真实类
     *
     * @return
     */
    public Object proxyInstance() {
        return Proxy.newProxyInstance(this.sub.getClass().getClassLoader(), this.sub.getClass().getInterfaces(), this);
    }

}
