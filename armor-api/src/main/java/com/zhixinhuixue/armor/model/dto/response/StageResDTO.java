package com.zhixinhuixue.armor.model.dto.response;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Lang on 2017/8/10 0010.
 */
public class StageResDTO {

    private long id;

    @Size(min = 1,max = 10,message = "阶段名称长度在{min}~{max}之间")
    @NotNull(message = "阶段名称不能为空")
    private String name;

    private Integer sort;

    //序号
    private Integer num;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
