package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYFeedbackMapper;
import com.zhixinhuixue.armor.dao.IZSYFeedbackPlanMapper;
import com.zhixinhuixue.armor.dao.IZSYFeedbackPlanTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.FeedbackPlanBO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackReqDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackListResDTO;
import com.zhixinhuixue.armor.model.bo.FeedbackBo;
import com.zhixinhuixue.armor.model.dto.response.FeedbackPlanResDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackTaskDetailResDTO;
import com.zhixinhuixue.armor.model.pojo.Feedback;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlan;
import com.zhixinhuixue.armor.model.pojo.FeedbackPlanTask;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYFeedbackService implements IZSYFeedbackService {

    @Autowired
    private IZSYFeedbackMapper feedbackMapper;

    @Autowired
    private IZSYFeedbackPlanMapper feedbackPlanMapper;

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private IZSYFeedbackPlanTaskMapper feedbackPlanTaskMapper;

    @Autowired
    private ZSYTaskService taskService;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;


    /**
     * 需求列表
     * @param feedbackListReqDTO
     * @return
     */
    @Override
    public PageInfo<FeedbackListResDTO> getFeedback(FeedbackListReqDTO feedbackListReqDTO) {
        PageHelper.startPage(feedbackListReqDTO.getPageNum(), ZSYConstants.PAGE_SIZE);

        feedbackListReqDTO.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
        Page<FeedbackBo> feedbackBOS = feedbackMapper.selectFeedbackPage(feedbackListReqDTO);
        Page<FeedbackListResDTO> feedbackListResDTOS = new Page();
        BeanUtils.copyProperties(feedbackBOS, feedbackListResDTOS);
        feedbackBOS.stream().forEach(feedbackBO -> {
            FeedbackListResDTO feebackListResDTO = new FeedbackListResDTO();
            BeanUtils.copyProperties(feedbackBO, feebackListResDTO);
            if(feedbackBO.getUsers()==null){
                feebackListResDTO.setTaskNo(0);
            }else if(feedbackBO.getUsers().contains(",")){
                String[] users= feedbackBO.getUsers().split(",");
                feebackListResDTO.setTaskNo(users.length);
                List<String> list = Lists.newArrayList();
                for (int i=0; i<users.length; i++) {
                    if(!list.contains(users[i])) {
                        list.add(users[i]);
                    }
                }
                feebackListResDTO.setUsers(String.join(",", list));
            }else{
                feebackListResDTO.setTaskNo(1);
            }
            feedbackListResDTOS.add(feebackListResDTO);
        });
        return new PageInfo<>(feedbackListResDTOS);
    }

    @Override
    public List<String> getOrigin() {
        List<String> origin =  Lists.newArrayList(new HashSet<>(feedbackMapper.getOrigin()));
        return origin;
    }

    /**
     * 新增需求
     * @param feedbackReqDTO
     */
    @Override
    public void addFeedback(FeedbackReqDTO feedbackReqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        Feedback feedback = new Feedback();
        BeanUtils.copyProperties(feedbackReqDTO,feedback);
        feedback.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        feedback.setCreateTime(new Date());
        feedback.setUpdateTime(new Date());
        feedback.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        feedback.setStatus(ZSYFeedbackStatus.WAIT.getValue());
        feedback.setId(snowFlakeIDHelper.nextId());

        feedbackMapper.insertFeedback(feedback);
    }

    /**
     * 编辑需求
     * @param feedbackId
     * @param feedbackReqDTO
     */
    @Override
    public void editFeedback(Long feedbackId, FeedbackReqDTO feedbackReqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        BeanUtils.copyProperties(feedbackReqDTO,feedback);
        feedback.setUpdateTime(new Date());
        feedback.setId(feedbackId);

        if (feedbackMapper.updateByFeedbackId(feedback) == 0) {
            throw new ZSYServiceException("需求更新失败");
        }
    }

    /**
     * 删除需求
     * @param feedbackId
     * @return
     */
    @Override
    public void deleteFeedback(Long feedbackId) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        Feedback feedback = new Feedback();
        feedback.setId(feedbackId);
        feedback.setIsDelete(ZSYDeleteStatus.DELETED.getValue());

        if (feedbackMapper.updateByFeedbackId(feedback)== 0) {
            throw new ZSYServiceException("需求删除失败");
        }
    }

    /**
     * 添加计划
     * @param feedbackPlanReqDTO
     */
    @Override
    public void addFeedbackPlan(FeedbackPlanReqDTO feedbackPlanReqDTO) {
        if(feedbackPlanReqDTO.getPlanTask().size()<1){
            throw new ZSYServiceException("请添加计划任务");
        }

        if(feedbackPlanReqDTO.getId()==null){
            FeedbackPlan feedbackPlan = new FeedbackPlan();
            BeanUtils.copyProperties(feedbackPlanReqDTO,feedbackPlan);
            feedbackPlan.setCreateTime(new Date());
            feedbackPlan.setUpdateTime(new Date());
            feedbackPlan.setId(snowFlakeIDHelper.nextId());
            feedbackPlanReqDTO.setId(feedbackPlan.getId());
            feedbackPlanMapper.insertFeedbackPlan(feedbackPlan);
        }


        feedbackPlanReqDTO.getPlanTask().stream().forEach(taskReqDTO -> {
            if(taskReqDTO.getProjectId()==0){
                taskReqDTO.setProjectId(feedbackPlanReqDTO.getProjectId());
                Long taskId = Long.parseLong(taskService.addTask(taskReqDTO,taskReqDTO.getCreateBy()).getData().toString());

                FeedbackPlanTask feedbackPlanTask  = new FeedbackPlanTask();
                feedbackPlanTask.setId(snowFlakeIDHelper.nextId());
                feedbackPlanTask.setFeedbackPlanId(feedbackPlanReqDTO.getId());
                feedbackPlanTask.setTaskId(taskId);

                feedbackPlanTaskMapper.insertFeedbackPlanTask(feedbackPlanTask);
            }
        });

        Feedback feedback = new Feedback();
        feedback.setId(feedbackPlanReqDTO.getFeedbackId());
        feedback.setUpdateTime(new Date());
        feedback.setPriority(feedbackPlanReqDTO.getPlanTask().get(0).getPriority());
        feedback.setStatus(ZSYTaskStatus.DOING.getValue());
        if (feedbackMapper.updateByFeedbackId(feedback)== 0) {
            throw new ZSYServiceException("需求修改失败");
        }

    }

    /**
     * 计划信息
     */
    @Override
    public FeedbackPlanResDTO getFeedbackPlan(Long feedbackPlanId){
        List<FeedbackPlanBO> feedbackPlanBOS = feedbackPlanMapper.getFeedbackPlanById(feedbackPlanId);
        FeedbackPlanResDTO feedbackPlanResDTO =  new FeedbackPlanResDTO();

        List<FeedbackTaskDetailResDTO> taskDetailResDTOS = Lists.newArrayList();
        feedbackPlanBOS.stream().forEach(feedbackPlanBO -> {
            BeanUtils.copyProperties(feedbackPlanBO, feedbackPlanResDTO);
            FeedbackTaskDetailResDTO feedbackTaskDetailResDTO = new FeedbackTaskDetailResDTO();
            BeanUtils.copyProperties(feedbackPlanBO.getPlanTask(), feedbackTaskDetailResDTO);

            feedbackTaskDetailResDTO.setTaskName(feedbackPlanBO.getPlanTask().getName());
            feedbackTaskDetailResDTO.setProjectId(feedbackPlanBO.getId());//将已存在的任务ID存储为项目ID
            User user = userMapper.selectById(feedbackPlanBO.getPlanTask().getCreateBy());//在上一条查询中未查出
            feedbackTaskDetailResDTO.setUserName(user.getName());
            feedbackTaskDetailResDTO.setTaskType(ZSYTaskType.PUBLIC_TASK.getValue());

            taskDetailResDTOS.add(feedbackTaskDetailResDTO);
        });
        feedbackPlanResDTO.setPlanTask(taskDetailResDTOS);

        return feedbackPlanResDTO;
    }

}
