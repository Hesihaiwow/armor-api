package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @time 2020/6/10 14:11
 */
public class UserGroup {

    /**
     * id
     */
    private Long ugId;

    /**
     * 团队id
     */
    private Long groupId;

    /**
     * 成员id
     */
    private Long userId;

    public Long getUgId() {
        return ugId;
    }

    public void setUgId(Long ugId) {
        this.ugId = ugId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
