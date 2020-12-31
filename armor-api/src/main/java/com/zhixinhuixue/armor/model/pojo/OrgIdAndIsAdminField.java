package com.zhixinhuixue.armor.model.pojo;

/**
 * TODO
 *
 * @author hsh
 * @create 2020年12月31日
 */
public class OrgIdAndIsAdminField {

    /**
     * 机构ID
     */
    private Long orgId;

    /**
     * 是否机构管理者
     */
    private Integer isAdmin;



    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}