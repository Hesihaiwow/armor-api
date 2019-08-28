package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYBugManageMapper;
import com.zhixinhuixue.armor.dao.IZSYBugUserMapper;
import com.zhixinhuixue.armor.dao.IZSYUserIntegralMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.BugManageBO;
import com.zhixinhuixue.armor.model.bo.BugManageListBO;
import com.zhixinhuixue.armor.model.bo.OnlineBugBO;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugManageAddReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugUserReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYBugService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYIntegralOrigin;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
                String bugNoStr = "";
                Integer bugNo = onlineBugBO.getBugNo();
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
        bugManage.setCreateTime(new Date());
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
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setId(snowFlakeIDHelper.nextId());
            userIntegral.setUserId(user.getUserId());
            userIntegral.setIntegral(Optional.ofNullable(user.getIntegral()).orElse(BigDecimal.ZERO));
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
        bugManage.setCreateTime(bugManageBO.getCreateTime());
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

        reqDTO.getBugUsers().forEach(user -> {
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setId(snowFlakeIDHelper.nextId());
            userIntegral.setUserId(user.getUserId());
            userIntegral.setIntegral(Optional.ofNullable(user.getIntegral()).orElse(BigDecimal.ZERO));
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
        List<UserIntegral> userIntegrals = userIntegralMapper.getUserIntegralByTaskId(id);
        userIntegrals.forEach(userIntegral -> {
            // 修改用户积分
            User userBug = userMapper.selectById(userIntegral.getUserId());
            BigDecimal currentIntegral = userBug.getIntegral();
            User userBO = new User();
            userBO.setId(userBug.getId());
            userBO.setIntegral(currentIntegral.subtract(userIntegral.getIntegral()));
            userMapper.updateSelectiveById(userBO);
            if (userIntegralMapper.deleteUserIntegral(id , userIntegral.getUserId()) == 0) {
                throw new ZSYServiceException("删除积分信息失败");
            }
        });

        if (bugUserMapper.deleteById(id) == 0) {
            throw new ZSYServiceException("删除Bug用户处理记录失败");
        }
    }

}
