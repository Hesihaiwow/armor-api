package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.BugManageListBO;
import com.zhixinhuixue.armor.model.bo.BugUserBO;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.pojo.BugManage;
import com.zhixinhuixue.armor.model.pojo.BugUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Lang on 2017/11/7 0008.
 */
public interface IZSYBugUserMapper {

    /**
     * 批量插入bug用户处理结果
     * @param bugUsers
     * @return
     */
    int insertList(List<BugUser> bugUsers);


    int deleteById(Long id);
}
