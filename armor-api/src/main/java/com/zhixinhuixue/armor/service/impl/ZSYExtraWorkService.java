package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYExtraWorkMapper;
import com.zhixinhuixue.armor.dao.IZSYRestHoursLogMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.AddExtraWorkReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYExtraWorkService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYRestHoursType;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author SCH
 * @date 2019/2/14 17:20
 */
@Service
public class ZSYExtraWorkService implements IZSYExtraWorkService {

    @Autowired
    private IZSYExtraWorkMapper extraWorkMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYRestHoursLogMapper restHoursLogMapper;

    /**
     * 新增加班申请
     * @param reqDTO
     */
    @Override
    @Transactional
    public void addExtraWork(AddExtraWorkReqDTO reqDTO) {

        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }

        ExtraWork extraWork = new ExtraWork();
        BeanUtils.copyProperties(reqDTO,extraWork);
        extraWork.setId(snowFlakeIDHelper.nextId());
        extraWork.setIsDelete(0);
        extraWork.setReviewStatus(0);
        extraWork.setCreateTime(new Date());
        extraWork.setUserId(ZSYTokenRequestContext.get().getUserId());

        if (extraWorkMapper.addExtraWork(extraWork) == 0){
            throw new ZSYServiceException("新增加班申请失败");
        }

        List<ExtraWorkTask> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reqDTO.getTaskIds())){
            reqDTO.getTaskIds().stream().forEach(taskId->{
                if (taskId != null){
                    ExtraWorkTask extraWorkTask = new ExtraWorkTask();
                    extraWorkTask.setId(snowFlakeIDHelper.nextId());
                    extraWorkTask.setEwId(extraWork.getId());
                    extraWorkTask.setTaskId(taskId);
                    list.add(extraWorkTask);
                }
            });
        }
        if (!CollectionUtils.isEmpty(list)){
            if (extraWorkMapper.addExtraWorkTaskByList(list) == 0){
                throw new ZSYServiceException("新增加班申请失败");
            }
        }else {
            throw new ZSYServiceException("关联任务为空");
        }
    }

    /**
     * 修改加班申请
     * @param reqDTO
     * @param ewId
     */
    @Override
    @Transactional
    public void updateExtraWork(AddExtraWorkReqDTO reqDTO, Long ewId) {
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
        ExtraWork extraWork = extraWorkMapper.selectById(ewId);
        if (extraWork == null){
            throw new ZSYServiceException("加班申请不存在");
        }
        BeanUtils.copyProperties(reqDTO,extraWork);
        extraWork.setUpdateTime(new Date());
        if (extraWorkMapper.updateExtraWorkById(extraWork,ewId) == 0){
            throw new ZSYServiceException("更新加班申请失败");
        }

        extraWorkMapper.deleteExtraWorkTaskByEwId(ewId);

        List<ExtraWorkTask> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reqDTO.getTaskIds())){
            reqDTO.getTaskIds().stream().forEach(taskId->{
                if (taskId != null){
                    ExtraWorkTask extraWorkTask = new ExtraWorkTask();
                    extraWorkTask.setId(snowFlakeIDHelper.nextId());
                    extraWorkTask.setEwId(extraWork.getId());
                    extraWorkTask.setTaskId(taskId);
                    list.add(extraWorkTask);
                }
            });
        }
        if (!CollectionUtils.isEmpty(list)){
            if (extraWorkMapper.addExtraWorkTaskByList(list) == 0){
                throw new ZSYServiceException("更新加班申请失败");
            }
        }else {
            throw new ZSYServiceException("关联任务为空");
        }
    }

    /**
     * 删除加班申请
     * @param ewId
     */
    @Override
    @Transactional
    public void deleteExtraWork(Long ewId) {
        ExtraWork extraWork = extraWorkMapper.selectById(ewId);
        if (extraWork == null){
            throw new ZSYServiceException("该加班申请不存在");
        }
        extraWork.setUpdateTime(new Date());
        extraWork.setReviewStatus(1);
        extraWork.setIsDelete(1);
        if (extraWorkMapper.updateExtraWorkById(extraWork,ewId) == 0){
            throw new ZSYServiceException("删除申请失败");
        }
        extraWorkMapper.deleteExtraWorkTaskByEwId(ewId);
    }

    /**
     * 审核通过
     * @param ewId
     */
    @Override
    @Transactional
    public void checkExtraWork(Long ewId) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        ExtraWork extraWork = extraWorkMapper.selectById(ewId);
        if (extraWork == null){
            throw new ZSYServiceException("该加班申请不存在");
        }
        extraWork.setReviewStatus(2);
        extraWork.setCheckTime(new Date());
        extraWork.setUpdateTime(new Date());
        if (extraWorkMapper.updateExtraWorkById(extraWork,ewId) == 0){
            throw new ZSYServiceException("通过加班申请失败");
        }
        User user = userMapper.selectById(extraWork.getUserId());
        //插入调休日志
        UserRestHoursLog restHoursLog = new UserRestHoursLog();
        restHoursLog.setId(snowFlakeIDHelper.nextId());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(extraWork.getBeginTime());
        int year = calendar.get(Calendar.YEAR);
        restHoursLog.setYear(year);
        restHoursLog.setUserId(user.getId());
        restHoursLog.setUserName(user.getName());
        restHoursLog.setType(ZSYRestHoursType.EWORK.getValue());
        restHoursLog.setRestHours(BigDecimal.valueOf(extraWork.getWorkHours()));
        restHoursLog.setContent(extraWork.getReason());
        restHoursLog.setEwId(ewId);
        restHoursLog.setCreateTime(new Date());
        restHoursLog.setRecordTime(extraWork.getBeginTime());
        restHoursLogMapper.insert(restHoursLog);
    }

    /**
     * 查询我的未完成任务
     * @return
     */
    @Override
    public List<Task> getMyRunningTaskList(Long userId) {
        List<Task> list = taskMapper.selectMyRunningTask(userId);
        return list;
    }

    /**
     * 查询我的加班申请(待审核)
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<ExtraWorkResDTO> getWaitExtraWorkByPage(Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ExtraWork> page = extraWorkMapper.selectWaitExtraWorkByPage(ZSYTokenRequestContext.get().getUserId());
        Page<ExtraWorkResDTO> list = new Page<>();
        BeanUtils.copyProperties(page,list);
        if (!CollectionUtils.isEmpty(page)){
            page.stream().forEach(extraWork -> {
                ExtraWorkResDTO extraWorkResDTO = new ExtraWorkResDTO();
                BeanUtils.copyProperties(extraWork,extraWorkResDTO);
                List<Task> tasks = extraWorkMapper.selectTasksByEwId(extraWork.getId());
                extraWorkResDTO.setTasks(tasks);
                User user = userMapper.selectByEwId(extraWork.getId());
                extraWorkResDTO.setUserId(user.getId());
                extraWorkResDTO.setAvatarUrl(user.getAvatarUrl());
                extraWorkResDTO.setUserName(user.getName());
                list.add(extraWorkResDTO);
            });
        }
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ExtraWorkResDTO> getAccessExtraWorkByPage(Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ExtraWork> page = extraWorkMapper.selectAccessExtraWorkByPage(ZSYTokenRequestContext.get().getUserId());
        Page<ExtraWorkResDTO> list = new Page<>();
        BeanUtils.copyProperties(page,list);
        if (!CollectionUtils.isEmpty(page)){
            page.stream().forEach(extraWork -> {
                ExtraWorkResDTO extraWorkResDTO = new ExtraWorkResDTO();
                BeanUtils.copyProperties(extraWork,extraWorkResDTO);
                List<Task> tasks = extraWorkMapper.selectTasksByEwId(extraWork.getId());
                extraWorkResDTO.setTasks(tasks);
                User user = userMapper.selectByEwId(extraWork.getId());
                extraWorkResDTO.setUserId(user.getId());
                extraWorkResDTO.setAvatarUrl(user.getAvatarUrl());
                extraWorkResDTO.setUserName(user.getName());
                list.add(extraWorkResDTO);
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * 查询审核中的加班申请
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<ExtraWorkResDTO> getCheckingExtraWorkByPage(Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ExtraWork> page = extraWorkMapper.selectCheckingExtraWorkByPage();
        Page<ExtraWorkResDTO> list = new Page<>();
        BeanUtils.copyProperties(page,list);
        if (!CollectionUtils.isEmpty(page)){
            page.stream().forEach(extraWork -> {
                ExtraWorkResDTO extraWorkResDTO = new ExtraWorkResDTO();
                BeanUtils.copyProperties(extraWork,extraWorkResDTO);
                List<Task> tasks = extraWorkMapper.selectTasksByEwId(extraWork.getId());
                extraWorkResDTO.setTasks(tasks);
                User user = userMapper.selectByEwId(extraWork.getId());
                extraWorkResDTO.setUserId(user.getId());
                extraWorkResDTO.setAvatarUrl(user.getAvatarUrl());
                extraWorkResDTO.setUserName(user.getName());
                list.add(extraWorkResDTO);
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * 查询审核通过的加班申请
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<ExtraWorkResDTO> getCheckedExtraWorkByPage(Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ExtraWork> page = extraWorkMapper.selectCheckedExtraWorkByPage();
        Page<ExtraWorkResDTO> list = new Page<>();
        BeanUtils.copyProperties(page,list);
        if (!CollectionUtils.isEmpty(page)){
            page.stream().forEach(extraWork -> {
                ExtraWorkResDTO extraWorkResDTO = new ExtraWorkResDTO();
                BeanUtils.copyProperties(extraWork,extraWorkResDTO);
                List<Task> tasks = extraWorkMapper.selectTasksByEwId(extraWork.getId());
                extraWorkResDTO.setTasks(tasks);
                User user = userMapper.selectByEwId(extraWork.getId());
                extraWorkResDTO.setUserId(user.getId());
                extraWorkResDTO.setAvatarUrl(user.getAvatarUrl());
                extraWorkResDTO.setUserName(user.getName());
                list.add(extraWorkResDTO);
            });
        }
        return new PageInfo<>(list);
    }

    @Override
    public ExtraWorkDetailResDTO getEWorkDetail(Long ewId) {
        ExtraWork extraWork = extraWorkMapper.selectById(ewId);
        if (extraWork == null){
            throw new ZSYServiceException("该加班申请不存在");
        }
        ExtraWorkDetailResDTO resDTO = new ExtraWorkDetailResDTO();
        List<Task> taskList = extraWorkMapper.selectTasksByEwId(ewId);
        User user = userMapper.selectByEwId(extraWork.getId());
        resDTO.setUserId(user.getId());
        resDTO.setAvatarUrl(user.getAvatarUrl());
        resDTO.setUserName(user.getName());
        BeanUtils.copyProperties(extraWork,resDTO);
        resDTO.setTasks(taskList);
        return resDTO;
    }
}
