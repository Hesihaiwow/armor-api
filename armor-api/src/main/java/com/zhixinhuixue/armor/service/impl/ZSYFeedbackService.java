package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.*;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.reflections.Reflections.collect;

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

    @Autowired
    private IZSYTaskUserMapper taskUserMapper;


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

    /**
     * 获取新需求列表
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<DemandResDTO> getDemandList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNum(),ZSYConstants.PAGE_SIZE);

        Long user = null;
        if (!Strings.isNullOrEmpty(reqDTO.getUser())){
            user = Long.valueOf(reqDTO.getUser());
        }
        Page<DemandBO> demandBOS = feedbackMapper.selectDemandList(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getReadStatus(),reqDTO.getType(),user);
        Page<DemandResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandBOS)){
            BeanUtils.copyProperties(demandBOS,list);
            demandBOS.stream().forEach(demandBO -> {
                DemandResDTO demandResDTO = new DemandResDTO();
                BeanUtils.copyProperties(demandBO,demandResDTO);
                list.add(demandResDTO);
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * 获取驳回需求列表
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<DemandRejectedResDTO> getDemandRejectedList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNum(),ZSYConstants.PAGE_SIZE);

        Long user = null;
        if (!Strings.isNullOrEmpty(reqDTO.getUser())){
            user = Long.valueOf(reqDTO.getUser());
        }
        List<DemandRejectedBO> demandRejectedBOS = feedbackMapper.selectDemandRejectedList(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getReadStatus(),reqDTO.getType(),user);
        List<DemandRejectedResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(demandRejectedBOS)){

            BeanUtils.copyProperties(demandRejectedBOS,list);
            demandRejectedBOS.stream().forEach(demandRejectedBO -> {
                DemandRejectedResDTO resDTO = new DemandRejectedResDTO();
                BeanUtils.copyProperties(demandRejectedBO,resDTO);
                String name = feedbackMapper.selectRejectUser(demandRejectedBO.getRejectUser());
                resDTO.setRejectUser(name);
                list.add(resDTO);
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * 获取排队需求列表
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<DemandQueuedResDTO> getDemandQueuedList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNum(),ZSYConstants.PAGE_SIZE);

        Long user = null;
        if (!Strings.isNullOrEmpty(reqDTO.getUser())){
            user = Long.valueOf(reqDTO.getUser());
        }
        List<DemandQueuedBO> demandQueuedBOS = feedbackMapper.selectDemandQueuedList(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getReadStatus(),reqDTO.getType(),user);
        List<DemandQueuedResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(demandQueuedBOS)){
            BeanUtils.copyProperties(demandQueuedBOS,list);
            demandQueuedBOS.stream().forEach(demandQueuedBO -> {
                DemandQueuedResDTO resDTO = new DemandQueuedResDTO();
                BeanUtils.copyProperties(demandQueuedBO,resDTO);
                list.add(resDTO);
            });
        }
        return new PageInfo<>(list);
    }

    /**
     * 获取完成需求列表
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<DemandCompletedResDTO> getDemandCompletedList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNum(),ZSYConstants.PAGE_SIZE);

        Long user = null;
        if (!Strings.isNullOrEmpty(reqDTO.getUser())){
            user = Long.valueOf(reqDTO.getUser());
        }
        Long chargeMan = null;
        if (!Strings.isNullOrEmpty(reqDTO.getChargeMan())){
            chargeMan = Long.valueOf(reqDTO.getChargeMan());
        }
        Page<DemandCompletedBO> demandCompletedBOS = feedbackMapper.selectDemandCompletedList(reqDTO.getBeginTime(),reqDTO.getEndTime(),user,chargeMan);
        Page<DemandCompletedResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandCompletedBOS)){
            BeanUtils.copyProperties(demandCompletedBOS,list);
            demandCompletedBOS.stream().forEach(demandCompletedBO -> {
                DemandCompletedResDTO resDTO = new DemandCompletedResDTO();
                BeanUtils.copyProperties(demandCompletedBO,resDTO);

                //查询需求提出人
                String createBy = feedbackMapper.selectUserById(demandCompletedBO.getId());
                resDTO.setCreateBy(createBy);

                //根据feedback_id查询task  按任务创建时间升序排序
                List<Long> taskIds = feedbackMapper.selectTasks(demandCompletedBO.getId());

                //根据feedback_id查询task  按任务结束时间降序排序
                List<Long> endTasks = feedbackMapper.selectEndTasks(demandCompletedBO.getId());

                if (!CollectionUtils.isEmpty(taskIds)){
                    resDTO.setTaskNum(taskIds.size());

                    //第一个即为最早创建的任务
                    Date beginDate = feedbackPlanTaskMapper.selectEndTime(taskIds.get(0));

                    //获取最后完成任务的时间
                    Date finishDate = feedbackPlanTaskMapper.selectEndTime(endTasks.get(0));
                    if (beginDate != null && finishDate != null){
                        Long weeks = (finishDate.getTime()-beginDate.getTime())/1000/3600/24/7;
                        if (weeks > 0){
                            resDTO.setWorkedTime(weeks);
                        }else {
                            resDTO.setWorkedTime(1L);
                        }
                        //设置任务开始时间
                        resDTO.setStartTime(beginDate);

                    }
                    Set<Long> set = new HashSet<>();
                    for (Long taskId : taskIds) {
                        List<Long> persons = feedbackPlanTaskMapper.selectPersonByTaskId(taskId);
                        set.addAll(persons);
                    }
                    //设置开发人数

                    if (!CollectionUtils.isEmpty(set)){
                        resDTO.setWorkerNum(set.size());
                    }else {
                        resDTO.setWorkerNum(taskIds.size());
                    }
                }
                list.add(resDTO);
            });
        }
        return new PageInfo(list);
    }

    /**
     * 获取我参与的需求
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<DemandJoinedResDTO> getDemandJoinedList(DemandQueryReqDTO reqDTO) {

        PageHelper.startPage(reqDTO.getPageNum(),ZSYConstants.PAGE_SIZE);

        Long chargeMan = null;
        if (!Strings.isNullOrEmpty(reqDTO.getChargeMan())){
            chargeMan = Long.valueOf(reqDTO.getChargeMan());
        }
        Long createMan = null;
        if (!Strings.isNullOrEmpty(reqDTO.getUser())){
            createMan = Long.valueOf(reqDTO.getUser());
        }
        //我参与的需求
        Page<DemandJoinedBO> demands = null;
        if (ZSYTokenRequestContext.get().getUserRole()> ZSYUserRole.PROJECT_MANAGER.getValue()){
            //此时,用户属于普通用户,我参与的即为我参与开发的
             demands = feedbackMapper.selectDemandJoinedList(reqDTO.getBeginTime(),reqDTO.getEndTime(),chargeMan,createMan,ZSYTokenRequestContext.get().getUserId());
        }else {
            //此时,用户为项目经理或超管,参与的需求即为提出的需求
            demands = feedbackMapper.selectDemandCreateList(reqDTO.getBeginTime(),reqDTO.getEndTime(),chargeMan,ZSYTokenRequestContext.get().getUserId());
        }



        Page<DemandJoinedResDTO> set = new Page<>();
        if (!CollectionUtils.isEmpty(demands)){
            BeanUtils.copyProperties(demands,set);
            demands.stream().forEach(demandJoinedBO -> {
                DemandJoinedResDTO resDTO = new DemandJoinedResDTO();
                BeanUtils.copyProperties(demandJoinedBO,resDTO);
                String createBy = feedbackMapper.selectUserById(demandJoinedBO.getId());
                resDTO.setCreateBy(createBy);
                Integer replyNum = feedbackMapper.selectReplyNum(demandJoinedBO.getId());
                resDTO.setReplyNum(replyNum);
                set.add(resDTO);
            });
        }
        return new PageInfo<>(set);
    }

    /**
     * 获取进行中需求列表
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<DemandRunningResDTO> getDemandRunningList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNum(),ZSYConstants.PAGE_SIZE);

        Long user = null;
        if (!Strings.isNullOrEmpty(reqDTO.getUser())){
            user = Long.valueOf(reqDTO.getUser());
        }
        Long chargeMan = null;
        if (!Strings.isNullOrEmpty(reqDTO.getChargeMan())){
            chargeMan = Long.valueOf(reqDTO.getChargeMan());
        }
        Page<DemandRunningBO> demandRunningBOS = feedbackMapper.selectDemandRunningList(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getReadStatus(),reqDTO.getType(),user,chargeMan);
        Page<DemandRunningResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandRunningBOS)){
            BeanUtils.copyProperties(demandRunningBOS,list);
            demandRunningBOS.stream().forEach(demandRunningBO -> {
                DemandRunningResDTO resDTO = new DemandRunningResDTO();
                BeanUtils.copyProperties(demandRunningBO,resDTO);
                Long planId = feedbackPlanMapper.selectPlanById(demandRunningBO.getId());
                resDTO.setPlanId(planId);

                //查询需求提出人
                String createBy = feedbackMapper.selectUserById(demandRunningBO.getId());
                resDTO.setCreateBy(createBy);

                //根据feedback_id查询task  按任务创建时间降序排序
                List<Long> taskIds = feedbackMapper.selectTasks(demandRunningBO.getId());
                if (!CollectionUtils.isEmpty(taskIds)){
                    resDTO.setTaskNum(taskIds.size());

                    Date beginTime = feedbackPlanTaskMapper.selectBeginTime(taskIds.get(0));
                    if (beginTime != null){
                        Date today = new Date();

                        Long weeks = (today.getTime()-beginTime.getTime())/1000/3600/24/7;
                        if (weeks > 0){
                            resDTO.setWorkedWeeks(weeks);
                        }else {
                            resDTO.setWorkedWeeks(1L);
                        }


                        //设置任务开始时间
                        resDTO.setStartTime(beginTime);
                    }
                    Set<Long> set = new HashSet<>();
                    for (Long taskId : taskIds) {
                        List<Long> persons = feedbackPlanTaskMapper.selectPersonByTaskId(taskId);
                        set.addAll(persons);
                    }
                    //设置开发人数

                    if (!CollectionUtils.isEmpty(set)){
                        resDTO.setWorkerNum(set.size());
                    }else {
                        resDTO.setWorkerNum(taskIds.size());
                    }
                }
                list.add(resDTO);
            });
        }
        return new PageInfo<>(list);
    }



    /**
     * 获取需求详情
     * @param id
     * @return
     */
    @Override
    public DemandDetailResDTO getDemandDetail(String id,String stat) {
        Long demandId = Long.valueOf(id);
        //查询需求详情基本信息
        DemandDetailBO demandDetailBO = feedbackMapper.selectDemandDetail(demandId,Integer.valueOf(stat));
        DemandDetailResDTO dto = new DemandDetailResDTO();
        BeanUtils.copyProperties(demandDetailBO,dto);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dto.setCreateTime(dateFormat.format(demandDetailBO.getCreateTime()));
        if (demandDetailBO.getReleaseTime() != null){
            dto.setReleaseTime(dateFormat.format(demandDetailBO.getReleaseTime()));
        }


        //查询点赞人
        List<String> likesPeople = feedbackMapper.selectLikesPeople(demandId);
        dto.setLikesPeople(likesPeople);

        //查询已读人
        List<String> readPeople = feedbackMapper.selectReadPeople(demandId);
        if (!CollectionUtils.isEmpty(readPeople)){
            int readNum = readPeople.size();
            dto.setReadNum(readNum);
        }else {
            dto.setReadNum(0);
        }
        dto.setReadPeople(readPeople);
        return dto;
    }

    /**
     * 获取需求回复详情
     * @param id
     * @return
     */
    @Override
    public List<DemandReplyResDTO> getDemandReply(String id) {
        List<DemandReplyBO> replyBOS = feedbackMapper.selectDemandReply(Long.valueOf(id));
        List<DemandReplyResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(replyBOS)){
            BeanUtils.copyProperties(replyBOS,list);
            replyBOS.stream().forEach(replyBO -> {
                DemandReplyResDTO resDTO = new DemandReplyResDTO();
                BeanUtils.copyProperties(replyBO,resDTO);
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 点赞
     * @param id
     */
    @Override
    public void like(String id) {
        Long demandId = Long.valueOf(id);
        //新增feedback_likes
        feedbackMapper.insertDemandLikes(demandId,ZSYTokenRequestContext.get().getUserId(),snowFlakeIDHelper.nextId(),new Date());

        //feedback中likesNum +1
        if (feedbackMapper.updateLikesNum(demandId) == 0){
            throw new ZSYServiceException("点赞失败");
        }
    }

    /**
     * 回复需求
     * @param reqDTO
     */
    @Override
    public void reply(DemandReplyReqDTO reqDTO) {
        feedbackMapper.insertReply(Long.valueOf(reqDTO.getDemandId()),ZSYTokenRequestContext.get().getUserId(),reqDTO.getContent()
                ,snowFlakeIDHelper.nextId(),new Date());
    }

    /**
     * 采纳需求,进入排队中
     * @param id
     */
    @Override
    public void agreeDemand(String id) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }

        Long demandId = Long.valueOf(id);
        //将feedback中的status设置为4(排队中)
        if (feedbackMapper.updateStatus(demandId,ZSYFeedbackStatus.QUEUE.getValue()) == 0){
            throw new ZSYServiceException("采纳需求失败");
        }

        //新增需求到feedback_queue
        feedbackMapper.insertDemandQueue(snowFlakeIDHelper.nextId(),demandId,ZSYTokenRequestContext.get().getUserId(),new Date());

        //删除feedback_rejected中的需求
        feedbackMapper.deleteDemandRejected(demandId);
    }

    /**
     * 驳回需求,进入驳回表中
     * @param id
     */
    @Override
    public void rejectDemand(String id) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }

        Long demandId = Long.valueOf(id);
        //将feedback中的status设置为3(驳回)
        if (feedbackMapper.updateStatus(demandId,ZSYFeedbackStatus.REJECT.getValue()) == 0){
            throw new ZSYServiceException("需求驳回失败");
        }

        //新增需求到feedback_rejected
        feedbackMapper.insertDemandReject(snowFlakeIDHelper.nextId(),demandId,ZSYTokenRequestContext.get().getUserId(),new Date());

        //删除feedback_queue中的需求
        feedbackMapper.deleteDemandQueue(demandId);
    }

    /**
     * 新增需求
     * @param reqDTO
     */
    @Override
    public void addDemand(DemandReqDTO reqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }

        Demand demand = new Demand();
        BeanUtils.copyProperties(reqDTO,demand);

        //新增需求时,不设置项目,暂时设置projectId为0L,再计划中加入项目
        demand.setId(snowFlakeIDHelper.nextId());
        demand.setProjectId(0L);
        demand.setFeedbackTime(new Date());
        demand.setStatus(ZSYFeedbackStatus.NEW.getValue());
        demand.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        demand.setCreateTime(new Date());
        demand.setUpdateTime(new Date());
        demand.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        demand.setReadStatus(0);
        demand.setContent("");
        feedbackMapper.insertDemand(demand);

        if (!CollectionUtils.isEmpty(reqDTO.getUrlList())){
            //把附件地址添加到feedback_accessory
            List<String> urls = reqDTO.getUrlList();
            List<DemandAccessory> list = new ArrayList<>();
            for (String url : urls) {
                DemandAccessory demandAccessory = new DemandAccessory();
                demandAccessory.setId(snowFlakeIDHelper.nextId());
                demandAccessory.setDemandId(demand.getId());
                demandAccessory.setUrl(url);
                demandAccessory.setCreateTime(new Date());
                list.add(demandAccessory);
            }
            feedbackMapper.insertFeedbackAccessory(list);
        }



    }

    /**
     * 读取需求
     * @param id
     */
    @Override
    public void readDemand(String id) {
        Long demandId = Long.valueOf(id);
        //设置readStatus为已读(1)
        if (feedbackMapper.updateReadStatus(demandId) == 0){
            throw new ZSYServiceException("更新已读状态失败");
        }

        //添加feedback_read
        feedbackMapper.insertFeedbackRead(snowFlakeIDHelper.nextId(),demandId,ZSYTokenRequestContext.get().getUserId(),new Date());
    }

    /**
     * 查询提出人员列表
     * @return
     */
    @Override
    public List<IntroducerResDTO> getIntroducerList() {
        return feedbackMapper.selectIntroducerList();
    }

    @Override
    public void editDemand(String id, DemandReqDTO reqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        Demand demand = feedbackMapper.selectDemandById(Long.valueOf(id));
        BeanUtils.copyProperties(reqDTO,demand);
        demand.setUpdateTime(new Date());
        if (feedbackMapper.updateByDemandId(demand) == 0){
            throw new ZSYServiceException("需求更新失败");
        }
    }

    /**
     * 查看需求是否已读
     * @param id
     * @return
     */
    @Override
    public DemandIsOperateResDTO isRead(String id) {
        Integer count = feedbackMapper.selectIsRead(Long.valueOf(id),ZSYTokenRequestContext.get().getUserId());
        DemandIsOperateResDTO demandIsReadResDTO = new DemandIsOperateResDTO();
        demandIsReadResDTO.setCount(count);
        return demandIsReadResDTO;
    }

    /**
     * 查看需求是否已点赞
     * @param id
     * @return
     */
    @Override
    public DemandIsOperateResDTO isLike(String id) {
        Integer count = feedbackMapper.selectIsLike(Long.valueOf(id),ZSYTokenRequestContext.get().getUserId());
        DemandIsOperateResDTO demandIsLikeResDTO = new DemandIsOperateResDTO();
        demandIsLikeResDTO.setCount(count);
        return demandIsLikeResDTO;
    }

    /*
    新增需求所属项目
     */
    @Override
    public void addDemandProject(DemandProjectReqDTO reqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        if (feedbackMapper.updateDemandProject(Long.valueOf(reqDTO.getId()),Long.valueOf(reqDTO.getProjectId())) == 0){
            throw new ZSYServiceException("新增需求所属项目失败");
        }
    }

    /**
     * 获取需求所属项目
     * @param id
     * @return
     */
    @Override
    public DemandProjectResDTO getProjectId(String id) {
        Long projectId = feedbackMapper.selectProjectId(Long.valueOf(id));
        DemandProjectResDTO demandProjectResDTO = new DemandProjectResDTO();
        demandProjectResDTO.setProjectId(projectId);
        return demandProjectResDTO;
    }

    /**
     * 需求是否已采纳
     * @param id
     * @return
     */
    @Override
    public DemandIsOperateResDTO isAgree(String id) {
        Integer count = feedbackMapper.selectIsAgree(Long.valueOf(id));
        DemandIsOperateResDTO demandIsReadResDTO = new DemandIsOperateResDTO();
        demandIsReadResDTO.setCount(count);
        return demandIsReadResDTO;
    }

    /**
     * 需求是否驳回
     * @param id
     * @return
     */
    @Override
    public DemandIsOperateResDTO isReject(String id) {
        Long demandId = Long.valueOf(id);
        Integer count = feedbackMapper.isReject(demandId);
        DemandIsOperateResDTO demandIsReadResDTO = new DemandIsOperateResDTO();
        demandIsReadResDTO.setCount(count);
        return demandIsReadResDTO;
    }

    /**
     * 获取需求附件
     * @param id
     * @return
     */
    @Override
    public List<String> getUrl(String id) {
        List<String> urls = feedbackMapper.selectUrlsById(Long.valueOf(id));
        return urls;
    }

    /**
     * 上线
     * @param id
     */
    @Override
    public void demandOnline(String id) {
        if (feedbackMapper.updateOnlineTime(Long.valueOf(id),new Date()) == 0){
            throw new ZSYServiceException("上线失败");
        }
    }

    /**
     * 需求是否上线
     * @param id
     * @return
     */
    @Override
    public DemandOnlineResDTO isOnline(String id) {
        Date onlineTime = feedbackMapper.selectOnlineTime(Long.valueOf(id));
        DemandOnlineResDTO resDTO = new DemandOnlineResDTO();
        if (onlineTime != null){
            resDTO.setOnlineTime(onlineTime);
        }else {
            resDTO.setOnlineTime(null);
        }
        return resDTO;
    }

    /**
     * 查看学管是否读取需求
     * @param id
     * @param userId
     * @return
     */
    @Override
    public DemandIsOperateResDTO isReadByCoach(String id, String userId) {
        Integer count = feedbackMapper.selectIsRead(Long.valueOf(id),Long.valueOf(userId));
        DemandIsOperateResDTO demandIsReadResDTO = new DemandIsOperateResDTO();
        demandIsReadResDTO.setCount(count);
        return demandIsReadResDTO;
    }

    @Override
    @Transactional
    public void readDemandByCoach(String id,String userId) {
        Long demandId = Long.valueOf(id);
        //设置readStatus为已读(1)
        if (feedbackMapper.updateReadStatus(demandId) == 0){
            throw new ZSYServiceException("更新已读状态失败");
        }

        //添加feedback_read
        feedbackMapper.insertFeedbackRead(snowFlakeIDHelper.nextId(),demandId,Long.valueOf(userId),new Date());
    }

    @Override
    public DemandIsOperateResDTO isLikeByCoach(String id, String userId) {
        Integer count = feedbackMapper.selectIsRead(Long.valueOf(id),Long.valueOf(userId));
        DemandIsOperateResDTO demandIsReadResDTO = new DemandIsOperateResDTO();
        demandIsReadResDTO.setCount(count);
        return demandIsReadResDTO;
    }

    @Override
    @Transactional
    public void likeDemandByCoach(String id, String userId) {
        Long demandId = Long.valueOf(id);
        //新增feedback_likes
        feedbackMapper.insertDemandLikes(demandId,Long.valueOf(userId),snowFlakeIDHelper.nextId(),new Date());

        //feedback中likesNum +1
        if (feedbackMapper.updateLikesNum(demandId) == 0){
            throw new ZSYServiceException("点赞失败");
        }
    }

    @Override
    public void replyDemandByCoach(DemandReplyReqDTO reqDTO, String userId) {
        feedbackMapper.insertReply(Long.valueOf(reqDTO.getDemandId()),Long.valueOf(userId),reqDTO.getContent()
                ,snowFlakeIDHelper.nextId(),new Date());
    }

    @Override
    public void addDemandByCoach(DemandReqDTO reqDTO, String userId) {
        Demand demand = new Demand();
        //新增需求时,不设置项目,暂时设置projectId为0L,再计划中加入项目
        demand.setId(snowFlakeIDHelper.nextId());
        demand.setProjectId(0L);
        demand.setFeedbackTime(new Date());
        demand.setStatus(ZSYFeedbackStatus.NEW.getValue());
        demand.setCreateBy(Long.valueOf(userId));
        demand.setCreateTime(new Date());
        demand.setUpdateTime(new Date());
        demand.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        demand.setReadStatus(0);
        demand.setContent("");
        feedbackMapper.insertDemand(demand);

        if (!CollectionUtils.isEmpty(reqDTO.getUrlList())){
            //把附件地址添加到feedback_accessory
            List<String> urls = reqDTO.getUrlList();
            List<DemandAccessory> list = new ArrayList<>();
            for (String url : urls) {
                DemandAccessory demandAccessory = new DemandAccessory();
                demandAccessory.setId(snowFlakeIDHelper.nextId());
                demandAccessory.setDemandId(demand.getId());
                demandAccessory.setUrl(url);
                demandAccessory.setCreateTime(new Date());
                list.add(demandAccessory);
            }
            feedbackMapper.insertFeedbackAccessory(list);
        }
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
        feedback.setStatus(ZSYFeedbackStatus.NEW.getValue());
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

        feedbackPlanMapper.deleteFeedbackPlan(feedbackId);

        if (feedbackMapper.updateByFeedbackId(feedback)== 0) {
            throw new ZSYServiceException("需求删除失败");
        }
    }

    /**
     * 添加计划
     * @param feedbackPlanReqDTO
     */
    @Override
    @Transactional
    public void addFeedbackPlan(FeedbackPlanReqDTO feedbackPlanReqDTO) {
        if(feedbackPlanReqDTO.getId()==null){
            FeedbackPlan feedbackPlan = new FeedbackPlan();
            BeanUtils.copyProperties(feedbackPlanReqDTO,feedbackPlan);
            feedbackPlan.setCreateTime(new Date());
            feedbackPlan.setUpdateTime(new Date());
            feedbackPlan.setId(snowFlakeIDHelper.nextId());
            feedbackPlanReqDTO.setId(feedbackPlan.getId());
            feedbackPlanMapper.insertFeedbackPlan(feedbackPlan);
        }else{
            FeedbackPlan feedbackPlan = feedbackPlanMapper.selectById(feedbackPlanReqDTO.getId());
            BeanUtils.copyProperties(feedbackPlanReqDTO , feedbackPlan);
            feedbackPlan.setUpdateTime(new Date());
            if(feedbackPlanMapper.updatePlan(feedbackPlan)==0){
                throw new ZSYServiceException("计划修改失败");
            }

        }

        //需求任务关联
        if(feedbackPlanReqDTO.getPlanTask().size()>0){
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
        }

        Feedback feedback = feedbackMapper.selectById(feedbackPlanReqDTO.getFeedbackId());
        feedback.setId(feedbackPlanReqDTO.getFeedbackId());
        feedback.setUpdateTime(new Date());
        feedback.setStatus(ZSYTaskStatus.DOING.getValue());
        if (feedbackMapper.updateByFeedbackId(feedback)== 0) {
            throw new ZSYServiceException("需求修改失败");
        }

    }

    /**
     * 计划信息
     */
    @Override
    public FeedbackPlanResDTO getFeedbackPlan(Long feedbackId){
        List<FeedbackPlanBO> feedbackPlanBOS = feedbackPlanMapper.getFeedbackPlanById(feedbackId);
        FeedbackPlanResDTO feedbackPlanResDTO =  new FeedbackPlanResDTO();

        if(feedbackPlanBOS.size()>0){
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
        }else{
            FeedbackPlan feedbackPlan = feedbackPlanMapper.selectByFeedbackId(feedbackId);
            if(feedbackPlan!=null){
                BeanUtils.copyProperties(feedbackPlan,feedbackPlanResDTO);
            }
        }

        return feedbackPlanResDTO;
    }

    @Override
    public void editTaskStatus(Long feedbackId, Long taskId) {
        List<FeedbackPlanBO> feedbackPlanBOS = feedbackPlanMapper.getFeedbackPlanById(feedbackId);
        //查看是否关联，无关联则添加关联，否则删除关联
        if(feedbackPlanBOS.size()==0 ||feedbackPlanTaskMapper.getTaskCount(feedbackPlanBOS.get(0).getId(),taskId)==0){
            FeedbackPlanTask feedbackPlanTask  = new FeedbackPlanTask();
            feedbackPlanTask.setId(snowFlakeIDHelper.nextId());
            feedbackPlanTask.setFeedbackPlanId(feedbackPlanMapper.selectByFeedbackId(feedbackId).getId());
            feedbackPlanTask.setTaskId(taskId);

            feedbackPlanTaskMapper.insertFeedbackPlanTask(feedbackPlanTask);
        }else{
            feedbackPlanTaskMapper.deleteByTaskId(taskId);
            if(feedbackPlanBOS.size()==1){
                Feedback feedback = feedbackMapper.selectById(feedbackId);
                feedback.setUpdateTime(new Date());
                feedback.setId(feedbackId);
                feedback.setStatus(ZSYFeedbackStatus.QUEUE.getValue());

                if (feedbackMapper.updateByFeedbackId(feedback) == 0) {
                    throw new ZSYServiceException("需求更新失败");
                }
            }
        }

    }



}
