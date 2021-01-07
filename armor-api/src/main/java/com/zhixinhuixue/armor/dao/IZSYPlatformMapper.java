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
    Platform selectByName(@Param("name") String name,@Param("orgId")Long orgId);

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
     * @return List<Platform>
     */
    List<Platform> selectList(@Param("groupMark") Integer groupMark,@Param("orgId")Long orgId);

    /**
     * 根据id集合查询
     * @param list id集合
     * @return List<Platform>
     */
    List<Platform> selectByIds(@Param("list") List<Long> list);

    /**
     * 删除
     * @param id 平台id
     */
    void deleteById(@Param("id") Long id);

    /**
     * 根据id 查询
     * @param id 主键
     * @return Platform
     */
    Platform selectById(@Param("id") Long id);

    /**
     * 更新
     * @param platform 参数
     */
    void updateById(Platform platform);
}
