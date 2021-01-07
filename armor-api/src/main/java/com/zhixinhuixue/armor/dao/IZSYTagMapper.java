package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.List;

public interface IZSYTagMapper {

    /**
     * 标签列表
     * @return
     */
    List<Tag> selectTag(Long id);

    /**
     *添加标签
     * @param record
     * @return
     */
    int insert(Tag record);

    /**
     *删除标签
     * @param id
     * @return
     */
    int deleteTag(Long id);

    /**
     * 标签条数
     * @return
     */
//    int countTag(@Param("orgId")Long orgId);

    /**
     * 验证标签是否存在
     * @param name
     * @return
     */
    int validateTag(@Param("name") String name, @Param("departmentId") Long departmentId);

    /**
     * 查询阶段ID是否被使用
     */
    int countTag(@Param("id") Long id,@Param("orgId")Long orgId);


}