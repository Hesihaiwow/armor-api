package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTaskFunctionMapper;
import com.zhixinhuixue.armor.dao.IZSYTaskMapper;
import com.zhixinhuixue.armor.dao.IZSYTestExampleMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.TestExampleBO;
import com.zhixinhuixue.armor.model.dto.request.AddTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.request.EditTestExampleReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExampleDetailResDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTreeResDTO;
import com.zhixinhuixue.armor.model.pojo.Task;
import com.zhixinhuixue.armor.model.pojo.TaskFunction;
import com.zhixinhuixue.armor.model.pojo.TestExample;
import com.zhixinhuixue.armor.service.IZSYTestExampleService;
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
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYTaskMapper taskMapper;
    @Autowired
    private IZSYTaskFunctionMapper functionMapper;
    private static final Logger logger = LoggerFactory.getLogger(ZSYTestExampleService.class);



    /**
     * 新增
     * @param reqDTO
     */
    @Override
    @Transactional
    public void add(AddTestExampleReqDTO reqDTO) {
        TestExample example = exampleMapper.selectByNameAndTaskAndFunction(reqDTO.getName(),reqDTO.getTaskId(),reqDTO.getFunctionId());
//        if (example != null){
//            throw new ZSYServiceException("当前测试用例已存在");
//        }
        TestExample testExample = new TestExample();
        testExample.setId(snowFlakeIDHelper.nextId());
        testExample.setName(reqDTO.getName().trim());
        testExample.setTaskId(reqDTO.getTaskId());
        testExample.setFunctionId(reqDTO.getFunctionId());
        testExample.setCheckPoint(reqDTO.getCheckPoint().trim());
        testExample.setExpectResult(reqDTO.getExpectResult().trim());
        if (reqDTO.getRemark() != null){
            testExample.setRemark(reqDTO.getRemark().trim());
        }
        testExample.setType(reqDTO.getType());
        testExample.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        testExample.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
        testExample.setCreateName(ZSYTokenRequestContext.get().getUserName());
        testExample.setUpdateName(ZSYTokenRequestContext.get().getUserName());
        testExample.setCreateTime(new Date());
        testExample.setUpdateTime(new Date());

        if (exampleMapper.insert(testExample) == 0){
            throw new ZSYServiceException("新增测试用例失败");
        }
    }

    /**
     * 查看树
     * @param taskId
     * @return
     */
    @Override
    public TaskTreeResDTO getTree(Long taskId) {
        TaskTreeResDTO taskTreeResDTO = new TaskTreeResDTO();
        Task task = taskMapper.selectByPrimaryKey(taskId);
        if (task != null){
            taskTreeResDTO.setTaskId(taskId);
            taskTreeResDTO.setTaskName(task.getName());
            List<TaskFunction> functions = functionMapper.selectListByTaskId(taskId);
            List<TaskTreeResDTO.TaskFunctionTreeResDTO> functionTreeResDTOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(functions)){
                functions.forEach(function->{
                    TaskTreeResDTO.TaskFunctionTreeResDTO taskFunctionTreeResDTO = new TaskTreeResDTO.TaskFunctionTreeResDTO();
                    taskFunctionTreeResDTO.setFunctionId(function.getId());
                    taskFunctionTreeResDTO.setFunction(function.getFunction());
                    List<TaskTreeResDTO.TaskFunctionTreeResDTO.TestExampleTreeResDTO> testExampleTreeResDTOS = new ArrayList<>();
                    List<TestExample> examples = exampleMapper.selectByFunction(function.getId());
                    if (!CollectionUtils.isEmpty(examples)){
                        examples.forEach(example->{
                            TaskTreeResDTO.TaskFunctionTreeResDTO.TestExampleTreeResDTO resDTO = new TaskTreeResDTO.TaskFunctionTreeResDTO.TestExampleTreeResDTO();
                            resDTO.setId(example.getId());
                            resDTO.setName(example.getName());
                            testExampleTreeResDTOS.add(resDTO);
                        });
                    }
                    taskFunctionTreeResDTO.setExampleTreeResDTOS(testExampleTreeResDTOS);
                    functionTreeResDTOS.add(taskFunctionTreeResDTO);
                });
            }
            taskTreeResDTO.setFunctionTreeResDTOS(functionTreeResDTOS);
        }
        return taskTreeResDTO;
    }

    /**
     * 查看测试用例详情
     * @param exampleId
     * @return
     */
    @Override
    public ExampleDetailResDTO getExampleDetail(Long exampleId) {
        ExampleDetailResDTO resDTO = new ExampleDetailResDTO();
        TestExampleBO exampleBO = exampleMapper.selectDetailById(exampleId);
        if (exampleBO == null){
            throw new ZSYServiceException("当前测试用例不存在,请检查");
        }
        BeanUtils.copyProperties(exampleBO,resDTO);
        if (exampleBO.getType() == 0){
            resDTO.setTypeName("正常用例");
        }else if (exampleBO.getType() == 1){
            resDTO.setTypeName("异常用例");
        }

        if (exampleBO.getStatus() == null){
            resDTO.setStatusName("暂无");
        }else if (exampleBO.getStatus() == 0){
            resDTO.setStatusName("通过");
        }else if (exampleBO.getStatus() == 1){
            resDTO.setStatusName("失败");
        }else if (exampleBO.getStatus() == 2){
            resDTO.setStatusName("阻塞");
        }

        if (exampleBO.getExamStatus() == null){
            resDTO.setExamStatusName("暂无");
        }else if (exampleBO.getExamStatus() == 0){
            resDTO.setExamStatusName("评审通过");
        }else if (exampleBO.getExamStatus() == 1){
            resDTO.setExamStatusName("评审失败");
        }

        return resDTO;
    }

    /**
     * 删除
     * @param exampleId
     */
    @Override
    @Transactional
    public void deleteExample(Long exampleId) {
        TestExample example = exampleMapper.selectById(exampleId);
        if (example == null){
            throw new ZSYServiceException("当前测试用例不存在,请检查");
        }
        exampleMapper.deleteById(exampleId);
    }

    /**
     * 编辑
     * @param reqDTO
     */
    @Override
    @Transactional
    public void editExample(EditTestExampleReqDTO reqDTO) {
        TestExample example = exampleMapper.selectById(reqDTO.getId());
        if (example == null){
            throw new ZSYServiceException("当前测试用例不存在,请检查");
        }
        BeanUtils.copyProperties(reqDTO,example);
        if (exampleMapper.update(example) == 0){
            throw new ZSYServiceException("更新测试用例失败");
        }
    }

    /**
     * 导入
     * @param uploadFile
     * @param taskId
     */
    @Override
    @Transactional
    public void importExample(MultipartFile uploadFile, Long taskId) {
        long time1 = System.currentTimeMillis();
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isExcel(suffix)){
            throw new ZSYServiceException("只能上传Excel");
        }
        Workbook book = null;
        try {
            File file = multipartFileToFile(uploadFile);
            String file_dir = file.getAbsolutePath();
            book = getExcelWorkbook(file_dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sheet sheet = getSheetByNum(book,0);
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum < 1){
            throw new ZSYServiceException("暂无数据导入,请检查");
        }

        //导入数据各个列的值的list  用来判断导入数据是否重复
        List<List<String>> fieldList = new ArrayList<>();

        for(int i = 1 ; i <= lastRowNum ; i++) {
            List<String> fields = new ArrayList<>();
            Row row = null;
            row = sheet.getRow(i);
            if( row != null ){
                int lastCellNum = row.getLastCellNum();
                Cell cell = null;
                for( int j = 0 ; j < lastCellNum ; j++ ){
                    cell = row.getCell(j);
                    if( cell != null ){
//                        cell.setCellType(CellType.STRING);
                        String cellValue = cell.getStringCellValue();
                        fields.add(cellValue);
                    }else {
                        fields.add("");
//                        throw new ZSYServiceException("第"+i+"行,第"+j+"列数据为空,请检查");
                    }
                }
            }
            fieldList.add(fields);
        }
        long time2 = System.currentTimeMillis();
        logger.info("解析Excel耗时: "+(time2-time1)+"ms");
        List<TaskFunction> functions = functionMapper.selectListByTaskId(taskId);
        Map<String,Long> functionMap = new HashMap<>();
        if (CollectionUtils.isEmpty(functions)){
            throw new ZSYServiceException("当前任务暂无功能点,请检查");
        }
        functions.forEach(function->{
            functionMap.put(function.getFunction(),function.getId());
        });
        List<String> functionNames = functions.stream().map(TaskFunction::getFunction).collect(Collectors.toList());
        List<TestExample> exampleList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fieldList)){
            for (int i = 0;i<fieldList.size();i++){
                if (!functionNames.contains(fieldList.get(i).get(0).trim())){
                    throw new ZSYServiceException("第"+(i+2)+"行的<功能点>不属于当前任务的功能点,请检查");
                }
                if (fieldList.get(i).get(1) == null || fieldList.get(i).get(1) == ""){
                    throw new ZSYServiceException("第"+(i+2)+"行的<用例名称>为空,请检查");
                }
//                if (fieldList.get(i).get(2) == null || fieldList.get(i).get(2) == ""){
//                    throw new ZSYServiceException("第"+(i+2)+"行的<正反用例>为空,请检查");
//                }
                TestExample example = new TestExample();
                example.setId(snowFlakeIDHelper.nextId());
                example.setName(fieldList.get(i).get(1).trim());
                example.setTaskId(taskId);
                example.setFunctionId(functionMap.get(fieldList.get(i).get(0)));
                example.setCheckPoint(fieldList.get(i).get(3).trim());
                example.setExpectResult(fieldList.get(i).get(4).trim());
                example.setRemark(fieldList.get(i).get(5).trim());
                if (fieldList.get(i).get(2).trim().equals("正常")){
                    example.setType(0);
                }else if (fieldList.get(i).get(2).trim().equals("异常")){
                    example.setType(1);
                }
                example.setCreateTime(new Date());
                example.setUpdateTime(new Date());
                example.setCreateBy(ZSYTokenRequestContext.get().getUserId());
                example.setUpdateBy(ZSYTokenRequestContext.get().getUserId());
                example.setCreateName(ZSYTokenRequestContext.get().getUserName());
                example.setUpdateName(ZSYTokenRequestContext.get().getUserName());
                exampleList.add(example);
            }
            long time3 = System.currentTimeMillis();
            logger.info("准备数据耗时: "+(time3-time2)+"ms");
            exampleMapper.insertBatch(exampleList);
            logger.info("插入数据耗时: "+(System.currentTimeMillis()-time3)+"ms");
        }


    }

    /**
     * 导出
     * @param taskId
     * @return
     */
    @Override
    public String exportExample(Long taskId) {
        List<TestExampleBO> exampleBOS = exampleMapper.selectListByTaskId(taskId);
        List<ExampleDetailResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(exampleBOS)){
            exampleBOS.forEach(exampleBO->{
                ExampleDetailResDTO resDTO = new ExampleDetailResDTO();
                BeanUtils.copyProperties(exampleBO,resDTO);

                if (exampleBO.getType() == null){
                    resDTO.setTypeName("暂无");
                }else if (exampleBO.getType() == 1){
                    resDTO.setTypeName("异常用例");
                }else if (exampleBO.getType() == 0){
                    resDTO.setTypeName("正常用例");
                }

                if (exampleBO.getStatus() == null){
                    resDTO.setStatusName("暂无");
                }else if (exampleBO.getStatus() == 0){
                    resDTO.setStatusName("通过");
                }else if (exampleBO.getStatus() == 1){
                    resDTO.setStatusName("失败");
                }else if (exampleBO.getStatus() == 2){
                    resDTO.setStatusName("阻塞");
                }

                if (exampleBO.getExamStatus() == null){
                    resDTO.setExamStatusName("暂无");
                }else if (exampleBO.getExamStatus() == 0){
                    resDTO.setExamStatusName("评审通过");
                }else if (exampleBO.getExamStatus() == 1){
                    resDTO.setExamStatusName("评审失败");
                }

                list.add(resDTO);
            });
        }
        if (!CollectionUtils.isEmpty(list)){

        }
        return null;
    }

    /**
     * 获取上传文件后缀名
     */
    private String getUploadSuffix(String uploadName) {
        return uploadName.substring(uploadName.lastIndexOf(".") + 1);
    }

    //判断是否是excel
    private static boolean isExcel(String url){
        Pattern p=Pattern.compile("\\.(xls|XLS)");
        Matcher m=p.matcher(url);
        if(m.find()){
            return true;
        }
        return false;
    }

    /**
     * MultipartFile 转 File
     */
    private static File multipartFileToFile( @RequestParam MultipartFile file ) throws Exception {

        File toFile = null;
        if(file.equals("")||file.getSize()<=0){
            file = null;
        }else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Sheet getSheetByNum(Workbook book,int number){
        Sheet sheet = null;
        try {
            sheet = book.getSheetAt(number);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return sheet;
    }
    private static Workbook getExcelWorkbook(String filePath) throws IOException {
        Workbook book = null;
        File file  = null;
        FileInputStream fis = null;

        try {
            file = new File(filePath);
            if(!file.exists()){
                throw new RuntimeException("文件不存在");
            }else{
                fis = new FileInputStream(file);
                book = WorkbookFactory.create(fis);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if(fis != null){
                fis.close();
            }
        }
        return book;
    }
}
