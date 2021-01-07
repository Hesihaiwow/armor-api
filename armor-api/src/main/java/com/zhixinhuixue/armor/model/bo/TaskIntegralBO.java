package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OrgIdField;
import org.aspectj.weaver.ast.Or;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/8/8 14:17
 *
 * 任务积分
 */
public class TaskIntegralBO extends OrgIdField {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 临时任务id
     */
    private Long ttId;

    /**
     * 任务评价
     */
    private TaskEvaluationScoreAndNumBO evaluationScoreAndNumBO;

    /**
     * 任务功能点
     */
    private List<TaskTempFunctionBO> functions;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TaskEvaluationScoreAndNumBO getEvaluationScoreAndNumBO() {
        return evaluationScoreAndNumBO;
    }

    public void setEvaluationScoreAndNumBO(TaskEvaluationScoreAndNumBO evaluationScoreAndNumBO) {
        this.evaluationScoreAndNumBO = evaluationScoreAndNumBO;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTtId() {
        return ttId;
    }

    public void setTtId(Long ttId) {
        this.ttId = ttId;
    }

    public List<TaskTempFunctionBO> getFunctions() {
        return functions;
    }

    public void setFunctions(List<TaskTempFunctionBO> functions) {
        this.functions = functions;
    }
}
