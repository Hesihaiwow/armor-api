package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYUserIntegralMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.UserIntegralHistoryBO;
import com.zhixinhuixue.armor.model.bo.UserIntegralInfoBO;
import com.zhixinhuixue.armor.model.dto.request.IntegralResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralHistoryPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralResDTO;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.UserIntegral;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYIntegralOrigin;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYIntegralService implements IZSYIntegralService {


    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 用户积分排名表
     *
     * @param startTime 结束时间
     * @param endTime   页码
     * @return
     */
    @Override
    public List<IntegralPageResDTO> getIntegralPage(String startTime, String endTime) {
        List<UserIntegralInfoBO> userIntegralInfoBOS = userIntegralMapper.getIntegralPage(startTime, endTime);
        List<IntegralPageResDTO> integralPageResDTOS = new ArrayList<>();
        BeanUtils.copyProperties(userIntegralInfoBOS, integralPageResDTOS);
        userIntegralInfoBOS.stream().forEach(userIntegralInfoBO -> {
            IntegralPageResDTO integralPageResDTO = new IntegralPageResDTO();
            BeanUtils.copyProperties(userIntegralInfoBO, integralPageResDTO);
            integralPageResDTOS.add(integralPageResDTO);
        });
        return integralPageResDTOS;
    }

    /**
     * 计算不同时间个人积分排行
     *
     * @return
     */
    @Override
    public UserIntegralResDTO getUserIntegral() {
        Long id = ZSYTokenRequestContext.get().getUserId();
        UserIntegralResDTO userIntegralResDTO = new UserIntegralResDTO();
        userIntegralResDTO.setId(id);
        userIntegralResDTO.setWeek(userIntegralMapper.getUserIntegral(DateHelper.getThisWeekFirstDay(), DateHelper.getThisWeekLastDay(), id));
        userIntegralResDTO.setMonth(userIntegralMapper.getUserIntegral(DateHelper.getThisMonthFirstDay(), DateHelper.getThisMonthLastDay(), id));
        userIntegralResDTO.setYear(userIntegralMapper.getUserIntegral(DateHelper.dateFormatter(DateHelper.getCurrYearFirst(), DateHelper.DATETIME_FORMAT), DateHelper.dateFormatter(DateHelper.getCurrYearLast(), DateHelper.DATETIME_FORMAT), id));
        Integer quarterRank = userIntegralMapper.getRank(DateHelper.getThisQuarterFirstDay(), DateHelper.getThisQuarterLastDay(), id);//季度排名为空设为0
        userIntegralResDTO.setQuarterRank(quarterRank != null ? quarterRank : 0);
        Integer yearRank = userIntegralMapper.getRank(DateHelper.dateFormatter(DateHelper.getCurrYearFirst(), DateHelper.DATETIME_FORMAT), DateHelper.dateFormatter(DateHelper.getCurrYearLast(), DateHelper.DATETIME_FORMAT), id);//年度排名为空设为0
        userIntegralResDTO.setYearRank(yearRank != null ? yearRank : 0);
        return userIntegralResDTO;
    }

    /**
     * 个人积分记录
     *
     * @param id
     * @param pageIndex
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public PageInfo<IntegralHistoryPageResDTO> getIntegralHistory(Long id, int pageIndex, String startTime, String endTime) {
        PageHelper.startPage(pageIndex, ZSYConstants.PAGE_SIZE);
        Page<UserIntegralHistoryBO> userIntegralHistoryBOS = userIntegralMapper.getIntegralHistory(id, startTime, endTime);
        Page<IntegralHistoryPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(userIntegralHistoryBOS, page);
        userIntegralHistoryBOS.stream().forEach(userIntegralHistoryBO -> {
            IntegralHistoryPageResDTO integralHistoryPageResDTO = new IntegralHistoryPageResDTO();
            BeanUtils.copyProperties(userIntegralHistoryBO, integralHistoryPageResDTO);
            integralHistoryPageResDTO.setCreateTime(userIntegralHistoryBO.getCreateTime());
            page.add(integralHistoryPageResDTO);
        });
        PageInfo<IntegralHistoryPageResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 手动添加积分记录
     *
     * @param integralResDTO
     */
    public void addIntegral(IntegralResDTO integralResDTO) {
        User user = userMapper.selectById(integralResDTO.getUserId());
        //积分记录添加
            BigDecimal integral = user.getIntegral().add(integralResDTO.getIntegral());
            user.setIntegral(integral);
            if (userMapper.updateSelectiveById(user) == 0) {
                throw new ZSYServiceException("用户积分更新失败");
            }
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setId(snowFlakeIDHelper.nextId());
            userIntegral.setCreateTime(new Date());
            userIntegral.setIntegral(integralResDTO.getIntegral());
            userIntegral.setUserId(integralResDTO.getUserId());
            userIntegral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            userIntegral.setDescription(integralResDTO.getDescription());
            userIntegral.setOrigin(ZSYIntegralOrigin.ARTIFICIAL.getValue());//手动添加
            userIntegralMapper.insert(userIntegral);
    }


}
