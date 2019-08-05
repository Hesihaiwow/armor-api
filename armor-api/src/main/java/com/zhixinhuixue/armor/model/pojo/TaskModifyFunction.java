package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @DATE 2019/8/2 16:10
 *
 * 修改任务关联功能点
 */
public class TaskModifyFunction {
    /**
     * id
     */
    private Long id;

    /**
     * tmId
     */
    private Long tmId;

    /**
     * 功能点id
     */
    private Long functionId;

    /**
     * 复杂度
     */
    private Integer level;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
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
