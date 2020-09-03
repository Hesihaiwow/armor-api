package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.WeekPublishPlan;

/**
 * @author sch
 * @time 2020/9/1 17:43
 */
public class WeekPublishPlanBO extends WeekPublishPlan {
    /**
     * 创建人姓名
     */
    private String createName;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
}
