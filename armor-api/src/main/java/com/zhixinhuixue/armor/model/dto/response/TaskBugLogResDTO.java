package com.zhixinhuixue.armor.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author sch
 * @time 2019/10/14 13:16
 */
@ApiModel
public class TaskBugLogResDTO {
    /**
     * id
     */
    @ApiModelProperty("日志id")
    private Long tblId;

    /**
     * bugid
     */
    @ApiModelProperty("任务bugId")
    private Long tbId;

    /**
     * 内容
     */
    @ApiModelProperty("日志内容 ")
    private String content;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    private Date createTime;

    public Long getTblId() {
        return tblId;
    }

    public void setTblId(Long tblId) {
        this.tblId = tblId;
    }

    public Long getTbId() {
        return tbId;
    }

    public void setTbId(Long tbId) {
        this.tbId = tbId;
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
}
