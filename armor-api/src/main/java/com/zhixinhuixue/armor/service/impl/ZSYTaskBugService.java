package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.AddTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugStatReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTaskBugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.QueryTaskBugPageReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskBug;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.service.IZSYTaskBugService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.pagehelper.page.PageMethod.startPage;

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
        Page<TaskBugBO> taskBugBOS;
        Page<TaskBugPageResDTO> page = new Page<>();
        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        taskBugBOS = taskBugMapper.selectPage(reqDTO,null);
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
        Long userId = ZSYTokenRequestContext.get().getUserId();
        taskBug.setStatus(reqDTO.getStatus());
        taskBug.setTitle(reqDTO.getTitle().trim());
        taskBug.setDescription(reqDTO.getDescription());
        taskBug.setFrequency(reqDTO.getFrequency());
        taskBug.setSeverity(reqDTO.getSeverity());
        taskBug.setHandlerId(reqDTO.getHandlerId());
        taskBug.setTaskId(reqDTO.getTaskId());
        taskBug.setProblemType(reqDTO.getProblemType());
        taskBug.setUpdateTime(new Date());
        taskBug.setUpdateBy(userId);
        if (taskBugMapper.updateById(taskBug) == 0){
            throw new ZSYServiceException("修改任务bug失败");
        }
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
        if (!userId.equals(taskBug.getCreateBy())){
            throw new ZSYServiceException("当前用户不是bug创建人,无法删除");
        }
        taskBug.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        taskBugMapper.updateById(taskBug);
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
        resDTO.setProblemTypeName(TaskBugType.getName(taskBugBO.getProblemType()));
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
        startPage(Optional.ofNullable(pageNum).orElse(1),ZSYConstants.PAGE_SIZE);
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
        Long userId = ZSYTokenRequestContext.get().getUserId();
        List<BugTestTaskBO> taskList = taskBugMapper.selectBugTestTaskList(userId);
        List<TaskBaseResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskList)){
            taskList.forEach(task -> {
                TaskBaseResDTO resDTO = new TaskBaseResDTO();
                resDTO.setId(task.getTaskId());
                resDTO.setName(task.getTaskName());
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
     * 我的视图跳转到查看问题对应的列表
     * @author sch
     * @param userId 用户id
     * @param status 查询状态
     * @param pageNum 页码
     */
    @Override
    public PageInfo<TaskBugPageResDTO> getCustomizedPage(Long userId, Integer status, Integer pageNum) {
        if (userId == null){
            throw new ZSYServiceException("指定用户为空");
        }
        Page<TaskBugBO> taskBugBOS = null;
        startPage(Optional.ofNullable(pageNum).orElse(1),ZSYConstants.PAGE_SIZE);
        if (status == 1 || status == 2){
            //1: 查询已分配的bug   2:查询已解决的bug
            taskBugBOS = taskBugMapper.selectAssignedOrSolved(userId,status);
        }else if (status == 3){
            //3: 查询我报告的bug
            taskBugBOS = taskBugMapper.selectMyReportBug(userId);
        }
        Page<TaskBugPageResDTO> list = new Page<>();
        BeanUtils.copyProperties(taskBugBOS,list);
        if (!CollectionUtils.isEmpty(taskBugBOS)){
            taskBugBOS.forEach(taskBugBO -> {
                TaskBugPageResDTO resDTO = new TaskBugPageResDTO();
                String bugNoStr = getBugNoStr(taskBugBO.getTbNo());
                BeanUtils.copyProperties(taskBugBO,resDTO);
                resDTO.setTbNoStr(bugNoStr);
                resDTO.setSeverityName(TaskBugSeverity.getName(taskBugBO.getSeverity()));
                resDTO.setStatusName(TaskBugStatus.getName(taskBugBO.getStatus()));
                list.add(resDTO);
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * bug状态饼形图
     */
    @Override
    public List<TaskBugStatusPieResDTO> getBugStatusPie(BugStatReqDTO reqDTO) {
        Date yearAndMonth = reqDTO.getYearAndMonth();
        String yearMonth = "";
        if (yearAndMonth != null){
            yearMonth = new SimpleDateFormat("yyyy-MM").format(yearAndMonth);
        }
        List<TaskBugStatusPieBO> statusPieBOS = taskBugMapper.selectBugStatusPie(yearMonth);
        List<TaskBugStatusPieResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(statusPieBOS)){
            list = statusPieBOS.stream().map(item->{
                TaskBugStatusPieResDTO resDTO = new TaskBugStatusPieResDTO();
                resDTO.setStatusName(TaskBugStatus.getName(item.getStatus()));
                resDTO.setNumber(item.getNumber());
                return resDTO;
            }).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * bug类型分类饼形图
     */
    @Override
    public List<TaskBugTypePieResDTO> getBugTypePie(BugStatReqDTO reqDTO) {
        Date yearAndMonth = reqDTO.getYearAndMonth();
        String yearMonth = "";
        if (yearAndMonth != null){
            yearMonth = new SimpleDateFormat("yyyy-MM").format(yearAndMonth);
        }
        List<TaskBugTypePieBO> typePieBOS = taskBugMapper.selectBugTypePie(yearMonth);
        List<TaskBugTypePieResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(typePieBOS)){
            list = typePieBOS.stream().map(item->{
                TaskBugTypePieResDTO resDTO = new TaskBugTypePieResDTO();
                resDTO.setNumber(item.getNumber());
                resDTO.setTypeName(TaskBugType.getName(item.getProblemType()));
                return resDTO;
            }).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 用户bug柱状图
     * @param reqDTO 查询条件
     */
    @Override
    public List<TaskBugUserHistogramResDTO> getBugUserHistogram(BugStatReqDTO reqDTO) {
        Date yearAndMonth = reqDTO.getYearAndMonth();
        String yearMonth = "";
        if (yearAndMonth != null){
            yearMonth = new SimpleDateFormat("yyyy-MM").format(yearAndMonth);
        }
        Integer isTester = reqDTO.getIsTester();
        if (isTester == null){
            throw new ZSYServiceException("请选择用户类型");
        }
        List<TaskBugUserHistogramBO> userHistogramBOS = new ArrayList<>();
        if (isTester == 0){
            //查询分配人的bug统计
            userHistogramBOS = taskBugMapper.selectBugHandlerHistogram(yearMonth,reqDTO.getGroupId());
        }else if (isTester == 1){
            userHistogramBOS = taskBugMapper.selectBugCreatorHistogram(yearMonth);
        }
        List<TaskBugUserHistogramResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userHistogramBOS)){
            list = userHistogramBOS.stream()
                    .map(item->{
                        TaskBugUserHistogramResDTO resDTO = new TaskBugUserHistogramResDTO();
                        resDTO.setUserName(item.getUserName());
                        resDTO.setNumber(item.getNumber());
                        return resDTO;
                    }).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 任务bug表
     * @param reqDTO 参数
     */
    @Override
    public ArmorPageInfo<TaskBugStatResDTO> getTaskBugStat(BugStatReqDTO reqDTO) {
        Date yearAndMonth = reqDTO.getYearAndMonth();
        String yearMonth = "";
        if (yearAndMonth != null){
            yearMonth = new SimpleDateFormat("yyyy-MM").format(yearAndMonth);
        }
        List<TaskBugBO> allTaskBugBOS = taskBugMapper.selectTaskStat(yearMonth,reqDTO.getTaskName().trim());
        Page<TaskBugStatResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(allTaskBugBOS)){
            Map<Long, List<TaskBugBO>> taskMap = allTaskBugBOS.stream().collect(Collectors.groupingBy(TaskBug::getTaskId));
            for (Long taskId : taskMap.keySet()) {
                TaskBugStatResDTO resDTO = new TaskBugStatResDTO();
                List<TaskBugBO> taskBugBOS = taskMap.get(taskId);
                resDTO.setTaskId(taskId);
                resDTO.setTaskName(taskBugBOS.get(0).getTaskName());
                resDTO.setTaskStatus(taskBugBOS.get(0).getTaskStatus());

                Map<Integer, List<TaskBugBO>> statusMap = taskBugBOS.stream().collect(Collectors.groupingBy(TaskBug::getStatus));
                List<TaskBugStatResDTO.BugStatusNum> bugStatusNumList = new ArrayList<>();
                List<TaskBugStatResDTO.BugSeverityNum> bugSeverityNumList = new ArrayList<>();
                List<TaskBugStatResDTO.BugTypeNum> bugTypeNumList = new ArrayList<>();
                List<TaskBugStatResDTO.UserBugNum> creatorNumList = new ArrayList<>();
                List<TaskBugStatResDTO.UserBugNum> handlerNumList = new ArrayList<>();
                for (Integer status : statusMap.keySet()) {
                    TaskBugStatResDTO.BugStatusNum bugStatusNum = new TaskBugStatResDTO.BugStatusNum();
                    bugStatusNum.setStatusName(TaskBugStatus.getName(status));
                    bugStatusNum.setNumber(statusMap.get(status).size());
                    bugStatusNumList.add(bugStatusNum);
                }
                Map<Integer, List<TaskBugBO>> severityMap = taskBugBOS.stream().collect(Collectors.groupingBy(TaskBug::getSeverity));
                for (Integer severity : severityMap.keySet()) {
                    TaskBugStatResDTO.BugSeverityNum bugSeverityNum = new TaskBugStatResDTO.BugSeverityNum();
                    bugSeverityNum.setSeverityName(TaskBugSeverity.getName(severity));
                    bugSeverityNum.setNumber(severityMap.get(severity).size());
                    bugSeverityNumList.add(bugSeverityNum);
                }
                Map<Integer, List<TaskBugBO>> typeMap = taskBugBOS.stream().collect(Collectors.groupingBy(TaskBug::getProblemType));
                for (Integer type : typeMap.keySet()) {
                    TaskBugStatResDTO.BugTypeNum bugTypeNum = new TaskBugStatResDTO.BugTypeNum();
                    bugTypeNum.setTypeName(TaskBugType.getName(type));
                    bugTypeNum.setNumber(typeMap.get(type).size());
                    bugTypeNumList.add(bugTypeNum);
                }
                Map<Long, List<TaskBugBO>> creatorMap = taskBugBOS.stream().collect(Collectors.groupingBy(TaskBug::getCreateBy));
                for (Long createUser : creatorMap.keySet()) {
                    TaskBugStatResDTO.UserBugNum userBugNum = new TaskBugStatResDTO.UserBugNum();
                    userBugNum.setUserName(creatorMap.get(createUser).get(0).getCreateName());
                    userBugNum.setNumber(creatorMap.get(createUser).size());
                    creatorNumList.add(userBugNum);
                }
                Map<Long, List<TaskBugBO>> handlerMap = taskBugBOS.stream().collect(Collectors.groupingBy(TaskBug::getHandlerId));
                for (Long handlerId : handlerMap.keySet()) {
                    TaskBugStatResDTO.UserBugNum userBugNum = new TaskBugStatResDTO.UserBugNum();
                    userBugNum.setUserName(handlerMap.get(handlerId).get(0).getHandlerName());
                    userBugNum.setNumber(handlerMap.get(handlerId).size());
                    handlerNumList.add(userBugNum);
                }
                resDTO.setBugStatusNumList(bugStatusNumList);
                resDTO.setBugSeverityNumList(bugSeverityNumList);
                resDTO.setBugTypeNumList(bugTypeNumList);
                resDTO.setCreatorNumList(creatorNumList);
                resDTO.setHandlerNumList(handlerNumList);
                resDTO.setBugNum(taskBugBOS.size());
                list.add(resDTO);
            }
            List<TaskBugStatResDTO> collect = list.stream().sorted(Comparator.comparing(TaskBugStatResDTO::getBugNum).reversed()).collect(Collectors.toList());
            Page<TaskBugStatResDTO> sortList = new Page<>();
            sortList.addAll(collect);
            ArmorPageInfo<TaskBugStatResDTO> page = ArmorPageInfo.pageByMemory(sortList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
            int current = page.getCurrent();
            List<TaskBugStatResDTO> result = page.getList();
            long totalSize = page.getTotalSize();
            return new ArmorPageInfo(current,ZSYConstants.PAGE_SIZE, totalSize,result);
        }
        return new ArmorPageInfo<>(list);
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
