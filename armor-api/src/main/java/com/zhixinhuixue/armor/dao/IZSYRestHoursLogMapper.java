package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.pojo.UserRestHoursLog;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author sch
 * @time 2019/10/10 9:55
 */
public interface IZSYRestHoursLogMapper {

    /**
     * 新增
     * * @author sch
     * @param restHoursLog 参数
     */
    void insert(UserRestHoursLog restHoursLog);

    /**
     * 分页查询用户调休修改日志
     * @author sch
     * @param userId 用户id
     */
    Page<UserRestHoursLog> selectUserRestHoursLogPage(@Param("userId") Long userId);

    /**
     * 查询请假扣除调休时间
     * @param userId 用户id
     */
    BigDecimal selectUsedHours(@Param("userId") Long userId);
}
