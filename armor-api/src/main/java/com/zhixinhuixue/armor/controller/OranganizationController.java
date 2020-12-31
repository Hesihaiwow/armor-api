package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.bo.OranganizationBo;
import com.zhixinhuixue.armor.model.dto.request.OrgReqDTO;
import com.zhixinhuixue.armor.model.dto.request.OrgUserReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserReqDTO;
import com.zhixinhuixue.armor.model.pojo.Oranganization;
import com.zhixinhuixue.armor.service.IZSYOranganizatinService;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 机构管理
 *
 * @author hsh
 * @create 2020年12月24日
 */

@RequestMapping("/api/org")
@RestController
public class OranganizationController {

    @Autowired
    private IZSYOranganizatinService orgService;

    @GetMapping("/search/{pageNum}")
    public String search(@PathVariable("pageNum") Integer papgeNum){
        PageInfo<OranganizationBo> pageInfo = orgService.search(papgeNum);
        return ZSYResult.success().data(pageInfo).build();
    }


    @PostMapping("/add")
    public String search(@Valid @RequestBody OrgReqDTO orgReqDTO){
        orgService.addOrg(orgReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改机构
     *
     * @param orgReqDTO
     * @param orgId
     * @return
     */
    @PutMapping("/modify-org/{orgId}")
    public String modifyOrg(@Valid @RequestBody OrgReqDTO orgReqDTO,
                             @PathVariable("orgId") Long orgId) {
        orgService.modifyOrg(orgReqDTO,orgId);
        return ZSYResult.success().build();
    }


    /**
     * 用户添加
     *
     * @param userReqDTO 参数
     */
    @PostMapping("/add-user")
    public String add(@Valid @RequestBody OrgUserReqDTO userReqDTO) {
        orgService.addOrgUser(userReqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改超管
     *
     * @param userReqDTO 参数
     */
    @PostMapping("/modify-user")
    public String modifyUser(@Valid @RequestBody OrgUserReqDTO userReqDTO) {
        orgService.modifyUser(userReqDTO);
        return ZSYResult.success().build();
    }


    /**
     * 删除超管
     *
     */
    @DeleteMapping("/delete/{userId}/{orgId}")
    public String deleteUser(@PathVariable("userId") Long userId,@PathVariable("orgId") Long orgId) {
        orgService.deleteByUserId(userId,orgId);
        return ZSYResult.success().build();
    }

    /**
     * 删除机构
     */
    @DeleteMapping("/delete/{orgId}")
    public String deleteUser(@PathVariable("orgId") Long orgId) {
        orgService.deleteByOrgId(orgId);
        return ZSYResult.success().build();
    }

}