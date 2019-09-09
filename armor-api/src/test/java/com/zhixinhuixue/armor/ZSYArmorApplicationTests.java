package com.zhixinhuixue.armor;

import com.zhixinhuixue.armor.helper.DateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZSYArmorApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
	}

	@Test
	public void test(){
		String weekFirstDayStr = DateHelper.getThisWeekFirstDay();
		System.out.println("weekFirstDayStr = " + weekFirstDayStr);

		double x = 89.0;
		double y = x/30;
		System.out.println("y = " + y);
		int xx = (int)Math.floor(y);
		System.out.println("xx = " + xx);
		double z = x%30;
		System.out.println("z = " + z);
	}
}
