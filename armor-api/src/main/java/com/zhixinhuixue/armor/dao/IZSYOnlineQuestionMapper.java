package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.OnlineQuestionBO;
import com.zhixinhuixue.armor.model.dto.request.MyQuestionReqDTO;
import com.zhixinhuixue.armor.model.pojo.OnlineQuestion;
import com.zhixinhuixue.armor.model.pojo.QuestionUrl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by SCH on 2018-12-27
 */
public interface IZSYOnlineQuestionMapper {
    /**
     * 新增线上问题
     * @param onlineQuestion
     * @return
     */
    int insertQuestion(OnlineQuestion onlineQuestion);

    /**
     * 新增线上问题图片地址
     * @param list
     * @return
     */
    int insertUrls(List<QuestionUrl> list);

    /**
     * 根据状态分页查看我的线上问题
     * @param reqDTO
     * @return
     */
    Page<OnlineQuestionBO> selectOnlineQuestionPageByStatus(@Param("reqDTO") MyQuestionReqDTO reqDTO,@Param("userId")Long userId);

    /**
     * 查看审核中(审核通过)线上问题
     * @param reqDTO
     * @return
     */
    Page<OnlineQuestionBO> selectCheckQuestionPageByReviewStatus(@Param("reqDTO") MyQuestionReqDTO reqDTO);

    /**
     * 根据id查询 线上问题
     * @param oqrId
     * @return
     */
    OnlineQuestion selectById(@Param("oqrId") Long oqrId);

    /**
     * 审核通过
     * @param onlineQuestion
     * @return
     */
    int updateById(@Param("onlineQuestion") OnlineQuestion onlineQuestion);

    /**
     * 删除线上问题图片地址
     * @param oqrId
     * @return
     */
    int deleteUrlByOqrId(@Param("oqrId")Long oqrId);

    /**
     * 查看进行中线上问题
     * @param userId
     * @return
     */
    List<OnlineQuestionBO> selectRunningQuestion(Long userId);

    /**
     * 查看已完成线上问题
     * @param userId
     * @return
     */
    Page<OnlineQuestionBO> selectCompletedQuestion(Long userId);

    /**
     * 查询图片地址
     * @param oqrId
     * @return
     */
    List<String> selectUrlsById(Long oqrId);

    /**
     * 查看待审核问题
     * @return
     */
    Page<OnlineQuestionBO> selectWaitQuestion();

    /**
     * 查看审核通过线上问题
     * @return
     */
    Page<OnlineQuestionBO> selectAcceptedQuestion();
}
