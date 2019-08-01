package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @DATE 2019/7/29 10:30
 *
 * 临时任务功能点
 */
public class TaskTempFunction {
    /**
     * id
     */
    private Long id;

    /**
     * 临时任务id
     */
    private Long ttId;

    /**
     * 关联功能点
     */
    private Long functionId;

    /**
     * 复杂度(1~5级)
     */
    private Integer level;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTtId() {
        return ttId;
    }

    public void setTtId(Long ttId) {
        this.ttId = ttId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
