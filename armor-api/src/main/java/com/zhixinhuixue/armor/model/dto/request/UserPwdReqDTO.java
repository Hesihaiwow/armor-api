package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Akuma on 2017/8/9.
 */
public class UserPwdReqDTO {

    //原始密码
    @NotNull(message = "原始密码不能为空")
    private String originalPassword;

    //新密码
    @Size(min = 6,max = 16,message = "新密码长度在{min}~{max}之间")
    @NotNull(message = "新密码不能为空")
    private String newPassword;


    public String getOriginalPassword() {
        return originalPassword;
    }

    public void setOriginalPassword(String originalPassword) {
        this.originalPassword = originalPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
