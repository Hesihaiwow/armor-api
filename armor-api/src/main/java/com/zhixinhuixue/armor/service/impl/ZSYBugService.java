package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugManageAddReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugUserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYBugService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Created by Lang on 2017/11/7
 */
@Service
public class ZSYBugService implements IZSYBugService {

    @Autowired
    private IZSYBugManageMapper bugManageMapper;

    @Autowired
    private IZSYBugUserMapper bugUserMapper;

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYUserTaskIntegralMapper integralMapper;
    @Autowired
    private IZSYWorkGroupMapper groupMapper;


    /**
     * bug处理结果表
     * @param bugListReqDTO
     * @return
     */
    @Override
    public PageInfo<BugPageResDTO> getBugList(BugListReqDTO bugListReqDTO) {
        PageHelper.startPage(bugListReqDTO.getPageNum(), ZSYConstants.PAGE_SIZE);
        bugListReqDTO.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
        Page<BugManageListBO> bugListBOS = bugManageMapper.getBugList(bugListReqDTO);
        Page<BugPageResDTO> bugPageResDTOS = new Page();
        BeanUtils.copyProperties(bugListBOS, bugPageResDTOS);
        bugListBOS.stream().forEach(bugListBO -> {
            BugPageResDTO bugPageResDTO = new BugPageResDTO();
            BeanUtils.copyProperties(bugListBO, bugPageResDTO);
            bugPageResDTO.setCreateTime(DateHelper.dateFormatter(bugListBO.getCreateTime(),DateHelper.DATE_FORMAT));
            bugPageResDTO.setProcessTime(DateHelper.dateFormatter(bugListBO.getProcessTime(),DateHelper.DATE_FORMAT));
            bugPageResDTO.setProjectId(new Long((bugListReqDTO.getPageNum()-1)*10)+bugListBO.getProjectId());//projectId这里用于序号值
            bugPageResDTOS.add(bugPageResDTO);
        });

        return new PageInfo<>(bugPageResDTOS);
    }


    /**
     * 分页查询线上bug
     * @author sch
     * @param bugListReqDTO
     * @return
     */
    @Override
    public PageInfo<OnlineBugResDTO> getBugManagePage(BugListReqDTO bugListReqDTO) {
        PageHelper.startPage(Optional.ofNullable(bugListReqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        bugListReqDTO.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
        Page<OnlineBugBO> onlineBugBOS = bugManageMapper.selectOnlineBugPage(bugListReqDTO);
        Page<OnlineBugResDTO> page = new Page<>();
        if (!CollectionUtils.isEmpty(onlineBugBOS)){
            BeanUtils.copyProperties(onlineBugBOS,page);
            onlineBugBOS.stream().forEach(onlineBugBO -> {
                OnlineBugResDTO onlineBugResDTO = new OnlineBugResDTO();
                BeanUtils.copyProperties(onlineBugBO,onlineBugResDTO);
                String developers = "";
                String testers = "";
                String others = "";
                if (!CollectionUtils.isEmpty(onlineBugBO.getUserIds())){
                    for (Long userId : onlineBugBO.getUserIds()) {
                        User user = userMapper.selectById(userId);
                        if (user.getJobRole()==ZSYJobRole.TEST.getValue()){
                            testers = testers  + user.getName()+ " ";
                        }else if (user.getJobRole()==ZSYJobRole.JAVA.getValue()
                                || user.getJobRole()==ZSYJobRole.PHP.getValue()
                                || user.getJobRole()==ZSYJobRole.C.getValue()
                                || user.getJobRole()==ZSYJobRole.IOS.getValue()
                                || user.getJobRole()==ZSYJobRole.ANDROID.getValue()
                                || user.getJobRole()==ZSYJobRole.FRONT.getValue()
                                || user.getJobRole()==ZSYJobRole.ARTIFICIAL.getValue()){
                            developers = developers  + user.getName()+ " ";
                        }else {
                            others = others + user.getName() + " ";
                        }
                    }
                }
                if (onlineBugBO.getAffectScope() != null){
                    onlineBugResDTO.setAffectScopeStr(AffectScopeEnum.getName(onlineBugBO.getAffectScope()));
                }
                onlineBugResDTO.setOthers(others);
                onlineBugResDTO.setDevelopers(developers);
                onlineBugResDTO.setTesters(testers);
                if (onlineBugBO.getProjectId() != null){
                    onlineBugResDTO.setDemandSystemName("知心慧学");
                }
                if (onlineBugBO.getProcessTime() != null && onlineBugBO.getProjectId() != null){
                    onlineBugResDTO.setIsSolved(1);
                }
                if (onlineBugBO.getType() == null){
                    onlineBugResDTO.setType(0);
                }
                String bugNoStr = getBugNoStr(onlineBugBO.getBugNo());
                onlineBugResDTO.setBugNoStr(bugNoStr);
                page.add(onlineBugResDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 线上bug详情
     * @author sch
     * @param id
     * @return
     */
    @Override
    public OnlineBugDetailResDTO getOnlineBugDetail(Long id) {
        OnlineBugDetailResDTO resDTO = new OnlineBugDetailResDTO();
        OnlineBugBO onlineBugBO = bugManageMapper.selectOnlineBugDetailById(id);
        if (onlineBugBO == null){
            throw new ZSYServiceException("无法找到Bug处理结果,id:" + id);
        }
        BeanUtils.copyProperties(onlineBugBO,resDTO);
        if (onlineBugBO.getAffectScope() != null){
            resDTO.setAffectScopeStr(AffectScopeEnum.getName(onlineBugBO.getAffectScope()));
        }
        String bugNoStr = getBugNoStr(onlineBugBO.getBugNo());
        resDTO.setBugNoStr(bugNoStr);
        if (resDTO.getDemandSystemName() == null){
            resDTO.setDemandSystemName("知心慧学");
        }
        return resDTO;
    }


    /**
     * 添加bug处理结果
     * @param bugReqDTO
     */
    @Override
    public void addBug(BugReqDTO bugReqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.EMPLOYEE.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
        // 判断含有重复的负责人
        if (bugReqDTO.getBugUsers() != null && bugReqDTO.getBugUsers().size() > 0) {
            List<Long> users = bugReqDTO.getBugUsers().stream().map(BugUserReqDTO::getUserId).distinct().collect(Collectors.toList());
            if (users.size() < bugReqDTO.getBugUsers().size()) {
                throw new ZSYServiceException("负责人重复");
            }
        }

        BugManage bugManage = new BugManage();
        bugManage.setId(snowFlakeIDHelper.nextId());
        bugManage.setCreateTime(bugReqDTO.getCreateTime());
        bugManage.setProcessTime(bugReqDTO.getProcessTime());
        bugManage.setDescription(bugReqDTO.getDescription());
        bugManage.setProjectId(bugReqDTO.getProjectId());
        bugManageMapper.insertBug(bugManage);

        // 插入Bug处理用户
        if (bugReqDTO.getBugUsers() != null && bugReqDTO.getBugUsers().size() > 0) {
            List<BugUser> bugUsers = Lists.newArrayList();
            bugReqDTO.getBugUsers().forEach(user -> {
                BugUser bugUser = new BugUser();
                bugUser.setBugId(bugManage.getId());
                bugUser.setId(snowFlakeIDHelper.nextId());
                bugUser.setIntegral(user.getIntegral());
                bugUser.setUserId(user.getUserId());
                bugUsers.add(bugUser);
            });
            bugUserMapper.insertList(bugUsers);
        }

        bugReqDTO.getBugUsers().forEach(user -> {
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setId(snowFlakeIDHelper.nextId());
            userIntegral.setUserId(user.getUserId());
            userIntegral.setIntegral(user.getIntegral());
            userIntegral.setOrigin(ZSYIntegralOrigin.BUG.getValue());
            userIntegral.setTaskId(bugManage.getId());
            userIntegral.setDescription("线上Bug处理");
            userIntegral.setCreateTime(new Date());
            userIntegralMapper.insert(userIntegral);
            // 修改用户积分
            User userBug = userMapper.selectById(user.getUserId());
            BigDecimal currentIntegral = userBug.getIntegral();
            User userBO = new User();
            userBO.setId(user.getUserId());
            userBO.setIntegral(currentIntegral.add(userIntegral.getIntegral()));
            userMapper.updateSelectiveById(userBO);
        });

    }


    /**
     * 添加新bug
     * @author sch
     * @param reqDTO
     */
    @Override
    public void addNewBug(BugManageAddReqDTO reqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.EMPLOYEE.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }
        // 判断含有重复的负责人
        if (reqDTO.getBugUsers() != null && reqDTO.getBugUsers().size() > 0) {
            List<Long> users = reqDTO.getBugUsers().stream().map(BugUserReqDTO::getUserId).distinct().collect(Collectors.toList());
            if (users.size() < reqDTO.getBugUsers().size()) {
                throw new ZSYServiceException("负责人重复");
            }
        }

        //查询最后一个bug编号
        OnlineBugManage lastBug = bugManageMapper.selectLastBugNo();

        OnlineBugManage bugManage = new OnlineBugManage();
        if (lastBug != null && lastBug.getBugNo() > 0){
            bugManage.setBugNo(lastBug.getBugNo()+1);
        }else {
            bugManage.setBugNo(1);
        }
        bugManage.setId(snowFlakeIDHelper.nextId());
        if (reqDTO.getTaskId() != null){
            bugManage.setTaskId(reqDTO.getTaskId());
        }
        bugManage.setCreateTime(reqDTO.getDiscoverTime());
        bugManage.setDiscoverTime(reqDTO.getDiscoverTime());
        bugManage.setProcessTime(reqDTO.getProcessTime());
        bugManage.setDescription(reqDTO.getDescription());
        bugManage.setProjectId(reqDTO.getProjectId());
        bugManage.setOrigin(reqDTO.getOrigin());
        bugManage.setAccountInfo(reqDTO.getAccountInfo());
        bugManage.setType(reqDTO.getType());
        bugManage.setDemandSystemId(reqDTO.getDemandSystemId());
        bugManage.setDemandSystemName(reqDTO.getDemandSystemName());
        bugManage.setIsSolved(reqDTO.getIsSolved());
        bugManage.setRemark(reqDTO.getRemark());
        bugManage.setGroupId(reqDTO.getGroupId());
        bugManage.setAffectScope(reqDTO.getAffectScope());
        LocalDateTime localDateTime =
                LocalDateTime.ofEpochSecond(reqDTO.getDiscoverTime().getTime() / 1000, 0, ZoneOffset.ofHours(8));
        bugManage.setYear(localDateTime.getYear());
        bugManageMapper.insertBugManager(bugManage);

        // 插入Bug处理用户
        if (reqDTO.getBugUsers() != null && reqDTO.getBugUsers().size() > 0) {
            List<BugUser> bugUsers = Lists.newArrayList();
            reqDTO.getBugUsers().forEach(user -> {
                BugUser bugUser = new BugUser();
                bugUser.setBugId(bugManage.getId());
                bugUser.setId(snowFlakeIDHelper.nextId());
                bugUser.setIntegral(Optional.ofNullable(user.getIntegral()).orElse(BigDecimal.ZERO));
                bugUser.setUserId(user.getUserId());
                bugUsers.add(bugUser);
            });
            bugUserMapper.insertList(bugUsers);
        }

        reqDTO.getBugUsers().forEach(user -> {
            //新增积分记录
            UserTaskIntegral integral = new UserTaskIntegral();
            integral.setId(snowFlakeIDHelper.nextId());
            integral.setUserId(user.getUserId());
            integral.setIntegral(user.getIntegral());
            integral.setOrigin(ZSYUserTaskIntegralOrigin.BUG.getValue());
            integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            integral.setCreateTime(new Date());
            integral.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
            String bugNoStr = getBugNoStr(bugManage.getBugNo());
            integral.setDescription("线上任务bug: 编号 "+bugNoStr);
            integral.setBugId(bugManage.getId());
            integralMapper.insert(integral);

        });
    }


    /**
     * 更新Bug处理
     * @param id
     * @param bugReqDTO
     */
    @Override
    public void updateBug(Long id,BugReqDTO bugReqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.EMPLOYEE.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }

        BugManageBO bugManageBO = bugManageMapper.selectDetailById(id);
        if (bugManageBO == null) {
            throw new ZSYServiceException("无法找到Bug信息,id:" + id);
        }

        // 判断含有重复的负责人
        if (bugReqDTO.getBugUsers() != null && bugReqDTO.getBugUsers().size() > 0) {
            List<Long> users = bugReqDTO.getBugUsers().stream().map(BugUserReqDTO::getUserId).distinct().collect(Collectors.toList());
            if (users.size() < bugReqDTO.getBugUsers().size()) {
                throw new ZSYServiceException("负责人重复");
            }
        }

        BugManage bugManage = new BugManage();
        bugManage.setId(id);
        bugManage.setCreateTime(bugReqDTO.getCreateTime());
        bugManage.setProcessTime(bugReqDTO.getProcessTime());
        bugManage.setDescription(bugReqDTO.getDescription());
        bugManage.setProjectId(bugReqDTO.getProjectId());
        bugManageMapper.updateBug(bugManage);


        if (bugUserMapper.deleteById(id) == 0) {
            throw new ZSYServiceException("删除Bug用户失败");
        }

        // 插入Bug处理用户
        if (bugReqDTO.getBugUsers() != null && bugReqDTO.getBugUsers().size() > 0) {
            List<BugUser> bugUsers = Lists.newArrayList();
            bugReqDTO.getBugUsers().forEach(user -> {
                BugUser bugUser = new BugUser();
                bugUser.setBugId(bugManage.getId());
                bugUser.setId(snowFlakeIDHelper.nextId());
                bugUser.setIntegral(user.getIntegral());
                bugUser.setUserId(user.getUserId());
                bugUsers.add(bugUser);
            });
            bugUserMapper.insertList(bugUsers);
        }

        //将旧的Bug处理积分还原
        List<UserIntegral> userIntegrals = userIntegralMapper.getUserIntegralByTaskId(id);
        userIntegrals.forEach(userIntegral -> {
            // 修改用户积分
            User userBug = userMapper.selectById(userIntegral.getUserId());
            BigDecimal currentIntegral = userBug.getIntegral();
            User userBO = new User();
            userBO.setId(userBug.getId());
            userBO.setIntegral(currentIntegral.subtract(userIntegral.getIntegral()));
            userMapper.updateSelectiveById(userBO);
            userIntegralMapper.deleteUserIntegral(id,userIntegral.getUserId());
        });

        bugReqDTO.getBugUsers().forEach(user -> {
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setId(snowFlakeIDHelper.nextId());
            userIntegral.setUserId(user.getUserId());
            userIntegral.setIntegral(user.getIntegral());
            userIntegral.setOrigin(ZSYIntegralOrigin.BUG.getValue());
            userIntegral.setTaskId(bugManage.getId());
            userIntegral.setDescription("线上Bug处理");
            userIntegral.setCreateTime(new Date());
            userIntegralMapper.insert(userIntegral);
            // 修改用户积分
            User userBug = userMapper.selectById(user.getUserId());
            BigDecimal currentIntegral = userBug.getIntegral();
            User userBO = new User();
            userBO.setId(user.getUserId());
            userBO.setIntegral(currentIntegral.add(userIntegral.getIntegral()));
            userMapper.updateSelectiveById(userBO);
        });
    }


    /**
     * 更新线上bug
     * @author sch
     * @param reqDTO
     * @param id
     */
    @Override
    public void updateOnlineBug(BugManageAddReqDTO reqDTO, Long id) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.EMPLOYEE.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        User userTemp = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        if (userTemp == null || userTemp.getIsDelete() == 1) {
            throw new ZSYServiceException("用户不存在");
        }

        OnlineBugBO bugManageBO = bugManageMapper.selectOnlineBugDetailById(id);
        if (bugManageBO == null) {
            throw new ZSYServiceException("无法找到Bug信息,id:" + id);
        }

        // 判断含有重复的负责人
        if (reqDTO.getBugUsers() != null && reqDTO.getBugUsers().size() > 0) {
            List<Long> users = reqDTO.getBugUsers().stream().map(BugUserReqDTO::getUserId).distinct().collect(Collectors.toList());
            if (users.size() < reqDTO.getBugUsers().size()) {
                throw new ZSYServiceException("负责人重复");
            }
        }

        OnlineBugManage bugManage = new OnlineBugManage();
        bugManage.setId(id);
        if (reqDTO.getTaskId()!=null){
            bugManage.setTaskId(reqDTO.getTaskId());
        }
        bugManage.setCreateTime(reqDTO.getDiscoverTime());
        bugManage.setDiscoverTime(reqDTO.getDiscoverTime());
        bugManage.setProcessTime(reqDTO.getProcessTime());
        bugManage.setDescription(reqDTO.getDescription());
        bugManage.setProjectId(reqDTO.getProjectId());
        bugManage.setOrigin(reqDTO.getOrigin());
        bugManage.setAccountInfo(reqDTO.getAccountInfo());
        bugManage.setType(reqDTO.getType());
        bugManage.setDemandSystemName(reqDTO.getDemandSystemName());
        bugManage.setDemandSystemId(reqDTO.getDemandSystemId());
        bugManage.setIsSolved(reqDTO.getIsSolved());
        bugManage.setRemark(reqDTO.getRemark());
        bugManage.setGroupId(reqDTO.getGroupId());
        bugManage.setAffectScope(reqDTO.getAffectScope());
        LocalDateTime localDateTime =
                LocalDateTime.ofEpochSecond(reqDTO.getDiscoverTime().getTime() / 1000, 0, ZoneOffset.ofHours(8));
        bugManage.setYear(localDateTime.getYear());
        bugManageMapper.updateBugManager(bugManage);

        if (bugUserMapper.deleteById(id) == 0) {
            throw new ZSYServiceException("删除Bug用户失败");
        }

        // 插入Bug处理用户
        if (reqDTO.getBugUsers() != null && reqDTO.getBugUsers().size() > 0) {
            List<BugUser> bugUsers = Lists.newArrayList();
            reqDTO.getBugUsers().forEach(user -> {
                BugUser bugUser = new BugUser();
                bugUser.setBugId(bugManage.getId());
                bugUser.setId(snowFlakeIDHelper.nextId());
                bugUser.setIntegral(Optional.ofNullable(user.getIntegral()).orElse(BigDecimal.ZERO));
                bugUser.setUserId(user.getUserId());
                bugUsers.add(bugUser);
            });
            bugUserMapper.insertList(bugUsers);
        }

        //删除原来的bug积分
        integralMapper.deleteByBugId(id);
        reqDTO.getBugUsers().forEach(user -> {
            //新增积分记录
            UserTaskIntegral integral = new UserTaskIntegral();
            integral.setId(snowFlakeIDHelper.nextId());
            integral.setUserId(user.getUserId());
            integral.setIntegral(user.getIntegral());
            integral.setOrigin(ZSYUserTaskIntegralOrigin.BUG.getValue());
            integral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            integral.setCreateTime(new Date());
            integral.setReviewStatus(ZSYReviewStatus.ACCEPT.getValue());
            String bugNoStr = getBugNoStr(bugManageBO.getBugNo());
            integral.setDescription("线上任务bug: 编号 "+bugNoStr);
            integral.setBugId(id);
            integralMapper.insert(integral);


        });
    }

    /**
     * 查询各个分类的线上bug数量
     * @author sch
     * @return
     */
    @Override
    public OnlineBugNumResDTO getDiffTypeBugNum(BugListReqDTO bugListReqDTO) {
        Integer optimizationNum = bugManageMapper.selectCountByType(1,bugListReqDTO);
        Integer assistanceNum = bugManageMapper.selectCountByType(2,bugListReqDTO);
        Integer bugNum = bugManageMapper.selectCountByType(0,bugListReqDTO);
        Integer totalNum = bugManageMapper.selectCountTotal();
        OnlineBugNumResDTO resDTO = new OnlineBugNumResDTO();
        resDTO.setAssistanceNum(assistanceNum);
        resDTO.setBugNum(bugNum);
        resDTO.setOptimizationNum(optimizationNum);
        resDTO.setTotalNum(totalNum);
        return resDTO;
    }

    /**
     * 分页查询线上bug(旧数据)
     * @author sch
     * @param bugListReqDTO
     * @return
     */
    @Override
    public PageInfo<OnlineBugResDTO> getOldBugManagePage(BugListReqDTO bugListReqDTO) {
        PageHelper.startPage(Optional.ofNullable(bugListReqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        bugListReqDTO.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
        Page<OnlineBugBO> onlineBugBOS = bugManageMapper.selectOldOnlineBugPage(bugListReqDTO);
        Page<OnlineBugResDTO> page = new Page<>();
        if (!CollectionUtils.isEmpty(onlineBugBOS)){
            BeanUtils.copyProperties(onlineBugBOS,page);
            onlineBugBOS.stream().forEach(onlineBugBO -> {
                OnlineBugResDTO onlineBugResDTO = new OnlineBugResDTO();
                BeanUtils.copyProperties(onlineBugBO,onlineBugResDTO);
                String developers = "";
                String testers = "";
                if (!CollectionUtils.isEmpty(onlineBugBO.getUserIds())){
                    for (Long userId : onlineBugBO.getUserIds()) {
                        User user = userMapper.selectById(userId);
                        if (user.getDepartmentId().equals(87526048211664896L)){
                            developers = developers  + user.getName()+ " ";
                        }
                        if (user.getDepartmentId().equals(87526088225325056L)){
                            testers = testers  + user.getName()+ " ";
                        }
                    }
                }
                onlineBugResDTO.setDevelopers(developers);
                onlineBugResDTO.setTesters(testers);
                if (onlineBugBO.getProjectId() != null){
                    onlineBugResDTO.setDemandSystemName("知心慧学");
                }
                if (onlineBugBO.getProcessTime() != null && onlineBugBO.getProjectId() != null){
                    onlineBugResDTO.setIsSolved(1);
                }
                if (onlineBugBO.getType() == null){
                    onlineBugResDTO.setType(0);
                }
                page.add(onlineBugResDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 更新老数据状态为已解决
     * @author sch
     */
    @Override
    public void updateStatus() {
        List<OnlineBugManage> onlineBugManages = bugManageMapper.selectIsSolvedIsNull();
        List<OnlineBugManage> newList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(onlineBugManages)){
            onlineBugManages.stream().forEach(onlineBugManage -> {
                onlineBugManage.setIsSolved(1);
                newList.add(onlineBugManage);
            });
        }
        if (!CollectionUtils.isEmpty(newList)){
            if (bugManageMapper.updateStatusBatch(newList) == 0){
                throw new ZSYServiceException("批量更新bug处理状态失败");
            }
        }
    }

    /**
     * 各个系统bug分类柱状图
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    public List<SystemBugResDTO> getSystemHistogram(BugListReqDTO reqDTO) {
        List<SystemBugResDTO> list = new ArrayList<>();
        //查询时间段内线上bug的系统数量
//        List<Integer> systemIds = bugManageMapper.selectSystemsByTime(reqDTO.getStartTime(),reqDTO.getEndTime());
        //查询时间段内线上bug的业务组数量
        List<Long> groupIds = bugManageMapper.selectGroupsByTime(reqDTO.getStartTime(),reqDTO.getEndTime(),reqDTO.getYear());
        //查询时间段内线上bug各个系统对应的各个类型的数量
        List<SystemBugTypeBO> bugTypeBOS = bugManageMapper.selectSystemTypeNum(reqDTO.getStartTime(),reqDTO.getEndTime(),reqDTO.getYear());
        if (!CollectionUtils.isEmpty(groupIds)){
            groupIds.forEach(groupId->{
//                Integer bugNum = bugManageMapper.selectNumBySystemAndType(systemId,ZSYBugType.BUG.getValue());
//                Integer optimizationNum = bugManageMapper.selectNumBySystemAndType(systemId,ZSYBugType.OPTIMIZATION.getValue());
//                Integer assistanceNum = bugManageMapper.selectNumBySystemAndType(systemId,ZSYBugType.ASSISTANCE.getValue());
                SystemBugResDTO systemBugResDTO = new SystemBugResDTO();
                if (!CollectionUtils.isEmpty(bugTypeBOS)){
                    systemBugResDTO.setGroupId(groupId);
                    systemBugResDTO.setBugNum(0);
                    systemBugResDTO.setOptimizationNum(0);
                    systemBugResDTO.setAssistanceNum(0);
                    List<SystemBugTypeBO> bugBOS = bugTypeBOS.stream()
                            .filter(bugTypeBO -> bugTypeBO.getGroupId().equals(groupId) && bugTypeBO.getType() == ZSYBugType.BUG.getValue())
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(bugBOS)){
                        SystemBugTypeBO bugBO = bugBOS.get(0);
                        if (bugBO!=null){
                            systemBugResDTO.setBugNum(bugBO.getNum());
                            systemBugResDTO.setGroupName(bugBO.getGroupName());
                        }
                    }


                    List<SystemBugTypeBO> optimizationBOS = bugTypeBOS.stream()
                            .filter(bugTypeBO -> bugTypeBO.getGroupId().equals(groupId) && bugTypeBO.getType() == ZSYBugType.OPTIMIZATION.getValue())
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(optimizationBOS)){
                        SystemBugTypeBO optimizationBO = optimizationBOS.get(0);
                        if (optimizationBO!=null){
                            systemBugResDTO.setOptimizationNum(optimizationBO.getNum());
                            systemBugResDTO.setGroupName(optimizationBO.getGroupName());
                        }
                    }


                    List<SystemBugTypeBO> assistanceBOS = bugTypeBOS.stream()
                            .filter(bugTypeBO -> bugTypeBO.getGroupId().equals(groupId) && bugTypeBO.getType() == ZSYBugType.ASSISTANCE.getValue())
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(assistanceBOS)){
                        SystemBugTypeBO assistanceBO = assistanceBOS.get(0);
                        if (assistanceBO!=null){
                            systemBugResDTO.setAssistanceNum(assistanceBO.getNum());
                            systemBugResDTO.setGroupName(assistanceBO.getGroupName());
                        }
                    }

                }
                list.add(systemBugResDTO);
            });

        }
        return list;
    }

    /**
     * 用户bug分类柱状图
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    public List<UserBugResDTO> getUserBugHistogram(BugListReqDTO reqDTO) {
        List<UserBugResDTO> list = new ArrayList<>();
        //查询时间段内bug人员
        List<Long> userIds = bugUserMapper.selectBugUsersByTime(reqDTO.getStartTime(),reqDTO.getEndTime(),reqDTO.getYear());
        //查询时间段内用户参与的bug
        List<UserBugTypeBO> userBugTypeBOS = bugUserMapper.selectUserTypeNum(reqDTO.getStartTime(),reqDTO.getEndTime(),reqDTO.getYear());
        if (!CollectionUtils.isEmpty(userIds)){
            userIds.forEach(userId->{
                UserBugResDTO userBugResDTO = new UserBugResDTO();
                userBugResDTO.setUserId(userId);
                userBugResDTO.setBugNum(0);
                userBugResDTO.setAssistanceNum(0);
                userBugResDTO.setOptimizationNum(0);
                if (!CollectionUtils.isEmpty(userBugTypeBOS)){
                    List<UserBugTypeBO> bugBOS = userBugTypeBOS.stream()
                            .filter(userBugTypeBO -> userBugTypeBO.getUserId().equals(userId) && userBugTypeBO.getType() == ZSYBugType.BUG.getValue())
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(bugBOS)){
                        UserBugTypeBO bugBO = bugBOS.get(0);
                        if (bugBO!=null){
                            userBugResDTO.setUserName(bugBO.getUserName());
                            userBugResDTO.setBugNum(bugBO.getNum());
                        }
                    }

                    List<UserBugTypeBO> optimizationBOS = userBugTypeBOS.stream()
                            .filter(userBugTypeBO -> userBugTypeBO.getUserId().equals(userId) && userBugTypeBO.getType() == ZSYBugType.OPTIMIZATION.getValue())
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(optimizationBOS)){
                        UserBugTypeBO optimizationBO = optimizationBOS.get(0);
                        if (optimizationBO!=null){
                            userBugResDTO.setUserName(optimizationBO.getUserName());
                            userBugResDTO.setOptimizationNum(optimizationBO.getNum());
                        }
                    }

                    List<UserBugTypeBO> assistanceBOS = userBugTypeBOS.stream()
                            .filter(userBugTypeBO -> userBugTypeBO.getUserId().equals(userId) && userBugTypeBO.getType() == ZSYBugType.ASSISTANCE.getValue())
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(assistanceBOS)){
                        UserBugTypeBO assistanceBO = assistanceBOS.get(0);
                        if (assistanceBO!=null){
                            userBugResDTO.setUserName(assistanceBO.getUserName());
                            userBugResDTO.setAssistanceNum(assistanceBO.getNum());
                        }
                    }
                }
                list.add(userBugResDTO);
            });
        }
        return list;
    }

    /**
     * 导入bug
     * @param uploadFile 文件
     */
    @Override
    @Transactional
    public void importBug(MultipartFile uploadFile) {
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isExcel(suffix)){
            throw new ZSYServiceException("只能上传Excel");
        }
        Workbook book;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat timeSDF2 = new SimpleDateFormat("yyyy.MM.dd.HH");
        SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
        try {
            File file = multipartFileToFile(uploadFile);
            String file_dir = file.getAbsolutePath();
            book = getExcelWorkbook(file_dir);

            Sheet sheet = getSheetByNum(book,0);
            int lastRowNum = sheet.getLastRowNum();

            List<List<String>> bugList = new ArrayList<>();
            for(int i = 1 ; i <= lastRowNum ; i++){
                List<String> bugFields = new ArrayList<>();
                Row row;
                row = sheet.getRow(i);
                if( row != null ){
                    int lastCellNum = 12;
                    Cell cell;
                    for( int j = 0 ; j <= lastCellNum ; j++ ){
                        cell = row.getCell(j);
                        if( cell != null ){
                            cell.setCellType(CellType.STRING);
                            String cellValue = cell.getStringCellValue();
                            if (j == 1 && !Strings.isNullOrEmpty(cellValue)){
                                //将Excel表中的日期转换成字符串
                                calendar.set(1900, 0, 1);
                                calendar.add(Calendar.DATE, Integer.valueOf(cellValue) - 2);
                                BigDecimal bd = new BigDecimal(cellValue);
                                int mills = (int) Math.round(bd.subtract(new BigDecimal(Integer.valueOf(cellValue))).doubleValue() * 24 * 3600);
                                int hour = mills / 3600;
                                int minute = (mills - hour * 3600) / 60;
                                int second = mills - hour * 3600 - minute * 60;
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, second);
                                Date d = calendar.getTime();//Date
                                cellValue = dateSDF.format(d)+" 00:00:00";
                            }
                            bugFields.add(cellValue);
                        }
                    }
                }
                bugList.add(bugFields);
            }
            bugList = bugList.stream().filter(item->!Strings.isNullOrEmpty(item.get(0))).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(bugList)){
                throw new ZSYServiceException("暂无数据导入,请检查");
            }
            //查询所有用户
            List<User> users = userMapper.selectEffectiveUsers(ZSYTokenRequestContext.get().getDepartmentId());
            Map<String, List<User>> userMap = users.stream().collect(Collectors.groupingBy(User::getName));
            //查询最近一条bug
            OnlineBugManage lastBug = bugManageMapper.selectLastBugNo();
            //查询所有业务组
            List<WorkGroup> groupList = groupMapper.selectList();
            Integer lastBugNo = lastBug.getBugNo();
            List<OnlineBugManage> bugManageList = new ArrayList<>();
            List<BugUser> bugUsers = new ArrayList<>();
            for (List<String> bugDetail : bugList) {
                lastBugNo++;
                OnlineBugManage bugManage = new OnlineBugManage();
                bugManage.setId(snowFlakeIDHelper.nextId());
                bugManage.setBugNo(lastBugNo);
                bugManage.setDescription(bugDetail.get(4));
                bugManage.setCreateTime(timeSDF.parse(bugDetail.get(1)));
                bugManage.setDiscoverTime(timeSDF.parse(bugDetail.get(1)));
                if (!Strings.isNullOrEmpty(bugDetail.get(11))){
                    bugManage.setProcessTime(timeSDF2.parse(bugDetail.get(11)));
                }
                bugManage.setOrigin(bugDetail.get(0));
                bugManage.setAccountInfo(bugDetail.get(3));
                bugManage.setDemandSystemId(1);
                if (!Strings.isNullOrEmpty(bugDetail.get(6))){
                    if (bugDetail.get(6).equals(ZSYBugType.BUG.getName())){
                        bugManage.setType(ZSYBugType.BUG.getValue());
                    }else if (bugDetail.get(6).equals(ZSYBugType.OPTIMIZATION.getName())){
                        bugManage.setType(ZSYBugType.OPTIMIZATION.getValue());
                    }else if (bugDetail.get(6).equals(ZSYBugType.ASSISTANCE.getName())){
                        bugManage.setType(ZSYBugType.ASSISTANCE.getValue());
                    }
                }
                if (!Strings.isNullOrEmpty(bugDetail.get(7))){
                    if (bugDetail.get(7).equals("已解决")){
                        bugManage.setIsSolved(1);
                    }else if (bugDetail.get(7).equals("未解决")){
                        bugManage.setIsSolved(0);
                    }else if (bugDetail.get(7).equals("暂搁置")){
                        bugManage.setIsSolved(2);
                    }
                }
                bugManage.setRemark(bugDetail.get(12));
                bugManage.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
                if (!Strings.isNullOrEmpty(bugDetail.get(2))){
                    List<WorkGroup> collect = groupList.stream()
                            .filter(group -> group.getName().contains(bugDetail.get(2))).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect) && collect.get(0) != null){
                        bugManage.setGroupId(collect.get(0).getId());
                    }else {
                        WorkGroup workGroup = groupList.stream()
                                .filter(group -> group.getName().contains("家教生校")).collect(Collectors.toList()).get(0);
                        bugManage.setGroupId(workGroup.getId());
                    }
                }
                if (!Strings.isNullOrEmpty(bugDetail.get(5))){
                    if (AffectScopeEnum.SINGLE.getName().equals(bugDetail.get(5))){
                        bugManage.setAffectScope(AffectScopeEnum.SINGLE.getValue());
                    }else if (AffectScopeEnum.MULTIPLE.getName().equals(bugDetail.get(5))){
                        bugManage.setAffectScope(AffectScopeEnum.MULTIPLE.getValue());
                    }else if (AffectScopeEnum.ALL.getName().equals(bugDetail.get(5))){
                        bugManage.setAffectScope(AffectScopeEnum.ALL.getValue());
                    }
                }
                Date date = bugManage.getCreateTime();
                Instant instant = date.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
                bugManage.setYear(localDateTime.getYear());
                String productStr = bugDetail.get(8);
                String developerStr = bugDetail.get(9);
                String testerStr = bugDetail.get(10);
                List<BugUser> bugUserList = new ArrayList<>();
                if (!Strings.isNullOrEmpty(productStr)){
                    bugUserList = getBugUserList(userMap, bugManage, productStr, bugUserList);
                }
                if (!Strings.isNullOrEmpty(developerStr)){
                    bugUserList = getBugUserList(userMap, bugManage, developerStr, bugUserList);
                }
                if (!Strings.isNullOrEmpty(testerStr)){
                    bugUserList = getBugUserList(userMap, bugManage, testerStr, bugUserList);
                }
                bugUsers.addAll(bugUserList);
                bugManageList.add(bugManage);
            }
            bugUserMapper.insertList(bugUsers);
            bugManageMapper.insertBatch(bugManageList);
        } catch (Exception e) {
            throw new ZSYServiceException(e.getMessage());
        }
    }

    private List<BugUser> getBugUserList(Map<String, List<User>> userMap, OnlineBugManage bugManage, String testerStr, List<BugUser> bugUserList) {
        String[] testerArr = testerStr.split(",");
        for (String tester : testerArr) {
            if (!CollectionUtils.isEmpty(userMap.get(tester))) {
                User user = userMap.get(tester).get(0);
                BugUser bugUser = new BugUser();
                bugUser.setId(snowFlakeIDHelper.nextId());
                bugUser.setBugId(bugManage.getId());
                bugUser.setUserId(user.getId());
                bugUser.setIntegral(BigDecimal.ZERO);
                bugUserList.add(bugUser);
            }
        }
        return bugUserList;
    }

    /**
     * 获取bug处理详情
     * @param id
     * @return
     */
    @Override
    public ZSYResult<BugDetailResDTO> getBugDetail(Long id) {
        BugManageBO bugManageBO = bugManageMapper.selectDetailById(id);
        Optional.ofNullable(bugManageBO).orElseThrow(() -> new ZSYServiceException("无法找到Bug处理结果,id:" + id));
        BugDetailResDTO bugDetailResDTO = new BugDetailResDTO();
        BeanUtils.copyProperties(bugManageBO, bugDetailResDTO);

        return ZSYResult.success().data(bugDetailResDTO);
    }

    @Override
    public void deleteBug(Long id) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.EMPLOYEE.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        OnlineBugManage bugManage = bugManageMapper.selectById(id);
        if (bugManage==null){
            throw new ZSYServiceException("当前bug不存在,请检查");
        }
        bugManage.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        if (bugManageMapper.updateBugManager(bugManage) == 0) {
            throw new ZSYServiceException("删除Bug处理记录失败");
        }

        //将旧的Bug处理积分还原
        integralMapper.deleteByBugId(id);


//        List<UserIntegral> userIntegrals = userIntegralMapper.getUserIntegralByTaskId(id);
//        userIntegrals.forEach(userIntegral -> {
//            // 修改用户积分
//            User userBug = userMapper.selectById(userIntegral.getUserId());
//            BigDecimal currentIntegral = userBug.getIntegral();
//            User userBO = new User();
//            userBO.setId(userBug.getId());
//            userBO.setIntegral(currentIntegral.subtract(userIntegral.getIntegral()));
//            userMapper.updateSelectiveById(userBO);
//            if (userIntegralMapper.deleteUserIntegral(id , userIntegral.getUserId()) == 0) {
//                throw new ZSYServiceException("删除积分信息失败");
//            }
//        });
//
        if (bugUserMapper.deleteById(id) == 0) {
            throw new ZSYServiceException("删除Bug用户处理记录失败");
        }
    }

    private String getBugNoStr(Integer bugNo){
        String bugNoStr = "";
        if (bugNo!=null && bugNo>0){
            if (bugNo<10){
                bugNoStr = "00000"+bugNo;
            }else if (bugNo<100){
                bugNoStr = "0000"+bugNo;
            }else if (bugNo<1000){
                bugNoStr = "000"+bugNo;
            }else if (bugNo<10000){
                bugNoStr = "00"+bugNo;
            }else if (bugNo<100000){
                bugNoStr = "0"+bugNo;
            }else {
                bugNoStr = ""+bugNo;
            }
        }
        return bugNoStr;
    }

    /**
     * 获取上传文件后缀名
     *
     * @param uploadName
     * @return
     */
    public String getUploadSuffix(String uploadName) {
        return uploadName.substring(uploadName.lastIndexOf(".") + 1);
    }

    //判断是否是excel
    public static boolean isExcel(String url){
        Pattern p=Pattern.compile("\\.(xls|XLS)");
        Matcher m=p.matcher(url);
        if(m.find()){
            return true;
        }
        return false;
    }

    /**
     * MultipartFile 转 File
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile( @RequestParam MultipartFile file ) throws Exception {

        File toFile = null;
        if(file.equals("")||file.getSize()<=0){
            file = null;
        }else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Workbook getExcelWorkbook(String filePath) throws IOException {
        Workbook book = null;
        File file  = null;
        FileInputStream fis = null;

        try {
            file = new File(filePath);
            if(!file.exists()){
                throw new RuntimeException("文件不存在");
            }else{
                fis = new FileInputStream(file);
                book = WorkbookFactory.create(fis);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if(fis != null){
                fis.close();
            }
        }
        return book;
    }

    public static Sheet getSheetByNum(Workbook book,int number){
        Sheet sheet = null;
        try {
            sheet = book.getSheetAt(number);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return sheet;
    }
}
