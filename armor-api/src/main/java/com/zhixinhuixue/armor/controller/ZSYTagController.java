package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.pojo.Project;
import com.zhixinhuixue.armor.model.pojo.Tag;
import com.zhixinhuixue.armor.service.IZSYProjectService;
import com.zhixinhuixue.armor.service.IZSYTagService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Lang on 2017/8/9 0007.
 */
@Api(value = "标签接口",description="标签相关操作接口",tags = "/tag")
@RequestMapping("/tag")
@RestController
public class ZSYTagController extends ZSYController{

    @Autowired
    private IZSYTagService izsyTagService;

    /**
     * 获取标签列表
     * @return
     */
    @ApiOperation("标签列表")
    @GetMapping(value = "/list")
    public String getTag(){
        List<Tag> tag = izsyTagService.getTag();
        return ZSYResult.success().data(tag).build();
    }
    /**
     * 添加标签
     * @return
     */
    @ApiOperation("添加标签")
    @PostMapping(value = "/add")
    public String addTag(@RequestParam String name){
        izsyTagService.addTag(name);
        return ZSYResult.success().build();
    }

    /**
     * 添加标签
     * @return
     */
    @ApiOperation("删除标签")
    @DeleteMapping(value = "/delete")
    public String deleteTag(@RequestParam String id){
        izsyTagService.deleteTag(id);
        return ZSYResult.success().build();
    }

}
