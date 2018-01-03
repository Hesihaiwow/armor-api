package com.zhixinhuixue.armor.dao;


import com.zhixinhuixue.armor.model.pojo.PublishInfo;

import java.util.Date;

public interface IZSYPublishInfoMapper {


    /**
     * 添加发版时间
     * @param publishTime
     * @return
     */
    int updatePublishInfo(Date publishTime);


    /**
     * 获取发版时间
     * @return
     */
    Date getPublishInfo();


}