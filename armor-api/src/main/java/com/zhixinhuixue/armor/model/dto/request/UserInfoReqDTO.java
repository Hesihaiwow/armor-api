package com.zhixinhuixue.armor.model.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by SCH on 2018-12-07
 */

/**
 * 用户修改个人基本信息
 */
public class UserInfoReqDTO {
    @Size(min = 1,max = 20,message = "用户名称长度在{min}~{max}之间")
    @NotNull(message = "用户名称不能为空")
    private String name;

    @Pattern(regexp = "^1[3|4|5|6|7|8|9][0-9]\\d{8}$",message = "用户手机号格式错误")
    private String phone;

    @Pattern(regexp = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}",message = "邮箱格式错误")
    @NotNull(message = "邮箱不能为空")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
