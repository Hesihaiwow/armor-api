package com.zhixinhuixue.armor.controller;

import com.zhixinhuixue.armor.model.dto.request.AddPlatformReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditPlatformReqDTO;
import com.zhixinhuixue.armor.service.IZSYPlatformService;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @ApiOperation("列表展示")
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
