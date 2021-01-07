package com.zhixinhuixue.armor.model.pojo;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/2/21 16:57
 *
 * 考勤打卡记录
 */
public class SignIn extends OrgIdField{
    /**
     * 主键
     */
    private Long id;

    /**
     * 打卡人id
     */
    private Long userId;

    /**
     * 打卡时间
     */
    private Date checkTime;

    /**
     * 类型(0:正常打卡,1:补打卡)
     */
    private Integer type;

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

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
