package com.zhixinhuixue.armor;

import com.zhixinhuixue.armor.helper.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZSYArmorApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
	}

	@Test
	public void test() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String weekFirstDayStr = DateHelper.getThisWeekFirstDay();
//		System.out.println("weekFirstDayStr = " + weekFirstDayStr);
//
//		double x = 89.0;
//		double y = x/30;
//		System.out.println("y = " + y);
//		int xx = (int)Math.floor(y);
//		System.out.println("xx = " + xx);
//		double z = x%30;
//		System.out.println("z = " + z);
//
//		long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-09-27 23:00:00").getTime();
//		long time1 = new Date().getTime();
//		long cha = (time-time1)/1000/3600/24;
//		BigDecimal cha2 = BigDecimal.valueOf(time-time1).divide(BigDecimal.valueOf(86400000),2,BigDecimal.ROUND_HALF_UP);
//		System.out.println("cha = " + cha);
//
//		if (cha2.compareTo(BigDecimal.ZERO)>=0 && cha2.compareTo(BigDecimal.valueOf(3))<0){
//			System.out.println("cha2 = " + cha2);
//		}
		Date createTime = dateFormat.parse("2019-09-25 00:00:00");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createTime);
		int createTimeYear = calendar.get(Calendar.YEAR);
		System.out.println("createTimeYear = " + createTimeYear);
		int createTimeMonth = calendar.get(Calendar.MONTH) + 1;
		System.out.println("createTimeMonth = " + createTimeMonth);
		int createTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println("createTimeDay = " + createTimeDay);
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println("i = " + i);


		Date beginTime = dateFormat.parse("2019-09-24 00:00:00");
		calendar.setTime(beginTime);
		int beginTimeYear = calendar.get(Calendar.YEAR);
		int beginTimeMonth = calendar.get(Calendar.MONTH) + 1;
		int beginTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
		LocalDate end = LocalDate.of(beginTimeYear, beginTimeMonth, beginTimeDay);
		LocalDate start = LocalDate.of(createTimeYear, createTimeMonth, createTimeDay);
		System.out.println("相差天数"+(end.toEpochDay() - start.toEpochDay() + 1));
	}
}
