package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.UserCommentBo;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
public interface IZSYStatsMapper {

    /**
     * 获取任务统计
     *
     * @return
     */
    List<StatsPageResDTO> getStats(Long id);

    /**
     * 统计用户评论记录
     * @param userId
     * @param grade
     * @return
     */
    Page<UserCommentBo> selectUserCommentsPage(@Param("userId") Long userId, @Param("grade") String grade, @Param("departmentId") Long departmentId);
}
