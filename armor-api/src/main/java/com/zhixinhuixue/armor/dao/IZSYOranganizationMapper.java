package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.bo.OranganizationBo;
import com.zhixinhuixue.armor.model.dto.request.OrgReqDTO;
import com.zhixinhuixue.armor.model.pojo.Oranganization;
import org.apache.ibatis.annotations.Param;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.security.access.method.P;

/**
 * 机构mapper
 *
 * @author hsh
 * @create 2020年12月24日
 */
public interface IZSYOranganizationMapper {

    /**
     * 分页查询机构
     *
     * @return
     */
    Page<OranganizationBo> selectOrgByPage();

    /**
     * 根据机构名称选择机构
     *
     * @param name 机构名称
     * @return
     */
    Oranganization selectByName(@Param("name") String name);

    /**
     * 将机构插入到机构表中
     *
     * @param org
     */
    void insert(@Param("org")Oranganization org);

    /**
     * 根据id选取机构
     *
     * @param orgId
     * @return
     */
    Oranganization selectById(@Param("orgId") Long orgId);

    /**
     * 根据id,更新机构名称和描述
     *
     * @param orgReqDTO
     * @param orgId
     * @return
     */
    Integer updateOrg(@Param("orgReqDTO") OrgReqDTO orgReqDTO, @Param("orgId") Long orgId);

    /**
     * 更新org的超管ID
     *
     * @param orgId
     * @param id
     * @return
     */
    Integer updateOrgUser(@Param("orgId") Long orgId, @Param("userId") Long id);

    /**
     * 删除机构
     *
     * @param orgId
     */
    void deleteOrgById(@Param("orgId")Long orgId);
}