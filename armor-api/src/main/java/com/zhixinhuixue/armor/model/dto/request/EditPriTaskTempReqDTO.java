package com.zhixinhuixue.armor.model.dto.request;

/**
 * 编辑个人任务入参
 *
 * @author SCH
 * @create 2020年12月09日
 */
public class EditPriTaskTempReqDTO extends AddPriTaskTempReqDTO{
    /**
     * 任务级别
     */
    private Integer taskLevel;

    /**
     * 审核意见
     */
    private String suggest;

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public Integer getTaskLevel() {
        return taskLevel;
    }

    public void setTaskLevel(Integer taskLevel) {
        this.taskLevel = taskLevel;
    }
}