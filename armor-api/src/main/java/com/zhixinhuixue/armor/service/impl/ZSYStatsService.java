package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYStatsMapper;
import com.zhixinhuixue.armor.dao.IZSYUserIntegralMapper;
import com.zhixinhuixue.armor.model.bo.UserCommentBo;
import com.zhixinhuixue.armor.model.bo.UserIntegralInfoBO;
import com.zhixinhuixue.armor.model.dto.request.CalculateReqDTO;
import com.zhixinhuixue.armor.model.dto.response.CalculateResDTO;
import com.zhixinhuixue.armor.model.dto.response.StatsPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserCommentsPageResDTO;
import com.zhixinhuixue.armor.service.IZSYStatsService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lang on 2017/9/4 0004.
 */
@Service
public class ZSYStatsService implements IZSYStatsService {

    @Autowired
    private IZSYStatsMapper statsMapper;

    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;

    @Override
    public List<StatsPageResDTO> getStats() {
        List<StatsPageResDTO> userIntegralHistoryBOS = statsMapper.getStats(ZSYTokenRequestContext.get().getDepartmentId());
        return userIntegralHistoryBOS;
    }


    /**
     * 积分奖金计算
     * @param calculateReqDTO
     * @return
     */
    @Override
    public List<CalculateResDTO> calculate(CalculateReqDTO calculateReqDTO){
        List<UserIntegralInfoBO> userIntegralInfoBOS = userIntegralMapper.getCalculate(calculateReqDTO.getStartTime(), calculateReqDTO.getEndTime(),calculateReqDTO.getDatumIntegral(),ZSYTokenRequestContext.get().getDepartmentId());
        //总绩效积分
        BigDecimal sumInt = new BigDecimal("0");
        for (UserIntegralInfoBO i:userIntegralInfoBOS){
            sumInt = sumInt.add(i.getIntegral()).subtract(calculateReqDTO.getDatumIntegral());
        }
        BigDecimal sumIntegral = sumInt;
        List<CalculateResDTO> calculateResDTOS = new ArrayList<>();
        BeanUtils.copyProperties(userIntegralInfoBOS, calculateResDTOS);
        userIntegralInfoBOS.stream().forEach(userIntegralInfoBO -> {
            CalculateResDTO calculateResDTO = new CalculateResDTO();
            BeanUtils.copyProperties(userIntegralInfoBO, calculateResDTO);
            calculateResDTO.setIntegral(userIntegralInfoBO.getIntegral().subtract(calculateReqDTO.getDatumIntegral()));
            BigDecimal x = calculateResDTO.getIntegral().multiply(calculateReqDTO.getBonus());
            calculateResDTO.setBonus(x.divide(sumIntegral,2, BigDecimal.ROUND_HALF_UP));
            calculateResDTOS.add(calculateResDTO);
        });


        return calculateResDTOS;
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
        Page<UserCommentBo> userCommentBos = statsMapper.selectUserCommentsPage(userId, grade, ZSYTokenRequestContext.get().getDepartmentId());
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
