package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Tate<i@ridog.me>
 */
public class CommentSalveDTO {
    @NotNull(message = "taskUserId不能为空")
    private Long taskUserId;
    @Size(max = 500, message = "评论内容最多{max}字")
    private String description;
    @NotNull(message = "评论不能为空")
    private String grade;

    public Long getTaskUserId() {
        return taskUserId;
    }

    public void setTaskUserId(Long taskUserId) {
        this.taskUserId = taskUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
