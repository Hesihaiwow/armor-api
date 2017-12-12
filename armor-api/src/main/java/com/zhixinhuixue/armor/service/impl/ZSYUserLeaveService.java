package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYUserLeaveMapper;
import com.zhixinhuixue.armor.dao.IZSYUserWeekMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.UserLeaveBO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveReqDTO;
import com.zhixinhuixue.armor.model.dto.response.UserLeaveResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserWeekResDTO;
import com.zhixinhuixue.armor.model.pojo.UserLeave;
import com.zhixinhuixue.armor.model.pojo.UserWeek;
import com.zhixinhuixue.armor.service.IZSYUserLeaveService;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserLeaveType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/12/7 0007.
 */
@Service
public class ZSYUserLeaveService implements IZSYUserLeaveService {

    @Autowired
    private IZSYUserLeaveMapper userLeaveMapper;

    @Autowired
    private IZSYUserWeekMapper userWeekMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 添加请假记录
     * @param userLeaveReqDTO
     */
    @Override
    public void add(UserLeaveReqDTO userLeaveReqDTO) {
        UserLeave userLeave = new UserLeave();

        BeanUtils.copyProperties(userLeaveReqDTO,userLeave);
        userLeave.setId(snowFlakeIDHelper.nextId());
        userLeave.setUserId(ZSYTokenRequestContext.get().getUserId());
        userLeave.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
        userLeave.setCreateTime(new Date());
        userLeaveMapper.insertLeave(userLeave);

        List<UserWeek> userWeeks = Lists.newArrayList();
        UserWeek userWeek = new UserWeek();
        userWeek.setId(snowFlakeIDHelper.nextId());
        userWeek.setUserId(ZSYTokenRequestContext.get().getUserId());
        userWeek.setYear(DateHelper.getYears(userLeaveReqDTO.getBeginTime()));
        userWeek.setTaskId(userLeave.getId());
        userWeek.setHours(userLeaveReqDTO.getHours().doubleValue());
        userWeek.setWeekNumber(DateHelper.getCurrentWeekNumber(userLeaveReqDTO.getBeginTime())-1);
        userWeeks.add(userWeek);
        userWeekMapper.insertList(userWeeks);

    }

    /**
     * 获取请假信息
     * @param id
     * @return
     */
    @Override
    public UserLeaveResDTO getUserLeaveDetail(Long id) {
        UserLeaveBO userLeaveBO =  userLeaveMapper.getUserLeaveById(id);
        UserLeaveResDTO userLeaveResDTO = new UserLeaveResDTO();
        BeanUtils.copyProperties(userLeaveBO,userLeaveResDTO);
        userLeaveResDTO.setTypeName(ZSYUserLeaveType.getName(userLeaveResDTO.getType()));
        return userLeaveResDTO;
    }

    /**
     * 获取请假列表
     * @param userLeaveListReqDTO
     * @return
     */
    @Override
    public PageInfo<UserLeaveResDTO> getLeaveList(UserLeaveListReqDTO userLeaveListReqDTO) {
        PageHelper.startPage(userLeaveListReqDTO.getPageNum(), 10);
        Page<UserLeaveBO> userLeaveBOS = userLeaveMapper.getLeaveList(userLeaveListReqDTO);

        Page<UserLeaveResDTO> page = new Page<>();
        BeanUtils.copyProperties(userLeaveBOS, page);
        userLeaveBOS.stream().forEach(userLeaveBO -> {
            UserLeaveResDTO userLeaveResDTO = new UserLeaveResDTO();
            BeanUtils.copyProperties(userLeaveBO, userLeaveResDTO);
            userLeaveResDTO.setTypeName(ZSYUserLeaveType.getName(userLeaveResDTO.getType()));

            page.add(userLeaveResDTO);
        });

        PageInfo<UserLeaveResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 根据审核状态获取请假列表
     * @param status
     * @param pageIndex
     * @return
     */
    @Override
    public PageInfo<UserLeaveResDTO> getUserLeaveList(Integer status, Integer pageIndex) {
        PageHelper.startPage(pageIndex, 5);
        Long userId = ZSYTokenRequestContext.get().getUserId();
        Page<UserLeaveBO> userLeaveBOS = new Page<UserLeaveBO>();
        if(ZSYTokenRequestContext.get().getUserRole()==0){
             userLeaveBOS= userLeaveMapper.getLeaveByReviewStatus(null, status);
        }else{
             userLeaveBOS= userLeaveMapper.getLeaveByReviewStatus(userId, status);
        }
        Page<UserLeaveResDTO> page = new Page<>();
        BeanUtils.copyProperties(userLeaveBOS, page);
        userLeaveBOS.stream().forEach(userLeaveBO -> {
            UserLeaveResDTO userLeaveResDTO = new UserLeaveResDTO();
            BeanUtils.copyProperties(userLeaveBO, userLeaveResDTO);
            userLeaveResDTO.setTypeName(ZSYUserLeaveType.getName(userLeaveResDTO.getType()));

            List<UserWeekResDTO> userWeekResDTOS = new ArrayList<>();
            userLeaveBO.getUserWeeks().stream().forEach(userWeekBO -> {
                UserWeekResDTO userWeekResDTO = new UserWeekResDTO();
                BeanUtils.copyProperties(userWeekBO, userWeekResDTO);
                userWeekResDTOS.add(userWeekResDTO);
            });
            userLeaveResDTO.setUserWeeks(userWeekResDTOS);

            page.add(userLeaveResDTO);
        });
        PageInfo<UserLeaveResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 更新请假信息
     * @param userLeaveReqDTO
     * @param id
     */
    @Override
    public void updateLeaveDetail(UserLeaveReqDTO userLeaveReqDTO, Long id) {
        UserLeave userLeave = new UserLeave();
        UserLeaveBO userLeaveBO = userLeaveMapper.getUserLeaveById(id);
        userLeave.setId(id);
        userLeave.setUserId(userLeaveBO.getUserId());
        userLeave.setReviewStatus(userLeaveBO.getReviewStatus());
        userLeave.setCreateTime(userLeaveBO.getCreateTime());
        BeanUtils.copyProperties(userLeaveReqDTO,userLeave);
        if(userLeaveMapper.updateLeave(userLeave)==0){
            throw new ZSYServiceException("更新请假信息失败");
        }

        userWeekMapper.deleteByTaskId(id);
        List<UserWeek> userWeeks = Lists.newArrayList();
        UserWeek userWeek = new UserWeek();
        userWeek.setId(snowFlakeIDHelper.nextId());
        userWeek.setUserId(ZSYTokenRequestContext.get().getUserId());
        userWeek.setYear(DateHelper.getYears(userLeaveReqDTO.getBeginTime()));
        userWeek.setTaskId(userLeave.getId());
        userWeek.setHours(userLeaveReqDTO.getHours().doubleValue());
        userWeek.setWeekNumber(DateHelper.getCurrentWeekNumber(userLeaveReqDTO.getBeginTime())-1);
        userWeeks.add(userWeek);
        userWeekMapper.insertList(userWeeks);

    }

    /**
     * 删除请假申请
     * @param id
     */
    @Override
    public void deleteLeaveDetail(Long id) {
        userLeaveMapper.deleteById(id);
        userWeekMapper.deleteByTaskId(id);
    }

    /**
     * 通过审核
     * @param id
     */
    @Override
    public void passLeave(Long id) {
        UserLeaveBO userLeaveBO = userLeaveMapper.getUserLeaveById(id);
        UserLeave userLeave = new UserLeave();
        BeanUtils.copyProperties(userLeaveBO,userLeave);
        userLeave.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
        if(userLeaveMapper.updateLeave(userLeave)==0){
            throw new ZSYServiceException("更新审核信息失败");
        }
    }
}

