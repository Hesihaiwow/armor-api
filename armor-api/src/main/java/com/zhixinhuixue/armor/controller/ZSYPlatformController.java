package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.service.IZSYPlatformService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sch
 * @DATE 2019/6/10 16:27
 */
@Api(value = "发布平台接口",description="发布平台操作接口",tags = "/platform")
@RequestMapping("/api/platform")
@RestController
public class ZSYPlatformController {
    @Autowired
    private IZSYPlatformService platformService;

    @ApiOperation("/新增")
    @PostMapping("/add")
    public String add(@RequestParam String name){
        platformService.add(name);
        return ZSYResult.success().build();
    }

    @ApiOperation("列表展示")
    @GetMapping("/list")
    public String list(){
        return ZSYResult.success().data(platformService.list()).build();
    }

    /**
     * 删除
     * @param id 平台id
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id")Long id){
        platformService.delete(id);
        return ZSYResult.success().build();
    }
}
