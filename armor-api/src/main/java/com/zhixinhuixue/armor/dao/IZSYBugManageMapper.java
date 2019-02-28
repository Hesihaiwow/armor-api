package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.BugManageBO;
import com.zhixinhuixue.armor.model.bo.BugManageListBO;
import com.zhixinhuixue.armor.model.bo.OnlineBugBO;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.pojo.BugManage;
import com.zhixinhuixue.armor.model.pojo.OnlineBugManage;
import org.apache.ibatis.annotations.Param;

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

    /**
     *  插入线上bug
     * @author sch
     * @param bugManage
     */
    int insertBugManager(OnlineBugManage bugManage);

    /**
     * 分页查询线上bug
     * @author sch
     * @param bugListReqDTO
     * @return
     */
    Page<OnlineBugBO> selectOnlineBugPage(@Param("bugListReqDTO") BugListReqDTO bugListReqDTO);

    /**
     * 线上bug详情
     * @author sch
     * @param id
     * @return
     */
    OnlineBugBO selectOnlineBugDetailById(@Param("id") Long id);

    /**
     * 更新线上bug
     * @author sch
     * @param bugManage
     * @return
     */
    int updateBugManager(OnlineBugManage bugManage);

    /**
     * 根据type查询bug数量
     * @author sch
     * @param type
     * @return
     */
    Integer selectCountByType(@Param("type") int type);

    /**
     * 查询总数
     * @return
     */
    Integer selectCountTotal();
}
