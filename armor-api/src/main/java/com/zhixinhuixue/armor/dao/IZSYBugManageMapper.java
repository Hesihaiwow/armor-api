package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.BugManageBO;
import com.zhixinhuixue.armor.model.bo.BugManageListBO;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.pojo.BugManage;

/**
 * Created by Lang on 2017/11/7 0008.
 */
public interface IZSYBugManageMapper {

    /**
     * 获取bug处理
     * @return
     */
    Page<BugManageListBO> getBugList(BugListReqDTO bugListReqDTO);

    /**
     * 插入bug处理结果
     * @param bugManage
     * @return
     */
    int insertBug(BugManage bugManage);

    /**
     * 更新Bug处理结果
     * @param bugManage
     * @return
     */
    int updateBug(BugManage bugManage);

    /**
     * 获取bug处理详情
     * @param id
     * @return
     */
    BugManageBO selectDetailById(Long id);

    /**
     * 删除Bug处理记录
     * @param id
     * @return
     */
    int deleteById(Long id);
}
