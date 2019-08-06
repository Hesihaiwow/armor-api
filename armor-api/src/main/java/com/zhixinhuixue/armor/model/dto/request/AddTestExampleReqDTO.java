package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author sch
 * @DATE 2019/8/6 9:34
 */
public class AddTestExampleReqDTO {
    /**
     * 关联任务id
     */
    @NotNull(message = "关联任务id不能为空")
    private Long taskId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 正反用例   0:正常用例  1:异常用例
     */
    @NotNull(message = "正反用例不能为空")
    private Integer type;

    /**
     * 关联功能点id
     */
    @NotNull(message = "关联功能点id不能为空")
    private Long functionId;

    /**
     * 检查项
     */
//    @NotBlank(message = "检查项不能为空")
    private String checkPoint;

    /**
     * 预期结果
     */
//    @NotBlank(message = "预期结果不能为空")
    private String expectResult;

    /**
     * 备注
     */
    private String remark;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
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
}
