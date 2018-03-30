package com.zhixinhuixue.armor.service.impl;

import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.dao.IZSYFeedbackPlanMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.FeedbackPlanTaskListBO;
import com.zhixinhuixue.armor.model.dto.request.FeedbackPlanListReqDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackPlanListResDTO;
import com.zhixinhuixue.armor.model.dto.response.FeedbackTaskDetailResDTO;
import com.zhixinhuixue.armor.service.IZSYFeedbackPlanService;
import com.zhixinhuixue.armor.source.enums.ZSYPlanSort;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Collections;
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

    @Override
    public List<FeedbackPlanListResDTO> getFeedbackPlanList(FeedbackPlanListReqDTO feedbackPlanListReqDTO) {
        List<FeedbackPlanTaskListBO> feedbackPlanBOS = izsyFeedbackPlanMapper.getFeedbackPlanBySort(feedbackPlanListReqDTO);
        List<FeedbackPlanListResDTO> feedbackPlanListResDTOS = Lists.newArrayList();

        feedbackPlanBOS.stream().forEach(planTask -> {
            FeedbackPlanListResDTO feedbackPlanListResDTO = new FeedbackPlanListResDTO();
            BeanUtils.copyProperties(planTask, feedbackPlanListResDTO);

            List<FeedbackTaskDetailResDTO> task = Lists.newArrayList();
            planTask.getPlanTask().stream().forEach(planTaskBO -> {
                FeedbackTaskDetailResDTO taskDetailResDTO = new FeedbackTaskDetailResDTO();
                BeanUtils.copyProperties(planTaskBO.getPlanTask(),taskDetailResDTO);
                taskDetailResDTO.setTaskName(planTaskBO.getPlanTask().getName());
                task.add(taskDetailResDTO);

                if(planTaskBO.getPlanTask().getStageName().contains("设计")){
                    taskDetailResDTO.setPercent(30);
                }else if(planTaskBO.getPlanTask().getStageName().contains("开发")){
                    taskDetailResDTO.setPercent(70);
                }else if(planTaskBO.getPlanTask().getStageName().contains("测试")){
                    taskDetailResDTO.setPercent(95);
                }else if(planTaskBO.getPlanTask().getStageName().contains("发布")){
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

        if(feedbackPlanListReqDTO.getSort()!=null){
            if(feedbackPlanListReqDTO.getSort().equals(ZSYPlanSort.PERCENTDOWN.getValue())){
                return  feedbackPlanListResDTOS.stream().sorted(Comparator.comparing(FeedbackPlanListResDTO::getPercent)).collect(Collectors.toList());
            }else if(feedbackPlanListReqDTO.getSort().equals(ZSYPlanSort.PERCENTTP.getValue())){
                return feedbackPlanListResDTOS.stream().sorted(Comparator.comparing(FeedbackPlanListResDTO::getPercent).reversed()).collect(Collectors.toList());
            }
        }

        return feedbackPlanListResDTOS;

    }


}
