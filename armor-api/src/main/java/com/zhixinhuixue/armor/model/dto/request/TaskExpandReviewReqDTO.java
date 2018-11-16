package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/12/7 0007.
 */
public class TaskExpandReviewReqDTO {

    @NotNull(message = "申请id不能为空")
    private Long teId;

    @NotNull(message = "审核状态不能为空")
    private Integer status;

    private List<UserWeekReqDTO> weeks;

    public Long getTeId() {
        return teId;
    }

    public void setTeId(Long teId) {
        this.teId = teId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<UserWeekReqDTO> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<UserWeekReqDTO> weeks) {
        this.weeks = weeks;
    }
}
