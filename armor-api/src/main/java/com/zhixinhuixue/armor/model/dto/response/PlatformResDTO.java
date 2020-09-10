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
     * 分组
     */
    private Integer groupMark;

    /**
     * 分组名
     */
    private String groupMarkText;

    /**
     * 随机颜色
     */
    private Integer color;

    public Integer getGroupMark() {
        return groupMark;
    }

    public void setGroupMark(Integer groupMark) {
        this.groupMark = groupMark;
    }

    public String getGroupMarkText() {
        return groupMarkText;
    }

    public void setGroupMarkText(String groupMarkText) {
        this.groupMarkText = groupMarkText;
    }

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
