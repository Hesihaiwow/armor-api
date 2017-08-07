package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskUserMapper;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskUser;
import com.zhixinhuixue.armor.service.ITaskService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Tate on 2017/8/7.
 */
@Service
public class TaskService implements ITaskService {

    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskUserMapper taskUserMapper;

    /**
     * 添加任务
     *
     * @param task
     * @param taskUserList
     * @return
     */
    @Override
    @Transactional
    public ZSYResult addTask(Task task, List<TaskUser> taskUserList) {
        return null;
    }
}
