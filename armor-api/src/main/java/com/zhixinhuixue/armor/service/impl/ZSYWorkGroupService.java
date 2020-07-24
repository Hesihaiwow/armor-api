package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYUserGroupMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.dao.IZSYWorkGroupMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.WorkGroupBO;
import com.zhixinhuixue.armor.model.dto.request.AddGroupUserReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddWorkGroupReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditWorkGroupReqDTO;
import com.zhixinhuixue.armor.model.dto.response.EffectUserResDTO;
import com.zhixinhuixue.armor.model.dto.response.WorkGroupDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.WorkGroupListResDTO;
import com.zhixinhuixue.armor.model.dto.response.WorkGroupTreeResDTO;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.UserGroup;
import com.zhixinhuixue.armor.model.pojo.WorkGroup;
import com.zhixinhuixue.armor.service.IZSYWorkGroupService;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sch
 * @time 2020/1/8 9:35
 */
@Service
public class ZSYWorkGroupService implements IZSYWorkGroupService {

    @Autowired
    private IZSYWorkGroupMapper groupMapper;
    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYUserGroupMapper userGroupMapper;


    /**
     * 新增团队
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    @Transactional
    public void addGroup(AddWorkGroupReqDTO reqDTO) {
        //根据名称查询,确保没有重名
        WorkGroup existGroup = groupMapper.selectByName(reqDTO.getName().trim());
        if (existGroup != null){
            throw new ZSYServiceException("当前团队名已存在,请修改名称");
        }
        User user = userMapper.selectById(reqDTO.getLeader());
        if (user == null){
            throw new ZSYServiceException("设置的团队负责人不存在,请检查");
        }
        if (user.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException("设置的团队负责人已删除,请检查");
        }
        if (user.getStatus() != ZSYUserStatus.NORMAL.getValue()){
            throw new ZSYServiceException("设置的团队负责人已冻结,请检查");
        }
        WorkGroup workGroup = new WorkGroup();
        workGroup.setId(snowFlakeIDHelper.nextId());
        workGroup.setName(reqDTO.getName().trim());
        workGroup.setDescription(reqDTO.getDescription().trim());
        workGroup.setLeader(reqDTO.getLeader());
        workGroup.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        workGroup.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        workGroup.setCreateTime(new Date());
        workGroup.setUpdateTime(new Date());
        workGroup.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());

        if (groupMapper.insertGroup(workGroup) == 0){
            throw new ZSYServiceException("新增团队失败");
        }

        UserGroup userGroup = new UserGroup();
        userGroup.setUgId(snowFlakeIDHelper.nextId());
        userGroup.setGroupId(workGroup.getId());
        userGroup.setUserId(reqDTO.getLeader());
        userGroupMapper.insert(userGroup);
    }

    /**
     * 编辑团队
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    @Transactional
    public void updateGroup(EditWorkGroupReqDTO reqDTO) {
        WorkGroup workGroup = groupMapper.selectById(reqDTO.getId());
        if (workGroup == null){
            throw new ZSYServiceException("当前团队不存在,请检查");
        }
        if (workGroup.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException("当前团队已删除,请检查");
        }
        User user = userMapper.selectById(reqDTO.getLeader());
        if (user == null){
            throw new ZSYServiceException("设置的团队负责人不存在,请检查");
        }
        if (user.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException("设置的团队负责人已删除,请检查");
        }
        if (user.getStatus() != ZSYUserStatus.NORMAL.getValue()){
            throw new ZSYServiceException("设置的团队负责人已冻结,请检查");
        }
        Long oldLeader = workGroup.getLeader();
        workGroup.setName(reqDTO.getName().trim());
        workGroup.setDescription(reqDTO.getDescription().trim());
        workGroup.setLeader(reqDTO.getLeader());
        workGroup.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        workGroup.setUpdateTime(new Date());

        if (groupMapper.updateById(workGroup) == 0){
            throw new ZSYServiceException("更新团队失败");
        }

        if (!reqDTO.getLeader().equals(oldLeader)){
            UserGroup userGroup = new UserGroup();
            userGroup.setUgId(snowFlakeIDHelper.nextId());
            userGroup.setGroupId(workGroup.getId());
            userGroup.setUserId(reqDTO.getLeader());
            userGroupMapper.insert(userGroup);
        }

    }

    /**
     * 删除团队
     * @author sch
     * @param id id
     */
    @Override
    @Transactional
    public void deleteGroup(Long id) {
        WorkGroup workGroup = groupMapper.selectById(id);
        if (workGroup == null){
            throw new ZSYServiceException("当前团队不存在,请检查");
        }
        if (workGroup.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException("当前团队已删除,请检查");
        }
        //查询是否有用户在当前团队中
        List<User> users = userMapper.selectUsersByGroup(id);
        if (!CollectionUtils.isEmpty(users)){
            throw new ZSYServiceException("当前团队内有用户存在,请先调整用户团队,再进行删除");
        }
        workGroup.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        workGroup.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        workGroup.setUpdateTime(new Date());

        if (groupMapper.updateById(workGroup) == 0){
            throw new ZSYServiceException("删除团队失败");
        }
    }

    /**
     * 查询团队列表
     * @author sch
     */
    @Override
    public List<WorkGroupListResDTO> getList() {
        List<WorkGroup> groups = groupMapper.selectList();
        List<WorkGroupListResDTO> resDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groups)){
            groups.forEach(group->{
                WorkGroupListResDTO resDTO = new WorkGroupListResDTO();
                resDTO.setId(group.getId());
                resDTO.setName(group.getName());
                resDTOList.add(resDTO);
            });
        }
        return resDTOList;
    }

    /**
     * 查询团队详情
     * @author sch
     * @param id id
     */
    @Override
    public WorkGroupDetailResDTO getGroupDetail(Long id) {
        WorkGroupBO workGroupBO = groupMapper.selectDetailById(id);
        if (workGroupBO == null){
            throw new ZSYServiceException("当前团队不存在,请检查");
        }
        WorkGroupDetailResDTO resDTO = new WorkGroupDetailResDTO();
        BeanUtils.copyProperties(workGroupBO,resDTO);
        return resDTO;
    }

    /**
     * 树结构
     * @author sch
     */
    @Override
    public WorkGroupTreeResDTO getTree() {
        List<WorkGroup> workGroups = groupMapper.selectList();
        WorkGroupTreeResDTO resDTO = new WorkGroupTreeResDTO();
        resDTO.setId(1L);
        resDTO.setLabel("团队");
        List<WorkGroupTreeResDTO> children = new ArrayList<>();
        if (!CollectionUtils.isEmpty(workGroups)){
            workGroups.forEach(workGroup -> {
                WorkGroupTreeResDTO dto = new WorkGroupTreeResDTO();
                dto.setId(workGroup.getId());
                dto.setLabel(workGroup.getName());
                dto.setChildren(new ArrayList<>());
                children.add(dto);
            });
        }
        resDTO.setChildren(children);
        return resDTO;
    }

    /**
     * 添加成员
     * @author sch
     * @param reqDTO 参数
     */
    @Override
    @Transactional
    public void addGroupUsers(AddGroupUserReqDTO reqDTO) {
        WorkGroup workGroup = groupMapper.selectById(reqDTO.getGroupId());
        if (workGroup == null){
            throw new ZSYServiceException("当前团队不存在,请检查");
        }
        if (workGroup.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException("当前团队已删除,请检查");
        }

        List<Long> userIds = reqDTO.getUserIds();
        //删除原来的团队_成员关系
        userGroupMapper.deleteByGroup(reqDTO.getGroupId());
        if (!CollectionUtils.isEmpty(userIds)){
            if (!userIds.contains(workGroup.getLeader())){
                userIds.add(workGroup.getLeader());
            }
            List<UserGroup> collect = userIds.stream().map(userId -> {
                UserGroup userGroup = new UserGroup();
                userGroup.setUgId(snowFlakeIDHelper.nextId());
                userGroup.setGroupId(reqDTO.getGroupId());
                userGroup.setUserId(userId);
                return userGroup;
            }).collect(Collectors.toList());
            userGroupMapper.insertBatch(collect);
        }else {
            UserGroup userGroup = new UserGroup();
            userGroup.setUgId(snowFlakeIDHelper.nextId());
            userGroup.setGroupId(reqDTO.getGroupId());
            userGroup.setUserId(workGroup.getLeader());
            userGroupMapper.insert(userGroup);
        }
    }

    /**
     * 查询团队成员
     * @param groupId 团队id
     * @return List<EffectUserResDTO>
     */
    @Override
    public List<EffectUserResDTO> getGroupUsers(Long groupId) {
        WorkGroup workGroup = groupMapper.selectById(groupId);
        if (workGroup == null){
            throw new ZSYServiceException("当前团队不存在,请检查");
        }
        if (workGroup.getIsDelete() == ZSYDeleteStatus.DELETED.getValue()){
            throw new ZSYServiceException("当前团队已删除,请检查");
        }
        List<User> users = userMapper.selectUsersByGroup(groupId);
        List<EffectUserResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(users)){
            list = users.stream().map(user -> {
                EffectUserResDTO resDTO = new EffectUserResDTO();
                BeanUtils.copyProperties(user,resDTO);
                return resDTO;
            }).collect(Collectors.toList());
        }
        return list;
    }
}
