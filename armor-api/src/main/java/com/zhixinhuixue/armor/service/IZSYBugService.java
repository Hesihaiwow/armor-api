package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugReqDTO;
import com.zhixinhuixue.armor.model.dto.response.BugDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.BugPageResDTO;
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
}
