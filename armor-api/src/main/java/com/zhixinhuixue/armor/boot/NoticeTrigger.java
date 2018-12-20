package com.zhixinhuixue.armor.boot;

import com.zhixinhuixue.armor.service.IZSYTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by SCH on 2018-12-10
 */
@Component
public class NoticeTrigger {

    @Autowired
    private IZSYTaskService taskService;

    /**
     * 9点定时检查是否有主任务超时,有的话,新增通知并短信通知负责人
     */
    @Scheduled(cron = "0 0 9 ? * *")
    public void noticeDelayMasterTaskPrincipal(){
        taskService.noticeDelayMasterTaskPrincipal();
    }

    /**
     * 9点定时检查是否有子任务超时,有的话,新增通知并短信通知负责人
     */
    @Scheduled(cron = "0 2 9 ? * *")
    public void noticeDelaySonTaskPrincipal(){
        taskService.noticeDelaySonTaskPrincipal();
    }

    /**
     * 9点定时检查是否有子任务超时,有的话,新增通知并短信通知当前子任务负责人
     */
    @Scheduled(cron = "0 4 9 ? * *")
    public void noticeDelaySonTaskChargeMan(){
        taskService.noticeDelaySonTaskChargeMan();
    }
}
