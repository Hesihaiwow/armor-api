package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskTempService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sch
 * @DATE 2019/4/2 9:45
 */
@Service
public class ZSYTaskTempService implements IZSYTaskTempService {
    @Autowired
    private IZSYTaskTempMapper taskTempMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    @Autowired
    private IZSYTaskMapper taskMapper;

    @Autowired
    private IZSYTaskUserMapper taskUserMapper;

    @Autowired
    private IZSYUserWeekMapper userWeekMapper;

    @Autowired
    private IZSYTaskLogMapper taskLogMapper;

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private ZSYTaskService taskService;

    @Autowired
    private IZSYStageMapper stageMapper;
    @Autowired
    private IZSYWeekPublishPlanMapper weekPublishPlanMapper;
    @Autowired
    private IZSYTaskTempFunctionMapper functionMapper;

    /**
     * 新增任务(临时)
     * @param addTaskTempReqDTO
     */
    @Override
    @Transactional
    public void addTaskTemp(AddTaskTempReqDTO addTaskTempReqDTO) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        TaskTemp existTaskTemp = taskTempMapper.selectByUserAndTask(userId,addTaskTempReqDTO.getTaskId());
        List<Long> userIds = taskUserMapper.selectUserByTaskId(addTaskTempReqDTO.getTaskId());
        if (existTaskTemp != null || userIds.contains(userId)){
            throw new ZSYServiceException("当前任务您已经存在子任务,请检查");
        }
        TaskTemp taskTemp = new TaskTemp();
        BeanUtils.copyProperties(addTaskTempReqDTO,taskTemp);
        taskTemp.setId(snowFlakeIDHelper.nextId());
        taskTemp.setUserId(userId);
        taskTemp.setCreateTime(new Date());
        taskTemp.setStatus(1);
        taskTemp.setType(2);
        taskTemp.setReviewStatus(1);
        taskTemp.setLevel(1);

        List<UserWeekReqDTO> userWeeks = addTaskTempReqDTO.getUserWeeks();
        List<UserWeekTemp> userWeekTempList = new ArrayList<>();
        BigDecimal totalHours = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(userWeeks)){
            for (UserWeekReqDTO week : userWeeks) {
                UserWeekTemp userWeek = new UserWeekTemp();
                userWeek.setId(snowFlakeIDHelper.nextId());
                userWeek.setHours(BigDecimal.valueOf(week.getHours()));
                userWeek.setTaskId(addTaskTempReqDTO.getTaskId());
                userWeek.setUserId(userId);
                userWeek.setWeekNumber(week.getWeekNumber());
                userWeek.setYear(week.getYear());
                userWeek.setCreateTime(new Date());
                userWeek.setStatus(1);
                userWeekTempList.add(userWeek);
                totalHours = totalHours.add(BigDecimal.valueOf(week.getHours()));
            }
        }else {
            throw new ZSYServiceException("周工时为空");
        }
        if (totalHours.compareTo(addTaskTempReqDTO.getWorkHours())!=0){
            throw new ZSYServiceException("周工作量之和与任务工作量不符,请检查");
        }
        List<TaskTempFunctionReqDTO> functionReqDTOS = addTaskTempReqDTO.getTaskTempFunctionList();
        List<TaskTempFunction> functionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(functionReqDTOS)){
            functionReqDTOS.forEach(functionReqDTO->{
                TaskTempFunction function = new TaskTempFunction();
                BeanUtils.copyProperties(functionReqDTO,function);
                function.setId(snowFlakeIDHelper.nextId());
                function.setTtId(taskTemp.getId());
                functionList.add(function);
            });
        }

        if (taskTempMapper.insertTaskTemp(taskTemp) == 0){
            throw new ZSYServiceException("新增任务(临时)失败");
        }
        if (!CollectionUtils.isEmpty(userWeekTempList)){
            //删除原有的userWeekTemp
            taskTempMapper.deleteUserWeekTempByUserAndTask(userId,addTaskTempReqDTO.getTaskId());
            if (taskTempMapper.insertUserWeekTempBatch(userWeekTempList) == 0){
                throw new ZSYServiceException("批量插入周工作量(临时)失败");
            }
        }
        if (!CollectionUtils.isEmpty(functionList)){
            functionMapper.insertBatch(functionList);
        }
    }

    /**
     * 修改任务
     * @param editTaskTempReqDTO
     */
    @Override
    @Transactional
    public void updateTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO) {
        Long taskId = editTaskTempReqDTO.getTaskId();
        Long userId = editTaskTempReqDTO.getUserId();
        TaskTemp existTaskTemp = taskTempMapper.selectById(editTaskTempReqDTO.getId());
        List<TaskReviewLogBO> taskReviewLogBOS = taskTempMapper.selectTaskReviewLogByTaskTemp(editTaskTempReqDTO.getId());
        if (!CollectionUtils.isEmpty(taskReviewLogBOS)){
            throw new ZSYServiceException("当前任务(临时)已通过审核,无法修改");
        }
        if (existTaskTemp == null){
            throw new ZSYServiceException("当前任务(临时)不存在");
        }
        BeanUtils.copyProperties(editTaskTempReqDTO,existTaskTemp);
        if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0){
            throw new ZSYServiceException("修改任务(临时)失败");
        }

        List<UserWeekReqDTO> userWeeks = editTaskTempReqDTO.getUserWeeks();
        List<UserWeekTemp> userWeekTempList = new ArrayList<>();
        BigDecimal totalHours = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(userWeeks)){
            for (UserWeekReqDTO week : userWeeks) {
                UserWeekTemp userWeekTemp = new UserWeekTemp();
                userWeekTemp.setId(snowFlakeIDHelper.nextId());
                userWeekTemp.setCreateTime(new Date());
                userWeekTemp.setStatus(1);
                userWeekTemp.setHours(BigDecimal.valueOf(week.getHours()));
                userWeekTemp.setTaskId(taskId);
                userWeekTemp.setUserId(userId);
                userWeekTemp.setWeekNumber(week.getWeekNumber());
                userWeekTemp.setYear(week.getYear());
                userWeekTempList.add(userWeekTemp);
                totalHours = totalHours.add(BigDecimal.valueOf(week.getHours()));
            }
        }else {
            throw new ZSYServiceException("周工时为空");
        }
        if (totalHours.compareTo(existTaskTemp.getWorkHours())!=0){
            throw new ZSYServiceException("周工作量之和与任务工作量不符,请检查");
        }
        if (!CollectionUtils.isEmpty(userWeekTempList)){
            //删除原有的userWeekTemp
            taskTempMapper.deleteUserWeekTempByUserAndTask(userId,taskId);
            if (taskTempMapper.insertUserWeekTempBatch(userWeekTempList) == 0){
                throw new ZSYServiceException("批量修改周工作量(临时)失败");
            }
        }

        List<TaskTempFunctionReqDTO> taskTempFunctionList = editTaskTempReqDTO.getTaskTempFunctionList();
        List<TaskTempFunction> functionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskTempFunctionList)){
            taskTempFunctionList.forEach(taskTempFunctionReqDTO -> {
                TaskTempFunction function = new TaskTempFunction();
                BeanUtils.copyProperties(taskTempFunctionReqDTO,function);
                function.setId(snowFlakeIDHelper.nextId());
                function.setTtId(editTaskTempReqDTO.getId());
                functionList.add(function);
            });
        }
        //删除原有的function
        functionMapper.deleteByTtId(editTaskTempReqDTO.getId());
        if (!CollectionUtils.isEmpty(functionList)){
            functionMapper.insertBatch(functionList);
        }
    }

    /**
     * 删除任务
     * @param id
     */
    @Override
    @Transactional
    public void deleteTaskTemp(Long id) {
        Integer userRole = ZSYTokenRequestContext.get().getUserRole();
        TaskTemp existTaskTemp = taskTempMapper.selectById(id);
        if (existTaskTemp == null){
            throw new ZSYServiceException("当前任务(临时)不存在");
        }
        List<TaskReviewLogBO> taskReviewLogBOS = taskTempMapper.selectTaskReviewLogByTaskTemp(id);
        if (ZSYUserRole.ADMINISTRATOR.getValue() != userRole && !CollectionUtils.isEmpty(taskReviewLogBOS)){
            throw new ZSYServiceException("当前任务(临时)已通过审核,无法删除");
        }
        existTaskTemp.setReviewStatus(3);
        if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0){
            throw new ZSYServiceException("删除任务(临时)失败");
        }

        List<UserWeekTemp> userWeekTemps = taskTempMapper.selectUserWeekTempByUserAndTask(existTaskTemp.getUserId(),existTaskTemp.getTaskId());
        List<Long> idList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userWeekTemps)){
            userWeekTemps.stream().forEach(userWeekTemp -> {
                idList.add(userWeekTemp.getId());
            });
        }
        if (!CollectionUtils.isEmpty(idList)){
            if (taskTempMapper.updateUserWeekTempBatch(idList) == 0){
                throw new ZSYServiceException("删除周工作量失败");
            }
        }

        //删除功能点
        functionMapper.deleteByTtId(id);

        taskTempMapper.deleteTaskReviewLog(existTaskTemp.getId());
    }

    /**
     * 个人分页查看任务
     * @param pageNum
     * @param reviewStatus
     * @return
     */
    @Override
    public PageInfo<TaskTempResDTO> getPersonalTaskTempPage(Integer pageNum, Integer reviewStatus) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskTempBO> taskTempBOList = taskTempMapper.selectPersonalTaskTempPage(userId,reviewStatus);
        Page<TaskTempResDTO> taskTempResDTOPage = new Page<>();
        BeanUtils.copyProperties(taskTempBOList,taskTempResDTOPage);
        if (!CollectionUtils.isEmpty(taskTempBOList)){
            taskTempBOList.stream().forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO,taskTempResDTO);
                List<UserWeekTemp> userWeekTempList = taskTempBO.getUserWeekTempList();
                List<UserWeekTempResDTO> userWeekTempResDTOList = new ArrayList<>();
                BeanUtils.copyProperties(userWeekTempList,userWeekTempResDTOList);
                if (!CollectionUtils.isEmpty(userWeekTempList)){
                    for (UserWeekTemp userWeekTemp : userWeekTempList) {
                        UserWeekTempResDTO userWeekTempResDTO = new UserWeekTempResDTO();
                        userWeekTempResDTO.setId(userWeekTemp.getId());
                        userWeekTempResDTO.setHours(userWeekTemp.getHours());
                        userWeekTempResDTO.setYear(userWeekTemp.getYear());
                        userWeekTempResDTO.setWeekNumber(userWeekTemp.getWeekNumber());
                        userWeekTempResDTOList.add(userWeekTempResDTO);
                    }
                    userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getWeekNumber)).collect(Collectors.toList());
                    userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getYear)).collect(Collectors.toList());

                }
                taskTempResDTO.setUserWeekTempList(userWeekTempResDTOList);
                taskTempResDTOPage.add(taskTempResDTO);
            });
        }
        return new PageInfo<>(taskTempResDTOPage);
    }

    /**
     * 个人查看待审核任务
     * @return
     */
    @Override
    public List<TaskResDTO> getPersonalTaskTempList() {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        List<TaskTempBO> taskTempBOList = taskTempMapper.selectPersonalTaskTempList(userId);
        List<TaskResDTO> taskResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskTempBOList)){
            taskTempBOList.stream().forEach(taskTempBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                taskResDTO.setTtId(taskTempBO.getId());
                taskResDTO.setExpand(false);
                taskResDTO.setAvatarUrl(taskTempBO.getAvatarUrl());
                taskResDTO.setCreateBy(taskTempBO.getUserId());
                taskResDTO.setCreateTime(taskTempBO.getCreateTime());
                taskResDTO.setDescription(taskTempBO.getDescription());
                taskResDTO.setEndTime(taskTempBO.getEndTime());
                taskResDTO.setId(taskTempBO.getTaskId());
                taskResDTO.setName(taskTempBO.getTaskName());
                taskResDTO.setPriority(taskTempBO.getPriority());
                taskResDTO.setProjectId(taskTempBO.getProjectId());
                taskResDTO.setProjectImage(taskTempBO.getProjectImage());
                taskResDTO.setProjectName(taskTempBO.getProjectName());
                taskResDTO.setReviewStatus(1);
                taskResDTO.setStatus(1);
                taskResDTO.setType(2);
                taskResDTO.setUserName(taskTempBO.getUserName());

                List<TaskUserResDTO> taskUserResDTOS = Lists.newArrayList();
                TaskUserResDTO taskUserResDTO = new TaskUserResDTO();
                taskUserResDTO.setBeginTime(taskTempBO.getBeginTime());
                taskUserResDTO.setEndTime(taskTempBO.getEndTime());
                taskUserResDTO.setCreateTime(taskTempBO.getCreateTime());
                taskUserResDTO.setDescription(taskTempBO.getDescription());
                taskUserResDTO.setStatus(1);
                taskUserResDTO.setTaskId(taskTempBO.getTaskId());
                taskUserResDTO.setUserId(taskTempBO.getUserId());
                taskUserResDTO.setTaskHours(taskTempBO.getWorkHours().doubleValue());
                taskUserResDTOS.add(taskUserResDTO);

                taskResDTO.setTaskUsers(taskUserResDTOS);

                taskResDTOList.add(taskResDTO);
            });
        }
        return taskResDTOList;
    }

    /**
     * 根据主键查看任务
     * @param ttId
     * @return
     */
    @Override
    public TaskTempResDTO getTaskTempById(Long ttId) {
        TaskTempBO taskTempBO = taskTempMapper.selectTaskTempBOById(ttId);
        TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
        BeanUtils.copyProperties(taskTempBO,taskTempResDTO);
        List<UserWeekTemp> userWeekTempList = taskTempBO.getUserWeekTempList();
        List<UserWeekTempResDTO> userWeekTempResDTOList = new ArrayList<>();
        BeanUtils.copyProperties(userWeekTempList,userWeekTempResDTOList);
        if (!CollectionUtils.isEmpty(userWeekTempList)){
            for (UserWeekTemp userWeekTemp : userWeekTempList) {
                UserWeekTempResDTO userWeekTempResDTO = new UserWeekTempResDTO();
                userWeekTempResDTO.setId(userWeekTemp.getId());
                userWeekTempResDTO.setHours(userWeekTemp.getHours());
                userWeekTempResDTO.setYear(userWeekTemp.getYear());
                userWeekTempResDTO.setWeekNumber(userWeekTemp.getWeekNumber());
                userWeekTempResDTOList.add(userWeekTempResDTO);
            }
            userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getWeekNumber)).collect(Collectors.toList());
            userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getYear)).collect(Collectors.toList());
        }
        taskTempResDTO.setUserWeekTempList(userWeekTempResDTOList);
        List<TaskReviewLogBO> taskReviewLogBOS = taskTempBO.getTaskReviewLogBOS();
        List<TaskReviewLogResDTO> taskReviewLogResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
            taskTempResDTO.setIsChecked(1);
            taskReviewLogBOS.stream().forEach(taskReviewLogBO -> {
                TaskReviewLogResDTO taskReviewLogResDTO = new TaskReviewLogResDTO();
                taskReviewLogResDTO.setCheckUserId(taskReviewLogBO.getCheckUserId());
                taskReviewLogResDTO.setCheckUserName(taskReviewLogBO.getCheckUserName());
                taskReviewLogResDTO.setReviewTime(taskReviewLogBO.getReviewTime());
                taskReviewLogResDTO.setLevel(taskReviewLogBO.getLevel());
                String suggest = taskReviewLogBO.getSuggest();
                if (suggest != null && !suggest.trim().equals("")){
                    taskReviewLogResDTO.setSuggest(taskReviewLogBO.getSuggest());
                }else {
                    taskReviewLogResDTO.setSuggest("无");
                }
                taskReviewLogResDTOList.add(taskReviewLogResDTO);
            });
        }else {
            taskTempResDTO.setIsChecked(0);
        }
        taskTempResDTO.setTaskReviewLogResDTOList(taskReviewLogResDTOList);

        List<TaskTempFunctionBO> functions = taskTempBO.getFunctions();
        List<TaskTempFunctionResDTO> functionResDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(functions)){
            functions.forEach(function->{
                TaskTempFunctionResDTO resDTO = new TaskTempFunctionResDTO();
                BeanUtils.copyProperties(function,resDTO);
                resDTO.setActionName(FunctionAction.getName(function.getAction()));
                Integer level = function.getLevel();
                if (level == 1){
                    resDTO.setLevelName("一级");
                }else if(level == 2){
                    resDTO.setLevelName("二级");
                }else if (level == 3){
                    resDTO.setLevelName("三级");
                }else if (level == 4){
                    resDTO.setLevelName("四级");
                }else if (level == 5){
                    resDTO.setLevelName("五级");
                }
                List<UserAndLevelBO> userAndLevelBOS = functionMapper.selectUserAndLevelByFunction(function.getId());
                List<UserAndLevelResDTO> userAndLevelResDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(userAndLevelBOS)){
                    userAndLevelBOS.forEach(userAndLevelBO -> {
                        UserAndLevelResDTO userAndLevelResDTO = new UserAndLevelResDTO();
                        BeanUtils.copyProperties(userAndLevelBO,userAndLevelResDTO);
                        Integer level2 = userAndLevelBO.getLevel();
                        if (level2 == 1){
                            userAndLevelResDTO.setLevelName("一级");
                        }else if(level2 == 2){
                            userAndLevelResDTO.setLevelName("二级");
                        }else if (level2 == 3){
                            userAndLevelResDTO.setLevelName("三级");
                        }else if (level2 == 4){
                            userAndLevelResDTO.setLevelName("四级");
                        }else if (level2 == 5){
                            userAndLevelResDTO.setLevelName("五级");
                        }
                        userAndLevelResDTOS.add(userAndLevelResDTO);
                    });
                }
                resDTO.setUserAndLevelResDTOS(userAndLevelResDTOS);
                functionResDTOS.add(resDTO);
            });
        }
        taskTempResDTO.setFunctionResDTOList(functionResDTOS);
        return taskTempResDTO;
    }

    /**
     * 根据阶段查询可用的多人任务
     * @param stageId
     * @return
     */
    @Override
    public List<TaskBaseResDTO> getMultipleTaskByStage(Long stageId) {
        List<TaskListBO> taskListBOS = taskMapper.selectTaskInfoByStageId(stageId,ZSYTokenRequestContext.get().getDepartmentId());

        List<TaskBaseResDTO> list = new ArrayList<>();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.stream().forEach(taskListBO -> {
            TaskBaseResDTO taskBaseResDTO = new TaskBaseResDTO();
            taskBaseResDTO.setId(taskListBO.getId());
            taskBaseResDTO.setName(taskListBO.getName());
            list.add(taskBaseResDTO);
        });
        return list;
    }

    /**
     * 管理员分页查看任务
     * @param pageNum
     * @param reviewStatus
     * @return
     */
    @Override
    public PageInfo<TaskTempResDTO> getTaskTempPage(Integer pageNum, Integer reviewStatus) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskTempBO> taskTempBOList = taskTempMapper.selectTaskTempPage(reviewStatus);
        Page<TaskTempResDTO> taskTempResDTOPage = new Page<>();
        BeanUtils.copyProperties(taskTempBOList,taskTempResDTOPage);
        if (!CollectionUtils.isEmpty(taskTempBOList)){
            taskTempBOList.stream().forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO,taskTempResDTO);
                List<UserWeekTemp> userWeekTempList = taskTempBO.getUserWeekTempList();
                List<UserWeekTempResDTO> userWeekTempResDTOList = new ArrayList<>();
                BeanUtils.copyProperties(userWeekTempList,userWeekTempResDTOList);
                if (!CollectionUtils.isEmpty(userWeekTempList)){
                    for (UserWeekTemp userWeekTemp : userWeekTempList) {
                        UserWeekTempResDTO userWeekTempResDTO = new UserWeekTempResDTO();
                        userWeekTempResDTO.setId(userWeekTemp.getId());
                        userWeekTempResDTO.setHours(userWeekTemp.getHours());
                        userWeekTempResDTO.setYear(userWeekTemp.getYear());
                        userWeekTempResDTO.setWeekNumber(userWeekTemp.getWeekNumber());
                        userWeekTempResDTOList.add(userWeekTempResDTO);
                    }
                    userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getWeekNumber)).collect(Collectors.toList());
                    userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getYear)).collect(Collectors.toList());
                }
                taskTempResDTO.setUserWeekTempList(userWeekTempResDTOList);
                taskTempResDTOPage.add(taskTempResDTO);
            });
        }
        return new PageInfo<>(taskTempResDTOPage);
    }

    /**
     * 查询待审核多人任务
     * @param checkUserId
     * @return
     */
    @Override
    public List<TaskTempResDTO> getPendingTaskTempListByCheckUser(Long checkUserId) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        List<UserCheckPeopleBO> userCheckPeopleBOS = userMapper.selectUserByCheckUserId(checkUserId);
        List<Long> controlledUserIds = userCheckPeopleBOS.stream().map(UserCheckPeople::getUserId).collect(Collectors.toList());
        //一级待审核临时任务
        List<TaskTempBO> levelOne = taskTempMapper.selectTaskTempLevelOne(userId);
        //二级待审核临时任务
        List<TaskTempBO> levelTwo = taskTempMapper.selectTaskTempLevelTwo(userId);
        //三级待审核临时任务
        List<TaskTempBO> levelThree = taskTempMapper.selectTaskTempLevelThree(userId);
        List<TaskTempBO> taskTempBOList = new ArrayList<>();
        taskTempBOList.addAll(levelOne);
        taskTempBOList.addAll(levelTwo);
        taskTempBOList.addAll(levelThree);
        List<TaskTempResDTO> taskTempResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskTempBOList)) {
            taskTempBOList.stream().forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO, taskTempResDTO);
                Integer taskLevel = taskTempBO.getTaskLevel();
                if (taskLevel != null){
                    if (taskLevel == 1){
                        taskTempResDTO.setTaskLevelName("一级");
                    }else if(taskLevel == 2){
                        taskTempResDTO.setTaskLevelName("二级");
                    }else if (taskLevel == 3){
                        taskTempResDTO.setTaskLevelName("三级");
                    }else if (taskLevel == 4){
                        taskTempResDTO.setTaskLevelName("四级");
                    }else if (taskLevel == 5){
                        taskTempResDTO.setTaskLevelName("五级");
                    }
                }
                List<UserWeekTemp> userWeekTempList = taskTempBO.getUserWeekTempList();
                List<UserWeekTempResDTO> userWeekTempResDTOList = new ArrayList<>();
                BeanUtils.copyProperties(userWeekTempList, userWeekTempResDTOList);
                if (!CollectionUtils.isEmpty(userWeekTempList)) {
                    for (UserWeekTemp userWeekTemp : userWeekTempList) {
                        UserWeekTempResDTO userWeekTempResDTO = new UserWeekTempResDTO();
                        userWeekTempResDTO.setId(userWeekTemp.getId());
                        userWeekTempResDTO.setHours(userWeekTemp.getHours());
                        userWeekTempResDTO.setYear(userWeekTemp.getYear());
                        userWeekTempResDTO.setWeekNumber(userWeekTemp.getWeekNumber());
                        userWeekTempResDTOList.add(userWeekTempResDTO);
                    }
                    userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getWeekNumber)).collect(Collectors.toList());
                    userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getYear)).collect(Collectors.toList());
                }
                taskTempResDTO.setUserWeekTempList(userWeekTempResDTOList);
                List<TaskReviewLogBO> taskReviewLogBOS = taskTempBO.getTaskReviewLogBOS();
                List<Long> checkUserIds = taskReviewLogBOS.stream().map(TaskReviewLog::getCheckUserId).collect(Collectors.toList());
                if (checkUserIds.contains(ZSYTokenRequestContext.get().getUserId())){
                    taskTempResDTO.setIsChecked(1);
                }else {
                    taskTempResDTO.setIsChecked(0);
                }
                List<TaskReviewLogResDTO> taskReviewLogResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
                    taskReviewLogBOS.stream().forEach(taskReviewLogBO -> {
                        TaskReviewLogResDTO taskReviewLogResDTO = new TaskReviewLogResDTO();
                        taskReviewLogResDTO.setCheckUserId(taskReviewLogBO.getCheckUserId());
                        taskReviewLogResDTO.setCheckUserName(taskReviewLogBO.getCheckUserName());
                        taskReviewLogResDTO.setReviewTime(taskReviewLogBO.getReviewTime());
                        taskReviewLogResDTO.setLevel(taskReviewLogBO.getLevel());
                        String suggest = taskReviewLogBO.getSuggest();
                        if (suggest != null && !suggest.trim().equals("")){
                            taskReviewLogResDTO.setSuggest(taskReviewLogBO.getSuggest());
                        }else {
                            taskReviewLogResDTO.setSuggest("无");
                        }
                        taskReviewLogResDTOList.add(taskReviewLogResDTO);
                    });
                }
                taskTempResDTO.setTaskReviewLogResDTOList(taskReviewLogResDTOList);
                List<TaskTempFunctionBO> functions = taskTempBO.getFunctions();
                List<TaskTempFunctionResDTO> functionResDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(functions)){
                    functions.forEach(function->{
                        TaskTempFunctionResDTO resDTO = new TaskTempFunctionResDTO();
                        BeanUtils.copyProperties(function,resDTO);
                        resDTO.setActionName(FunctionAction.getName(function.getAction()));
                        Integer level = function.getLevel();
                        if (level == 1){
                            resDTO.setLevelName("一级");
                        }else if(level == 2){
                            resDTO.setLevelName("二级");
                        }else if (level == 3){
                            resDTO.setLevelName("三级");
                        }else if (level == 4){
                            resDTO.setLevelName("四级");
                        }else if (level == 5){
                            resDTO.setLevelName("五级");
                        }
                        List<UserAndLevelBO> userAndLevelBOS = functionMapper.selectUserAndLevelByFunction(function.getFunctionId());
                        List<UserAndLevelResDTO> userAndLevelResDTOS = new ArrayList<>();
                        if (!CollectionUtils.isEmpty(userAndLevelBOS)){
                            userAndLevelBOS.forEach(userAndLevelBO -> {
                                UserAndLevelResDTO userAndLevelResDTO = new UserAndLevelResDTO();
                                BeanUtils.copyProperties(userAndLevelBO,userAndLevelResDTO);
                                Integer level2 = userAndLevelBO.getLevel();
                                if (level2 == 1){
                                    userAndLevelResDTO.setLevelName("一级");
                                }else if(level2 == 2){
                                    userAndLevelResDTO.setLevelName("二级");
                                }else if (level2 == 3){
                                    userAndLevelResDTO.setLevelName("三级");
                                }else if (level2 == 4){
                                    userAndLevelResDTO.setLevelName("四级");
                                }else if (level2 == 5){
                                    userAndLevelResDTO.setLevelName("五级");
                                }
                                userAndLevelResDTOS.add(userAndLevelResDTO);
                            });
                        }
                        resDTO.setUserAndLevelResDTOS(userAndLevelResDTOS);
                        functionResDTOS.add(resDTO);
                    });
                }
                taskTempResDTO.setFunctionResDTOList(functionResDTOS);
                taskTempResDTOList.add(taskTempResDTO);
            });
        }
        return taskTempResDTOList;
    }

    /**
     * 查询审核通过多人任务
     * @param checkUserId
     * @return
     */
    @Override
    public PageInfo<TaskTempResDTO> getAccessedTaskTempListByCheckUser(Integer pageNum,Long checkUserId) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1),ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskTempBO> taskTempBOList = taskTempMapper.selectAccessedByCheckUser(checkUserId);
        Page<TaskTempResDTO> taskTempResDTOList = new Page<>();
        BeanUtils.copyProperties(taskTempBOList,taskTempResDTOList);
        if (!CollectionUtils.isEmpty(taskTempBOList)) {
            taskTempBOList.stream().forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO, taskTempResDTO);
                Integer taskLevel = taskTempBO.getTaskLevel();
                if (taskLevel != null){
                    if (taskLevel == 1){
                        taskTempResDTO.setTaskLevelName("一级");
                    }else if(taskLevel == 2){
                        taskTempResDTO.setTaskLevelName("二级");
                    }else if (taskLevel == 3){
                        taskTempResDTO.setTaskLevelName("三级");
                    }else if (taskLevel == 4){
                        taskTempResDTO.setTaskLevelName("四级");
                    }else if (taskLevel == 5){
                        taskTempResDTO.setTaskLevelName("五级");
                    }
                }

                List<UserWeekTemp> userWeekTempList = taskTempBO.getUserWeekTempList();
                List<UserWeekTempResDTO> userWeekTempResDTOList = new ArrayList<>();
                BeanUtils.copyProperties(userWeekTempList, userWeekTempResDTOList);
                if (!CollectionUtils.isEmpty(userWeekTempList)) {
                    for (UserWeekTemp userWeekTemp : userWeekTempList) {
                        UserWeekTempResDTO userWeekTempResDTO = new UserWeekTempResDTO();
                        userWeekTempResDTO.setId(userWeekTemp.getId());
                        userWeekTempResDTO.setHours(userWeekTemp.getHours());
                        userWeekTempResDTO.setYear(userWeekTemp.getYear());
                        userWeekTempResDTO.setWeekNumber(userWeekTemp.getWeekNumber());
                        userWeekTempResDTOList.add(userWeekTempResDTO);
                    }
                    userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getWeekNumber)).collect(Collectors.toList());
                    userWeekTempResDTOList = userWeekTempResDTOList.stream().sorted(Comparator.comparing(UserWeekTempResDTO::getYear)).collect(Collectors.toList());
                }
                taskTempResDTO.setUserWeekTempList(userWeekTempResDTOList);
                List<TaskReviewLogBO> taskReviewLogBOS = taskTempBO.getTaskReviewLogBOS();
                List<Long> checkUserIds = taskReviewLogBOS.stream().map(TaskReviewLog::getCheckUserId).collect(Collectors.toList());
                if (checkUserIds.contains(ZSYTokenRequestContext.get().getUserId())){
                    taskTempResDTO.setIsChecked(1);
                }else {
                    taskTempResDTO.setIsChecked(0);
                }
                List<TaskReviewLogResDTO> taskReviewLogResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
                    taskReviewLogBOS.stream().forEach(taskReviewLogBO -> {
                        TaskReviewLogResDTO taskReviewLogResDTO = new TaskReviewLogResDTO();
                        taskReviewLogResDTO.setCheckUserId(taskReviewLogBO.getCheckUserId());
                        taskReviewLogResDTO.setCheckUserName(taskReviewLogBO.getCheckUserName());
                        taskReviewLogResDTO.setReviewTime(taskReviewLogBO.getReviewTime());
                        taskReviewLogResDTO.setLevel(taskReviewLogBO.getLevel());
                        String suggest = taskReviewLogBO.getSuggest();
                        if (suggest != null && !suggest.trim().equals("")){
                            taskReviewLogResDTO.setSuggest(taskReviewLogBO.getSuggest());
                        }else {
                            taskReviewLogResDTO.setSuggest("无");
                        }
                        taskReviewLogResDTOList.add(taskReviewLogResDTO);
                    });
                }
                taskTempResDTO.setTaskReviewLogResDTOList(taskReviewLogResDTOList);

                List<TaskTempFunctionBO> functions = taskTempBO.getFunctions();
                List<TaskTempFunctionResDTO> functionResDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(functions)){
                    functions.forEach(function->{
                        TaskTempFunctionResDTO resDTO = new TaskTempFunctionResDTO();
                        BeanUtils.copyProperties(function,resDTO);
                        resDTO.setActionName(FunctionAction.getName(function.getAction()));
                        Integer level = function.getLevel();
                        if (level == 1){
                            resDTO.setLevelName("一级");
                        }else if(level == 2){
                            resDTO.setLevelName("二级");
                        }else if (level == 3){
                            resDTO.setLevelName("三级");
                        }else if (level == 4){
                            resDTO.setLevelName("四级");
                        }else if (level == 5){
                            resDTO.setLevelName("五级");
                        }
                        functionResDTOS.add(resDTO);
                    });
                }
                taskTempResDTO.setFunctionResDTOList(functionResDTOS);
                taskTempResDTOList.add(taskTempResDTO);
            });
        }
        return new PageInfo<>(taskTempResDTOList);
    }

    /**
     * 管理员审核任务
     * @param editTaskTempReqDTO
     */
    @Override
    @Transactional
    public void accessTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO){
        if (editTaskTempReqDTO.getTaskLevel() == null){
            throw new ZSYServiceException("任务级别不能为空");
        }
            Long taskId = editTaskTempReqDTO.getTaskId();
            Long userId = editTaskTempReqDTO.getUserId();
            TaskTemp existTaskTemp = taskTempMapper.selectById(editTaskTempReqDTO.getId());
            if (existTaskTemp == null) {
                throw new ZSYServiceException("当前任务(临时)不存在");
            }

        User user = userMapper.selectById(userId);
        //查询申请人有几级审核人
        List<UserCheckPeopleBO> userCheckPeopleBOS = userMapper.selectUserCheckPeopleByUserId(userId);
        //查询申请人,当前临时任务审核日志
//        List<TaskReviewLogBO> taskReviewLogBOS = taskTempMapper.selectTaskReviewLogByTaskTemp(existTaskTemp.getId());
        Integer level = 0;
        for (UserCheckPeopleBO userCheckPeopleBO : userCheckPeopleBOS) {
            if (userCheckPeopleBO.getCheckUserId().equals(ZSYTokenRequestContext.get().getUserId())){
                level = userCheckPeopleBO.getLevel();
            }
        }
        TaskReviewLog existTaskReviewLog = taskTempMapper.selectTaskReviewLogByTaskTempAndCheckUser(editTaskTempReqDTO.getId(),ZSYTokenRequestContext.get().getUserId());
        if (existTaskReviewLog != null){
            throw new ZSYServiceException("你已审核通过,请不要重复审核");
        }
        TaskReviewLog taskReviewLog = new TaskReviewLog();
        taskReviewLog.setId(snowFlakeIDHelper.nextId());
        taskReviewLog.setCheckUserId(ZSYTokenRequestContext.get().getUserId());
        taskReviewLog.setCheckUserName(ZSYTokenRequestContext.get().getUserName());
        taskReviewLog.setLevel(level);
        taskReviewLog.setReviewTime(new Date());
        taskReviewLog.setSuggest(editTaskTempReqDTO.getSuggest());
        taskReviewLog.setTaskTempId(existTaskTemp.getId());
        taskReviewLog.setTaskId(taskId);

            BeanUtils.copyProperties(editTaskTempReqDTO, existTaskTemp);
            existTaskTemp.setLevel(level+1);
            if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0) {
                throw new ZSYServiceException("修改任务(临时)失败");
            }

            List<UserWeekReqDTO> userWeeks = editTaskTempReqDTO.getUserWeeks();
            List<UserWeekTemp> userWeekTempList = new ArrayList<>();
            BigDecimal totalHours = BigDecimal.ZERO;
            if (!CollectionUtils.isEmpty(userWeeks)) {
                for (UserWeekReqDTO week : userWeeks) {
                    UserWeekTemp userWeekTemp = new UserWeekTemp();
                    userWeekTemp.setId(snowFlakeIDHelper.nextId());
                    userWeekTemp.setCreateTime(new Date());
                    userWeekTemp.setStatus(1);
                    userWeekTemp.setHours(BigDecimal.valueOf(week.getHours()));
                    userWeekTemp.setTaskId(taskId);
                    userWeekTemp.setUserId(userId);
                    userWeekTemp.setWeekNumber(week.getWeekNumber());
                    userWeekTemp.setYear(week.getYear());
                    userWeekTempList.add(userWeekTemp);
                    totalHours = BigDecimal.valueOf(week.getHours()).add(totalHours);
                }

            }
            if (totalHours.compareTo(existTaskTemp.getWorkHours()) != 0){
                throw new ZSYServiceException("周工作量之和与任务工作量不符,请检查");
            }
            if (!CollectionUtils.isEmpty(userWeekTempList)) {
                //删除原有的userWeekTemp
                taskTempMapper.deleteUserWeekTempByUserAndTask(userId, taskId);
                if (taskTempMapper.insertUserWeekTempBatch(userWeekTempList) == 0) {
                    throw new ZSYServiceException("批量修改周工作量(临时)失败");
                }
            }
        List<TaskTempFunctionReqDTO> taskTempFunctionList = editTaskTempReqDTO.getTaskTempFunctionList();
        List<TaskTempFunction> functionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskTempFunctionList)){
            taskTempFunctionList.forEach(taskTempFunctionReqDTO -> {
                TaskTempFunction function = new TaskTempFunction();
                BeanUtils.copyProperties(taskTempFunctionReqDTO,function);
                function.setId(snowFlakeIDHelper.nextId());
                function.setTtId(editTaskTempReqDTO.getId());
                functionList.add(function);
            });
        }
        //删除原有的function
        functionMapper.deleteByTtId(editTaskTempReqDTO.getId());
        if (!CollectionUtils.isEmpty(functionList)){
            functionMapper.insertBatch(functionList);
        }

        Long lastCheckUser = userMapper.selectUserLastCheckUser(userId);
        //当前情况:  不是最后一级审核
        if (!lastCheckUser.equals(ZSYTokenRequestContext.get().getUserId())){
            taskTempMapper.insertTaskReviewLog(taskReviewLog);
            taskLogMapper.insert(taskService.buildLog(ZSYTokenRequestContext.get().getUserName() + "通过了" + user.getName() + "的多人任务审核", existTaskTemp.getDescription(), taskId));
        }else {
            taskTempMapper.insertTaskReviewLog(taskReviewLog);

            TaskDetailBO taskTemp = taskMapper.selectTaskDetailByTaskId(taskId);
            if (taskTemp == null) {
                throw new ZSYServiceException("无法找到任务,id:" + taskId);
            }
            if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue() && taskTemp.getType() == ZSYTaskType.PUBLIC_TASK.getValue()) {
                throw new ZSYServiceException("该任务已结束");
            }
            Long stageId = taskTemp.getStageId();
            if (ZSYTaskStage.WAIT_DESIGN.getValue().equals(stageId)){
                if (ZSYJobRole.DESIGN.getValue() == user.getJobRole() || ZSYJobRole.PRODUCT.getValue() == user.getJobRole()){
                    taskTemp.setStageId(ZSYTaskStage.DESIGNING.getValue());
                }
            }else if (ZSYTaskStage.WAIT_DEVELOP.getValue().equals(stageId) && getIsDeveloper(user.getJobRole())){
                taskTemp.setStageId(ZSYTaskStage.DEVELOPING.getValue());
            }else if (ZSYTaskStage.WAIT_TEST.getValue().equals(stageId) && ZSYJobRole.TEST.getValue() == user.getJobRole()){
                taskTemp.setStageId(ZSYTaskStage.TESTING.getValue());
            }
            if (ZSYTaskStage.TESTING.getValue().equals(taskTemp.getStageId())){

            }
            taskMapper.updateByPrimaryKeySelective(taskTemp);
            if (existTaskTemp.getReviewStatus() == 1) {
                existTaskTemp.setReviewStatus(2);
                existTaskTemp.setTaskLevel(editTaskTempReqDTO.getTaskLevel());
                if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0) {
                    throw new ZSYServiceException("审核失败");
                }

                //删除taskUser
                taskUserMapper.deleteByTaskIdAndUserId(taskId, userId);
                //删除userWeek
                userWeekMapper.deleteByTaskIdAndUserId(taskId, userId);

                //新增taskUser
                TaskUser taskUser = new TaskUser();
                taskUser.setId(snowFlakeIDHelper.nextId());
                taskUser.setStatus(1);
                taskUser.setCompleteHours(null);
                taskUser.setCompleteTime(null);
                taskUser.setBeginTime(existTaskTemp.getBeginTime());
                taskUser.setCreateTime(new Date());
                taskUser.setEndTime(existTaskTemp.getEndTime());
                taskUser.setDescription(existTaskTemp.getDescription());
                taskUser.setTaskHours(existTaskTemp.getWorkHours().doubleValue());
                taskUser.setTaskId(taskId);
                taskUser.setUserId(userId);
                taskUser.setStageId(null);
                taskUser.setTaskLevel(editTaskTempReqDTO.getTaskLevel());
                taskUserMapper.insert(taskUser);


//                List<UserWeekTemp> userWeekTemps = taskTempMapper.selectUserWeekTempByUserAndTask(userId, taskId);
                List<UserWeek> userWeekList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(userWeekTempList)) {
                    List<Long> uwtIds = userWeekTempList.stream().map(UserWeekTemp::getId).collect(Collectors.toList());
                    taskTempMapper.checkUserWeekTempBatch(uwtIds);
                    userWeekTempList.stream().forEach(userWeekTemp -> {
                        UserWeek userWeek = new UserWeek();
                        userWeek.setId(snowFlakeIDHelper.nextId());
                        userWeek.setUserId(userId);
                        userWeek.setTaskId(taskId);
                        userWeek.setYear(userWeekTemp.getYear());
                        userWeek.setWeekNumber(userWeekTemp.getWeekNumber());
                        userWeek.setHours(userWeekTemp.getHours().doubleValue());
                        userWeekList.add(userWeek);
                    });
                }
                if (!CollectionUtils.isEmpty(userWeekList)) {
                    if (userWeekMapper.insertList(userWeekList) == 0) {
                        throw new ZSYServiceException("批量插入userWeek失败");
                    }
                }

                taskLogMapper.insert(taskService.buildLog(ZSYTokenRequestContext.get().getUserName() + "通过了" + user.getName() + "的多人任务审核", existTaskTemp.getDescription(), taskId));

                TaskReqDTO taskReqDTO = new TaskReqDTO();
                taskReqDTO.setProjectId(taskTemp.getProjectId());
                taskReqDTO.setBeginTime(taskTemp.getBeginTime());
                taskReqDTO.setCreateBy(taskTemp.getCreateBy());
                taskReqDTO.setDescription(taskTemp.getDescription());
                taskReqDTO.setEndTime(taskTemp.getEndTime());
                taskReqDTO.setFacility(taskTemp.getFacility());
                taskReqDTO.setModifyDescription(taskTemp.getDescription());
                taskReqDTO.setPriority(taskTemp.getPriority());
                taskReqDTO.setStageId(taskTemp.getStageId());
                taskReqDTO.setTags(taskTemp.getTaskTags().stream().map(Tag::getId).collect(Collectors.toList()));
                taskReqDTO.setTaskName(taskTemp.getName());
                taskReqDTO.setTaskType(taskTemp.getType());
                taskReqDTO.setTestTime(taskTemp.getTestTime());
                List<TaskUserBO> taskUsers = taskTemp.getTaskUsers();
                List<TaskUserReqDTO> taskUserReqDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(taskUsers)) {
                    taskUsers.stream().forEach(taskUserBO -> {
                        TaskUserReqDTO taskUserReqDTO = new TaskUserReqDTO();
                        taskUserReqDTO.setBeginTime(taskUserBO.getBeginTime());
                        taskUserReqDTO.setCompleteHours(taskUserBO.getCompleteHours());
                        taskUserReqDTO.setCompleteTime(taskUserBO.getCompleteTime());
                        taskUserReqDTO.setDescription(taskUserBO.getDescription());
                        taskUserReqDTO.setStatus(taskUserBO.getStatus());
                        taskUserReqDTO.setTaskHours(taskUserBO.getTaskHours());
                        taskUserReqDTO.setEndTime(taskUserBO.getEndTime());
                        taskUserReqDTO.setUserId(taskUserBO.getUserId());

                        List<UserWeekReqDTO> userWeekReqDTOS = new ArrayList<>();
                        taskUserBO.getUserWeeks().stream().forEach(userWeek -> {
                            UserWeekReqDTO userWeekReqDTO = new UserWeekReqDTO();
                            userWeekReqDTO.setHours(userWeek.getHours());
                            userWeekReqDTO.setWeekNumber(userWeek.getWeekNumber());
                            userWeekReqDTO.setYear(userWeek.getYear());
                            userWeekReqDTOS.add(userWeekReqDTO);
                        });
                        taskUserReqDTO.setUserWeeks(userWeekReqDTOS);
                        taskUserReqDTOS.add(taskUserReqDTO);
                    });
                }
                TaskUserReqDTO taskUserReqDTO = new TaskUserReqDTO();
                taskUserReqDTO.setUserId(userId);
                taskUserReqDTO.setEndTime(existTaskTemp.getEndTime());
                taskUserReqDTO.setTaskHours(existTaskTemp.getWorkHours().doubleValue());
                taskUserReqDTO.setStatus(1);
                taskUserReqDTO.setDescription(existTaskTemp.getDescription());
                taskUserReqDTO.setCompleteTime(null);
                taskUserReqDTO.setCompleteHours(null);
                taskUserReqDTO.setBeginTime(existTaskTemp.getBeginTime());
                List<UserWeekReqDTO> userWeekReqDTOS = new ArrayList<>();
                userWeekList.stream().forEach(userWeek -> {
                    UserWeekReqDTO userWeekReqDTO = new UserWeekReqDTO();
                    userWeekReqDTO.setYear(userWeek.getYear());
                    userWeekReqDTO.setWeekNumber(userWeek.getWeekNumber());
                    userWeekReqDTO.setHours(userWeek.getHours());
                    userWeekReqDTOS.add(userWeekReqDTO);
                });
                taskUserReqDTO.setUserWeeks(userWeekReqDTOS);
                taskUserReqDTOS.add(taskUserReqDTO);

                taskReqDTO.setTaskUsers(taskUserReqDTOS);

                taskService.missionModified(taskTemp, taskReqDTO);
            } else {
                throw new ZSYServiceException("已通过审核,请检查");
            }
        }
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
    };
//    public void accessTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO){
//            Long taskId = editTaskTempReqDTO.getTaskId();
//            Long userId = editTaskTempReqDTO.getUserId();
//            TaskTemp existTaskTemp = taskTempMapper.selectById(editTaskTempReqDTO.getId());
//            if (existTaskTemp == null) {
//                throw new ZSYServiceException("当前任务(临时)不存在");
//            }
//            BeanUtils.copyProperties(editTaskTempReqDTO, existTaskTemp);
//            if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0) {
//                throw new ZSYServiceException("修改任务(临时)失败");
//            }
//
//            List<UserWeekReqDTO> userWeeks = editTaskTempReqDTO.getUserWeeks();
//            List<UserWeekTemp> userWeekTempList = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(userWeeks)) {
//                userWeeks.stream().forEach(week -> {
//                    UserWeekTemp userWeekTemp = new UserWeekTemp();
//                    userWeekTemp.setId(snowFlakeIDHelper.nextId());
//                    userWeekTemp.setCreateTime(new Date());
//                    userWeekTemp.setStatus(1);
//                    userWeekTemp.setHours(BigDecimal.valueOf(week.getHours()));
//                    userWeekTemp.setTaskId(taskId);
//                    userWeekTemp.setUserId(userId);
//                    userWeekTemp.setWeekNumber(week.getWeekNumber());
//                    userWeekTemp.setYear(week.getYear());
//                    userWeekTempList.add(userWeekTemp);
//                });
//
//            }
//            if (!CollectionUtils.isEmpty(userWeekTempList)) {
//                //删除原有的userWeekTemp
//                taskTempMapper.deleteUserWeekTempByUserAndTask(userId, taskId);
//                if (taskTempMapper.insertUserWeekTempBatch(userWeekTempList) == 0) {
//                    throw new ZSYServiceException("批量修改周工作量(临时)失败");
//                }
//            }
//
//            User user = userMapper.selectById(userId);
//            TaskDetailBO taskTemp = taskMapper.selectTaskDetailByTaskId(taskId);
//            if (taskTemp == null) {
//                throw new ZSYServiceException("无法找到任务,id:" + taskId);
//            }
//            if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue() && taskTemp.getType() == ZSYTaskType.PUBLIC_TASK.getValue()) {
//                throw new ZSYServiceException("该任务已结束");
//            }
//            if (existTaskTemp.getReviewStatus() == 1) {
//                existTaskTemp.setReviewStatus(2);
//                if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0) {
//                    throw new ZSYServiceException("审核失败");
//                }
//
//                //删除taskUser
//                taskUserMapper.deleteByTaskIdAndUserId(taskId, userId);
//                //删除userWeek
//                userWeekMapper.deleteByTaskIdAndUserId(taskId, userId);
//
//                //新增taskUser
//                TaskUser taskUser = new TaskUser();
//                taskUser.setId(snowFlakeIDHelper.nextId());
//                taskUser.setStatus(1);
//                taskUser.setCompleteHours(null);
//                taskUser.setCompleteTime(null);
//                taskUser.setBeginTime(existTaskTemp.getBeginTime());
//                taskUser.setCreateTime(new Date());
//                taskUser.setEndTime(existTaskTemp.getEndTime());
//                taskUser.setDescription(existTaskTemp.getDescription());
//                taskUser.setTaskHours(existTaskTemp.getWorkHours().doubleValue());
//                taskUser.setTaskId(taskId);
//                taskUser.setUserId(userId);
//                taskUser.setStageId(null);
//                taskUserMapper.insert(taskUser);
//
//
//                List<UserWeekTemp> userWeekTemps = taskTempMapper.selectUserWeekTempByUserAndTask(userId, taskId);
//                List<UserWeek> userWeekList = new ArrayList<>();
//                if (!CollectionUtils.isEmpty(userWeekTempList)) {
//                    List<Long> uwtIds = userWeekTempList.stream().map(UserWeekTemp::getId).collect(Collectors.toList());
//                    taskTempMapper.checkUserWeekTempBatch(uwtIds);
//                    userWeekTempList.stream().forEach(userWeekTemp -> {
//                        UserWeek userWeek = new UserWeek();
//                        userWeek.setId(snowFlakeIDHelper.nextId());
//                        userWeek.setUserId(userId);
//                        userWeek.setTaskId(taskId);
//                        userWeek.setYear(userWeekTemp.getYear());
//                        userWeek.setWeekNumber(userWeekTemp.getWeekNumber());
//                        userWeek.setHours(userWeekTemp.getHours().doubleValue());
//                        userWeekList.add(userWeek);
//                    });
//                }
//                if (!CollectionUtils.isEmpty(userWeekList)) {
//                    if (userWeekMapper.insertList(userWeekList) == 0) {
//                        throw new ZSYServiceException("批量插入userWeek失败");
//                    }
//                }
//
//                taskLogMapper.insert(taskService.buildLog(ZSYTokenRequestContext.get().getUserName() + "通过了" + user.getName() + "的多人任务审核", existTaskTemp.getDescription(), taskId));
//
//                TaskReqDTO taskReqDTO = new TaskReqDTO();
//                taskReqDTO.setProjectId(taskTemp.getProjectId());
//                taskReqDTO.setBeginTime(taskTemp.getBeginTime());
//                taskReqDTO.setCreateBy(taskTemp.getCreateBy());
//                taskReqDTO.setDescription(taskTemp.getDescription());
//                taskReqDTO.setEndTime(taskTemp.getEndTime());
//                taskReqDTO.setFacility(taskTemp.getFacility());
//                taskReqDTO.setModifyDescription(taskTemp.getDescription());
//                taskReqDTO.setPriority(taskTemp.getPriority());
//                taskReqDTO.setStageId(taskTemp.getStageId());
//                taskReqDTO.setTags(taskTemp.getTaskTags().stream().map(Tag::getId).collect(Collectors.toList()));
//                taskReqDTO.setTaskName(taskTemp.getName());
//                taskReqDTO.setTaskType(taskTemp.getType());
//                taskReqDTO.setTestTime(taskTemp.getTestTime());
//                List<TaskUserBO> taskUsers = taskTemp.getTaskUsers();
//                List<TaskUserReqDTO> taskUserReqDTOS = new ArrayList<>();
//                if (!CollectionUtils.isEmpty(taskUsers)) {
//                    taskUsers.stream().forEach(taskUserBO -> {
//                        TaskUserReqDTO taskUserReqDTO = new TaskUserReqDTO();
//                        taskUserReqDTO.setBeginTime(taskUserBO.getBeginTime());
//                        taskUserReqDTO.setCompleteHours(taskUserBO.getCompleteHours());
//                        taskUserReqDTO.setCompleteTime(taskUserBO.getCompleteTime());
//                        taskUserReqDTO.setDescription(taskUserBO.getDescription());
//                        taskUserReqDTO.setStatus(taskUserBO.getStatus());
//                        taskUserReqDTO.setTaskHours(taskUserBO.getTaskHours());
//                        taskUserReqDTO.setEndTime(taskUserBO.getEndTime());
//                        taskUserReqDTO.setUserId(taskUserBO.getUserId());
//
//                        List<UserWeekReqDTO> userWeekReqDTOS = new ArrayList<>();
//                        taskUserBO.getUserWeeks().stream().forEach(userWeek -> {
//                            UserWeekReqDTO userWeekReqDTO = new UserWeekReqDTO();
//                            userWeekReqDTO.setHours(userWeek.getHours());
//                            userWeekReqDTO.setWeekNumber(userWeek.getWeekNumber());
//                            userWeekReqDTO.setYear(userWeek.getYear());
//                            userWeekReqDTOS.add(userWeekReqDTO);
//                        });
//                        taskUserReqDTO.setUserWeeks(userWeekReqDTOS);
//                        taskUserReqDTOS.add(taskUserReqDTO);
//                    });
//                }
//                TaskUserReqDTO taskUserReqDTO = new TaskUserReqDTO();
//                taskUserReqDTO.setUserId(userId);
//                taskUserReqDTO.setEndTime(existTaskTemp.getEndTime());
//                taskUserReqDTO.setTaskHours(existTaskTemp.getWorkHours().doubleValue());
//                taskUserReqDTO.setStatus(1);
//                taskUserReqDTO.setDescription(existTaskTemp.getDescription());
//                taskUserReqDTO.setCompleteTime(null);
//                taskUserReqDTO.setCompleteHours(null);
//                taskUserReqDTO.setBeginTime(existTaskTemp.getBeginTime());
//                List<UserWeekReqDTO> userWeekReqDTOS = new ArrayList<>();
//                userWeekList.stream().forEach(userWeek -> {
//                    UserWeekReqDTO userWeekReqDTO = new UserWeekReqDTO();
//                    userWeekReqDTO.setYear(userWeek.getYear());
//                    userWeekReqDTO.setWeekNumber(userWeek.getWeekNumber());
//                    userWeekReqDTO.setHours(userWeek.getHours());
//                    userWeekReqDTOS.add(userWeekReqDTO);
//                });
//                taskUserReqDTO.setUserWeeks(userWeekReqDTOS);
//                taskUserReqDTOS.add(taskUserReqDTO);
//
//                taskReqDTO.setTaskUsers(taskUserReqDTOS);
//
//                taskService.missionModified(taskTemp, taskReqDTO);
//            } else {
//                throw new ZSYServiceException("已通过审核,请检查");
//            }
//
//    }
}
