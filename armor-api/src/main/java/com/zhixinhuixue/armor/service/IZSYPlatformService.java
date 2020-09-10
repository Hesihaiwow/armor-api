package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.AddPlatformReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditPlatformReqDTO;
import com.zhixinhuixue.armor.model.dto.response.PlatformResDTO;

import java.util.List;

/**
 * @author sch
 * @DATE 2019/6/10 16:28
 */
public interface IZSYPlatformService {
    /**
     * 新增
     * author sch
     * @param reqDTO 参数
     */
    void add(AddPlatformReqDTO reqDTO);

    /**
     * 列表展示
     * @author sch
     * @return List<PlatformResDTO>
     */
    List<PlatformResDTO> list(String groupMark);

    /**
     * 删除
     * @param id 平台id
     */
    void delete(Long id);

    /**
     * 编辑
     * @param reqDTO 参数
     * @param id id
     */
    void editPlatform(EditPlatformReqDTO reqDTO, Long id);
}
