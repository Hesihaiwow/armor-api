package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskUser;
import com.zhixinhuixue.armor.source.ZSYResult;

import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
public interface ITaskService {

    /**
     * 添加任务
     *
     * @param task
     * @param taskUserList
     * @return
     */
    ZSYResult addTask(Task task, List<TaskUser> taskUserList);
}
