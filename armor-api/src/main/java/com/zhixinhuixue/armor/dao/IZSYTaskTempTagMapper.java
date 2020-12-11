package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.TaskTempTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author SCH
 * @create 2020年12月09日
 */
public interface IZSYTaskTempTagMapper {
    /**
     * 批量插入
     *
     * @param list 集合
     */
    void insertBatch(@Param("list") List<TaskTempTag> list);

    /**
     * 根据临时任务id删除
     *
     * @param ttId 临时任务id
     */
    void deleteByTtId(@Param("ttId") Long ttId);

    /**
     * 根据临时任务id查询集合
     *
     * @param ttId 临时任务id
     * @return List<TaskTempTag>
     */
    List<TaskTempTag> selectByTtId(@Param("ttId") Long ttId);
}
