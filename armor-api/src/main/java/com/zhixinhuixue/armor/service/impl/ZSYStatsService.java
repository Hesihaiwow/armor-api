package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.dao.IZSYStatsMapper;
import com.zhixinhuixue.armor.model.bo.UserCommentBo;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserCommentsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Service
public class ZSYStatsService implements IZSYStatsService {

    @Autowired
    private IZSYStatsMapper statsMapper;

    @Override

    public List<StatsPageResDTO> getStats() {
        List<StatsPageResDTO> userIntegralHistoryBOS = statsMapper.getStats();
        return userIntegralHistoryBOS;
    }

    /**
     * 统计用户评论
     *
     * @param pageNum
     * @param userId
     * @param grade
     * @return
     */
    @Override
    public PageInfo<UserCommentsPageResDTO> findByPage(Integer pageNum, Long userId, String grade) {
        if (pageNum==null) {
            pageNum = 0;
        }
        PageHelper.startPage(pageNum, ZSYConstants.PAGE_SIZE_20);
        Page<UserCommentBo> userCommentBos = statsMapper.selectUserCommentsPage(userId, grade);
        Page<UserCommentsPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(userCommentBos, page);
        userCommentBos.stream().forEach(bo-> {
            UserCommentsPageResDTO dto = new UserCommentsPageResDTO();
            BeanUtils.copyProperties(bo, dto);
            page.add(dto);
        });
        PageInfo<UserCommentsPageResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }
}
