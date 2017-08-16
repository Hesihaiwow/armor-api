package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.response.IntegralPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralResDTO;


/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYIntegralService {

    /**
     * 查询积分列表
     * @param pageIndex 开始时间
     * @param startTime 结束时间
     * @param endTime 页码
     * @return
     */
    PageInfo<IntegralPageResDTO> getIntegralPage(int pageIndex, String startTime, String endTime);

    /**
     * 查询用户积分记录
     * @return
     */
    UserIntegralResDTO getUserIntegral();


}
