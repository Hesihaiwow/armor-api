package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.BugManageBO;
import com.zhixinhuixue.armor.model.bo.BugManageListBO;
import com.zhixinhuixue.armor.model.bo.OnlineBugBO;
import com.zhixinhuixue.armor.model.bo.SystemBugTypeBO;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.pojo.BugManage;
import com.zhixinhuixue.armor.model.pojo.OnlineBugManage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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
    Integer selectCountByType(@Param("type") int type,@Param("bugListReqDTO")BugListReqDTO bugListReqDTO);

    /**
     * 查询总数
     * @return
     */
    Integer selectCountTotal();

    /**
     * 查询bug数据(老数据)
     * @author sch
     * @param bugListReqDTO
     * @return
     */
    Page<OnlineBugBO> selectOldOnlineBugPage(@Param("bugListReqDTO")BugListReqDTO bugListReqDTO);

    /**
     * 查询解决状态为空的数据
     * @author sch
     * @return
     */
    List<OnlineBugManage> selectIsSolvedIsNull();

    /**
     * 批量更新bug解决状态
     * @param list
     * @return
     */
    int updateStatusBatch(@Param("list") List<OnlineBugManage> list);

    /**
     * 查询最后一个bug编号
     * @author sch
     * @return
     */
    OnlineBugManage selectLastBugNo();

    /**
     * 根据主键查询
     * @author sch
     * @param id 主键
     * @return
     */
    OnlineBugManage selectById(@Param("id") Long id);

    /**
     * 查询时间段内线上bug的系统数量
     * @param startTime 开始时间
     * @param endTime 截止时间
     */
    List<Integer> selectSystemsByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据反馈系统和类型查询数量
     * @param systemId 系统id
     * @param type 类型
     */
    Integer selectNumBySystemAndType(@Param("systemId") Integer systemId, @Param("type") int type);

    /**
     * 查询时间段内线上bug各个系统对应的各个类型的数量
     * @param startTime 开始时间
     * @param endTime 截止时间
     */
    List<SystemBugTypeBO> selectSystemTypeNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
