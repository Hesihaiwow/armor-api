package com.zhixinhuixue.armor.service;


import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.BugListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.BugReqDTO;
import com.zhixinhuixue.armor.model.dto.response.BugPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskDetailResDTO;
import com.zhixinhuixue.armor.source.ZSYResult;

import java.util.List;

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
     * bug处理详情
     * @param id
     * @return
     */
    ZSYResult<BugReqDTO> getBugDetail(Long id);
}
