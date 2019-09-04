package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskReview;

/**
 * @author sch
 * @DATE 2019/9/3 15:16
 */
public class TaskReviewBO extends TaskReview {
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
