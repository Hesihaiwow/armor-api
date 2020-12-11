package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.AddExtraWorkReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYExtraWorkService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYRestHoursType;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.pagehelper.page.PageMethod.startPage;

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
    @Autowired
    private IZSYSignInMapper signInMapper;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateHelper.DATE_FORMAT);
        String dateStr = dateFormat.format(reqDTO.getBeginTime());
        List<Long> taskIds = extraWorkMapper.selectTaskIdsByUserAndTime(userTemp.getId(),dateStr);
        if (!CollectionUtils.isEmpty(taskIds)){
            List<Long> intersection = taskIds.stream()
                    .filter(item -> reqDTO.getTaskIds().contains(item)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(intersection)){
                throw new ZSYServiceException("存在一个任务当天多次申请加班,请检查");
            }
        }

        ExtraWork extraWork = new ExtraWork();
        BeanUtils.copyProperties(reqDTO,extraWork);
        extraWork.setId(snowFlakeIDHelper.nextId());
        extraWork.setIsDelete(0);
        extraWork.setReviewStatus(0);
        extraWork.setCreateTime(new Date());
        extraWork.setUserId(ZSYTokenRequestContext.get().getUserId());
        Float workHours = reqDTO.getWorkHours();
        int i = workHours.intValue();
        extraWork.setWorkHours((float) i);
        if (extraWorkMapper.addExtraWork(extraWork) == 0){
            throw new ZSYServiceException("新增加班申请失败");
        }

        List<ExtraWorkTask> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reqDTO.getTaskIds())){
            reqDTO.getTaskIds().forEach(taskId->{
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
        if (extraWork.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException("当前申请已删除");
        }
        //审核前,先校验当前加班关联的任务,在申请当天是否已经提交加班申请
        List<Long> collect = reqDTO.getTaskIds();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateHelper.DATE_FORMAT);
        List<Long> taskIds = extraWorkMapper.selectTaskIdsByUserAndTimeReviewed(extraWork.getUserId(),dateFormat.format(extraWork.getBeginTime()));
        if (!CollectionUtils.isEmpty(taskIds)){
            List<Long> intersection = taskIds.stream()
                    .filter(collect::contains).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(intersection)){
                throw new ZSYServiceException("该用户存在一个任务当天多次申请加班,请检查");
            }
        }
        BeanUtils.copyProperties(reqDTO,extraWork);
        Float workHours = reqDTO.getWorkHours();
        int i = workHours.intValue();
        extraWork.setWorkHours(Float.valueOf(i));
        extraWork.setUpdateTime(new Date());
        if (extraWorkMapper.updateExtraWorkById(extraWork,ewId) == 0){
            throw new ZSYServiceException("更新加班申请失败");
        }

        extraWorkMapper.deleteExtraWorkTaskByEwId(ewId);

        List<ExtraWorkTask> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(reqDTO.getTaskIds())){
            reqDTO.getTaskIds().forEach(taskId->{
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
        if (extraWork.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException("当前申请已删除");
        }
        //审核前,先校验当前加班关联的任务,在申请当天是否已经提交加班申请
        List<Task> taskList = extraWorkMapper.selectTasksByEwId(ewId);
        List<Long> collect = taskList.stream().map(Task::getId).collect(Collectors.toList());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateHelper.DATE_FORMAT);
        List<Long> taskIds = extraWorkMapper.selectTaskIdsByUserAndTimeReviewed(extraWork.getUserId(),dateFormat.format(extraWork.getBeginTime()));
        if (!CollectionUtils.isEmpty(taskIds)){
            List<Long> intersection = taskIds.stream()
                    .filter(collect::contains).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(intersection)){
                throw new ZSYServiceException("该用户存在一个任务当天多次申请加班,请检查");
            }
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
        //查询进行中的任务
        List<Task> list = taskMapper.selectMyRunningTask(userId);
        //查询2020-02总任务已完成的多人任务
        LocalDate localDate = LocalDate.now().minusMonths(2);
        List<Task> list2 = taskMapper.selectTaskDone(userId,localDate);
        //查询2020-02总任务已完成的个人任务
        List<Task> list3 = taskMapper.selectPrivateTaskDone(userId,localDate);
        List<Task> total = new ArrayList<>();
        total.addAll(list);
        total.addAll(list2);
        total.addAll(list3);
        total = total.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(Task::getId))
        ), ArrayList::new));
        return total;
    }

    /**
     * 查询我的加班申请(待审核)
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<ExtraWorkResDTO> getWaitExtraWorkByPage(Integer pageNum) {
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ExtraWork> page = extraWorkMapper.selectWaitExtraWorkByPage(ZSYTokenRequestContext.get().getUserId());
        Page<ExtraWorkResDTO> list = new Page<>();
        BeanUtils.copyProperties(page,list);
        if (!CollectionUtils.isEmpty(page)){
            page.forEach(extraWork -> {
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
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ExtraWork> page = extraWorkMapper.selectAccessExtraWorkByPage(ZSYTokenRequestContext.get().getUserId());
        Page<ExtraWorkResDTO> list = new Page<>();
        BeanUtils.copyProperties(page,list);
        if (!CollectionUtils.isEmpty(page)){
            page.forEach(extraWork -> {
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
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ExtraWork> page = extraWorkMapper.selectCheckingExtraWorkByPage();
        Page<ExtraWorkResDTO> list = new Page<>();
        BeanUtils.copyProperties(page,list);
        if (!CollectionUtils.isEmpty(page)){
            page.forEach(extraWork -> {
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
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ExtraWork> page = extraWorkMapper.selectCheckedExtraWorkByPage();
        Page<ExtraWorkResDTO> list = new Page<>();
        BeanUtils.copyProperties(page,list);
        if (!CollectionUtils.isEmpty(page)){
            page.forEach(extraWork -> {
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
        Date beginTime = extraWork.getBeginTime();
        SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateSDF = new SimpleDateFormat(DateHelper.DATE_FORMAT);
        SimpleDateFormat timeSDF2 = new SimpleDateFormat("HH:mm:ss");
        String dateStr = dateSDF.format(beginTime);
        Date begin = null;
        Date end = null;
        Date todayFive = null;
        Date today15 = null;

        try {
            begin = timeSDF.parse(dateStr + " 00:00:00");
            end = timeSDF.parse(dateStr + " 23:59:59");
            todayFive = timeSDF.parse(dateStr + " 05:00:00");
            today15 = timeSDF.parse(dateStr + " 15:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (todayFive != null && end != null){
            //查询申请当日考勤
            List<SignIn> signIns = signInMapper.selectSignInByUserAndTimeRange(extraWork.getUserId(), todayFive, end);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(begin);
            int weekNum = calendar.get(Calendar.DAY_OF_WEEK);
            String weekday = getWeekday(weekNum);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            Date nextZero = calendar.getTime();
            Date nextFive = null;
            String nextDateStr = dateSDF.format(nextZero);
            try {
                nextFive = timeSDF.parse(nextDateStr + " 05:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //查询第二天0点--5点考勤
            List<SignIn> signIns1 = signInMapper.selectSignInByUserAndTimeRange(extraWork.getUserId(), nextZero, nextFive);

            String checkInStr = " ";
            String checkOutStr = " ";
            if (!CollectionUtils.isEmpty(signIns)){
                List<Date> collect = signIns.stream().sorted(Comparator.comparing(SignIn::getCheckTime))
                        .map(SignIn::getCheckTime)
                        .collect(Collectors.toList());
                //当日存在考勤,则第一条为上班打卡
                checkInStr = timeSDF2.format(collect.get(0));

                //当日最后一条在15点之后,暂定为当日下班打卡
                Date date = collect.get(collect.size() - 1);
                if (date.compareTo(today15)>=0){
                    checkOutStr = timeSDF2.format(date);
                }
            }

            //第二日0点--5点存在考勤,则最后一条为前一日的下班打卡
            if (!CollectionUtils.isEmpty(signIns1)){
                List<Date> collect = signIns1.stream().sorted(Comparator.comparing(SignIn::getCheckTime))
                        .map(SignIn::getCheckTime)
                        .collect(Collectors.toList());
                checkOutStr = timeSDF2.format(collect.get(collect.size()-1))+"(+1)";
            }

            String checkTimeStr = weekday + ",   上班: " + checkInStr + ",   下班: " + checkOutStr;
            resDTO.setCheckTimeStr(checkTimeStr);
        }
        resDTO.setUserId(user.getId());
        resDTO.setAvatarUrl(user.getAvatarUrl());
        resDTO.setUserName(user.getName());
        BeanUtils.copyProperties(extraWork,resDTO);
        resDTO.setTasks(taskList);
        return resDTO;
    }

    private String  getWeekday(int weekNum) {
        String weekday;
        switch (weekNum){
            case 1:
                weekday = "星期日";
                break;
            case 2:
                weekday = "星期一";
                break;
            case 3:
                weekday = "星期二";
                break;
            case 4:
                weekday = "星期三";
                break;
            case 5:
                weekday = "星期四";
                break;
            case 6:
                weekday = "星期五";
                break;
            case 7:
                weekday = "星期六";
                break;
            default:
                weekday = "";
                break;
        }

        return weekday;
    }
}
