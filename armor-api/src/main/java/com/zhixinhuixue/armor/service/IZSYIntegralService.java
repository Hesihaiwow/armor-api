package com.zhixinhuixue.armor.service;


import com.github.pagehelper.Page;

import java.util.Date;

/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYIntegralService {

    /**
     * 查询积分列表
     * @param startTime
     * @param endTime
     * @return
     */
    Page findIntegral(Date startTime,Date endTime);

}
