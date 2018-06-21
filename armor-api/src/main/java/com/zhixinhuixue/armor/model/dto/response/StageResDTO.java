package com.zhixinhuixue.armor.model.dto.response;

import com.zhixinhuixue.armor.model.pojo.Stage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Lang on 2017/8/10 0010.
 */
public class StageResDTO extends Stage {

    @Size(min = 1,max = 10,message = "阶段名称长度在{min}~{max}之间")
    @NotNull(message = "阶段名称不能为空")
    private String name;

    private Long firstId;

    private Long lastId;

    //序号
    private Integer num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFirstId() {
        return firstId;
    }

    public void setFirstId(Long firstId) {
        this.firstId = firstId;
    }

    public Long getLastId() {
        return lastId;
    }

    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
