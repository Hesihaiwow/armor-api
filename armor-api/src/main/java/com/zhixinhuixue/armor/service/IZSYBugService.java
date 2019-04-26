package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugManageAddReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.source.ZSYResult;

/**
 * Created by Lang on 2017/9/4 0004.
 */
public interface IZSYBugService {

    /**
     * bug处理表
     *
     * @return
     */
    PageInfo<BugPageResDTO> getBugList(BugListReqDTO bugListReqDTO);


    /**
     * 添加bug处理
     * @param bugReqDTO
     */
    void addBug(BugReqDTO bugReqDTO);

    /**
     * 更新Bug处理
     * @param bugReqDTO
     */
    void updateBug(Long id ,BugReqDTO bugReqDTO);

    /**
     * bug处理详情
     * @param id
     * @return
     */
    ZSYResult<BugDetailResDTO> getBugDetail(Long id);

    /**
     * 删除Bug处理记录
     * @param id
     */
    void deleteBug(Long id);

    /**
     * 添加新bug
     * @author sch
     * @param reqDTO
     */
    void addNewBug(BugManageAddReqDTO reqDTO);

    /**
     * 分页查询线上bug
     * @author sch
     * @param bugListReqDTO
     * @return
     */
    PageInfo<OnlineBugResDTO> getBugManagePage(BugListReqDTO bugListReqDTO);

    /**
     * bug处理详情
     * @author sch
     * @param id
     * @return
     */
    OnlineBugDetailResDTO getOnlineBugDetail(Long id);

    /**
     * 更新bug
     * @author sch
     * @param reqDTO
     * @param id
     */
    void updateOnlineBug(BugManageAddReqDTO reqDTO, Long id);

    /**
     * 查询各个分类的线上bug数量
     * @author sch
     * @return
     */
    OnlineBugNumResDTO getDiffTypeBugNum(BugListReqDTO bugListReqDTO);
}
