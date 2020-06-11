package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author sch
 * @time 2020/1/8 10:22
 */
public class EditWorkGroupReqDTO {
    @NotNull(message = "工作组id不能为空")
    private Long id;

    @NotBlank(message = "组名不能为空")
    @Size(min = 1,max = 20,message = "组名长度在{min}~{max}之间")
    private String name;

    @NotBlank(message = "描述不能为空")
    @Size(min = 1,max = 200,message = "描述长度在{min}~{max}之间")
    private String description;

    @NotNull(message = "请选择工作组负责人")
    private Long leader;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLeader() {
        return leader;
    }

    public void setLeader(Long leader) {
        this.leader = leader;
    }
}
