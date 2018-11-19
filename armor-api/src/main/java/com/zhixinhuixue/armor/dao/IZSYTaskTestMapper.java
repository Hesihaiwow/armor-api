package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.TaskBO;
import com.zhixinhuixue.armor.model.bo.TaskTestBO;
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

    /**
     * 查找是否建立测试时间
     * @param taskId
     * @return
     */
    int selectTesting(Long taskId);

    /**
     * 根据状态查询任务
     *
     * @param id
     * @return
     */
    List<TaskTestBO> selectTestTask(Long id);

}