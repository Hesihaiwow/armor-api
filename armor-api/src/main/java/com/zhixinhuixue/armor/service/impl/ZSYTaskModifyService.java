package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.TaskModifyBO;
import com.zhixinhuixue.armor.model.bo.TaskModifyDetailBO;
import com.zhixinhuixue.armor.model.bo.TaskModifyFunctionBO;
import com.zhixinhuixue.armor.model.bo.TaskTempFunctionBO;
import com.zhixinhuixue.armor.model.dto.request.AddTaskModifyReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskModifyReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskTempFunctionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserWeekReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskModifyService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.FunctionAction;
import com.zhixinhuixue.armor.source.enums.TaskLevel;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
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
 * @DATE 2019/5/9 10:34
 */
@Service
public class ZSYTaskModifyService implements IZSYTaskModifyService {

    @Autowired
    private IZSYTaskModifyMapper taskModifyMapper;

    @Autowired
    private IZSYTaskModifyUserWeekMapper taskModifyUserWeekMapper;

    @Autowired
    private IZSYTaskMapper taskMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    @Autowired
    private IZSYUserWeekMapper userWeekMapper;

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private IZSYTaskLogMapper logMapper;

    @Autowired
    private IZSYTaskUserMapper taskUserMapper;

    @Autowired
    private IZSYNotificationMapper notificationMapper;

    @Autowired
    private IZSYTaskModifyFunctionMapper taskModifyFuntionMapper;

    @Autowired
    private IZSYTaskTempMapper taskTempMapper;

    @Autowired
    private IZSYTaskTempFunctionMapper taskTempFunctionMapper;

    /**
     * 新增任务修改申请
     * @author sch
     * @param addTaskModifyReqDTO
     */
    @Override
    @Transactional
    public void add(AddTaskModifyReqDTO addTaskModifyReqDTO) {
        //校验任务是否存在
        Long taskId = addTaskModifyReqDTO.getTaskId();
        Long userId = addTaskModifyReqDTO.getUserId();
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task == null){
            throw new ZSYServiceException("当前任务不存在");
        }
        if (task.getStatus() > 1){
            throw new ZSYServiceException("当前任务已完成,无法修改");
        }

        List<UserWeekReqDTO> userWeeks = addTaskModifyReqDTO.getUserWeeks();
        if (CollectionUtils.isEmpty(userWeeks)){
            throw new ZSYServiceException("周工时为空,请检查");
        }else {
            TaskModify taskModify = new TaskModify();
            taskModify.setId(snowFlakeIDHelper.nextId());
            taskModify.setBeginTime(addTaskModifyReqDTO.getBeginTime());
            taskModify.setEndTime(addTaskModifyReqDTO.getEndTime());
            taskModify.setCreateTime(new Date());
            taskModify.setWorkHours(addTaskModifyReqDTO.getWorkHours());
            taskModify.setReason(addTaskModifyReqDTO.getReason());
            taskModify.setDescription(addTaskModifyReqDTO.getDescription());
            taskModify.setType(2);
            taskModify.setStatus(1);
            taskModify.setReviewStatus(1);
            taskModify.setTaskId(taskId);
            taskModify.setUserId(userId);
            taskModify.setTaskLevel(addTaskModifyReqDTO.getTaskLevel());

            List<TaskTempFunctionReqDTO> taskModifyFunctionList = addTaskModifyReqDTO.getTaskModifyFunctionList();
            List<TaskModifyFunction> functionList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(taskModifyFunctionList)){
                taskModifyFunctionList.forEach(taskTempFunctionReqDTO -> {
                    TaskModifyFunction function = new TaskModifyFunction();
                    BeanUtils.copyProperties(taskTempFunctionReqDTO,function);
                    function.setId(snowFlakeIDHelper.nextId());
                    function.setTmId(taskModify.getId());
                    functionList.add(function);
                });
                taskModifyFuntionMapper.insertBatch(functionList);
            }

            List<TaskModifyUserWeek> taskModifyUserWeekList = new ArrayList<>();
            userWeeks.stream().forEach(userWeekReqDTO -> {
                TaskModifyUserWeek taskModifyUserWeek = new TaskModifyUserWeek();
                taskModifyUserWeek.setId(snowFlakeIDHelper.nextId());
                taskModifyUserWeek.setCreateTime(new Date());
                taskModifyUserWeek.setHours(BigDecimal.valueOf(userWeekReqDTO.getHours()));
                taskModifyUserWeek.setStatus(1);
                taskModifyUserWeek.setTmId(taskModify.getId());
                taskModifyUserWeek.setTaskId(taskId);
                taskModifyUserWeek.setUserId(userId);
                taskModifyUserWeek.setWeekNumber(userWeekReqDTO.getWeekNumber());
                taskModifyUserWeek.setYear(userWeekReqDTO.getYear());
                taskModifyUserWeekList.add(taskModifyUserWeek);
            });

            if (!CollectionUtils.isEmpty(taskModifyUserWeekList)){
                if (taskModifyMapper.insert(taskModify) == 0){
                    throw new ZSYServiceException("新增任务修改申请失败");
                }
                if (taskModifyUserWeekMapper.insertBatch(taskModifyUserWeekList) == 0){
                    throw new ZSYServiceException("新增任务修改周工时分配失败");
                }
            }else {
                throw new ZSYServiceException("周工作量为空,请检查");
            }
        }

    }


    /**
     * 删除申请
     * @author sch
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        TaskModify taskModify = taskModifyMapper.selectById(id);
        if (taskModify == null){
            throw new ZSYServiceException("当前修改任务申请不存在,请检查");
        }
        if (taskModifyMapper.deleteById(id) == 0){
            throw new ZSYServiceException("删除申请失败");
        }
        if (taskModifyUserWeekMapper.deleteByTmId(id) == 0){
            throw new ZSYServiceException("删除周工作量分配失败");
        }
        taskModifyFuntionMapper.deleteByTmId(id);
    }

    /**
     * 个人查看待审核任务修改申请
     * @author sch
     * @return
     */
    @Override
    public List<TaskModifyListResDTO> getPersonalTaskModifyList() {
        List<TaskModifyBO> modifyBOS = taskModifyMapper.selectListByUser(ZSYTokenRequestContext.get().getUserId());
        List<TaskModifyListResDTO> modifyListResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(modifyBOS)){
            modifyBOS.stream().forEach(modifyBO ->{
                TaskModifyListResDTO resDTO = new TaskModifyListResDTO();
                resDTO.setId(modifyBO.getId());
                resDTO.setReason(modifyBO.getReason());
                resDTO.setDescription(modifyBO.getDescription());
                resDTO.setAvatarUrl(modifyBO.getAvatarUrl());
                resDTO.setTaskId(modifyBO.getTaskId());
                resDTO.setUserId(modifyBO.getUserId());
                resDTO.setTaskName(modifyBO.getTaskName());
                resDTO.setUserName(modifyBO.getUserName());
                resDTO.setBeginTime(modifyBO.getBeginTime());
                resDTO.setEndTime(modifyBO.getEndTime());
                Integer taskLevel = modifyBO.getTaskLevel();
                if (taskLevel != null){{
                    resDTO.setTaskLevel(taskLevel);
                    resDTO.setTaskLevelName(TaskLevel.getName(taskLevel));
                }}
                modifyListResDTOList.add(resDTO);
            });
        }
        return modifyListResDTOList;
    }

    /**
     * 个人分页查看审核通过的任务修改申请
     * @author sch
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<TaskModifyListResDTO> getPersonalTaskModifyPage(Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskModifyBO> modifyBOPage = taskModifyMapper.selectPageByUser(ZSYTokenRequestContext.get().getUserId());
        Page<TaskModifyListResDTO> modifyListResDTOPage = new Page<>();
        BeanUtils.copyProperties(modifyBOPage,modifyListResDTOPage);
        if (!CollectionUtils.isEmpty(modifyBOPage)){
            modifyBOPage.stream().forEach(modifyBO->{
                TaskModifyListResDTO resDTO = new TaskModifyListResDTO();
                resDTO.setId(modifyBO.getId());
                resDTO.setReason(modifyBO.getReason());
                resDTO.setAvatarUrl(modifyBO.getAvatarUrl());
                resDTO.setDescription(modifyBO.getDescription());
                resDTO.setTaskId(modifyBO.getTaskId());
                resDTO.setUserId(modifyBO.getUserId());
                resDTO.setTaskName(modifyBO.getTaskName());
                resDTO.setUserName(modifyBO.getUserName());
                resDTO.setBeginTime(modifyBO.getBeginTime());
                resDTO.setEndTime(modifyBO.getEndTime());
                Integer taskLevel = modifyBO.getTaskLevel();
                if (taskLevel != null){
                    resDTO.setTaskLevel(taskLevel);
                    resDTO.setTaskLevelName(TaskLevel.getName(taskLevel));
                }
                modifyListResDTOPage.add(resDTO);
            });
        }
        return new PageInfo<>(modifyListResDTOPage);
    }

    /**
     * 管理员查看待审核任务修改申请
     * @author sch
     * @return
     */
    @Override
    public List<TaskModifyListResDTO> getTaskModifyList() {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        List<TaskModifyBO> modifyBOS = taskModifyMapper.selectList();
        List<TaskModifyListResDTO> modifyListResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(modifyBOS)){
            modifyBOS.stream().forEach(modifyBO ->{
                TaskModifyListResDTO resDTO = new TaskModifyListResDTO();
                resDTO.setId(modifyBO.getId());
                resDTO.setReason(modifyBO.getReason());
                resDTO.setDescription(modifyBO.getDescription());
                resDTO.setAvatarUrl(modifyBO.getAvatarUrl());
                resDTO.setTaskId(modifyBO.getTaskId());
                resDTO.setUserId(modifyBO.getUserId());
                resDTO.setTaskName(modifyBO.getTaskName());
                resDTO.setUserName(modifyBO.getUserName());
                resDTO.setBeginTime(modifyBO.getBeginTime());
                resDTO.setEndTime(modifyBO.getEndTime());
                Integer taskLevel = modifyBO.getTaskLevel();
                if (taskLevel != null){{
                    resDTO.setTaskLevel(taskLevel);
                    resDTO.setTaskLevelName(TaskLevel.getName(taskLevel));
                }}
                modifyListResDTOList.add(resDTO);
            });
        }
        return modifyListResDTOList;
    }

    /**
     * 管理员分页查看审核通过任务修改申请
     * @author sch
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<TaskModifyListResDTO> getTaskModifyPage(Integer pageNum) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskModifyBO> modifyBOPage = taskModifyMapper.selectPage();
        Page<TaskModifyListResDTO> modifyListResDTOPage = new Page<>();
        BeanUtils.copyProperties(modifyBOPage,modifyListResDTOPage);
        if (!CollectionUtils.isEmpty(modifyBOPage)){
            modifyBOPage.stream().forEach(modifyBO->{
                TaskModifyListResDTO resDTO = new TaskModifyListResDTO();
                resDTO.setId(modifyBO.getId());
                resDTO.setReason(modifyBO.getReason());
                resDTO.setDescription(modifyBO.getDescription());
                resDTO.setAvatarUrl(modifyBO.getAvatarUrl());
                resDTO.setTaskId(modifyBO.getTaskId());
                resDTO.setUserId(modifyBO.getUserId());
                resDTO.setTaskName(modifyBO.getTaskName());
                resDTO.setUserName(modifyBO.getUserName());
                resDTO.setBeginTime(modifyBO.getBeginTime());
                resDTO.setEndTime(modifyBO.getEndTime());
                Integer taskLevel = modifyBO.getTaskLevel();
                if (taskLevel != null){{
                    resDTO.setTaskLevel(taskLevel);
                    resDTO.setTaskLevelName(TaskLevel.getName(taskLevel));
                }}
                modifyListResDTOPage.add(resDTO);
            });
        }
        return new PageInfo<>(modifyListResDTOPage);
    }

    /**
     * 审核任务修改申请
     * @author sch
     * @param editTaskModifyReqDTO
     */
    @Override
    @Transactional
    public void reviewTaskModify(EditTaskModifyReqDTO editTaskModifyReqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        TaskModify taskModify = taskModifyMapper.selectById(editTaskModifyReqDTO.getId());
        if (taskModify == null){
            throw new ZSYServiceException("申请不存在,请检查");
        }
        if (taskModify.getReviewStatus() == 2){
            throw new ZSYServiceException("申请已经审核通过,请检查");
        }
        Long taskId = taskModify.getTaskId();
        Long userId = taskModify.getUserId();
        User user = userMapper.selectById(userId);
        Task task = taskMapper.selectByPrimaryKey(taskId);
        List<UserWeekReqDTO> userWeeks = editTaskModifyReqDTO.getUserWeeks();
        List<UserWeek> userWeekList = new ArrayList<>();
        List<TaskModifyUserWeek> modifyUserWeekList = new ArrayList<>();
        if (CollectionUtils.isEmpty(userWeeks)){
            throw new ZSYServiceException("周工时分配不能为空,请检查");
        }else {
            taskModify.setReason(editTaskModifyReqDTO.getReason());
            taskModify.setDescription(editTaskModifyReqDTO.getDescription());
            taskModify.setBeginTime(editTaskModifyReqDTO.getBeginTime());
            taskModify.setEndTime(editTaskModifyReqDTO.getEndTime());
            taskModify.setWorkHours(editTaskModifyReqDTO.getWorkHours());
            taskModify.setReviewStatus(2);
            taskModify.setReviewTime(new Date());
            taskModify.setTaskLevel(editTaskModifyReqDTO.getTaskLevel());

            TaskUser taskUser = taskUserMapper.selectByTaskAndUser(taskId,userId);
            taskUser.setTaskHours(taskModify.getWorkHours().doubleValue());
            taskUser.setDescription(taskModify.getDescription());
            taskUser.setBeginTime(taskModify.getBeginTime());
            taskUser.setEndTime(taskModify.getEndTime());
            taskUser.setTaskLevel(editTaskModifyReqDTO.getTaskLevel());

            //删除原来的周工时分配
            userWeekMapper.deleteByTaskIdAndUserId(taskId,userId);

            //删除原来的任务修改申请周工时分配
            taskModifyUserWeekMapper.deleteByTmId(taskModify.getId());
            userWeeks.stream().forEach(userWeekReqDTO -> {
                UserWeek userWeek = new UserWeek();
                userWeek.setTaskId(taskId);
                userWeek.setUserId(userId);
                userWeek.setYear(userWeekReqDTO.getYear());
                userWeek.setWeekNumber(userWeekReqDTO.getWeekNumber());
                userWeek.setHours(userWeekReqDTO.getHours());
                userWeek.setId(snowFlakeIDHelper.nextId());
                userWeekList.add(userWeek);

                TaskModifyUserWeek taskModifyUserWeek = new TaskModifyUserWeek();
                taskModifyUserWeek.setId(snowFlakeIDHelper.nextId());
                taskModifyUserWeek.setTaskId(taskId);
                taskModifyUserWeek.setUserId(userId);
                taskModifyUserWeek.setTmId(taskModify.getId());
                taskModifyUserWeek.setYear(userWeekReqDTO.getYear());
                taskModifyUserWeek.setWeekNumber(userWeekReqDTO.getWeekNumber());
                taskModifyUserWeek.setHours(BigDecimal.valueOf(userWeekReqDTO.getHours()));
                taskModifyUserWeek.setStatus(2);
                taskModifyUserWeek.setCreateTime(new Date());
                modifyUserWeekList.add(taskModifyUserWeek);

            });

            //更新原来的taskUser
            taskUserMapper.updateByPrimaryKeySelective(taskUser);

            //更新任务修改申请状态
            if (taskModifyMapper.updateById(taskModify) == 0){
                throw new ZSYServiceException("审核任务修改申请失败");
            }
            //插入新的任务修改申请周工时分配状态
            if (taskModifyUserWeekMapper.insertBatch(modifyUserWeekList) == 0){
                throw new ZSYServiceException("批量更新任务修改申请周工时分审核状态失败");
            }
            //插入新的用户周工时分配
            if (userWeekMapper.insertList(userWeekList) == 0){
                throw new ZSYServiceException("批量更新用户任务周工时分配失败");
            }

            //删除原来的临时任务功能点
            TaskTemp taskTemp = taskTempMapper.selectByUserAndTask(userId, taskId);
            if (taskTemp != null){
                taskTempFunctionMapper.deleteByTtId(taskTemp.getId());
                taskModifyFuntionMapper.deleteByTmId(taskModify.getId());
                List<TaskTempFunctionReqDTO> taskModifyFunctionList = editTaskModifyReqDTO.getTaskModifyFunctionList();
                List<TaskTempFunction> functionList = new ArrayList<>();
                List<TaskModifyFunction> functionList2 = new ArrayList<>();
                if (!CollectionUtils.isEmpty(taskModifyFunctionList)){
                    taskModifyFunctionList.forEach(taskTempFunctionReqDTO -> {
                        TaskTempFunction function = new TaskTempFunction();
                        function.setId(snowFlakeIDHelper.nextId());
                        function.setTtId(taskTemp.getId());
                        function.setFunctionId(taskTempFunctionReqDTO.getFunctionId());
                        function.setLevel(taskTempFunctionReqDTO.getLevel());
                        functionList.add(function);

                        TaskModifyFunction modifyFunction = new TaskModifyFunction();
                        modifyFunction.setId(snowFlakeIDHelper.nextId());
                        modifyFunction.setTmId(taskModify.getId());
                        modifyFunction.setFunctionId(taskTempFunctionReqDTO.getFunctionId());
                        modifyFunction.setLevel(taskTempFunctionReqDTO.getLevel());
                        functionList2.add(modifyFunction);
                    });
                    taskTempFunctionMapper.insertBatch(functionList);
                    taskModifyFuntionMapper.insertBatch(functionList2);
                }
            }

            //新增一条任务日志
            TaskLog taskLog = new TaskLog();
            taskLog.setId(snowFlakeIDHelper.nextId());
            taskLog.setTaskId(taskId);
            String title = ZSYTokenRequestContext.get().getUserName()+"批准了"+user.getName()+"的<子任务修改申请>";
            taskLog.setTitle(title);
            String content = "原因:"+taskModify.getReason();
            taskLog.setContent(content);
            taskLog.setCreateTime(new Date());
            taskLog.setUserId(ZSYTokenRequestContext.get().getUserId());
            taskLog.setUserName(ZSYTokenRequestContext.get().getUserName());
            logMapper.insert(taskLog);

            //任务参与者,包括负责人和开发人员
            Set<Long> joiners = new HashSet<>();
            //1.添加负责人
            joiners.add(task.getCreateBy());
            //2.添加子任务人员
            joiners.add(userId);
            List<Notification> list = new ArrayList<>();
            if (!CollectionUtils.isEmpty(joiners)){
                joiners.stream().forEach(joinerId -> {
                    Notification notification = new Notification();
                    notification.setNid(snowFlakeIDHelper.nextId());
                    notification.setTaskId(taskId);
                    notification.setUserId(joinerId);
                    notification.setContent("您参与的任务:( "+task.getName()+" )有修改: "+user.getName()+"修改了子任务");
                    notification.setCreateTime(new Date());
                    notification.setStatus(0);
                    list.add(notification);
                });
            }
            if (notificationMapper.insertBatch(list) == 0){
                throw new ZSYServiceException("新增任务修改通知失败");
            }
        }
    }

    /**
     * 查看申请详情
     * @author sch
     * @param id
     * @return
     */
    @Override
    public TaskModifyDetailResDTO getTaskModifyDetail(Long id) {
        TaskModifyDetailBO taskModifyDetailBO = taskModifyMapper.selectDetailById(id);
        if (taskModifyDetailBO == null){
            throw new ZSYServiceException("当前修改任务申请不存在,请检查");
        }
        TaskModifyDetailResDTO taskModifyDetailResDTO = new TaskModifyDetailResDTO();
        BeanUtils.copyProperties(taskModifyDetailBO,taskModifyDetailResDTO);
        List<TaskModifyUserWeek> taskModifyUserWeeks = taskModifyDetailBO.getTaskModifyUserWeeks();
        List<UserWeekResDTO> userWeekResDTOList = new ArrayList<>();
        for (TaskModifyUserWeek taskModifyUserWeek : taskModifyUserWeeks) {
            UserWeekResDTO userWeekResDTO = new UserWeekResDTO();
            userWeekResDTO.setYear(taskModifyUserWeek.getYear());
            userWeekResDTO.setWeekNumber(taskModifyUserWeek.getWeekNumber());
            userWeekResDTO.setHours(taskModifyUserWeek.getHours().doubleValue());
            userWeekResDTOList.add(userWeekResDTO);
        }
        userWeekResDTOList = userWeekResDTOList.stream().sorted(Comparator.comparing(UserWeekResDTO::getWeekNumber)).collect(Collectors.toList());
        taskModifyDetailResDTO.setUserWeekResDTOList(userWeekResDTOList);

        List<TaskModifyFunctionBO> functionBOS = taskModifyDetailBO.getFunctionBOS();
        List<TaskModifyFunctionResDTO> functionResDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(functionBOS)){
            functionBOS.forEach(functionBO->{
                TaskModifyFunctionResDTO resDTO = new TaskModifyFunctionResDTO();
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
        taskModifyDetailResDTO.setFunctionResDTOList(functionResDTOS);
        TaskTemp taskTemp = taskTempMapper.selectByUserAndTask(taskModifyDetailBO.getUserId(), taskModifyDetailBO.getTaskId());
        if (taskTemp != null){
            List<TaskTempFunctionBO> functionBOS1 = taskTempFunctionMapper.selectListByTtId(taskTemp.getId());
            List<TaskTempFunctionResDTO> functionResDTOList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(functionBOS1)){
                functionBOS1.forEach(functionBO->{
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
                    functionResDTOList.add(resDTO);
                });
            }
            taskModifyDetailResDTO.setOldFunctionResDTOList(functionResDTOList);
        }
        Integer taskLevel = taskModifyDetailBO.getTaskLevel();
        if (taskLevel != null){
            taskModifyDetailResDTO.setTaskLevel(taskLevel);
            taskModifyDetailResDTO.setTaskLevelName(TaskLevel.getName(taskLevel));
        }
        return taskModifyDetailResDTO;
    }

    /**
     * 根据任务和用户查询周工时分配
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    @Override
    public TaskUserWeekResDTO getBeforeTaskUserWeek(Long taskId, Long userId) {
        return null;
    }

    /**
     * 修改申请
     * @author sch
     * @param editTaskModifyReqDTO
     */
    @Override
    @Transactional
    public void updateModify(EditTaskModifyReqDTO editTaskModifyReqDTO) {
        TaskModify taskModify = taskModifyMapper.selectById(editTaskModifyReqDTO.getId());
        if (taskModify == null){
            throw new ZSYServiceException("当前任务修改申请不存在,请检查");
        }
        Long userId = taskModify.getUserId();
        Long taskId = taskModify.getTaskId();
        List<UserWeekReqDTO> userWeeks = editTaskModifyReqDTO.getUserWeeks();
        List<TaskModifyUserWeek> taskModifyUserWeekList = new ArrayList<>();
        if (CollectionUtils.isEmpty(userWeeks)){
            throw new ZSYServiceException("任务修改周工时分配为空,请检查");
        }else {
            //删除原有的工时分配
            taskModifyUserWeekMapper.deleteByTmId(taskModify.getId());
            taskModify.setReason(editTaskModifyReqDTO.getReason());
            taskModify.setDescription(editTaskModifyReqDTO.getDescription());
            taskModify.setBeginTime(editTaskModifyReqDTO.getBeginTime());
            taskModify.setEndTime(editTaskModifyReqDTO.getEndTime());
            taskModify.setWorkHours(editTaskModifyReqDTO.getWorkHours());
            taskModify.setTaskLevel(editTaskModifyReqDTO.getTaskLevel());

            userWeeks.stream().forEach(userWeekReqDTO -> {
                TaskModifyUserWeek taskModifyUserWeek = new TaskModifyUserWeek();
                taskModifyUserWeek.setCreateTime(new Date());
                taskModifyUserWeek.setStatus(1);
                taskModifyUserWeek.setHours(BigDecimal.valueOf(userWeekReqDTO.getHours()));
                taskModifyUserWeek.setWeekNumber(userWeekReqDTO.getWeekNumber());
                taskModifyUserWeek.setYear(userWeekReqDTO.getYear());
                taskModifyUserWeek.setTmId(taskModify.getId());
                taskModifyUserWeek.setUserId(userId);
                taskModifyUserWeek.setTaskId(taskId);
                taskModifyUserWeek.setId(snowFlakeIDHelper.nextId());
                taskModifyUserWeekList.add(taskModifyUserWeek);
            });

            //删除原有的修改任务功能点
            taskModifyFuntionMapper.deleteByTmId(editTaskModifyReqDTO.getId());
            List<TaskTempFunctionReqDTO> taskModifyFunctionList = editTaskModifyReqDTO.getTaskModifyFunctionList();
            List<TaskModifyFunction> functionList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(taskModifyFunctionList)){
                taskModifyFunctionList.forEach(taskTempFunctionReqDTO -> {
                    TaskModifyFunction function = new TaskModifyFunction();
                    BeanUtils.copyProperties(taskTempFunctionReqDTO,function);
                    function.setId(snowFlakeIDHelper.nextId());
                    function.setTmId(editTaskModifyReqDTO.getId());
                    functionList.add(function);
                });
                taskModifyFuntionMapper.insertBatch(functionList);
            }
            if (taskModifyMapper.updateById(taskModify) == 0){
                throw new ZSYServiceException("更新任务修改申请失败");
            }
            taskModifyUserWeekMapper.insertBatch(taskModifyUserWeekList);
        }

    }
}
