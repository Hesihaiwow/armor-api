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
import com.zhixinhuixue.armor.source.enums.TaskBugFrequency;
import com.zhixinhuixue.armor.source.enums.TaskBugSeverity;
import com.zhixinhuixue.armor.source.enums.TaskBugStatus;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
//        Integer selectAll = reqDTO.getSelectAll();
//        Long userId = ZSYTokenRequestContext.get().getUserId();
        Page<TaskBugBO> taskBugBOS = new Page<>();
        Page<TaskBugPageResDTO> page = new Page<>();
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        //查看当前用户的bug
//        if (selectAll == 0){
            taskBugBOS = taskBugMapper.selectPage(reqDTO,null);
//        }else {
//            taskBugBOS = taskBugMapper.selectPage(reqDTO,null);
//        }
        BeanUtils.copyProperties(taskBugBOS,page);
        if (!CollectionUtils.isEmpty(taskBugBOS)){
            taskBugBOS.forEach(taskBugBO -> {
                TaskBugPageResDTO resDTO = new TaskBugPageResDTO();
                String bugNoStr = getBugNoStr(taskBugBO.getTbNo());
                BeanUtils.copyProperties(taskBugBO,resDTO);
                resDTO.setTbNoStr(bugNoStr);
                resDTO.setSeverityName(TaskBugSeverity.getName(taskBugBO.getSeverity()));
                resDTO.setStatusName(TaskBugStatus.getName(taskBugBO.getStatus()));
                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 获取bug编号
     */
    private String getBugNoStr(Integer bugNo){
        String bugNoStr = "";
        if (bugNo != null){
            if (bugNo<10){
                bugNoStr = "000000"+bugNo;
            }else if (bugNo<100){
                bugNoStr = "00000"+bugNo;
            }else if (bugNo<1000){
                bugNoStr = "0000"+bugNo;
            }else if (bugNo<10000){
                bugNoStr = "000"+bugNo;
            }else if (bugNo<100000){
                bugNoStr = "00"+bugNo;
            }else if (bugNo<1000000){
                bugNoStr = "0"+bugNo;
            }else {
                bugNoStr = ""+bugNo;
            }
        }
        return bugNoStr;
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
        Integer lastBugNo = taskBugMapper.selectLastBugNo();
        TaskBug taskBug = new TaskBug();
        BeanUtils.copyProperties(reqDTO,taskBug);
        Long userId = ZSYTokenRequestContext.get().getUserId();
        String userName = ZSYTokenRequestContext.get().getUserName();
//        User user = userMapper.selectById(userId);

        taskBug.setTbId(snowFlakeIDHelper.nextId());
        if (lastBugNo == null){
            taskBug.setTbNo(1);
        }else {
            taskBug.setTbNo(lastBugNo+1);
        }
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
//        Long createBy = taskBug.getCreateBy();
        Long userId = ZSYTokenRequestContext.get().getUserId();
//        String userName = ZSYTokenRequestContext.get().getUserName();
//        User user = userMapper.selectById(userId);
        //当前用户为bug创建人,  可修改全部内容
//        if (createBy.equals(userId)){
            taskBug.setStatus(reqDTO.getStatus());
            taskBug.setTitle(reqDTO.getTitle().trim());
            taskBug.setDescription(reqDTO.getDescription());
            taskBug.setFrequency(reqDTO.getFrequency());
            taskBug.setSeverity(reqDTO.getSeverity());
            taskBug.setHandlerId(reqDTO.getHandlerId());
//        }else {
//            taskBug.setStatus(reqDTO.getStatus());
//        }
        taskBug.setUpdateTime(new Date());
        taskBug.setUpdateBy(userId);
        if (taskBugMapper.updateById(taskBug) == 0){
            throw new ZSYServiceException("修改任务bug失败");
        }

//        if (reqDTO.getRemark() != null && reqDTO.getRemark().trim() != ""){
//            TaskBugRemark taskBugRemark = new TaskBugRemark();
//            taskBugRemark.setTbrId(snowFlakeIDHelper.nextId());
//            taskBugRemark.setTbId(taskBug.getTbId());
//            taskBugRemark.setRemark(reqDTO.getRemark());
//            taskBugRemark.setCreateTime(new Date());
//            taskBugRemark.setCreateBy(userId);
//            remarkMapper.insert(taskBugRemark);
//        }
//
//        //插入日志
//        TaskBugLog taskBugLog = new TaskBugLog();
//        taskBugLog.setTblId(snowFlakeIDHelper.nextId());
//        taskBugLog.setTbId(taskBug.getTbId());
//        taskBugLog.setContent(userName+"修改了任务bug,tbId: "+taskBug.getTbId());
//        taskBugLog.setCreateTime(new Date());
//        taskBugLog.setCreateBy(userId);
//        bugLogMapper.insert(taskBugLog);
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
//        String userName = ZSYTokenRequestContext.get().getUserName();
        if (!userId.equals(taskBug.getCreateBy())){
            throw new ZSYServiceException("当前用户不是bug创建人,无法删除");
        }
        taskBug.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        taskBugMapper.updateById(taskBug);
        //删除备注
//        remarkMapper.deleteByTbId(tbId);
        //插入日志
//        TaskBugLog taskBugLog = new TaskBugLog();
//        taskBugLog.setTblId(snowFlakeIDHelper.nextId());
//        taskBugLog.setTbId(taskBug.getTbId());
//        taskBugLog.setContent(userName+"删除了任务bug,tbId: "+taskBug.getTbId());
//        taskBugLog.setCreateTime(new Date());
//        taskBugLog.setCreateBy(userId);
//        bugLogMapper.insert(taskBugLog);
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
        resDTO.setSeverityName(TaskBugSeverity.getName(taskBugBO.getSeverity()));
        resDTO.setFrequencyName(TaskBugFrequency.getName(taskBugBO.getFrequency()));
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
        Long taskId = reqDTO.getTaskId();
        Integer totalNum = 0;
        Integer notSolvedNum = 0;
        Integer solvedNum = 0;
        Integer closedNum = 0;
        if (selectAll == 0){
            totalNum = taskBugMapper.selectTaskBugNum(1,userId,taskId);
            notSolvedNum = taskBugMapper.selectTaskBugNum(2,userId,taskId);
            solvedNum = taskBugMapper.selectTaskBugNum(3,userId,taskId);
            closedNum = taskBugMapper.selectTaskBugNum(4,userId,taskId);
        }else {
            totalNum = taskBugMapper.selectTaskBugNum(1,null,taskId);
            notSolvedNum = taskBugMapper.selectTaskBugNum(2,null,taskId);
            solvedNum = taskBugMapper.selectTaskBugNum(3,null,taskId);
            closedNum = taskBugMapper.selectTaskBugNum(4,null,taskId);
        }
        resDTO.setTotalNum(totalNum);
        resDTO.setClosedNum(closedNum);
        resDTO.setNotSolvedNum(notSolvedNum);
        resDTO.setSolvedNum(solvedNum);
        return resDTO;
    }

    /**
     * 个人主页显示bug分页
     * @param pageNum 页码
     */
    @Override
    public PageInfo<TaskBugPageResDTO> getPersonalBugPage(Integer pageNum) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        QueryTaskBugPageReqDTO reqDTO = new QueryTaskBugPageReqDTO();
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<TaskBugBO> taskBugBOS = taskBugMapper.selectPage(reqDTO,userId);
        Page<TaskBugPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(taskBugBOS,page);
        if (!CollectionUtils.isEmpty(taskBugBOS)){
            taskBugBOS.forEach(taskBugBO -> {
                TaskBugPageResDTO resDTO = new TaskBugPageResDTO();
                BeanUtils.copyProperties(taskBugBO,resDTO);
                resDTO.setStatusName(TaskBugStatus.getName(taskBugBO.getStatus()));
                resDTO.setSeverityName(TaskBugSeverity.getName(taskBugBO.getSeverity()));
                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 查询已经产生bug的任务
     * @author sch
     */
    @Override
    public List<TaskBaseResDTO> getReadyTasks() {
        List<TaskBaseResDTO> list = new ArrayList<>();
        List<Task> taskList = taskMapper.selectBugTasks();
        if (!CollectionUtils.isEmpty(taskList)){
            taskList.forEach(task -> {
                TaskBaseResDTO resDTO = new TaskBaseResDTO();
                resDTO.setId(task.getId());
                resDTO.setName(task.getName());
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 我的bug视图
     * @author sch
     */
    @Override
    public MyBugResDTO getMyBugList() {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        //查询我的已分派未解决bug
        List<TaskBugBO> assignedBugs = taskBugMapper.selectMyBugList(TaskBugStatus.ASSIGNED.getValue(),userId,null);
        //查询我的已解决bug
        List<TaskBugBO> solvedBugs = taskBugMapper.selectMyBugList(TaskBugStatus.RESOLVED.getValue(), userId, null);
        //查询我提交的bug
        List<TaskBugBO> reportBugs = taskBugMapper.selectMyBugList(null, null, userId);
        //查询我的已分派未解决bug数量
        Integer solvingNum = taskBugMapper.selectTaskBugNumByStatus(userId,TaskBugStatus.ASSIGNED.getValue(),null);
        Integer solvedNum = taskBugMapper.selectTaskBugNumByStatus(userId,TaskBugStatus.RESOLVED.getValue(),null);
        Integer reportNum = taskBugMapper.selectTaskBugNumByStatus(null,null,userId);
        MyBugResDTO resDTO = new MyBugResDTO();
        resDTO.setSolvedBugNum(solvedNum);
        resDTO.setSolvingBugNum(solvingNum);
        resDTO.setReportBugNum(reportNum);
        List<TaskBugPageResDTO> solvingBugList = getBugList(assignedBugs);
        List<TaskBugPageResDTO> solvedBugList = getBugList(solvedBugs);
        List<TaskBugPageResDTO> reportBugList = getBugList(reportBugs);
        resDTO.setSolvingBugList(solvingBugList);
        resDTO.setSolvedBugList(solvedBugList);
        resDTO.setReportBugList(reportBugList);
        return resDTO;
    }

    /**
     * 测试相关阶段的任务
     * @author sch
     */
    @Override
    public List<TaskBaseResDTO> getTaskTesting() {
        List<Task> taskList = taskMapper.selectTestTask();
        List<TaskBaseResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskList)){
            taskList.forEach(task -> {
                TaskBaseResDTO resDTO = new TaskBaseResDTO();
                resDTO.setId(task.getId());
                resDTO.setName(task.getName());
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 查询bug报告员
     * @author sch
     */
    @Override
    public List<EffectUserResDTO> getBugReporters() {
        List<EffectUserResDTO> list = new ArrayList<>();
        List<User> users = userMapper.selectBugReporters();
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
     * 查询bug分派员
     * @author sch
     */
    @Override
    public List<EffectUserResDTO> getBugHandlers() {
        List<EffectUserResDTO> list = new ArrayList<>();
        List<User> users = userMapper.selectBugHandlers();
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
     * 获取任务bug集合
     * @param taskBugs 参数
     */
    private List<TaskBugPageResDTO> getBugList(List<TaskBugBO> taskBugs) {
        List<TaskBugPageResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskBugs)){
            taskBugs.forEach(taskBug -> {
                TaskBugPageResDTO bugResDTO = new TaskBugPageResDTO();
                bugResDTO.setTbId(taskBug.getTbId());
                bugResDTO.setTitle(taskBug.getTitle());
                bugResDTO.setTbNoStr(getBugNoStr(taskBug.getTbNo()));
                bugResDTO.setTaskId(taskBug.getTaskId());
                bugResDTO.setTaskName(taskBug.getTaskName());
                bugResDTO.setCreateTime(taskBug.getCreateTime());
                bugResDTO.setStatus(taskBug.getStatus());
                bugResDTO.setStatusName(TaskBugStatus.getName(taskBug.getStatus()));
                list.add(bugResDTO);
            });
        }
        return list;
    }
}
