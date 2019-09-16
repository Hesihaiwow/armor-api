package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.dao.IZSYWeekPublishPlanMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.WeekPublishTaskBO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishEditReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.response.PlatformResDTO;
import com.zhixinhuixue.armor.model.dto.response.WeekPublishTaskResDTO;
import com.zhixinhuixue.armor.model.pojo.Platform;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.WeekPublishPlan;
import com.zhixinhuixue.armor.model.pojo.WeekPublishPlanPlatform;
import com.zhixinhuixue.armor.service.IZSYWeekPublishService;
import com.zhixinhuixue.armor.source.enums.ZSYJobRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
        List<WeekPublishTaskResDTO> filterList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(weekPublishTaskBOS)){
            weekPublishTaskBOS.stream().forEach(weekPublishTaskBO -> {
                WeekPublishTaskResDTO resDTO = new WeekPublishTaskResDTO();
                BeanUtils.copyProperties(weekPublishTaskBO,resDTO);
                List<PlatformResDTO> platformResDTOS = new ArrayList<>();
                List<Platform> platformList = weekPublishTaskBO.getPlatforms();
                String platforms = "";
                if (!CollectionUtils.isEmpty(platformList)){
                    for (Platform platform : platformList) {
                        PlatformResDTO platformResDTO = new PlatformResDTO();
                        platformResDTO.setId(platform.getId());
                        platformResDTO.setName(platform.getName());
                        platformResDTOS.add(platformResDTO);

                        platforms = platforms + platform.getName()+ " ";
                    }
                }
                resDTO.setPlatformResDTOS(platformResDTOS);
                resDTO.setPlatforms(platforms);

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
                weekPublishTaskResDTOList.add(resDTO);
            });
            filterList = weekPublishTaskResDTOList.stream().distinct().collect(Collectors.toList());
        }
        //插入到周发版计划表
        if (!CollectionUtils.isEmpty(weekPublishTaskResDTOList)){
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

        return weekPublishTaskResDTOList;
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
            //删除原来的task-platform
            weekPublishPlanMapper.deletePublishPlatformByTask(reqDTO.getTaskId());
            List<String> platforms = reqDTO.getPlatforms();
            List<WeekPublishPlanPlatform> list = new ArrayList<>();
            if (!CollectionUtils.isEmpty(platforms)){
                platforms.stream().forEach(platformId->{
                    WeekPublishPlanPlatform weekPublishPlanPlatform = new WeekPublishPlanPlatform();
                    weekPublishPlanPlatform.setId(snowFlakeIDHelper.nextId());
                    weekPublishPlanPlatform.setTaskId(reqDTO.getTaskId());
                    weekPublishPlanPlatform.setPlatformId(Long.valueOf(platformId));
                    list.add(weekPublishPlanPlatform);
                });

                if (weekPublishPlanMapper.insertPublishPlatformBatch(list) == 0){
                    throw new ZSYServiceException("批量插入任务需要发布的平台失败");
                }
            }
        }
        //更新
        if (weekPublishPlanMapper.updateById(exist) == 0){
            throw new ZSYServiceException("更新周发版计划任务失败,请检查");
        }

    }
}
