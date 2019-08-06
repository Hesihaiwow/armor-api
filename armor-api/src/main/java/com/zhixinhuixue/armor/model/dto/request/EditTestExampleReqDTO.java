package com.zhixinhuixue.armor.model.dto.request;

/**
 * @author sch
 * @DATE 2019/8/6 16:57
 */
public class EditTestExampleReqDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 检查项
     */
    private String checkPoint;

    /**
     * 预期结果
     */
    private String expectResult;

    /**
     * 备注
     */
    private String remark;

    /**
     * 正反用例
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 评审状态
     */
    private Integer examStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint) {
        this.checkPoint = checkPoint;
    }

    public String getExpectResult() {
        return expectResult;
    }

    public void setExpectResult(String expectResult) {
        this.expectResult = expectResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(Integer examStatus) {
        this.examStatus = examStatus;
    }
}
