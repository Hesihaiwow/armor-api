package com.zhixinhuixue.armor.model.pojo;

/**
 * @author sch
 * @time 2019/10/12 17:25
 *
 * 任务id
 */
public class TaskBug {
    /**
     * bugId
     */
    private Long tbId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 分派人
     */
    private Long handlerId;

    /**
     * 严重程度(1:需求或建议,2:一般错误,3:主要错误,4:严重错误)
     */
    private Integer  severity;

    /**
     * 出现频率(1:固定重现, 2:测试随机, 3:无法重现)
     */
    private Integer frequency;

    /**
     * 摘要
     */
    private String title;
}
