package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTaskFunctionMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYTestExampleMapper;
import com.zhixinhuixue.armor.dao.IZSYTestFunctionMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.TestExampleBO;
import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.AddTestFunctionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestFunctionReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExampleDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTreeResDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TestExample;
import com.zhixinhuixue.armor.model.pojo.TestFunction;
import com.zhixinhuixue.armor.service.IZSYTestExampleService;
import com.zhixinhuixue.armor.source.enums.TestExampleStatus;
import com.zhixinhuixue.armor.source.enums.TestExampleType;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author sch
 * @DATE 2019/8/6 9:42
 */
@Service
public class ZSYTestExampleService implements IZSYTestExampleService {
    @Autowired
    private IZSYTestExampleMapper exampleMapper;
    @Autowired
    private IZSYTestFunctionMapper testFunctionMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskFunctionMapper functionMapper;
    private static final Logger logger = LoggerFactory.getLogger(ZSYTestExampleService.class);


    /**
     * 新增
     *
     * @param reqDTO
     */
    @Override
    @Transactional
    public void add(AddTestExampleReqDTO reqDTO) {
        Task task = taskMapper.selectByPrimaryKey(reqDTO.getTaskId());
        if (task == null) {
            throw new ZSYServiceException("关联任务不存在,请检查");
        }
        List<Long> functionIds = testFunctionMapper.selectListByTask(reqDTO.getTaskId()).stream().map(TestFunction::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(functionIds)) {
            throw new ZSYServiceException("关联任务暂无功能点");
        } else {
            TestFunction function = testFunctionMapper.selectById(reqDTO.getFunctionId());
            if (function == null) {
                throw new ZSYServiceException("任务功能点不存在,请检查");
            }
            if (!functionIds.contains(function.getId())) {
                throw new ZSYServiceException("关联功能点不属于当前任务,请检查");
            }
        }

        TestExample testExample = new TestExample();
        testExample.setId(snowFlakeIDHelper.nextId());
        testExample.setName(reqDTO.getName().trim());
        testExample.setTaskId(reqDTO.getTaskId());
        testExample.setFunctionId(reqDTO.getFunctionId());
        if (reqDTO.getCheckPoint() != null) {
            testExample.setCheckPoint(reqDTO.getCheckPoint().trim());
        }
        if (reqDTO.getExpectResult() != null) {
            testExample.setExpectResult(reqDTO.getExpectResult().trim());
        }
        if (reqDTO.getRemark() != null) {
            testExample.setRemark(reqDTO.getRemark().trim());
        }
        testExample.setType(reqDTO.getType());
        testExample.setStatus(TestExampleStatus.NONE.getValue());
        testExample.setExamStatus(TestExampleStatus.NONE.getValue());
        testExample.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        testExample.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        testExample.setCreateName(ZSYTokenRequestContext.get().getUserName());
        testExample.setUpdateName(ZSYTokenRequestContext.get().getUserName());
        testExample.setCreateTime(new Date());
        testExample.setUpdateTime(new Date());

        if (exampleMapper.insert(testExample) == 0) {
            throw new ZSYServiceException("新增测试用例失败");
        }
    }

    /**
     * 查看树
     *
     * @param taskId
     * @return
     */
    @Override
    public TaskTreeResDTO getTree(Long taskId) {
        TaskTreeResDTO taskTreeResDTO = new TaskTreeResDTO();
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task != null) {
            taskTreeResDTO.setTaskId(taskId);
            taskTreeResDTO.setId(taskId);
            taskTreeResDTO.setTaskName(task.getName());
            taskTreeResDTO.setTreeName(task.getName());
            taskTreeResDTO.setLevel(1);
            taskTreeResDTO.setIsEdit(false);
            List<TestFunction> functions = testFunctionMapper.selectListByTask(taskId);
            List<TaskTreeResDTO.TestFunctionTreeResDTO> functionTreeResDTOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(functions)) {
                functions.forEach(function -> {
                    TaskTreeResDTO.TestFunctionTreeResDTO taskFunctionTreeResDTO = new TaskTreeResDTO.TestFunctionTreeResDTO();
                    taskFunctionTreeResDTO.setPid(taskId);
                    taskFunctionTreeResDTO.setFunctionId(function.getId());
                    taskFunctionTreeResDTO.setId(function.getId());
                    taskFunctionTreeResDTO.setFunction(function.getName());
                    taskFunctionTreeResDTO.setTreeName(function.getName());
                    taskFunctionTreeResDTO.setLevel(2);
                    taskFunctionTreeResDTO.setIsEdit(false);
                    List<TaskTreeResDTO.TestFunctionTreeResDTO.TestExampleTreeResDTO> testExampleTreeResDTOS = new ArrayList<>();
                    List<TestExample> examples = exampleMapper.selectByFunction(function.getId());
                    if (!CollectionUtils.isEmpty(examples)) {
                        examples.forEach(example -> {
                            TaskTreeResDTO.TestFunctionTreeResDTO.TestExampleTreeResDTO resDTO = new TaskTreeResDTO.TestFunctionTreeResDTO.TestExampleTreeResDTO();
                            resDTO.setPid(function.getId());
                            resDTO.setId(example.getId());
                            resDTO.setName(example.getName());
                            resDTO.setTreeName(example.getName());
                            resDTO.setLevel(3);
                            resDTO.setIsEdit(false);
                            testExampleTreeResDTOS.add(resDTO);
                        });
                    }
                    taskFunctionTreeResDTO.setFunctionTreeResDTOS(testExampleTreeResDTOS);
                    functionTreeResDTOS.add(taskFunctionTreeResDTO);
                });
            }
            taskTreeResDTO.setFunctionTreeResDTOS(functionTreeResDTOS);
        }
        return taskTreeResDTO;
    }

    /**
     * 查看测试用例详情
     *
     * @param exampleId
     * @return
     */
    @Override
    public ExampleDetailResDTO getExampleDetail(Long exampleId) {
        ExampleDetailResDTO resDTO = new ExampleDetailResDTO();
        TestExampleBO exampleBO = exampleMapper.selectDetailById(exampleId);
        if (exampleBO == null) {
            throw new ZSYServiceException("当前测试用例不存在,请检查");
        }
        BeanUtils.copyProperties(exampleBO, resDTO);
        resDTO.setTypeName(TestExampleType.getName(exampleBO.getType()));
        resDTO.setStatusName(TestExampleStatus.getName(exampleBO.getStatus()));
        resDTO.setExamStatusName(TestExampleStatus.getName(exampleBO.getExamStatus()));
        return resDTO;
    }

    /**
     * 删除
     *
     * @param exampleId
     */
    @Override
    @Transactional
    public void deleteExample(Long exampleId) {
        TestExample example = exampleMapper.selectById(exampleId);
        if (example == null) {
            throw new ZSYServiceException("当前测试用例不存在,请检查");
        }
        exampleMapper.deleteById(exampleId);
    }

    /**
     * 编辑
     *
     * @param reqDTO
     */
    @Override
    @Transactional
    public void editExample(EditTestExampleReqDTO reqDTO) {
        TestExample example = exampleMapper.selectById(reqDTO.getExampleId());
        if (example == null) {
            throw new ZSYServiceException("当前测试用例不存在,请检查");
        }
        BeanUtils.copyProperties(reqDTO, example);
        example.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        example.setUpdateName(ZSYTokenRequestContext.get().getUserName());
        example.setUpdateTime(new Date());
        if (exampleMapper.update(example) == 0) {
            throw new ZSYServiceException("更新测试用例失败");
        }
    }

    /**
     * 导入
     *
     * @param uploadFile
     * @param taskId
     */
    @Override
    @Transactional
    public void importExample(MultipartFile uploadFile, Long taskId) {
        long time1 = System.currentTimeMillis();
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isExcel(suffix)) {
            throw new ZSYServiceException("只能上传Excel");
        }
        Workbook book = null;
        try {
            File file = multipartFileToFile(uploadFile);
            String fileDir = file.getAbsolutePath();
            book = getExcelWorkbook(fileDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sheet sheet = getSheetByNum(book, 0);
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum < 1) {
            throw new ZSYServiceException("暂无数据导入,请检查");
        }

        //导入数据各个列的值的list  用来判断导入数据是否重复
        List<List<String>> fieldList = new ArrayList<>();
        Row firstRow = sheet.getRow(0);
        int firstCellNum = firstRow.getLastCellNum();
        for (int i = 1; i <= lastRowNum; i++) {
            List<String> fields = new ArrayList<>();
            Row row;
            row = sheet.getRow(i);
            if (row != null) {
                Cell cell;
                for (int j = 0; j < firstCellNum; j++) {
                    cell = row.getCell(j);
                    if (cell != null) {
                        String cellValue = cell.getStringCellValue();
                        fields.add(cellValue);
                    } else {
                        fields.add(" ");
                    }
                }
            }
            fieldList.add(fields);
        }
        long time2 = System.currentTimeMillis();
        logger.info("解析Excel耗时: {}ms", (time2 - time1));
        List<TestExample> exampleList = new ArrayList<>();
        List<TestFunction> functionList = new ArrayList<>();
        Set<String> functionNameSet = new HashSet<>();
        Map<String, Long> functionMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(fieldList)) {
            for (int i = 0; i < fieldList.size(); i++) {

                if (fieldList.get(i).get(0) == null || "".equals(fieldList.get(i).get(0))) {
                    throw new ZSYServiceException("第" + (i + 2) + "行的<功能点>为空,请检查");
                }
                if (fieldList.get(i).get(1) == null || "".equals(fieldList.get(i).get(1))) {
                    throw new ZSYServiceException("第" + (i + 2) + "行的<用例名称>为空,请检查");
                }
                functionNameSet.add(fieldList.get(i).get(0).trim());

            }
            for (String functionName : functionNameSet) {
                TestFunction function = new TestFunction();
                function.setId(snowFlakeIDHelper.nextId());
                function.setName(functionName);
                function.setTaskId(taskId);
                function.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                function.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
                function.setCreateName(ZSYTokenRequestContext.get().getUserName());
                function.setUpdateName(ZSYTokenRequestContext.get().getUserName());
                function.setCreateTime(new Date());
                function.setUpdateTime(new Date());
                functionList.add(function);

                functionMap.put(functionName, function.getId());
            }
            for (int i = 0; i < fieldList.size(); i++) {

                TestExample example = new TestExample();
                example.setId(snowFlakeIDHelper.nextId());
                example.setName(fieldList.get(i).get(1).trim());
                example.setTaskId(taskId);
                Long functionId = functionMap.get(fieldList.get(i).get(0).trim());
                example.setFunctionId(functionId);
                example.setCheckPoint(fieldList.get(i).get(3).trim());
                example.setExpectResult(fieldList.get(i).get(4).trim());
                example.setRemark(fieldList.get(i).get(5).trim());
                example.setType(TestExampleType.NONE.getValue());
                if (fieldList.get(i).get(2).trim().equals("正例")) {
                    example.setType(TestExampleType.NORMAL.getValue());
                } else if (fieldList.get(i).get(2).trim().equals("反例")) {
                    example.setType(TestExampleType.NOT_NORMAL.getValue());
                }
                example.setStatus(TestExampleStatus.NONE.getValue());
                example.setExamStatus(TestExampleStatus.NONE.getValue());
                example.setCreateTime(new Date());
                example.setUpdateTime(new Date());
                example.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                example.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
                example.setCreateName(ZSYTokenRequestContext.get().getUserName());
                example.setUpdateName(ZSYTokenRequestContext.get().getUserName());
                exampleList.add(example);
            }

            long time3 = System.currentTimeMillis();
            logger.info("准备数据耗时: {}ms", (time3 - time2));
            testFunctionMapper.insertBatch(functionList);
            exampleMapper.insertBatch(exampleList);
            logger.info("插入数据耗时: {}ms", (System.currentTimeMillis() - time3));
        }


    }

    /**
     * 导出
     *
     * @param taskId
     * @return
     */
    @Override
    public String exportExample(Long taskId) {
        List<TestExampleBO> exampleBOS = exampleMapper.selectListByTaskId(taskId);
        List<ExampleDetailResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(exampleBOS)) {
            exampleBOS.forEach(exampleBO -> {
                ExampleDetailResDTO resDTO = new ExampleDetailResDTO();
                BeanUtils.copyProperties(exampleBO, resDTO);

                if (exampleBO.getType() == null) {
                    resDTO.setTypeName("暂无");
                } else if (exampleBO.getType() == 1) {
                    resDTO.setTypeName("异常用例");
                } else if (exampleBO.getType() == 0) {
                    resDTO.setTypeName("正常用例");
                }

                if (exampleBO.getStatus() == null) {
                    resDTO.setStatusName("暂无");
                } else if (exampleBO.getStatus() == 0) {
                    resDTO.setStatusName("通过");
                } else if (exampleBO.getStatus() == 1) {
                    resDTO.setStatusName("失败");
                } else if (exampleBO.getStatus() == 2) {
                    resDTO.setStatusName("阻塞");
                }

                if (exampleBO.getExamStatus() == null) {
                    resDTO.setExamStatusName("暂无");
                } else if (exampleBO.getExamStatus() == 0) {
                    resDTO.setExamStatusName("评审通过");
                } else if (exampleBO.getExamStatus() == 1) {
                    resDTO.setExamStatusName("评审失败");
                }

                list.add(resDTO);
            });
        }
        return null;
    }

    @Override
    @Transactional
    public void addFunction(AddTestFunctionReqDTO reqDTO) {
        TestFunction testFunction = new TestFunction();
        testFunction.setId(snowFlakeIDHelper.nextId());
        testFunction.setTaskId(reqDTO.getTaskId());
        testFunction.setName(reqDTO.getName().trim());
        testFunction.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        testFunction.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        testFunction.setCreateName(ZSYTokenRequestContext.get().getUserName());
        testFunction.setUpdateName(ZSYTokenRequestContext.get().getUserName());
        testFunction.setCreateTime(new Date());
        testFunction.setUpdateTime(new Date());
        if (testFunctionMapper.insert(testFunction) == 0) {
            throw new ZSYServiceException("新增功能点失败");
        }
    }

    /**
     * 修改审批状态
     *
     * @param exampleId
     * @param examStatus
     */
    @Override
    @Transactional
    public void editExamStatus(Long exampleId, Integer examStatus) {
        TestExample example = exampleMapper.selectById(exampleId);
        if (example == null) {
            throw new ZSYServiceException("当前测试用例不存在,请检查");
        }
        example.setExamStatus(examStatus);
        example.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        example.setUpdateName(ZSYTokenRequestContext.get().getUserName());
        example.setUpdateTime(new Date());
        if (exampleMapper.update(example) == 0) {
            throw new ZSYServiceException("更新测试用例审批状态失败");
        }
    }

    /**
     * 修改状态
     *
     * @param exampleId
     * @param status
     */
    @Override
    public void editStatus(Long exampleId, Integer status) {
        TestExample example = exampleMapper.selectById(exampleId);
        if (example == null) {
            throw new ZSYServiceException("当前测试用例不存在,请检查");
        }
        example.setStatus(status);
        example.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        example.setUpdateName(ZSYTokenRequestContext.get().getUserName());
        example.setUpdateTime(new Date());
        if (exampleMapper.update(example) == 0) {
            throw new ZSYServiceException("更新测试用例状态失败");
        }
    }

    /**
     * 修改功能点名称
     *
     * @param reqDTO
     */
    @Override
    @Transactional
    public void editFunction(EditTestFunctionReqDTO reqDTO) {
        TestFunction function = testFunctionMapper.selectById(reqDTO.getId());
        if (function == null) {
            throw new ZSYServiceException("当前功能点不存在,请检查");
        }
        function.setName(reqDTO.getName().trim());
        function.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        function.setUpdateName(ZSYTokenRequestContext.get().getUserName());
        function.setUpdateTime(new Date());
        if (testFunctionMapper.update(function) == 0) {
            throw new ZSYServiceException("修改功能点失败");

        }
    }

    /**
     * 删除功能点
     *
     * @param functionId
     */
    @Override
    @Transactional
    public void deleteFunction(Long functionId) {
        TestFunction function = testFunctionMapper.selectById(functionId);
        if (function == null) {
            throw new ZSYServiceException("当前功能点不存在,请检查");
        }
        List<TestExample> examples = exampleMapper.selectByFunction(functionId);
        if (!CollectionUtils.isEmpty(examples)) {
            throw new ZSYServiceException("当前功能点下存在测试用例,请先删除测试用例后,再删除功能点");
        }
        testFunctionMapper.deleteById(functionId);
    }

    /**
     * 获取上传文件后缀名
     */
    private String getUploadSuffix(String uploadName) {
        return uploadName.substring(uploadName.lastIndexOf(".") + 1);
    }

    //判断是否是excel
    private static boolean isExcel(String url) {
        Pattern p = Pattern.compile("\\.(xls|XLS)");
        Matcher m = p.matcher(url);
        return m.find();
    }

    /**
     * MultipartFile 转 File
     */
    private static File multipartFileToFile(@RequestParam MultipartFile file) throws Exception {

        File toFile = null;
        if (file != null) {
            InputStream ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try (OutputStream os = new FileOutputStream(file)) {
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Sheet getSheetByNum(Workbook book, int number) {
        Sheet sheet;
        try {
            sheet = book.getSheetAt(number);
        } catch (Exception e) {
            throw new ZSYServiceException(e.getMessage());
        }
        return sheet;
    }

    private static Workbook getExcelWorkbook(String filePath) throws IOException {
        Workbook book;
        File file;
        file = new File(filePath);
        if (!file.exists()) {
            throw new ZSYServiceException("文件不存在");
        }
        try (FileInputStream fis = new FileInputStream(file);) {
            book = WorkbookFactory.create(fis);
        } catch (Exception e) {
            throw new ZSYServiceException(e.getMessage());
        }
        return book;
    }
}
