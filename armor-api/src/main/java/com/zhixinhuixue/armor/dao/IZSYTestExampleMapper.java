package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TestExample;
import org.apache.ibatis.annotations.Param;

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
}
