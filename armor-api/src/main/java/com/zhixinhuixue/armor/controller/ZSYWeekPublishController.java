package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.service.IZSYWeekPublishService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sch
 * @DATE 2019/6/11 9:55
 */
@Api(value = "周发版计划接口",description="周发版计划接口",tags = "/week-publish")
@RequestMapping("/api/week-publish")
@RestController
public class ZSYWeekPublishController {
    @Autowired
    private IZSYWeekPublishService weekPublishService;

}
