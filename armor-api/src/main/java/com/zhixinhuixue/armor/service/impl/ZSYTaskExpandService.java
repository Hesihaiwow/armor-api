package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTaskExpandMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskUserMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.TaskExpandBO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReqDTO;
import com.zhixinhuixue.armor.model.dto.request.TaskExpandReviewReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskExpandResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskExpandService;
import com.zhixinhuixue.armor.service.IZSYTaskService;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskExpandStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/12/7 0007.
 */
@Service
public class ZSYTaskExpandService implements IZSYTaskExpandService {

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYTaskExpandMapper taskExpandMapper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private IZSYTaskUserMapper taskUserMapper;

    @Override
    public void add(TaskExpandReqDTO expandReqDTO) {

        Task task = taskMapper.selectByPrimaryKey(expandReqDTO.getTaskId());
        if(task.getStatus() != ZSYTaskStatus.DOING.getValue()){
            throw new ZSYServiceException("任务不在进行中，请检查");
        }

        TaskExpand taskExpand  = new TaskExpand();
        taskExpand.setTeId(snowFlakeIDHelper.nextId());
        taskExpand.setUserId(ZSYTokenRequestContext.get().getUserId());
        BeanUtils.copyProperties(expandReqDTO,taskExpand);
        taskExpand.setStatus(ZSYTaskExpandStatus.PENDING.getValue());

        taskExpandMapper.insert(taskExpand);
    }

    @Override
    public TaskExpandResDTO getTaskExpandDetail(Long id) {
        TaskExpandBO expandBO = taskExpandMapper.selectExpandDetail(id);

        TaskExpandResDTO resDTO = new TaskExpandResDTO();
        BeanUtils.copyProperties(expandBO,resDTO);

        return resDTO;
    }

    @Override
    public PageInfo<TaskExpandResDTO> getExpandList(TaskExpandListReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNum(), 5);
        User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());

        Long userId = ZSYTokenRequestContext.get().getUserId();
        if(user.getUserRole() == ZSYUserRole.ADMINISTRATOR.getValue()){
            userId = null;
        }

        Page<TaskExpandBO> expandList = taskExpandMapper.getExpandList(reqDTO.getStatus(),userId);

        Page<TaskExpandResDTO> resDTOS = new Page<>();
        BeanUtils.copyProperties(expandList,resDTOS);
        expandList.forEach(taskExpandBO -> {
            TaskExpandResDTO resDTO = new TaskExpandResDTO();
            BeanUtils.copyProperties(taskExpandBO,resDTO);
            resDTOS.add(resDTO);
        });

        return new PageInfo<>(resDTOS);
    }

    @Override
    public List<TaskExpandResDTO> getExpandDoing(Integer status) {
            User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
            List<TaskExpandBO> expandList = Lists.newArrayList();
            if(user.getUserRole() == ZSYUserRole.ADMINISTRATOR.getValue()){
                expandList = taskExpandMapper.getExpandList(status,null);
            }else{
                expandList = taskExpandMapper.getExpandList(status,ZSYTokenRequestContext.get().getUserId());
            }

            List<TaskExpandResDTO> resDTOS =Lists.newArrayList();
            expandList.forEach(taskExpandBO -> {
                TaskExpandResDTO resDTO = new TaskExpandResDTO();
                BeanUtils.copyProperties(taskExpandBO,resDTO);
                resDTOS.add(resDTO);
            });

            return resDTOS;
    }

    @Override
    public void passExpand(TaskExpandReviewReqDTO reviewReqDTO) {
        TaskExpand expand = taskExpandMapper.selectById(reviewReqDTO.getTeId());
        if(expand.getStatus() != ZSYTaskExpandStatus.PENDING.getValue()){
            throw new ZSYServiceException("任务延长申请已经完成审核，请检查");
        }

        if(taskExpandMapper.reviewStatus(reviewReqDTO.getTeId(),reviewReqDTO.getStatus()) == 0){
            throw new ZSYServiceException("申请审核失败，请检查");
        }

        if(reviewReqDTO.getStatus() == ZSYTaskExpandStatus.ACCEPT.getValue()){
            //task_user表  任务时间增加
            User user = userMapper.selectById(expand.getUserId());
            BigDecimal expandHours = expand.getHours();
            if (taskExpandMapper.updateTaskHours(expandHours,expand.getEndTime(),expand.getTaskId(),expand.getUserId()) == 0){
                throw new ZSYServiceException("新增时间失败");
            }
            //新增一条任务日志
            TaskLog taskLog = new TaskLog();
            taskLog.setId(snowFlakeIDHelper.nextId());
            taskLog.setTaskId(expand.getTaskId());
            String title = ZSYTokenRequestContext.get().getUserName()+"修改了任务";
            taskLog.setTitle(title);
            String content = user.getName()+"延长了任务时间"+expand.getHours()+"小时,原因:"+expand.getReason();
            taskLog.setContent(content);
            taskLog.setCreateTime(new Date());
            taskLog.setUserId(ZSYTokenRequestContext.get().getUserId());
            taskLog.setUserName(ZSYTokenRequestContext.get().getUserName());
            taskExpandMapper.insertTaskLog(taskLog);
        }



    }
}

