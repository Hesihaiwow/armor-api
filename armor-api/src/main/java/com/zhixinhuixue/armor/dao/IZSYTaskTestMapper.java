package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskTest;

import java.util.List;

public interface IZSYTaskTestMapper {

    int insert(TaskTest taskTest);

    /**
     * 批量插入
     * @param taskTests
     * @return
     */
    int insertList(List<TaskTest> taskTests);

}