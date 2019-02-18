package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.pojo.ExtraWork;
import com.zhixinhuixue.armor.model.pojo.Task;

import java.util.List;

/**
 * @author SCH
 * @date 2019/2/18 10:18
 *
 * 加班申请详情
 */
public class ExtraWorkDetailResDTO extends ExtraWork {
    /**
     * 关联任务
     */
    private List<Task> tasks;

    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * 用户头像
     * @return
     */
    private String avatarUrl;

    /**
     * 用户名
     * @return
     */
    private String userName;

    /**
     * 用户id
     * @return
     */
    private Long userId;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
