package com.zhixinhuixue.armor.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.dao.IZSYFeedbackMapper;
import com.zhixinhuixue.armor.dao.IZSYFeedbackPlanMapper;
import com.zhixinhuixue.armor.dao.IZSYFeedbackPlanTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.FeedbackPlanTaskListBO;
import com.zhixinhuixue.armor.model.bo.TaskDetailBO;
import com.zhixinhuixue.armor.model.dto.request.DemandPlanQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanListReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DemandPlanListResDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackPlanListResDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackTaskDetailResDTO;
import com.zhixinhuixue.armor.model.pojo.BugUser;
import com.zhixinhuixue.armor.service.IZSYFeedbackPlanService;
import com.zhixinhuixue.armor.source.enums.ZSYPlanSort;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Lang on 2018/3/8 0008.
 */
@Service
public class ZSYFeedbackPlanService implements IZSYFeedbackPlanService {

    @Autowired
    private IZSYFeedbackPlanMapper izsyFeedbackPlanMapper;

    @Autowired
    private IZSYFeedbackPlanTaskMapper izsyFeedbackPlanTaskMapper;

    @Autowired
    private IZSYTaskMapper izsyTaskMapper;

    @Autowired
    private IZSYFeedbackMapper feedbackMapper;

    @Override
    public List<FeedbackPlanListResDTO> getFeedbackPlanList(FeedbackPlanListReqDTO feedbackPlanListReqDTO) {
        List<FeedbackPlanTaskListBO> feedbackPlanBOS = izsyFeedbackPlanMapper.getFeedbackPlanBySort(feedbackPlanListReqDTO);
        List<FeedbackPlanListResDTO> feedbackPlanListResDTOS = Lists.newArrayList();

        feedbackPlanBOS.stream().forEach(planTask -> {
            if(planTask==null){
                System.out.println(1);
            }
            FeedbackPlanListResDTO feedbackPlanListResDTO = new FeedbackPlanListResDTO();
            BeanUtils.copyProperties(planTask, feedbackPlanListResDTO);

            List<FeedbackTaskDetailResDTO> task = Lists.newArrayList();
            planTask.getPlanTask().stream().forEach(planTaskBO -> {
                FeedbackTaskDetailResDTO taskDetailResDTO = new FeedbackTaskDetailResDTO();
                BeanUtils.copyProperties(planTaskBO.getPlanTask(),taskDetailResDTO);
                taskDetailResDTO.setTaskName(planTaskBO.getPlanTask().getName());
                task.add(taskDetailResDTO);

                if(planTaskBO.getPlanTask().getStageName().contains("设计")){
                    taskDetailResDTO.setPercent(0);
                }else if(planTaskBO.getPlanTask().getStageName().contains("开发")){
                    taskDetailResDTO.setPercent(25);
                }else if(planTaskBO.getPlanTask().getStageName().contains("测试")){
                    taskDetailResDTO.setPercent(70);
                }else if(planTaskBO.getPlanTask().getStageName().contains("待发布")){
                    taskDetailResDTO.setPercent(95);
                }else if(planTaskBO.getPlanTask().getStageName().contains("已发布")){
                    taskDetailResDTO.setPercent(100);
                }
            });

            feedbackPlanListResDTO.setChilds(task);
            feedbackPlanListResDTO.setEndTime((DateHelper.dateFormatter(planTask.getExpectOfficialTime(),DateHelper.DATE_FORMAT)));

            double x =(float) 1/planTask.getPlanTask().size();//系数
            double percent = 0.00;
            //计算进度
            for(int i = 0;i<feedbackPlanListResDTO.getChilds().size();i++){
                percent += feedbackPlanListResDTO.getChilds().get(i).getPercent()*x;
            }

            feedbackPlanListResDTO.setPercent(Double.valueOf(new DecimalFormat(".00").format(percent)).intValue());

            feedbackPlanListResDTOS.add(feedbackPlanListResDTO);
        });

        if(feedbackPlanListReqDTO.getSort()==null || feedbackPlanListReqDTO.getSort().equals(ZSYPlanSort.PERCENTTP.getValue())){
            return feedbackPlanListResDTOS.stream().sorted(Comparator.comparing(FeedbackPlanListResDTO::getPercent).reversed()).collect(Collectors.toList());
        }else if( feedbackPlanListReqDTO.getSort().equals(ZSYPlanSort.PERCENTDOWN.getValue())){
            return  feedbackPlanListResDTOS.stream().sorted(Comparator.comparing(FeedbackPlanListResDTO::getPercent)).collect(Collectors.toList());
        }

        return feedbackPlanListResDTOS;

    }

    @Override
    public List<FeedbackPlanListResDTO> getDemandPlanList(DemandPlanQueryReqDTO reqDTO) {

        List<FeedbackPlanTaskListBO> feedbackPlanBOS = izsyFeedbackPlanMapper.getDemandPlanBySort(reqDTO);
        List<FeedbackPlanListResDTO> feedbackPlanListResDTOS = Lists.newArrayList();
        feedbackPlanBOS.stream().forEach(planTask -> {
            if(planTask==null){
                System.out.println(1);
            }
            FeedbackPlanListResDTO feedbackPlanListResDTO = new FeedbackPlanListResDTO();
            BeanUtils.copyProperties(planTask, feedbackPlanListResDTO);

            List<FeedbackTaskDetailResDTO> task = Lists.newArrayList();
            planTask.getPlanTask().stream().forEach(planTaskBO -> {
                FeedbackTaskDetailResDTO taskDetailResDTO = new FeedbackTaskDetailResDTO();
                BeanUtils.copyProperties(planTaskBO.getPlanTask(),taskDetailResDTO);
                taskDetailResDTO.setTaskName(planTaskBO.getPlanTask().getName());

                //查询负责人  默认最早开始的任务的负责人即为当前需求负责人
                String chargeMan = feedbackMapper.selectChargeManByTaskId(planTaskBO.getPlanTask().getId());
                taskDetailResDTO.setUserName(chargeMan);
                task.add(taskDetailResDTO);

                feedbackPlanListResDTO.setStageId(planTaskBO.getPlanTask().getStageId());
                feedbackPlanListResDTO.setStatus(planTaskBO.getPlanTask().getStatus());


                if(planTaskBO.getPlanTask().getStageName().contains("设计")){
                    taskDetailResDTO.setPercent(0);
                }else if(planTaskBO.getPlanTask().getStageName().contains("开发")){
                    taskDetailResDTO.setPercent(25);
                }else if(planTaskBO.getPlanTask().getStageName().contains("测试")){
                    taskDetailResDTO.setPercent(70);
                }else if(planTaskBO.getPlanTask().getStageName().contains("待发布")){
                    taskDetailResDTO.setPercent(95);
                }else if(planTaskBO.getPlanTask().getStageName().contains("已发布")){
                    taskDetailResDTO.setPercent(100);
                }

            });

            feedbackPlanListResDTO.setChilds(task);
            feedbackPlanListResDTO.setEndTime((DateHelper.dateFormatter(planTask.getExpectOfficialTime(),DateHelper.DATE_FORMAT)));

            double x =(float) 1/planTask.getPlanTask().size();//系数
            double percent = 0.00;
            //计算进度
            for(int i = 0;i<feedbackPlanListResDTO.getChilds().size();i++){
                percent += feedbackPlanListResDTO.getChilds().get(i).getPercent()*x;
            }

            feedbackPlanListResDTO.setPercent(Double.valueOf(new DecimalFormat(".00").format(percent)).intValue());

            if (!CollectionUtils.isEmpty(feedbackPlanListResDTO.getChilds())){
                feedbackPlanListResDTOS.add(feedbackPlanListResDTO);
            }

        });

        List<FeedbackPlanListResDTO> filterList = new ArrayList<>();

        //查询各个阶段的计划
        if (!Strings.isNullOrEmpty(reqDTO.getStage())){
            feedbackPlanListResDTOS.stream().forEach(feedbackPlanListResDTO -> {
                List<FeedbackTaskDetailResDTO> filterChilds = new ArrayList<>();
                filterChilds.addAll(feedbackPlanListResDTO.getChilds().stream().filter(child ->
                        child.getStageId()!= null && child.getStageId().equals(Long.valueOf(reqDTO.getStage()))).collect(Collectors.toList()));
                feedbackPlanListResDTO.setChilds(filterChilds);
                if (!CollectionUtils.isEmpty(feedbackPlanListResDTO.getChilds())){
                    filterList.add(feedbackPlanListResDTO);

                }
            });

            return filterList;

        }

        //查询暂停计划
        if (reqDTO.getStatus()!= null && reqDTO.getStatus() == 0){
            feedbackPlanListResDTOS.stream().forEach(feedbackPlanListResDTO -> {
                List<FeedbackTaskDetailResDTO> filterChilds = new ArrayList<>();
                filterChilds.addAll(feedbackPlanListResDTO.getChilds().stream().filter(child ->
                        child.getStatus() != null && child.getStatus() == reqDTO.getStatus()).collect(Collectors.toList()));
                feedbackPlanListResDTO.setChilds(filterChilds);
                if (!CollectionUtils.isEmpty(feedbackPlanListResDTO.getChilds())){
                    filterList.add(feedbackPlanListResDTO);
                }
            });

            return filterList;
        }

        return feedbackPlanListResDTOS;

    }


}
