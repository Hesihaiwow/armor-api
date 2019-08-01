package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTempModuleResDTO;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/7/29 10:44
 */
public interface IZSYTaskTempModuleService {

    /**
     * 新增
     * @author sch
     * @param reqDTO
     */
    void add(ProjectReqDTO reqDTO);

    /**
     * 查看列表
     * @author sch
     * @return
     */
    List<TaskTempModuleResDTO> getList();

    /**
     * 修改
     * @author sch
     * @param reqDTO
     * @param id
     */
    void update(ProjectReqDTO reqDTO, Long id);

    /**
     * 删除
     * @author sch
     * @param id
     */
    void delete(Long id);
}
