package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.EvaluationPageQueryReqDTO;
import com.zhixinhuixue.armor.model.pojo.TaskEvaluation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/5/22 11:00
 */
public interface IZSYTaskEvaluationMapper {

    /**
     * 根据任务查询已经评价的用户
     * @author sch
     * @param taskId
     * @return
     */
    List<Long> selectEvaluatedUsersByTask(@Param("taskId") Long taskId);

    /**
     * 批量插入任务评价
     * @author sch
     * @param list
     * @return
     */
    int insertBatch(List<TaskEvaluation> list);

    /**
     * 根据任务和用户查询评价
     * @author sch
     * @param taskId
     * @param userId
     * @return
     */
    List<EvaluationBO> selectByTaskAndUser(@Param("taskId") Long taskId, @Param("userId")  Long userId);

    List<EvaluationScoreBO> selectByTaskAndTaskUser(@Param("taskId") Long taskId, @Param("userId")  Long userId,@Param("evaluateUserId")  Long evaluateUserId);

    /**
     * 查询待评价任务
     * @author sch
     * @return
     */
    List<TaskDetailBO> selectWaitEvaluatedByUser(@Param("userId")Long userId);

    /**
     * 根据用户查询已经评价的任务
     * @author sch
     * @param userId
     * @return
     */
    List<Long> selectEvaluatedTaskIdsByUser(@Param("userId")Long userId);

    /**
     * 分页根据用户查询已经评价的任务
     * @author sch
     * @param userId
     * @return
     */
    Page<Long> selectEvaluatedTaskIdsByUserPage(@Param("userId")Long userId);

    /**
     * 查看某个人对当前用户的评价
     * @param taskId
     * @param userId
     * @return
     */
    List<EvaluationBO> selectSomeoneToMe(@Param("taskId") Long taskId, @Param("userId")  Long userId
            , @Param("evaluateUserId")  Long evaluateUserId);

    /**
     * 查看某人都其他用户的评价
     * @param taskId
     * @param evaluateUserId
     * @return
     */
    List<EvaluationBO> selectMeToOthers(@Param("taskId") Long taskId, @Param("evaluateUserId")  Long evaluateUserId);


    /**
     * 管理员查看其他人对某个人的评价
     * @param taskId
     * @param taskUserId
     * @return
     */
    List<EvaluationBO> selectOthersToMe(@Param("taskId") Long taskId, @Param("taskUserId")  Long taskUserId);

    /**
     * 管理员分页查看用户所有任务综合评价
     * @author sch
     * @param reqDTO
     * @return
     */
    Page<TaskEvaluationPageBO> selectUserAvgEvaluation(@Param("reqDTO") EvaluationPageQueryReqDTO reqDTO);

    /**
     * 查询任务
     * @author sch
     * @param reqDTO
     * @param userId
     * @return
     */
    List<TaskBaseBO> selectTaskBaseInfoByTaskUser(@Param("reqDTO")EvaluationPageQueryReqDTO reqDTO, @Param("userId")Long userId);

    /**
     * 查询任务数量
     * @author sch
     * @param taskIds
     * @param userId
     * @return
     */
    Integer selectTaskNumByTaskUser(@Param("taskIds")List<Long> taskIds, @Param("userId")Long userId);

    /**
     * 查询各项评分
     * @author sch
     * @param taskIds
     * @param userId
     * @return
     */
    List<OptionScoreBO> selectOptionScoreByTaskUser(@Param("taskIds")List<Long> taskIds, @Param("userId")Long userId);

    /**
     * 查询评价数量
     * @author sch
     * @param taskIds
     * @param userId
     * @return
     */
    Integer selectEvaluationNumByTaskUser(@Param("taskIds")List<Long> taskIds, @Param("userId")Long userId);
}
