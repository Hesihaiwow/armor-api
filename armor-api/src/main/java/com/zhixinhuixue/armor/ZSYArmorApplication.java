package com.zhixinhuixue.armor;

import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("com.zhixinhuixue.armor.dao")
@SpringBootApplication
public class ZSYArmorApplication {


	public static void main(String[] args) {
		SpringApplication.run(ZSYArmorApplication.class, args);
	}

	/**
	 * 全局唯一ID
	 * @return
	 */
	@Bean
	public SnowFlakeIDHelper snowFlakeIDHelper() {
		return new SnowFlakeIDHelper(0L, 0L);
	}
}
