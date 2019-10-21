package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author sch
 * @time 2019/10/14 10:00
 */
@ApiModel
public class QueryTaskBugPageReqDTO {
    /**
     * 关联任务
     */
//    @NotNull(message = "关联任务id不能为空")
    @ApiModelProperty("任务Id")
    private Long taskId;

    /**
     * 页码
     */
    @ApiModelProperty("页码")
    private Integer pageNum;

    /**
     * 状态
     */
    @ApiModelProperty("查询状态(1:全部, 2:已分派和打回, 3:已解决, 4:已关闭)")
    private Integer status;

    /**
     * 是否查看全部
     */
    @ApiModelProperty("是否查看全部(0:查看个人(默认), 1:查看全部)")
    @NotNull(message = "是否查看全部不能为空")
    private Integer selectAll;

    /**
     * 是否查看所有任务
     */
    private Integer allTask;

    public Integer getAllTask() {
        return allTask;
    }

    public void setAllTask(Integer allTask) {
        this.allTask = allTask;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSelectAll() {
        return selectAll;
    }

    public void setSelectAll(Integer selectAll) {
        this.selectAll = selectAll;
    }
}
