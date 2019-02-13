package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.pojo.Task;

import java.util.List;

/**
 * @author SCH
 * @date 2019/2/12 15:14
 *
 * 年度已完成任务最耗时前十
 */
public class AnnualTop10Task {
    private List<Task> taskList;

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
