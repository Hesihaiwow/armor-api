package com.zhixinhuixue.armor.model.dto.response;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author sch
 * @time 2020/3/12 17:12
 */
public class EditSignInReqDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 打卡时间
     */
    @NotNull(message = "打卡时间不能为空")
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
