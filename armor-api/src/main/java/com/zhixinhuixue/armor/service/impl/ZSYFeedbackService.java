package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.mail.util.BEncoderStream;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.helper.ZSYQinuHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYQinuOssProperty;
import com.zhixinhuixue.armor.source.enums.*;
import io.swagger.models.auth.In;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private ZSYQinuOssProperty qinuOssProperty;

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
    public ArmorPageInfo<DemandResDTO> getDemandList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);

        Page<DemandBO> demandBOS = feedbackMapper.selectDemandPage(reqDTO.getOrigin(),reqDTO.getPriority()
                ,reqDTO.getType(),reqDTO.getFromCoach(),reqDTO.getFbTimeStart(),reqDTO.getFbTimeEnd());
        Page<DemandResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandBOS)){
            BeanUtils.copyProperties(demandBOS,list);
            demandBOS.stream().forEach(demandBO -> {
                DemandResDTO demandResDTO = new DemandResDTO();
                BeanUtils.copyProperties(demandBO,demandResDTO);
                if (feedbackMapper.selectIsRead(demandBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                    demandResDTO.setReadStatus(1);
                }else {
                    demandResDTO.setReadStatus(0);
                }
                list.add(demandResDTO);
            });
        }

        //根据读取状态过滤  重新查询数据   然后内存分页
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            List<DemandResDTO> demandResDTOS = new ArrayList<>();
            List<DemandResDTO> filterList = new ArrayList<>();
            List<DemandBO> demandBOList = feedbackMapper.selectDemandList(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getType()
                    ,reqDTO.getFromCoach(),reqDTO.getFbTimeStart(),reqDTO.getFbTimeEnd());
            if (!CollectionUtils.isEmpty(demandBOList)){
                BeanUtils.copyProperties(demandBOList,demandResDTOS);
                demandBOList.stream().forEach(demandBO -> {
                    DemandResDTO demandResDTO = new DemandResDTO();
                    BeanUtils.copyProperties(demandBO,demandResDTO);
                    if (feedbackMapper.selectIsRead(demandBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                        demandResDTO.setReadStatus(1);
                    }else {
                        demandResDTO.setReadStatus(0);
                    }
                    demandResDTOS.add(demandResDTO);
                });
                filterList.addAll(demandResDTOS.stream().filter(demandResDTO ->
                        demandResDTO.getReadStatus() == reqDTO.getReadStatus()).collect(Collectors.toList()));

                ArmorPageInfo<DemandResDTO> page = ArmorPageInfo.pageByMemory(filterList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
                int current = page.getCurrent();
                List<DemandResDTO> result = page.getList();
                long totalSize = page.getTotalSize();
                return new ArmorPageInfo(current,ZSYConstants.PAGE_SIZE, totalSize,result);
            }
        }
        return new ArmorPageInfo<>(list);
    }

    /**
     * 学管端获取新需求
     * @param reqDTO
     * @return
     */
    @Override
    public ArmorPageInfo<DemandResDTO> getDemandListByCoach(DemandQueryReqDTO reqDTO,Integer coachId) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<DemandBO> demandBOS = feedbackMapper.selectDemandPageByCoach(reqDTO);
        Page<DemandResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandBOS)){
            BeanUtils.copyProperties(demandBOS,list);
            demandBOS.stream().forEach(demandBO -> {
                DemandResDTO demandResDTO = new DemandResDTO();
                BeanUtils.copyProperties(demandBO,demandResDTO);
                if (feedbackMapper.selectIsReadByCoach(demandBO.getId(),coachId) != null){
                    demandResDTO.setReadStatus(1);
                }else {
                    demandResDTO.setReadStatus(0);
                }
                list.add(demandResDTO);
            });
        }

        //根据读取状态进行过滤   重新查询数据,内存分页
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            List<DemandBO> demandBOList = feedbackMapper.selectDemandByListCoach(reqDTO);
            List<DemandResDTO> demandResDTOList = new ArrayList<>();
            List<DemandResDTO> filterList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(demandBOList)){
                BeanUtils.copyProperties(demandBOList,demandResDTOList);
                demandBOList.stream().forEach(demandBO -> {
                    DemandResDTO demandResDTO = new DemandResDTO();
                    BeanUtils.copyProperties(demandBO,demandResDTO);
                    if (feedbackMapper.selectIsReadByCoach(demandBO.getId(),coachId) != null){
                        demandResDTO.setReadStatus(1);
                    }else {
                        demandResDTO.setReadStatus(0);
                    }
                    demandResDTOList.add(demandResDTO);
                });
                //开始过滤
                filterList.addAll(demandResDTOList.stream().filter(demandResDTO -> demandResDTO.getReadStatus() == reqDTO.getReadStatus()).collect(Collectors.toList()));
                ArmorPageInfo<DemandResDTO> page = ArmorPageInfo.pageByMemory(filterList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
                int current = page.getCurrent();
                List<DemandResDTO> result = page.getList();
                long totalSize = page.getTotalSize();
                return new ArmorPageInfo<>(current,ZSYConstants.PAGE_SIZE,totalSize,result);
            }
        }
        return new ArmorPageInfo<>(list);
    }


    /**
     * 获取驳回需求列表
     * @param reqDTO
     * @return
     */
    @Override
    public ArmorPageInfo<DemandRejectedResDTO> getDemandRejectedList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);

        Page<DemandRejectedBO> demandRejectedBOS = feedbackMapper.selectDemandRejectedPage(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getType());
        Page<DemandRejectedResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandRejectedBOS)){

            BeanUtils.copyProperties(demandRejectedBOS,list);
            demandRejectedBOS.stream().forEach(demandRejectedBO -> {
                DemandRejectedResDTO resDTO = new DemandRejectedResDTO();
                BeanUtils.copyProperties(demandRejectedBO,resDTO);
                String name = feedbackMapper.selectUserNameByUserId(demandRejectedBO.getRejectUser());
                if (feedbackMapper.selectIsRead(demandRejectedBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                    resDTO.setReadStatus(1);
                }else {
                    resDTO.setReadStatus(0);
                }
                resDTO.setRejectUser(name);
                list.add(resDTO);
            });
        }

        //重新查询list  用于过滤已读状态  内存分页
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            List<DemandRejectedBO> demandRejectedBOList = feedbackMapper.selectDemandRejectedList(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getType());
            List<DemandRejectedResDTO> demandRejectedResDTOList = new ArrayList<>();
            List<DemandRejectedResDTO> filterList = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(demandRejectedBOList)){
                BeanUtils.copyProperties(demandRejectedBOList,demandRejectedResDTOList);
                demandRejectedBOList.stream().forEach(demandRejectedBO -> {
                    DemandRejectedResDTO demandRejectedResDTO = new DemandRejectedResDTO();
                    BeanUtils.copyProperties(demandRejectedBO,demandRejectedResDTO);
                    String name = feedbackMapper.selectUserNameByUserId(demandRejectedBO.getRejectUser());
                    demandRejectedResDTO.setRejectUser(name);
                    if (feedbackMapper.selectIsRead(demandRejectedBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                        demandRejectedResDTO.setReadStatus(1);
                    }else {
                        demandRejectedResDTO.setReadStatus(0);
                    }
                    demandRejectedResDTOList.add(demandRejectedResDTO);
                });

                //开始过滤
                filterList.addAll(demandRejectedResDTOList.stream().filter(demandRejectedResDTO -> demandRejectedResDTO.getReadStatus()==reqDTO.getReadStatus()).collect(Collectors.toList()));
                ArmorPageInfo<DemandRejectedResDTO> page = ArmorPageInfo.pageByMemory(filterList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
                int current = page.getCurrent();
                long totalSize = page.getTotalSize();
                List<DemandRejectedResDTO> result = page.getList();
                return new ArmorPageInfo<>(current,ZSYConstants.PAGE_SIZE,totalSize,result);
            }
        }
        return new ArmorPageInfo<>(list);
    }


    /**
     * 学管端获取驳回需求
     * @param reqDTO
     * @return
     */
    @Override
    public ArmorPageInfo<DemandRejectedResDTO> getDemandRejectedListByCoach(DemandQueryReqDTO reqDTO, Integer coachId) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);

        Page<DemandRejectedBO> demandRejectedBOS = feedbackMapper.selectDemandRejectedPageByCoach(reqDTO);
        Page<DemandRejectedResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandRejectedBOS)){

            BeanUtils.copyProperties(demandRejectedBOS,list);
            demandRejectedBOS.stream().forEach(demandRejectedBO -> {
                DemandRejectedResDTO resDTO = new DemandRejectedResDTO();
                BeanUtils.copyProperties(demandRejectedBO,resDTO);
                String name = feedbackMapper.selectUserNameByUserId(demandRejectedBO.getRejectUser());
                resDTO.setRejectUser(name);
                if (feedbackMapper.selectIsReadByCoach(demandRejectedBO.getId(),coachId) != null){
                    resDTO.setReadStatus(1);
                }else {
                    resDTO.setReadStatus(0);
                }
                list.add(resDTO);
            });
        }

        //重新查询驳回需求集合用于过滤是否已读  内存分页
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            List<DemandRejectedBO> demandRejectedBOList = feedbackMapper.selectDemandRejectedListByCoach(reqDTO);
            List<DemandRejectedResDTO> demandRejectedResDTOList = new ArrayList<>();
            List<DemandRejectedResDTO> filterList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(demandRejectedBOList)){
                BeanUtils.copyProperties(demandRejectedBOList,demandRejectedResDTOList);
                demandRejectedBOList.stream().forEach(demandRejectedBO -> {
                    DemandRejectedResDTO demandRejectedResDTO = new DemandRejectedResDTO();
                    BeanUtils.copyProperties(demandRejectedBO,demandRejectedResDTO);
                    String name = feedbackMapper.selectUserNameByUserId(demandRejectedBO.getRejectUser());
                    demandRejectedResDTO.setRejectUser(name);
                    if (feedbackMapper.selectIsReadByCoach(demandRejectedBO.getId(),coachId)!= null){
                        demandRejectedResDTO.setReadStatus(1);
                    }else {
                        demandRejectedResDTO.setReadStatus(0);
                    }
                    demandRejectedResDTOList.add(demandRejectedResDTO);
                });
                //开始过滤
                filterList.addAll(demandRejectedResDTOList.stream().filter(demandRejectedResDTO -> demandRejectedResDTO.getReadStatus() == reqDTO.getReadStatus()).collect(Collectors.toList()));
                ArmorPageInfo<DemandRejectedResDTO> page = ArmorPageInfo.pageByMemory(filterList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
                int current = page.getCurrent();
                long totalSize = page.getTotalSize();
                List<DemandRejectedResDTO> result = page.getList();
                return new ArmorPageInfo<>(current,ZSYConstants.PAGE_SIZE,totalSize,result);
            }
        }
        return new ArmorPageInfo<>(list);
    }



    /**
     * 获取排队需求列表
     * @param reqDTO
     * @return
     */
    @Override
    public ArmorPageInfo<DemandQueuedResDTO> getDemandQueuedList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);

        Page<DemandQueuedBO> demandQueuedBOS = feedbackMapper.selectDemandQueuedPage(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getType());
        Page<DemandQueuedResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandQueuedBOS)){
            BeanUtils.copyProperties(demandQueuedBOS,list);
            demandQueuedBOS.stream().forEach(demandQueuedBO -> {
                DemandQueuedResDTO resDTO = new DemandQueuedResDTO();
                BeanUtils.copyProperties(demandQueuedBO,resDTO);
                if (feedbackMapper.selectIsRead(demandQueuedBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                    resDTO.setReadStatus(1);
                }else {
                    resDTO.setReadStatus(0);
                }
                list.add(resDTO);
            });
        }

        //重新查询排队需求集合用于过滤读取状态  内存分页
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            List<DemandQueuedBO> demandQueuedBOList = feedbackMapper.selectDemandQueuedList(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getType());
            List<DemandQueuedResDTO> demandQueuedResDTOList = new ArrayList<>();
            List<DemandQueuedResDTO> filterList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(demandQueuedBOList)){
                BeanUtils.copyProperties(demandQueuedBOList,demandQueuedResDTOList);
                demandQueuedBOList.stream().forEach(demandQueuedBO -> {
                    DemandQueuedResDTO demandQueuedResDTO = new DemandQueuedResDTO();
                    BeanUtils.copyProperties(demandQueuedBO,demandQueuedResDTO);
                    if (feedbackMapper.selectIsRead(demandQueuedBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                        demandQueuedResDTO.setReadStatus(1);
                    }else {
                        demandQueuedResDTO.setReadStatus(0);
                    }
                    demandQueuedResDTOList.add(demandQueuedResDTO);
                });

                //开始过滤
                filterList.addAll(demandQueuedResDTOList.stream().filter(demandQueuedResDTO -> demandQueuedResDTO.getReadStatus() == reqDTO.getReadStatus()).collect(Collectors.toList()));
                ArmorPageInfo<DemandQueuedResDTO> page = ArmorPageInfo.pageByMemory(filterList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
                int current = page.getCurrent();
                long totalSize = page.getTotalSize();
                List<DemandQueuedResDTO> result = page.getList();
                return new ArmorPageInfo<>(current,ZSYConstants.PAGE_SIZE,totalSize,result);
            }
        }
        return new ArmorPageInfo<>(list);
    }

    /**
     * 学管端获取排队需求
     * @param reqDTO
     * @return
     */
    @Override
    public ArmorPageInfo<DemandQueuedResDTO> getDemandQueuedListByCoach(DemandQueryReqDTO reqDTO,Integer coachId) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<DemandQueuedBO> demandQueuedBOS = feedbackMapper.selectDemandQueuedPageByCoach(reqDTO);
        Page<DemandQueuedResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandQueuedBOS)){
            BeanUtils.copyProperties(demandQueuedBOS,list);
            demandQueuedBOS.stream().forEach(demandQueuedBO -> {
                DemandQueuedResDTO resDTO = new DemandQueuedResDTO();
                BeanUtils.copyProperties(demandQueuedBO,resDTO);
                if (feedbackMapper.selectIsReadByCoach(demandQueuedBO.getId(),coachId) != null){
                    resDTO.setReadStatus(1);
                }else {
                    resDTO.setReadStatus(0);
                }
                list.add(resDTO);
            });
        }
        //重新查询排队需求集合用于过滤读取状态  内存分页
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            List<DemandQueuedBO> demandQueuedBOList = feedbackMapper.selectDemandQueuedListByCoach(reqDTO);
            List<DemandQueuedResDTO> demandQueuedResDTOList = new ArrayList<>();
            List<DemandQueuedResDTO> filterList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(demandQueuedBOList)){
                BeanUtils.copyProperties(demandQueuedBOList,demandQueuedResDTOList);
                demandQueuedBOList.stream().forEach(demandQueuedBO -> {
                    DemandQueuedResDTO demandQueuedResDTO = new DemandQueuedResDTO();
                    BeanUtils.copyProperties(demandQueuedBO,demandQueuedResDTO);
                    if (feedbackMapper.selectIsReadByCoach(demandQueuedBO.getId(),coachId) != null){
                        demandQueuedResDTO.setReadStatus(1);
                    }else {
                        demandQueuedResDTO.setReadStatus(0);
                    }
                    demandQueuedResDTOList.add(demandQueuedResDTO);
                });
                //开始过滤
                filterList.addAll(demandQueuedResDTOList.stream().filter(demandQueuedResDTO -> demandQueuedResDTO.getReadStatus() == reqDTO.getReadStatus()).collect(Collectors.toList()));
                ArmorPageInfo<DemandQueuedResDTO> page = ArmorPageInfo.pageByMemory(filterList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
                int current = page.getCurrent();
                long totalSize = page.getTotalSize();
                List<DemandQueuedResDTO> result = page.getList();
                return new ArmorPageInfo<>(current,ZSYConstants.PAGE_SIZE,totalSize,result);
            }
        }
        return new ArmorPageInfo<>(list);
    }

    /**
     * 学管端获取已完成需求
     * @param reqDTO
     * @return
     */
    @Override
    public ArmorPageInfo<DemandCompletedResDTO> getDemandCompletedListByCoach(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<DemandCompletedBO> demandCompletedBOS = feedbackMapper.selectDemandCompletedPageByCoach(reqDTO);
        Page<DemandCompletedResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandCompletedBOS)){
            BeanUtils.copyProperties(demandCompletedBOS,list);
            demandCompletedBOS.stream().forEach(demandCompletedBO -> {
                DemandCompletedResDTO resDTO = new DemandCompletedResDTO();
                BeanUtils.copyProperties(demandCompletedBO,resDTO);

                //根据feedback_id查询task  按任务创建时间升序排序
                List<Long> taskIds = feedbackMapper.selectTasks(demandCompletedBO.getId());

                //根据feedback_id查询task  按任务结束时间降序排序
                List<Long> endTasks = feedbackMapper.selectEndTasks(demandCompletedBO.getId());

                if (!CollectionUtils.isEmpty(taskIds)){
                    resDTO.setTaskNum(taskIds.size());

                    //第一个即为最早创建的任务
                    Date beginDate = feedbackPlanTaskMapper.selectBeginTime(taskIds.get(0));

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
        return new ArmorPageInfo(list);
    }



    /**
     * 获取完成需求列表
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<DemandCompletedResDTO> getDemandCompletedList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);

        Page<DemandCompletedBO> demandCompletedBOS = feedbackMapper.selectDemandCompletedPage(reqDTO.getBeginTime(),reqDTO.getEndTime(),reqDTO.getChargeMan(),reqDTO.getOrigin());
        Page<DemandCompletedResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandCompletedBOS)){
            BeanUtils.copyProperties(demandCompletedBOS,list);
            demandCompletedBOS.stream().forEach(demandCompletedBO -> {
                DemandCompletedResDTO resDTO = new DemandCompletedResDTO();
                BeanUtils.copyProperties(demandCompletedBO,resDTO);

                //根据feedback_id查询task  按任务创建时间升序排序
                List<Long> taskIds = feedbackMapper.selectTasks(demandCompletedBO.getId());

                //根据feedback_id查询task  按任务结束时间降序排序
                List<Long> endTasks = feedbackMapper.selectEndTasks(demandCompletedBO.getId());

                if (!CollectionUtils.isEmpty(taskIds)){
                    resDTO.setTaskNum(taskIds.size());

                    //第一个即为最早创建的任务
                    Date beginDate = feedbackPlanTaskMapper.selectBeginTime(taskIds.get(0));

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

        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);

        //我参与的需求
        Page<DemandJoinedBO> demands = null;
        if (ZSYTokenRequestContext.get().getUserRole()> ZSYUserRole.PROJECT_MANAGER.getValue()){
            //此时,用户属于普通用户,我参与的即为我参与开发的
             demands = feedbackMapper.selectDemandDevelopPage(reqDTO.getBeginTime(),reqDTO.getEndTime(),reqDTO.getChargeMan(),reqDTO.getOrigin(),ZSYTokenRequestContext.get().getUserId());
        }else {
            //此时,用户为项目经理或超管,参与的需求即为提出的需求
            demands = feedbackMapper.selectDemandCreatePage(reqDTO.getBeginTime(),reqDTO.getEndTime(),reqDTO.getChargeMan(),reqDTO.getOrigin(),ZSYTokenRequestContext.get().getUserId());
        }



        Page<DemandJoinedResDTO> set = new Page<>();
        if (!CollectionUtils.isEmpty(demands)){
            BeanUtils.copyProperties(demands,set);
            demands.stream().forEach(demandJoinedBO -> {
                DemandJoinedResDTO resDTO = new DemandJoinedResDTO();
                BeanUtils.copyProperties(demandJoinedBO,resDTO);
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
    public ArmorPageInfo<DemandRunningResDTO> getDemandRunningList(DemandQueryReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);

        Page<DemandRunningBO> demandRunningBOS = feedbackMapper.selectDemandRunningPage(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getType(),reqDTO.getChargeMan());
        Page<DemandRunningResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandRunningBOS)){
            BeanUtils.copyProperties(demandRunningBOS,list);
            demandRunningBOS.stream().forEach(demandRunningBO -> {
                DemandRunningResDTO resDTO = new DemandRunningResDTO();
                BeanUtils.copyProperties(demandRunningBO,resDTO);
                Long planId = feedbackPlanMapper.selectPlanById(demandRunningBO.getId());
                resDTO.setPlanId(planId);

                if (feedbackMapper.selectIsRead(demandRunningBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                    resDTO.setReadStatus(1);
                }else {
                    resDTO.setReadStatus(0);
                }

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

        //重新查询进行中需求集合  用于过滤读取状态  内存分页
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            List<DemandRunningBO> demandRunningBOList = feedbackMapper.selectDemandRunningList(reqDTO.getOrigin(),reqDTO.getPriority(),reqDTO.getType(),reqDTO.getChargeMan());
            List<DemandRunningResDTO> demandRunningResDTOList = new ArrayList<>();
            List<DemandRunningResDTO> filterList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(demandRunningBOList)){
                BeanUtils.copyProperties(demandRunningBOList,demandRunningResDTOList);
                demandRunningBOList.stream().forEach(demandRunningBO -> {
                    DemandRunningResDTO demandRunningResDTO = new DemandRunningResDTO();
                    BeanUtils.copyProperties(demandRunningBO,demandRunningResDTO);
                    Long planId = feedbackPlanMapper.selectPlanById(demandRunningBO.getId());
                    demandRunningResDTO.setPlanId(planId);

                    if (feedbackMapper.selectIsRead(demandRunningBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                        demandRunningResDTO.setReadStatus(1);
                    }else {
                        demandRunningResDTO.setReadStatus(0);
                    }

                    //根据feedback_id查询task  按任务创建时间降序排序
                    List<Long> taskIds = feedbackMapper.selectTasks(demandRunningBO.getId());
                    if (!CollectionUtils.isEmpty(taskIds)){
                        demandRunningResDTO.setTaskNum(taskIds.size());

                        Date beginTime = feedbackPlanTaskMapper.selectBeginTime(taskIds.get(0));
                        if (beginTime != null){
                            Date today = new Date();

                            Long weeks = (today.getTime()-beginTime.getTime())/1000/3600/24/7;
                            if (weeks > 0){
                                demandRunningResDTO.setWorkedWeeks(weeks);
                            }else {
                                demandRunningResDTO.setWorkedWeeks(1L);
                            }


                            //设置任务开始时间
                            demandRunningResDTO.setStartTime(beginTime);
                        }
                        Set<Long> set = new HashSet<>();
                        for (Long taskId : taskIds) {
                            List<Long> persons = feedbackPlanTaskMapper.selectPersonByTaskId(taskId);
                            set.addAll(persons);
                        }
                        //设置开发人数

                        if (!CollectionUtils.isEmpty(set)){
                            demandRunningResDTO.setWorkerNum(set.size());
                        }else {
                            demandRunningResDTO.setWorkerNum(taskIds.size());
                        }
                    }
                    demandRunningResDTOList.add(demandRunningResDTO);
                });

                //开始过滤
                filterList.addAll(demandRunningResDTOList.stream().filter(demandRunningResDTO -> demandRunningResDTO.getReadStatus() == reqDTO.getReadStatus()).collect(Collectors.toList()));
                ArmorPageInfo<DemandRunningResDTO> page = ArmorPageInfo.pageByMemory(filterList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
                int current = page.getCurrent();
                long totalSize = page.getTotalSize();
                List<DemandRunningResDTO> result = page.getList();
                return new ArmorPageInfo<>(current,ZSYConstants.PAGE_SIZE,totalSize,result);
            }
        }
        return new ArmorPageInfo<>(list);
    }

    /**
     * 学管端获取进行中需求
     * @param reqDTO
     * @return
     */
    @Override
    public ArmorPageInfo<DemandRunningResDTO> getDemandRunningListByCoach(DemandQueryReqDTO reqDTO,Integer coachId) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1),ZSYConstants.PAGE_SIZE);
        Page<DemandRunningBO> demandRunningBOS = feedbackMapper.selectDemandRunningPageByCoach(reqDTO);
        Page<DemandRunningResDTO> list = new Page<>();
        if (!CollectionUtils.isEmpty(demandRunningBOS)){
            BeanUtils.copyProperties(demandRunningBOS,list);
            demandRunningBOS.stream().forEach(demandRunningBO -> {
                DemandRunningResDTO resDTO = new DemandRunningResDTO();
                BeanUtils.copyProperties(demandRunningBO,resDTO);
                Long planId = feedbackPlanMapper.selectPlanById(demandRunningBO.getId());
                resDTO.setPlanId(planId);

                if(feedbackMapper.selectIsReadByCoach(demandRunningBO.getId(),coachId)!= null){
                    resDTO.setReadStatus(1);
                }else {
                    resDTO.setReadStatus(0);
                }

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

        //重新查询进行中需求集合  用于过滤读取状态  内存分页
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            List<DemandRunningBO> demandRunningBOList = feedbackMapper.selectDemandRunningListByCoach(reqDTO);
            List<DemandRunningResDTO> demandRunningResDTOList = new ArrayList<>();
            List<DemandRunningResDTO> filterList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(demandRunningBOList)){
                BeanUtils.copyProperties(demandRunningBOList,demandRunningResDTOList);
                demandRunningBOList.stream().forEach(demandRunningBO -> {
                    DemandRunningResDTO resDTO = new DemandRunningResDTO();
                    BeanUtils.copyProperties(demandRunningBO,resDTO);
                    Long planId = feedbackPlanMapper.selectPlanById(demandRunningBO.getId());
                    resDTO.setPlanId(planId);

                    if(feedbackMapper.selectIsReadByCoach(demandRunningBO.getId(),coachId)!= null){
                        resDTO.setReadStatus(1);
                    }else {
                        resDTO.setReadStatus(0);
                    }

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
                    demandRunningResDTOList.add(resDTO);
                });

                //开始过滤
                filterList.addAll(demandRunningResDTOList.stream().filter(demandRunningResDTO -> demandRunningResDTO.getReadStatus()==reqDTO.getReadStatus()).collect(Collectors.toList()));
                ArmorPageInfo<DemandRunningResDTO> page = ArmorPageInfo.pageByMemory(filterList, Optional.ofNullable(reqDTO.getPageNum()).orElse(1));
                int current = page.getCurrent();
                long totalSize = page.getTotalSize();
                List<DemandRunningResDTO> result = page.getList();
                return new ArmorPageInfo<>(current,ZSYConstants.PAGE_SIZE,totalSize,result);
            }
        }
        return new ArmorPageInfo<>(list);
    }

    @Override
    public DemandDetailResDTO getDemandDetail(DemandDetailQueryReqDTO reqDTO) {
        //先根据coachId和需求id判断当前需求是否已读

        if (feedbackMapper.selectIsRead(Long.valueOf(reqDTO.getDemandId()),ZSYTokenRequestContext.get().getUserId()) == null){
            String readName = feedbackMapper.selectUserNameByUserId(ZSYTokenRequestContext.get().getUserId());
            feedbackMapper.insertFeedbackRead(snowFlakeIDHelper.nextId(),Long.valueOf(reqDTO.getDemandId())
                    ,ZSYTokenRequestContext.get().getUserId(),readName,new Date());
        }
        //查询需求详情基本信息
        DemandDetailBO demandDetailBO = feedbackMapper.selectDemandDetail(Long.valueOf(reqDTO.getDemandId()),reqDTO.getStatus());
        DemandDetailResDTO dto = new DemandDetailResDTO();
        if (demandDetailBO != null){
            BeanUtils.copyProperties(demandDetailBO,dto);

            List<String> urlList = feedbackMapper.selectUrlsById(Long.valueOf(reqDTO.getDemandId()));
            if (!CollectionUtils.isEmpty(urlList)){
                dto.setUrlList(urlList);
            }

            //查询点赞人
            List<String> likesPeople = feedbackMapper.selectLikesPeople(Long.valueOf(reqDTO.getDemandId()));
            dto.setLikesPeople(likesPeople);

            //查询已读人
            List<String> readPeople = feedbackMapper.selectReadPeople(Long.valueOf(reqDTO.getDemandId()));
            if (!CollectionUtils.isEmpty(readPeople)){
                dto.setReadNum(readPeople.size());
            }else {
                dto.setReadNum(0);
            }
            dto.setReadPeople(readPeople);
        }

        return dto;
    }


    /**
     * 获取需求详情
     * @return
     */
    @Override
    public DemandDetailResDTO getDemandDetailByCoach(DemandDetailQueryReqDTO reqDTO) {
        //先根据coachId和需求id判断当前需求是否已读
        if (feedbackMapper.selectIsReadByCoach(Long.valueOf(reqDTO.getDemandId()),reqDTO.getCoachId()) == null){
            feedbackMapper.insertFeedbackReadByCoach(snowFlakeIDHelper.nextId(),Long.valueOf(reqDTO.getDemandId())
                                                    ,reqDTO.getCoachId(),reqDTO.getReadPeople(),new Date());
        }
        //查询需求详情基本信息
        DemandDetailBO demandDetailBO = feedbackMapper.selectDemandDetail(Long.valueOf(reqDTO.getDemandId()),reqDTO.getStatus());
        DemandDetailResDTO dto = new DemandDetailResDTO();
        if (demandDetailBO != null){
            BeanUtils.copyProperties(demandDetailBO,dto);

            List<String> urlList = feedbackMapper.selectUrlsById(Long.valueOf(reqDTO.getDemandId()));
            if (!CollectionUtils.isEmpty(urlList)){
                dto.setUrlList(urlList);
            }

            //查询点赞人
            List<String> likesPeople = feedbackMapper.selectLikesPeople(Long.valueOf(reqDTO.getDemandId()));
            dto.setLikesPeople(likesPeople);

            //查询已读人
            List<String> readPeople = feedbackMapper.selectReadPeople(Long.valueOf(reqDTO.getDemandId()));
            if (!CollectionUtils.isEmpty(readPeople)){
                dto.setReadNum(readPeople.size());
            }else {
                dto.setReadNum(0);
            }
            dto.setReadPeople(readPeople);
        }

        return dto;
    }

    /**
     * 获取需求回复详情
     * @param id
     * @return
     */
    @Override
    public List<DemandReplyResDTO> getDemandReply(Long id) {
        List<DemandReplyBO> replyBOS = feedbackMapper.selectDemandReply(id);
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
    @Transactional
    public void like(Long id) {
        //新增feedback_likes
        String likePeople = feedbackMapper.selectUserNameByUserId(ZSYTokenRequestContext.get().getUserId());
        feedbackMapper.insertDemandLikes(id,ZSYTokenRequestContext.get().getUserId(),likePeople,snowFlakeIDHelper.nextId(),new Date());

        //feedback中likesNum +1
        Integer likesNum = feedbackMapper.selectLikesNumById(id);
        likesNum += 1;
        if (feedbackMapper.updateLikesNum(id,likesNum) == 0){
            throw new ZSYServiceException("点赞失败");
        }
    }

    @Override
    public void dislike(Long id) {
        //删除feedback_likes
        feedbackMapper.deleteDemandLikes(id,ZSYTokenRequestContext.get().getUserId());
        Integer likesNum = feedbackMapper.selectLikesNumById(id);
        likesNum -= 1;
        if (feedbackMapper.updateLikesNum(id,likesNum) == 0){
            throw new ZSYServiceException("取消点赞失败");
        }
    }

    /**
     * 回复需求
     * @param reqDTO
     */
    @Override
    public void reply(DemandReplyReqDTO reqDTO) {
        String replyPeople = feedbackMapper.selectUserNameByUserId(ZSYTokenRequestContext.get().getUserId());
        if (feedbackMapper.insertReply(Long.valueOf(reqDTO.getDemandId()),ZSYTokenRequestContext.get().getUserId(),reqDTO.getContent()
                ,snowFlakeIDHelper.nextId(),new Date(),replyPeople) == 0){
            throw new ZSYServiceException("回复失败");
        };
    }

    /**
     * 采纳需求,进入排队中
     * @param id
     */
    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public void addDemand(DemandReqDTO reqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }

        Demand demand = new Demand();
        BeanUtils.copyProperties(reqDTO,demand);

        //新增需求时,不设置项目projectId,再计划中加入项目
        demand.setId(snowFlakeIDHelper.nextId());
        demand.setStatus(ZSYFeedbackStatus.NEW.getValue());
        demand.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        demand.setCreateTime(new Date());
        demand.setUpdateTime(new Date());
        demand.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
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
    public void readDemand(Long id) {
        //添加feedback_read
        String readPeople = feedbackMapper.selectUserNameByUserId(ZSYTokenRequestContext.get().getUserId());
        if (feedbackMapper.insertFeedbackRead(snowFlakeIDHelper.nextId(),id,ZSYTokenRequestContext.get().getUserId(),readPeople,new Date()) == 0){
            throw new ZSYServiceException("需求读取失败");
        }
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
    @Transactional
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
    public DemandIsOperateResDTO isRead(Long id) {
        DemandIsOperateResDTO demandIsReadResDTO = new DemandIsOperateResDTO();
        if (feedbackMapper.selectIsRead(id,ZSYTokenRequestContext.get().getUserId()) != null){
            demandIsReadResDTO.setCount(1);
        }else {
            demandIsReadResDTO.setCount(0);
        }
        return demandIsReadResDTO;
    }

    /**
     * 查看需求是否已点赞
     * @param id
     * @return
     */
    @Override
    public DemandIsOperateResDTO isLike(Long id) {
        Integer count = feedbackMapper.selectIsLike(id,ZSYTokenRequestContext.get().getUserId());
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
    public List<String> getUrl(Long id) {
        List<String> urls = feedbackMapper.selectUrlsById(id);
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
     * @param coachId
     * @return
     */
    @Override
    public DemandIsOperateResDTO isReadByCoach(Long id, Integer coachId) {
        DemandIsOperateResDTO demandIsReadResDTO = new DemandIsOperateResDTO();

        if (feedbackMapper.selectIsReadByCoach(id,coachId) != null){
            demandIsReadResDTO.setCount(1);
        }else {
            demandIsReadResDTO.setCount(0);
        }
        return demandIsReadResDTO;
    }

    @Override
    @Transactional
    public void readDemandByCoach(Long id,DemandReadReqDTO reqDTO) {
        //添加feedback_read
        if (feedbackMapper.insertFeedbackReadByCoach(snowFlakeIDHelper.nextId(),id,reqDTO.getCoachId(),reqDTO.getReadPeople(),new Date())==0){
            throw new ZSYServiceException("读取需求失败");
        }
    }

    @Override
    public DemandIsOperateResDTO isLikeByCoach(Long id, Integer coachId) {
        Integer count = feedbackMapper.selectIsLikeByCoach(id,coachId);
        DemandIsOperateResDTO demandIsReadResDTO = new DemandIsOperateResDTO();
        demandIsReadResDTO.setCount(count);
        return demandIsReadResDTO;
    }

    @Override
    @Transactional
    public void likeDemandByCoach(Long id, DemandLikeReqDTO reqDTO) {
        //新增feedback_likes
        feedbackMapper.insertDemandLikesByCoach(id, reqDTO.getCoachId(),reqDTO.getLikePeople(),snowFlakeIDHelper.nextId(),new Date());

        //feedback中likesNum +1
        Integer likesNum = feedbackMapper.selectLikesNumById(id);
        likesNum += 1;
        if (feedbackMapper.updateLikesNum(id,likesNum) == 0){
            throw new ZSYServiceException("点赞失败");
        }
    }

    @Override
    @Transactional
    public void dislikeDemandByCoach(Long id, Integer coachId) {
        //删除feedback_likes数据
        if (feedbackMapper.deleteDemandLikesByCoach(id,coachId) != 0){
            //feedback中likesNum - 1
            Integer likesNum = feedbackMapper.selectLikesNumById(id);
            likesNum -= 1;
            if (feedbackMapper.updateLikesNum(id,likesNum) == 0){
                throw new ZSYServiceException("取消点赞失败");
            }
        }
    }

    @Override
    public void replyDemandByCoach(DemandReplyReqDTO reqDTO, Integer coachId) {
        if (feedbackMapper.insertReplyByCoach(Long.valueOf(reqDTO.getDemandId()),coachId,reqDTO.getContent()
                ,snowFlakeIDHelper.nextId(),new Date(),reqDTO.getReplyPeople()) == 0){
            throw new ZSYServiceException("回复失败");
        }
    }

    @Override
    @Transactional
    public void addDemandByCoach(DemandReqDTO reqDTO, Integer coachId) {
        Demand demand = new Demand();
        BeanUtils.copyProperties(reqDTO,demand);

        //新增需求时,不设置项目ID,再计划中加入项目
        demand.setId(snowFlakeIDHelper.nextId());
        demand.setFeedbackTime(new Date());
        demand.setStatus(ZSYFeedbackStatus.NEW.getValue());
        demand.setCoachId(coachId);
        demand.setFeedbackTime(new Date());
        demand.setCreateTime(new Date());
        demand.setUpdateTime(new Date());
        demand.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        if (feedbackMapper.insertDemandByCoach(demand) == 0){
            throw new ZSYServiceException("提需求失败");
        }

        if (!CollectionUtils.isEmpty(reqDTO.getUrlList())){
            List<DemandAccessory> list = new ArrayList<>();
            reqDTO.getUrlList()
                    .stream().filter(url -> !Strings.isNullOrEmpty(url))
                    .forEach(url -> {
                        DemandAccessory demandAccessory = new DemandAccessory();
                        demandAccessory.setId(snowFlakeIDHelper.nextId());
                        demandAccessory.setDemandId(demand.getId());
                        demandAccessory.setUrl(url);
                        demandAccessory.setCreateTime(new Date());
                        list.add(demandAccessory);
                    });
            if (!list.isEmpty()){
                feedbackMapper.insertFeedbackAccessory(list);
            }
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
                if (feedbackPlanBO.getPlanTask() != null){
                    BeanUtils.copyProperties(feedbackPlanBO.getPlanTask(), feedbackTaskDetailResDTO);
                    feedbackTaskDetailResDTO.setTaskName(feedbackPlanBO.getPlanTask().getName());
                    feedbackTaskDetailResDTO.setProjectId(feedbackPlanBO.getId());//将已存在的任务ID存储为项目ID
                    User user = userMapper.selectById(feedbackPlanBO.getPlanTask().getCreateBy());//在上一条查询中未查出
                    feedbackTaskDetailResDTO.setUserName(user.getName());
                    feedbackTaskDetailResDTO.setTaskType(ZSYTaskType.PUBLIC_TASK.getValue());
                    taskDetailResDTOS.add(feedbackTaskDetailResDTO);
                }
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

    /**
     * 新需求导出Excel
     * @param reqDTO
     * @return
     */
    @Override
    public String newDemandExcel(DemandQueryReqDTO reqDTO) {
        List<DemandBO> demandBOList = feedbackMapper.selectDemandListByReqDTO(reqDTO);
        List<DemandResDTO> demandResDTOList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(demandBOList)){
            BeanUtils.copyProperties(demandBOList,demandResDTOList);
            demandBOList.stream().forEach(demandBO -> {
                DemandResDTO demandResDTO = new DemandResDTO();
                BeanUtils.copyProperties(demandBO,demandResDTO);
                //设置当前登录人对需求是否已读
                if (feedbackMapper.selectIsRead(demandBO.getId(),ZSYTokenRequestContext.get().getUserId()) != null){
                    demandResDTO.setReadStatus(1);
                }else {
                    demandResDTO.setReadStatus(0);
                }
                demandResDTOList.add(demandResDTO);
            });
        }

        //过滤是否已读
        List<DemandResDTO> filterList = Lists.newArrayList();
        if (reqDTO.getReadStatus() != null && reqDTO.getReadStatus() != -1){
            filterList.addAll(demandResDTOList.stream().filter(demandResDTO -> demandResDTO.getReadStatus() == reqDTO.getReadStatus()).collect(Collectors.toList()));
            return getDemandNewExcel(filterList);
        }
        return getDemandNewExcel(demandResDTOList);

    }

    /**
     * 更新老数据的来源
     * @author sch
     */
    @Override
    @Transactional
    public void updateSource() {
        List<Demand> demandList = feedbackMapper.selectSourceIsNull();
        List<Demand> newList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(demandList)){
            demandList.stream().forEach(demand -> {
                if (demand.getCoachId() != null && demand.getCoachId() > 0){
                    demand.setSource(4);
                }else {
                    demand.setSource(0);
                }
                newList.add(demand);
            });
        }
        if (!CollectionUtils.isEmpty(newList)){
            if (feedbackMapper.updateSourceBatch(newList) == 0){
                throw new ZSYServiceException("批量更新需求来源失败");
            }
        }
    }

    /**
     * 更新老数据的负责人
     * @author sch
     */
    @Override
    @Transactional
    public void updateChargeMan() {
        User user = userMapper.selectByName("颜林艳");
        if (user != null){
            List<Demand> demandList = feedbackMapper.selectChargeManIsNull();
            List<Demand> newList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(demandList)){
                demandList.stream().forEach(demand -> {
                    demand.setChargeMan(user.getId());
                    newList.add(demand);
                });
            }
            if (!CollectionUtils.isEmpty(newList)){
                if (feedbackMapper.updateChargeManBatch(newList) == 0){
                    throw new ZSYServiceException("批量更新需求负责人失败");
                }
            }
        }else {
            throw new ZSYServiceException("当前用户不存在,请检查");
        }


    }

    //导出新需求Excel
    private String getDemandNewExcel(List<DemandResDTO> demandResDTOList) {
        //设置表头
        List<String> headers = new ArrayList<>();
        headers.add("需求标题");
        headers.add("来源");
        headers.add("类型");
        headers.add("优先级");
        headers.add("提出人");
        headers.add("提出日期");
        headers.add("期待上线日期");
        headers.add("点赞数");

        //设置文件名
        String fileName = "待处理需求" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             HSSFWorkbook workbook = new HSSFWorkbook()){
            //创建sheet
            HSSFSheet sheet = workbook.createSheet("新需求");
            //设置列宽
            sheet.setDefaultColumnWidth(25);
            //创建行
            HSSFRow row = sheet.createRow(0);
            //创建样式
            HSSFCellStyle style = workbook.createCellStyle();
            //设置样式
            style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
            style.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            //创建字体
            HSSFFont font = workbook.createFont();
            //设置字体
            font.setFontHeightInPoints((short) 15);
            font.setBold(true);
            font.setFontName("微软雅黑");
            style.setFont(font);
            HSSFCell cell = null;
            //创建标题
            for (int i = 0; i < headers.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(style);
            }
            int num = 0;
            //创建内容
            for (int i = 0; i < demandResDTOList.size(); i++) {
                row = sheet.createRow(num + 1);
                row.setRowStyle(style);
                row.createCell(0).setCellValue(demandResDTOList.get(i).getTitle());
                //设置来源(是否来自学管端)
                if (demandResDTOList.get(i).getCoachId() != null){
                    if (demandResDTOList.get(i).getCoachId() == 0){
                        row.createCell(1).setCellValue("其他");
                    }else {
                        row.createCell(1).setCellValue("学管端");
                    }
                }else {
                    row.createCell(1).setCellValue("其他");
                }
                //设置类型
                if (demandResDTOList.get(i).getType() != null){
                    if (demandResDTOList.get(i).getType() == 0){
                        row.createCell(2).setCellValue("个人建议");
                    }else if (demandResDTOList.get(i).getType() == 1){
                        row.createCell(2).setCellValue("市场反馈");
                    }else {
                        row.createCell(2).setCellValue("公司建议");
                    }
                }else {
                    row.createCell(2).setCellValue("无");
                }
                //设置优先级
                if (demandResDTOList.get(i).getPriority() != null){
                    if (demandResDTOList.get(i).getPriority() == 0){
                        row.createCell(3).setCellValue("普通");
                    }else if (demandResDTOList.get(i).getPriority() == 1){
                        row.createCell(3).setCellValue("紧急");
                    }else {
                        row.createCell(3).setCellValue("非常紧急");
                    }
                }else {
                    row.createCell(3).setCellValue("无");
                }
                row.createCell(4).setCellValue(demandResDTOList.get(i).getOrigin());
                row.createCell(5).setCellValue(DateHelper.dateFormatter(demandResDTOList.get(i).getFeedbackTime(),"yyyy-MM-dd"));
                if (demandResDTOList.get(i).getReleaseTime() != null){
                    row.createCell(6).setCellValue(DateHelper.dateFormatter(demandResDTOList.get(i).getReleaseTime(),"yyyy-MM-dd"));
                }else {
                    row.createCell(6).setCellValue("无");
                }
                row.createCell(7).setCellValue(demandResDTOList.get(i).getLikesNum());
                num++;
            }
            workbook.write(os);
            return ZSYQinuHelper.upload(os.toByteArray(), fileName, "application/vnd.ms-excel", qinuOssProperty);
        }catch (Exception e){
            e.printStackTrace();
            throw new ZSYServiceException("导出表失败");
        }
    }


}
