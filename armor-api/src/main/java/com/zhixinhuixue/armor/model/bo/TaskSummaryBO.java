package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskSummary;

/**
 * @author sch
 * @DATE 2019/9/3 15:17
 */
public class TaskSummaryBO extends TaskSummary {
    /**
     * 创建人
     */
    private String createName;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
}
