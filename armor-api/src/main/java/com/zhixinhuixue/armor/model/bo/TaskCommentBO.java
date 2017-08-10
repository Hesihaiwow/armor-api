package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.TaskComment;

/**
 * Created by Tate on 2017/8/10.
 */
public class TaskCommentBO extends TaskComment {

    /**
     * 评论者姓名
     */
    private String commentUserName;

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }
}
