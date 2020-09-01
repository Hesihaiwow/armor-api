package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.dao.IZSYWeekPublishPlanMapper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.service.IZSYWeekPublishService;
import com.zhixinhuixue.armor.source.enums.ZSYJobRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

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
     * 验证当前角色是否为开发人员
     * @param jobRole 角色
     */
    private Boolean getIsDeveloper(Integer jobRole){
        return jobRole == ZSYJobRole.JAVA.getValue()
                || jobRole == ZSYJobRole.C.getValue()
                || jobRole == ZSYJobRole.PHP.getValue()
                || jobRole == ZSYJobRole.FRONT.getValue()
                || jobRole == ZSYJobRole.IOS.getValue()
                || jobRole == ZSYJobRole.ANDROID.getValue()
                || jobRole == ZSYJobRole.ARTIFICIAL.getValue();
    };


}
