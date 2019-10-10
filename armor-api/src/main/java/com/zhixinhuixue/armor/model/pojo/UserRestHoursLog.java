package com.zhixinhuixue.armor.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sch
 * @time 2019/10/10 9:48
 *
 * 调休日志
 */
public class UserRestHoursLog {
    /**
     * id
     */
    private Long id;

    /**
     * 用户
     */
    private Long userId;
    private String userName;

    /**
     * 请假id
     */
    private Long leaveId;

    /**
     * 调休时长(正数为增加,负数是减少)
     */
    private BigDecimal restHours;

    /**
     * 类型(1:手动修改, 2:请假扣除,3: 加班累加)
     */
    private Integer type;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 记录时间
     */
    private Date recordTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Long leaveId) {
        this.leaveId = leaveId;
    }

    public BigDecimal getRestHours() {
        return restHours;
    }

    public void setRestHours(BigDecimal restHours) {
        this.restHours = restHours;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }
}
