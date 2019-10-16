package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.TaskBugRemarkBO;
import com.zhixinhuixue.armor.model.pojo.TaskBugRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @time 2019/10/15 9:42
 */
public interface IZSYTaskBugRemarkMapper {
    /**
     * 新增
     * @param taskBugRemark 备注
     */
    void insert(TaskBugRemark taskBugRemark);

    /**
     * 查询备注集合
     * @param tbId bugId
     * @return
     */
    List<TaskBugRemarkBO> selectListByTbId(@Param("tbId") Long tbId);

    /**
     * 删除备注
     * @param tbId bugId
     */
    void deleteByTbId(@Param("tbId") Long tbId);
}
