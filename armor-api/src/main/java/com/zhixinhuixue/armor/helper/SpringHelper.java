package com.zhixinhuixue.armor.helper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;

public class SpringHelper extends ApplicationObjectSupport {

    private static ApplicationContext applicationContext = null;

    @Override
    protected void initApplicationContext(ApplicationContext context)throws BeansException {
        super.initApplicationContext(context);
        if(SpringHelper.applicationContext == null){
            SpringHelper.applicationContext = context;
        }
    }

    public static ApplicationContext getAppContext() {
        return applicationContext;
    }

    public static  <T>  T getBean(String name,Class<T> clazz){
        return (T)getAppContext().getBean(name, clazz);
    }
}
