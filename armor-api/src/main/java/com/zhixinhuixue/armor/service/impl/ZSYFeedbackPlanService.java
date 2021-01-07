package com.zhixinhuixue.armor.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYFeedbackMapper;
import com.zhixinhuixue.armor.dao.IZSYFeedbackPlanMapper;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.FeedbackPlanTaskListBO;
import com.zhixinhuixue.armor.model.dto.request.DemandPlanQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanListReqDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackPlanListResDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackTaskDetailResDTO;
import com.zhixinhuixue.armor.service.IZSYFeedbackPlanService;
import com.zhixinhuixue.armor.source.enums.ZSYPlanSort;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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

    @Resource
    private IZSYFeedbackPlanMapper izsyFeedbackPlanMapper;
    @Resource
    private IZSYFeedbackMapper feedbackMapper;

    @Override
    public List<FeedbackPlanListResDTO> getFeedbackPlanList(FeedbackPlanListReqDTO feedbackPlanListReqDTO) {
        feedbackPlanListReqDTO.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        List<FeedbackPlanTaskListBO> feedbackPlanBOS = izsyFeedbackPlanMapper.getFeedbackPlanBySort(feedbackPlanListReqDTO);
        List<FeedbackPlanListResDTO> feedbackPlanListResDTOS = Lists.newArrayList();

        feedbackPlanBOS.forEach(planTask -> {
            FeedbackPlanListResDTO feedbackPlanListResDTO = new FeedbackPlanListResDTO();
            BeanUtils.copyProperties(planTask, feedbackPlanListResDTO);

            List<FeedbackTaskDetailResDTO> task = Lists.newArrayList();
            planTask.getPlanTask().forEach(planTaskBO -> {
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
        reqDTO.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        List<FeedbackPlanTaskListBO> feedbackPlanBOS = izsyFeedbackPlanMapper.getDemandPlanBySort(reqDTO);
        List<FeedbackPlanListResDTO> feedbackPlanListResDTOS = Lists.newArrayList();
        feedbackPlanBOS.forEach(planTask -> {
            FeedbackPlanListResDTO feedbackPlanListResDTO = new FeedbackPlanListResDTO();
            BeanUtils.copyProperties(planTask, feedbackPlanListResDTO);
            List<String> chargeManList = new ArrayList<>();
            List<FeedbackTaskDetailResDTO> task = Lists.newArrayList();
            planTask.getPlanTask().forEach(planTaskBO -> {
                FeedbackTaskDetailResDTO taskDetailResDTO = new FeedbackTaskDetailResDTO();
                BeanUtils.copyProperties(planTaskBO.getPlanTask(),taskDetailResDTO);
                taskDetailResDTO.setTaskName(planTaskBO.getPlanTask().getName());

                //查询负责人  默认最早开始的任务的负责人即为当前需求负责人
                String chargeMan = feedbackMapper.selectChargeManByTaskId(planTaskBO.getPlanTask().getId());
                chargeManList.add(chargeMan);
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
            if (!CollectionUtils.isEmpty(chargeManList)){
                feedbackPlanListResDTO.setChargeMan(chargeManList.get(0));
            }
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
            feedbackPlanListResDTOS.forEach(feedbackPlanListResDTO -> {
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
            feedbackPlanListResDTOS.forEach(feedbackPlanListResDTO -> {
                List<FeedbackTaskDetailResDTO> filterChilds = new ArrayList<>();
                filterChilds.addAll(feedbackPlanListResDTO.getChilds().stream().filter(child ->
                        child.getStatus() != null && child.getStatus().equals( reqDTO.getStatus())).collect(Collectors.toList()));
                feedbackPlanListResDTO.setChilds(filterChilds);
                if (!CollectionUtils.isEmpty(feedbackPlanListResDTO.getChilds())){
                    filterList.add(feedbackPlanListResDTO);
                }
            });

            return filterList;
        }

        return feedbackPlanListResDTOS;

    }

    @Override
    public List<FeedbackPlanListResDTO> getDemandPlanListByCoach(DemandPlanQueryReqDTO reqDTO) {
        reqDTO.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        List<FeedbackPlanTaskListBO> feedbackPlanBOS = izsyFeedbackPlanMapper.getDemandPlanBySort(reqDTO);
        List<FeedbackPlanListResDTO> feedbackPlanListResDTOS = Lists.newArrayList();
        feedbackPlanBOS.forEach(planTask -> {
            FeedbackPlanListResDTO feedbackPlanListResDTO = new FeedbackPlanListResDTO();
            BeanUtils.copyProperties(planTask, feedbackPlanListResDTO);

            List<FeedbackTaskDetailResDTO> task = Lists.newArrayList();
            List<String> chargeManList = new ArrayList<>();
            planTask.getPlanTask().forEach(planTaskBO -> {
                FeedbackTaskDetailResDTO taskDetailResDTO = new FeedbackTaskDetailResDTO();
                BeanUtils.copyProperties(planTaskBO.getPlanTask(),taskDetailResDTO);
                taskDetailResDTO.setTaskName(planTaskBO.getPlanTask().getName());

                //查询负责人  默认最早开始的任务的负责人即为当前需求负责人
                String chargeMan = feedbackMapper.selectChargeManByTaskId(planTaskBO.getPlanTask().getId());
                chargeManList.add(chargeMan);
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
            if (!CollectionUtils.isEmpty(chargeManList)){
                feedbackPlanListResDTO.setChargeMan(chargeManList.get(0));
            }
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
            if (reqDTO.getStage().equals("1")){
                reqDTO.setStage("212754785051344892");
            }else if (reqDTO.getStage().equals("2")){
                reqDTO.setStage("212754785051344890");
            }else if (reqDTO.getStage().equals("3")){
                reqDTO.setStage("212754785051344894");
            }else if (reqDTO.getStage().equals("4")){
                reqDTO.setStage("212754785051344895");
            }else if (reqDTO.getStage().equals("5")){
                reqDTO.setStage("212754785051344896");
            }else if (reqDTO.getStage().equals("6")){
                reqDTO.setStage("212754785051344898");
            }
            feedbackPlanListResDTOS.forEach(feedbackPlanListResDTO -> {
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
            feedbackPlanListResDTOS.forEach(feedbackPlanListResDTO -> {
                List<FeedbackTaskDetailResDTO> filterChilds = new ArrayList<>();
                filterChilds.addAll(feedbackPlanListResDTO.getChilds().stream().filter(child ->
                        child.getStatus() != null && child.getStatus().equals( reqDTO.getStatus())).collect(Collectors.toList()));
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
