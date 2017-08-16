package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.response.TagResDTO;
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
    private IZSYTagService tagService;

    /**
     * 获取标签列表
     * @return
     */
    @ApiOperation("标签列表")
    @GetMapping(value = "/list")
    public String getTag(){
        List<TagResDTO> tagDTOS = tagService.getTag();
        return ZSYResult.success().data(tagDTOS).build();
    }
    /**
     * 添加标签
     * @return
     */
    @ApiOperation("添加标签")
    @PostMapping(value = "/add")
    public String addTag(@RequestParam String name){
        Long id = tagService.addTag(name);
        return ZSYResult.success().data(id).build();
    }

    /**
     * 添加标签
     * @return
     */
    @ApiOperation("删除标签")
    @DeleteMapping(value = "/{id}")
    public String deleteTag(@PathVariable Long id){
        tagService.deleteTag(id);
        return ZSYResult.success().build();
    }

}
