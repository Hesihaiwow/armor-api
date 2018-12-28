package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Notification;

/**
 * Created by SCH on 2018-12-28
 */
public class NotificationBO extends Notification {
    /**
     * 通知人
     */
    private String noticeUser;

    public String getNoticeUser() {
        return noticeUser;
    }

    public void setNoticeUser(String noticeUser) {
        this.noticeUser = noticeUser;
    }
}
