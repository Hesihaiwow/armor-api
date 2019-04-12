package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.bo.TaskTempBO;
import com.zhixinhuixue.armor.model.bo.TaskUserBO;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.TaskTempResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserWeekTempResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.service.IZSYTaskTempService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYTaskStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskType;
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

        if (taskTempMapper.insertTaskTemp(taskTemp) == 0){
            throw new ZSYServiceException("新增任务(临时)失败");
        }

        List<UserWeekReqDTO> userWeeks = addTaskTempReqDTO.getUserWeeks();
        List<UserWeekTemp> userWeekTempList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userWeeks)){
            userWeeks.stream().forEach(week ->{
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
            });
        }else {
            throw new ZSYServiceException("周工时为空");
        }
        if (!CollectionUtils.isEmpty(userWeekTempList)){
            //删除原有的userWeekTemp
            taskTempMapper.deleteUserWeekTempByUserAndTask(userId,addTaskTempReqDTO.getTaskId());
            if (taskTempMapper.insertUserWeekTempBatch(userWeekTempList) == 0){
                throw new ZSYServiceException("批量插入周工作量(临时)失败");
            }
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
        if (existTaskTemp == null){
            throw new ZSYServiceException("当前任务(临时)不存在");
        }
        BeanUtils.copyProperties(editTaskTempReqDTO,existTaskTemp);
        if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0){
            throw new ZSYServiceException("修改任务(临时)失败");
        }

        List<UserWeekReqDTO> userWeeks = editTaskTempReqDTO.getUserWeeks();
        List<UserWeekTemp> userWeekTempList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userWeeks)){
            userWeeks.stream().forEach(week ->{
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
            });

        }
        if (!CollectionUtils.isEmpty(userWeekTempList)){
            //删除原有的userWeekTemp
            taskTempMapper.deleteUserWeekTempByUserAndTask(userId,taskId);
            if (taskTempMapper.insertUserWeekTempBatch(userWeekTempList) == 0){
                throw new ZSYServiceException("批量修改周工作量(临时)失败");
            }
        }
    }

    /**
     * 删除任务
     * @param id
     */
    @Override
    @Transactional
    public void deleteTaskTemp(Long id) {
        TaskTemp existTaskTemp = taskTempMapper.selectById(id);
        if (existTaskTemp == null){
            throw new ZSYServiceException("当前任务(临时)不存在");
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

                }
                taskTempResDTO.setUserWeekTempList(userWeekTempResDTOList);
                taskTempResDTOPage.add(taskTempResDTO);
            });
        }
        return new PageInfo<>(taskTempResDTOPage);
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

                }
                taskTempResDTO.setUserWeekTempList(userWeekTempResDTOList);
                taskTempResDTOPage.add(taskTempResDTO);
            });
        }
        return new PageInfo<>(taskTempResDTOPage);
    }

    /**
     * 管理员审核任务
     * @param editTaskTempReqDTO
     */
    @Override
    public void accessTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO) {
        Long taskId = editTaskTempReqDTO.getTaskId();
        Long userId = editTaskTempReqDTO.getUserId();
        TaskTemp existTaskTemp = taskTempMapper.selectById(editTaskTempReqDTO.getId());
        if (existTaskTemp == null){
            throw new ZSYServiceException("当前任务(临时)不存在");
        }
        BeanUtils.copyProperties(editTaskTempReqDTO,existTaskTemp);
        if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0){
            throw new ZSYServiceException("修改任务(临时)失败");
        }

        List<UserWeekReqDTO> userWeeks = editTaskTempReqDTO.getUserWeeks();
        List<UserWeekTemp> userWeekTempList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userWeeks)){
            userWeeks.stream().forEach(week ->{
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
            });

        }
        if (!CollectionUtils.isEmpty(userWeekTempList)){
            //删除原有的userWeekTemp
            taskTempMapper.deleteUserWeekTempByUserAndTask(userId,taskId);
            if (taskTempMapper.insertUserWeekTempBatch(userWeekTempList) == 0){
                throw new ZSYServiceException("批量修改周工作量(临时)失败");
            }
        }

        User user = userMapper.selectById(userId);
        TaskDetailBO taskTemp = taskMapper.selectTaskDetailByTaskId(taskId);
        if (taskTemp == null) {
            throw new ZSYServiceException("无法找到任务,id:" + taskId);
        }
        if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue() && taskTemp.getType() == ZSYTaskType.PUBLIC_TASK.getValue()) {
            throw new ZSYServiceException("该任务已结束");
        }
        if (existTaskTemp.getReviewStatus() == 1){
            existTaskTemp.setReviewStatus(2);
            if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0){
                throw new ZSYServiceException("审核失败");
            }

            //删除taskUser
            taskUserMapper.deleteByTaskIdAndUserId(taskId,userId);
            //删除userWeek
            userWeekMapper.deleteByTaskIdAndUserId(taskId,userId);

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
            taskUserMapper.insert(taskUser);


            List<UserWeekTemp> userWeekTemps = taskTempMapper.selectUserWeekTempByUserAndTask(userId, taskId);
            List<UserWeek> userWeekList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(userWeekTempList)){
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
            if (!CollectionUtils.isEmpty(userWeekList)){
                if(userWeekMapper.insertList(userWeekList) == 0){
                    throw new ZSYServiceException("批量插入userWeek失败");
                }
            }

            taskLogMapper.insert(taskService.buildLog(ZSYTokenRequestContext.get().getUserName() + "通过了"+user.getName()+"的多人任务审核", existTaskTemp.getDescription(), taskId));

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
            if (!CollectionUtils.isEmpty(taskUsers)){
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

            taskService.missionModified(taskTemp,taskReqDTO);
        }else {
            throw new ZSYServiceException("已通过审核,请检查");
        }

    }
}
