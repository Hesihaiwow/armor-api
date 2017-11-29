package com.zhixinhuixue.armor.helper;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Lang on 2017/11/23 0023.
 */
public class classParse {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            //返回给定字符串名的类 Class 对象
            //并创建此 Class 对象所表示的类的一个新实例
            Object object = Class.forName("com.zhixinhuixue.armor.helper.DateHelper").newInstance();
            //返回方法名为“testMethod”的一个 Method 对象，后面跟的是该方法参数
            Method method = object.getClass().getMethod("dateFormatter",
                    new Class[] { Date.class, String.class });
            //执行该方法
            method.invoke(object, new Object[]{new Date(),DateHelper.DATE_FORMAT});
            System.out.print(method.invoke(object, new Object[]{new Date(),DateHelper.DATE_FORMAT}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
