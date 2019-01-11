package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.YearReqDTO;
import com.zhixinhuixue.armor.model.dto.response.AnnualFeedbackResDTO;
import com.zhixinhuixue.armor.model.dto.response.AnnualTaskResDTO;

/**
 * @author SCH
 * @date 2019/1/10 13:02
 */
public interface IZSYDataService {

    /**
     * 年度需求总数(学管端,其他)
     * @param reqDTO
     * @return
     * @author sch
     */
    AnnualFeedbackResDTO getAnnualFeedbackData(YearReqDTO reqDTO);

    /**
     * 年度任务总数(多人任务,个人任务)
     * @author sch
     * @param reqDTO
     * @return
     */
    AnnualTaskResDTO getAnnualTaskData(YearReqDTO reqDTO);
}
