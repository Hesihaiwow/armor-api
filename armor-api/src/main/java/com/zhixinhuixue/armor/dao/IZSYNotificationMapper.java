package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.NotificationBO;
import com.zhixinhuixue.armor.model.dto.request.NoticeReqDTO;
import com.zhixinhuixue.armor.model.pojo.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

    /**
     * 查询最近5条未读通知
     * @param userId
     * @return
     */
    List<Notification> selectUnReadNotice(@Param("userId") Long userId);

    /**
     * 查询所有通知
     * @param userId
     * @return
     */
    Page<Notification> selectAllNotice(@Param("userId")Long userId, @Param("reqDTO") NoticeReqDTO reqDTO);

    /**
     * 查询所有未读通知条数
     * @param userId
     * @return
     */
    Integer selectUnreadNoticeNum(Long userId);

    /**
     * 读取通知
     * @param nid
     * @param readTime
     * @return
     */
    int updateNoticeById(@Param("nid") Long nid,@Param("readTime") Date readTime);

    /**
     * 插入通知
     * @param notification
     * @return
     */
    int insertNotice(@Param("notification") Notification notification);

    /**
     * 查询所有人所有通知
     * @param reqDTO
     * @return
     */
    Page<NotificationBO> selectEveryoneNotice(@Param("reqDTO")NoticeReqDTO reqDTO);

    /**
     * 阅读所有通知
     * @param userId
     * @return
     */
    int updateNoticeByUser(@Param("userId") Long userId,@Param("readTime") Date readTime);
}
