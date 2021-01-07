package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @DATE 2019/5/5 14:17
 *
 * 用户创建任务审核人
 */
public class UserCheckPeople extends OrgIdField {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 审核人id
     */
    private Long checkUserId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 状态(0:可用,1:不可用)
     * @return
     */
    private Integer status;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(Long checkUserId) {
        this.checkUserId = checkUserId;
    }
}
