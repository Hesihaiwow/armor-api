package com.zhixinhuixue.armor.model.pojo;

import java.math.BigDecimal;

/**
 * Created by Lang on 2017/11/7 0007.
 */
public class BugUser extends OrgIdField{

    private Long id;

    private Long userId;

    private BigDecimal integral;

    private Long bugId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public Long getBugId() {
        return bugId;
    }

    public void setBugId(Long bugId) {
        this.bugId = bugId;
    }
}
