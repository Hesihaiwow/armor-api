package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.DeptReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptLevelResDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptResDTO;

import java.util.List;

/**
 * Created by Akuma on 2017/8/9.
 */
public interface IZSYDeptService {

    /**
     * 添加部门
     * @param deptReqDTO 部门信息
     */
    void addDept(DeptReqDTO deptReqDTO);

    /**
     * 添加组织
     * @param name
     */
    void addOrganization(String name);

    /**
     * 部门树结构
     * @return
     */
    DeptResDTO getDeptTree();

    /**
     * 部门树结构
     * @return
     */
    List<DeptLevelResDTO> getDeptLevel();

    /**
     * 所有部门
     * @return
     */
    List<DeptResDTO> getAllDept();

    /**
     * 查询全部部门
     *
     * @return
     * @author sch
     */
    List<DeptResDTO> getDeptList();
}
