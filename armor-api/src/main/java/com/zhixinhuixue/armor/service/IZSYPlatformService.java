package com.zhixinhuixue.armor.service;

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
     * @param name
     */
    void add(String name);

    /**
     * 列表展示
     * @author  sch
     * @return
     */
    List<PlatformResDTO> list();

    /**
     * 删除
     * @param id 平台id
     */
    void delete(Long id);
}
