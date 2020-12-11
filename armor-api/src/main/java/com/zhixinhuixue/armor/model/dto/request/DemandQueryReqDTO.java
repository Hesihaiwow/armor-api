package com.zhixinhuixue.armor.model.dto.request;

import java.util.Date;

/**
 * 查询需求条件
 * Created by SCH on 2018-10-16
 */
public class DemandQueryReqDTO {
    /**
     * 页码
     */
    private Integer pageNum;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 是否已读(0:未读,1:已读)
     */
    private Integer readStatus;

    /**
     * 负责人
     */
    private Long chargeMan;

    /**
     * 任务负责人
     */
    private String taskChargeMan;

    /**
     * 来源
     */
    private String origin;

    /**
     * 类型(0:个人建议,1:市场反馈,2:公司决策)
     */
    private Integer type;

    /**
     * 上线时间(开始)
     */
    private Date beginTime;

    /**
     * 上线时间(结束)
     */
    private Date endTime;

    /**
     * 排序(0:提出日期升序,1:提出日期升序降序,2:期待上线日期升序,3:期待上线日期升序降序,4:点赞数升序,5:点赞数升序降序)
     */
    private Integer sort;

    /**
     * 是否由学管提出
     */
    private Integer fromCoach;

    /**
     * 提出时间(开始)
     */
    private Date fbTimeStart;

    /**
     * 提出时间(结束)
     */
    private Date fbTimeEnd;

    /**
     * 来源
     */
    private Integer source;

    /**
     * 标题
     */
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskChargeMan() {
        return taskChargeMan;
    }

    public void setTaskChargeMan(String taskChargeMan) {
        this.taskChargeMan = taskChargeMan;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Date getFbTimeEnd() {
        return fbTimeEnd;
    }

    public void setFbTimeEnd(Date fbTimeEnd) {
        this.fbTimeEnd = fbTimeEnd;
    }

    public Date getFbTimeStart() {
        return fbTimeStart;
    }

    public void setFbTimeStart(Date fbTimeStart) {
        this.fbTimeStart = fbTimeStart;
    }

    public Integer getFromCoach() {
        return fromCoach;
    }

    public void setFromCoach(Integer fromCoach) {
        this.fromCoach = fromCoach;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getChargeMan() {
        return chargeMan;
    }

    public void setChargeMan(Long chargeMan) {
        this.chargeMan = chargeMan;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
