package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.AddUserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserTaskIntegralReqDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralHistoryPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserTaskIntegralListResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserTaskIntegralResDTO;

import java.util.List;
import java.util.Map;

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

    /**
     * 查看积分列表
     * @author sch
     * @param reqDTO 参数
     */
    List<UserTaskIntegralListResDTO> getIntegralRank(UserTaskIntegralReqDTO reqDTO);

    /**
     * 获取积分列数
     * @author sch
     * @param reqDTO 参数
     */
    Map getIntegralCount(UserTaskIntegralReqDTO reqDTO);

    /**
     * 获取用户积分历史
     * @author sch
     * @param reqDTO 参数
     */
    PageInfo<UserIntegralHistoryPageResDTO> getIntegralHistoryPage(UserTaskIntegralReqDTO reqDTO);

    /**
     * 添加积分
     * @author sch
     * @param reqDTO 参数
     */
    void addIntegral(AddUserTaskIntegralReqDTO reqDTO);

    /**
     * 统计7月之后的有功能点的且已评价多人任务积分(已完成,没结束)
     * @author sch
     */
    void updateAfterJulyWithEvaluation();

    /**
     * 统计7月之后的没有功能点的且已评价多人任务积分(已完成,没结束)
     * @author sch
     */
    void updateAfterJulyWithoutEvaluation();

    /**
     * 更新7月之后,没有任务级别的个人任务,新增级别
     * @author sch
     */
    void privateTaskAddLevel();

    /**
     * 更新7月之后,没有任务级别的多人任务,根据工时新增级别
     * @author sch
     */
    void multiTaskAddLevel();

    /**
     * 计算7月之后完成的个人任务积分
     * @author sch
     */
    void countPrivateIntegral();

    /**
     * 计算7月之后完成的多人任务积分
     * @author sch
     */
    void countMultiIntegral();

    /**
     * 计算7月之后完成的未结束的多人任务积分
     * @author sch
     */
    void countMultiCompletedIntegral();
}
