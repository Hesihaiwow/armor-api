package com.zhixinhuixue.armor.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.bo.OranganizationBo;
import com.zhixinhuixue.armor.model.dto.request.OrgReqDTO;
import com.zhixinhuixue.armor.model.dto.request.OrgUserReqDTO;
import com.zhixinhuixue.armor.model.pojo.Oranganization;

/**
 * 机构服务
 *
 * @author hsh
 * @create 2020年12月24日
 */
public interface IZSYOranganizatinService {


    /**
     * 查询机构，展示机构
     *
     * @param papgeNum 页码
     * @return
     */
    PageInfo<OranganizationBo> search(Integer papgeNum);

    /**
     * 添加机构
     *
     * @param orgReqDTO
     */
    void addOrg(OrgReqDTO orgReqDTO);

    /**
     * 编辑机构
     *
     * @param orgReqDTO
     * @param orgId
     */
    void modifyOrg(OrgReqDTO orgReqDTO, Long orgId);

    /**
     * 为机构添加超管
     *
     * @param userReqDTO
     */
    void addOrgUser(OrgUserReqDTO userReqDTO);

    /**
     * 编辑超管
     *
     * @param userReqDTO
     */
    void modifyUser(OrgUserReqDTO userReqDTO);

    /**
     * 删除超管
     *
     * @param userId
     */
    void deleteByUserId(Long userId,Long orgId);

    /**
     * 删除机构
     *
     * @param orgId
     */
    void deleteByOrgId(Long orgId);

}