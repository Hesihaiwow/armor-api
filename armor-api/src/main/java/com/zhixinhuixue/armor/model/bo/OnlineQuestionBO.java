package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.OnlineQuestion;

/**
 * Created by SCH on 2018-12-27
 */
public class OnlineQuestionBO extends OnlineQuestion {
    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 创建人姓名
     * @return
     */
    private String userName;

    /**
     * 创建人头像
     * @return
     */
    private String avatar_url;

    /**
     * 项目图片
     * @return
     */
    private String projectImage;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
