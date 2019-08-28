package com.zhixinhuixue.armor.dao;

import com.zhixinhuixue.armor.model.bo.DeptBo;
import com.zhixinhuixue.armor.model.dto.request.QueryUserPageReqDTO;
import com.zhixinhuixue.armor.model.pojo.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IZSYDepartmentMapper {

    /**
     * 添加部门
     * @param dept 部门信息
     */
    void insertDept(Department dept);

    /**
     * 查询部门(校验部门名称)
     * @param deptName 部门名称
     * @return
     */
    List<Department> selectByDeptName(@Param("deptName") String deptName,@Param("id") Long id);

    /**
     * 查询部门树结构
     * @param pDeptId 父级部门ID
     * @return
     */
    DeptBo selectRootDept(@Param("pDeptId") Long pDeptId);

    /**
     * 查询部门
     * @param id 部门ID
     * @return
     */
    Department selectById(@Param("id") Long id);

    int updateByPrimaryKeySelective(Department record);

    /**
     * 查询所有部门
     * @return
     */
    List<DeptBo> getAllDept();

}