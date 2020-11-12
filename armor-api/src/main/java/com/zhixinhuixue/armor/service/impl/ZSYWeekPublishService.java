package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.bo.TaskUserBO;
import com.zhixinhuixue.armor.model.bo.WeekPublishPlanBO;
import com.zhixinhuixue.armor.model.bo.WeekPublishPlanPlatformUserBO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishAddReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishEditReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishPlanDetailResDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskBaseResDTO;
import com.zhixinhuixue.armor.model.dto.response.WeekPublishPlanPageResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYWeekPublishService;
import com.zhixinhuixue.armor.source.enums.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sch
 * @DATE 2019/6/11 9:57
 */
@Service
public class ZSYWeekPublishService implements IZSYWeekPublishService {
    @Autowired
    private IZSYWeekPublishPlanMapper weekPublishPlanMapper;
    @Autowired
    private IZSYWeekPublishPlanPlatformMapper publishPlatformMapper;
    @Autowired
    private IZSYWeekPublishPlanTaskMapper publishTaskMapper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYPlatformMapper platformMapper;
    @Autowired
    private IZSYWeekPublishPlanPlatformUserMapper platformUserMapper;

    /**
     * 新增发版计划
     * @param reqDTO 参数
     */
    @Override
    @Transactional
    public void addPublishPlan(WeekPublishAddReqDTO reqDTO) {
        Integer userRole = ZSYTokenRequestContext.get().getUserRole();
        if (userRole>ZSYUserRole.PROJECT_MANAGER.getValue()){
            throw new ZSYServiceException("当前用户暂无权限");
        }
        //校验是否存在同名发版计划
        WeekPublishPlan sameNameWpp = weekPublishPlanMapper.selectByName(reqDTO.getWppName().trim());
        if (sameNameWpp != null){
            throw new ZSYServiceException("当前发版标题已存在,无法新增");
        }
        WeekPublishPlan weekPublishPlan = new WeekPublishPlan();
        weekPublishPlan.setWppId(snowFlakeIDHelper.nextId());
        weekPublishPlan.setWppName(reqDTO.getWppName().trim());
        weekPublishPlan.setPublishTime(reqDTO.getPublishTime());
        weekPublishPlan.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        weekPublishPlan.setCreateTime(new Date());
        if (Strings.isNullOrEmpty(reqDTO.getRemark())){
            weekPublishPlan.setRemark(reqDTO.getRemark().trim());
        }
        if (reqDTO.getTestReport() != null){
            weekPublishPlan.setTestReport(reqDTO.getTestReport());
        }
        weekPublishPlan.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        weekPublishPlanMapper.insert(weekPublishPlan);

        List<Long> taskIds = reqDTO.getTaskIds();
        if (!CollectionUtils.isEmpty(taskIds)){
            taskIds = taskIds.stream().distinct().collect(Collectors.toList());
            List<Task> taskList = taskMapper.selectByIds(taskIds);
            if (!CollectionUtils.isEmpty(taskList)){
                boolean flag;
                if (taskIds.size() != taskList.size()){
                    flag = true;
                }else {
                    flag = taskList.stream()
                            .anyMatch(task -> task.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()
                                    || task.getReviewStatus() != ZSYReviewStatus.ACCEPT.getValue()
                                    || task.getStatus() == ZSYTaskStatus.FINISHED.getValue()
                                    || task.getStatus() == ZSYTaskStatus.STOP.getValue()
                                    || task.getType() != ZSYTaskType.PUBLIC_TASK.getValue());
                }
                if (flag){
                    throw new ZSYServiceException("关联的任务中,存在某些任务已删除,或者不是多人任务等情况,请刷新重试");
                }
            }else {
                throw new ZSYServiceException("关联的任务中,存在某些任务已删除,或者不是多人任务等情况,请刷新重试");
            }
            List<WeekPublishPlanTask> publishPlanTasks = taskIds.stream().map(taskId -> {
                WeekPublishPlanTask publishTask = new WeekPublishPlanTask();
                publishTask.setWpptId(snowFlakeIDHelper.nextId());
                publishTask.setWppId(weekPublishPlan.getWppId());
                publishTask.setTaskId(taskId);
                return publishTask;
            }).collect(Collectors.toList());
            publishTaskMapper.insertBatch(publishPlanTasks);
        }

        List<Long> platformIds = reqDTO.getPlatformIds();
        if (!CollectionUtils.isEmpty(platformIds)){
            platformIds = platformIds.stream().distinct().collect(Collectors.toList());
            List<WeekPublishPlanPlatform> publishPlanPlatforms = platformIds.stream().map(platformId -> {
                WeekPublishPlanPlatform planPlatform = new WeekPublishPlanPlatform();
                planPlatform.setWpppId(snowFlakeIDHelper.nextId());
                planPlatform.setWppId(weekPublishPlan.getWppId());
                planPlatform.setPlatformId(platformId);
                return planPlatform;
            }).collect(Collectors.toList());
            publishPlatformMapper.insertBatch(publishPlanPlatforms);
        }

        List<WeekPublishAddReqDTO.PlatformAndUser> platformAndUserList = reqDTO.getPlatformAndUserList();
        if (!CollectionUtils.isEmpty(platformAndUserList)){
            Set<WeekPublishAddReqDTO.PlatformAndUser> hashSet = new HashSet<>();
            hashSet.addAll(platformAndUserList);
            if (platformAndUserList.size() != hashSet.size()){
                throw new ZSYServiceException("请勿添加重复数据");
            }
            List<WeekPublishPlanPlatformUser> collect = platformAndUserList.stream().map(item -> {
                WeekPublishPlanPlatformUser platformUser = new WeekPublishPlanPlatformUser();
                platformUser.setWpppuId(snowFlakeIDHelper.nextId());
                platformUser.setWppId(weekPublishPlan.getWppId());
                platformUser.setPlatformId(item.getPlatformId());
                platformUser.setUserId(item.getUserId());
                return platformUser;
            }).collect(Collectors.toList());
            platformUserMapper.insertBatch(collect);
        }
    }

    /**
     * 编辑发版计划
     * @param reqDTO 参数
     * @param wppId 计划id
     */
    @Override
    @Transactional
    public void editPublishPlan(WeekPublishEditReqDTO reqDTO, Long wppId) {
        Integer userRole = ZSYTokenRequestContext.get().getUserRole();
        if (userRole>ZSYUserRole.PROJECT_MANAGER.getValue()){
            throw new ZSYServiceException("当前用户暂无权限");
        }
        WeekPublishPlan existPlan = weekPublishPlanMapper.selectById(wppId);
        if (existPlan == null){
            throw new ZSYServiceException("当前发版计划不存在");
        }
        if (existPlan.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException(String.format("当前发版计划[%s]已删除",existPlan.getWppName()));
        }
        String wppName = reqDTO.getWppName();
        WeekPublishPlan sameNameWpp = weekPublishPlanMapper.selectByName(wppName.trim());
        if (sameNameWpp != null && !sameNameWpp.getWppId().equals(wppId)){
            throw new ZSYServiceException("当前发版标题已存在");
        }
        WeekPublishPlan weekPublishPlan = new WeekPublishPlan();
        weekPublishPlan.setWppId(wppId);
        weekPublishPlan.setWppName(wppName.trim());
        weekPublishPlan.setPublishTime(reqDTO.getPublishTime());
        if (!Strings.isNullOrEmpty(reqDTO.getRemark())){
            weekPublishPlan.setRemark(reqDTO.getRemark().trim());
        }else {
            weekPublishPlan.setRemark("");
        }
        if (reqDTO.getTestReport() != null){
            weekPublishPlan.setTestReport(reqDTO.getTestReport());
        }else {
            weekPublishPlan.setTestReport(null);
        }
        weekPublishPlanMapper.updateById(weekPublishPlan);

        //删除原来关联的任务
        publishTaskMapper.deleteByWppId(wppId);
        //删除原来关联的平台
        publishPlatformMapper.deleteByWppId(wppId);

        List<Long> taskIds = reqDTO.getTaskIds();
        if (!CollectionUtils.isEmpty(taskIds)){
            taskIds = taskIds.stream().distinct().collect(Collectors.toList());
            List<Task> taskList = taskMapper.selectByIds(taskIds);
            if (!CollectionUtils.isEmpty(taskList)){
                boolean flag;
                if (taskIds.size() != taskList.size()){
                    flag = true;
                }else {
                    flag = taskList.stream()
                            .anyMatch(task -> task.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()
                                    || task.getReviewStatus() != ZSYReviewStatus.ACCEPT.getValue()
                                    || task.getStatus() == ZSYTaskStatus.STOP.getValue()
                                    || task.getStatus() == ZSYTaskStatus.FINISHED.getValue()
                                    || task.getType() != ZSYTaskType.PUBLIC_TASK.getValue());
                }

                if (flag){
                    throw new ZSYServiceException("关联的任务中,存在某些任务已删除,或者不是多人任务等情况,请刷新重试");
                }
            }else {
                throw new ZSYServiceException("关联的任务中,存在某些任务已删除,或者不是多人任务等情况,请刷新重试");
            }
            List<WeekPublishPlanTask> publishPlanTasks = taskIds.stream().map(taskId -> {
                WeekPublishPlanTask publishTask = new WeekPublishPlanTask();
                publishTask.setWpptId(snowFlakeIDHelper.nextId());
                publishTask.setWppId(weekPublishPlan.getWppId());
                publishTask.setTaskId(taskId);
                return publishTask;
            }).collect(Collectors.toList());
            publishTaskMapper.insertBatch(publishPlanTasks);
        }

        List<Long> platformIds = reqDTO.getPlatformIds();
        if (!CollectionUtils.isEmpty(platformIds)){
            platformIds = platformIds.stream().distinct().collect(Collectors.toList());
            List<WeekPublishPlanPlatform> publishPlanPlatforms = platformIds.stream().map(platformId -> {
                WeekPublishPlanPlatform planPlatform = new WeekPublishPlanPlatform();
                planPlatform.setWpppId(snowFlakeIDHelper.nextId());
                planPlatform.setWppId(weekPublishPlan.getWppId());
                planPlatform.setPlatformId(platformId);
                return planPlatform;
            }).collect(Collectors.toList());
            publishPlatformMapper.insertBatch(publishPlanPlatforms);
        }

        //删除原来的多次发版平台和用户
        platformUserMapper.deleteByWppId(wppId);
        List<WeekPublishAddReqDTO.PlatformAndUser> platformAndUserList = reqDTO.getPlatformAndUserList();
        if (!CollectionUtils.isEmpty(platformAndUserList)){
            Set<WeekPublishAddReqDTO.PlatformAndUser> hashSet = new HashSet<>();
            hashSet.addAll(platformAndUserList);
            if (platformAndUserList.size() != hashSet.size()){
                throw new ZSYServiceException("请勿添加重复数据");
            }
            List<WeekPublishPlanPlatformUser> collect = platformAndUserList.stream().map(item -> {
                WeekPublishPlanPlatformUser platformUser = new WeekPublishPlanPlatformUser();
                platformUser.setWpppuId(snowFlakeIDHelper.nextId());
                platformUser.setWppId(weekPublishPlan.getWppId());
                platformUser.setPlatformId(item.getPlatformId());
                platformUser.setUserId(item.getUserId());
                return platformUser;
            }).collect(Collectors.toList());
            platformUserMapper.insertBatch(collect);
        }
    }

    /**
     * 删除发版计划
     * @param wppId 计划id
     */
    @Override
    @Transactional
    public void deletePublishPlan(Long wppId) {
        Integer userRole = ZSYTokenRequestContext.get().getUserRole();
        if (userRole>ZSYUserRole.PROJECT_MANAGER.getValue()){
            throw new ZSYServiceException("当前用户暂无权限");
        }
        WeekPublishPlan existPlan = weekPublishPlanMapper.selectById(wppId);
        if (existPlan == null){
            throw new ZSYServiceException("当前发版计划不存在");
        }
        if (existPlan.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException(String.format("当前发版计划[%s]已删除",existPlan.getWppName()));
        }
        WeekPublishPlan weekPublishPlan = new WeekPublishPlan();
        weekPublishPlan.setWppId(wppId);
        weekPublishPlan.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        weekPublishPlanMapper.updateById(weekPublishPlan);
        //删除原来关联的任务
        publishTaskMapper.deleteByWppId(wppId);
        //删除原来关联的平台
        publishPlatformMapper.deleteByWppId(wppId);
        //删除原来的多次发版平台和用户
        platformUserMapper.deleteByWppId(wppId);
    }

    /**
     * 获取待发布任务
     */
    @Override
    public List<TaskBaseResDTO> getWaitDeployTasks() {
        List<Task> waitDeployTasks = taskMapper.selectWaitDeployTasks();
        List<TaskBaseResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(waitDeployTasks)){
            list = getTaskBaseResDTOS(waitDeployTasks);
        }
        return list;
    }

    /**
     * 获取开发和测试阶段任务
     */
    @Override
    public List<TaskBaseResDTO> getDevAndTestTasks(String wppIdStr) {
        //查询测试相关任务
        List<Task> devAndTestTasks = taskMapper.selectDevAndTestTasks();
        //查询待发布任务
        List<Task> waitDeployTasks = taskMapper.selectWaitDeployTasks();
        //查询发版计划关联的任务
        List<Task> planTasks = new ArrayList<>();
        if (!Strings.isNullOrEmpty(wppIdStr)){
            planTasks = taskMapper.selectPublishPlanTask(Long.valueOf(wppIdStr));
        }
        List<TaskBaseResDTO> list = new ArrayList<>();
        Set<TaskBaseResDTO> set = new HashSet<>();
        List<TaskBaseResDTO> list1 = new ArrayList<>();
        List<TaskBaseResDTO> list2 = new ArrayList<>();
        List<TaskBaseResDTO> list3 = new ArrayList<>();
        if (!CollectionUtils.isEmpty(waitDeployTasks)){
            list1 = getTaskBaseResDTOS(waitDeployTasks);
        }
        if (!CollectionUtils.isEmpty(devAndTestTasks)){
            list2 = getTaskBaseResDTOS(devAndTestTasks);
        }
        if (!CollectionUtils.isEmpty(planTasks)){
            list3 = getTaskBaseResDTOS(planTasks);
        }
        set.addAll(list1);
        set.addAll(list2);
        set.addAll(list3);
        list.addAll(set);
        return list;
    }

    /**
     * 分页查询
     * @param reqDTO 参数
     */
    @Override
    public PageInfo<WeekPublishPlanPageResDTO> getPage(WeekPublishQueryReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), 1);
        Page<WeekPublishPlanBO> weekPublishPlanBOs = weekPublishPlanMapper.selectPage(reqDTO);
        Page<WeekPublishPlanPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(weekPublishPlanBOs,page);
        if (!CollectionUtils.isEmpty(weekPublishPlanBOs)){
            weekPublishPlanBOs.forEach(item->{
                WeekPublishPlanPageResDTO resDTO = new WeekPublishPlanPageResDTO();
                resDTO.setWppId(item.getWppId());
                resDTO.setWppName(item.getWppName());
                resDTO.setRemark(item.getRemark());
                resDTO.setPublishTimeStr(DateHelper.dateFormatter(item.getPublishTime(),DateHelper.DATE_FORMAT));
                List<WeekPublishPlanPlatform> planPlatforms = publishPlatformMapper.selectByWppId(item.getWppId());
                List<String> platformNames;
                if (!CollectionUtils.isEmpty(planPlatforms)){
                    List<Long> platformIds = planPlatforms.stream().map(WeekPublishPlanPlatform::getPlatformId).collect(Collectors.toList());
                    List<Platform> platformList = platformMapper.selectByIds(platformIds);
                    if (!CollectionUtils.isEmpty(platformList)){
                        platformNames = platformList.stream().map(Platform::getName).collect(Collectors.toList());
                        resDTO.setPlatformList(platformNames);
                    }
                }
                List<WeekPublishPlanTask> planTasks = publishTaskMapper.selectByWppId(item.getWppId());
                if (!CollectionUtils.isEmpty(planTasks)){
                    List<TaskDetailBO> taskDetailBOS = new ArrayList<>();
                    List<TaskUserBO> taskUserBOList = new ArrayList<>();
                    planTasks.forEach(planTask->{
                        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(planTask.getTaskId());
                        if (taskDetailBO != null){
                            taskDetailBOS.add(taskDetailBO);
                            List<TaskUserBO> taskUsers = taskDetailBO.getTaskUsers();
                            taskUserBOList.addAll(taskUsers);
                        }

                    });
                    resDTO.setTaskNum(taskDetailBOS.size());
                    List<WeekPublishPlanPageResDTO.UserAndTask> userAndTaskList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(taskDetailBOS)){
                        Map<Long, List<TaskDetailBO>> userTaskMap = taskDetailBOS.stream().collect(Collectors.groupingBy(Task::getCreateBy));
                        for (Long userId : userTaskMap.keySet()) {
                            List<TaskDetailBO> taskList = userTaskMap.get(userId);
                            WeekPublishPlanPageResDTO.UserAndTask userAndTask = new WeekPublishPlanPageResDTO.UserAndTask();
                            userAndTask.setUserName(taskList.get(0).getUserName());
                            List<String> taskNameList = taskList.stream().map(Task::getName).collect(Collectors.toList());
                            userAndTask.setTaskNameList(taskNameList);
                            userAndTaskList.add(userAndTask);
                        }
                    }

                    resDTO.setUserAndTaskList(userAndTaskList);

                    if (!CollectionUtils.isEmpty(taskUserBOList)){
                        ArrayList<TaskUserBO> distinctUsers = taskUserBOList.stream().sorted(Comparator.comparing(TaskUser::getUserId))
                                .collect(Collectors.collectingAndThen(Collectors.toCollection(
                                        () -> new TreeSet<>(Comparator.comparing(TaskUser::getUserId))
                                ), ArrayList::new));
                        List<String> testerList = distinctUsers.stream()
                                .filter(taskUserBO -> taskUserBO.getJobRole() == ZSYJobRole.TEST.getValue())
                                .map(TaskUserBO::getUserName).collect(Collectors.toList());
                        resDTO.setTesterList(testerList);

                        List<String> developerList = distinctUsers.stream()
                                .filter(taskUserBO -> getIsDeveloper(taskUserBO.getJobRole()))
                                .map(TaskUserBO::getUserName).collect(Collectors.toList());
                        resDTO.setDeveloperList(developerList);

                        List<String> productorList = distinctUsers.stream()
                                .filter(taskUserBO -> taskUserBO.getJobRole() == ZSYJobRole.PRODUCT.getValue())
                                .map(TaskUserBO::getUserName).collect(Collectors.toList());
                        resDTO.setProductorList(productorList);
                    }

                }
                List<WeekPublishPlanPlatformUserBO> platformUserBOS = platformUserMapper.selectByWppId(item.getWppId());
                List<WeekPublishPlanPageResDTO.PlatformUser> platformUsers = new ArrayList<>();
                if (!CollectionUtils.isEmpty(platformUserBOS)){
                    Map<Long, List<WeekPublishPlanPlatformUserBO>> platformUserMap =
                            platformUserBOS.stream().collect(Collectors.groupingBy(WeekPublishPlanPlatformUser::getPlatformId));
                    for (Long platformId : platformUserMap.keySet()) {
                        List<WeekPublishPlanPlatformUserBO> weekPublishPlanPlatformUserBOS = platformUserMap.get(platformId);
                        WeekPublishPlanPageResDTO.PlatformUser platformUser = new WeekPublishPlanPageResDTO.PlatformUser();
                        platformUser.setPlatformName(weekPublishPlanPlatformUserBOS.get(0).getPlatformName());
                        List<String> userNameList = weekPublishPlanPlatformUserBOS.stream()
                                .map(WeekPublishPlanPlatformUserBO::getUserName).collect(Collectors.toList());
                        platformUser.setUserNameList(userNameList);
                        platformUsers.add(platformUser);
                    }
                }
                resDTO.setPlatformUsers(platformUsers);

                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 查询发版计划详情
     * @param wppId 计划id
     */
    @Override
    public WeekPublishPlanDetailResDTO getPublishPlanById(Long wppId) {
        WeekPublishPlan weekPublishPlan = weekPublishPlanMapper.selectById(wppId);
        if (weekPublishPlan == null){
            throw new ZSYServiceException("当前发版计划不存在");
        }
        if (weekPublishPlan.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException(String.format("当前发版计划[%s]已删除",weekPublishPlan.getWppName()));
        }
        WeekPublishPlanDetailResDTO resDTO = new WeekPublishPlanDetailResDTO();
        resDTO.setWppId(wppId);
        resDTO.setWppName(weekPublishPlan.getWppName());
        resDTO.setPublishTime(weekPublishPlan.getPublishTime());
        resDTO.setRemark(weekPublishPlan.getRemark());
        resDTO.setTestReport(weekPublishPlan.getTestReport());
        List<WeekPublishPlanTask> planTasks = publishTaskMapper.selectByWppId(wppId);
        if (!CollectionUtils.isEmpty(planTasks)){
            List<String> taskIds = planTasks.stream().map(item->item.getTaskId().toString()).collect(Collectors.toList());
            resDTO.setTaskIds(taskIds);
        }
        List<WeekPublishPlanPlatform> planPlatforms = publishPlatformMapper.selectByWppId(wppId);
        if (!CollectionUtils.isEmpty(planPlatforms)){
            List<String> platformIds = planPlatforms.stream().map(item->item.getPlatformId().toString()).collect(Collectors.toList());
            resDTO.setPlatformIds(platformIds);
        }
        List<WeekPublishPlanPlatformUserBO> boList = platformUserMapper.selectByWppId(wppId);
        List<WeekPublishPlanDetailResDTO.PlatformUser> platformUserList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(boList)){
            platformUserList = boList.stream().map(item->{
                WeekPublishPlanDetailResDTO.PlatformUser platformUser = new WeekPublishPlanDetailResDTO.PlatformUser();
                BeanUtils.copyProperties(item,platformUser);
                return platformUser;
            }).collect(Collectors.toList());
        }
        resDTO.setPlatformUserList(platformUserList);
        return resDTO;
    }

    /**
     * 查询发版计划涉及人员
     * @param wppId 计划id
     */
    @Override
    public List<EffectUserResDTO> getPublishUsers(Long wppId) {
        return null;
    }

    /**
     * 获取任务返回集合
     * @param tasks 任务集合
     * @return List<TaskBaseResDTO>
     */
    private List<TaskBaseResDTO> getTaskBaseResDTOS(List<Task> tasks) {
        List<TaskBaseResDTO> list;
        list = tasks.stream().map(task -> {
            TaskBaseResDTO resDTO = new TaskBaseResDTO();
            resDTO.setId(task.getId());
            resDTO.setName(task.getName());
            return resDTO;
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 获取2个时间直接的天数
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return Integer
     */
    private Integer getWorkDays(Date beginTime, Date endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        int beginTimeYear = calendar.get(Calendar.YEAR);
        int beginTimeMonth = calendar.get(Calendar.MONTH) + 1;
        int beginTimeDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(endTime);
        int endTimeYear = calendar.get(Calendar.YEAR);
        int endTimeMonth = calendar.get(Calendar.MONTH) + 1;
        int endTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate start = LocalDate.of(beginTimeYear, beginTimeMonth, beginTimeDay);
        LocalDate end = LocalDate.of(endTimeYear, endTimeMonth, endTimeDay);
        Integer workDays = (int)(end.toEpochDay() - start.toEpochDay() + 1);
        return workDays;
    }

    /**
     * 验证当前角色是否为开发人员
     * @param jobRole 角色
     */
    private Boolean getIsDeveloper(Integer jobRole){
        return jobRole == ZSYJobRole.JAVA.getValue()
                || jobRole == ZSYJobRole.C.getValue()
                || jobRole == ZSYJobRole.PHP.getValue()
                || jobRole == ZSYJobRole.FRONT.getValue()
                || jobRole == ZSYJobRole.IOS.getValue()
                || jobRole == ZSYJobRole.ANDROID.getValue()
                || jobRole == ZSYJobRole.ARTIFICIAL.getValue();
    }
}
