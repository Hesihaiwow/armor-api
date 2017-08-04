package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.dao.IZSYTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Akuma on 2017/8/2.
 */
@RestController
public class ZSYTestController {

    @Autowired
    private IZSYTestMapper testMapper;

    @GetMapping(value = "/test")
    public String test(){
        return testMapper.selectByPrimaryKey(1).getName();
    }


}
