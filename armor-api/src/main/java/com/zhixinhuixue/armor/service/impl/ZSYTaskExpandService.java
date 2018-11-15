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
import com.zhixinhuixue.armor.model.dto.response.TaskExpandResDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskExpand;
import com.zhixinhuixue.armor.model.pojo.TaskUser;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.service.IZSYTaskExpandService;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskExpandStatus;
import com.zhixinhuixue.armor.source.enums.ZSYTaskStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void passExpand(Long id,Integer status) {
        TaskExpand expand = taskExpandMapper.selectById(id);
        if(expand.getStatus() != ZSYTaskExpandStatus.PENDING.getValue()){
            throw new ZSYServiceException("任务延长申请已经完成审核，请检查");
        }

        if(taskExpandMapper.reviewStatus(id,status) == 0){
            throw new ZSYServiceException("申请审核失败，请检查");
        }

        if(status == ZSYTaskExpandStatus.ACCEPT.getValue()){

        }



    }
}

