package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYUserIntegralMapper;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.UserIntegralInfoBO;
import com.zhixinhuixue.armor.model.dto.response.IntegralPageDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralDTO;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYIntegralService implements IZSYIntegralService{

    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;

    @Override
    public PageInfo<IntegralPageDTO> getIntegralPage(int pageIndex,String startTime,String endTime){
        PageHelper.startPage(pageIndex, ZSYConstants.PAGE_SIZE);
        Page<UserIntegralInfoBO> userIntegralInfoBOS = userIntegralMapper.getIntegralPage(startTime,endTime);
        Page<IntegralPageDTO> page = new Page<>();
        BeanUtils.copyProperties(userIntegralInfoBOS,page);
        userIntegralInfoBOS.stream().forEach(userIntegralInfoBO -> {
            IntegralPageDTO integralPageDTO = new IntegralPageDTO();
            BeanUtils.copyProperties(userIntegralInfoBO,integralPageDTO);
            integralPageDTO.setIntegral(userIntegralInfoBO.getIntegral());
            integralPageDTO.setName(userIntegralInfoBO.getName());
            page.add(integralPageDTO);
        });
        PageInfo<IntegralPageDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    @Override
    public UserIntegralDTO getUserIntegral(){
        long id = ZSYTokenRequestContext.get().getUserId();
        UserIntegralDTO userIntegralDTO = new UserIntegralDTO();
        userIntegralDTO.setWeek(userIntegralMapper.getUserIntegral(DateHelper.getThisWeekFirstDay(),DateHelper.getThisWeekLastDay(),id));
        userIntegralDTO.setMonth(userIntegralMapper.getUserIntegral(DateHelper.getThisMonthFirstDay(),DateHelper.getThisMonthLastDay(),id));
        userIntegralDTO.setYear(userIntegralMapper.getUserIntegral(DateHelper.dateFormatter(DateHelper.getCurrYearFirst(),DateHelper.DATETIME_FORMAT),DateHelper.dateFormatter(DateHelper.getCurrYearLast(),DateHelper.DATETIME_FORMAT),id));
        userIntegralDTO.setQuarterRank(userIntegralMapper.getRank(DateHelper.getThisQuarterFirstDay(),DateHelper.getThisQuarterLastDay(),id));
        userIntegralDTO.setYearRank(userIntegralMapper.getRank(DateHelper.dateFormatter(DateHelper.getCurrYearFirst(),DateHelper.DATETIME_FORMAT),DateHelper.dateFormatter(DateHelper.getCurrYearLast(),DateHelper.DATETIME_FORMAT),id));
        return userIntegralDTO;
    }


}
