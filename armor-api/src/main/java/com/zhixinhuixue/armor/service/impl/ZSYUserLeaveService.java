package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYRestHoursLogMapper;
import com.zhixinhuixue.armor.dao.IZSYUserLeaveMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.dao.IZSYUserWeekMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.UserLeaveBO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveReqDTO;
import com.zhixinhuixue.armor.model.dto.response.UserLeaveResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserWeekResDTO;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.UserLeave;
import com.zhixinhuixue.armor.model.pojo.UserRestHoursLog;
import com.zhixinhuixue.armor.model.pojo.UserWeek;
import com.zhixinhuixue.armor.service.IZSYUserLeaveService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYRestHoursType;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserLeaveType;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.github.pagehelper.page.PageMethod.startPage;

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

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private IZSYRestHoursLogMapper restHoursLogMapper;

    /**
     * 添加请假记录
     * @param userLeaveReqDTO
     */
    @Override
    @Transactional
    public void add(UserLeaveReqDTO userLeaveReqDTO) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        if (userLeaveReqDTO.getType()==ZSYUserLeaveType.CHANGEREST.getValue()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userLeaveReqDTO.getBeginTime());
            BigDecimal totalRestHours = restHoursLogMapper.selectTotalRestHours(userId, calendar.get(Calendar.YEAR));
            BigDecimal usedHours = restHoursLogMapper.selectUsedHours(userId, calendar.get(Calendar.YEAR));
            BigDecimal leftHours = totalRestHours.add(usedHours);
            if (leftHours.subtract(userLeaveReqDTO.getHours()).compareTo(BigDecimal.ZERO)<0){
                throw new ZSYServiceException("用户当前所剩调休时间不足此次调休扣除,请选择其他请假类型");
            }
        }

        UserLeave userLeave = new UserLeave();

        BeanUtils.copyProperties(userLeaveReqDTO,userLeave);
        userLeave.setId(snowFlakeIDHelper.nextId());
        userLeave.setUserId(ZSYTokenRequestContext.get().getUserId());
        userLeave.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
        userLeave.setCreateTime(new Date());
        userLeave.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        userLeaveMapper.insertLeave(userLeave);

        List<UserWeek> userWeeks = Lists.newArrayList();
        UserWeek userWeek = new UserWeek();
        userWeek.setId(snowFlakeIDHelper.nextId());
        userWeek.setUserId(ZSYTokenRequestContext.get().getUserId());
        userWeek.setYear(DateHelper.getYears(userLeaveReqDTO.getBeginTime()));
        userWeek.setTaskId(userLeave.getId());
        userWeek.setHours(userLeaveReqDTO.getHours().doubleValue());
        userWeek.setWeekNumber(DateHelper.getCurrentWeekNumber(userLeaveReqDTO.getBeginTime()));
        userWeek.setOrgId(ZSYTokenRequestContext.get().getOrgId());
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
        UserLeaveBO userLeaveBO =  userLeaveMapper.getUserLeaveById(id,ZSYTokenRequestContext.get().getOrgId());
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
        startPage(userLeaveListReqDTO.getPageNum(), ZSYConstants.PAGE_SIZE);
        userLeaveListReqDTO.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
        if(userLeaveListReqDTO.getBeginTime()==null){
            userLeaveListReqDTO.setBeginTime(null);
        }
        if (userLeaveListReqDTO.getEndTime() == null){
            userLeaveListReqDTO.setEndTime(null);
        }
        userLeaveListReqDTO.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        Page<UserLeaveBO> userLeaveBOS = userLeaveMapper.getLeaveList(userLeaveListReqDTO);

        Page<UserLeaveResDTO> page = new Page<>();
        BeanUtils.copyProperties(userLeaveBOS, page);
        userLeaveBOS.forEach(userLeaveBO -> {
            UserLeaveResDTO userLeaveResDTO = new UserLeaveResDTO();
            BeanUtils.copyProperties(userLeaveBO, userLeaveResDTO);
            userLeaveResDTO.setTypeName(ZSYUserLeaveType.getName(userLeaveResDTO.getType()));

            page.add(userLeaveResDTO);
        });

        return new PageInfo<>(page);
    }

    /**
     * 根据审核状态获取请假列表
     * @param status
     * @param pageIndex
     * @return
     */
    @Override
    public PageInfo<UserLeaveResDTO> getUserLeaveList(Integer status, Integer pageIndex) {
        startPage(pageIndex, 5);
        Long userId = ZSYTokenRequestContext.get().getUserId();
        Long departmentId = ZSYTokenRequestContext.get().getDepartmentId();
        Page<UserLeaveBO> userLeaveBOS;
        if(ZSYTokenRequestContext.get().getUserRole()==0){
             userLeaveBOS= userLeaveMapper.getLeaveByReviewStatus(null, status, departmentId,ZSYTokenRequestContext.get().getOrgId());
        }else{
             userLeaveBOS= userLeaveMapper.getLeaveByReviewStatus(userId, status,departmentId,ZSYTokenRequestContext.get().getOrgId());
        }
        Page<UserLeaveResDTO> page = new Page<>();
        BeanUtils.copyProperties(userLeaveBOS, page);
        userLeaveBOS.forEach(userLeaveBO -> {
            UserLeaveResDTO userLeaveResDTO = new UserLeaveResDTO();
            BeanUtils.copyProperties(userLeaveBO, userLeaveResDTO);
            userLeaveResDTO.setTypeName(ZSYUserLeaveType.getName(userLeaveResDTO.getType()));

            List<UserWeekResDTO> userWeekResDTOS = new ArrayList<>();
            userLeaveBO.getUserWeeks().forEach(userWeekBO -> {
                UserWeekResDTO userWeekResDTO = new UserWeekResDTO();
                BeanUtils.copyProperties(userWeekBO, userWeekResDTO);
                userWeekResDTOS.add(userWeekResDTO);
            });
            userLeaveResDTO.setUserWeeks(userWeekResDTOS);

            page.add(userLeaveResDTO);
        });
        return new PageInfo<>(page);
    }

    /**
     * 更新请假信息
     * @param userLeaveReqDTO
     * @param id
     */
    @Override
    @Transactional
    public void updateLeaveDetail(UserLeaveReqDTO userLeaveReqDTO, Long id) {

        UserLeave userLeave = new UserLeave();
        UserLeaveBO userLeaveBO = userLeaveMapper.getUserLeaveById(id,ZSYTokenRequestContext.get().getOrgId());
        if (userLeaveReqDTO.getType()==ZSYUserLeaveType.CHANGEREST.getValue()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userLeaveReqDTO.getBeginTime());
            BigDecimal totalRestHours = restHoursLogMapper.selectTotalRestHours(userLeaveBO.getUserId(), calendar.get(Calendar.YEAR));
            BigDecimal usedHours = restHoursLogMapper.selectUsedHours(userLeaveBO.getUserId(), calendar.get(Calendar.YEAR));
            BigDecimal leftHours = totalRestHours.add(usedHours);
            if (leftHours.subtract(userLeaveReqDTO.getHours()).compareTo(BigDecimal.ZERO)<0){
                throw new ZSYServiceException("用户当前所剩调休时间不足此次调休扣除,请选择其他请假类型");
            }
        }
        userLeave.setId(id);
        userLeave.setUserId(userLeaveBO.getUserId());
        userLeave.setReviewStatus(userLeaveBO.getReviewStatus());
        userLeave.setCreateTime(userLeaveBO.getCreateTime());
        BeanUtils.copyProperties(userLeaveReqDTO,userLeave);
        if(userLeaveMapper.updateLeave(userLeave)==0){
            throw new ZSYServiceException("更新请假信息失败");
        }

        userWeekMapper.deleteByTaskId(id,ZSYTokenRequestContext.get().getOrgId());
        List<UserWeek> userWeeks = Lists.newArrayList();
        UserWeek userWeek = new UserWeek();
        userWeek.setId(snowFlakeIDHelper.nextId());
        userWeek.setUserId(ZSYTokenRequestContext.get().getUserId());
        userWeek.setYear(DateHelper.getYears(userLeaveReqDTO.getBeginTime()));
        userWeek.setTaskId(userLeave.getId());
        userWeek.setHours(userLeaveReqDTO.getHours().doubleValue());
        userWeek.setWeekNumber(DateHelper.getCurrentWeekNumber(userLeaveReqDTO.getBeginTime()));
        userWeek.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        userWeeks.add(userWeek);
        userWeekMapper.insertList(userWeeks);

    }

    /**
     * 删除请假申请
     * @param id
     */
    @Override
    @Transactional
    public void deleteLeaveDetail(Long id) {
        UserLeave userLeave = userLeaveMapper.selectById(id);
        if (userLeave == null){
            throw new ZSYServiceException("请假申请不存在");
        }
        if (userLeave.getReviewStatus() == ZSYReviewStatus.ACCEPT.getValue()){
            if (ZSYTokenRequestContext.get().getUserRole() != ZSYUserRole.ADMINISTRATOR.getValue()){
                throw new ZSYServiceException("暂无权限");
            }
        }
        userLeaveMapper.deleteById(id);
        userWeekMapper.deleteByTaskId(id,ZSYTokenRequestContext.get().getOrgId());
        restHoursLogMapper.deleteByLeave(id);
    }

    /**
     * 通过审核
     * @param id
     */
    @Override
    @Transactional
    public void passLeave(Long id) {
        UserLeaveBO userLeaveBO = userLeaveMapper.getUserLeaveById(id,ZSYTokenRequestContext.get().getOrgId());
        User user = userMapper.selectById(userLeaveBO.getUserId());
        if (userLeaveBO.getType()==ZSYUserLeaveType.CHANGEREST.getValue()){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userLeaveBO.getBeginTime());
            BigDecimal totalRestHours = restHoursLogMapper.selectTotalRestHours(userLeaveBO.getUserId(), calendar.get(Calendar.YEAR));
            BigDecimal usedHours = restHoursLogMapper.selectUsedHours(userLeaveBO.getUserId(), calendar.get(Calendar.YEAR));
            BigDecimal leftHours = totalRestHours.add(usedHours);
            if (leftHours.subtract(userLeaveBO.getHours()).compareTo(BigDecimal.ZERO)<0){
                throw new ZSYServiceException("用户当前所剩调休时间不足此次调休扣除,请选择其他请假类型");
            }
        }
        UserLeave userLeave = new UserLeave();
        BeanUtils.copyProperties(userLeaveBO,userLeave);
        userLeave.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
        if(userLeaveMapper.updateLeave(userLeave)==0){
            throw new ZSYServiceException("更新审核信息失败");
        }
        if (userLeave.getType()==ZSYUserLeaveType.CHANGEREST.getValue()){

            //新增调休日志
            UserRestHoursLog restHoursLog = new UserRestHoursLog();
            restHoursLog.setId(snowFlakeIDHelper.nextId());
            restHoursLog.setUserId(user.getId());
            restHoursLog.setLeaveId(userLeaveBO.getId());
            restHoursLog.setUserName(user.getName());
            restHoursLog.setRestHours(BigDecimal.ZERO.subtract(userLeaveBO.getHours()));
            restHoursLog.setType(ZSYRestHoursType.LEAVE.getValue());
            restHoursLog.setContent(userLeaveBO.getDescription());
            restHoursLog.setCreateTime(new Date());
            restHoursLog.setRecordTime(userLeaveBO.getBeginTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userLeave.getBeginTime());
            int year = calendar.get(Calendar.YEAR);
            restHoursLog.setYear(year);
            restHoursLog.setOrgId(ZSYTokenRequestContext.get().getOrgId());

            restHoursLogMapper.insert(restHoursLog);
        }


    }
}

