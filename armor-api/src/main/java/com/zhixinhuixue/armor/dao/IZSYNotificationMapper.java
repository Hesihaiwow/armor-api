package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.pojo.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by SCH on 2018-12-06
 */
public interface IZSYNotificationMapper {
    /**
     * 批量插入通知
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<Notification> list);
}
