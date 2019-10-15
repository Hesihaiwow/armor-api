package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.TaskBugBO;
import com.zhixinhuixue.armor.model.bo.TaskBugRemarkBO;
import com.zhixinhuixue.armor.model.dto.request.AddTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.QueryTaskBugPageReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYTaskBugService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.TaskBugStatus;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author sch
 * @time 2019/10/14 10:22
 */
@Service
public class ZSYTaskBugService implements IZSYTaskBugService {
    @Autowired
    private IZSYTaskBugMapper taskBugMapper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private IZSYTaskBugLogMapper bugLogMapper;
    @Autowired
    private IZSYTaskBugRemarkMapper remarkMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 分页查看任务bug
     * @param reqDTO 条件
     */
    @Override
    public PageInfo<TaskBugPageResDTO> getTaskBugPage(QueryTaskBugPageReqDTO reqDTO) {
        Integer selectAll = reqDTO.getSelectAll();
        Long userId = ZSYTokenRequestContext.get().getUserId();
        Page<TaskBugBO> taskBugBOS = new Page<>();
        Page<TaskBugPageResDTO> page = new Page<>();
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        //查看当前用户的bug
        if (selectAll == 0){
            taskBugBOS = taskBugMapper.selectPage(reqDTO,userId);
        }else {
            taskBugBOS = taskBugMapper.selectPage(reqDTO,null);
        }
        BeanUtils.copyProperties(taskBugBOS,page);
        if (!CollectionUtils.isEmpty(taskBugBOS)){
            taskBugBOS.forEach(taskBugBO -> {
                TaskBugPageResDTO resDTO = new TaskBugPageResDTO();
                BeanUtils.copyProperties(taskBugBO,resDTO);
                resDTO.setStatusName(TaskBugStatus.getName(taskBugBO.getStatus()));
                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 新增
     * @param reqDTO 条件
     */
    @Override
    @Transactional
    public void addTaskBug(AddTaskBugReqDTO reqDTO) {
        Task task = taskMapper.selectByPrimaryKey(reqDTO.getTaskId());
        if (task == null){
            throw new ZSYServiceException("任务不存在,请检查");
        }
        TaskBug taskBug = new TaskBug();
        BeanUtils.copyProperties(reqDTO,taskBug);
        Long userId = ZSYTokenRequestContext.get().getUserId();
        String userName = ZSYTokenRequestContext.get().getUserName();
//        User user = userMapper.selectById(userId);

        taskBug.setTbId(snowFlakeIDHelper.nextId());
        taskBug.setCreateBy(userId);
        taskBug.setCreateTime(new Date());
        taskBug.setUpdateBy(userId);
        taskBug.setUpdateTime(new Date());
        taskBug.setStatus(TaskBugStatus.ASSIGNED.getValue());
        taskBug.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        //新增任务bug
        if (taskBugMapper.insert(taskBug) == 0){
            throw new ZSYServiceException("新增任务bug失败");
        }

        TaskBugLog taskBugLog = new TaskBugLog();
        taskBugLog.setTblId(snowFlakeIDHelper.nextId());
        taskBugLog.setTbId(taskBug.getTbId());
        taskBugLog.setContent(userName+"创建了bug,tbId: "+taskBug.getTbId());
        taskBugLog.setCreateBy(userId);
        taskBugLog.setCreateTime(new Date());
        //插入日志
        bugLogMapper.insert(taskBugLog);
    }

    /**
     * 修改
     * @param reqDTO 条件
     */
    @Override
    @Transactional
    public void updateTaskBug(EditTaskBugReqDTO reqDTO) {
        TaskBug taskBug = taskBugMapper.selectById(reqDTO.getTbId());
        if (taskBug == null){
            throw new ZSYServiceException("任务bug不存在,请检查");
        }
        Long createBy = taskBug.getCreateBy();
        Long userId = ZSYTokenRequestContext.get().getUserId();
        String userName = ZSYTokenRequestContext.get().getUserName();
//        User user = userMapper.selectById(userId);
        //当前用户为bug创建人,  可修改全部内容
        if (createBy.equals(userId)){
            taskBug.setStatus(reqDTO.getStatus());
            taskBug.setTitle(reqDTO.getTitle().trim());
            taskBug.setDescription(reqDTO.getDescription());
            taskBug.setFrequency(reqDTO.getFrequency());
            taskBug.setSeverity(reqDTO.getSeverity());
            taskBug.setHandlerId(reqDTO.getHandlerId());
        }else {
            taskBug.setStatus(reqDTO.getStatus());
        }
        taskBug.setUpdateTime(new Date());
        taskBug.setUpdateBy(userId);
        if (taskBugMapper.updateById(taskBug) == 0){
            throw new ZSYServiceException("修改任务bug失败");
        }

        if (reqDTO.getRemark() != null){
            TaskBugRemark taskBugRemark = new TaskBugRemark();
            taskBugRemark.setTbrId(snowFlakeIDHelper.nextId());
            taskBugRemark.setTbId(taskBug.getTbId());
            taskBugRemark.setRemark(reqDTO.getRemark());
            taskBugRemark.setCreateTime(new Date());
            taskBugRemark.setCreateBy(userId);
            remarkMapper.insert(taskBugRemark);
        }

        //插入日志
        TaskBugLog taskBugLog = new TaskBugLog();
        taskBugLog.setTblId(snowFlakeIDHelper.nextId());
        taskBugLog.setTbId(taskBug.getTbId());
        taskBugLog.setContent(userName+"修改了任务bug,tbId: "+taskBug.getTbId());
        taskBugLog.setCreateTime(new Date());
        taskBugLog.setCreateBy(userId);
        bugLogMapper.insert(taskBugLog);
    }

    /**
     * 删除
     * @param tbId  bugId
     */
    @Override
    @Transactional
    public void deleteTaskBug(Long tbId) {
        TaskBug taskBug = taskBugMapper.selectById(tbId);
        if (taskBug == null){
            throw new ZSYServiceException("任务bug不存在,请检查");
        }
        Long userId = ZSYTokenRequestContext.get().getUserId();
        String userName = ZSYTokenRequestContext.get().getUserName();
        if (!userId.equals(taskBug.getCreateBy())){
            throw new ZSYServiceException("当前用户不是bug创建人,无法删除");
        }
        taskBug.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        taskBugMapper.updateById(taskBug);

        //插入日志
        TaskBugLog taskBugLog = new TaskBugLog();
        taskBugLog.setTblId(snowFlakeIDHelper.nextId());
        taskBugLog.setTbId(taskBug.getTbId());
        taskBugLog.setContent(userName+"删除了任务bug,tbId: "+taskBug.getTbId());
        taskBugLog.setCreateTime(new Date());
        taskBugLog.setCreateBy(userId);
        bugLogMapper.insert(taskBugLog);
    }

    /**
     * 查看详情
     * @param tbId bugId
     */
    @Override
    public TaskBugDetailResDTO getTaskBugDetail(Long tbId) {
        TaskBugDetailResDTO resDTO = new TaskBugDetailResDTO();
        TaskBugBO taskBugBO = taskBugMapper.selectDetailById(tbId);
        if (taskBugBO == null){
            throw new ZSYServiceException("任务bug不存在,请检查");
        }
        BeanUtils.copyProperties(taskBugBO,resDTO);
        Long userId = ZSYTokenRequestContext.get().getUserId();
        if (taskBugBO.getCreateBy().equals(userId)){
            resDTO.setIsCreater(1);
        }else {
            resDTO.setIsCreater(0);
        }
        resDTO.setStatusName(TaskBugStatus.getName(taskBugBO.getStatus()));

        //查询备注
        List<TaskBugRemarkBO> remarkBOS = remarkMapper.selectListByTbId(tbId);
        List<TaskBugRemarkResDTO> remarkResDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(remarkBOS)){
            remarkBOS.forEach(remarkBO->{
                TaskBugRemarkResDTO remarkResDTO = new TaskBugRemarkResDTO();
                BeanUtils.copyProperties(remarkBO,remarkResDTO);
                remarkResDTOS.add(remarkResDTO);
            });
        }
        resDTO.setRemarkResDTOS(remarkResDTOS);
        //查询日志
        List<TaskBugLog> logs = bugLogMapper.selectLogListByTbId(tbId);
        List<TaskBugLogResDTO> logResDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(logs)){
            logs.forEach(log->{
                TaskBugLogResDTO logResDTO = new TaskBugLogResDTO();
                BeanUtils.copyProperties(log,logResDTO);
                logResDTOS.add(logResDTO);
            });
        }
        resDTO.setLogResDTOList(logResDTOS);
        return resDTO;
    }

    /**
     * 查询任务人员
     * @param taskId 任务id
     */
    @Override
    public List<EffectUserResDTO> getTaskUsers(Long taskId) {
        List<EffectUserResDTO> list = new ArrayList<>();
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task == null){
            throw new ZSYServiceException("任务不存在,请检查");
        }
        List<User> users = userMapper.selectTaskUsers(taskId);
        if (!CollectionUtils.isEmpty(users)){
            users.forEach(user -> {
                EffectUserResDTO resDTO = new EffectUserResDTO();
                resDTO.setId(user.getId());
                resDTO.setName(user.getName());
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 查询任务数量
     * @param reqDTO 条件
     */
    @Override
    public TaskBugNumResDTO getTaskBugNum(QueryTaskBugPageReqDTO reqDTO) {
        TaskBugNumResDTO resDTO = new TaskBugNumResDTO();
        Long userId = ZSYTokenRequestContext.get().getUserId();
        Integer selectAll = reqDTO.getSelectAll();

        Integer totalNum = 0;
        Integer notSolvedNum = 0;
        Integer solvedNum = 0;
        Integer closedNum = 0;
        if (selectAll == 0){
            totalNum = taskBugMapper.selectTaskBugNum(1,userId);
            notSolvedNum = taskBugMapper.selectTaskBugNum(2,userId);
            solvedNum = taskBugMapper.selectTaskBugNum(3,userId);
            closedNum = taskBugMapper.selectTaskBugNum(4,userId);
        }else {
            totalNum = taskBugMapper.selectTaskBugNum(1,null);
            notSolvedNum = taskBugMapper.selectTaskBugNum(2,null);
            solvedNum = taskBugMapper.selectTaskBugNum(3,null);
            closedNum = taskBugMapper.selectTaskBugNum(4,null);
        }
        resDTO.setTotalNum(totalNum);
        resDTO.setClosedNum(closedNum);
        resDTO.setNotSolvedNum(notSolvedNum);
        resDTO.setSolvedNum(solvedNum);
        return resDTO;
    }
}
