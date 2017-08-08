package com.zhixinhuixue.armor.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public class UUIDHelper {

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    public static long twUUID(){
        long id = UUID.randomUUID().getMostSignificantBits();
        return id;
    }




    public static void main(String[] args) {
        System.out.println(twUUID());
    }



}
