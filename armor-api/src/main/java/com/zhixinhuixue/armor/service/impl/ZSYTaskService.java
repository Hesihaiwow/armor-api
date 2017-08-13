package com.zhixinhuixue.armor.service.impl;

import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYGlobeException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.TaskBO;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.dto.request.CommentReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskCompleteReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.model.pojo.UserIntegral;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    private static final Logger logger = LoggerFactory.getLogger(ZSYGlobeException.class);

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
    public ZSYResult addTask(TaskReqDTO taskReqDTO) {
        checkUser();
        Task task = new Task();
        task.setId(snowFlakeIDHelper.nextId());
        task.setName(taskReqDTO.getTaskName());
        task.setDescription(taskReqDTO.getDescription());
        task.setProjectId(taskReqDTO.getProjectId());
        task.setEndTime(taskReqDTO.getEndTime());
        task.setPriority(taskReqDTO.getPriority());
        task.setStatus(ZSYTaskStatus.DOING.getValue());
        task.setType(taskReqDTO.getTaskType());
        task.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
        task.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        // 插入新任务
        taskMapper.insert(task);
        // 插入任务用户
        if (taskReqDTO.getTaskUsers() != null && taskReqDTO.getTaskUsers().size() > 0) {
            List<TaskUser> taskUsers = Lists.newArrayList();
            taskReqDTO.getTaskUsers().forEach(user -> {
                TaskUser taskUser = new TaskUser();
                taskUser.setId(snowFlakeIDHelper.nextId());
                taskUser.setTaskId(task.getId());
                taskUser.setUserId(user.getUserId());
                taskUser.setStageId(user.getStageId());
                taskUser.setDescription(user.getDescription());
                taskUser.setBeginTime(user.getBeginTime());
                taskUser.setEndTime(user.getEndTime());
                taskUser.setTaskHours(user.getTaskHours());
                taskUser.setCreateTime(new Date());
                taskUser.setStatus(ZSYTaskUserStatus.DOING.getValue());
                taskUsers.add(taskUser);
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
        taskLogMapper.insert(buildLog("创建了任务", task.getName(), task.getId()));
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
    public ZSYResult auditTask(Long taskId, Integer auditStatus) {
        checkUser();
        Task taskTemp = taskMapper.selectByPrimaryKey(taskId);
        if (taskTemp == null) {
            logger.warn("无法找到任务,id:{}", taskId);
            throw new ZSYServiceException("无法找到任务,id:" + taskId);
        }
        if (taskTemp.getReviewStatus() != null && (taskTemp.getReviewStatus() == ZSYReviewStatus.REJECT.getValue()
                || taskTemp.getReviewStatus() == ZSYReviewStatus.ACCEPT.getValue())) {
            logger.warn("该任务已被审核,id:{}", ZSYReviewStatus.getName(auditStatus), taskId);
            throw new ZSYServiceException("该任务已被审核");
        }
        Task task = new Task();
        task.setId(taskId);
        task.setReviewStatus(auditStatus);
        task.setUpdateTime(new Date());
        taskMapper.updateByPrimaryKeySelective(task);
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
        userIntegral.setIntegral(ZSYIntegral.A.getValue());
        userIntegral.setCreateTime(new Date());
        userIntegralMapper.insert(userIntegral);

        // 修改用户积分
        int currentIntegral = userTemp.getIntegral();
        User user = new User();
        user.setId(ZSYTokenRequestContext.get().getUserId());
        user.setIntegral(currentIntegral + ZSYIntegral.A.getValue());
        userMapper.updateSelectiveById(user);
        return ZSYResult.success();
    }

    /**
     * 完成多人任务中我的任务
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

        // copy 任务标签
        List<TaskTagResDTO> taskTagResDTOS = new ArrayList<>();
        taskDetailBO.getTaskTags().stream().forEach(tag -> {
            TaskTagResDTO taskTagResDTO = new TaskTagResDTO();
            BeanUtils.copyProperties(tag, taskTagResDTO, "id", "createTime", "createBy");
            taskTagResDTOS.add(taskTagResDTO);
        });
        taskDetailResDTO.setTags(taskTagResDTOS);

        // copy 任务阶段
        List<TaskUserResDTO> taskUserResDTOS = new ArrayList<>();
        taskDetailBO.getTaskUsers().stream().forEach(taskUserBO -> {
            TaskUserResDTO taskUserResDTO = new TaskUserResDTO();
            BeanUtils.copyProperties(taskUserBO, taskUserResDTO);
            // copy 评价
            List<TaskCommentResDTO> taskCommentResDTOS = new ArrayList<>();
            taskUserBO.getTaskComments().stream().forEach(taskCommentBO -> {
                TaskCommentResDTO taskCommentResDTO = new TaskCommentResDTO();
                BeanUtils.copyProperties(taskCommentBO, taskCommentResDTO, "taskId", "taskUserId");
                taskCommentResDTOS.add(taskCommentResDTO);
            });
            taskUserResDTO.setComments(taskCommentResDTOS);
            taskUserResDTOS.add(taskUserResDTO);
        });
        taskDetailResDTO.setUsers(taskUserResDTOS);
        return ZSYResult.success().data(taskDetailResDTO);
    }

    /**
     * 按状态查询任务
     *
     * @return
     */
    @Override
    public ZSYResult<List<TaskResDTO>> getTaskByStatus(Integer status, Integer reviewStatus) {
        List<TaskBO> taskBOS = taskMapper.selectTaskByStatus(status, reviewStatus, ZSYTokenRequestContext.get().getUserId());
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
     * 获取已完成&已评价的任务，包含积分
     *
     * @return
     */
    @Override
    public ZSYResult<List<TaskResDTO>> getFinishedTask() {
        List<TaskBO> taskBOS = taskMapper.selectFinishedTask(ZSYTokenRequestContext.get().getUserId());
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
     * 完成主任务
     *
     * @param taskId
     * @return
     */
    @Override
    public ZSYResult completeMasterTask(Long taskId) {
        checkUser();
        // TODO: 2017/8/12 权限验证
        TaskDetailBO taskDetailBO = taskMapper.selectTaskDetailByTaskId(taskId);
        // 验证子任务是否都已经完成了
        if (taskDetailBO != null && taskDetailBO.getTaskUsers() != null && taskDetailBO.getTaskUsers().size() > 0) {
            long count = taskDetailBO.getTaskUsers().stream().filter(taskUserBO -> taskUserBO.getStatus() == ZSYTaskUserStatus.COMPLETED.getValue()).count();
            if (count == taskDetailBO.getTaskUsers().size()) {
                Task task = new Task();
                task.setId(taskId);
                task.setStatus(ZSYTaskStatus.COMPLETED.getValue());
                task.setUpdateTime(new Date());
                // 将任务状态改为已完成（待评价）
                taskMapper.updateByPrimaryKeySelective(task);
                // 插入日志
                taskLogMapper.insert(buildLog("阶段全部完成", "将全部阶段标记为已完成", taskId));
                return ZSYResult.success();
            }
        }
        return ZSYResult.fail().msg("任务阶段未完成");
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
        if (taskDetailBO.getType() == ZSYTaskType.PRIVATE_TASK.getValue()) {
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
        int sum = commentedNum.stream().mapToInt(i -> i.intValue()).sum();
        commentCompleted = sum == ((userIds.size() - 1) * taskDetailBO.getTaskUsers().size());
        if (commentCompleted || taskDetailBO.getStatus() == ZSYTaskStatus.FINISHED.getValue()) {
            throw new ZSYServiceException("任务已结束");
        }
        if (commentReqDTO.getComments() == null || commentReqDTO.getComments().size() == 0) {
            throw new ZSYServiceException("评价不能为空");
        }
        List<TaskComment> commentList = new ArrayList<>();
        commentReqDTO.getComments().stream().forEach(commentSalveDTO -> {
            Integer value = ZSYIntegral.getValue(commentSalveDTO.getGrade());
            if (value == null) {
                throw new ZSYServiceException("无法找到评价等级:" + commentSalveDTO.getGrade());
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
        return ZSYResult.success();
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
