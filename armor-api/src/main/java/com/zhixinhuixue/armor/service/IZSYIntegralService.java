package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.bo.UserIntegralHistoryBO;
import com.zhixinhuixue.armor.model.dto.request.IntegralResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralHistoryPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralResDTO;

import java.util.List;


/**
 * Created by Lang on 2017/8/7 0007.
 */
public interface IZSYIntegralService {

    /**
     * 查询积分列表
     * @param startTime 结束时间
     * @param endTime 页码
     * @return
     */
    List<IntegralPageResDTO> getIntegralPage(String startTime, String endTime);

    /**
     * 用户积分排名信息
     * @return
     */
    UserIntegralResDTO getUserIntegral();

    /**
     * 用户积分历史
      * @param id
     * @param startTime
     * @param endTime
     * @return
     */
    PageInfo<IntegralHistoryPageResDTO> getIntegralHistory(Long id, int pageIndex, String startTime, String endTime);

    /**
     * 添加积分记录
     * @param integralResDTO
     */
    void addIntegral(IntegralResDTO integralResDTO);
}
