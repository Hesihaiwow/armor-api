package com.zhixinhuixue.armor.helper;

import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Akuma on 16/7/25.
 */
public class DateHelper {

    /**
     * 年-月-日 时:分:秒
     */
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 年-月-日
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 时间开始
     */
    public static final String TIME_BEGIN = "00:00:00";

    /**
     * 时间结束
     */
    public static final String TIME_END = "23:59:59";

    /**
     * 格式化日期
     * @param date
     * @param pattern
     * @return
     */
    public static String dateFormatter(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 查询今天开始时间
     * @return
     */
    public static String getTodayBeginTime(){
        Calendar calendar = Calendar.getInstance();
        String day_first = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuffer str = new StringBuffer().append(day_first).append(" ").append(TIME_BEGIN);
        return str.toString();
    }

    /**
     * 查询今天结束时间
     * @return
     */
    public static String getTodayEndTime(){
        Calendar calendar = Calendar.getInstance();
        String day_first = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuffer str = new StringBuffer().append(day_first).append(" ").append(TIME_END);
        return str.toString();
    }

    /**
     * 本星期第一天
     * @return
     */
    public static String getThisWeekFirstDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        String day_first = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuffer str = new StringBuffer().append(day_first).append(" ").append(TIME_BEGIN);
        return str.toString();
    }


    /**
     * 本星期最后一天
     * @return
     */
    public static String getThisWeekLastDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.DAY_OF_WEEK);
        String day_first = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuffer str = new StringBuffer().append(day_first).append(" ").append(TIME_BEGIN);
        return str.toString();
    }

    /**
     * 当月第一天
     * @return
     */
    public static String getThisMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);

        String day_first = dateFormatter(gcLast.getTime(), DATE_FORMAT);
        StringBuffer str = new StringBuffer().append(day_first).append(" ").append(TIME_BEGIN);
        return str.toString();
    }

    /**
     * 当月最后一天
     * @return
     */
    public static String getThisMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String s = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuffer str = new StringBuffer().append(s).append(" ").append(TIME_END);
        return str.toString();

    }

    /**
     * 查询这个月所有天数
     * @return
     */
    public static List<String> getDaysOfThisMonth(){
        List<String> days = Lists.newArrayList();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);

        int month = cal.get(Calendar.MONTH);
        while(cal.get(Calendar.MONTH) == month){
            days.add(dateFormatter(cal.getTime(), DATE_FORMAT));
            cal.add(Calendar.DATE, 1);
        }
        return days;
    }

    /**
     * 查询今年所有月数
     * @return
     */
    public static List<String> getMonthsOfThisYear(){
        List<String> months = Lists.newArrayList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 0);

        int year = cal.get(Calendar.YEAR);
        while(cal.get(Calendar.YEAR) == year){
            months.add(df.format(cal.getTime()));
            cal.add(Calendar.MONTH, 1);
        }
        return months;
    }


    /**
     * 日期时间字符串转秒
     * @param datetime
     * @return
     */
    public static Long stringToSecond(String datetime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = sdf.parse(datetime);
            return  parse.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取今年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取今年的最后一天
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }


    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 日期追加天数
     * @param day 添加的天数
     * @return
     */
    public static Date afterDate(Date base,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(base);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 时间字符串转日期
     * @param date
     * @param format
     * @return
     */
    public static Date formatDate(String date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
