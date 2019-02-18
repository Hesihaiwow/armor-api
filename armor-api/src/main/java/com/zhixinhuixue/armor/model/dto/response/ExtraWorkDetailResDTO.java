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

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
