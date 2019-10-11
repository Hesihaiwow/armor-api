package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;

/**
 * @author sch
 * @DATE 2019/9/9 18:55
 */
public class UserTaskIntegralListResDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 作为排列序号
     */
    private Integer sort;

    /**
     * 用户
     */
    private Long userId;
    private String userName ;

    /**
     * 积分
     */
    private BigDecimal integral;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }
}
