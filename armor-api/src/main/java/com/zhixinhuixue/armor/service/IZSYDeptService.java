package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.DeptReqDTO;
import com.zhixinhuixue.armor.model.dto.response.DeptResDTO;

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
     * 部门树结构
     * @return
     */
    DeptResDTO getDeptTree();

}
