package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @DATE 2019/4/17 17:53
 */
public class BugStatisticsQueryReqDTO {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 分类
     */
    private Integer categoryId;

    /**
     * 提交时间 开始
     */
    private Integer beginTime;

    /**
     * 提交时间 截止
     */
    private Integer endTime;


}
