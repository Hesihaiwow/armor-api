package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.helper.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author SCH
 * @date 2019/1/10 13:44
 */
public class TestService {
    public static void main(String[] args) throws ParseException {
        Date date = DateHelper.getCurrYearFirst();
        System.out.println("date = " + date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        System.out.println("format = " + format);
        format = format + " 23:59:59";
        System.out.println("format = " + format);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = sdf1.parse(format);
        System.out.println("newDate = " + newDate);
    }
}
