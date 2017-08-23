package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;

/**
 * Created by Lang on 2017/8/10 0010.
 */
public class IntegralPageResDTO {

    //作为排列序号
    private long id;

    private String name ;

    private BigDecimal integral;

    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
