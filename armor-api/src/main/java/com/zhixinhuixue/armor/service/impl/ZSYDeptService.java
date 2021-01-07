package com.zhixinhuixue.armor.service.impl;

import com.google.common.collect.Lists;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYDepartmentMapper;
import com.zhixinhuixue.armor.exception.ZSYAuthException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.DeptBo;
import com.zhixinhuixue.armor.model.dto.request.DeptReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptLevelResDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptResDTO;
import com.zhixinhuixue.armor.model.pojo.Department;
import com.zhixinhuixue.armor.service.IZSYDeptService;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Akuma on 2017/8/9.
 */
@Service
public class ZSYDeptService implements IZSYDeptService {

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    @Autowired
    private IZSYDepartmentMapper departmentMapper;

    @Override
    public void addDept(DeptReqDTO deptReqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole()> ZSYUserRole.PROJECT_MANAGER.getValue()){
            throw new ZSYAuthException("没有权限执行此操作");
        }
        //校验部门名称是否唯一
        List<Department> departments = departmentMapper.selectByDeptName(deptReqDTO.getName(),ZSYTokenRequestContext.get().getDepartmentId(),ZSYTokenRequestContext.get().getOrgId());
        if (!CollectionUtils.isEmpty(departments)){
            throw new ZSYServiceException(String.format("部门(%s)已存在",deptReqDTO.getName()));
        }
        Department department = new Department();
        BeanUtils.copyProperties(deptReqDTO,department);
        department.setId(snowFlakeIDHelper.nextId());
        department.setCreateTime(new Date());
        department.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        department.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        departmentMapper.insertDept(department);
    }

    @Override
    public void addOrganization(String name){
        //校验部门名称是否唯一
        List<Department> departments = departmentMapper.selectByDeptName(name,null,ZSYTokenRequestContext.get().getOrgId());
        if (!CollectionUtils.isEmpty(departments)){
            throw new ZSYServiceException(String.format("组织(%s)已存在",name));
        }
        Department department = new Department();
        department.setName(name);
        department.setId(snowFlakeIDHelper.nextId());
        department.setParentId(department.getId());
        department.setCreateTime(new Date());
        department.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        department.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        departmentMapper.insertDept(department);
    }

    @Override
    public DeptResDTO getDeptTree() {
        DeptBo deptBo = departmentMapper.selectRootDept(ZSYTokenRequestContext.get().getDepartmentId());
        DeptResDTO deptResDTO = new DeptResDTO();
        BeanUtils.copyProperties(deptBo,deptResDTO,"children");
        deptResDTO.setLabel(deptBo.getName());
        deptResDTO.setChildren(deepCopy(deptBo.getChildren()));
        return deptResDTO;
    }

    @Override
    public List<DeptResDTO> getAllDept() {
        List<DeptBo> deptBos = departmentMapper.getAllDept(ZSYTokenRequestContext.get().getOrgId());
        List<DeptResDTO> deptResDTOS =  Lists.newArrayList();
        deptBos.forEach(deptBo->{
                DeptResDTO deptResDTO = new DeptResDTO();
                BeanUtils.copyProperties(deptBo,deptResDTO);
                deptResDTO.setLabel(deptBo.getName());
                deptResDTO.setChildren(deepCopy(deptBo.getChildren()));
                deptResDTOS.add(deptResDTO);
            }
        );
        return deptResDTOS;
    }

    /**
     * 查询全部部门
     *
     * @return
     * @author sch
     */
    @Override
    public List<DeptResDTO> getDeptList() {
        List<DeptResDTO> list = new ArrayList<>();
        List<Department> departmentList = departmentMapper.selectAll(ZSYTokenRequestContext.get().getOrgId());
        if (!CollectionUtils.isEmpty(departmentList)){
            departmentList.forEach(item->{
                DeptResDTO resDTO = new DeptResDTO();
                resDTO.setId(item.getId());
                resDTO.setLabel(item.getName());
                list.add(resDTO);
            });
        }
        return list;
    }

    @Override
    public List<DeptLevelResDTO> getDeptLevel() {
        List<DeptLevelResDTO> deptLevelResDTOS = Lists.newArrayList();
        DeptBo deptBo = departmentMapper.selectRootDept(ZSYTokenRequestContext.get().getDepartmentId());
        DeptLevelResDTO deptLevelResDTO = new DeptLevelResDTO();
        BeanUtils.copyProperties(deptBo,deptLevelResDTO,"children");
        deptLevelResDTO.setLabel(deptBo.getName());
        deptLevelResDTOS.add(deptLevelResDTO);
        deptLevelResDTOS.addAll(deepLevelCopy(1,deptBo.getChildren()));
        return deptLevelResDTOS;
    }

    /**
     * 对象深度拷贝
     * @param children 对象中的集合
     * @return
     */
    private List<DeptResDTO> deepCopy(List<DeptBo> children){
        List<DeptResDTO> childDept = new ArrayList<>();
        children.forEach(child->{
            DeptResDTO tmp = new DeptResDTO();
            BeanUtils.copyProperties(child,tmp);
            tmp.setLabel(child.getName());
            tmp.setChildren(deepCopy(child.getChildren()));
            childDept.add(tmp);
        });
        return childDept;
    }

    /**
     * 对象深度拷贝
     * @param children 对象中的集合
     * @return
     */
    private List<DeptLevelResDTO> deepLevelCopy(int level,List<DeptBo> children){
        List<DeptLevelResDTO> childDept = new ArrayList<>();
        children.forEach(child->{
            DeptLevelResDTO tmp = new DeptLevelResDTO();
            tmp.setId(child.getId());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < level; i++) {
                sb.append("-");
            }
            sb.append(child.getName());
            tmp.setLabel(sb.toString());
            childDept.add(tmp);
            childDept.addAll(deepLevelCopy(level+1,child.getChildren()));
        });
        return childDept;
    }
}
