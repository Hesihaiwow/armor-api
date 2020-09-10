package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author sch
 * @time 2020/9/8 14:14
 */
public class AddPlatformReqDTO {
    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 256,message = "名称不能大于{max}")
    private String name;

    /**
     * 分组
     */
    @NotNull(message = "分组不能为空")
    private Integer groupMark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGroupMark() {
        return groupMark;
    }

    public void setGroupMark(Integer groupMark) {
        this.groupMark = groupMark;
    }
}
