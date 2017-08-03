package com.zhixinhuixue.armor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zhixinhuixue.armor.dao")
@SpringBootApplication
public class ZSYArmorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZSYArmorApplication.class, args);
	}
}
