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
     * UTC时间格式 年-月-日 时:分:秒
     */
    public static final String DATETIME_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

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
        String dayFirst = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuilder str = new StringBuilder().append(dayFirst).append(" ").append(TIME_BEGIN);
        return str.toString();
    }

    /**
     * 查询今天结束时间
     * @return
     */
    public static String getTodayEndTime(){
        Calendar calendar = Calendar.getInstance();
        String dayFirst = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuilder str = new StringBuilder().append(dayFirst).append(" ").append(TIME_END);
        return str.toString();
    }

    /**
     * 本星期第一天
     * @return
     */
    public static String getThisWeekFirstDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        String dayFirst = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuilder str = new StringBuilder().append(dayFirst).append(" ").append(TIME_BEGIN);
        return str.toString();
    }


    /**
     * 本星期最后一天
     * @return
     */
    public static String getThisWeekLastDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.DAY_OF_WEEK);
        String dayFirst = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuilder str = new StringBuilder().append(dayFirst).append(" ").append(TIME_END);
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

        String dayFirst = dateFormatter(gcLast.getTime(), DATE_FORMAT);
        StringBuilder str = new StringBuilder().append(dayFirst).append(" ").append(TIME_BEGIN);
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
        StringBuilder str = new StringBuilder().append(s).append(" ").append(TIME_END);
        return str.toString();

    }

    /**
     * 当季度第一天
     * @return
     */
    public static String  getThisQuarterFirstDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                calendar.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                calendar.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                calendar.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                calendar.set(Calendar.MONTH, 9);
            calendar.set(Calendar.DATE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuilder str = new StringBuilder().append(s).append(" ").append(TIME_BEGIN);
        return str.toString();
    }

    /**
     * 当季度最后一天
     * @return
     */
    public static String  getThisQuarterLastDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                calendar.set(Calendar.MONTH, 2);
                calendar.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                calendar.set(Calendar.MONTH, 5);
                calendar.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                calendar.set(Calendar.MONTH, 8);
                calendar.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                calendar.set(Calendar.MONTH, 11);
                calendar.set(Calendar.DATE, 31);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = dateFormatter(calendar.getTime(), DATE_FORMAT);
        StringBuilder str = new StringBuilder().append(s).append(" ").append(TIME_END);
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
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
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

    /**
     * 时间戳转日期
     * @param date
     * @return
     */
    public static Date timestampToDate(String date){
        return new Date(Long.valueOf(date));
    }


    /**
     * 获取当前第几周
     * @return
     */
    public static int getCurrentWeekNumber(){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前第几周
     * @return
     */
    public static int getCurrentWeekNumber(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        if(getYears(date)!=2017){
            return calendar.get(Calendar.WEEK_OF_YEAR);
        }
        return calendar.get(Calendar.WEEK_OF_YEAR)-1;
    }

    /**
     * 获取年份
     * @return
     */
    public static int getYears(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 查询两个日期间的天数
     *
     * @return
     */
    public static List<String> getDaysBetweenTwoDate(Date start, Date end) {
        List<String> days = Lists.newArrayList();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);

        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            days.add(dateFormatter(startCal.getTime(), DATE_FORMAT));
            startCal.add(Calendar.DATE, 1);
        }
        return days;
    }

    /**
     * 获取指定月份第一天
     * @param yearAndMonth 年月 "yyyy-MM"
     * @return Date
     */
    public static Date getFirstDayOfMonth(String yearAndMonth){
        String[] arr = yearAndMonth.split("-");

        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);

        return cal.getTime();
    }

    /**
     * 获取指定月份最后一天
     * @param yearAndMonth 年月 "yyyy-MM"
     * @return Date
     */
    public static Date getLastDayOfMonth(String yearAndMonth){
        String[] arr = yearAndMonth.split("-");

        int year = Integer.parseInt(arr[0]);
        int month = Integer.parseInt(arr[1]);

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return cal.getTime();
    }
}
