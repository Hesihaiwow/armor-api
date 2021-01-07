package com.zhixinhuixue.armor.dao;


import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.Date;

public interface IZSYPublishInfoMapper {


    /**
     * 添加发版时间
     * @param publishTime
     * @return
     */
    int updatePublishInfo(@Param("time") Date publishTime,@Param("id") Long id);

    int insertPublishInfo(@Param("time") Date publishTime, @Param("id") Long id, @Param("orgId")Long orgId);


    /**
     * 获取发版时间
     * @return
     */
    Date getPublishInfo(Long id);


}