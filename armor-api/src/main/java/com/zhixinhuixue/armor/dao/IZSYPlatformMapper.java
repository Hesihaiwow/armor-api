package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Platform;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/10 16:29
 */
public interface IZSYPlatformMapper {

    /**
     * 校验是否同名
     * @author sch
     * @param name
     * @return
     */
    Platform selectByName(@Param("name") String name);

    /**
     * 新增平台
     * @author sch
     * @param platform
     * @return
     */
    int insert(@Param("platform") Platform platform);

    /**
     * 列表查询平台
     * @author sch
     * @return
     */
    List<Platform> selectList();

    /**
     * 根据id集合查询
     * @param list id集合
     * @return List<Platform>
     */
    List<Platform> selectByIds(@Param("list") List<Long> list);
}
