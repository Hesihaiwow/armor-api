package com.zhixinhuixue.armor.model.dto.response;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Lang on 2017/8/23 0023.
 */
public class IntegralHistoryPageResDTO {

    private String name;

    private BigDecimal integral;

    private int origin;

    private Date createTime;

    private String description;

    private String grade;

    private int type;

    private Double taskHours;

    private Long taskId;

    //taskUserId
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGrade() {
        if (grade != null && !grade.equals("")) {
            double average = Double.parseDouble(grade);
            if (average > 80) {
                return "A";
            } else if (average > 60) {
                return "B";
            } else {
                return "C";
            }
        }
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Double getTaskHours() {
        return taskHours;
    }

    public void setTaskHours(Double taskHours) {
        this.taskHours = taskHours;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
