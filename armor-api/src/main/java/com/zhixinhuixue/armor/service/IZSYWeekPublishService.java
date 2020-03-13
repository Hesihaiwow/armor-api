package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.WeekPublishEditReqDTO;
import com.zhixinhuixue.armor.model.dto.request.WeekPublishQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.response.WeekPublishResDTO;
import com.zhixinhuixue.armor.model.dto.response.WeekPublishTaskResDTO;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/11 9:57
 */
public interface IZSYWeekPublishService {

    /**
     * 周发版计划列表
     * @author sch
     * @param reqDTO 参数
     */
    List<WeekPublishTaskResDTO> list(WeekPublishQueryReqDTO reqDTO);

    /**
     * 编辑
     * @author sch
     * @param reqDTO 参数
     */
    void edit(WeekPublishEditReqDTO reqDTO);

    /**
     * 按任务负责人分组查看
     * @author sch
     * @param reqDTO 参数
     */
    List<WeekPublishResDTO> getListByChargeMan(WeekPublishQueryReqDTO reqDTO);
}
