package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.response.UserTaskIntegralResDTO;

/**
 * @author sch
 * @DATE 2019/9/9 9:51
 */
public interface IZSYUserTaskIntegralService {
    /**
     * 统计7月之后的多人任务积分
     * @author sch
     */
    void updateAfterJuly();

    /**
     * 统计7月之后的个人任务积分
     * @author sch
     */
    void updateAfterJulyPrivate();

    /**
     * 统计7月之后的有功能点的已完成的多人任务积分
     * @author sch
     */
    void updateAfterJulyFinished();

    /**
     * 统计7月之后的有任务级别的而且已经完成的个人任务积分
     * @author sch
     */
    void updateAfterJulyPrivateFinished();

    /**
     * 个人积分信息
     * @author sch
     */
    UserTaskIntegralResDTO getPersonalIntegral();
}
