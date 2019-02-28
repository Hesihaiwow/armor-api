package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.pojo.SignIn;

import java.util.Date;

/**
 * @author SCH
 * @date 2019/2/22 14:15
 *
 * 导入考勤记录后,最新一条考勤记录
 */
public class SignInLastRecordResDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 打卡时间
     */
    private Date checkTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
}
