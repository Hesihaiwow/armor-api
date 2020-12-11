package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskTempService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.pagehelper.page.PageMethod.startPage;

/**
 * @author sch
 * @DATE 2019/4/2 9:45
 */
@Service
public class ZSYTaskTempService implements IZSYTaskTempService {
    @Resource
    private IZSYTaskTempMapper taskTempMapper;
    @Resource
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Resource
    private IZSYTaskMapper taskMapper;
    @Resource
    private IZSYTaskUserMapper taskUserMapper;
    @Resource
    private IZSYUserWeekMapper userWeekMapper;
    @Resource
    private IZSYTaskLogMapper taskLogMapper;
    @Resource
    private IZSYUserMapper userMapper;
    @Resource
    private ZSYTaskService taskService;
    @Resource
    private IZSYTaskTempFunctionMapper functionMapper;
    @Resource
    private IZSYTaskTempTagMapper taskTempTagMapper;
    @Resource
    private IZSYProjectMapper projectMapper;
    @Resource
    private IZSYTaskTagMapper taskTagMapper;

    /**
     * 新增任务(临时)
     *
     * @param addTaskTempReqDTO 参数
     */
    @Override
    @Transactional
    public void addTaskTemp(AddTaskTempReqDTO addTaskTempReqDTO) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        TaskTemp existTaskTemp = taskTempMapper.selectByUserAndTask(userId, addTaskTempReqDTO.getTaskId());
        List<Long> userIds = taskUserMapper.selectUserByTaskId(addTaskTempReqDTO.getTaskId());
        if (existTaskTemp != null || userIds.contains(userId)) {
            throw new ZSYServiceException("当前任务您已经存在子任务,请检查");
        }
        TaskTemp taskTemp = new TaskTemp();
        BeanUtils.copyProperties(addTaskTempReqDTO, taskTemp);
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
        if (!CollectionUtils.isEmpty(userWeeks)) {
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
        } else {
            throw new ZSYServiceException("周工时为空");
        }
        if (totalHours.compareTo(addTaskTempReqDTO.getWorkHours()) != 0) {
            throw new ZSYServiceException("周工作量之和与任务工作量不符,请检查");
        }
        List<TaskTempFunctionReqDTO> functionReqDTOS = addTaskTempReqDTO.getTaskTempFunctionList();
        List<TaskTempFunction> functionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(functionReqDTOS)) {
            functionReqDTOS.forEach(functionReqDTO -> {
                TaskTempFunction function = new TaskTempFunction();
                BeanUtils.copyProperties(functionReqDTO, function);
                function.setId(snowFlakeIDHelper.nextId());
                function.setTtId(taskTemp.getId());
                functionList.add(function);
            });
        }

        if (taskTempMapper.insertTaskTemp(taskTemp) == 0) {
            throw new ZSYServiceException("新增任务(临时)失败");
        }
        if (!CollectionUtils.isEmpty(userWeekTempList)) {
            //删除原有的userWeekTemp
            taskTempMapper.deleteUserWeekTempByUserAndTask(userId, addTaskTempReqDTO.getTaskId());
            if (taskTempMapper.insertUserWeekTempBatch(userWeekTempList) == 0) {
                throw new ZSYServiceException("批量插入周工作量(临时)失败");
            }
        }
        if (!CollectionUtils.isEmpty(functionList)) {
            functionMapper.insertBatch(functionList);
        }
    }

    /**
     * 修改任务
     *
     * @param editTaskTempReqDTO 参数
     */
    @Override
    @Transactional
    public void updateTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO) {
        Long taskId = editTaskTempReqDTO.getTaskId();
        Long userId = editTaskTempReqDTO.getUserId();
        TaskTemp existTaskTemp = taskTempMapper.selectById(editTaskTempReqDTO.getId());
        List<TaskReviewLogBO> taskReviewLogBOS = taskTempMapper.selectTaskReviewLogByTaskTemp(editTaskTempReqDTO.getId());
        if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
            throw new ZSYServiceException("当前任务(临时)已通过审核,无法修改");
        }
        if (existTaskTemp == null) {
            throw new ZSYServiceException("当前任务(临时)不存在");
        }
        BeanUtils.copyProperties(editTaskTempReqDTO, existTaskTemp);
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
                totalHours = totalHours.add(BigDecimal.valueOf(week.getHours()));
            }
        } else {
            throw new ZSYServiceException("周工时为空");
        }
        if (totalHours.compareTo(existTaskTemp.getWorkHours()) != 0) {
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
        if (!CollectionUtils.isEmpty(taskTempFunctionList)) {
            taskTempFunctionList.forEach(taskTempFunctionReqDTO -> {
                TaskTempFunction function = new TaskTempFunction();
                BeanUtils.copyProperties(taskTempFunctionReqDTO, function);
                function.setId(snowFlakeIDHelper.nextId());
                function.setTtId(editTaskTempReqDTO.getId());
                functionList.add(function);
            });
        }
        //删除原有的function
        functionMapper.deleteByTtId(editTaskTempReqDTO.getId());
        if (!CollectionUtils.isEmpty(functionList)) {
            functionMapper.insertBatch(functionList);
        }
    }

    /**
     * 删除任务
     *
     * @param id 主键
     */
    @Override
    @Transactional
    public void deleteTaskTemp(Long id) {
        Integer userRole = ZSYTokenRequestContext.get().getUserRole();
        TaskTemp existTaskTemp = taskTempMapper.selectById(id);
        if (existTaskTemp == null) {
            throw new ZSYServiceException("当前任务(临时)不存在");
        }
        List<TaskReviewLogBO> taskReviewLogBOS = taskTempMapper.selectTaskReviewLogByTaskTemp(id);
        if (ZSYUserRole.ADMINISTRATOR.getValue() != userRole && !CollectionUtils.isEmpty(taskReviewLogBOS)) {
            throw new ZSYServiceException("当前任务(临时)已通过审核,无法删除");
        }
        existTaskTemp.setReviewStatus(3);
        if (taskTempMapper.updateTaskTemp(existTaskTemp) == 0) {
            throw new ZSYServiceException("删除任务(临时)失败");
        }

        List<UserWeekTemp> userWeekTemps = taskTempMapper.selectUserWeekTempByUserAndTask(existTaskTemp.getUserId(), existTaskTemp.getTaskId());
        List<Long> idList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userWeekTemps)) {
            userWeekTemps.forEach(userWeekTemp ->
                    idList.add(userWeekTemp.getId())
            );
        }
        if (!CollectionUtils.isEmpty(idList)) {
            taskTempMapper.updateUserWeekTempBatch(idList);
        }

        //删除功能点
        functionMapper.deleteByTtId(id);

        //删除审核日志
        taskTempMapper.deleteTaskReviewLog(id);

        //删除标签
        taskTempTagMapper.deleteByTtId(id);
    }

    /**
     * 个人分页查看任务
     *
     * @param pageNum      页码
     * @param reviewStatus 审核状态
     * @return PageInfo<TaskTempResDTO>
     */
    @Override
    public PageInfo<TaskTempResDTO> getPersonalTaskTempPage(Integer pageNum, Integer reviewStatus) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskTempBO> taskTempBOList = taskTempMapper.selectPersonalTaskTempPage(userId, reviewStatus);
        Page<TaskTempResDTO> taskTempResDTOPage = new Page<>();
        BeanUtils.copyProperties(taskTempBOList, taskTempResDTOPage);
        if (!CollectionUtils.isEmpty(taskTempBOList)) {
            taskTempBOList.forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO, taskTempResDTO);
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
                taskTempResDTOPage.add(taskTempResDTO);
            });
        }
        return new PageInfo<>(taskTempResDTOPage);
    }

    /**
     * 个人查看待审核任务
     *
     * @return List<TaskResDTO>
     */
    @Override
    public List<TaskResDTO> getPersonalTaskTempList() {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        //待审核多人任务
        List<TaskTempBO> taskTempBOList = taskTempMapper.selectPersonalTaskTempList(userId);
        //待审核个人任务
        List<TaskTempBO> priTaskTempBOList = taskTempMapper.selectPersonalPriTaskTempList(userId);
        List<TaskResDTO> taskResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskTempBOList)) {
            taskTempBOList.forEach(taskTempBO -> {
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

        if (!CollectionUtils.isEmpty(priTaskTempBOList)) {
            priTaskTempBOList.forEach(taskTempBO -> {
                TaskResDTO taskResDTO = new TaskResDTO();
                taskResDTO.setTtId(taskTempBO.getId());
                taskResDTO.setExpand(false);
                taskResDTO.setAvatarUrl(taskTempBO.getAvatarUrl());
                taskResDTO.setCreateBy(taskTempBO.getUserId());
                taskResDTO.setCreateTime(taskTempBO.getCreateTime());
                taskResDTO.setDescription(taskTempBO.getDescription());
                taskResDTO.setEndTime(taskTempBO.getEndTime());
                taskResDTO.setName(taskTempBO.getPriTaskName());
                taskResDTO.setPriority(1);
                taskResDTO.setProjectId(taskTempBO.getProjectId());
                taskResDTO.setProjectImage(taskTempBO.getProjectImage());
                taskResDTO.setProjectName(taskTempBO.getProjectName());
                taskResDTO.setReviewStatus(1);
                taskResDTO.setId(taskTempBO.getId());
                taskResDTO.setStatus(1);
                taskResDTO.setType(1);
                taskResDTO.setUserName(taskTempBO.getUserName());

                List<TaskUserResDTO> taskUserResDTOS = Lists.newArrayList();
                TaskUserResDTO taskUserResDTO = new TaskUserResDTO();
                taskUserResDTO.setBeginTime(taskTempBO.getBeginTime());
                taskUserResDTO.setEndTime(taskTempBO.getEndTime());
                taskUserResDTO.setCreateTime(taskTempBO.getCreateTime());
                taskUserResDTO.setDescription(taskTempBO.getDescription());
                taskUserResDTO.setStatus(1);
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
     *
     * @param ttId 主键
     * @return TaskTempResDTO
     */
    @Override
    public TaskTempResDTO getTaskTempById(Long ttId) {
        TaskTempBO taskTempBO = taskTempMapper.selectTaskTempBOById(ttId);
        TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
        BeanUtils.copyProperties(taskTempBO, taskTempResDTO);
        if (taskTempBO.getType().equals(ZSYTaskType.PRIVATE_TASK.getValue())) {
            taskTempResDTO.setTaskName(taskTempBO.getPriTaskName());
            Project project = projectMapper.selectById(taskTempBO.getProjectId());
            if (project != null) {
                taskTempResDTO.setProjectImage(project.getImageUrl());
                taskTempResDTO.setProjectName(project.getName());
            }
            List<TaskTempTag> tags = taskTempTagMapper.selectByTtId(ttId);
            if (!CollectionUtils.isEmpty(tags)) {
                taskTempResDTO.setTagList(tags.stream().map(tag -> tag.getTagId() + "").collect(Collectors.toList()));
            }
            if (taskTempBO.getLinkTaskId() != null) {
                taskTempResDTO.setLinkTaskId(taskTempBO.getLinkTaskId());
            }
        } else {
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

            List<TaskTempFunctionBO> functions = taskTempBO.getFunctions();
            List<TaskTempFunctionResDTO> functionResDTOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(functions)) {
                functions.forEach(function -> {
                    TaskTempFunctionResDTO resDTO = new TaskTempFunctionResDTO();
                    BeanUtils.copyProperties(function, resDTO);
                    resDTO.setActionName(FunctionAction.getName(function.getAction()));
                    Integer level = function.getLevel();
                    if (level == 1) {
                        resDTO.setLevelName("一级");
                    } else if (level == 2) {
                        resDTO.setLevelName("二级");
                    } else if (level == 3) {
                        resDTO.setLevelName("三级");
                    } else if (level == 4) {
                        resDTO.setLevelName("四级");
                    } else if (level == 5) {
                        resDTO.setLevelName("五级");
                    }
                    List<UserAndLevelBO> userAndLevelBOS = functionMapper.selectUserAndLevelByFunction(function.getId());
                    List<UserAndLevelResDTO> userAndLevelResDTOS = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(userAndLevelBOS)) {
                        userAndLevelBOS.forEach(userAndLevelBO -> {
                            UserAndLevelResDTO userAndLevelResDTO = new UserAndLevelResDTO();
                            BeanUtils.copyProperties(userAndLevelBO, userAndLevelResDTO);
                            Integer level2 = userAndLevelBO.getLevel();
                            if (level2 == 1) {
                                userAndLevelResDTO.setLevelName("一级");
                            } else if (level2 == 2) {
                                userAndLevelResDTO.setLevelName("二级");
                            } else if (level2 == 3) {
                                userAndLevelResDTO.setLevelName("三级");
                            } else if (level2 == 4) {
                                userAndLevelResDTO.setLevelName("四级");
                            } else if (level2 == 5) {
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
        }

        List<TaskReviewLogBO> taskReviewLogBOS = taskTempBO.getTaskReviewLogBOS();
        List<TaskReviewLogResDTO> taskReviewLogResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
            List<TaskReviewLogBO> curUserReviewLogs = taskReviewLogBOS.stream()
                    .filter(item -> item.getCheckUserId().equals(ZSYTokenRequestContext.get().getUserId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(curUserReviewLogs)) {
                taskTempResDTO.setIsChecked(1);
            } else {
                taskTempResDTO.setIsChecked(0);
            }
            taskReviewLogBOS.forEach(taskReviewLogBO -> {
                TaskReviewLogResDTO taskReviewLogResDTO = new TaskReviewLogResDTO();
                taskReviewLogResDTO.setCheckUserId(taskReviewLogBO.getCheckUserId());
                taskReviewLogResDTO.setCheckUserName(taskReviewLogBO.getCheckUserName());
                taskReviewLogResDTO.setReviewTime(taskReviewLogBO.getReviewTime());
                taskReviewLogResDTO.setLevel(taskReviewLogBO.getLevel());
                String suggest = taskReviewLogBO.getSuggest();
                if (suggest != null && !suggest.trim().equals("")) {
                    taskReviewLogResDTO.setSuggest(taskReviewLogBO.getSuggest());
                } else {
                    taskReviewLogResDTO.setSuggest("无");
                }
                taskReviewLogResDTOList.add(taskReviewLogResDTO);
            });
        } else {
            taskTempResDTO.setIsChecked(0);
        }
        taskTempResDTO.setTaskReviewLogResDTOList(taskReviewLogResDTOList);

        return taskTempResDTO;
    }

    /**
     * 根据阶段查询可用的多人任务
     *
     * @param stageId 阶段id
     * @return List<TaskBaseResDTO>
     */
    @Override
    public List<TaskBaseResDTO> getMultipleTaskByStage(Long stageId) {
        List<TaskListBO> taskListBOS = taskMapper.selectTaskInfoByStageId(stageId, ZSYTokenRequestContext.get().getDepartmentId());

        List<TaskBaseResDTO> list = new ArrayList<>();
        BeanUtils.copyProperties(taskListBOS, list);
        taskListBOS.forEach(taskListBO -> {
            TaskBaseResDTO taskBaseResDTO = new TaskBaseResDTO();
            taskBaseResDTO.setId(taskListBO.getId());
            taskBaseResDTO.setName(taskListBO.getName());
            list.add(taskBaseResDTO);
        });
        return list;
    }

    /**
     * 查询待审核多人任务
     *
     * @param checkUserId 审核人
     * @return List<TaskTempResDTO>
     */
    @Override
    public List<TaskTempResDTO> getPendingTaskTempListByCheckUser(Long checkUserId) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
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
            taskTempBOList.forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO, taskTempResDTO);
                Integer taskLevel = taskTempBO.getTaskLevel();
                if (taskLevel != null) {
                    if (taskLevel == 1) {
                        taskTempResDTO.setTaskLevelName("一级");
                    } else if (taskLevel == 2) {
                        taskTempResDTO.setTaskLevelName("二级");
                    } else if (taskLevel == 3) {
                        taskTempResDTO.setTaskLevelName("三级");
                    } else if (taskLevel == 4) {
                        taskTempResDTO.setTaskLevelName("四级");
                    } else if (taskLevel == 5) {
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
                if (checkUserIds.contains(ZSYTokenRequestContext.get().getUserId())) {
                    taskTempResDTO.setIsChecked(1);
                } else {
                    taskTempResDTO.setIsChecked(0);
                }
                List<TaskReviewLogResDTO> taskReviewLogResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
                    taskReviewLogBOS.forEach(taskReviewLogBO -> {
                        TaskReviewLogResDTO taskReviewLogResDTO = new TaskReviewLogResDTO();
                        taskReviewLogResDTO.setCheckUserId(taskReviewLogBO.getCheckUserId());
                        taskReviewLogResDTO.setCheckUserName(taskReviewLogBO.getCheckUserName());
                        taskReviewLogResDTO.setReviewTime(taskReviewLogBO.getReviewTime());
                        taskReviewLogResDTO.setLevel(taskReviewLogBO.getLevel());
                        String suggest = taskReviewLogBO.getSuggest();
                        if (suggest != null && !suggest.trim().equals("")) {
                            taskReviewLogResDTO.setSuggest(taskReviewLogBO.getSuggest());
                        } else {
                            taskReviewLogResDTO.setSuggest("无");
                        }
                        taskReviewLogResDTOList.add(taskReviewLogResDTO);
                    });
                }
                taskTempResDTO.setTaskReviewLogResDTOList(taskReviewLogResDTOList);
                List<TaskTempFunctionBO> functions = taskTempBO.getFunctions();
                List<TaskTempFunctionResDTO> functionResDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(functions)) {
                    functions.forEach(function -> {
                        TaskTempFunctionResDTO resDTO = new TaskTempFunctionResDTO();
                        BeanUtils.copyProperties(function, resDTO);
                        resDTO.setActionName(FunctionAction.getName(function.getAction()));
                        Integer level = function.getLevel();
                        if (level == 1) {
                            resDTO.setLevelName("一级");
                        } else if (level == 2) {
                            resDTO.setLevelName("二级");
                        } else if (level == 3) {
                            resDTO.setLevelName("三级");
                        } else if (level == 4) {
                            resDTO.setLevelName("四级");
                        } else if (level == 5) {
                            resDTO.setLevelName("五级");
                        }
                        List<UserAndLevelBO> userAndLevelBOS = functionMapper.selectUserAndLevelByFunction(function.getFunctionId());
                        List<UserAndLevelResDTO> userAndLevelResDTOS = new ArrayList<>();
                        if (!CollectionUtils.isEmpty(userAndLevelBOS)) {
                            userAndLevelBOS.forEach(userAndLevelBO -> {
                                UserAndLevelResDTO userAndLevelResDTO = new UserAndLevelResDTO();
                                BeanUtils.copyProperties(userAndLevelBO, userAndLevelResDTO);
                                Integer level2 = userAndLevelBO.getLevel();
                                if (level2 == 1) {
                                    userAndLevelResDTO.setLevelName("一级");
                                } else if (level2 == 2) {
                                    userAndLevelResDTO.setLevelName("二级");
                                } else if (level2 == 3) {
                                    userAndLevelResDTO.setLevelName("三级");
                                } else if (level2 == 4) {
                                    userAndLevelResDTO.setLevelName("四级");
                                } else if (level2 == 5) {
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
     *
     * @param checkUserId 审核人
     * @return PageInfo<TaskTempResDTO>
     */
    @Override
    public PageInfo<TaskTempResDTO> getAccessedTaskTempListByCheckUser(Integer pageNum, Long checkUserId) {
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskTempBO> taskTempBOList = taskTempMapper.selectAccessedByCheckUser(checkUserId);
        Page<TaskTempResDTO> taskTempResDTOList = new Page<>();
        BeanUtils.copyProperties(taskTempBOList, taskTempResDTOList);
        if (!CollectionUtils.isEmpty(taskTempBOList)) {
            taskTempBOList.forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO, taskTempResDTO);
                Integer taskLevel = taskTempBO.getTaskLevel();
                if (taskLevel != null) {
                    if (taskLevel == 1) {
                        taskTempResDTO.setTaskLevelName("一级");
                    } else if (taskLevel == 2) {
                        taskTempResDTO.setTaskLevelName("二级");
                    } else if (taskLevel == 3) {
                        taskTempResDTO.setTaskLevelName("三级");
                    } else if (taskLevel == 4) {
                        taskTempResDTO.setTaskLevelName("四级");
                    } else if (taskLevel == 5) {
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
                if (checkUserIds.contains(ZSYTokenRequestContext.get().getUserId())) {
                    taskTempResDTO.setIsChecked(1);
                } else {
                    taskTempResDTO.setIsChecked(0);
                }
                List<TaskReviewLogResDTO> taskReviewLogResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
                    taskReviewLogBOS.forEach(taskReviewLogBO -> {
                        TaskReviewLogResDTO taskReviewLogResDTO = new TaskReviewLogResDTO();
                        taskReviewLogResDTO.setCheckUserId(taskReviewLogBO.getCheckUserId());
                        taskReviewLogResDTO.setCheckUserName(taskReviewLogBO.getCheckUserName());
                        taskReviewLogResDTO.setReviewTime(taskReviewLogBO.getReviewTime());
                        taskReviewLogResDTO.setLevel(taskReviewLogBO.getLevel());
                        String suggest = taskReviewLogBO.getSuggest();
                        if (suggest != null && !suggest.trim().equals("")) {
                            taskReviewLogResDTO.setSuggest(taskReviewLogBO.getSuggest());
                        } else {
                            taskReviewLogResDTO.setSuggest("无");
                        }
                        taskReviewLogResDTOList.add(taskReviewLogResDTO);
                    });
                }
                taskTempResDTO.setTaskReviewLogResDTOList(taskReviewLogResDTOList);

                List<TaskTempFunctionBO> functions = taskTempBO.getFunctions();
                List<TaskTempFunctionResDTO> functionResDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(functions)) {
                    functions.forEach(function -> {
                        TaskTempFunctionResDTO resDTO = new TaskTempFunctionResDTO();
                        BeanUtils.copyProperties(function, resDTO);
                        resDTO.setActionName(FunctionAction.getName(function.getAction()));
                        Integer level = function.getLevel();
                        if (level == 1) {
                            resDTO.setLevelName("一级");
                        } else if (level == 2) {
                            resDTO.setLevelName("二级");
                        } else if (level == 3) {
                            resDTO.setLevelName("三级");
                        } else if (level == 4) {
                            resDTO.setLevelName("四级");
                        } else if (level == 5) {
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
     * 查询待审核个人任务
     *
     * @param checkUserId 审核人
     * @return List<TaskTempResDTO>
     */
    @Override
    public List<TaskTempResDTO> getPendingPriTaskTempListByCheckUser(Long checkUserId) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        //一级待审核临时个人任务
        List<TaskTempBO> levelOne = taskTempMapper.selectPriTaskTemp(userId, 1);
        //二级待审核临时个人任务
        List<TaskTempBO> levelTwo = taskTempMapper.selectPriTaskTemp(userId, 2);
        //三级待审核临时个人任务
        List<TaskTempBO> levelThree = taskTempMapper.selectPriTaskTemp(userId, 3);
        List<TaskTempBO> taskTempBOList = new ArrayList<>();
        taskTempBOList.addAll(levelOne);
        taskTempBOList.addAll(levelTwo);
        taskTempBOList.addAll(levelThree);
        List<TaskTempResDTO> taskTempResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskTempBOList)) {
            taskTempBOList.forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO, taskTempResDTO);
                taskTempResDTO.setTaskName(taskTempBO.getPriTaskName());
                Integer taskLevel = taskTempBO.getTaskLevel();
                if (taskLevel != null) {
                    if (taskLevel == 1) {
                        taskTempResDTO.setTaskLevelName("一级");
                    } else if (taskLevel == 2) {
                        taskTempResDTO.setTaskLevelName("二级");
                    } else if (taskLevel == 3) {
                        taskTempResDTO.setTaskLevelName("三级");
                    } else if (taskLevel == 4) {
                        taskTempResDTO.setTaskLevelName("四级");
                    } else if (taskLevel == 5) {
                        taskTempResDTO.setTaskLevelName("五级");
                    }
                }

//                List<TaskReviewLogBO> taskReviewLogBOS = taskTempBO.getTaskReviewLogBOS();
//                List<Long> checkUserIds = taskReviewLogBOS.stream().map(TaskReviewLog::getCheckUserId).collect(Collectors.toList());
//                if (checkUserIds.contains(ZSYTokenRequestContext.get().getUserId())) {
//                    taskTempResDTO.setIsChecked(1);
//                } else {
//                    taskTempResDTO.setIsChecked(0);
//                }
//                List<TaskReviewLogResDTO> taskReviewLogResDTOList = new ArrayList<>();
//                if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
//                    taskReviewLogBOS.forEach(taskReviewLogBO -> {
//                        TaskReviewLogResDTO taskReviewLogResDTO = new TaskReviewLogResDTO();
//                        taskReviewLogResDTO.setCheckUserId(taskReviewLogBO.getCheckUserId());
//                        taskReviewLogResDTO.setCheckUserName(taskReviewLogBO.getCheckUserName());
//                        taskReviewLogResDTO.setReviewTime(taskReviewLogBO.getReviewTime());
//                        taskReviewLogResDTO.setLevel(taskReviewLogBO.getLevel());
//                        String suggest = taskReviewLogBO.getSuggest();
//                        if (suggest != null && !suggest.trim().equals("")) {
//                            taskReviewLogResDTO.setSuggest(taskReviewLogBO.getSuggest());
//                        } else {
//                            taskReviewLogResDTO.setSuggest("无");
//                        }
//                        taskReviewLogResDTOList.add(taskReviewLogResDTO);
//                    });
//                }
//                taskTempResDTO.setTaskReviewLogResDTOList(taskReviewLogResDTOList);
                taskTempResDTOList.add(taskTempResDTO);
            });
        }
        return taskTempResDTOList;
    }

    /**
     * 查询审核通过个人任务
     *
     * @param pageNum     页码
     * @param checkUserId 审核人
     * @return PageInfo<TaskTempResDTO>
     */
    @Override
    public PageInfo<TaskTempResDTO> getAccessedPriTaskTempListByCheckUser(Integer pageNum, Long checkUserId) {
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<TaskTempBO> taskTempBOList = taskTempMapper.selectAccessedPriTasksByCheckUser(checkUserId);
        Page<TaskTempResDTO> taskTempResDTOList = new Page<>();
        BeanUtils.copyProperties(taskTempBOList, taskTempResDTOList);
        if (!CollectionUtils.isEmpty(taskTempBOList)) {
            taskTempBOList.forEach(taskTempBO -> {
                TaskTempResDTO taskTempResDTO = new TaskTempResDTO();
                BeanUtils.copyProperties(taskTempBO, taskTempResDTO);
                taskTempResDTO.setTaskName(taskTempBO.getPriTaskName());
                Integer taskLevel = taskTempBO.getTaskLevel();
                if (taskLevel != null) {
                    if (taskLevel == 1) {
                        taskTempResDTO.setTaskLevelName("一级");
                    } else if (taskLevel == 2) {
                        taskTempResDTO.setTaskLevelName("二级");
                    } else if (taskLevel == 3) {
                        taskTempResDTO.setTaskLevelName("三级");
                    } else if (taskLevel == 4) {
                        taskTempResDTO.setTaskLevelName("四级");
                    } else if (taskLevel == 5) {
                        taskTempResDTO.setTaskLevelName("五级");
                    }
                }
                List<TaskReviewLogBO> taskReviewLogBOS = taskTempBO.getTaskReviewLogBOS();
                List<Long> checkUserIds = taskReviewLogBOS.stream().map(TaskReviewLog::getCheckUserId).collect(Collectors.toList());
                if (checkUserIds.contains(ZSYTokenRequestContext.get().getUserId())) {
                    taskTempResDTO.setIsChecked(1);
                } else {
                    taskTempResDTO.setIsChecked(0);
                }
                List<TaskReviewLogResDTO> taskReviewLogResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(taskReviewLogBOS)) {
                    taskReviewLogBOS.forEach(taskReviewLogBO -> {
                        TaskReviewLogResDTO taskReviewLogResDTO = new TaskReviewLogResDTO();
                        taskReviewLogResDTO.setCheckUserId(taskReviewLogBO.getCheckUserId());
                        taskReviewLogResDTO.setCheckUserName(taskReviewLogBO.getCheckUserName());
                        taskReviewLogResDTO.setReviewTime(taskReviewLogBO.getReviewTime());
                        taskReviewLogResDTO.setLevel(taskReviewLogBO.getLevel());
                        String suggest = taskReviewLogBO.getSuggest();
                        if (suggest != null && !suggest.trim().equals("")) {
                            taskReviewLogResDTO.setSuggest(taskReviewLogBO.getSuggest());
                        } else {
                            taskReviewLogResDTO.setSuggest("无");
                        }
                        taskReviewLogResDTOList.add(taskReviewLogResDTO);
                    });
                }
                taskTempResDTO.setTaskReviewLogResDTOList(taskReviewLogResDTOList);
                taskTempResDTOList.add(taskTempResDTO);
            });
        }
        return new PageInfo<>(taskTempResDTOList);
    }

    /**
     * 新增个人任务(临时)
     *
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional
    public void addPriTaskTemp(AddPriTaskTempReqDTO reqDTO) {
        String taskName = reqDTO.getTaskName();
        if (Strings.isNullOrEmpty(taskName)) {
            throw new ZSYServiceException("个人任务名称不能为空");
        }
        String description = reqDTO.getDescription();
        if (Strings.isNullOrEmpty(description)) {
            throw new ZSYServiceException("任务描述不能为空");
        }
        if (reqDTO.getBeginTime().after(reqDTO.getEndTime())) {
            throw new ZSYServiceException("开始时间不能在截止时间后面");
        }
        int beginDayOfWeek = DateHelper.getCurrentWeekNumber(reqDTO.getBeginTime());
        int endDayOfWeek = DateHelper.getCurrentWeekNumber(reqDTO.getEndTime());
        if (beginDayOfWeek != endDayOfWeek) {
            throw new ZSYServiceException("个人任务不能跨周");
        }
        LoginInfoReqDTO loginUser = ZSYTokenRequestContext.get();
        Date curDate = new Date();
        TaskTemp taskTemp = new TaskTemp();
        taskTemp.setId(snowFlakeIDHelper.nextId());
        if (reqDTO.getLinkTaskId() != null) {
            taskTemp.setLinkTaskId(reqDTO.getLinkTaskId());
        }
        taskTemp.setProjectId(reqDTO.getProjectId());
        taskTemp.setUserId(loginUser.getUserId());
        taskTemp.setWorkHours(reqDTO.getWorkHours());
        taskTemp.setBeginTime(reqDTO.getBeginTime());
        taskTemp.setEndTime(reqDTO.getEndTime());
        taskTemp.setType(1);
        taskTemp.setStatus(1);
        taskTemp.setReviewStatus(1);
        taskTemp.setCreateTime(curDate);
        taskTemp.setDescription(description.trim());
        taskTemp.setLevel(1);
        taskTemp.setPriTaskName(taskName.trim());
        taskTempMapper.insertTaskTemp(taskTemp);

        List<TaskTempTag> taskTempTagList = reqDTO.getTagList().stream().map(tagId -> {
            TaskTempTag taskTempTag = new TaskTempTag();
            taskTempTag.setId(snowFlakeIDHelper.nextId());
            taskTempTag.setTtId(taskTemp.getId());
            taskTempTag.setTagId(tagId);
            return taskTempTag;
        }).collect(Collectors.toList());
        taskTempTagMapper.insertBatch(taskTempTagList);

    }

    /**
     * 编辑个人任务(临时)
     *
     * @param reqDTO 入参
     * @param ttId   id
     */
    @Override
    @Transactional
    public void editPriTaskTemp(EditPriTaskTempReqDTO reqDTO, Long ttId) {
        TaskTemp exist = taskTempMapper.selectById(ttId);
        if (exist == null) {
            throw new ZSYServiceException("个人任务临时任务不存在");
        }
        LoginInfoReqDTO loginUser = ZSYTokenRequestContext.get();
        if (loginUser.getUserRole() > ZSYUserRole.ADMINISTRATOR.getValue()) {
            if (exist.getReviewStatus() > 1) {
                throw new ZSYServiceException("个人任务临时任务已审核或已删除,无法修改");
            } else if (exist.getLevel() > 1) {
                throw new ZSYServiceException("个人任务临时任务已在审核流程中,无法修改");
            }
        }
        String taskName = reqDTO.getTaskName();
        if (Strings.isNullOrEmpty(taskName)) {
            throw new ZSYServiceException("个人任务名称不能为空");
        }
        String description = reqDTO.getDescription();
        if (Strings.isNullOrEmpty(description)) {
            throw new ZSYServiceException("任务描述不能为空");
        }
        if (reqDTO.getBeginTime().after(reqDTO.getEndTime())) {
            throw new ZSYServiceException("开始时间不能在截止时间后面");
        }
        int beginDayOfWeek = DateHelper.getCurrentWeekNumber(reqDTO.getBeginTime());
        int endDayOfWeek = DateHelper.getCurrentWeekNumber(reqDTO.getEndTime());
        if (beginDayOfWeek != endDayOfWeek) {
            throw new ZSYServiceException("个人任务不能跨周");
        }
        TaskTemp taskTemp = new TaskTemp();
        taskTemp.setId(ttId);
        if (reqDTO.getLinkTaskId() != null) {
            taskTemp.setLinkTaskId(reqDTO.getLinkTaskId());
        }
        taskTemp.setProjectId(reqDTO.getProjectId());
        taskTemp.setUserId(loginUser.getUserId());
        taskTemp.setWorkHours(reqDTO.getWorkHours());
        taskTemp.setBeginTime(reqDTO.getBeginTime());
        taskTemp.setEndTime(reqDTO.getEndTime());
        taskTemp.setDescription(description.trim());
        taskTemp.setPriTaskName(taskName.trim());
        taskTempMapper.updateTaskTemp(taskTemp);

        taskTempTagMapper.deleteByTtId(ttId);
        List<TaskTempTag> taskTempTagList = reqDTO.getTagList().stream().map(tagId -> {
            TaskTempTag taskTempTag = new TaskTempTag();
            taskTempTag.setId(snowFlakeIDHelper.nextId());
            taskTempTag.setTtId(taskTemp.getId());
            taskTempTag.setTagId(tagId);
            return taskTempTag;
        }).collect(Collectors.toList());
        taskTempTagMapper.insertBatch(taskTempTagList);
    }

    /**
     * 审核个人任务(临时)
     *
     * @param reqDTO 参数
     * @param ttId   id
     */
    @Override
    @Transactional
    public void acceptPriTaskTemp(EditPriTaskTempReqDTO reqDTO, Long ttId) {
        TaskTemp exist = taskTempMapper.selectById(ttId);
        if (exist == null) {
            throw new ZSYServiceException("个人任务临时任务不存在");
        }
        if (exist.getReviewStatus() > 1) {
            throw new ZSYServiceException("个人任务临时任务已审核或已删除,无法审核");
        }
        String taskName = reqDTO.getTaskName();
        if (Strings.isNullOrEmpty(taskName)) {
            throw new ZSYServiceException("个人任务名称不能为空");
        }
        String description = reqDTO.getDescription();
        if (Strings.isNullOrEmpty(description)) {
            throw new ZSYServiceException("任务描述不能为空");
        }
        if (reqDTO.getBeginTime().after(reqDTO.getEndTime())) {
            throw new ZSYServiceException("开始时间不能在截止时间后面");
        }
        int beginDayOfWeek = DateHelper.getCurrentWeekNumber(reqDTO.getBeginTime());
        int endDayOfWeek = DateHelper.getCurrentWeekNumber(reqDTO.getEndTime());
        if (beginDayOfWeek != endDayOfWeek) {
            throw new ZSYServiceException("个人任务不能跨周");
        }
        User user = userMapper.selectById(exist.getUserId());
        LoginInfoReqDTO loginUser = ZSYTokenRequestContext.get();
        //查询申请人有几级审核人
        List<UserCheckPeopleBO> userCheckPeopleBOS = userMapper.selectUserCheckPeopleByUserId(user.getId());
        if (CollectionUtils.isEmpty(userCheckPeopleBOS)) {
            throw new ZSYServiceException(String.format("用户未配置任务审核人,用户名: [%s]", user.getName()));
        }
        List<Long> checkUserIds = userCheckPeopleBOS.stream().map(UserCheckPeople::getCheckUserId).collect(Collectors.toList());
        if (!checkUserIds.contains(loginUser.getUserId())) {
            throw new ZSYServiceException(String.format("当前用户不是[%s]的任务审核人,无法审核", user.getName()));
        }
        // 当前审核人
        UserCheckPeopleBO curCheckUser = userCheckPeopleBOS.stream().filter(item -> item.getCheckUserId().equals(loginUser.getUserId())).findFirst().orElse(new UserCheckPeopleBO());
        if (!exist.getLevel().equals(curCheckUser.getLevel())) {
            throw new ZSYServiceException("你已审核通过,请不要重复审核");
        }

        TaskReviewLog existTaskReviewLog = taskTempMapper.selectTaskReviewLogByTaskTempAndCheckUser(ttId, loginUser.getUserId());
        if (existTaskReviewLog != null) {
            throw new ZSYServiceException("你已审核通过,请不要重复审核");
        }
        Date curDate = new Date();

        TaskTemp taskTemp = new TaskTemp();
        taskTemp.setId(ttId);
        if (reqDTO.getLinkTaskId() != null) {
            taskTemp.setLinkTaskId(reqDTO.getLinkTaskId());
        }
        taskTemp.setProjectId(reqDTO.getProjectId());
        taskTemp.setUserId(loginUser.getUserId());
        taskTemp.setWorkHours(reqDTO.getWorkHours());
        taskTemp.setBeginTime(reqDTO.getBeginTime());
        taskTemp.setEndTime(reqDTO.getEndTime());
        taskTemp.setDescription(description.trim());
        taskTemp.setPriTaskName(taskName.trim());
        taskTemp.setLevel(exist.getLevel() + 1);
        taskTemp.setTaskLevel(reqDTO.getTaskLevel());

        List<TaskTempTag> taskTempTagList = new ArrayList<>();
        reqDTO.getTagList().forEach(tagId -> {
            TaskTempTag taskTempTag = new TaskTempTag();
            taskTempTag.setId(snowFlakeIDHelper.nextId());
            taskTempTag.setTtId(taskTemp.getId());
            taskTempTag.setTagId(tagId);
            taskTempTagList.add(taskTempTag);
        });

        //插入日志
        TaskReviewLog taskReviewLog = new TaskReviewLog();
        taskReviewLog.setId(snowFlakeIDHelper.nextId());
        taskReviewLog.setCheckUserId(ZSYTokenRequestContext.get().getUserId());
        taskReviewLog.setCheckUserName(ZSYTokenRequestContext.get().getUserName());
        taskReviewLog.setLevel(curCheckUser.getLevel());
        taskReviewLog.setReviewTime(curDate);
        if (!Strings.isNullOrEmpty(reqDTO.getSuggest())) {
            taskReviewLog.setSuggest(reqDTO.getSuggest().trim());
        }
        taskReviewLog.setTaskTempId(ttId);
        //最后审核人
        Long lastCheckUserId = userCheckPeopleBOS.stream().max(Comparator.comparing(UserCheckPeople::getLevel)).orElse(new UserCheckPeopleBO()).getCheckUserId();
        if (loginUser.getUserId().equals(lastCheckUserId)) {
            // 最后一级审核,新增个人任务,周工时分配
            Task task = new Task();
            task.setId(snowFlakeIDHelper.nextId());
            task.setName(reqDTO.getTaskName().trim());
            task.setDescription(reqDTO.getDescription().trim());
            if (reqDTO.getLinkTaskId() != null) {
                task.setLinkTask(reqDTO.getLinkTaskId());
            }
            task.setProjectId(reqDTO.getProjectId());
            task.setType(ZSYTaskType.PRIVATE_TASK.getValue());
            task.setStatus(ZSYTaskStatus.DOING.getValue());
            task.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
            task.setPriority(1);
            task.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
            task.setCreateBy(user.getId());
            task.setCreateTime(curDate);
            task.setUpdateTime(curDate);
            task.setBeginTime(reqDTO.getBeginTime());
            task.setEndTime(reqDTO.getEndTime());
            task.setFacility(1);
            taskMapper.insert(task);

            taskTemp.setReviewStatus(2);
            taskTemp.setTaskId(task.getId());
            taskReviewLog.setTaskId(task.getId());

            TaskUser taskUser = new TaskUser();
            taskUser.setId(snowFlakeIDHelper.nextId());
            taskUser.setTaskId(task.getId());
            taskUser.setUserId(user.getId());
            taskUser.setTaskHours(reqDTO.getWorkHours().doubleValue());
            taskUser.setBeginTime(reqDTO.getBeginTime());
            taskUser.setEndTime(reqDTO.getEndTime());
            taskUser.setDescription(description.trim());
            taskUser.setStatus(ZSYTaskStatus.DOING.getValue());
            taskUser.setCreateTime(curDate);
            taskUser.setTaskLevel(reqDTO.getTaskLevel());
            taskUserMapper.insert(taskUser);

            UserWeek userWeek = new UserWeek();
            userWeek.setId(snowFlakeIDHelper.nextId());
            userWeek.setHours(reqDTO.getWorkHours().doubleValue());
            userWeek.setUserId(user.getId());
            userWeek.setTaskId(task.getId());
            userWeek.setWeekNumber(beginDayOfWeek);
            userWeek.setYear(DateHelper.getYears(reqDTO.getBeginTime()));
            List<UserWeek> userWeekList = new ArrayList<>();
            userWeekList.add(userWeek);
            userWeekMapper.insertList(userWeekList);

            List<TaskTag> taskTagList = taskTempTagList.stream().map(tag -> {
                TaskTag taskTag = new TaskTag();
                taskTag.setId(snowFlakeIDHelper.nextId());
                taskTag.setTaskId(task.getId());
                taskTag.setTagId(tag.getTagId());
                return taskTag;
            }).collect(Collectors.toList());
            taskTagMapper.insertList(taskTagList);
        }

        //修改临时任务
        taskTempMapper.updateTaskTemp(taskTemp);
        //删除原来的tag
        taskTempTagMapper.deleteByTtId(ttId);
        //新增tag
        taskTempTagMapper.insertBatch(taskTempTagList);
        //新增审核记录
        taskTempMapper.insertTaskReviewLog(taskReviewLog);

    }

    /**
     * 管理员审核任务
     *
     * @param editTaskTempReqDTO 参数
     */
    @Override
    @Transactional
    public void accessTaskTemp(EditTaskTempReqDTO editTaskTempReqDTO) {

        if (editTaskTempReqDTO.getTaskLevel() == null) {
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
        Integer level = 0;
        for (UserCheckPeopleBO userCheckPeopleBO : userCheckPeopleBOS) {
            if (userCheckPeopleBO.getCheckUserId().equals(ZSYTokenRequestContext.get().getUserId())) {
                level = userCheckPeopleBO.getLevel();
            }
        }
        TaskReviewLog existTaskReviewLog = taskTempMapper.selectTaskReviewLogByTaskTempAndCheckUser(editTaskTempReqDTO.getId(), ZSYTokenRequestContext.get().getUserId());
        if (existTaskReviewLog != null) {
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
        existTaskTemp.setLevel(level + 1);
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
        if (totalHours.compareTo(existTaskTemp.getWorkHours()) != 0) {
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
        if (!CollectionUtils.isEmpty(taskTempFunctionList)) {
            taskTempFunctionList.forEach(taskTempFunctionReqDTO -> {
                TaskTempFunction function = new TaskTempFunction();
                BeanUtils.copyProperties(taskTempFunctionReqDTO, function);
                function.setId(snowFlakeIDHelper.nextId());
                function.setTtId(editTaskTempReqDTO.getId());
                functionList.add(function);
            });
        }
        //删除原有的function
        functionMapper.deleteByTtId(editTaskTempReqDTO.getId());
        if (!CollectionUtils.isEmpty(functionList)) {
            functionMapper.insertBatch(functionList);
        }

        Long lastCheckUser = userMapper.selectUserLastCheckUser(userId);
        //当前情况:  不是最后一级审核
        if (!lastCheckUser.equals(ZSYTokenRequestContext.get().getUserId())) {
            taskTempMapper.insertTaskReviewLog(taskReviewLog);
            taskLogMapper.insert(taskService.buildLog(ZSYTokenRequestContext.get().getUserName() + "通过了" + user.getName() + "的多人任务审核", existTaskTemp.getDescription(), taskId));
        } else {
            taskTempMapper.insertTaskReviewLog(taskReviewLog);

            TaskDetailBO taskTemp = taskMapper.selectTaskDetailByTaskId(taskId);
            if (taskTemp == null) {
                throw new ZSYServiceException("无法找到任务,id:" + taskId);
            }
            if (taskTemp.getStatus() == ZSYTaskStatus.FINISHED.getValue() && taskTemp.getType() == ZSYTaskType.PUBLIC_TASK.getValue()) {
                throw new ZSYServiceException("该任务已结束");
            }
            Long stageId = taskTemp.getStageId();
            if (ZSYTaskStage.WAIT_DESIGN.getValue().equals(stageId)) {
                if (ZSYJobRole.DESIGN.getValue() == user.getJobRole() || ZSYJobRole.PRODUCT.getValue() == user.getJobRole()) {
                    taskTemp.setStageId(ZSYTaskStage.DESIGNING.getValue());
                }
            } else if (ZSYTaskStage.WAIT_DEVELOP.getValue().equals(stageId) && getIsDeveloper(user.getJobRole())) {
                taskTemp.setStageId(ZSYTaskStage.DEVELOPING.getValue());
            } else if (ZSYTaskStage.WAIT_TEST.getValue().equals(stageId) && ZSYJobRole.TEST.getValue() == user.getJobRole()) {
                taskTemp.setStageId(ZSYTaskStage.TESTING.getValue());
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


                List<UserWeek> userWeekList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(userWeekTempList)) {
                    List<Long> uwtIds = userWeekTempList.stream().map(UserWeekTemp::getId).collect(Collectors.toList());
                    taskTempMapper.checkUserWeekTempBatch(uwtIds);
                    userWeekTempList.forEach(userWeekTemp -> {
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
                    userWeekMapper.insertList(userWeekList);
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
                    taskUsers.forEach(taskUserBO -> {
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
                        taskUserBO.getUserWeeks().forEach(userWeek -> {
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
                userWeekList.forEach(userWeek -> {
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
     *
     * @param jobRole 角色
     */
    private boolean getIsDeveloper(Integer jobRole) {
        return jobRole == ZSYJobRole.JAVA.getValue()
                || jobRole == ZSYJobRole.C.getValue()
                || jobRole == ZSYJobRole.PHP.getValue()
                || jobRole == ZSYJobRole.FRONT.getValue()
                || jobRole == ZSYJobRole.IOS.getValue()
                || jobRole == ZSYJobRole.ANDROID.getValue()
                || jobRole == ZSYJobRole.ARTIFICIAL.getValue();
    }
}
