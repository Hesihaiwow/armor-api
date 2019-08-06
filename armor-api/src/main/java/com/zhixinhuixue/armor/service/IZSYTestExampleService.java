package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;

/**
 * @author sch
 * @DATE 2019/8/6 9:42
 */
public interface IZSYTestExampleService {

    /**
     * 新增测试用例
     * @param reqDTO
     */
    void add(AddTestExampleReqDTO reqDTO);
}
