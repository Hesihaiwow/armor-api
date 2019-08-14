package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TestFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/8/9 14:22
 */
public interface IZSYTestFunctionMapper {

    /**
     * 新增
     * @param testFunction
     * @return
     */
    int insert(TestFunction testFunction);

    /**
     * 根据任务id查询功能点
     * @param taskId
     * @return
     */
    List<TestFunction> selectListByTask(@Param("taskId") Long taskId);

    /**
     * 根据id查询
     * @param functionId
     * @return
     */
    TestFunction selectById(@Param("functionId") Long functionId);

    /**
     * 修改功能点
     * @param function
     * @return
     */
    int update(TestFunction function);

    /**
     * 删除功能点
     * @param functionId
     */
    void deleteById(@Param("functionId")Long functionId);

    /**
     * 批量新增
     * @param list
     */
    void insertBatch(@Param("list")List<TestFunction> list);
}
