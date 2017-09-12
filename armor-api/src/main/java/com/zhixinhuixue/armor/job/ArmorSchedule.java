package com.zhixinhuixue.armor.job;

import com.zhixinhuixue.armor.service.IZSYTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Tate on 2017/9/5.
 */
@Component
public class ArmorSchedule {
    @Autowired
    private IZSYTaskService taskService;

    /**
     * 每10分钟执行一次,将评价完成的任务结算积分
     */
    @Scheduled(fixedRate = 600000)
    public void sendNotCachedZsyServiceToRedis() {
        taskService.syncSettlementTask();
    }

}
