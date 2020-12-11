package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddPlatformReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditPlatformReqDTO;
import com.zhixinhuixue.armor.service.IZSYPlatformService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 发布平台接口
 * @author sch
 * @DATE 2019/6/10 16:27
 */
@RequestMapping("/api/platform")
@RestController
public class ZSYPlatformController {
    @Resource
    private IZSYPlatformService platformService;

    /**
     * 新增
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public String add(@Valid @RequestBody AddPlatformReqDTO reqDTO){
        platformService.add(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑
     * @param reqDTO 参数
     * @param id id
     */
    @PutMapping("/{id}")
    public String editPlatform(@Valid @RequestBody EditPlatformReqDTO reqDTO,@PathVariable("id")Long id){
        platformService.editPlatform(reqDTO,id);
        return ZSYResult.success().build();
    }

    /**
     * 列表展示
     * @param groupMark
     * @return
     */
    @GetMapping("/list")
    public String list(@RequestParam String groupMark){
        return ZSYResult.success().data(platformService.list(groupMark)).build();
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
