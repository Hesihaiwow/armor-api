package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.zhixinhuixue.armor.config.ZSYSmsConfig;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.filter.ZSYLoginFilter;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.MD5Helper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.helper.ZSYOKHttpHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.*;
import io.swagger.models.auth.In;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.LoggingMXBean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Tate on 2017/8/7.
 */
@SuppressWarnings("Duplicates")
@Service
public class ZSYTaskService implements IZSYTaskService {

    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskUserMapper taskUserMapper;
    @Autowired
    private IZSYTaskTagMapper taskTagMapper;
    @Autowired
    private IZSYTaskLogMapper taskLogMapper;
    @Autowired
    private IZSYTaskCommentMapper taskCommentMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYUserWeekMapper userWeekMaper;
    @Autowired
    private IZSYStageMapper stageMapper;
    @Autowired
    private IZSYPublishInfoMapper publishInfoMapper;
    @Autowired
    private IZSYFeedbackPlanMapper feedbackPlanMapper;
    @Autowired
    private IZSYFeedbackMapper feedbackMapper;
    @Autowired
    private IZSYFeedbackPlanTaskMapper feedbackPlanTaskMapper;
    @Autowired
    private IZSYTaskTestMapper taskTestMapper;
    @Autowired
    private IZSYTaskExpandMapper expandMapper;

    // sch --
    @Autowired
    private IZSYNotificationMapper notificationMapper;
    @Autowired
    private IZSYTaskEvaluationMapper evaluationMapper;

    @Autowired
    private ZSYSmsConfig smsConfig;
    @Autowired
    private IZSYTaskTempMapper taskTempMapper;
    @Autowired
    private IZSYTaskModifyMapper taskModifyMapper;
    @Autowired
    private IZSYTaskModifyUserWeekMapper taskModifyUserWeekMapper;
    @Autowired
    private IZSYWeekPublishPlanMapper weekPublishPlanMapper;
    @Autowired
    private IZSYTaskTempFunctionMapper taskTempFunctionMapper;
    @Autowired
    private IZSYTaskFunctionMapper taskFunctionMapper;
    @Autowired
    private IZSYTaskModifyFunctionMapper taskModifyFunctionMapper;
    // -- sch


    private static final Logger logger = LoggerFactory.getLogger(ZSYTaskService.class);

    protected TaskLog buildLog(String title, String content, Long taskId) {
        TaskLog taskLog = new TaskLog();
        taskLog.setId(snowFlakeIDHelper.nextId());
        taskLog.setTaskId(taskId);
        taskLog.setTitle(title);
        taskLog.setContent(content);
        taskLog.setCreateTime(new Date());
        taskLog.setUserId(ZSYTokenRequestContext.get().getUserId());
        taskLog.setUserName(ZSYTokenRequestContext.get().getUserName());
        return taskLog;
    }

    /**
     * 检查当前登录用户是否有效
     */
    protected void checkUser() {
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
    }


    /**
     * 添加任务
     *
     * @param taskReqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult addTask(TaskReqDTO taskReqDTO,Long origin) {
        if(origin.equals(ZSYConstants.taskOrigin)){
            checkUser();
        }
        // 判断含有重复的负责人
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<Long> users = taskReqDTO.getTaskUsers().stream().map(TaskUserReqDTO::getUserId).distinct().collect(Collectors.toList());
            if (users.size() < taskReqDTO.getTaskUsers().size()) {
                throw new ZSYServiceException("负责人重复");
            }
        }
        Task task = new Task();
        task.setId(snowFlakeIDHelper.nextId());
        task.setName(taskReqDTO.getTaskName());
        task.setDescription(taskReqDTO.getDescription());
        task.setProjectId(taskReqDTO.getProjectId());
        task.setStageId(taskReqDTO.getStageId());
        task.setEndTime(taskReqDTO.getEndTime());
        task.setTestTime(taskReqDTO.getTestTime());
        task.setBeginTime(taskReqDTO.getBeginTime());
        task.setPriority(taskReqDTO.getPriority());
        task.setStatus(ZSYTaskStatus.DOING.getValue());
        task.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        task.setType(taskReqDTO.getTaskType());
        if (taskReqDTO.getDoc() != null){
            task.setDoc(taskReqDTO.getDoc().trim());
        }
        if (taskReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue()) {
            task.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
            task.setFacility(ZSYTaskFacility.EASY.getValue());
        } else {
            task.setFacility(taskReqDTO.getFacility());
            // 多人任务直接通过
            task.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
            // 排序的index
            Integer index = taskMapper.selectLastIndexByStageId(task.getStageId());
            if (index == null) {
                task.setSort(0);
            } else {
                task.setSort(index + 1);
            }
        }
        if(origin.equals(ZSYConstants.taskOrigin)){//任务来源于计划中时，origin为任务创建人
            if(taskReqDTO.getCreateBy()!=null){
                task.setCreateBy(taskReqDTO.getCreateBy());
            }else {
                task.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            }

        }else{
            task.setCreateBy(origin);
        }

        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());

        //任务功能点
        List<TaskFunctionReqDTO> functionReqDTOS = taskReqDTO.getFunctionReqDTOS();
        List<TaskFunction> functions = new ArrayList<>();
        if (!CollectionUtils.isEmpty(functionReqDTOS)){
            functionReqDTOS.forEach(functionReqDTO->{
                TaskFunction function = new TaskFunction();
                BeanUtils.copyProperties(functionReqDTO,function);
                function.setId(snowFlakeIDHelper.nextId());
                function.setTaskId(task.getId());
                function.setFunction(functionReqDTO.getFunction().trim());
                functions.add(function);
            });
        }

        task.setExamine(ZSYTaskExamine.NOTEXAM.getValue());
        // 插入新任务
        taskMapper.insert(task);
        //插入任务功能点
        if (!CollectionUtils.isEmpty(functions)){
            taskFunctionMapper.insertBatch(functions);
        }
        // 插入任务用户
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<TaskUser> taskUsers = Lists.newArrayList();
            taskReqDTO.getTaskUsers().forEach(user -> {
                TaskUser taskUser = new TaskUser();
                taskUser.setId(snowFlakeIDHelper.nextId());
                taskUser.setTaskId(task.getId());
                taskUser.setUserId(user.getUserId());
                taskUser.setDescription(user.getDescription());
                taskUser.setBeginTime(user.getBeginTime());
                taskUser.setEndTime(user.getEndTime());
                taskUser.setTaskHours(user.getTaskHours());
                taskUser.setCreateTime(new Date());
                taskUser.setStatus(ZSYTaskUserStatus.DOING.getValue());
                taskUsers.add(taskUser);

                List<UserWeek> userWeeks = Lists.newArrayList();
                if (taskReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue()) {
                    UserWeek userWeek = new UserWeek();
                    userWeek.setId(snowFlakeIDHelper.nextId());
                    userWeek.setTaskId(task.getId());
                    userWeek.setUserId(user.getUserId());
                    userWeek.setHours(user.getTaskHours());
                    userWeek.setYear(DateHelper.getYears(user.getEndTime()));
                    userWeek.setWeekNumber(DateHelper.getCurrentWeekNumber(user.getEndTime()));
                    userWeeks.add(userWeek);
                } else {
                    user.getUserWeeks().forEach(week ->{
                        UserWeek userWeek = new UserWeek();
                        userWeek.setId(snowFlakeIDHelper.nextId());
                        userWeek.setTaskId(task.getId());
                        userWeek.setUserId(user.getUserId());
                        userWeek.setYear(week.getYear());
                        userWeek.setHours(week.getHours());
                        userWeek.setWeekNumber(week.getWeekNumber());
                        userWeeks.add(userWeek);
                    });
                }

                userWeekMaper.insertList(userWeeks);
            });
            taskUserMapper.insertList(taskUsers);
        }
        // 插入任务标签
        if (taskReqDTO.getTags() != null && taskReqDTO.getTags().size() > 0) {
            List<TaskTag> taskTags = Lists.newArrayList();
            taskReqDTO.getTags().forEach(tag -> {
                TaskTag taskTag = new TaskTag();
                taskTag.setId(snowFlakeIDHelper.nextId());
                taskTag.setTaskId(task.getId());
                taskTag.setTagId(tag);
                taskTags.add(taskTag);
            });
            taskTagMapper.insertList(taskTags);
        }
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "创建了任务", task.getName(), task.getId()));
        if(origin==0){
            return ZSYResult.success();

        }else{
            return ZSYResult.success().data(task.getId());
        }
    }


    /***
     * 修改任务
     * @param taskId
     * @param taskReqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult modifyTask(Long taskId, TaskReqDTO taskReqDTO) {
        TaskDetailBO taskTemp = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskTemp == null) {
            logger.warn("无法找到任务,id:{}", taskId);
            throw new ZSYServiceException("无法找到任务,id:" + taskId);
        }
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue() && taskTemp.getType() == ZSYTaskType.PUBLIC_TASK.getValue()) {
            throw new ZSYServiceException("该任务已结束");
        }
        checkUser();

        Task task = new Task();
        //查询编辑任务时是否修改工作时间
        TaskDetailBO taskTempBo = taskMapper.selectTaskDetailByTaskId(taskId);
        if(taskTempBo.getTaskUsers().size()!=taskReqDTO.getTaskUsers().size()){
            task.setExamine(ZSYTaskExamine.NOTEXAM.getValue());
        }else{
            taskTempBo.getTaskUsers().forEach(taskUser ->{
                taskReqDTO.getTaskUsers().forEach(taskUserReq ->{
                    if(taskUser.getUserId().equals(taskUserReq.getUserId())){
                        if(!taskUser.getTaskHours().equals(taskUserReq.getTaskHours())){
                            task.setExamine(ZSYTaskExamine.NOTEXAM.getValue());
                        }
                    }
                });
            });
        }
        //查询taskUser
        List<Long> oldTaskUserIds = taskUserMapper.selectUserByTaskId(taskId);

        taskTagMapper.deleteByTaskId(taskId);
        taskUserMapper.deleteByTaskId(taskId);
        taskCommentMapper.deleteByTaskId(taskId);
        userWeekMaper.deleteByTaskId(taskId);

        // 判断含有重复的负责人
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<Long> users = taskReqDTO.getTaskUsers().stream().map(TaskUserReqDTO::getUserId).distinct().collect(Collectors.toList());
            if (users.size() < taskReqDTO.getTaskUsers().size()) {
                throw new ZSYServiceException("负责人重复");
            }
        }
        task.setId(taskId);
        task.setName(taskReqDTO.getTaskName());
        if (taskReqDTO.getDoc() != null){
            task.setDoc(taskReqDTO.getDoc().trim());
        }
        task.setDescription(taskReqDTO.getDescription());
        task.setProjectId(taskReqDTO.getProjectId());
        task.setStageId(taskReqDTO.getStageId());
        task.setEndTime(taskReqDTO.getEndTime());
        task.setBeginTime(taskReqDTO.getBeginTime());
        task.setTestTime(taskReqDTO.getTestTime());
        task.setPriority(taskReqDTO.getPriority());
        task.setFacility(taskReqDTO.getFacility());
        task.setUpdateTime(new Date());
        task.setCreateBy(taskReqDTO.getCreateBy());
        // 修改任务
        taskMapper.updateByPrimaryKeySelective(task);

        List<TaskFunction> functionList = taskFunctionMapper.selectListByTaskId(taskId);
        List<TaskFunctionReqDTO> functionReqDTOS = taskReqDTO.getFunctionReqDTOS();
        if (!CollectionUtils.isEmpty(functionReqDTOS)){
            if (!CollectionUtils.isEmpty(functionList)){
                if (functionList.size()>functionReqDTOS.size()){
                    throw new ZSYServiceException("任务功能点不可删除,请检查");
                }
            }
            //过滤出新增的功能点
            List<TaskFunctionReqDTO> newFunctions = functionReqDTOS.stream()
                    .filter(functionReqDTO -> functionReqDTO.getId() == null).collect(Collectors.toList());

            //原有的功能点
            List<TaskFunctionReqDTO> oldFunctions = functionReqDTOS.stream()
                    .filter(functionReqDTO -> functionReqDTO.getId() != null).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(functionList) && !CollectionUtils.isEmpty(oldFunctions)){
                for (TaskFunctionReqDTO oldFunction : oldFunctions) {
                    for (TaskFunction taskFunction : functionList) {
                        if (oldFunction.getId().equals(taskFunction.getId())){
                            taskFunction.setAction(oldFunction.getAction());
                            taskFunction.setFunction(oldFunction.getFunction());
                            taskFunction.setModuleId(oldFunction.getModuleId());
                        }
                    }
                }
                //批量修改原来的功能点
                taskFunctionMapper.updateBatch(functionList);
            }

            if (!CollectionUtils.isEmpty(newFunctions)){
                List<TaskFunction> newFunctionList = new ArrayList<>();
                newFunctions.forEach(newFunction->{
                    TaskFunction function = new TaskFunction();
                    BeanUtils.copyProperties(newFunction,function);
                    function.setFunction(newFunction.getFunction().trim());
                    function.setId(snowFlakeIDHelper.nextId());
                    function.setTaskId(taskId);
                    newFunctionList.add(function);
                });
                //批量插入新的功能点
                taskFunctionMapper.insertBatch(newFunctionList);
            }

        }
        if (ZSYTaskStage.TESTING.getValue().equals(task.getStageId())){
            //移动到测试中阶段

            //校验是否已经存在
            WeekPublishPlan exist = weekPublishPlanMapper.selectByTaskId(task.getId());
            if (exist == null){
                WeekPublishPlan weekPublishPlan = new WeekPublishPlan();
                weekPublishPlan.setId(snowFlakeIDHelper.nextId());
                weekPublishPlan.setTaskId(task.getId());
                weekPublishPlan.setCanOnline(0);
                weekPublishPlan.setCondition("");

                weekPublishPlanMapper.insert(weekPublishPlan);
            }
        }
        // 插入任务用户
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<TaskUser> taskUsers = Lists.newArrayList();
            taskReqDTO.getTaskUsers().forEach(user -> {
                TaskUser taskUser = new TaskUser();
                taskUser.setId(snowFlakeIDHelper.nextId());
                taskUser.setTaskId(task.getId());
                taskUser.setUserId(user.getUserId());
                taskUser.setDescription(user.getDescription());
                taskUser.setBeginTime(user.getBeginTime());
                taskUser.setEndTime(user.getEndTime());
                taskUser.setTaskHours(user.getTaskHours());
                taskUser.setCreateTime(new Date());
                if (user.getStatus() != null) {
                    taskUser.setStatus(user.getStatus());
                } else {
                    taskUser.setStatus(ZSYTaskUserStatus.DOING.getValue());
                }
                if (user.getCompleteHours() != null) {
                    taskUser.setCompleteHours(user.getCompleteHours());
                }
                if (user.getCompleteTime() != null) {
                    taskUser.setCompleteTime(user.getCompleteTime());
                }
                taskUsers.add(taskUser);

                List<UserWeek> userWeeks = Lists.newArrayList();
                if(taskReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue()){
                        UserWeek userWeek = new UserWeek();
                        userWeek.setId(snowFlakeIDHelper.nextId());
                        userWeek.setTaskId(task.getId());
                        userWeek.setUserId(user.getUserId());
                        userWeek.setHours(user.getTaskHours());
                        userWeek.setWeekNumber(DateHelper.getCurrentWeekNumber(user.getEndTime()));
                        userWeek.setYear(DateHelper.getYears(user.getEndTime()));
                        userWeeks.add(userWeek);
                }else{
                    if(user.getUserWeeks().size()<1){
                        throw new ZSYServiceException("请检查各成员周工作量是否填写完整");
                    }
                    user.getUserWeeks().forEach(week ->{
                        UserWeek userWeek = new UserWeek();
                        userWeek.setId(snowFlakeIDHelper.nextId());
                        userWeek.setTaskId(task.getId());
                        userWeek.setUserId(user.getUserId());
                        userWeek.setHours(week.getHours());
                        userWeek.setWeekNumber(week.getWeekNumber());
                        userWeek.setYear(week.getYear());
                        userWeeks.add(userWeek);
                    });
                }
                userWeekMaper.insertList(userWeeks);
            });
            taskUserMapper.insertList(taskUsers);
            List<Long> newTaskUserIds = taskUsers.stream().map(TaskUser::getUserId).collect(Collectors.toList());
            List<Long> deleteTaskUserIds = new ArrayList<>();
            List<Long> existTaskUserIds = new ArrayList<>();
            deleteTaskUserIds = oldTaskUserIds.stream().filter(item->!newTaskUserIds.contains(item)).collect(Collectors.toList());
            deleteTaskUserIds.stream().forEach(deleteTaskUserId ->{
                TaskTemp existTaskTemp = taskTempMapper.selectByUserAndTask(deleteTaskUserId, taskId);
                TaskModify taskModify = taskModifyMapper.selectByTaskAndUser(taskId, deleteTaskUserId);
                if (existTaskTemp != null){
                    taskTempMapper.deleteByUserAndTask(deleteTaskUserId,taskId);
                    taskTempMapper.deleteTaskReviewLog(existTaskTemp.getId());
                    taskTempMapper.deleteUserWeekTempByUserAndTask(deleteTaskUserId,taskId);
                }
                if (taskModify != null){
                    taskModifyMapper.deleteById(taskModify.getId());
                    taskModifyUserWeekMapper.deleteByTmId(taskModify.getId());
                }
            });

        }else {
            oldTaskUserIds.stream().forEach(userId->{
                TaskTemp existTaskTemp = taskTempMapper.selectByUserAndTask(userId, taskId);
                TaskModify taskModify = taskModifyMapper.selectByTaskAndUser(taskId, userId);
                if (existTaskTemp != null){
                    taskTempMapper.deleteByUserAndTask(userId,taskId);
                    taskTempMapper.deleteTaskReviewLog(existTaskTemp.getId());
                    taskTempMapper.deleteUserWeekTempByUserAndTask(userId,taskId);
                }
                if (taskModify != null){
                    taskModifyMapper.deleteById(taskModify.getId());
                    taskModifyUserWeekMapper.deleteByTmId(taskModify.getId());
                }
            });

        }
        // 插入任务标签
        if (taskReqDTO.getTags() != null && taskReqDTO.getTags().size() > 0) {
            List<TaskTag> taskTags = Lists.newArrayList();
            taskReqDTO.getTags().forEach(tag -> {
                TaskTag taskTag = new TaskTag();
                taskTag.setId(snowFlakeIDHelper.nextId());
                taskTag.setTaskId(task.getId());
                taskTag.setTagId(tag);
                taskTags.add(taskTag);
            });
            taskTagMapper.insertList(taskTags);
        }
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "修改了任务", taskReqDTO.getModifyDescription(), task.getId()));

        // sch --
        //任务发生修改,新增通知
        missionModified(taskTemp,taskReqDTO);
        // -- sch
        // 个人任务修改工时，更新积分
        if (taskReqDTO.getTaskType() == ZSYTaskType.PRIVATE_TASK.getValue() && taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            taskTemp.getTaskUsers().forEach(userOld -> {
                // 减去用户积分
                User userTemp = userMapper.selectById(userOld.getUserId());
                BigDecimal currentIntegral = userTemp.getIntegral();
                User userBO = new User();
                userBO.setId(userOld.getUserId());
                userBO.setIntegral(currentIntegral.subtract(new BigDecimal(userOld.getTaskHours())));
                userMapper.updateSelectiveById(userBO);
            });
            taskReqDTO.getTaskUsers().forEach(user -> {
                userIntegralMapper.deleteUserIntegral(taskId, user.getUserId());
                UserIntegral userIntegral = new UserIntegral();
                userIntegral.setId(snowFlakeIDHelper.nextId());
                userIntegral.setTaskId(taskId);
                userIntegral.setUserId(user.getUserId());
                userIntegral.setIntegral(new BigDecimal(user.getTaskHours()));
                userIntegral.setOrigin(ZSYIntegralOrigin.SYSTEM.getValue());
                userIntegral.setDescription("完成了多人任务：" + user.getDescription());
                userIntegral.setCreateTime(new Date());
                userIntegralMapper.insert(userIntegral);
                // 修改用户积分
                User userTemp = userMapper.selectById(user.getUserId());
                BigDecimal currentIntegral = userTemp.getIntegral();
                User userBO = new User();
                userBO.setId(user.getUserId());
                userBO.setIntegral(currentIntegral.add(userIntegral.getIntegral()));
                userMapper.updateSelectiveById(userBO);

            });
        }


        return ZSYResult.success();
    }


    /**
     * 审核任务
     *
     * @param taskId
     * @param auditStatus
     * @return
     */
    @Override
    @Transactional
    public ZSYResult auditTask(Long taskId,Integer level,Long userId,Integer auditStatus) {
        checkUser();
        Task taskTemp = taskMapper.selectByPrimaryKey(taskId);
        TaskUser taskUser = taskUserMapper.selectByTaskAndUser(taskId,userId);
        if (taskTemp == null) {
            logger.warn("无法找到任务,id:{}", taskId);
            throw new ZSYServiceException("无法找到任务,id:" + taskId);
        }
        if (taskTemp.getReviewStatus() != null && (taskTemp.getReviewStatus() == ZSYReviewStatus.REJECT.getValue()
                || taskTemp.getReviewStatus() == ZSYReviewStatus.ACCEPT.getValue())) {
            logger.warn("该任务已被审核,id:{}", ZSYReviewStatus.getName(auditStatus), taskId);
            throw new ZSYServiceException("该任务已被审核");
        }
        if (taskUser == null){
            logger.warn("无法找到任务人员,userId:{}", userId);
            throw new ZSYServiceException("无法找到任务人员,userId:{}" + userId);
        }
        taskUser.setTaskLevel(level);
        Task task = new Task();
        task.setId(taskId);
        task.setReviewStatus(auditStatus);
        task.setUpdateTime(new Date());
        taskMapper.updateByPrimaryKeySelective(task);
        taskUserMapper.updateByPrimaryKeySelective(taskUser);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYReviewStatus.getName(auditStatus), taskTemp.getName(), taskTemp.getId()));
        return ZSYResult.success();
    }

    /**
     * 完成个人任务
     *
     * @param taskCompleteReqDTO
     * @return
     */
    @Override
    @Transactional
    public ZSYResult completePrivateTask(TaskCompleteReqDTO taskCompleteReqDTO) {
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
        Task taskTemp = taskMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskId());
        Optional.ofNullable(taskTemp).orElseThrow(() -> new ZSYServiceException("找不到任务,id:" + taskCompleteReqDTO.getTaskId()));
        TaskUser taskUserTemp = taskUserMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskUserId());
        Optional.ofNullable(taskUserTemp).orElseThrow(() -> new ZSYServiceException("找不到任务用户,id:" + taskCompleteReqDTO.getTaskUserId()));

        if (taskTemp.getType() != ZSYTaskType.PRIVATE_TASK.getValue()) {
            logger.warn("该任务非个人任务,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务非个人任务");
        }
        if (taskTemp.getReviewStatus() != ZSYReviewStatus.ACCEPT.getValue()) {
            logger.warn("该任务未审核通过,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务未审核通过");
        }
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            logger.warn("该任务已被关闭,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务已被关闭");
        }
        if (taskUserTemp.getStatus() == ZSYTaskUserStatus.COMPLETED.getValue()) {
            logger.warn("该任务已完成,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务已完成");
        }

        // 修改子任务状态为 已完成
        TaskUser taskUser = new TaskUser();
        taskUser.setId(taskCompleteReqDTO.getTaskUserId());
        taskUser.setStatus(ZSYTaskUserStatus.COMPLETED.getValue());
        taskUser.setCompleteHours(taskCompleteReqDTO.getCompleteHours());
        taskUser.setCompleteTime(taskCompleteReqDTO.getCompleteTime());
        taskUserMapper.updateByPrimaryKeySelective(taskUser);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "完成了任务",
                taskUserTemp.getDescription(), taskTemp.getId()));

        // 关闭主任务
        Task task = new Task();
        task.setId(taskCompleteReqDTO.getTaskId());
        task.setStatus(ZSYTaskStatus.FINISHED.getValue());
        task.setCompleteTime(taskCompleteReqDTO.getCompleteTime());
        taskMapper.updateByPrimaryKeySelective(task);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "关闭了任务",
                taskTemp.getName(), taskTemp.getId()));


        // 插入评价
        TaskComment taskComment = new TaskComment();
        taskComment.setId(snowFlakeIDHelper.nextId());
        taskComment.setTaskId(taskCompleteReqDTO.getTaskId());
        taskComment.setTaskUserId(ZSYTokenRequestContext.get().getUserId());
        taskComment.setGrade(ZSYIntegral.A.getName());// 默认评价为A
        taskComment.setIntegral(ZSYIntegral.A.getValue());
        taskComment.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        taskComment.setCreateTime(new Date());
        taskCommentMapper.insert(taskComment);

        // 插入积分记录
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setId(snowFlakeIDHelper.nextId());
        userIntegral.setTaskId(taskCompleteReqDTO.getTaskId());
        userIntegral.setUserId(ZSYTokenRequestContext.get().getUserId());
        userIntegral.setIntegral(new BigDecimal(taskUserTemp.getTaskHours()));
        userIntegral.setCreateTime(new Date());
        userIntegral.setOrigin(1);
        userIntegral.setDescription("完成了单人任务:" + taskTemp.getDescription());
        userIntegralMapper.insert(userIntegral);

        // 修改用户积分
        BigDecimal currentIntegral = userTemp.getIntegral();
        User user = new User();
        user.setId(ZSYTokenRequestContext.get().getUserId());
        user.setIntegral(currentIntegral.add(userIntegral.getIntegral()));
        userMapper.updateSelectiveById(user);
        return ZSYResult.success();
    }

    /**
     * 完成我的任务
     *
     * @param taskCompleteReqDTO
     * @return
     */
    @Override
    public ZSYResult completePublicTask(TaskCompleteReqDTO taskCompleteReqDTO) {
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
        Task taskTemp = taskMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskId());
        Optional.ofNullable(taskTemp).orElseThrow(() -> new ZSYServiceException("无法找到任务,id:" + taskCompleteReqDTO.getTaskId()));
        TaskUser taskUserTemp = taskUserMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskUserId());
        Optional.ofNullable(taskUserTemp).orElseThrow(() -> new ZSYServiceException("无法找到任务阶段,id:" + taskCompleteReqDTO.getTaskId()));

        if (taskTemp.getType() != ZSYTaskType.PUBLIC_TASK.getValue()) {
            logger.warn("该任务为个人任务,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务为个人任务");
        }
        if (taskTemp.getReviewStatus() != ZSYReviewStatus.ACCEPT.getValue()) {
            logger.warn("该任务未审核通过,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务未审核通过");
        }
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            logger.warn("该任务已被关闭,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务已被关闭");
        }
        if (taskUserTemp.getStatus() == ZSYTaskUserStatus.COMPLETED.getValue()) {
            logger.warn("该任务已完成,id:{}", taskTemp.getId());
            throw new ZSYServiceException("该任务已完成");
        }

        // 修改阶段状态为 已完成
        TaskUser taskUser = new TaskUser();
        taskUser.setId(taskCompleteReqDTO.getTaskUserId());
        taskUser.setStatus(ZSYTaskUserStatus.COMPLETED.getValue());
        taskUser.setCompleteHours(taskCompleteReqDTO.getCompleteHours());
        taskUser.setCompleteTime(taskCompleteReqDTO.getCompleteTime());
        taskUserMapper.updateByPrimaryKeySelective(taskUser);
        // 插入日志
        taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "完成了任务",
                taskUserTemp.getDescription(), taskTemp.getId()));
        // sch --
        //多人任务子任务完成,新增通知
        sonMissionCompleted(taskCompleteReqDTO);
        // -- sch
        return ZSYResult.success();
    }


    /**
     * 获取任务详情
     *
     * @param taskId
     * @return
     */
    @Override
    public ZSYResult<TaskDetailResDTO> getTaskDetail(Long taskId) {
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        Optional.ofNullable(taskDetailBO).orElseThrow(() -> new ZSYServiceException("无法找到任务,id:" + taskId));

        // copy 任务基本属性
        TaskDetailResDTO taskDetailResDTO = new TaskDetailResDTO();
        BeanUtils.copyProperties(taskDetailBO, taskDetailResDTO);
        if (taskDetailResDTO.getStageName() == null){
            taskDetailResDTO.setStageName("无");
        }
        // copy 任务标签
        List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
        taskDetailBO.getTaskTags().stream().forEach(tag -> {
            TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
            taskTagResDTO.setId(tag.getId());
            taskTagResDTO.setColor(tag.getColor());
            taskTagResDTO.setName(tag.getName());
            taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
            taskTagResDTOS.add(taskTagResDTO);
        });
        taskDetailResDTO.setTags(taskTagResDTOS);

        //任务功能点
        List<TaskFunctionBO> taskFunctionBOS = taskDetailBO.getFunctionBOS();
        List<TaskFunctionResDTO> taskFunctionResDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskFunctionBOS)){
            taskFunctionBOS.forEach(taskFunctionBO -> {
                TaskFunctionResDTO resDTO = new TaskFunctionResDTO();
                BeanUtils.copyProperties(taskFunctionBO,resDTO);
                resDTO.setActionName(FunctionAction.getName(taskFunctionBO.getAction()));
                taskFunctionResDTOS.add(resDTO);
            });
        }
        if (ZSYTokenRequestContext.get().getUserRole()>ZSYUserRole.ADMINISTRATOR.getValue()){
            //我的任务功能点
            TaskTemp temp = taskTempMapper.selectByUserAndTask(ZSYTokenRequestContext.get().getUserId(), taskId);
            if (temp!= null){
                List<TaskTempFunctionBO> functionBOS = taskTempFunctionMapper.selectListByTtId(temp.getId());
                List<TaskTempFunctionResDTO> functionResDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(functionBOS)){
                    functionBOS.forEach(functionBO->{
                        TaskTempFunctionResDTO resDTO = new TaskTempFunctionResDTO();
                        BeanUtils.copyProperties(functionBO,resDTO);
                        resDTO.setActionName(FunctionAction.getName(functionBO.getAction()));
                        Integer level = functionBO.getLevel();
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
                    taskDetailResDTO.setMyFunctionResDTOS(functionResDTOS);
                }
            }

        }

        taskDetailResDTO.setFunctionResDTOS(taskFunctionResDTOS);
        // copy 任务阶段
        List<TaskUserResDTO> taskUserResDTOS = new ArrayList<>();

        List<String> proName = Lists.newArrayList();
        taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
            TaskUserResDTO taskUserResDTO = new TaskUserResDTO();
            // sch --
            Integer taskLevel = taskUserBO.getTaskLevel();
            if (taskLevel != null){
                taskUserResDTO.setTaskLevel(taskLevel);
                if (taskLevel == 1){
                    taskUserResDTO.setTaskLevelName("一级");
                }else if(taskLevel == 2){
                    taskUserResDTO.setTaskLevelName("二级");
                }else if (taskLevel == 3){
                    taskUserResDTO.setTaskLevelName("三级");
                }else if (taskLevel == 4){
                    taskUserResDTO.setTaskLevelName("四级");
                }else if (taskLevel == 5){
                    taskUserResDTO.setTaskLevelName("五级");
                }
            }
            List<EvaluationBO> evaluationBOS = new ArrayList<>();
            if (ZSYTokenRequestContext.get().getUserRole() >= ZSYUserRole.PROJECT_MANAGER.getValue()){
                //查看某个人对当前用户的评价
                evaluationBOS = evaluationMapper.selectSomeoneToMe(taskId,taskUserBO.getUserId(),ZSYTokenRequestContext.get().getUserId());

            }else if (ZSYTokenRequestContext.get().getUserRole() == ZSYUserRole.ADMINISTRATOR.getValue()){
                //管理员查看其他人对某个人的评价
                evaluationBOS = evaluationMapper.selectOthersToMe(taskId,taskUserBO.getUserId());

            }
            List<EvaluationBO> evaluationBOS1 = evaluationMapper.selectMeToOthers(taskId, taskUserBO.getUserId());
            if (!CollectionUtils.isEmpty(evaluationBOS1) && evaluationBOS1.size()>0){
                taskUserResDTO.setIsEvaluated(1);
            }else {
                taskUserResDTO.setIsEvaluated(0);
            }
            List<EvaluationResDTO> evaluationResDTOList = new ArrayList<>();
            Double totalScore = 0.0;
            Integer size = 0;
            if (!CollectionUtils.isEmpty(evaluationBOS)){
                for (EvaluationBO evaluationBO : evaluationBOS) {
                    EvaluationResDTO resDTO = new EvaluationResDTO();
                    resDTO.setEvaluateUserId(evaluationBO.getEvaluateUserId());
                    resDTO.setTaskId(taskId);
                    resDTO.setTaskUserName(evaluationBO.getTaskUserName());
                    resDTO.setEvaluateUserName(evaluationBO.getEvaluateUserName());
                    resDTO.setTaskUserId(evaluationBO.getTaskUserId());
                    resDTO.setEvaluateTime(evaluationBO.getEvaluateTime());
                    List<EvaluationScoreBO> evaluationScoreBOS = evaluationBO.getEvaluationScoreBOS();
                    List<EvaluationScoreResDTO> evaluationScoreResDTOList = new ArrayList<>();
                    Integer totalIntegral = 0;
                    Double singleTotalScore = 0.0;
                    for (EvaluationScoreBO evaluationScoreBO : evaluationScoreBOS) {
                        EvaluationScoreResDTO evaluationScoreResDTO = new EvaluationScoreResDTO();
                        evaluationScoreResDTO.setId(evaluationScoreBO.getId());
                        evaluationScoreResDTO.setEvaluationOption(evaluationScoreBO.getEvaluationOption());
                        evaluationScoreResDTO.setScore(evaluationScoreBO.getScore());
                        evaluationScoreResDTO.setEvaluationOptionName(ZSYTaskEvaluationOption.getName(evaluationScoreBO.getEvaluationOption()));
                        totalIntegral += evaluationScoreBO.getIntegral();
                        singleTotalScore += evaluationScoreBO.getScore();
                        size ++;
                        evaluationScoreResDTOList.add(evaluationScoreResDTO);
                    }
                    resDTO.setEvaluationScoreResDTOS(evaluationScoreResDTOList);
                    BigDecimal avaIntegral = BigDecimal.valueOf(totalIntegral).divide(BigDecimal.valueOf(evaluationScoreBOS.size()),2,BigDecimal.ROUND_HALF_UP);
                    resDTO.setAvgIntegral(avaIntegral);
                    evaluationResDTOList.add(resDTO);
                    totalScore += singleTotalScore;
                }

            }
            Integer jobRole = taskUserBO.getJobRole();
            taskUserResDTO.setJobRole(jobRole);
            if (jobRole == 0){
                taskUserResDTO.setJobRoleName("测试");
            }else if (jobRole == 1){
                taskUserResDTO.setJobRoleName("开发");
            }else if (jobRole == 2){
                taskUserResDTO.setJobRoleName("设计");
            }else if (jobRole == 3){
                taskUserResDTO.setJobRoleName("产品");
            }
            taskUserResDTO.setEvaluationResDTOS(evaluationResDTOList);

            //个人子任务任务功能点
            TaskTemp taskTemp = taskTempMapper.selectByUserAndTask(taskUserBO.getUserId(), taskId);
            List<TaskTempFunctionResDTO> functionResDTOS = new ArrayList<>();
            List<TaskTempFunctionBO> functionBOS = new ArrayList<>();
            if (taskTemp != null){
                functionBOS = taskTempFunctionMapper.selectListByTtId(taskTemp.getId());
                if (!CollectionUtils.isEmpty(functionBOS)){
                    functionBOS.forEach(functionBO->{
                        TaskTempFunctionResDTO resDTO = new TaskTempFunctionResDTO();
                        BeanUtils.copyProperties(functionBO,resDTO);
                        resDTO.setActionName(FunctionAction.getName(functionBO.getAction()));
                        Integer level = functionBO.getLevel();
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
            }
            taskUserResDTO.setFunctionResDTOList(functionResDTOS);
            // -- sch
            BeanUtils.copyProperties(taskUserBO, taskUserResDTO);
            // copy 评价
            List<TaskCommentResDTO> taskCommentResDTOS = new ArrayList<>();
            taskUserBO.getTaskComments().stream().forEach(taskCommentBO -> {
                TaskCommentResDTO taskCommentResDTO = new TaskCommentResDTO();
                BeanUtils.copyProperties(taskCommentBO, taskCommentResDTO, "taskId", "taskUserId");
                taskCommentResDTOS.add(taskCommentResDTO);
            });
            taskUserResDTO.setComments(taskCommentResDTOS);

            // copy 周任务工作量
            List<UserWeekResDTO> userWeekResDTOS = new ArrayList<>();
            taskUserBO.getUserWeeks().stream().forEach(userWeekBO -> {
                UserWeekResDTO userWeekResDTO = new UserWeekResDTO();
                BeanUtils.copyProperties(userWeekBO, userWeekResDTO);
                userWeekResDTOS.add(userWeekResDTO);
            });
            taskUserResDTO.setUserWeeks(userWeekResDTOS);

            //查询进行中任务
            List<TaskBO> taskBOS = taskMapper.selectTaskByStatus(ZSYTaskStatus.DOING.getValue(),
                    ZSYReviewStatus.ACCEPT.getValue(), ZSYTaskUserStatus.DOING.getValue(), taskUserResDTO.getUserId());
            List<UserTaskResDTO> taskList = new ArrayList<>();
            if (taskBOS != null && taskBOS.size() >= 0) {
                taskBOS.stream().forEach(taskBO -> {
                    UserTaskResDTO taskResDTO = new UserTaskResDTO();
                    taskResDTO.setTaskName(taskBO.getName());
                    taskBO.getTaskUsers().stream().forEach(taskUser -> {
                        if(taskUser.getUserId().equals(taskUserBO.getUserId())){
                            taskResDTO.setBeginTime(taskUser.getBeginTime());
                            taskResDTO.setEndTime(taskUser.getEndTime());
                            taskResDTO.setTaskHours(taskUser.getTaskHours());
                            taskResDTO.setId(taskUser.getTaskId());
                        }
                    });

                    taskList.add(taskResDTO);
                });
            }
            taskUserResDTO.setUserTask(taskList);

            User user = userMapper.selectById(taskUserResDTO.getUserId());
            if(user.getJobRole() == ZSYJobRole.PROGRAMER.getValue() && taskDetailResDTO.getStageName().contains("测试")){
                taskUserResDTO.setProTest(true);
                proName.add(user.getName());
            }else{
                taskUserResDTO.setProTest(false);
            }
            taskUserResDTO.setCommentGrade(null);
            taskUserResDTOS.add(taskUserResDTO);
            if (!CollectionUtils.isEmpty(evaluationBOS)){
                if (ZSYUserRole.ADMINISTRATOR.getValue() == ZSYTokenRequestContext.get().getUserRole()){
                    BigDecimal avgScore = BigDecimal.valueOf(totalScore).divide(BigDecimal.valueOf(size),2,BigDecimal.ROUND_HALF_UP);
                    taskUserResDTO.setAvgScore(avgScore);
                }
            }
        });
        if(taskDetailResDTO.getStageName().contains("测试") && taskTestMapper.selectTesting(taskDetailBO.getId()) == 0){
            taskDetailResDTO.setTesting(true);
        }else {
            taskDetailResDTO.setTesting(false);
        }
        taskDetailResDTO.setUsers(taskUserResDTOS);
        List<TaskUserResDTO> collect = taskDetailResDTO.getUsers().stream().filter(taskUserResDTO -> taskUserResDTO.getUserId().equals(ZSYTokenRequestContext.get().getUserId())).collect(Collectors.toList());
        taskDetailResDTO.setMyTaskLevelName("");
        if (!CollectionUtils.isEmpty(collect)){
            taskDetailResDTO.setMyTaskLevelName(collect.get(0).getTaskLevelName());
        }
        taskDetailResDTO.setProNames(proName);
        return ZSYResult.success().data(taskDetailResDTO);
    }

    /**
     * 按状态查询任务
     *
     * @return
     */
    @Override
    public ZSYResult<List<TaskResDTO>> getTaskByStatus(Integer status, Integer reviewStatus, Integer taskUserStatus, Long userId) {
        List<TaskBO> taskBOS = taskMapper.selectTaskByStatus(status, reviewStatus, taskUserStatus, userId);
        List<TaskResDTO> taskList = new ArrayList<>();
        if (taskBOS != null && taskBOS.size() >= 0) {
            taskBOS.stream().forEach(taskBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                BeanUtils.copyProperties(taskBO, taskResDTO);
//                if(expandMapper.findIsExpand(taskBO.getId(),ZSYTokenRequestContext.get().getUserId())>0){
//                    taskResDTO.setExpand(true);
//                }else {
//                    taskResDTO.setExpand(false);
//                }
                TaskModify taskModify = taskModifyMapper.selectTaskAndUser(taskBO.getId(),ZSYTokenRequestContext.get().getUserId());
                if (taskModify == null){
                    taskResDTO.setExpand(false);
                }else{
                    taskResDTO.setExpand(true);
                }

                taskList.add(taskResDTO);
            });
        }
        return ZSYResult.success().data(taskList);
    }

    @Override
    public ZSYResult<List<TaskTestResDTO>> getTestingTask(Long userId) {
        List<TaskTestBO> taskBOS = taskTestMapper.selectTestTask(userId);
        List<TaskTestResDTO> taskList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskBOS)) {
            taskBOS.stream().forEach(taskTestBO -> {
                TaskTestResDTO taskResDTO = new TaskTestResDTO();
                User user = userMapper.selectById(taskTestBO.getUserId());
                BeanUtils.copyProperties(taskTestBO, taskResDTO);
                taskResDTO.setAvatarUrl(user.getAvatarUrl());
                taskResDTO.setUserName(user.getName());
                double hor=taskTestBO.getPercent()*taskTestBO.getHours()*0.01;
                double value = new BigDecimal(hor).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
                taskResDTO.setHours(value);
                taskList.add(taskResDTO);
            });
        }
        return ZSYResult.success().data(taskList);
    }

    /**
     * 获取已完成&已评价的任务，包含积分
     *
     * @return
     */
    @Override
    public PageInfo<TaskResDTO> getFinishedTask(Integer pageNum) {
        PageHelper.startPage(pageNum, 5);
        Page<TaskBO> taskBOS = taskMapper.selectFinishedTask(ZSYTokenRequestContext.get().getUserId());
        Page<TaskResDTO> page = new Page<>();
        BeanUtils.copyProperties(taskBOS, page);
        if (taskBOS != null && taskBOS.size() >= 0) {
            taskBOS.stream().forEach(taskBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                BeanUtils.copyProperties(taskBO, taskResDTO);
                if (taskResDTO.getType() == ZSYTaskType.PUBLIC_TASK.getValue() && taskResDTO.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
                    if (taskBO.getTaskUsers() != null && taskBO.getTaskUsers().size() > 0) {
                        Long taskUserId = taskBO.getTaskUsers().get(0).getId();
                        Long taskId = taskBO.getTaskUsers().get(0).getTaskId();
                        List<TaskComment> taskComment = taskMapper.findTaskComment(taskId, taskUserId);
                        if (!CollectionUtils.isEmpty(taskComment)){
                            OptionalDouble average = taskComment.stream().mapToInt(map -> {
                                String grade = map.getGrade();
                                Integer value = ZSYIntegral.getValue(grade);
                                if (value == null) {
                                    throw new ZSYServiceException("无法找到评价等级:" + grade);
                                }
                                return value;
                            }).average();
                            if (average.getAsDouble() >= 90) {
                                taskResDTO.setIntegralGrade("A");
                            } else if (average.getAsDouble() >= 80) {
                                taskResDTO.setIntegralGrade("B");
                            } else {
                                taskResDTO.setIntegralGrade("C");
                            }
                        }
                        List<EvaluationBO> evaluationBOS = evaluationMapper.selectOthersToMe(taskId, ZSYTokenRequestContext.get().getUserId());
                        if (!CollectionUtils.isEmpty(evaluationBOS)){
                        }
                    }
                }
                page.add(taskResDTO);
            });
        }
        PageInfo<TaskResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 完成主任务
     *
     * @param taskId
     * @return
     */
    @Override
    public ZSYResult completeMasterTask(Long taskId) {
        checkUser();
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskDetailBO == null) {
            throw new ZSYServiceException("无法找到任务,id:" + taskId);
        }
        if (taskDetailBO.getTaskUsers() == null || taskDetailBO.getTaskUsers().size() == 0) {
            throw new ZSYServiceException("当前任务无阶段信息");
        }
        if (taskDetailBO.getTaskUsers().size() < 2) {
            throw new ZSYServiceException("多人任务至少需要添加2个成员");
        }
        // 验证子任务是否都已经完成了
        long count = taskDetailBO.getTaskUsers().stream().filter(taskUserBO -> taskUserBO.getStatus() == ZSYTaskUserStatus.COMPLETED.getValue()).count();
        if (count != taskDetailBO.getTaskUsers().size()) {
            throw new ZSYServiceException("任务阶段未完成");
        }
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(ZSYTaskStatus.COMPLETED.getValue());
        task.setCompleteTime(new Date());
        task.setUpdateTime(new Date());
        // 将任务状态改为已完成（待评价）
        taskMapper.updateByPrimaryKeySelective(task);

        // sch --
        //主任务完成,新增通知
        missionCompleted(taskId);
        // -- sch
        //查找计划并判断是否完成计划
//        List<FeedbackPlanTaskBO> planTasks = feedbackPlanMapper.getTaskIdFromPlan(taskId);
//        if(planTasks.size()>0){
//            Boolean planComplete = true;
//            for(int i=0;i<planTasks.size();i++){
//                Task taskPlan = taskMapper.selectByPrimaryKey(planTasks.get(i).getTaskId());
//                if(taskPlan.getStatus()!=ZSYTaskStatus.COMPLETED.getValue()){
//                    planComplete = false;
//                }
//            }
//            if(planComplete){
//                Feedback feedback =feedbackMapper.selectById(planTasks.get(0).getId());
//                feedback.setStatus(ZSYTaskStatus.COMPLETED.getValue());
//                feedbackMapper.updateByFeedbackId(feedback);
//            }
//        }

        // 插入日志
        taskLogMapper.insert(buildLog("阶段全部完成", ZSYTokenRequestContext.get().getUserName()+"将全部阶段标记为已完成", taskId));
        return ZSYResult.success();
    }

    /**
     * 添加评价
     *
     * @param commentReqDTO
     * @return
     */
    @Override
    public ZSYResult addComment(CommentReqDTO commentReqDTO) {
        // 检查是否登录
        checkUser();
        Long taskId = commentReqDTO.getTaskId();
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskDetailBO.getTaskUsers() == null || taskDetailBO.getTaskUsers().size() == 0) {
            throw new ZSYServiceException("任务阶段为空，无法评价");
        }
        if (taskDetailBO.getStatus() != ZSYTaskStatus.COMPLETED.getValue()) {
            throw new ZSYServiceException("该任务暂未完成，无法评价");
        }
        if (taskDetailBO.getType() == ZSYTaskType.PRIVATE_TASK.getValue() || taskDetailBO.getTaskUsers().size() == 1) {
            throw new ZSYServiceException("该任务为单人任务无需评价");
        }
        // 检查是否已经评价完了
        List<Long> userIds = taskDetailBO.getTaskUsers().stream().map(TaskUser::getUserId).distinct().collect(Collectors.toList());
        boolean commentCompleted;
        List<Long> commentedNum = new ArrayList<>();
        taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
            if (taskUserBO.getTaskComments() != null && userIds != null) {
                if (taskUserBO.getTaskComments().size() == (userIds.size() - 1)) {
                    userIds.stream().forEach(uid -> {
                        // 过滤评论中的重复uid，并找出uid的所有评论
                        long count = taskUserBO.getTaskComments().stream()
                                .filter(distinctByKey(p -> p.getCreateBy()))
                                .filter(taskCommentBO -> taskCommentBO.getCreateBy().equals(uid)).count();
                        commentedNum.add(count);
                    });
                }
            }
        });
        if (userIds.size() > 1) {
            int sum = commentedNum.stream().mapToInt(i -> i.intValue()).sum();
            commentCompleted = (sum == ((userIds.size() - 1) * taskDetailBO.getTaskUsers().size()));
            if (commentCompleted || taskDetailBO.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
                throw new ZSYServiceException("任务已结束");
            }
        }
        if (commentReqDTO.getComments() == null || commentReqDTO.getComments().size() == 0) {
            throw new ZSYServiceException("评价不能为空");
        }
        List<TaskComment> commentList = new ArrayList<>();
        commentReqDTO.getComments().stream().forEach(commentSalveDTO -> {
            Integer value = ZSYIntegral.getValue(commentSalveDTO.getGrade());
            if (value == null) {
//                throw new ZSYServiceException("无法找到评价等级:" + commentSalveDTO.getGrade());
                throw new ZSYServiceException("评价等级不能为空");
            }
            TaskComment taskComment = new TaskComment();
            taskComment.setId(snowFlakeIDHelper.nextId());
            taskComment.setTaskId(commentReqDTO.getTaskId());
            taskComment.setTaskUserId(commentSalveDTO.getTaskUserId());
            taskComment.setGrade(commentSalveDTO.getGrade());
            taskComment.setIntegral(value);
            taskComment.setDescription(commentSalveDTO.getDescription());
            taskComment.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            taskComment.setCreateTime(new Date());
            commentList.add(taskComment);
        });
        taskCommentMapper.insertList(commentList);
         // 修改子任务状态
       /* commentReqDTO.getComments().stream().forEach(comment -> {
            TaskUser taskUser = new TaskUser();
            taskUser.setId(comment.getTaskUserId());
            taskUser.setStatus(ZSYTaskUserStatus.COMMENTED.getValue());
            taskUserMapper.updateByPrimaryKeySelective(taskUser);
        }); */
        return ZSYResult.success();
    }

    /**
     * 主任务完成，结算积分
     */
    @Override
    @Async
    public void finishTask(Long taskId) {
        this.settlementTask(taskId);
    }

    /**
     * 结算积分
     *
     * @return
     */
    @Override
    public void syncSettlementTask() {
        List<Task> taskList = taskMapper.findNotFinishedTask();
        if (taskList != null && taskList.size() > 0) {
            BlockingQueue<Task> queue = new ArrayBlockingQueue<>(taskList.size());
            for (Task task : taskList) {
                try {
                    queue.put(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (!queue.isEmpty()) {
                try {
                    Task task = queue.take();
                    this.settlementTask(task.getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 结算积分
     *
     * @param taskId
     */
    public void settlementTask(Long taskId) {
        logger.info("正在结算任务积分, taskId:{}", taskId);
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskDetailBO.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            logger.warn("任务已结算,id{}", taskId);
            return;
        }
        List<Long> userIds = taskDetailBO.getTaskUsers().stream().map(TaskUser::getUserId).distinct().collect(Collectors.toList());
        boolean commentCompleted = false;
        List<Long> commentedNum = new ArrayList<>();
        taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
            if (taskUserBO.getTaskComments() != null && userIds != null) {
                if (taskUserBO.getTaskComments().size() == (userIds.size() - 1)) {
                    userIds.stream().forEach(uid -> {
                        // 过滤评论中的重复uid，并找出uid的所有评论
                        long count = taskUserBO.getTaskComments().stream()
                                .filter(distinctByKey(p -> p.getCreateBy()))
                                .filter(taskCommentBO -> taskCommentBO.getCreateBy().equals(uid)).count();
                        commentedNum.add(count);
                    });
                }
            }
        });
        if (userIds.size() > 1) {
            int sum = commentedNum.stream().mapToInt(i -> i.intValue()).sum();
            commentCompleted = (sum == ((userIds.size() - 1) * taskDetailBO.getTaskUsers().size()));
        }
        if (commentCompleted) {
            // 计算积分
            taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
                OptionalDouble average = taskUserBO.getTaskComments().stream().mapToInt(map -> {
                    String grade = map.getGrade();
                    Integer value = ZSYIntegral.getValue(grade);
                    if (value == null) {
                        throw new ZSYServiceException("无法找到评价等级:" + grade);
                    }
                    return value;
                }).average();
                average.orElseThrow(() -> new ZSYServiceException("积分计算异常"));
                BigDecimal integral = BigDecimal.valueOf(taskUserBO.getTaskHours())
                        .multiply(new BigDecimal(average.getAsDouble()))
                        .divide(new BigDecimal(100), 1, BigDecimal.ROUND_HALF_UP).setScale(1);
                UserIntegral userIntegral = new UserIntegral();
                userIntegral.setId(snowFlakeIDHelper.nextId());
                userIntegral.setTaskId(taskUserBO.getTaskId());
                userIntegral.setUserId(taskUserBO.getUserId());
                userIntegral.setIntegral(integral);
                userIntegral.setOrigin(1);
                userIntegral.setDescription("完成了多人任务：" + taskDetailBO.getName());
                userIntegral.setCreateTime(new Date());
                userIntegralMapper.insert(userIntegral);
                Task task = new Task();
                task.setId(taskId);
                task.setStatus(ZSYTaskStatus.FINISHED.getValue());
                task.setUpdateTime(new Date());
                taskMapper.updateByPrimaryKeySelective(task);
                // 修改子任务状态
                TaskUser taskUser = new TaskUser();
                taskUser.setId(taskUserBO.getId());
                taskUser.setStatus(ZSYTaskUserStatus.COMMENTED.getValue());
                taskUserMapper.updateByPrimaryKeySelective(taskUser);
                // 修改用户积分
                User userTemp = userMapper.selectById(taskUserBO.getUserId());
                BigDecimal currentIntegral = userTemp.getIntegral();
                User user = new User();
                user.setId(taskUserBO.getUserId());
                user.setIntegral(currentIntegral.add(integral));
                userMapper.updateSelectiveById(user);

            });
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 分页查询任务
     *
     * @param taskListReqDTO
     * @return
     */
    @Override
    public PageInfo<TaskListResDTO> getTaskListPage(TaskListReqDTO taskListReqDTO) {
        long time1 = System.currentTimeMillis();
        if (taskListReqDTO.getPageSize() != null && taskListReqDTO.getPageNum() != null) {
            PageHelper.startPage(taskListReqDTO.getPageNum(), taskListReqDTO.getPageSize());
        }
        taskListReqDTO.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
        Page<TaskListBO> taskListBOS = taskMapper.selectPage(taskListReqDTO);
        long time2 = System.currentTimeMillis();
        logger.info("查询耗时: "+(time2-time1)+"ms");
        Page<TaskListResDTO> list = new Page();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.stream().forEach(taskListBO -> {
            TaskListResDTO taskListResDTO = new TaskListResDTO();
            BeanUtils.copyProperties(taskListBO, taskListResDTO, "tags");
            List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
            taskListBO.getTags().stream().forEach(tag -> {
                TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
                taskTagResDTO.setColor(tag.getColor());
                taskTagResDTO.setName(tag.getName());
                taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
                taskTagResDTOS.add(taskTagResDTO);
            });
            taskListResDTO.setTags(taskTagResDTOS);
            list.add(taskListResDTO);
        });
        long time3 = System.currentTimeMillis();
        logger.info("准备返回值耗时: "+(time3-time2)+"ms");
        return new PageInfo<>(list);
    }

    @Override
    public List<TaskListResDTO> getTaskExport() {
        List<TaskListBO> taskListBOS = taskMapper.selectPage1();
        List<TaskListResDTO> list = Lists.newArrayList();
        taskListBOS.stream().forEach(taskListBO -> {
            TaskListResDTO taskListResDTO = new TaskListResDTO();
            BeanUtils.copyProperties(taskListBO, taskListResDTO, "tags");
            List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
            list.add(taskListResDTO);
        });
        return list;
    }

    /**
     * 获取所有待审核状态的任务
     *
     * @return
     */
    @Override
    public ZSYResult<List<TaskResDTO>> getAllWaitAudit() {
        List<TaskBO> taskBOS = taskMapper.selectAllWaitAudit(ZSYTokenRequestContext.get().getDepartmentId());
        List<TaskResDTO> taskList = new ArrayList<>();
        if (taskBOS != null && taskBOS.size() >= 0) {
            taskBOS.stream().forEach(taskBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                BeanUtils.copyProperties(taskBO, taskResDTO);
                taskList.add(taskResDTO);
            });
        }
        return ZSYResult.success().data(taskList);
    }

    /**
     * 获取所有待审核的任务
     * @author sch
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<TaskResDTO> getPendingTaskPage(Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1),ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskBO> taskBOS = taskMapper.selectPendingTaskPage(ZSYTokenRequestContext.get().getDepartmentId());
        Page<TaskResDTO> taskResDTOPage = new Page<>();
        BeanUtils.copyProperties(taskBOS,taskResDTOPage);
        if (!CollectionUtils.isEmpty(taskBOS)){
            taskBOS.stream().forEach(taskBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                BeanUtils.copyProperties(taskBO,taskResDTO);
                taskResDTOPage.add(taskResDTO);
            });
        }
        return new PageInfo<>(taskResDTOPage);
    }

    /**
     * 获取所有审核通过的任务
     *
     * @return
     */
    @Override
    public PageInfo<TaskResDTO> getAuditSuccessAll(Integer pageNum) {
        if (pageNum != null) {
            PageHelper.startPage(pageNum, 5);
        }
        Page<TaskBO> taskBOS = taskMapper.selectAllAuditSuccess(ZSYTokenRequestContext.get().getDepartmentId());
        Page<TaskResDTO> page = new Page<>();
        BeanUtils.copyProperties(taskBOS, page);
        taskBOS.stream().forEach(taskBO -> {
            TaskResDTO taskResDTO = new TaskResDTO();
            BeanUtils.copyProperties(taskBO, taskResDTO);
            if (taskResDTO.getUserIntegral() != null && taskResDTO.getType() == ZSYTaskType.PUBLIC_TASK.getValue()) {
                if (taskResDTO.getUserIntegral() >= 90) {
                    taskResDTO.setIntegralGrade(ZSYIntegral.A.getName());
                } else if (taskResDTO.getUserIntegral() >= 80) {
                    taskResDTO.setIntegralGrade(ZSYIntegral.B.getName());
                } else {
                    taskResDTO.setIntegralGrade(ZSYIntegral.C.getName());
                }
            } else {
                taskResDTO.setIntegralGrade(ZSYIntegral.A.getName());
            }
            page.add(taskResDTO);
        });
        return new PageInfo<>(page);
    }

    /**
     * 获取所有待评价的任务
     *
     * @param userId
     * @return
     */
    @Override
    public ZSYResult<List<TaskDetailBO>> getAllWaitComment(Long userId) {
        long time1 = System.currentTimeMillis();
        List<TaskDetailBO> taskBOS = taskMapper.selectAllNotClosed(userId);
        long time2 = System.currentTimeMillis();
        System.out.println("查询待评价任务耗时: "+(time2-time1)+"ms");
        List<TaskDetailBO> waitCommentList = new ArrayList<>();
        for (TaskDetailBO taskBO : taskBOS) {
            // 只要有1个评价就说明该任务已经评价过了
            boolean hasNext = true;
            List<TaskUserBO> collect = taskBO.getTaskUsers().stream().filter(f ->
                    !f.getUserId().equals(userId)
            ).collect(Collectors.toList());

            for (TaskUserBO taskUserBO :collect) {
                if (hasNext) {
                    if (taskUserBO.getTaskComments()==null ||taskUserBO.getTaskComments().size()==0) {
                        waitCommentList.add(taskBO);
                        hasNext = false;
                        continue;
                    }
                    long count = taskUserBO.getTaskComments().stream().filter(user ->
                            user.getCreateBy().equals(userId)
                    ).count();
                    if (count==0) {
                        waitCommentList.add(taskBO);
                        hasNext = false;
                    }
                }else {
                    break;
                }
            }
        }
        return ZSYResult.success().data(waitCommentList);
    }


    /**
     * 获取用户评价记录
     *
     * @param userId
     * @return
     */
    @Override
    public ZSYResult<List<TaskDetailBO>> getCommented(Long userId,Integer page) {
        PageHelper.startPage(page, ZSYConstants.PAGE_SIZE_WAIT);
        long time1 = System.currentTimeMillis();
        Page<TaskDetailBO> taskBOS = taskMapper.selectCommented(userId);
        long time2 = System.currentTimeMillis();
        System.out.println("查询已评价任务耗时: "+(time2-time1)+"ms");
        Page<TaskDetailBO> commentEndList = new Page<>();
        BeanUtils.copyProperties(taskBOS,commentEndList);
        for (TaskDetailBO taskBO : taskBOS) {
            // 只要有1个评价就说明该任务已经评价过了
            boolean hasNext = true;
            List<TaskUserBO> collect = taskBO.getTaskUsers().stream().filter(f ->
                    !f.getUserId().equals(userId)
            ).collect(Collectors.toList());

            for (TaskUserBO taskUserBO :collect) {
                if (hasNext) {

                    long count = taskUserBO.getTaskComments().stream().filter(user ->
                            user.getCreateBy().equals(userId)
                    ).count();
                    if (count>0) {
                        commentEndList.add(taskBO);
                        hasNext = false;
                    }
                }else {
                    break;
                }
            }
        }
        PageInfo<TaskDetailBO> pageInfo = new PageInfo<>(commentEndList);
        BeanUtils.copyProperties(commentEndList, pageInfo);
        return ZSYResult.success().data(pageInfo);
    }

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    @Override
    public ZSYResult deleteTaskById(Long taskId) {
        checkUser();
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        Task taskTemp = taskMapper.selectByPrimaryKey(taskId);
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            throw new ZSYServiceException("当前任务已结束，无法删除");
        }
        Task task = new Task();
        task.setId(taskId);
        task.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        //查询taskUser
//        List<Long> taskUserIds = taskUserMapper.selectUserByTaskId(taskId);
        userWeekMaper.deleteByTaskId(taskId);
        feedbackPlanTaskMapper.deleteByTaskId(taskId);
        taskMapper.updateByPrimaryKeySelective(task);
        taskUserMapper.deleteByTaskId(taskId);

//        if (!CollectionUtils.isEmpty(taskUserIds)){
//            taskUserIds.stream().forEach(userId->{
//                TaskTemp existTaskTemp = taskTempMapper.selectByUserAndTask(userId, taskId);
//                if (existTaskTemp != null){
//                    taskTempMapper.deleteByUserAndTask(userId,taskId);
//                    taskTempMapper.deleteTaskReviewLog(existTaskTemp.getId());
//                    taskTempMapper.deleteUserWeekTempByUserAndTask(userId,taskId);
//                    taskTempFunctionMapper.deleteByTtId(existTaskTemp.getId());
//                }
//                TaskModify taskModify = taskModifyMapper.selectByTaskAndUser(taskId, userId);
//                if (taskModify != null){
//                    taskModifyMapper.deleteById(taskModify.getId());
//                    taskModifyUserWeekMapper.deleteByTmId(taskModify.getId());
//                }
//            });
//        }else {
        List<TaskTemp> taskTemps = taskTempMapper.selectByTask(taskId);
        if (!CollectionUtils.isEmpty(taskTemps)){
            List<Long> collect = taskTemps.stream().map(TaskTemp::getId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)){
                collect.forEach(ttId->{
                    taskTempFunctionMapper.deleteByTtId(ttId);
                });
            }
        }
        taskFunctionMapper.deleteByTaskId(taskId);
        taskTempMapper.deleteByTask(taskId);
        taskTempMapper.deleteUserWeekTempByTask(taskId);
        taskTempMapper.deleteReviewLogByTask(taskId);
        taskModifyFunctionMapper.deleteByTask(taskId);
        taskModifyMapper.deleteByTask(taskId);
        taskModifyUserWeekMapper.deleteByTask(taskId);
//        }
        return ZSYResult.success();
    }

    /**
     * 获取任务日志
     *
     * @param taskId
     * @return
     */
    @Override
    public PageInfo<TaskLogResDTO> getTaskLog(Long taskId, int pageNum) {
        PageHelper.startPage(pageNum, ZSYConstants.PAGE_SIZE);
        Page<TaskLog> taskLogs = taskLogMapper.selectPage(taskId);
        Page<TaskLogResDTO> page = new Page<>();
        BeanUtils.copyProperties(taskLogs, page);
        taskLogs.stream().forEach(taskLog -> {
            TaskLogResDTO taskLogResDTO = new TaskLogResDTO();
            BeanUtils.copyProperties(taskLog, taskLogResDTO);
            page.add(taskLogResDTO);
        });
        return new PageInfo<>(page);
    }

    /**
     * 获取阶段下的任务
     *
     * @param stageId
     * @return
     */
    @Override
    public List<TaskListResDTO> getTaskByStageId(Long stageId) {
        Stage stage = stageMapper.selectById(stageId);
        List<TaskListBO> taskListBOS = new ArrayList<>();
        if(stage.getName().equals("已发布")){
            taskListBOS = taskMapper.selectTaskByStageId(stageId,ZSYTokenRequestContext.get().getDepartmentId());
        }else{
            taskListBOS = taskMapper.selectTaskByEndStageId(stageId,ZSYTokenRequestContext.get().getDepartmentId());
        }
        List<TaskListResDTO> list = new ArrayList<>();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.stream().forEach(taskListBO -> {
            if(!(taskListBO.getStatus()==ZSYTaskStatus.FINISHED.getValue()&&stage.getName().equals("已发布"))){//隐藏看板模式中已完成发布的任务避免太长引起混乱
                TaskListResDTO taskListResDTO = new TaskListResDTO();
                BeanUtils.copyProperties(taskListBO, taskListResDTO, "tags");
                List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
                taskListBO.getTags().stream().forEach(tag -> {
                    TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
                    taskTagResDTO.setColor(tag.getColor());
                    taskTagResDTO.setName(tag.getName());
                    taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
                    taskTagResDTOS.add(taskTagResDTO);
                });
                if(stage.getName().indexOf("设计")!=-1 && taskListBO.getBeginTime()!=null){
                    taskListResDTO.setEndTime(taskListBO.getBeginTime());
                }else if(stage.getName().indexOf("开发")!=-1 && taskListBO.getTestTime()!=null){
                    taskListResDTO.setEndTime(taskListBO.getTestTime());
                }
                taskListResDTO.setTags(taskTagResDTOS);
                list.add(taskListResDTO);
            }

        });
        return list;
    }

    /**
     * 获取阶段下的任务根据发版时间
     *
     * @param stageId
     * @return
     */
    @Override
    public List<TaskListResDTO> getTaskByStageTime(Long stageId) {
        Stage stage = stageMapper.selectById(stageId);
        List<TaskListBO> taskListBOS = new ArrayList<>();
        if(stage.getName().equals("已发布")){
            taskListBOS = taskMapper.selectTaskByStageId(stageId,ZSYTokenRequestContext.get().getDepartmentId());
        }else{
            taskListBOS = taskMapper.selectTaskByEndStageId(stageId,ZSYTokenRequestContext.get().getDepartmentId());
        }
//        List<TaskListBO> taskListBOS = taskMapper.selectTaskByStageTime(stageId,ZSYTokenRequestContext.get().getDepartmentId(),publishInfoMapper.getPublishInfo(ZSYTokenRequestContext.get().getDepartmentId()));
        List<TaskListResDTO> list = new ArrayList<>();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.stream().forEach(taskListBO -> {
            if(!(taskListBO.getStatus()==ZSYTaskStatus.FINISHED.getValue()&&stageMapper.selectById(taskListBO.getStageId()).getName().equals("已发布"))){//隐藏看板模式中已完成发布的任务避免太长引起混乱
                TaskListResDTO taskListResDTO = new TaskListResDTO();
                BeanUtils.copyProperties(taskListBO, taskListResDTO, "tags");
                List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
                taskListBO.getTags().stream().forEach(tag -> {
                    TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
                    taskTagResDTO.setColor(tag.getColor());
                    taskTagResDTO.setName(tag.getName());
                    taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
                    taskTagResDTOS.add(taskTagResDTO);
                });
                taskListResDTO.setTags(taskTagResDTOS);
                list.add(taskListResDTO);
            }

        });
        return list;
    }

    /**
     * 移动任务
     *
     * @param taskMoveReqDTO
     * @return
     */
    @Override
    @Transactional
    public void moveTask(TaskMoveReqDTO taskMoveReqDTO) {
        if (taskMoveReqDTO.getOriginId() == null) {
            throw new ZSYServiceException("移动失败，任务不存在");
        }
        if (taskMoveReqDTO.getTargetId() == null && taskMoveReqDTO.getTargetStageId() == null) {
            throw new ZSYServiceException("移动失败，任务不存在");
        }
        // 原任务
        Task originTask = taskMapper.selectByPrimaryKey(taskMoveReqDTO.getOriginId());
        if (originTask == null) {
            throw new ZSYServiceException("移动失败，任务不存在");
        }
        // 移动到阶段底部
        if (taskMoveReqDTO.getTargetId() == null) {
            Integer lastIndex = taskMapper.selectLastIndexByStageId(taskMoveReqDTO.getTargetStageId());
            Task task = new Task();
            task.setId(taskMoveReqDTO.getOriginId());
            task.setStageId(taskMoveReqDTO.getTargetStageId());
            task.setSort(lastIndex == null ? 0 : lastIndex + 1);
            task.setUpdateTime(new Date());
            taskMapper.updateByPrimaryKeySelective(task);
            // sch --
            //阶段改变后,新增通知
            stageChange(originTask,originTask.getStageId(),taskMoveReqDTO.getTargetStageId());
            // -- sch
        } else {
            // 移动到阶段中间
            Task targetTask = taskMapper.selectByPrimaryKey(taskMoveReqDTO.getTargetId());
            if (targetTask == null) {
                throw new ZSYServiceException("移动失败，任务不存在");
            }
            // 当前阶段内移动
            if (targetTask.getStageId().equals(originTask.getStageId())) {
                // 交换位置
                // 原对象
                Task origin = new Task();
                origin.setId(taskMoveReqDTO.getOriginId());
                origin.setSort(targetTask.getSort());
                origin.setUpdateTime(new Date());
                taskMapper.updateByPrimaryKeySelective(origin);
                // 目标对象
                Task target = new Task();
                target.setId(targetTask.getId());
                target.setSort(originTask.getSort());
                target.setUpdateTime(new Date());
                taskMapper.updateByPrimaryKeySelective(target);

            }else{ // 插入到其他阶段中
                // 更新目标对象之后的下标+1
                taskMapper.updateIndexByStageId(targetTask.getStageId(), targetTask.getSort());
                // 更新原对象下标
                Task task = new Task();
                task.setId(taskMoveReqDTO.getOriginId());
                task.setStageId(targetTask.getStageId());
                task.setSort(targetTask.getSort());
                task.setUpdateTime(new Date());
                taskMapper.updateByPrimaryKeySelective(task);
                // sch --
                if (ZSYTaskStage.TESTING.getValue().equals(targetTask.getStageId())){
                    //移动到测试中阶段

                    //校验是否已经存在
                    WeekPublishPlan exist = weekPublishPlanMapper.selectByTaskId(task.getId());
                    if (exist == null){
                        WeekPublishPlan weekPublishPlan = new WeekPublishPlan();
                        weekPublishPlan.setId(snowFlakeIDHelper.nextId());
                        weekPublishPlan.setTaskId(task.getId());
                        weekPublishPlan.setCanOnline(0);
                        weekPublishPlan.setCondition("");

                        weekPublishPlanMapper.insert(weekPublishPlan);
                    }
                }
                //阶段改变后,新增通知
                stageChange(originTask,originTask.getStageId(),targetTask.getStageId());
                // -- sch
            }
        }
    }

    // sch --
    //阶段变化,新增通知
    public void stageChange(Task originTask,Long oldStageId,Long newStageId){
        Stage oldStage = stageMapper.selectById(oldStageId);
        Stage newStage = stageMapper.selectById(newStageId);
        //任务参与者,包括负责人和开发人员
        Set<Long> joiners = new HashSet<>();
        //1.添加负责人
        joiners.add(originTask.getCreateBy());
        //2.查询开发者,并添加
        List<Long> developers = taskUserMapper.selectUserByTaskId(originTask.getId());
        joiners.addAll(developers);
        List<Notification> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(joiners)){
            joiners.stream().forEach(userId -> {
                Notification notification = new Notification();
                notification.setNid(snowFlakeIDHelper.nextId());
                notification.setTaskId(originTask.getId());
                notification.setUserId(userId);
                String content = "您参与的任务: ( "+originTask.getName()+" )阶段有变化; "+oldStage.getName()+" --> "+newStage.getName();
                notification.setContent(content);
                notification.setCreateTime(new Date());
                notification.setStatus(0);
                list.add(notification);
            });
            if (notificationMapper.insertBatch(list) == 0){
                throw new ZSYServiceException("新增任务阶段变化通知失败");
            }

        }
    }

    //暂停或启动,新增通知
    public void pauseOrStart(Integer status,Long taskId,String desc){
        Task task = taskMapper.selectByPrimaryKey(taskId);
        //任务参与者,包括负责人和开发人员
        Set<Long> joiners = new HashSet<>();
        //1.添加负责人
        joiners.add(task.getCreateBy());
        //2.查询开发者,并添加
        List<Long> developers = taskUserMapper.selectUserByTaskId(taskId);
        joiners.addAll(developers);
        List<Notification> list = new ArrayList<>();
        if (status == ZSYTaskStatus.STOP.getValue()){
            joiners.stream().forEach(userId -> {
                Notification notification = new Notification();
                notification.setNid(snowFlakeIDHelper.nextId());
                notification.setTaskId(taskId);
                notification.setUserId(userId);
                String content = "您参与的任务: ( "+task.getName()+" )已暂停; 原因: "+ desc;
                notification.setContent(content);
                notification.setCreateTime(new Date());
                notification.setStatus(0);
                list.add(notification);
            });
            if (notificationMapper.insertBatch(list) == 0){
                throw new ZSYServiceException("新增暂停任务通知失败");
            }
        }else {
            joiners.stream().forEach(userId -> {
                Notification notification = new Notification();
                notification.setNid(snowFlakeIDHelper.nextId());
                notification.setTaskId(taskId);
                notification.setUserId(userId);
                String content = "您参与的任务: ( "+task.getName()+" )已启动;";
                notification.setContent(content);
                notification.setCreateTime(new Date());
                notification.setStatus(0);
                list.add(notification);
            });
            if (notificationMapper.insertBatch(list) == 0){
                throw new ZSYServiceException("新增启动任务通知失败");
            }
        }
    }

    //主任务完成,新增通知
    public void missionCompleted(Long taskId){
        Task task = taskMapper.selectByPrimaryKey(taskId);
        //任务参与者,包括负责人和开发人员
        Set<Long> joiners = new HashSet<>();
        //1.添加负责人
        joiners.add(task.getCreateBy());
        //2.查询开发者,并添加
        List<Long> developers = taskUserMapper.selectUserByTaskId(taskId);
        joiners.addAll(developers);
        List<Notification> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(joiners)){
            joiners.stream().forEach(userId -> {
                Notification notification = new Notification();
                notification.setNid(snowFlakeIDHelper.nextId());
                notification.setTaskId(taskId);
                notification.setUserId(userId);
                String content = "您参与的任务: ( "+task.getName()+" )已完成,请及时评价;";
                notification.setContent(content);
                notification.setCreateTime(new Date());
                notification.setStatus(0);
                list.add(notification);
            });
            if (notificationMapper.insertBatch(list) == 0){
                throw new ZSYServiceException("新增主任务完成通知失败");
            }
        }
    }

    //任务修改了,新增通知
    public void missionModified(TaskDetailBO taskTemp, TaskReqDTO taskReqDTO){
        //任务参与者,包括负责人和开发人员
        Set<Long> joiners = new HashSet<>();
        //1.添加负责人
        joiners.add(taskTemp.getCreateBy());
        //2.查询开发者,并添加
        List<Long> developers = taskUserMapper.selectUserByTaskId(taskTemp.getId());
        joiners.addAll(developers);
        List<Notification> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(joiners)){
            joiners.stream().forEach(userId -> {
                Notification notification = new Notification();
                notification.setNid(snowFlakeIDHelper.nextId());
                notification.setTaskId(taskTemp.getId());
                notification.setUserId(userId);
//                String content = "您参与的任务:"+taskTemp.getName()+"("+taskTemp.getId()+")已完成,请及时评价;";
                StringBuilder sb = new StringBuilder();
                sb.append("您参与的任务:( "+taskTemp.getName()+" )有修改: "+"\n\t");
                if (taskReqDTO.getTaskType() != null && taskReqDTO.getTaskType() != taskTemp.getType()){
                    String str = "类型由 ( "+ZSYTaskType.getName(taskTemp.getType())+" ) --> ( "+ZSYTaskType.getName(taskReqDTO.getTaskType())+" );\n";
                    sb.append(str);
                }
                if ((!Strings.isNullOrEmpty(taskReqDTO.getTaskName())) && (!taskReqDTO.getTaskName().equals(taskTemp.getName()))){
                    String str = "名称改为: ( "+taskReqDTO.getTaskName()+" );\n";
                    sb.append(str);
                }
                if((!Strings.isNullOrEmpty(taskReqDTO.getDescription())) && (!taskReqDTO.getDescription().equals(taskTemp.getDescription()))){
                    String str = "描述改为: ( "+taskReqDTO.getDescription()+" );\n";
                    sb.append(str);
                }
                if(taskReqDTO.getProjectId() != null && (!taskReqDTO.getProjectId().equals(taskTemp.getProjectId()))){
                    String str = "项目id改为: ["+taskReqDTO.getProjectId()+"];\n";
                    sb.append(str);
                }
                if(taskReqDTO.getStageId() != null && (!taskReqDTO.getStageId().equals(taskTemp.getStageId()))){
                    Stage oldStage = stageMapper.selectById(taskTemp.getStageId());
                    Stage newStage = stageMapper.selectById(taskReqDTO.getStageId());
                    String str = "阶段由( "+oldStage.getName()+" ) --> ( "+newStage.getName()+" );\n";
                    sb.append(str);
                }
                if (taskReqDTO.getPriority() != null && taskReqDTO.getPriority() != taskTemp.getPriority()){
                    String str = "优先级由( "+ZSYTaskPriority.getName(taskTemp.getPriority())+" ) --> ( "+ZSYTaskPriority.getName(taskReqDTO.getPriority())+" );\n";
                    sb.append(str);
                }
                if (taskReqDTO.getFacility() != null && taskReqDTO.getFacility() != taskTemp.getFacility()){
                    String str = "难易度由( "+ZSYTaskFacility.getName(taskTemp.getFacility())+" ) --> ( "+ZSYTaskFacility.getName(taskReqDTO.getFacility())+" );\n";
                    sb.append(str);
                }
                if (taskReqDTO.getBeginTime() != null && (taskReqDTO.getBeginTime().compareTo(Optional.ofNullable(taskTemp.getBeginTime()).orElse(new Date())) != 0)){
//                    String oldTime = DateHelper.dateFormatter(taskTemp.getBeginTime(), DateHelper.DATE_FORMAT);
                    String newTime = DateHelper.dateFormatter(taskReqDTO.getBeginTime(), DateHelper.DATE_FORMAT);
                    String str = "开始开发时间改成: "+ newTime + ";\n";
                    sb.append(str);
                }
                if (taskReqDTO.getTestTime() != null && (taskReqDTO.getTestTime().compareTo(Optional.ofNullable(taskTemp.getTestTime()).orElse(new Date())) != 0)){
                    String newTime = DateHelper.dateFormatter(taskReqDTO.getTestTime(), DateHelper.DATE_FORMAT);
                    String str = "提测时间改成: "+ newTime + ";\n";
                    sb.append(str);
                }
                if (taskReqDTO.getEndTime() != null && (taskReqDTO.getEndTime().compareTo(Optional.ofNullable(taskTemp.getEndTime()).orElse(new Date())) != 0)){
                    String newTime = DateHelper.dateFormatter(taskReqDTO.getEndTime(), DateHelper.DATE_FORMAT);
                    String str = "截止时间改成: "+ newTime + ";\n";
                    sb.append(str);
                }
                if (taskReqDTO.getCreateBy() != null && (!taskReqDTO.getCreateBy().equals(taskTemp.getCreateBy()))){
                    User oldMan = userMapper.selectById(taskTemp.getCreateBy());
                    User newMan = userMapper.selectById(taskReqDTO.getCreateBy());
                    String str  = "负责人由( "+oldMan.getName() + " ) --> ( " + newMan.getName() + " );\n";
                    sb.append(str);
                }
                if ((!CollectionUtils.isEmpty(taskReqDTO.getTaskUsers()))){
                    //当没有任务用户时
                    List<String> userNames = new ArrayList<>();
                    if (CollectionUtils.isEmpty(taskTemp.getTaskUsers())){
                        List<Long> taskUsers = taskReqDTO.getTaskUsers().stream().map(TaskUserReqDTO::getUserId).collect(Collectors.toList());
                        taskUsers.stream().forEach(userId1 -> {
                            User user = userMapper.selectById(userId1);
                            userNames.add(user.getName());
                        });
                        String str = "新增任务人员: " + userNames.toString() + ";\n";
                        sb.append(str);
                    }else if (taskReqDTO.getTaskUsers().size() > taskTemp.getTaskUsers().size()){
                        Set<Long> taskUsers = new HashSet<>();
                        taskTemp.getTaskUsers().stream().forEach(taskUserBO -> {
                            //过滤出相同的taskUser
                            List<TaskUserReqDTO> taskUserReqDTOS = taskReqDTO.getTaskUsers().stream().filter(taskUserReqDTO -> (taskUserReqDTO.getUserId().equals(taskUserBO.getUserId()))).collect(Collectors.toList());
                            List<Long> userIds = taskUserReqDTOS.stream().map(TaskUserReqDTO::getUserId).collect(Collectors.toList());
                            taskUsers.addAll(userIds);
                        });
                        List<Long> addUserIds = taskReqDTO.getTaskUsers().stream().map(TaskUserReqDTO::getUserId).collect(Collectors.toList());
                        addUserIds.removeAll(taskUsers);
                        addUserIds.stream().forEach(userId2 -> {
                            User user = userMapper.selectById(userId2);
                            userNames.add(user.getName());
                        });
                        String str = "新增任务人员: " + userNames.toString() + ";\n";
                        sb.append(str);
                    }else if (taskReqDTO.getTaskUsers().size() < taskTemp.getTaskUsers().size()){
                        Set<Long> removeUsers = new HashSet<>();
                        taskReqDTO.getTaskUsers().stream().forEach(taskUserReqDTO -> {
                            //获取相同的部分
                            List<TaskUserBO> taskUserBOS = taskTemp.getTaskUsers().stream().filter(taskUserBO -> (taskUserBO.getUserId().equals(taskUserReqDTO.getUserId()))).collect(Collectors.toList());
                            List<Long> userIds = taskUserBOS.stream().map(TaskUser::getUserId).collect(Collectors.toList());
                            removeUsers.addAll(userIds);
                        });
                        List<Long> reduceUserIds = taskTemp.getTaskUsers().stream().map(TaskUser::getUserId).collect(Collectors.toList());
                        reduceUserIds.removeAll(removeUsers);
                        reduceUserIds.stream().forEach(userId3 -> {
                            User user = userMapper.selectById(userId3);
                            userNames.add(user.getName());
                        });
                        String str = "移除任务人员: " + userNames.toString() + ";\n";
                        sb.append(str);
                    }
                }
                notification.setContent(sb.toString());
                notification.setCreateTime(new Date());
                notification.setStatus(0);
                list.add(notification);
            });
            if (notificationMapper.insertBatch(list) == 0){
                throw new ZSYServiceException("新增任务修改通知失败");
            }
        }
    }

    //多人任务完成子任务,新增通知
    public void sonMissionCompleted(TaskCompleteReqDTO taskCompleteReqDTO){
        Task task = taskMapper.selectByPrimaryKey(taskCompleteReqDTO.getTaskId());
        //任务参与者,包括负责人和开发人员
        Set<Long> joiners = new HashSet<>();
        //1.添加负责人
        joiners.add(task.getCreateBy());
        //2.查询开发者,并添加
        List<Long> developers = taskUserMapper.selectUserByTaskId(taskCompleteReqDTO.getTaskId());
        joiners.addAll(developers);
        List<Notification> list = new ArrayList<>();
        User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        String time = DateHelper.dateFormatter(taskCompleteReqDTO.getCompleteTime(), DateHelper.DATETIME_FORMAT);
        if (!CollectionUtils.isEmpty(joiners)){
            joiners.stream().forEach(userId -> {
                Notification notification = new Notification();
                notification.setNid(snowFlakeIDHelper.nextId());
                notification.setTaskId(taskCompleteReqDTO.getTaskId());
                notification.setUserId(userId);
                String content = "您参与的任务:( "+task.getName()+" )有变化; " + user.getName() + " 完成了子任务!完成时间为: " + time;
                notification.setContent(content);
                notification.setCreateTime(new Date());
                notification.setStatus(0);
                list.add(notification);
            });
            if (notificationMapper.insertBatch(list) == 0){
                throw new ZSYServiceException("新增完成子任务通知失败");
            }
        }
    }

    /**
     * 查询最近5条未读通知
     * @return
     */
    @Override
    public List<NoticeResDTO> getUnreadNotification() {
        List<Notification> notifications = notificationMapper.selectUnReadNotice(ZSYTokenRequestContext.get().getUserId());
        List<NoticeResDTO> noticeResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(notifications)){
            BeanUtils.copyProperties(notifications,noticeResDTOList);
            notifications.stream().forEach(notification -> {
                NoticeResDTO noticeResDTO = new NoticeResDTO();
                BeanUtils.copyProperties(notification,noticeResDTO);
                noticeResDTOList.add(noticeResDTO);
            });
        }
        return noticeResDTOList;
    }

    /**
     * 查询所有通知
     * @return
     */
    @Override
    public PageInfo<NoticeResDTO> getAllNotifications(NoticeReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<Notification> notifications = notificationMapper.selectAllNotice(ZSYTokenRequestContext.get().getUserId(),reqDTO);
        Page<NoticeResDTO> noticeResDTOList = new Page<>();
        if (!CollectionUtils.isEmpty(notifications)){
            BeanUtils.copyProperties(notifications,noticeResDTOList);
            notifications.stream().forEach(notification -> {
                NoticeResDTO noticeResDTO = new NoticeResDTO();
                BeanUtils.copyProperties(notification,noticeResDTO);
                noticeResDTOList.add(noticeResDTO);
            });
        }
        return new PageInfo<>(noticeResDTOList);
    }

    /**
     * 查询所有未读通知条数
     * @return
     */
    @Override
    public UnreadNoticeNumResDTO getUnreadNoticeNum() {
        Integer count = notificationMapper.selectUnreadNoticeNum(ZSYTokenRequestContext.get().getUserId());
        UnreadNoticeNumResDTO unreadNoticeNumResDTO = new UnreadNoticeNumResDTO();
        unreadNoticeNumResDTO.setCount(count);
        return unreadNoticeNumResDTO;
    }

    /**
     * 读取通知
     */
    @Override
    @Transactional
    public void readNotice(Long nid) {
        if (notificationMapper.updateNoticeById(nid,new Date()) == 0){
            throw new ZSYServiceException("读取通知失败");
        }
    }

    /**
     * 标记全部通知已读
     */
    @Override
    @Transactional
    public void readAll() {
        if(notificationMapper.updateNoticeByUser(ZSYTokenRequestContext.get().getUserId(),new Date()) == 0){

        }
    }

    @Override
    public List<TaskListResDTO> getMyTaskByStage(Long stageId) {
        Stage stage = stageMapper.selectById(stageId);
        List<TaskListBO> taskListBOS = new ArrayList<>();
        if(stage.getName().equals("已发布")){
            taskListBOS = taskMapper.selectMyTaskByStageId(stageId,ZSYTokenRequestContext.get().getDepartmentId(),ZSYTokenRequestContext.get().getUserId());
        }else{
            taskListBOS = taskMapper.selectMyTaskByEndStageId(stageId,ZSYTokenRequestContext.get().getDepartmentId(),ZSYTokenRequestContext.get().getUserId());
        }
        List<TaskListResDTO> list = new ArrayList<>();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.stream().forEach(taskListBO -> {
            if(!(taskListBO.getStatus()==ZSYTaskStatus.FINISHED.getValue()&&stage.getName().equals("已发布"))){//隐藏看板模式中已完成发布的任务避免太长引起混乱
                TaskListResDTO taskListResDTO = new TaskListResDTO();
                BeanUtils.copyProperties(taskListBO, taskListResDTO, "tags");
                List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
                taskListBO.getTags().stream().forEach(tag -> {
                    TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
                    taskTagResDTO.setColor(tag.getColor());
                    taskTagResDTO.setName(tag.getName());
                    taskTagResDTO.setColorValue(ZSYTagColor.getName(Integer.parseInt(tag.getColor())));
                    taskTagResDTOS.add(taskTagResDTO);
                });
                if(stage.getName().indexOf("设计")!=-1 && taskListBO.getBeginTime()!=null){
                    taskListResDTO.setEndTime(taskListBO.getBeginTime());
                }else if(stage.getName().indexOf("开发")!=-1 && taskListBO.getTestTime()!=null){
                    taskListResDTO.setEndTime(taskListBO.getTestTime());
                }
                taskListResDTO.setTags(taskTagResDTOS);
                list.add(taskListResDTO);
            }

        });
        return list;
    }

    /**
     * 所有进行中的多人任务
     * @return
     */
    @Override
    public List<TaskBaseResDTO> getAllMultipleTask() {
        List<Task> tasks = taskMapper.selectAllTaskBase();
        List<TaskBaseResDTO> taskBaseResDTOList = new ArrayList<>();
        BeanUtils.copyProperties(tasks,taskBaseResDTOList);
        if (!CollectionUtils.isEmpty(tasks)){
            tasks.stream().forEach(task->{
                TaskBaseResDTO taskBaseResDTO = new TaskBaseResDTO();
                BeanUtils.copyProperties(task,taskBaseResDTO);
                taskBaseResDTOList.add(taskBaseResDTO);
            });
        }
        return taskBaseResDTOList;
    }

    /**
     * 根据任务id和用户id查询taskUser
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    @Override
    public TaskUserBaseInfoResDTO getTaskUserByTaskAndUsr(Long taskId, Long userId) {
        TaskUser taskUser = taskUserMapper.selectByTaskAndUser(taskId,userId);
        User user = userMapper.selectById(userId);
        TaskUserBaseInfoResDTO resDTO = new TaskUserBaseInfoResDTO();
        resDTO.setId(taskUser.getId());
        resDTO.setTaskId(taskUser.getTaskId());
        resDTO.setUserId(taskUser.getUserId());
        resDTO.setUserName(user.getName());
        resDTO.setBeginTime(taskUser.getBeginTime());
        resDTO.setEndTime(taskUser.getEndTime());
        resDTO.setTaskHours(taskUser.getTaskHours());
        resDTO.setDescription(taskUser.getDescription());
        List<UserWeek> userWeeks = userWeekMaper.selectByTaskAndUser(taskId,userId);
        List<UserWeekResDTO> userWeekResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userWeeks)){
            for (UserWeek userWeek : userWeeks) {
                UserWeekResDTO userWeekResDTO = new UserWeekResDTO();
                userWeekResDTO.setHours(userWeek.getHours());
                userWeekResDTO.setWeekNumber(userWeek.getWeekNumber());
                userWeekResDTO.setYear(userWeek.getYear());
                userWeekResDTOList.add(userWeekResDTO);
            }
            userWeekResDTOList = userWeekResDTOList.stream().sorted(Comparator.comparing(UserWeekResDTO::getWeekNumber)).collect(Collectors.toList());
        }
        resDTO.setUserWeeks(userWeekResDTOList);
        return resDTO;
    }

    /**
     * 检查是否有主任务超时,有的话,新增通知并短信通知负责人
     * @return
     */
    @Override
    @Transactional
    public void noticeDelayMasterTaskPrincipal() {
        //查询审核通过,进行中的任务
        List<TaskListBO> taskListBOS = taskMapper.selectTaskList();
        //有超时的任务集合
        List<TaskListBO> delayTaskList = Lists.newArrayList();
        taskListBOS.stream().forEach(taskListBO -> {
            //设计阶段任务
            if (taskListBO.getStageId().equals(212754785051344891L) || taskListBO.getStageId().equals(212754785051344892L)){
                if ((taskListBO.getBeginTime() != null) && (taskListBO.getBeginTime().compareTo(new Date()) < 0) || taskListBO.getEndTime().compareTo(new Date()) < 0){
                    delayTaskList.add(taskListBO);
                }
            }
            //开发阶段任务
            else if (taskListBO.getStageId().equals(212754785051344894L) || taskListBO.getStageId().equals(212754785051344890L)){
                if ((taskListBO.getTestTime() != null) && (taskListBO.getTestTime().compareTo(new Date()) < 0) || taskListBO.getEndTime().compareTo(new Date()) < 0){
                    delayTaskList.add(taskListBO);
                }
            }else {
                if ((taskListBO.getEndTime() != null) && (taskListBO.getEndTime().compareTo(new Date()) < 0)){
                    delayTaskList.add(taskListBO);
                }
            }
        });
        //因短信接口一分钟只可以给一个手机发一次短信,当一个负责人有多个超时任务,将任务拼接起来
        Map<Long,String> taskMap = new HashMap<>();
        delayTaskList.stream().forEach(delayTask -> {
            //根据taskid查询超时人员信息
            User user = userMapper.selectById(delayTask.getCreateBy());
            //要发信息的人员
            List<Long> messageUsers = taskMap.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            if (messageUsers.contains(delayTask.getCreateBy())){
                taskMap.put(delayTask.getCreateBy(),taskMap.get(delayTask.getCreateBy())+","+delayTask.getName());
            }else {
                taskMap.put(delayTask.getCreateBy(),delayTask.getName());
            }
            //新增通知
            Notification notification = new Notification();
            notification.setNid(snowFlakeIDHelper.nextId());
            notification.setTaskId(delayTask.getId());
            notification.setUserId(delayTask.getCreateBy());
            String content = "您负责的任务 ("+delayTask.getName()+") 已超时,请及时确认;";
            notification.setContent(content);
            notification.setCreateTime(new Date());
            notification.setStatus(0);
            if (notificationMapper.insertNotice(notification) == 0){
                throw new ZSYServiceException("新增任务超时通知失败");
            }
        });
        if ("nothing".equals(smsConfig.getApi())){
            System.out.println(smsConfig.getApi());
        }else {
            taskMap.entrySet().stream().forEach(longStringEntry -> {
                User user = userMapper.selectById(longStringEntry.getKey());
                if (user.getStatus() == 0 && user.getIsDelete() == 0){
                    List<String> taskNames = Arrays.asList(longStringEntry.getValue().replaceAll("&", ";").split(","));
                    taskNames = taskNames.stream().map(taskName -> "<"+taskName+">").collect(Collectors.toList());
                    String templateJson = "{\"taskName\":\""+taskNames.toString()+"\"}";
                    logger.info("主任务超时,发短信通知负责人");
                    String message = sendMessage(user.getPhone(), smsConfig.getSmsTemplateOne(), templateJson);
                    String code = Arrays.asList(Arrays.asList(message.split(",")).get(1).split(":")).get(1).replaceAll("\"", "");
                    String errorMessage = Arrays.asList(Arrays.asList(message.split(",")).get(2).split(":")).get(1).replaceAll("\"", "");
                    if ("00".equals(code)){
                        logger.info("短信发送成功");
                    }else {
                        logger.info("短信发送失败: "+errorMessage);
                    }
                }
            });
        }
    }

    /**
     * 发短信
     * @param phone
     * @param templateId
     * @param templateJson
     */
    private String sendMessage(String phone,String templateId,String templateJson){
        List<ZSYOKHttpHelper.OkHttpParam> params = new ArrayList<>();
        params.add(new ZSYOKHttpHelper.OkHttpParam("mobile", phone));
        params.add(new ZSYOKHttpHelper.OkHttpParam("appId", smsConfig.getAppId()));
        String timestamp = String.valueOf(System.currentTimeMillis());
        params.add(new ZSYOKHttpHelper.OkHttpParam("timestamp", timestamp));
        params.add(new ZSYOKHttpHelper.OkHttpParam("sign", MD5Helper.convert(smsConfig.getAppId() + smsConfig.getAppSecret() + timestamp, 32, false)));
        params.add(new ZSYOKHttpHelper.OkHttpParam("templateId", templateId));
        params.add(new ZSYOKHttpHelper.OkHttpParam("templateJson", templateJson));
        String message = ZSYOKHttpHelper.post(ZSYOKHttpHelper.componentUrl(smsConfig.getApi(), params));
        return message;
    }

    /**
     * 9点定时检查是否有子任务超时,有的话,新增通知并短信通知负责人
     */
    @Override
    @Transactional
    public void noticeDelaySonTaskPrincipal() {
        //查询审核通过,进行中的子任务
        List<TaskBO> taskBOS = taskMapper.selectSonTaskList();
        Map<Long,String> taskMap = new HashMap<>();
        taskBOS.stream().forEach(taskBO -> {
            if (!CollectionUtils.isEmpty(taskBO.getTaskUsers())){
                taskBO.getTaskUsers().stream().forEach(taskUser -> {
                    if (taskUser.getEndTime().compareTo(new Date()) < 0){
                        //负责人
                        User user = userMapper.selectByTaskId(taskBO.getId());
                        //超时人员
                        User delayUser = userMapper.selectById(taskUser.getUserId());
                        //要发信息的人员
                        List<Long> messageUsers = taskMap.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
                        if (messageUsers.contains(taskBO.getCreateBy())){
                            taskMap.put(taskBO.getCreateBy(),taskMap.get(taskBO.getCreateBy())+","+taskBO.getName()+"="+delayUser.getName());
                        }else {
                            taskMap.put(taskBO.getCreateBy(),taskBO.getName()+"="+delayUser.getName());
                        }
                        Notification notification = new Notification();
                        notification.setNid(snowFlakeIDHelper.nextId());
                        notification.setTaskId(taskBO.getId());
                        notification.setUserId(user.getId());
                        String content = "您负责的任务 ("+taskBO.getName()+") 有超时子任务,请及时确认;超时人员: " + delayUser.getName();
                        notification.setContent(content);
                        notification.setCreateTime(new Date());
                        notification.setStatus(0);
                        if (notificationMapper.insertNotice(notification) == 0){
                            throw new ZSYServiceException("新增任务超时通知失败");
                        }
                    }
                });
            }
        });
        if ("nothing".equals(smsConfig.getApi())){
            System.out.println(smsConfig.getApi());
        }else {
            taskMap.entrySet().stream().forEach(entrySet ->{
                User user = userMapper.selectById(entrySet.getKey());
                if (user.getIsDelete() == 0 && user.getStatus() == 0){
                    //获取超时任务和人员的集合
                    List<String> taskUsers = Arrays.asList(entrySet.getValue().split(","));
                    //超时任务集合
                    List<String> delayTasks = new ArrayList<>();
                    //超时人员集合
                    List<String> delayUsers = new ArrayList<>();
                    taskUsers.stream().forEach(taskUser ->{
                        String taskName = "<"+Arrays.asList(taskUser.split("=")).get(0)+">";
                        String userName = "<"+Arrays.asList(taskUser.split("=")).get(1)+">";
                        delayTasks.add(taskName);
                        delayUsers.add(userName);
                    });
                    String taskNames = delayTasks.toString().replaceAll("&", ";");
                    String templateJson = "{\"taskName\":\""+taskNames+ "\",\"timeOutUsers\":\""+delayUsers.toString()+"\"}";
                    logger.info("子任务超时,发短信通知任务负责人");
                    String message = sendMessage(user.getPhone(), smsConfig.getSmsTemplateTwo(), templateJson);
                    String code = Arrays.asList(Arrays.asList(message.split(",")).get(1).split(":")).get(1).replaceAll("\"", "");
                    String errorMessage = Arrays.asList(Arrays.asList(message.split(",")).get(2).split(":")).get(1).replaceAll("\"", "");
                    if ("00".equals(code)){
                        logger.info("短信发送成功");
                    }else {
                        logger.info("短信发送失败: "+errorMessage);
                    }
                }
            });
        }
    }

    /**
     * 9点定时检查是否有子任务超时,有的话,新增通知并短信通知当前子任务负责人
     */
    @Override
    public void noticeDelaySonTaskChargeMan() {
        //查询审核通过,进行中的子任务
        List<TaskBO> taskBOS = taskMapper.selectSonTaskList();
        Map<Long,String> taskMap = new HashMap<>();
        taskBOS.stream().forEach(taskBO -> {
            if (!CollectionUtils.isEmpty(taskBO.getTaskUsers())){
                taskBO.getTaskUsers().stream().forEach(taskUser -> {
                    if (taskUser.getEndTime().compareTo(new Date()) < 0){
                        //超时人员
                        User delayUser = userMapper.selectById(taskUser.getUserId());
                        //要发信息的人员
                        List<Long> messageUsers = taskMap.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
                        if (messageUsers.contains(delayUser.getId())){
                            taskMap.put(delayUser.getId(),taskMap.get(delayUser.getId())+","+taskBO.getName());
                        }else {
                            taskMap.put(delayUser.getId(),taskBO.getName());
                        }
                        //新增通知
                        Notification notification = new Notification();
                        notification.setNid(snowFlakeIDHelper.nextId());
                        notification.setTaskId(taskBO.getId());
                        notification.setUserId(delayUser.getId());
                        String content = "("+taskBO.getName()+") 任务已超时,请及时确认!!! 如需延期，请及时联系任务负责人！";
                        notification.setContent(content);
                        notification.setCreateTime(new Date());
                        notification.setStatus(0);
                        if (notificationMapper.insertNotice(notification) == 0){
                            throw new ZSYServiceException("新增任务超时通知失败");
                        }
                    }
                });
            }
        });
        if ("nothing".equals(smsConfig.getApi())){
            System.out.println(smsConfig.getApi());
        }else {
            taskMap.entrySet().stream().forEach(entrySet ->{
                User user = userMapper.selectById(entrySet.getKey());
                if (user.getStatus() == 0 && user.getIsDelete() == 0){
                    List<String> taskNames = Arrays.asList(entrySet.getValue().replaceAll("&", ";").split(","));
                    taskNames = taskNames.stream().map(taskName -> "<" + taskName + ">").collect(Collectors.toList());
                    String templateJson = "{\"taskName\":\""+taskNames.toString()+"\"}";
                    logger.info("子任务超时,发短信通知超时人员");
                    String message = sendMessage(user.getPhone(), smsConfig.getSmsTemplateThree(), templateJson);
                    String code = Arrays.asList(Arrays.asList(message.split(",")).get(1).split(":")).get(1).replaceAll("\"", "");
                    String errorMessage = Arrays.asList(Arrays.asList(message.split(",")).get(2).split(":")).get(1).replaceAll("\"", "");
                    if ("00".equals(code)){
                        logger.info("短信发送成功");
                    }else {
                        logger.info("短信发送失败: "+errorMessage);
                    }
                    System.out.println(templateJson);
                }
            });
        }
    }

    @Override
    public PageInfo<NoticeResDTO> getEveryoneNotice(NoticeReqDTO reqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<NotificationBO> notifications = notificationMapper.selectEveryoneNotice(reqDTO);
        Page<NoticeResDTO> noticeResDTOList = new Page<>();
        if (!CollectionUtils.isEmpty(notifications)){
            BeanUtils.copyProperties(notifications,noticeResDTOList);
            notifications.stream().forEach(notification -> {
                NoticeResDTO noticeResDTO = new NoticeResDTO();
                BeanUtils.copyProperties(notification,noticeResDTO);
                noticeResDTOList.add(noticeResDTO);
            });
        }
        return new PageInfo<>(noticeResDTOList);
    }
    // -- sch

    /**
     * 修改评审状态
     * @param taskId
     */
    @Override
    @Transactional
    public void examineTask(Long taskId,Integer examine){
        checkUser();
        Task task = new Task();
        task.setId(taskId);
        task.setExamine(examine);
        if(taskMapper.updateByPrimaryKeySelective(task)==0){
            throw new ZSYServiceException("任务不存在，请检查");
        }
    }

    /**
     * 修改评审状态
     * @param taskId
     */
    @Override
    @Transactional
    public void stopTask(Long taskId,Integer status,String desc){
        checkUser();
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(status);
        if(taskMapper.updateByPrimaryKeySelective(task)==0){
            throw new ZSYServiceException("任务不存在，请检查");
        }
        // 插入日志
        if(status ==ZSYTaskStatus.STOP.getValue()){
            taskLogMapper.insert(buildLog(ZSYTokenRequestContext.get().getUserName() + "暂停了任务", desc, taskId));
        }
        // sch --
        //暂停或启动,新增通知
        pauseOrStart(status,taskId,desc);
        //-- sch
    }

    /**
     * 设置发版时间
     * @param publishTime
     */
    @Override
    @Transactional
    public void setPublishTime(Date publishTime){
        checkUser();
        if(publishInfoMapper.getPublishInfo(ZSYTokenRequestContext.get().getDepartmentId())==null){
            publishInfoMapper.insertPublishInfo(publishTime,ZSYTokenRequestContext.get().getDepartmentId());
        }else {
            publishInfoMapper.updatePublishInfo(publishTime,ZSYTokenRequestContext.get().getDepartmentId());
        }

    }

    /**
     * 获取发版时间
     */
    @Override
    @Transactional
    public Date getPublishTime(){
        checkUser();
        if(publishInfoMapper.getPublishInfo(ZSYTokenRequestContext.get().getDepartmentId())==null){
            return DateHelper.TimestampToDate(ZSYConstants.PUBLISHTIME);
        }else{
            return  publishInfoMapper.getPublishInfo(ZSYTokenRequestContext.get().getDepartmentId());
        }
    }

    /**
     * 查询未关联计划任务
     * @return
     */
    @Override
    public List<TaskPlanResDTO> getPlanTask() {
        List<Task> task = taskMapper.getTaskPlanList();
        List<TaskPlanResDTO> planResDTOS = new ArrayList<>();
        task.stream().forEach(task1 -> {
            TaskPlanResDTO planResDTO = new TaskPlanResDTO();
            BeanUtils.copyProperties(task1,planResDTO);
            planResDTOS.add(planResDTO);
        });
        return planResDTOS;
    }

    @Override
    public void setTestingTask(TestingTaskReqDTO testingTask) {
        TaskDetailBO bo = taskMapper.selectTaskDetailByTaskId(testingTask.getTaskId());
        List<TaskTest> taskTests = Lists.newArrayList();

        List<UserWeek> userWeeks = Lists.newArrayList();

        bo.getTaskUsers().forEach(taskUserBO -> {
            User user = userMapper.selectById(taskUserBO.getUserId());
            if(user.getJobRole() == ZSYJobRole.PROGRAMER.getValue()){
                TaskTest taskTest = new TaskTest();
                taskTest.setTtId(snowFlakeIDHelper.nextId());
                taskTest.setTaskId(testingTask.getTaskId());
                taskTest.setUserId(user.getId());
                taskTest.setPercent(testingTask.getPercent());
                taskTest.setBeginTime(testingTask.getBeginTime());
                taskTest.setEndTime(testingTask.getEndTime());
                taskTests.add(taskTest);

                testingTask.getWeeks().forEach(userWeekReqDTO->{
                    UserWeek week = new UserWeek();
                    week.setId(snowFlakeIDHelper.nextId());
                    week.setWeekNumber(userWeekReqDTO.getWeekNumber());
                    week.setTaskId(taskTest.getTtId());
                    week.setUserId(user.getId());
                    week.setYear(userWeekReqDTO.getYear());

                    double hor=(taskUserBO.getTaskHours().doubleValue()*userWeekReqDTO.getHours()*0.01)*(testingTask.getPercent()*0.01);
                    double value = new BigDecimal(hor).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
                    week.setHours(value);
                    userWeeks.add(week);
                });
            }

            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setId(snowFlakeIDHelper.nextId());
            userIntegral.setTaskId(taskUserBO.getTaskId());
            userIntegral.setUserId(taskUserBO.getUserId());
            double integral = new BigDecimal(taskUserBO.getTaskHours()*testingTask.getPercent()*0.01).setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
            userIntegral.setIntegral(BigDecimal.valueOf(integral));
            userIntegral.setOrigin(ZSYIntegralOrigin.BUG.getValue());
            userIntegral.setDescription("新增开发Bug修复时间：" + bo.getName());
            userIntegral.setCreateTime(new Date());
            userIntegralMapper.insert(userIntegral);

            // 修改用户积分
            User userTemp = userMapper.selectById(taskUserBO.getUserId());
            BigDecimal currentIntegral = userTemp.getIntegral();
            User userNow = new User();
            userNow.setId(taskUserBO.getUserId());
            userNow.setIntegral(currentIntegral.add(BigDecimal.valueOf(integral)));
            userMapper.updateSelectiveById(user);
        });

        if(CollectionUtils.isEmpty(userWeeks)){
            throw new ZSYServiceException("没有开发任务，请检查");
        }

        userWeekMaper.insertList(userWeeks);
        taskTestMapper.insertList(taskTests);

        //新增一条任务日志
        TaskLog taskLog = new TaskLog();
        taskLog.setId(snowFlakeIDHelper.nextId());
        taskLog.setTaskId(bo.getId());
        String title = ZSYTokenRequestContext.get().getUserName()+"修改了任务";
        taskLog.setTitle(title);
        String content = "添加开发Bug修复时间";
        taskLog.setContent(content);
        taskLog.setCreateTime(new Date());
        taskLog.setUserId(ZSYTokenRequestContext.get().getUserId());
        taskLog.setUserName(ZSYTokenRequestContext.get().getUserName());
        taskLogMapper.insert(taskLog);

    }
}
