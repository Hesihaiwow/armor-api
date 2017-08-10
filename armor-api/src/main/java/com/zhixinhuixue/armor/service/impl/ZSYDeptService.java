package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYDepartmentMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.DeptBo;
import com.zhixinhuixue.armor.model.dto.request.DeptReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptResDTO;
import com.zhixinhuixue.armor.model.pojo.Department;
import com.zhixinhuixue.armor.service.IZSYDeptService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //校验部门名称是否唯一
        List<Department> departments = departmentMapper.selectByDeptName(deptReqDTO.getName());
        if (departments.size()>0){
            throw new ZSYServiceException(String.format("部门(%s)已存在",deptReqDTO.getName()));
        }
        Department department = new Department();
        BeanUtils.copyProperties(deptReqDTO,department);
        department.setId(snowFlakeIDHelper.nextId());
        department.setCreateTime(new Date());
        department.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        departmentMapper.insertDept(department);
    }

    @Override
    public DeptResDTO getDeptTree() {
        DeptBo deptBo = departmentMapper.selectRootDept(ZSYConstants.DEFAULT_DEPT_ROOT_ID);
        DeptResDTO deptResDTO = new DeptResDTO();
        BeanUtils.copyProperties(deptBo,deptResDTO,"children");
        deptResDTO.setChildren(deepCopy(deptBo.getChildren()));
        return deptResDTO;
    }

    /**
     * 对象深度拷贝
     * @param children 对象中的集合
     * @return
     */
    private List<DeptResDTO> deepCopy(List<DeptBo> children){
        List<DeptResDTO> childDept = new ArrayList<>();
        children.stream().forEach(child->{
            DeptResDTO tmp = new DeptResDTO();
            BeanUtils.copyProperties(child,tmp);
            tmp.setChildren(deepCopy(child.getChildren()));
            childDept.add(tmp);
        });
        return childDept;
    }
}
