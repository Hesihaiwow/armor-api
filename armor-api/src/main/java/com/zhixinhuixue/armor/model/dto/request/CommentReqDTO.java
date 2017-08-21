package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Tate<i@ridog.me>
 */
public class CommentReqDTO {

    @NotNull(message = "taskId不能为空")
    private Long taskId;
    @Size(min = 1, message = "评论不能为空")
    @Valid
    private List<CommentSalveReqDTO> comments;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public List<CommentSalveReqDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentSalveReqDTO> comments) {
        this.comments = comments;
    }
}
