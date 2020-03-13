package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.dao.IZSYWeekPublishPlanMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.WeekPublishTaskBO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishEditReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.response.PlatformResDTO;
import com.zhixinhuixue.armor.model.dto.response.WeekPublishResDTO;
import com.zhixinhuixue.armor.model.dto.response.WeekPublishTaskResDTO;
import com.zhixinhuixue.armor.model.pojo.Platform;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.WeekPublishPlan;
import com.zhixinhuixue.armor.model.pojo.WeekPublishPlanPlatform;
import com.zhixinhuixue.armor.service.IZSYWeekPublishService;
import com.zhixinhuixue.armor.source.enums.ZSYJobRole;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sch
 * @DATE 2019/6/11 9:57
 */
@Service
public class ZSYWeekPublishService implements IZSYWeekPublishService {
    @Autowired
    private IZSYWeekPublishPlanMapper weekPublishPlanMapper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 周发版计划列表
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public List<WeekPublishTaskResDTO> list(WeekPublishQueryReqDTO reqDTO) {
        List<WeekPublishTaskBO> weekPublishTaskBOS = taskMapper.selectWeekPublishTask(reqDTO);
        List<WeekPublishTaskResDTO> weekPublishTaskResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(weekPublishTaskBOS)){
            weekPublishTaskBOS.stream().forEach(weekPublishTaskBO -> {
                WeekPublishTaskResDTO resDTO = new WeekPublishTaskResDTO();
                BeanUtils.copyProperties(weekPublishTaskBO,resDTO);

//                List<PlatformResDTO> platformResDTOS = new ArrayList<>();
//                List<Platform> platformList = weekPublishTaskBO.getPlatforms();
//                String platforms = "";
//                if (!CollectionUtils.isEmpty(platformList)){
//                    for (Platform platform : platformList) {
//                        PlatformResDTO platformResDTO = new PlatformResDTO();
//                        platformResDTO.setId(platform.getId());
//                        platformResDTO.setName(platform.getName());
//                        platformResDTOS.add(platformResDTO);
//
//                        platforms = platforms + platform.getName()+ " ";
//                    }
//                }
//                resDTO.setPlatformResDTOS(platformResDTOS);
//                resDTO.setPlatforms(platforms);

                String developers = "";
                String testers = "";
                if (!CollectionUtils.isEmpty(weekPublishTaskBO.getUserIds())){
                    for (Long userId : weekPublishTaskBO.getUserIds()) {
                        User user = userMapper.selectById(userId);
                        if (user.getJobRole() == ZSYJobRole.PROGRAMER.getValue()){
                            developers = developers  + user.getName()+ " ";
                        }
                        if (user.getJobRole() == ZSYJobRole.TEST.getValue()){
                            testers = testers  + user.getName()+ " ";
                        }
                    }
                }
                resDTO.setDevelopers(developers);
                resDTO.setTesters(testers);
//                resDTO.setTestTimeColor(0);
//                resDTO.setDesignDays(0);
//                resDTO.setDevelopDays(0);
//                resDTO.setTestDays(0);
//                Date createTime = weekPublishTaskBO.getCreateTime();
//                Date beginTime = weekPublishTaskBO.getBeginTime();
//                Date testTime = weekPublishTaskBO.getTestTime();
                Date endTime = weekPublishTaskBO.getEndTime();
                //如果周发版计划没有预计上线时间,则显示任务截止时间
                if (weekPublishTaskBO.getOnlineTime() == null && endTime != null){
                    resDTO.setOnlineTime(new SimpleDateFormat(DateHelper.DATE_FORMAT).format(endTime));
                }

//                //创建---设计结束  共用天数
//                if (createTime != null && beginTime != null){
//                    Integer workDays = getWorkDays(createTime, beginTime);
//                    resDTO.setDesignDays(workDays);
//                }
//
//                //开发开始---开发结束  共用天数
//                if (beginTime != null && testTime != null){
//                    Integer workDays = getWorkDays(beginTime, testTime);
//                    resDTO.setDevelopDays(workDays);
//                }
//
//                //测试开始---测试结束
//                if (testTime != null && endTime != null){
//                    Integer workDays = getWorkDays(testTime, endTime);
//                    resDTO.setTestDays(workDays);
//                }
//
//                if (testTime != null){
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(testTime);
//                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                    if (dayOfWeek == 2 || dayOfWeek == 3 || dayOfWeek == 4){
//                        resDTO.setTestTimeColor(1);
//                    }else if (dayOfWeek == 5){
//                        resDTO.setTestTimeColor(2);
//                    }
//                }

                weekPublishTaskResDTOList.add(resDTO);
            });
        }
        //插入到周发版计划表
        List<WeekPublishTaskResDTO> collect = new ArrayList<>();
        if (!CollectionUtils.isEmpty(weekPublishTaskResDTOList)){
            collect = weekPublishTaskResDTOList.stream()
                    .sorted(Comparator.comparing(WeekPublishTaskResDTO::getCreateBy))
                    .collect(Collectors.toList());
            for (WeekPublishTaskResDTO task : weekPublishTaskResDTOList) {
                //校验是否存在
                WeekPublishPlan exist = weekPublishPlanMapper.selectById(task.getWppId());
                if (exist == null){
                    WeekPublishPlan weekPublishPlan = new WeekPublishPlan();
                    weekPublishPlan.setId(snowFlakeIDHelper.nextId());
                    weekPublishPlan.setTaskId(task.getTaskId());
                    weekPublishPlan.setCanOnline(0);
                    weekPublishPlan.setCondition("");
                    weekPublishPlanMapper.insert(weekPublishPlan);
                }
            }
        }

        return collect;
    }

    private Integer getWorkDays(Date beginTime, Date endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginTime);
        int beginTimeYear = calendar.get(Calendar.YEAR);
        int beginTimeMonth = calendar.get(Calendar.MONTH) + 1;
        int beginTimeDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(endTime);
        int endTimeYear = calendar.get(Calendar.YEAR);
        int endTimeMonth = calendar.get(Calendar.MONTH) + 1;
        int endTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate start = LocalDate.of(beginTimeYear, beginTimeMonth, beginTimeDay);
        LocalDate end = LocalDate.of(endTimeYear, endTimeMonth, endTimeDay);
        Integer workDays = (int)(end.toEpochDay() - start.toEpochDay() + 1);
        return workDays;
    }

    /**
     * 编辑
     * @author sch
     * @param reqDTO
     */
    @Override
    @Transactional
    public void edit(WeekPublishEditReqDTO reqDTO) {
        WeekPublishPlan exist = weekPublishPlanMapper.selectById(reqDTO.getId());
        if (exist == null){
            throw new ZSYServiceException("当前周发版计划任务不存在,请检查");
        }

        //当前情况只是修改是否能发布上线
        if (reqDTO.getCanOnline() != null){
            exist.setCanOnline(reqDTO.getCanOnline());
        }else {
            exist.setCondition(reqDTO.getCondition());
            exist.setSpecialTestTime(reqDTO.getSpecialTestTime());
            exist.setZujuan(reqDTO.getZujuan());
            exist.setYuejuan(reqDTO.getYuejuan());
            exist.setSaomiao(reqDTO.getSaomiao());
            exist.setXueyebaogao(reqDTO.getXueyebaogao());
//            exist.setChanpin(reqDTO.getChanpin());
            exist.setRealTestTime(reqDTO.getRealTestTime());
            exist.setOnlineTime(reqDTO.getOnlineTime());
            exist.setRealOnlineTime(reqDTO.getRealOnlineTime());
            //删除原来的task-platform
//            weekPublishPlanMapper.deletePublishPlatformByTask(reqDTO.getTaskId());
//            List<String> platforms = reqDTO.getPlatforms();
//            List<WeekPublishPlanPlatform> list = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(platforms)){
//                platforms.stream().forEach(platformId->{
//                    WeekPublishPlanPlatform weekPublishPlanPlatform = new WeekPublishPlanPlatform();
//                    weekPublishPlanPlatform.setId(snowFlakeIDHelper.nextId());
//                    weekPublishPlanPlatform.setTaskId(reqDTO.getTaskId());
//                    weekPublishPlanPlatform.setPlatformId(Long.valueOf(platformId));
//                    list.add(weekPublishPlanPlatform);
//                });
//
//                if (weekPublishPlanMapper.insertPublishPlatformBatch(list) == 0){
//                    throw new ZSYServiceException("批量插入任务需要发布的平台失败");
//                }
//            }
        }
        //更新
        if (weekPublishPlanMapper.updateById(exist) == 0){
            throw new ZSYServiceException("更新周发版计划任务失败,请检查");
        }

    }

    /**
     * 按任务负责人分组查看
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    public List<WeekPublishResDTO> getListByChargeMan(WeekPublishQueryReqDTO reqDTO) {
        List<WeekPublishTaskBO> weekPublishTaskBOS = taskMapper.selectWeekPublishTaskList(reqDTO);
        List<WeekPublishResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(weekPublishTaskBOS)){
            Map<Long, List<WeekPublishTaskBO>> userTaskMap = weekPublishTaskBOS.stream()
                    .collect(Collectors.groupingBy(WeekPublishTaskBO::getCreateBy));
            for (Long userId : userTaskMap.keySet()) {
                List<WeekPublishTaskBO> publishTaskBOS = userTaskMap.get(userId);
                WeekPublishResDTO resDTO = new WeekPublishResDTO();
                resDTO.setUserId(userId);
                resDTO.setUserName(publishTaskBOS.get(0).getCreateByName());
                List<WeekPublishResDTO.PublishResDTO> publishResDTOS = new ArrayList<>();
                publishTaskBOS.forEach(weekPublishTaskBO -> {
                    WeekPublishResDTO.PublishResDTO publishResDTO = new WeekPublishResDTO.PublishResDTO();
                    BeanUtils.copyProperties(weekPublishTaskBO,publishResDTO);
                    Date endTime = weekPublishTaskBO.getEndTime();
                    //如果周发版计划没有预计上线时间,则显示任务截止时间
                    if (weekPublishTaskBO.getOnlineTime() == null && endTime != null){
                        publishResDTO.setOnlineTime(new SimpleDateFormat(DateHelper.DATE_FORMAT).format(endTime));
                    }
                    List<User> userList = weekPublishTaskBO.getUserList();
                    if (!CollectionUtils.isEmpty(userList)){
                        String developers = "";
                        String testers = "";
                        for (User user : userList) {
                            if (user.getJobRole() == ZSYJobRole.PROGRAMER.getValue()){
                                developers = developers  + user.getName()+ " ";
                            }
                            if (user.getJobRole() == ZSYJobRole.TEST.getValue()){
                                testers = testers  + user.getName()+ " ";
                            }
                        }
                        publishResDTO.setDevelopers(developers);
                        publishResDTO.setTesters(testers);
                    }
                    publishResDTOS.add(publishResDTO);
                });

                resDTO.setPublishResDTOList(publishResDTOS);
                list.add(resDTO);
            }
        }
        return list;
    }
}
