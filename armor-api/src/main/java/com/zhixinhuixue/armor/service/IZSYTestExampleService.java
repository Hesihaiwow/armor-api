package com.zhixinhuixue.armor.service;

import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddTestFunctionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestFunctionReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExampleDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTreeResDTO;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 新增功能点
     * @param reqDTO
     */
    void addFunction(AddTestFunctionReqDTO reqDTO);

    /**
     * 修改审批状态
     * @param exampleId
     * @param examStatus
     */
    void editExamStatus(Long exampleId, Integer examStatus);

    /**
     * 修改状态
     * @param exampleId
     * @param status
     */
    void editStatus(Long exampleId, Integer status);

    /**
     * 修改功能点名称
     * @param reqDTO
     */
    void editFunction(EditTestFunctionReqDTO reqDTO);

    /**
     * 删除功能点
     * @param functionId
     */
    void deleteFunction(Long functionId);
}
