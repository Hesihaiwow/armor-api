package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author sch
 * @time 2020/6/10 14:55
 */
public class AddGroupUserReqDTO {

    /**
     * 团队id
     */
    @NotNull(message = "团队id不能为空")
    private Long groupId;

    /**
     * 成员id集合
     */
    private List<Long> userIds;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
