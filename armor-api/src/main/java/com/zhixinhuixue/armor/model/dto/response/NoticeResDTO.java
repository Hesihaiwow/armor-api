package com.zhixinhuixue.armor.model.dto.response;

/**
 * Created by SCH on 2018-12-07
 */

import java.util.Date;

/**
 * 通知
 */
public class NoticeResDTO {
    /**
     * 通知id
     */
    private Long nid;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 已读状态(0:未读,1:已读)
     */
    private Integer status;

    /**
     * 通知生成时间
     */
    private Date createTime;

    /**
     * 查看时间
     * @return
     */
    private Date readTime;

    /**
     * 通知人
     * @return
     */
    private String noticeUser;

    public String getNoticeUser() {
        return noticeUser;
    }

    public void setNoticeUser(String noticeUser) {
        this.noticeUser = noticeUser;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public Long getNid() {
        return nid;
    }

    public void setNid(Long nid) {
        this.nid = nid;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
