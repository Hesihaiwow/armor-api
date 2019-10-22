package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.pojo.UserRestHoursLog;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

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
    BigDecimal selectUsedHours(@Param("userId") Long userId, @Param("year") int year);

    /**
     * 查询总的调休时间
     * @param userId 用户
     * @param year 年份
     */
    BigDecimal selectTotalRestHours(@Param("userId") Long userId, @Param("year") int year);

    /**
     * 批量新增
     * @param list 集合
     */
    void insertBatch(@Param("list") List<UserRestHoursLog> list);

    /**
     * 根据id查询
     * @param logId 日志id
     * @return
     */
    UserRestHoursLog selectById(@Param("logId") Long logId);

    /**
     * 删除调休日志
     * @param logId 日志id
     */
    void deleteById(@Param("logId") Long logId);

    /**
     * 修改调休日志
     * @param restHoursLog 日志
     */
    int updateById(UserRestHoursLog restHoursLog);
}
