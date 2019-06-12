package com.zhixinhuixue.armor.model.dto.response;

/**
 * @author sch
 * @DATE 2019/6/10 17:45
 */
public class PlatformResDTO {
    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 随机颜色
     * @return
     */
    private Integer color;

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

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
}
