package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExampleDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTreeResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * 查看树
     * @param taskId
     * @return
     */
    TaskTreeResDTO getTree(Long taskId);

    /**
     * 查看测试用例详情
     * @param exampleId
     * @return
     */
    ExampleDetailResDTO getExampleDetail(Long exampleId);

    /**
     * 删除
     * @param exampleId
     */
    void deleteExample(Long exampleId);

    /**
     * 编辑
     * @param reqDTO
     */
    void editExample(EditTestExampleReqDTO reqDTO);

    /**
     * 导入
     * @param uploadFile
     * @param taskId
     */
    void importExample(MultipartFile uploadFile, Long taskId);

    /**
     * 导出
     * @return
     */
    String exportExample(Long taskId);
}
