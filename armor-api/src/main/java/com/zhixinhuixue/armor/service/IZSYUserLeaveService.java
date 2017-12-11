package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveListReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserLeaveReqDTO;
import com.zhixinhuixue.armor.model.dto.response.UserLeaveResDTO;

import java.util.List;

/**
 * Created by Lang on 2017/12/4 0004.
 */
public interface IZSYUserLeaveService {

    /**
     * 添加请假记录
     * @param userLeaveReqDTO
     */
    void add(UserLeaveReqDTO userLeaveReqDTO);

    UserLeaveResDTO getUserLeaveDetail(Long id);

    PageInfo<UserLeaveResDTO> getLeaveList(UserLeaveListReqDTO userLeaveListReqDTO);

    PageInfo<UserLeaveResDTO> getUserLeaveList(Integer status, Integer pageIndex);

    void updateLeaveDetail(UserLeaveReqDTO userLeaveReqDTO,Long id);

    void deleteLeaveDetail(Long id);

    void passLeave(Long id);


}
