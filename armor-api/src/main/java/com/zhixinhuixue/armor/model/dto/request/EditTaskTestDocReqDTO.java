package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;

/**
 * @author sch
 * @time 2020/5/11 16:39
 */
public class EditTaskTestDocReqDTO {
    /**
     * 任务id
     */
    @NotNull(message = "关联任务不能为空")
    private Long taskId;

    /**
     * 测试用例文档url
     */
    private String testDoc;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTestDoc() {
        return testDoc;
    }

    public void setTestDoc(String testDoc) {
        this.testDoc = testDoc;
    }
}
