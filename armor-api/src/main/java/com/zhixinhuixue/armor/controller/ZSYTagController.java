package com.zhixinhuixue.armor.controller;


import com.zhixinhuixue.armor.model.dto.response.TagResDTO;
import com.zhixinhuixue.armor.service.IZSYTagService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签接口
 * Created by Lang on 2017/8/9 0007.
 */
@RequestMapping("/api/tag")
@RestController
public class ZSYTagController extends ZSYController {

    @Resource
    private IZSYTagService tagService;

    /**
     * 获取标签列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public String getTag() {
        List<TagResDTO> tagDTOS = tagService.getTag();
        return ZSYResult.success().data(tagDTOS).build();
    }

    /**
     * 添加标签
     *
     * @return
     */
    @PostMapping(value = "/add")
    public String addTag(@RequestParam String name) {
        Long id = tagService.addTag(name);
        return ZSYResult.success().data(id).build();
    }

    /**
     * 删除标签
     *
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public String deleteTag(@PathVariable("id") Long id) {
        tagService.deleteTag(id);
        return ZSYResult.success().build();
    }

}
