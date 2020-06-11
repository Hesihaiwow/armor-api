package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.AddGroupUserReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddWorkGroupReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditWorkGroupReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.dto.response.WorkGroupDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.WorkGroupListResDTO;
import com.zhixinhuixue.armor.model.dto.response.WorkGroupTreeResDTO;

import java.util.List;

/**
 * @author sch
 * @time 2020/1/8 9:35
 */
public interface IZSYWorkGroupService {

    /**
     * 新增团队
     * @author sch
     * @param reqDTO 参数
     */
    void addGroup(AddWorkGroupReqDTO reqDTO);

    /**
     * 编辑团队
     * @author sch
     * @param reqDTO 参数
     */
    void updateGroup(EditWorkGroupReqDTO reqDTO);

    /**
     * 删除团队
     * @author sch
     * @param id id
     */
    void deleteGroup(Long id);

    /**
     * 查询团队列表
     * @author sch
     */
    List<WorkGroupListResDTO> getList();

    /**
     * 查询团队详情
     * @author sch
     * @param id id
     */
    WorkGroupDetailResDTO getGroupDetail(Long id);

    /**
     * 树结构
     * @author sch
     */
    WorkGroupTreeResDTO getTree();

    /**
     * 添加成员
     * @author sch
     * @param reqDTO 参数
     */
    void addGroupUsers(AddGroupUserReqDTO reqDTO);

    /**
     * 查询团队成员
     * @param groupId 团队id
     * @return List<EffectUserResDTO>
     */
    List<EffectUserResDTO> getGroupUsers(Long groupId);
}
