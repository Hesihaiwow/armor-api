package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.TestExampleBO;
import com.zhixinhuixue.armor.model.pojo.TestExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/8/6 9:42
 */
public interface IZSYTestExampleMapper {
    /**
     * 根据名称查询
     * @param name
     * @return
     */
    TestExample selectByNameAndTaskAndFunction(@Param("name") String name,@Param("taskId")Long taskId,@Param("functionId")Long functionId);

    /**
     * 新增
     * @param testExample
     * @return
     */
    int insert(TestExample testExample);

    /**
     * 根据功能点id查询测试用例
     * @param functionId
     * @return
     */
    List<TestExample> selectByFunction(@Param("functionId") Long functionId);

    /**
     * 查看测试用例详情
     * @param exampleId
     * @return
     */
    TestExampleBO selectDetailById(@Param("exampleId") Long exampleId);

    /**
     * 根据id删除
     * @param exampleId
     */
    void deleteById(@Param("exampleId") Long exampleId);

    /**
     * 根据主键查询
     * @param exampleId
     * @return
     */
    TestExample selectById(Long exampleId);

    /**
     * 更新
     * @param example
     * @return
     */
    int update(TestExample example);

    /**
     * 批量插入
     * @param exampleList
     */
    void insertBatch(@Param("exampleList") List<TestExample> exampleList);

    /**
     * 根据taskId查询测试用例
     * @param taskId
     * @return
     */
    List<TestExampleBO> selectListByTaskId(@Param("taskId")Long taskId);
}
