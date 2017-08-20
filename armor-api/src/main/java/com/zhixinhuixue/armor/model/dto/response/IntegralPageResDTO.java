package com.zhixinhuixue.armor.model.dto.response;

/**
 * Created by Lang on 2017/8/10 0010.
 */
public class IntegralPageResDTO {

    //作为排列序号
    private long id;

    private String name ;

    private int integral;

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

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }
}
