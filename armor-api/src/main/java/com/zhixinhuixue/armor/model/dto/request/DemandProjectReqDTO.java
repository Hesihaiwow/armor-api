package com.zhixinhuixue.armor.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by SCH on 2018-10-26
 */
@ApiModel("新增需求所属项目")
public class DemandProjectReqDTO {

    @ApiModelProperty("需求id")
    @NotBlank(message = "需求id不能为空")
    private String id;

    @ApiModelProperty("项目id")
    @NotBlank(message = "项目id不能为空")
    private String projectId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
