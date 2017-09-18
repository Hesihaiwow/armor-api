package com.zhixinhuixue.armor.model.dto.request;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

/**
 * Created by Tate on 2017/9/14.
 */
public class UploadAvatarReqDTO {

    @NotEmpty(message = "url不能为空")
    @URL(message = "url地址不正确")
    private String url;
    @NotNull(message = "userID不能为空")
    private Long userId;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
