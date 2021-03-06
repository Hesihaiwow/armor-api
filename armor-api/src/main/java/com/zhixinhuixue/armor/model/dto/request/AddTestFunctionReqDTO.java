package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author sch
 * @DATE 2019/8/9 14:18
 */
public class AddTestFunctionReqDTO {
    /**
     * 任务id
     */
    @NotNull(message = "关联任务id不能为空")
    private Long taskId;

    /**
     * 功能点名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
