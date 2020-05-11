package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author sch
 * @time 2020/5/11 15:02
 * 导出月度加班,调休统计
 */
public class ExportLeaveAndEworkReqDTO {
    /**
     * 时间
     */
    @NotNull(message = "年月不能为空")
    private Date yearAndMonth;

    public Date getYearAndMonth() {
        return yearAndMonth;
    }

    public void setYearAndMonth(Date yearAndMonth) {
        this.yearAndMonth = yearAndMonth;
    }
}
