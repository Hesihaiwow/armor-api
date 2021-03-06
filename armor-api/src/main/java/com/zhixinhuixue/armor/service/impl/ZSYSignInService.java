package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.*;
import com.zhixinhuixue.armor.exception.ZSYServerException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.helper.ZSYQinuHelper;
import com.zhixinhuixue.armor.model.bo.SignInBO;
import com.zhixinhuixue.armor.model.bo.SignInOriginBO;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYSignInService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYQinuOssProperty;
import com.zhixinhuixue.armor.source.ZSYUFileProperties;
import com.zhixinhuixue.armor.source.enums.ZSYRestHoursType;
import com.zhixinhuixue.armor.source.enums.ZSYSignInType;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.pagehelper.page.PageMethod.startPage;
import static com.zhixinhuixue.armor.service.impl.ZSYMantisBugService.getExcelWorkbook;
import static com.zhixinhuixue.armor.service.impl.ZSYMantisBugService.getSheetByNum;

/**
 * @author SCH
 * @date 2019/2/22 13:27
 */
@Service
public class ZSYSignInService implements IZSYSignInService {

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    @Autowired
    private IZSYSignInMapper signInMapper;
    @Autowired
    private IZSYUserMapper userMapper;
    @Autowired
    private IZSYUserLeaveMapper userLeaveMapper;
    @Autowired
    private IZSYExtraWorkMapper extraWorkMapper;
    @Autowired
    private ZSYQinuOssProperty qinuOssProperty;
    @Autowired
    private ZSYUFileProperties uFileProperties;
    @Autowired
    private IZSYRestHoursLogMapper restHoursLogMapper;
    @Autowired
    private IZSYUserGroupMapper userGroupMapper;
    @Autowired
    private IZSYWorkGroupMapper workGroupMapper;

    private static final Logger logger = LoggerFactory.getLogger(ZSYSignInService.class);

    /**
     * 上传 user-sort 文件到库
     *
     * @param uploadFile
     */
    @Override
    public void addUserSortToMysql(MultipartFile uploadFile) {
        throw new ZSYServerException("接口废除");
    }

    /**
     * 上传.dat的打卡记录文件到库
     *
     * @param uploadFile
     */

    @Override
    @Transactional
    public void uploadToMysql(MultipartFile uploadFile) {
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isExcel(suffix)) {
            throw new ZSYServiceException("只能上传Excel");
        }
        org.apache.poi.ss.usermodel.Workbook book;
        try {
            File file = multipartFileToFile(uploadFile);
            String fileDir = "";
            if (file != null) {
                fileDir = file.getAbsolutePath();
            }
            book = getExcelWorkbook(fileDir);
            org.apache.poi.ss.usermodel.Sheet sheet = getSheetByNum(book, 0);
            int lastRowNum = sheet.getLastRowNum();


            List<String> fields = new ArrayList<>();
            Row row = null;
            row = sheet.getRow(0);
            if (row != null) {
                int lastCellNum = row.getLastCellNum();
                Cell cell = null;
                for (int j = 0; j <= lastCellNum; j++) {
                    cell = row.getCell(j);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        String cellValue = cell.getStringCellValue();
                        fields.add(cellValue);
                    }
                }
            }
            SimpleDateFormat timeSDF = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
            SimpleDateFormat dateSDF = new SimpleDateFormat(DateHelper.DATE_FORMAT);
            Calendar calendar = Calendar.getInstance();
            String dateStr = fields.get(0);
            if (dateStr == null || dateStr.trim().equals("")) {
                throw new ZSYServiceException("上传文件内容出错或没有数据,请检查.error1");
            }
            if (dateStr.length() < 34) {
                throw new ZSYServiceException("上传文件内容出错或没有数据,请检查.error2");
            }
            String dateStr1 = dateStr.substring(11, 21);
            String dateStr2 = dateStr.substring(24);
            if (dateStr1.equals("")) {
                throw new ZSYServiceException("上传文件内容出错或没有数据,请检查.error3");
            }
            if (dateStr2.equals("")) {
                throw new ZSYServiceException("上传文件内容出错或没有数据,请检查.error4");
            }

            //查询有工号的人员
            List<User> users = signInMapper.selectEffectUsers(ZSYTokenRequestContext.get().getOrgId());
            Map<String, Long> userMap = new HashMap<>();
            Map<String, String> nameMap = new HashMap<>();
            for (User user : users) {
                userMap.put(user.getJobNumber(), user.getId());
                nameMap.put(user.getJobNumber(), user.getName());
            }
            Date beginDate = dateSDF.parse(dateStr1);
            Date beginDate2 = null;
            Date endDate = dateSDF.parse(dateStr2);
            Date endDateZero = dateSDF.parse(dateStr2 + " 00:00:00");
            Calendar instance1 = Calendar.getInstance();
            instance1.setTime(endDate);
            int weekday = instance1.get(Calendar.DAY_OF_WEEK);
            Date endDayFive = timeSDF.parse(dateStr2 + " 05:00:00");
            calendar.setTime(beginDate);
            int beginTimeYear = calendar.get(Calendar.YEAR);
            int beginTimeMonth = calendar.get(Calendar.MONTH) + 1;
            int beginTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
            LocalDate start = LocalDate.of(beginTimeYear, beginTimeMonth, beginTimeDay);

            calendar.setTime(endDate);
            int endTimeYear = calendar.get(Calendar.YEAR);
            int endTimeMonth = calendar.get(Calendar.MONTH) + 1;
            int endTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
            LocalDate end = LocalDate.of(endTimeYear, endTimeMonth, endTimeDay);
            Integer checkDays = (int) (end.toEpochDay() - start.toEpochDay() + 1);
            if (checkDays < 1) {
                throw new ZSYServiceException("获取Excel表格第一行日期信息有误");
            }
            //查询最后一条考勤记录
            SignIn lastRecord = signInMapper.selectSingInLastRecord(ZSYTokenRequestContext.get().getOrgId());
            //如果有,那么此次只导入这条记录之后的数据
            Integer lastToBegin = 0;
            if (lastRecord != null) {
                if (lastRecord.getCheckTime().compareTo(endDayFive) < 0) {
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(lastRecord.getCheckTime());
                    instance.add(Calendar.DAY_OF_MONTH, -1);
                    lastRecord.setCheckTime(instance.getTime());
                }
                //Excel的开始日期在最后一条记录之前的 情况
                if (beginDate.compareTo(lastRecord.getCheckTime()) <= 0) {
                    //Excel的截止日期也在最后一条记录之前
                    if (endDate.compareTo(lastRecord.getCheckTime()) <= 0) {
                        throw new ZSYServiceException("此次导入数据,已存在,请检查导入日期");
                    }
                    //Excel的截止日期也在最后一条记录之后,此次导入只需导入后面的数据
                    else {
                        calendar.setTime(lastRecord.getCheckTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        beginDate2 = calendar.getTime();
                        int beginTimeYear2 = calendar.get(Calendar.YEAR);
                        int beginTimeMonth2 = calendar.get(Calendar.MONTH) + 1;
                        int beginTimeDay2 = calendar.get(Calendar.DAY_OF_MONTH);
                        LocalDate start2 = LocalDate.of(beginTimeYear2, beginTimeMonth2, beginTimeDay2);
                        checkDays = (int) (end.toEpochDay() - start2.toEpochDay() + 1);
                    }
                }

                //Excel的开始日期在最后一条记录之后
                else {
                    calendar.setTime(lastRecord.getCheckTime());
                    int beginTimeYear4 = calendar.get(Calendar.YEAR);
                    int beginTimeMonth4 = calendar.get(Calendar.MONTH) + 1;
                    int beginTimeDay4 = calendar.get(Calendar.DAY_OF_MONTH);
                    LocalDate start4 = LocalDate.of(beginTimeYear4, beginTimeMonth4, beginTimeDay4);

                    calendar.setTime(beginDate);
                    int endTimeYear4 = calendar.get(Calendar.YEAR);
                    int endTimeMonth4 = calendar.get(Calendar.MONTH) + 1;
                    int endTimeDay4 = calendar.get(Calendar.DAY_OF_MONTH);
                    LocalDate end4 = LocalDate.of(endTimeYear4, endTimeMonth4, endTimeDay4);
                    lastToBegin = (int) (end4.toEpochDay() - start4.toEpochDay() - 2);
                }
            }
            List<List<String>> userCheckList = new ArrayList<>();
            for (int i = 3; i <= lastRowNum; i++) {
                List<String> checkTimeList = new ArrayList<>();
                Row row2 = null;
                row2 = sheet.getRow(i);
                if (row2 != null) {
                    int lastCellNum = row2.getLastCellNum();
                    Cell cell = null;
                    for (int j = 0; j <= lastCellNum; j++) {
                        cell = row2.getCell(j);
                        if (cell != null) {
                            cell.setCellType(CellType.STRING);
                            String cellValue = cell.getStringCellValue();
                            checkTimeList.add(cellValue);
                        }
                    }
                }
                userCheckList.add(checkTimeList);
            }

            if (CollectionUtils.isEmpty(userCheckList)) {
                throw new ZSYServiceException("表格暂无数据");
            }
            //遍历Excel,按用户准备数据
            List<SignIn> totalList = new ArrayList<>();
            for (List<String> userCheck : userCheckList) {
                List<SignIn> signInList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(userCheck)) {
                    String jobNumber = userCheck.get(2);
                    Long userId = userMap.get(jobNumber);
                    //能匹配到,就准备数据
                    if (userId != null) {
                        for (int i = 0; i < checkDays; i++) {
                            String checkTimeStr = null;
                            String format = null;
                            if (beginDate2 != null) {
                                //计算beginDate2和beginDate直接的天数
                                calendar.setTime(beginDate);
                                int beginTimeYear3 = calendar.get(Calendar.YEAR);
                                int beginTimeMonth3 = calendar.get(Calendar.MONTH) + 1;
                                int beginTimeDay3 = calendar.get(Calendar.DAY_OF_MONTH);
                                LocalDate start3 = LocalDate.of(beginTimeYear3, beginTimeMonth3, beginTimeDay3);

                                calendar.setTime(beginDate2);
                                int endTimeYear3 = calendar.get(Calendar.YEAR);
                                int endTimeMonth3 = calendar.get(Calendar.MONTH) + 1;
                                int endTimeDay3 = calendar.get(Calendar.DAY_OF_MONTH);
                                LocalDate end3 = LocalDate.of(endTimeYear3, endTimeMonth3, endTimeDay3);
                                Integer dayRange = (int) (end3.toEpochDay() - start3.toEpochDay());
                                calendar.setTime(beginDate2);
                                calendar.add(Calendar.DAY_OF_MONTH, i);

                                format = dateSDF.format(calendar.getTime());
                                checkTimeStr = userCheck.get(5 + dayRange + i);
                            } else {
                                calendar.setTime(beginDate);
                                calendar.add(Calendar.DAY_OF_MONTH, i);
                                checkTimeStr = userCheck.get(5 + i);
                                format = dateSDF.format(calendar.getTime());
                            }
                            if (checkTimeStr != null && !checkTimeStr.trim().equals("")) {
                                String[] checkTimeList = checkTimeStr.split("  \n");

                                for (int j = 0; j < checkTimeList.length; j++) {
                                    String singleCheckTimeStr = "";
                                    singleCheckTimeStr = format + " " + checkTimeList[j].trim() + ":00";
                                    if (checkTimeList[j].startsWith("0.")) {
                                        singleCheckTimeStr = format + " 09:30:00";
                                    }
                                    SignIn signIn = new SignIn();
                                    signIn.setId(snowFlakeIDHelper.nextId());
                                    Date parse = timeSDF.parse(singleCheckTimeStr);
                                    Calendar instance = Calendar.getInstance();
                                    instance.setTime(parse);
                                    String format1 = dateSDF.format(parse);
                                    Date todayZero = timeSDF.parse(format1 + " 00:00:00");
                                    Date todayFive = timeSDF.parse(format1 + " 05:00:00");
                                    //如果导入的时间在今日0点到5点之间,默认是下一天的时间,天数加一
                                    if (parse.compareTo(todayZero) >= 0 && parse.compareTo(todayFive) <= 0) {
                                        instance.add(Calendar.DAY_OF_MONTH, 1);
                                    }

                                    signIn.setCheckTime(instance.getTime());
                                    signIn.setType(ZSYSignInType.NORMAL_SIGN.getValue());
                                    signIn.setUserId(userId);
                                    signInList.add(signIn);
                                }
                            } else {
                                String singleCheckTimeStr = format + " 00:00:00";
                                SignIn signIn = new SignIn();
                                signIn.setId(snowFlakeIDHelper.nextId());
                                signIn.setCheckTime(timeSDF.parse(singleCheckTimeStr));
                                signIn.setType(ZSYSignInType.MANUAL.getValue());
                                signIn.setUserId(userId);
                                signInList.add(signIn);
                            }
                        }
                        //上一条记录到此次导入的第一天直接的数据
                        if (lastToBegin > 0) {
                            for (int k = 0; k < lastToBegin; k++) {
                                calendar.setTime(beginDate);
                                calendar.add(Calendar.DAY_OF_MONTH, -(k + 1));
                                Date time = calendar.getTime();
                                String format = dateSDF.format(time);
                                Date checkTime = timeSDF.parse(format + " 00:00:00");
                                SignIn signIn = new SignIn();
                                signIn.setId(snowFlakeIDHelper.nextId());
                                signIn.setUserId(userId);
                                signIn.setType(ZSYSignInType.MANUAL.getValue());
                                signIn.setCheckTime(checkTime);
                                signInList.add(signIn);
                            }
                        }
                        totalList.addAll(signInList);
                    } else {
                        throw new ZSYServiceException("工号: " + jobNumber + " 的用户没有匹配上,请让管理员查看");
                    }
                }
            }
            if (!CollectionUtils.isEmpty(totalList)) {
                List<SignIn> lastDayList = totalList.stream().filter(signIn -> signIn.getCheckTime().compareTo(endDateZero) >= 0)
                        .collect(Collectors.toList());
                int excelUserNum = userCheckList.size();
                //最后一天不是周末
                if ((weekday != 1 && weekday != 7) && (excelUserNum * 2 - lastDayList.size() > 20)) {
                    //假如打卡人数的2倍 减去 最后一天的打卡记录 > 20   可视为当天的数据只有一半,剔除之后再新增
                    logger.info("最后一天数据只有半天,剔除");
                    totalList = totalList.stream().filter(signIn -> signIn.getCheckTime().compareTo(endDateZero) < 0)
                            .collect(Collectors.toList());
                }

                Long orgId = ZSYTokenRequestContext.get().getOrgId();

                List<SignIn> totalListOrg = totalList.stream().map(signIn -> {

                    SignIn signIn1 = new SignIn();

                    BeanUtils.copyProperties(signIn,signIn1);

                    signIn1.setOrgId(orgId);

                    return signIn1;

                }).collect(Collectors.toList());

                if (signInMapper.insertSignInBatch(totalListOrg) == 0) {
                    throw new ZSYServiceException("导入考勤记录失败");
                }
            }

        } catch (Exception e) {
            throw new ZSYServiceException(e.getMessage());
        }

    }

    /**
     * 导入记录的最后一条记录打卡时间
     *
     * @return
     */
    @Override
    public SignInLastRecordResDTO getSignInLastRecord() {
        SignIn signIn = signInMapper.selectSingInLastRecord(ZSYTokenRequestContext.get().getOrgId());
        SignInLastRecordResDTO resDTO = new SignInLastRecordResDTO();
        BeanUtils.copyProperties(signIn, resDTO);
        return resDTO;
    }

    /**
     * 分页查询考勤记录
     *
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<SignInResDTO> getSignInPage(SignInReqDTO reqDTO) {
        if (reqDTO.getBeginTime() == null) {
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DATE_FORMAT);
            SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, -1);
            String yesterdayPrefix = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE, -6);
            String prefix = sdf.format(calendar.getTime());
            try {
                Date beginTime = sdf2.parse(prefix + " 00:00:00");
                Date endTime = sdf2.parse(yesterdayPrefix + " 23:59:59");
                reqDTO.setBeginTime(beginTime);
                reqDTO.setEndTime(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<SignInUser> signInUsersGroup = getSignInUsersGroup();
        List<Long> userIds = signInUsersGroup.stream().map(SignInUser::getUserId).collect(Collectors.toList());
        if (ZSYTokenRequestContext.get().getUserRole().intValue() == ZSYUserRole.ADMINISTRATOR.getValue()) {
            reqDTO.setUserIds(null);
        } else {
            reqDTO.setUserIds(userIds);
        }
        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), 20);
        Page<SignInBO> signInPage = signInMapper.selectSignInPage(reqDTO,ZSYTokenRequestContext.get().getOrgId());
        Page<SignInResDTO> signInResDTOS = new Page<>();
        BeanUtils.copyProperties(signInPage, signInResDTOS);
        if (!CollectionUtils.isEmpty(signInPage)) {
            signInPage.stream().forEach(signIn -> {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signIn, resDTO);
                User user = userMapper.selectById(signIn.getUserId());
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE, 1);
                String prefix = sdf2.format(dateList.get(0));
                String nextPrefix = sdf2.format(calendar.getTime());
                Date today0 = null;
                Date today10 = null;
                Date today18 = null;
                Date today23 = null;
                Date zero = null;
                Date seven = null;
                Date nextSeven = null;
                Date fifteen = null;
                try {
                    today0 = sdf.parse(prefix + " 00:00:00");
                    today10 = sdf.parse(prefix + " 09:31:00");
                    today18 = sdf.parse(prefix + " 18:30:00");
                    today23 = sdf.parse(prefix + " 23:59:59");
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 05:00:00");
                    seven = sdf.parse(prefix + " 05:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time -> {
                    try {
                        return time.after(sdf.parse(prefix + " 05:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)) {
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven, zero,ZSYTokenRequestContext.get().getOrgId());
                    if (!CollectionUtils.isEmpty(before7Clock)) {
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)) {
                            resDTO.setIsCheckOutBeforeSix(1);
                        } else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                    } else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                }
                //只有一条打卡记录
                else if (dateList.size() == 1) {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven, zero,ZSYTokenRequestContext.get().getOrgId());
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(before7Clock)) {
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.get(0).getType() == 1) {
                                resDTO.setIsRecheckIn(1);
                            } else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)) {
                                resDTO.setIsCheckInAfterTen(1);
                            } else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        } else {
                            List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.get(0).getType() == 1) {
                                resDTO.setIsRecheckOut(1);
                            } else {
                                resDTO.setIsRecheckOut(0);
                            }
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            if (resDTO.getCheckOutTime().before(today18)) {
                                resDTO.setIsCheckOutBeforeSix(1);
                            } else {
                                resDTO.setIsCheckOutBeforeSix(0);
                            }
                            resDTO.setIsWorkToNextDay(0);
                        }
                    }
                    //第二天有7点前的打卡记录,取最后时间作为下班时间
                    else {
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)) {
                                resDTO.setIsCheckInAfterTen(1);
                            } else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.get(0).getType() == 1) {
                                resDTO.setIsRecheckIn(1);
                            } else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setIsForget(0);
                        } else {
                            resDTO.setCheckInTime(null);
                            resDTO.setIsForget(1);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)) {
                            resDTO.setIsCheckOutBeforeSix(1);
                        } else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (signIn1.get(0).getType() == 1) {
                            resDTO.setIsRecheckOut(1);
                        } else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = null;
                        if (resDTO.getIsForget() == 0) {
                            workTimeMillis = resDTO.getCheckOutTime().getTime() - resDTO.getCheckInTime().getTime();
                        }
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                //2条或以上
                else {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven, zero, ZSYTokenRequestContext.get().getOrgId());
                    //第二天早于7点没有打卡记录
                    if (CollectionUtils.isEmpty(before7Clock)) {
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)) {
                            resDTO.setIsCheckInAfterTen(1);
                        } else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.get(0).getType() == 1) {
                            resDTO.setIsRecheckIn(1);
                        } else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(dateList.get(dateList.size() - 1));
                        if (resDTO.getCheckOutTime().before(today18)) {
                            resDTO.setIsCheckOutBeforeSix(1);
                        } else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        List<SignIn> signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(dateList.size() - 1));
                        if (signIn2.get(0).getType() == 1) {
                            resDTO.setIsRecheckOut(1);
                        } else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(0);
                        Long workTimeMillis = dateList.get(dateList.size() - 1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                    //第二天早于7点有打卡记录
                    else {
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)) {
                            resDTO.setIsCheckInAfterTen(1);
                        } else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.get(0).getType() == 1) {
                            resDTO.setIsRecheckIn(1);
                        } else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)) {
                            resDTO.setIsCheckOutBeforeSix(1);
                        } else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        List<SignIn> signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (signIn2.get(0).getType() == 1) {
                            resDTO.setIsRecheckOut(1);
                        } else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = before7Clock.get(before7Clock.size() - 1).getCheckTime().getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                UserLeave userLeave = userLeaveMapper.selectByUserAndTime(user.getId(), today0, today23,ZSYTokenRequestContext.get().getOrgId());
                if (userLeave != null) {
                    // 1.先判断请假持续几天
                    List<String> leaveDays = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), userLeave.getEndTime());
                    //此条请假记录只有一天,则请假时长即为当天的请假时长
                    if (leaveDays.size() == 1) {
                        resDTO.setLeaveTime(userLeave.getHours());
                    } else {
                        // 2.当请假持续天数超过一天时,再判断今天是第几天
                        List<String> firstDayToToday = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), today23);
                        //当请假开始时间早于10:00时,则第一天请假为8h
                        if (userLeave.getBeginTime().getHours() <= 10) {
                            if (firstDayToToday.size() < leaveDays.size()) {
                                //当今天不是最后一天,请假时长为8h
                                resDTO.setLeaveTime(BigDecimal.valueOf(8));
                            } else {
                                //当今天是最后一天,请假时长为 总时长减去之前的时长
                                BigDecimal firstLeaveHours = BigDecimal.valueOf(8);
                                BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf((long) leaveDays.size() - 2));
                                resDTO.setLeaveTime(userLeave.getHours().
                                        subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                            }
                        } else {
                            if (firstDayToToday.size() < leaveDays.size()) {
                                //当今天不是最后一天,请假时长为4h
                                if (firstDayToToday.size() == 1) {
                                    resDTO.setLeaveTime(BigDecimal.valueOf(5));
                                } else {
                                    resDTO.setLeaveTime(BigDecimal.valueOf(8));
                                }
                            } else {
                                //当今天是最后一天,请假时长为 总时长减去之前的时长
                                BigDecimal firstLeaveHours = BigDecimal.valueOf(5);
                                BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf((long) leaveDays.size() - 2));
                                resDTO.setLeaveTime(userLeave.getHours().
                                        subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                            }
                        }
                    }
                } else {
                    resDTO.setLeaveTime(BigDecimal.ZERO);
                }
                if (resDTO.getWorkTime() != null) {
                    if (resDTO.getWorkTime() > 9 * 60 * 60 * 1000) {
                        resDTO.setLessThanNine(0);
                    } else {
                        resDTO.setLessThanNine(1);
                    }
                }
                resDTO.setDate(today0);
                calendar.setTime(today0);
                if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                    resDTO.setIsWeekend(1);
                } else {
                    resDTO.setIsWeekend(0);
                }
                Float eWorkHours = extraWorkMapper.selectEWorkHours(user.getId(), today0, today23);
                resDTO.setEWorkHours(BigDecimal.valueOf(eWorkHours));
                calendar.setTime(today0);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek) {
                    case 1:
                        resDTO.setWeekday("星期日");
                        break;
                    case 2:
                        resDTO.setWeekday("星期一");
                        break;
                    case 3:
                        resDTO.setWeekday("星期二");
                        break;
                    case 4:
                        resDTO.setWeekday("星期三");
                        break;
                    case 5:
                        resDTO.setWeekday("星期四");
                        break;
                    case 6:
                        resDTO.setWeekday("星期五");
                        break;
                    case 7:
                        resDTO.setWeekday("星期六");
                        break;
                    default:
                        resDTO.setWeekday("");
                        break;
                }

                signInResDTOS.add(resDTO);
            });
        }

        return new PageInfo<>(signInResDTOS);
    }

    /**
     * 个人分页查询考勤统计
     *
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<SignInResDTO> getPersonalSignInPage(SignInReqDTO reqDTO) {
        reqDTO.setUserId(ZSYTokenRequestContext.get().getUserId());
        if (reqDTO.getBeginTime() == null) {
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DATE_FORMAT);
            SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE, -1);
            String yesterdayPrefix = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE, -6);
            String prefix = sdf.format(calendar.getTime());
            try {
                Date beginTime = sdf2.parse(prefix + " 00:00:00");
                Date endTime = sdf2.parse(yesterdayPrefix + " 23:59:59");
                reqDTO.setBeginTime(beginTime);
                reqDTO.setEndTime(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), 7);
        Page<SignInBO> signInPage = signInMapper.selectSignInPage(reqDTO,ZSYTokenRequestContext.get().getOrgId());
        Page<SignInResDTO> signInResDTOS = new Page<>();
        BeanUtils.copyProperties(signInPage, signInResDTOS);
        if (!CollectionUtils.isEmpty(signInPage)) {
            signInPage.stream().forEach(signIn -> {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signIn, resDTO);
                User user = userMapper.selectById(signIn.getUserId());
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE, 1);
                String prefix = sdf2.format(dateList.get(0));
                String nextPrefix = sdf2.format(calendar.getTime());
                Date zero = null;
                Date today10 = null;
                Date today18 = null;
                Date seven = null;
                Date nextSeven = null;
                Date fifteen = null;
                Date today0 = null;
                Date today23 = null;
                try {
                    today0 = sdf.parse(prefix + " 00:00:00");
                    today10 = sdf.parse(prefix + " 09:31:00");
                    today18 = sdf.parse(prefix + " 18:30:00");
                    today23 = sdf.parse(prefix + " 23:59:59");
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 05:00:00");
                    seven = sdf.parse(prefix + " 05:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time -> {
                    try {
                        return time.after(sdf.parse(prefix + " 05:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)) {
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven, zero,ZSYTokenRequestContext.get().getOrgId());
                    if (!CollectionUtils.isEmpty(before7Clock)) {
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)) {
                            resDTO.setIsCheckOutBeforeSix(1);
                        } else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                    } else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                }
                //只有一条打卡记录
                else if (dateList.size() == 1) {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven, zero,ZSYTokenRequestContext.get().getOrgId());
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(before7Clock)) {
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)) {
                                resDTO.setIsCheckInAfterTen(1);
                            } else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.get(0).getType() == 1) {
                                resDTO.setIsRecheckIn(1);
                            } else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        } else {
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            if (resDTO.getCheckOutTime().before(today18)) {
                                resDTO.setIsCheckOutBeforeSix(1);
                            } else {
                                resDTO.setIsCheckOutBeforeSix(0);
                            }
                            List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.get(0).getType() == 1) {
                                resDTO.setIsRecheckOut(1);
                            } else {
                                resDTO.setIsRecheckOut(0);
                            }
                            resDTO.setIsWorkToNextDay(0);
                        }
                    }
                    //第二天有7点前的打卡记录,取最后时间作为下班时间
                    else {
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)) {
                                resDTO.setIsCheckInAfterTen(1);
                            } else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.get(0).getType() == 1) {
                                resDTO.setIsRecheckIn(1);
                            } else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setIsForget(0);
                        } else {
                            resDTO.setCheckInTime(null);
                            resDTO.setIsForget(1);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)) {
                            resDTO.setIsCheckOutBeforeSix(1);
                        } else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (signIn1.get(0).getType() == 1) {
                            resDTO.setIsRecheckOut(1);
                        } else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = null;
                        if (resDTO.getIsForget() == 0) {
                            workTimeMillis = resDTO.getCheckOutTime().getTime() - resDTO.getCheckInTime().getTime();
                        }
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                //2条或以上
                else {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven, zero,ZSYTokenRequestContext.get().getOrgId());
                    //第二天早于7点没有打卡记录
                    if (CollectionUtils.isEmpty(before7Clock)) {
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)) {
                            resDTO.setIsCheckInAfterTen(1);
                        } else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.get(0).getType() == 1) {
                            resDTO.setIsRecheckIn(1);
                        } else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(dateList.get(dateList.size() - 1));
                        if (resDTO.getCheckOutTime().before(today18)) {
                            resDTO.setIsCheckOutBeforeSix(1);
                        } else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        List<SignIn> signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(dateList.size() - 1));
                        if (signIn2.get(0).getType() == 1) {
                            resDTO.setIsRecheckOut(1);
                        } else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(0);
                        Long workTimeMillis = dateList.get(dateList.size() - 1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                    //第二天早于7点有打卡记录
                    else {
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)) {
                            resDTO.setIsCheckInAfterTen(1);
                        } else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        List<SignIn> signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.get(0).getType() == 1) {
                            resDTO.setIsRecheckIn(1);
                        } else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)) {
                            resDTO.setIsCheckOutBeforeSix(1);
                        } else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        List<SignIn> signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        if (signIn2.get(0).getType() == 1) {
                            resDTO.setIsRecheckOut(1);
                        } else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = before7Clock.get(before7Clock.size() - 1).getCheckTime().getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                UserLeave userLeave = userLeaveMapper.selectByUserAndTime(user.getId(), today0, today23,ZSYTokenRequestContext.get().getOrgId());
                if (userLeave != null) {
                    // 1.先判断请假持续几天
                    List<String> leaveDays = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), userLeave.getEndTime());
                    //此条请假记录只有一天,则请假时长即为当天的请假时长
                    if (leaveDays.size() == 1) {
                        resDTO.setLeaveTime(userLeave.getHours());
                    } else {
                        // 2.当请假持续天数超过一天时,再判断今天是第几天
                        List<String> firstDayToToday = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), today23);
                        //当请假开始时间早于10:00时,则第一天请假为8h
                        if (userLeave.getBeginTime().getHours() <= 10) {
                            if (firstDayToToday.size() < leaveDays.size()) {
                                //当今天不是最后一天,请假时长为8h
                                resDTO.setLeaveTime(BigDecimal.valueOf(8));
                            } else {
                                //当今天是最后一天,请假时长为 总时长减去之前的时长
                                BigDecimal firstLeaveHours = BigDecimal.valueOf(8);
                                BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf((long) leaveDays.size() - 2));
                                resDTO.setLeaveTime(userLeave.getHours().
                                        subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                            }
                        } else {
                            if (firstDayToToday.size() < leaveDays.size()) {
                                //当今天不是最后一天,请假时长为4h
                                if (firstDayToToday.size() == 1) {
                                    resDTO.setLeaveTime(BigDecimal.valueOf(5));
                                } else {
                                    resDTO.setLeaveTime(BigDecimal.valueOf(8));
                                }
                            } else {
                                //当今天是最后一天,请假时长为 总时长减去之前的时长
                                BigDecimal firstLeaveHours = BigDecimal.valueOf(5);
                                BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf((long) leaveDays.size() - 2));
                                resDTO.setLeaveTime(userLeave.getHours().
                                        subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                            }
                        }
                    }
                } else {
                    resDTO.setLeaveTime(BigDecimal.ZERO);
                }
                if (resDTO.getWorkTime() != null) {
                    if (resDTO.getWorkTime() > 9 * 60 * 60 * 1000) {
                        resDTO.setLessThanNine(0);
                    } else {
                        resDTO.setLessThanNine(1);
                    }
                }
                resDTO.setDate(today0);
                calendar.setTime(today0);
                if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                    resDTO.setIsWeekend(1);
                } else {
                    resDTO.setIsWeekend(0);
                }
                Float eWorkHours = extraWorkMapper.selectEWorkHours(user.getId(), today0, today23);
                resDTO.setEWorkHours(BigDecimal.valueOf(eWorkHours));
                List<String> daysBetweenTwoDate = DateHelper.getDaysBetweenTwoDate(today0, new Date());
                if (daysBetweenTwoDate.size() <= 8) {
                    resDTO.setCanReCheck(1);
                }
                calendar.setTime(today0);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek) {
                    case 1:
                        resDTO.setWeekday("星期日");
                        break;
                    case 2:
                        resDTO.setWeekday("星期一");
                        break;
                    case 3:
                        resDTO.setWeekday("星期二");
                        break;
                    case 4:
                        resDTO.setWeekday("星期三");
                        break;
                    case 5:
                        resDTO.setWeekday("星期四");
                        break;
                    case 6:
                        resDTO.setWeekday("星期五");
                        break;
                    case 7:
                        resDTO.setWeekday("星期六");
                        break;
                    default:
                        resDTO.setWeekday("");
                        break;
                }

                signInResDTOS.add(resDTO);
            });
        }
        return new PageInfo<>(signInResDTOS);
    }

    /**
     * 个人漏打卡考勤记录
     *
     * @param pageNum
     * @return
     */
    @Override
    public ArmorPageInfo<SignInResDTO> getForgetSignInPage(Integer pageNum) {

        List<SignInBO> signInPage = signInMapper.selectForgetSignInList(ZSYTokenRequestContext.get().getUserId());
        List<SignInResDTO> signInResDTOS = new ArrayList<>();
        BeanUtils.copyProperties(signInPage, signInResDTOS);
        if (!CollectionUtils.isEmpty(signInPage)) {
            signInPage.stream().forEach(signIn -> {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signIn, resDTO);
                User user = userMapper.selectById(signIn.getUserId());
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE, 1);
                String prefix = sdf2.format(dateList.get(0));
                String nextPrefix = sdf2.format(calendar.getTime());
                String thisWeekFirst = DateHelper.getThisWeekFirstDay();
                String thisWeekLast = DateHelper.getThisWeekLastDay();
                Date thisWeekFirstDay = null;
                Date thisWeekLastDay = null;
                Date zero = null;
                Date seven = null;
                Date nextSeven = null;
                Date fifteen = null;
                try {
                    thisWeekFirstDay = sdf.parse(thisWeekFirst);
                    thisWeekLastDay = sdf.parse(thisWeekLast);
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 05:00:00");
                    seven = sdf.parse(prefix + " 05:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //看此条打卡记录是否在本周之内
                if (dateList.get(0).after(thisWeekFirstDay) && dateList.get(0).before(thisWeekLastDay)) {
                    resDTO.setCanReCheck(1);
                } else {
                    resDTO.setCanReCheck(0);
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time -> {
                    try {
                        return time.after(sdf.parse(prefix + " 05:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)) {
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven, zero, ZSYTokenRequestContext.get().getOrgId());
                    if (!CollectionUtils.isEmpty(before7Clock)) {
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size() - 1).getCheckTime());
                        resDTO.setIsWorkToNextDay(1);
                    } else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                    signInResDTOS.add(resDTO);
                }
                //只有一条打卡记录
                else if (dateList.size() == 1) {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven, zero,ZSYTokenRequestContext.get().getOrgId());
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(before7Clock)) {
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        } else {
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            resDTO.setIsWorkToNextDay(0);
                        }
                        signInResDTOS.add(resDTO);
                    }
                }
            });
        }
        ArmorPageInfo<SignInResDTO> page = ArmorPageInfo.pageByMemory(signInResDTOS, Optional.ofNullable(pageNum).orElse(1));
        List<SignInResDTO> list = page.getList();
        int current = page.getCurrent();
        long totalSize = page.getTotalSize();
        return new ArmorPageInfo<>(current, ZSYConstants.PAGE_SIZE_WAIT, totalSize, list);
    }

    /**
     * 补打卡申请
     *
     * @param reqDTO
     */
    @Override
    @Transactional
    public void addResignIn(ResignInReqDTO reqDTO) {
        if (reqDTO.getRecheckInTime() != null) {
            ResignIn resignIn = new ResignIn();
            BeanUtils.copyProperties(reqDTO, resignIn);
            resignIn.setRecheckTime(reqDTO.getRecheckInTime());
            resignIn.setType(0);
            resignIn.setId(snowFlakeIDHelper.nextId());
            resignIn.setReviewStatus(0);
            resignIn.setOrgId(ZSYTokenRequestContext.get().getOrgId());
            if (signInMapper.addResignIn(resignIn) == 0) {
                throw new ZSYServiceException("新增上班补打卡申请失败");
            }
        }
        if (reqDTO.getRecheckOutTime() != null) {
            ResignIn resignIn = new ResignIn();
            BeanUtils.copyProperties(reqDTO, resignIn);
            resignIn.setRecheckTime(reqDTO.getRecheckOutTime());
            resignIn.setType(1);
            resignIn.setId(snowFlakeIDHelper.nextId());
            resignIn.setReviewStatus(0);
            resignIn.setOrgId(ZSYTokenRequestContext.get().getOrgId());
            if (signInMapper.addResignIn(resignIn) == 0) {
                throw new ZSYServiceException("新增下班补打卡申请失败");
            }
        }

    }

    /**
     * 修改补打卡申请
     *
     * @param reqDTO
     * @param id
     */
    @Override
    @Transactional
    public void updateResignIn(ResignInReqDTO reqDTO, Long id) {
        ResignIn resignIn = signInMapper.selectResignInById(id);
        if (resignIn == null) {
            throw new ZSYServiceException("该条补打卡申请不存在");
        }
        BeanUtils.copyProperties(reqDTO, resignIn);
        if (resignIn.getType() == 0) {
            resignIn.setRecheckTime(reqDTO.getRecheckInTime());
        } else {
            resignIn.setRecheckTime(reqDTO.getRecheckOutTime());
        }
        if (signInMapper.updateResignIn(resignIn, id) == 0) {
            throw new ZSYServiceException("修改补打卡申请失败");
        }
    }


    /**
     * 删除补打卡申请
     *
     * @param id
     */
    @Override
    @Transactional
    public void deleteResignIn(Long id) {
        ResignIn resignIn = signInMapper.selectResignInById(id);
        if (resignIn == null) {
            throw new ZSYServiceException("该条补打卡申请不存在");
        }
        if (signInMapper.deleteResignInById(id) == 0) {
            throw new ZSYServiceException("删除补打卡申请失败");
        }
        //如果删除的是审核通过的  补打卡   还需要删除与之相关的signIn记录
        if (resignIn.getReviewStatus() == 2) {
            List<SignIn> signIn = signInMapper.selectSignInByUserAndTime(resignIn.getUserId(), resignIn.getRecheckTime());
            if (!CollectionUtils.isEmpty(signIn) && signInMapper.deleteSignIn(resignIn.getRecheckTime(), resignIn.getUserId()) == 0) {
                throw new ZSYServiceException("删除补打卡申请失败");
            }
        }
    }

    /**
     * 审核通过补打卡申请
     *
     * @param id
     */
    @Override
    @Transactional
    public void accessResignIn(ResignInReqDTO reqDTO, Long id) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.ADMINISTRATOR.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        ResignIn resignIn = signInMapper.selectResignInById(id);
        if (resignIn == null) {
            throw new ZSYServiceException("该条补打卡申请不存在");
        }
        resignIn.setReviewStatus(2);
        if (resignIn.getType() == 0) {
            resignIn.setRecheckTime(reqDTO.getRecheckInTime());
        } else {
            resignIn.setRecheckTime(reqDTO.getRecheckOutTime());
        }
        resignIn.setReason(reqDTO.getReason());
        if (signInMapper.updateResignIn(resignIn, id) == 0) {
            throw new ZSYServiceException("审核通过补打卡申请失败");
        }
        SignIn signIn = new SignIn();
        signIn.setId(snowFlakeIDHelper.nextId());
        signIn.setUserId(resignIn.getUserId());
        signIn.setCheckTime(resignIn.getRecheckTime());
        signIn.setType(ZSYSignInType.RE_SIGN.getValue());
        signIn.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        if (signInMapper.addSignIn(signIn) == 0) {
            throw new ZSYServiceException("审核通过补打卡申请失败");
        }
        List<SignIn> existSignIn = signInMapper.selectSignInByUserAndTime(signIn.getUserId(), signIn.getCheckTime());
        if (!CollectionUtils.isEmpty(existSignIn)) {
            SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.DATE_FORMAT);
            String prefix = sdf2.format(existSignIn.get(0).getCheckTime());
            try {
                Date today0 = sdf2.parse(prefix + " 00:00:00");
                Date today23 = sdf2.parse(prefix + " 23:59:59");
                signInMapper.deleteUselessSignIn(existSignIn.get(0).getUserId(), today0, today23);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 个人查看加班总时长
     *
     * @param yearAndMonth
     * @return
     */
    @Override
    public TotalExtraHoursResDTO getPersonalTotalExtraHours(String yearAndMonth) {
        TotalExtraHoursResDTO totalExtraHoursResDTO = new TotalExtraHoursResDTO();
        Long extraWorkTime = 0L;
        if (yearAndMonth == null || yearAndMonth.trim().equals("")) {
            SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
            yearAndMonth = yearSdf.format(new Date());
        }
        List<SignInBO> signInList = signInMapper.selectPersonalSignInList(ZSYTokenRequestContext.get().getUserId(), yearAndMonth);
        User user = userMapper.selectById(ZSYTokenRequestContext.get().getUserId());
        //根据userId和给定月份查询每天0点到7点之间的数据
        List<SignIn> before7Clock = signInMapper.selectAllBetween0And7AndUserId(yearAndMonth, ZSYTokenRequestContext.get().getUserId());
        if (!CollectionUtils.isEmpty(signInList)) {
            for (SignInBO signInBO : signInList) {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signInBO, resDTO);
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signInBO.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE, 1);
                String prefix = sdf2.format(dateList.get(0));
                String nextPrefix = sdf2.format(calendar.getTime());
                Date zero = null;
                Date seven = null;
                Date nextSeven = null;
                Date fifteen = null;
                try {
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 05:00:00");
                    seven = sdf.parse(prefix + " 05:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time -> {
                    try {
                        return time.after(sdf.parse(prefix + " 05:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)) {
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<Date> between0And7Dates = new ArrayList<>();
                    for (SignIn signIn : before7Clock) {
                        if ((signIn.getCheckTime().after(zero)) && (signIn.getCheckTime().before(nextSeven))) {
                            between0And7Dates.add(signIn.getCheckTime());
                        }
                    }
                    if (!CollectionUtils.isEmpty(between0And7Dates)) {
                        resDTO.setCheckOutTime(between0And7Dates.get(between0And7Dates.size() - 1));
                        resDTO.setIsWorkToNextDay(1);
                    } else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                }
                //只有一条打卡记录
                else if (dateList.size() == 1) {
                    List<Date> between0And7Dates = new ArrayList<>();
                    for (SignIn signIn : before7Clock) {
                        if ((signIn.getCheckTime().after(zero)) && (signIn.getCheckTime().before(nextSeven))) {
                            between0And7Dates.add(signIn.getCheckTime());
                        }
                    }
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(between0And7Dates)) {
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        } else {
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            resDTO.setIsWorkToNextDay(0);
                        }
                    }
                    //第二天有7点前的打卡记录,取最后时间作为下班时间
                    else {
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            resDTO.setIsForget(0);
                        } else {
                            resDTO.setCheckInTime(null);
                            resDTO.setIsForget(1);
                        }
                        resDTO.setCheckOutTime(between0And7Dates.get(between0And7Dates.size() - 1));
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = null;
                        if (resDTO.getIsForget() == 0) {
                            workTimeMillis = resDTO.getCheckOutTime().getTime() - resDTO.getCheckInTime().getTime();
                        }
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                //2条或以上
                else {
                    List<Date> between0And7Dates = new ArrayList<>();
                    for (SignIn signIn : before7Clock) {
                        if ((signIn.getCheckTime().after(zero)) && (signIn.getCheckTime().before(nextSeven))) {
                            between0And7Dates.add(signIn.getCheckTime());
                        }
                    }
                    //第二天早于7点没有打卡记录
                    if (CollectionUtils.isEmpty(between0And7Dates)) {
                        resDTO.setCheckInTime(dateList.get(0));
                        resDTO.setCheckOutTime(dateList.get(dateList.size() - 1));
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(0);
                        Long workTimeMillis = dateList.get(dateList.size() - 1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                    //第二天早于7点有打卡记录
                    else {
                        resDTO.setCheckInTime(dateList.get(0));
                        resDTO.setCheckOutTime(between0And7Dates.get(between0And7Dates.size() - 1));
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = between0And7Dates.get(between0And7Dates.size() - 1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                if (resDTO.getEWorkTime() != null) {
                    extraWorkTime += resDTO.getEWorkTime();
                } else {
                    extraWorkTime += 0L;
                }
            }
        }
        totalExtraHoursResDTO.setUserId(ZSYTokenRequestContext.get().getUserId());
        totalExtraHoursResDTO.setYearAndMonth(yearAndMonth);
        totalExtraHoursResDTO.setExtraTime(extraWorkTime);
        return totalExtraHoursResDTO;
    }

    /**
     * 管理员查看用户的加班总时长
     *
     * @param userId
     * @param yearAndMonth
     * @return
     */
    @Override
    public TotalExtraHoursResDTO getTotalExtraHoursByUserId(Long userId, String yearAndMonth) {
        TotalExtraHoursResDTO totalExtraHoursResDTO = new TotalExtraHoursResDTO();
        Long extraWorkTime = 0L;
        if (yearAndMonth == null || yearAndMonth.trim().equals("")) {
            SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
            yearAndMonth = yearSdf.format(new Date());
        }
        List<SignInBO> signInList = signInMapper.selectPersonalSignInList(userId, yearAndMonth);
        User user = userMapper.selectById(userId);
        List<SignIn> before7Clock = signInMapper.selectAllBetween0And7AndUserId(yearAndMonth, userId);
        if (!CollectionUtils.isEmpty(signInList)) {
            for (SignInBO signInBO : signInList) {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signInBO, resDTO);
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signInBO.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE, 1);
                String prefix = sdf2.format(dateList.get(0));
                String nextPrefix = sdf2.format(calendar.getTime());
                Date zero = null;
                Date seven = null;
                Date nextSeven = null;
                Date fifteen = null;
                try {
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 05:00:00");
                    seven = sdf.parse(prefix + " 05:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time -> {
                    try {
                        return time.after(sdf.parse(prefix + " 05:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)) {
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<Date> between0And7Dates = new ArrayList<>();
                    for (SignIn signIn : before7Clock) {
                        if ((signIn.getCheckTime().after(zero)) && (signIn.getCheckTime().before(nextSeven))) {
                            between0And7Dates.add(signIn.getCheckTime());
                        }
                    }
                    if (!CollectionUtils.isEmpty(between0And7Dates)) {
                        resDTO.setCheckOutTime(between0And7Dates.get(between0And7Dates.size() - 1));
                        resDTO.setIsWorkToNextDay(1);
                    } else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                }
                //只有一条打卡记录
                else if (dateList.size() == 1) {
                    List<Date> between0And7Dates = new ArrayList<>();
                    for (SignIn signIn : before7Clock) {
                        if ((signIn.getCheckTime().after(zero)) && (signIn.getCheckTime().before(nextSeven))) {
                            between0And7Dates.add(signIn.getCheckTime());
                        }
                    }
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(between0And7Dates)) {
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        } else {
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            resDTO.setIsWorkToNextDay(0);
                        }
                    }
                    //第二天有7点前的打卡记录,取最后时间作为下班时间
                    else {
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            resDTO.setIsForget(0);
                        } else {
                            resDTO.setCheckInTime(null);
                            resDTO.setIsForget(1);
                        }
                        resDTO.setCheckOutTime(between0And7Dates.get(between0And7Dates.size() - 1));
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = null;
                        if (resDTO.getIsForget() == 0) {
                            workTimeMillis = resDTO.getCheckOutTime().getTime() - resDTO.getCheckInTime().getTime();
                        }
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                //2条或以上
                else {
                    List<Date> between0And7Dates = new ArrayList<>();
                    for (SignIn signIn : before7Clock) {
                        if ((signIn.getCheckTime().after(zero)) && (signIn.getCheckTime().before(nextSeven))) {
                            between0And7Dates.add(signIn.getCheckTime());
                        }
                    }
                    //第二天早于7点没有打卡记录
                    if (CollectionUtils.isEmpty(between0And7Dates)) {
                        resDTO.setCheckInTime(dateList.get(0));
                        resDTO.setCheckOutTime(dateList.get(dateList.size() - 1));
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(0);
                        Long workTimeMillis = dateList.get(dateList.size() - 1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                    //第二天早于7点有打卡记录
                    else {
                        resDTO.setCheckInTime(dateList.get(0));
                        resDTO.setCheckOutTime(between0And7Dates.get(between0And7Dates.size() - 1));
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = between0And7Dates.get(between0And7Dates.size() - 1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                            Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                            resDTO.setEWorkTime(eWorkTime);
                        } else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                if (resDTO.getEWorkTime() != null) {
                    extraWorkTime += resDTO.getEWorkTime();
                } else {
                    extraWorkTime += 0L;
                }
            }
        }
        totalExtraHoursResDTO.setUserId(userId);
        totalExtraHoursResDTO.setUserName(user.getName());
        totalExtraHoursResDTO.setYearAndMonth(yearAndMonth);
        totalExtraHoursResDTO.setExtraTime(extraWorkTime);
        return totalExtraHoursResDTO;
    }

    /**
     * 按月导出考勤情况Excel
     *
     * @param yearAndMonth
     * @return
     */
    @Override
    public String excelSignInData(String yearAndMonth) {
        if (yearAndMonth == null || yearAndMonth.trim().equals("")) {
            throw new ZSYServiceException("请选择月份");
        }
        long time1 = System.currentTimeMillis();
        //查询给定年月的所有考勤记录
        List<SignInBO> signInBOS = signInMapper.selectAllSignInByMonth(yearAndMonth,ZSYTokenRequestContext.get().getOrgId());
        //查询给定年月的所有考勤人员
        List<User> signInUsers = signInMapper.selectCheckInUsers(yearAndMonth,ZSYTokenRequestContext.get().getOrgId());
        //查询给定年月每天0点到5点之间的考勤记录
        List<SignIn> before7Clock = signInMapper.selectAllBetween0And7(yearAndMonth,ZSYTokenRequestContext.get().getOrgId());
        List<Date> dates = signInMapper.selectDatesByYearAndMonth(yearAndMonth,ZSYTokenRequestContext.get().getOrgId());
        //根据用户id  获取对应的  userId--List<SignInBO>   的map
        Map<Long, List<SignInBO>> map = signInBOS.stream().collect(Collectors.groupingBy(SignInBO::getUserId));
        Map<Long, List<SignInResDTO>> resultMap = new HashMap<>();
        map.keySet().forEach(userId -> {
            List<SignInResDTO> signInResDTOS = new ArrayList<>();
            List<SignInBO> personalSignIns = map.get(userId);
            if (!CollectionUtils.isEmpty(personalSignIns)) {
                personalSignIns.forEach(personalSignInBo -> {
                    SignInResDTO resDTO = new SignInResDTO();
                    BeanUtils.copyProperties(personalSignInBo, resDTO);
                    List<User> users = signInUsers.stream().filter(user ->
                            user.getId().equals(userId)
                    ).distinct().collect(Collectors.toList());
                    resDTO.setUserName(users.get(0).getName());
                    List<String> list = Arrays.asList(personalSignInBo.getCheckTimeStr().split(","));
                    SimpleDateFormat timeSdf = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
                    List<Date> dateList = list.stream().map(str -> {
                        try {
                            return timeSdf.parse(str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        throw new ZSYServiceException("解析当天考勤时间集合失败");
                    }).sorted().collect(Collectors.toList());
                    resDTO.setCheckTimeList(dateList);
                    SimpleDateFormat dateSdf = new SimpleDateFormat(DateHelper.DATE_FORMAT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateList.get(0));
                    calendar.add(Calendar.DATE, 1);
                    String prefix = dateSdf.format(dateList.get(0));
                    String nextPrefix = dateSdf.format(calendar.getTime());
                    Date today7clock = null;
                    Date today15clock = null;
                    Date tomorrow0clock = null;
                    Date tomorrow7clock = null;
                    try {
                        today7clock = timeSdf.parse(prefix + " 05:00:00");
                        today15clock = timeSdf.parse(prefix + " 15:00:00");
                        tomorrow0clock = timeSdf.parse(nextPrefix + " 00:00:00");
                        tomorrow7clock = timeSdf.parse(nextPrefix + " 05:00:00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //过滤当天上午7点之前的打卡记录
                    dateList = dateList.stream().filter(time -> {
                        try {
                            return time.after(timeSdf.parse(prefix + " 05:00:00"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        throw new ZSYServiceException("解析当天考勤时间集合失败");
                    }).collect(Collectors.toList());
                    //过滤之后,打卡记录为0,则视为漏打卡
                    if (CollectionUtils.isEmpty(dateList)) {
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        resDTO.setCheckInTime(null);
                        //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                        List<Date> personalBefore7Clock = new ArrayList<>();
                        for (SignIn signIn : before7Clock) {
                            if ((signIn.getCheckTime().after(tomorrow0clock))
                                    && (signIn.getCheckTime().before(tomorrow7clock))
                                    && (signIn.getUserId().equals(userId))) {
                                personalBefore7Clock.add(signIn.getCheckTime());
                            }
                        }
                        if (!CollectionUtils.isEmpty(personalBefore7Clock)) {
                            resDTO.setCheckOutTime(personalBefore7Clock.get(personalBefore7Clock.size() - 1));

                        } else {
                            resDTO.setCheckOutTime(null);
                        }
                    }
                    //只有一条打卡记录
                    else if (dateList.size() == 1) {
                        List<Date> personalBefore7Clock = new ArrayList<>();
                        for (SignIn signIn : before7Clock) {
                            if ((signIn.getCheckTime().after(tomorrow0clock))
                                    && (signIn.getCheckTime().before(tomorrow7clock))
                                    && (signIn.getUserId().equals(userId))) {
                                personalBefore7Clock.add(signIn.getCheckTime());
                            }
                        }
                        //第二天没有7点前的打卡记录,则当天漏打卡
                        if (CollectionUtils.isEmpty(personalBefore7Clock)) {
                            resDTO.setIsForget(1);
                            resDTO.setWorkTime(null);
                            resDTO.setEWorkTime(null);
                            //如果时间在7:00--15:00之间,即为上班打卡
                            if (dateList.get(0).after(today7clock) && dateList.get(0).before(today15clock)) {
                                resDTO.setCheckInTime(dateList.get(0));
                                resDTO.setCheckOutTime(null);
                            } else {
                                resDTO.setCheckInTime(null);
                                resDTO.setCheckOutTime(dateList.get(0));

                            }
                        }
                        //第二天有7点前的打卡记录,取最后时间作为下班时间
                        else {
                            if (dateList.get(0).after(today7clock) && dateList.get(0).before(today15clock)) {
                                resDTO.setCheckInTime(dateList.get(0));
                                resDTO.setIsForget(0);
                            } else {
                                resDTO.setCheckInTime(null);
                                resDTO.setIsForget(1);
                            }
                            resDTO.setCheckOutTime(personalBefore7Clock.get(personalBefore7Clock.size() - 1));
                            Long workTimeMillis = null;
                            if (resDTO.getIsForget() == 0) {
                                workTimeMillis = resDTO.getCheckOutTime().getTime() - resDTO.getCheckInTime().getTime();
                            }
                            resDTO.setWorkTime(workTimeMillis);
                            //当工作时长大于10小时,计算为加班时间
                            if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                                Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                                resDTO.setEWorkTime(eWorkTime);
                            } else {
                                resDTO.setEWorkTime(null);
                            }
                        }
                    }
                    //2条或以上
                    else {
                        List<Date> personalBefore7Clock = new ArrayList<>();
                        for (SignIn signIn : before7Clock) {
                            if ((signIn.getCheckTime().after(tomorrow0clock))
                                    && (signIn.getCheckTime().before(tomorrow7clock))
                                    && (signIn.getUserId().equals(userId))) {
                                personalBefore7Clock.add(signIn.getCheckTime());
                            }
                        }
                        //第二天早于7点没有打卡记录
                        if (CollectionUtils.isEmpty(personalBefore7Clock)) {
                            resDTO.setCheckInTime(dateList.get(0));
                            resDTO.setCheckOutTime(dateList.get(dateList.size() - 1));
                            resDTO.setIsForget(0);
                            Long workTimeMillis = dateList.get(dateList.size() - 1).getTime()
                                    - dateList.get(0).getTime();
                            resDTO.setWorkTime(workTimeMillis);
                            //当工作时长大于10小时,计算为加班时间
                            if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                                Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                                resDTO.setEWorkTime(eWorkTime);
                            } else {
                                resDTO.setEWorkTime(null);
                            }
                        }
                        //第二天早于7点有打卡记录
                        else {
                            resDTO.setCheckInTime(dateList.get(0));
                            resDTO.setCheckOutTime(personalBefore7Clock.get(personalBefore7Clock.size() - 1));

                            resDTO.setIsForget(0);
                            Long workTimeMillis = personalBefore7Clock.get(personalBefore7Clock.size() - 1).getTime()
                                    - dateList.get(0).getTime();
                            resDTO.setWorkTime(workTimeMillis);
                            //当工作时长大于10小时,计算为加班时间
                            if (workTimeMillis != null && workTimeMillis > 9 * 60 * 60 * 1000) {
                                Long eWorkTime = workTimeMillis - (1000 * 60 * 60 * 9);
                                resDTO.setEWorkTime(eWorkTime);
                            } else {
                                resDTO.setEWorkTime(null);
                            }
                        }
                    }
                    resDTO.setDate(today7clock);
                    calendar.setTime(today7clock);
                    if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                        resDTO.setIsWeekend(1);
                    } else {
                        resDTO.setIsWeekend(0);
                    }
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    switch (dayOfWeek) {
                        case 1:
                            resDTO.setWeekday("星期日");
                            break;
                        case 2:
                            resDTO.setWeekday("星期一");
                            break;
                        case 3:
                            resDTO.setWeekday("星期二");
                            break;
                        case 4:
                            resDTO.setWeekday("星期三");
                            break;
                        case 5:
                            resDTO.setWeekday("星期四");
                            break;
                        case 6:
                            resDTO.setWeekday("星期五");
                            break;
                        case 7:
                            resDTO.setWeekday("星期六");
                            break;
                        default:
                            resDTO.setWeekday("");
                            break;
                    }
                    signInResDTOS.add(resDTO);
                });
            }
            if (signInResDTOS.size() < dates.size()) {
                int size = dates.size() - signInResDTOS.size();
                for (int a = 0; a < size; a++) {
                    Calendar calendar = Calendar.getInstance();

                    SignInResDTO resDTO = new SignInResDTO();
                    resDTO.setLessThanNine(0);
                    resDTO.setEWorkTime(null);
                    resDTO.setIsForget(1);
                    calendar.setTime(dates.get(dates.size() - a - 1));
                    if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
                        resDTO.setIsWeekend(1);
                    } else {
                        resDTO.setIsWeekend(0);
                    }
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    switch (dayOfWeek) {
                        case 1:
                            resDTO.setWeekday("星期日");
                            break;
                        case 2:
                            resDTO.setWeekday("星期一");
                            break;
                        case 3:
                            resDTO.setWeekday("星期二");
                            break;
                        case 4:
                            resDTO.setWeekday("星期三");
                            break;
                        case 5:
                            resDTO.setWeekday("星期四");
                            break;
                        case 6:
                            resDTO.setWeekday("星期五");
                            break;
                        case 7:
                            resDTO.setWeekday("星期六");
                            break;
                        default:
                            resDTO.setWeekday("");
                            break;
                    }
                    resDTO.setLeaveTime(null);
                    resDTO.setEWorkHours(null);
                    resDTO.setCanReCheck(null);
                    resDTO.setDate(dates.get(dates.size() - a - 1));
                    resDTO.setIsCheckInAfterTen(null);
                    resDTO.setIsCheckOutBeforeSix(null);
                    resDTO.setIsRecheckOut(null);
                    resDTO.setIsRecheckIn(null);
                    resDTO.setWorkTime(null);
                    resDTO.setIsWorkToNextDay(null);
                    resDTO.setCheckOutTime(null);
                    resDTO.setCheckInTime(null);
                    resDTO.setUserName("");
                    resDTO.setUserId(userId);
                    List<Date> checkTimes = new ArrayList<>();
                    checkTimes.add(dates.get(size - a - 1));
                    resDTO.setCheckTimeList(checkTimes);
                    signInResDTOS.add(resDTO);
                }
            }
            List<SignInResDTO> collect = signInResDTOS.stream().sorted(Comparator.comparing(SignInResDTO::getDate).reversed()).collect(Collectors.toList());
            resultMap.put(userId, collect);
        });
        long time2 = System.currentTimeMillis();
        String url = getSignInExcel(dates, resultMap, signInUsers, yearAndMonth);
        logger.info("查询数据耗时: {}ms", (time2 - time1));
        if (url == null) {
            throw new ZSYServiceException("当前月份无数据,请选择正确月份");
        }
        logger.info("导出时间: {}ms", (System.currentTimeMillis() - time2));
        return url;
    }

    /**
     * 考勤人员列表
     *
     * @return
     */
    @Override
    public List<SignInUser> getSignInUsers() {
        List<User> userList = signInMapper.selectEffectUsers(ZSYTokenRequestContext.get().getOrgId());
        List<SignInUser> signInUsers = new ArrayList<>();
        BeanUtils.copyProperties(userList, signInUsers);
        if (!CollectionUtils.isEmpty(userList)) {
            userList.stream().forEach(user -> {
                SignInUser signInUser = new SignInUser();
                signInUser.setUserId(user.getId());
                signInUser.setUserName(user.getName());
                signInUsers.add(signInUser);
            });
        }
        return signInUsers;
    }

    /**
     * 个人查看调休时间
     *
     * @param userId 用户id
     */
    @Override
    public List<RestHoursResDTO> getPersonalRestHours(Long userId) {
        List<RestHoursResDTO> list = new ArrayList<>();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ZSYServiceException("当前用户不存在");
        }
        RestHoursResDTO resDTO = new RestHoursResDTO();
        resDTO.setUserId(user.getId());
        resDTO.setUserName(user.getName());
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        BigDecimal totalRestHours = restHoursLogMapper.selectTotalRestHours(userId, instance.get(Calendar.YEAR));
        BigDecimal goneRestHours = restHoursLogMapper.selectUsedHours(userId, instance.get(Calendar.YEAR));
        resDTO.setTotalRestHours(totalRestHours);
        resDTO.setGoneRestHours(BigDecimal.ZERO.subtract(goneRestHours));
        resDTO.setLeftRestHours(totalRestHours.add(goneRestHours));
        resDTO.setEndDate(instance.get(Calendar.YEAR) + "-12-31");
        list.add(resDTO);
        return list;
    }

    /**
     * 个人查看调休修改日志
     *
     * @param reqDTO 条件
     * @author sch
     */
    @Override
    public PageInfo<RestHoursLogPageResDTO> getPersonalRestHoursLogPage(QueryRestHoursLogReqDTO reqDTO) {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        Page<UserRestHoursLog> restHoursLogs = restHoursLogMapper.selectUserRestHoursLogPage(userId);
        Page<RestHoursLogPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(restHoursLogs, page);
        if (!CollectionUtils.isEmpty(restHoursLogs)) {
            restHoursLogs.forEach(restHoursLog -> {
                RestHoursLogPageResDTO resDTO = new RestHoursLogPageResDTO();
                BeanUtils.copyProperties(restHoursLog, resDTO);
                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 管理员查看用户调休修改日志
     *
     * @param reqDTO 条件
     * @author sch
     */
    @Override
    public PageInfo<RestHoursLogPageResDTO> getUserRestHoursLogPage(QueryRestHoursLogReqDTO reqDTO) {
        Long userId = reqDTO.getUserId();
        if (userId == null) {
            throw new ZSYServiceException("用户id不能为空");
        }

        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        Page<UserRestHoursLog> restHoursLogs = restHoursLogMapper.selectUserRestHoursLogPage(userId);
        Page<RestHoursLogPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(restHoursLogs, page);
        if (!CollectionUtils.isEmpty(restHoursLogs)) {
            restHoursLogs.forEach(restHoursLog -> {
                SimpleDateFormat timeSDF = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);
                SimpleDateFormat dateSDF = new SimpleDateFormat(DateHelper.DATE_FORMAT);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                RestHoursLogPageResDTO resDTO = new RestHoursLogPageResDTO();
                BeanUtils.copyProperties(restHoursLog, resDTO);
                resDTO.setTypeName(ZSYRestHoursType.getName(restHoursLog.getType()));
                if (ZSYRestHoursType.LEAVE.getValue() == restHoursLog.getType()
                        && restHoursLog.getLeaveId() != null) {
                    UserLeave userLeave = userLeaveMapper.selectById(restHoursLog.getLeaveId());
                    if (userLeave != null) {
                        String leaveTimeStr = sdf.format(userLeave.getBeginTime()) + "  " + sdf.format(userLeave.getEndTime());
                        resDTO.setLeaveTimeStr(leaveTimeStr);
                    }
                } else if (ZSYRestHoursType.EWORK.getValue() == restHoursLog.getType()
                        && restHoursLog.getEwId() != null) {
                    ExtraWork extraWork = extraWorkMapper.selectById(restHoursLog.getEwId());
                    if (extraWork != null) {
                        String eworkTimeStr = sdf.format(extraWork.getBeginTime()) + "  " + sdf.format(extraWork.getEndTime());
                        resDTO.setEworkTimeStr(eworkTimeStr);
                    }
                } else if (ZSYRestHoursType.EXTRA.getValue() == restHoursLog.getType()) {

                    String format = dateSDF.format(restHoursLog.getRecordTime());
                    Date begin = null;
                    Date end = null;
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Date nextDate = calendar.getTime();
                    String nextDateStr = dateSDF.format(nextDate);
                    Date nextZero = null;
                    Date nextFive = null;
                    Date today15 = null;
                    try {
                        begin = timeSDF.parse(format + " 05:00:00");
                        end = timeSDF.parse(format + " 23:59:59");
                        nextZero = timeSDF.parse(nextDateStr + " 00:00:00");
                        nextFive = timeSDF.parse(nextDateStr + " 05:00:00");
                        today15 = timeSDF.parse(format + " 15:00:00");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date checkInTime = null;
                    Date checkOutTime = null;
                    List<SignIn> signIns = signInMapper.selectSignInByUserAndTimeRange(restHoursLog.getUserId(), begin, end);
                    if (!CollectionUtils.isEmpty(signIns)) {
                        if (today15.compareTo(signIns.get(0).getCheckTime()) > 0) {
                            checkInTime = signIns.get(0).getCheckTime();
                        }

                        List<SignIn> nextSignIgs = signInMapper.selectSignInByUserAndTimeRange(restHoursLog.getUserId(), nextZero, nextFive);
                        //如果第二天 7点之前有打卡,   第一条为  前一天的 下班卡
                        if (!CollectionUtils.isEmpty(nextSignIgs)) {
                            checkOutTime = nextSignIgs.get(0).getCheckTime();
                        } else {
                            //如果 今天的最后一条记录是再15点之后 就是 今天的下班卡
                            if (signIns.get(signIns.size() - 1).getCheckTime().compareTo(today15) > 0) {
                                checkOutTime = signIns.get(signIns.size() - 1).getCheckTime();
                            }
                        }
                    }
                    if (checkInTime != null && checkOutTime != null) {
                        String checkTimeStr = sdf.format(checkInTime) + "  " + sdf.format(checkOutTime);
                        resDTO.setCheckTimeStr(checkTimeStr);
                    }

                }
                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 查看所有人调休时间
     *
     * @param reqDTO 条件
     */
    @Override
    public List<UserRestHoursListResDTO> getUserRestHoursList(QueryUserRestHoursReqDTO reqDTO) {
        //查询用户剩余调休
        List<User> users = userMapper.selectUserRestHours(reqDTO.getJobRole(), reqDTO.getUserId(),ZSYTokenRequestContext.get().getOrgId());
        List<UserRestHoursListResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(users)) {
            users.forEach(user -> {
                UserRestHoursListResDTO resDTO = new UserRestHoursListResDTO();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                Integer year = calendar.get(Calendar.YEAR);
                if (reqDTO.getYear() != null) {
                    year = reqDTO.getYear();
                }
                String endDate = year + "-12-31 23:59:59";
                resDTO.setEndDate(endDate);
                //查询请假扣除
                BigDecimal goneRestHours = restHoursLogMapper.selectUsedHours(user.getId(), year);
                //查询总调休
                BigDecimal totalRestHours = restHoursLogMapper.selectTotalRestHours(user.getId(), year);
                resDTO.setLeftRestHours(totalRestHours.add(goneRestHours));
                resDTO.setGoneRestHours(BigDecimal.ZERO.subtract(goneRestHours));
                resDTO.setTotalRestHours(totalRestHours);
                resDTO.setUserId(user.getId());
                resDTO.setUserName(user.getName());
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 手动新增调休
     *
     * @param reqDTO 参数
     */
    @Override
    @Transactional
    public void addUserRestHoursLog(AddUserRestHourLogReqDTO reqDTO) {
        User user = userMapper.selectById(reqDTO.getUserId());
        if (user == null) {
            throw new ZSYServiceException("当前用户不存在");
        }
        //修改用户剩余调休
        user.setRestHours(user.getRestHours().add(reqDTO.getRestHour()));
        userMapper.updateSelectiveById(user);

        //创建日志
        UserRestHoursLog userRestHoursLog = new UserRestHoursLog();
        userRestHoursLog.setId(snowFlakeIDHelper.nextId());
        userRestHoursLog.setRecordTime(reqDTO.getRecordTime());
        userRestHoursLog.setCreateTime(new Date());
        userRestHoursLog.setContent(reqDTO.getContent().trim());
        userRestHoursLog.setRestHours(reqDTO.getRestHour());
        userRestHoursLog.setType(ZSYRestHoursType.MANUAL.getValue());
        userRestHoursLog.setUserId(user.getId());
        userRestHoursLog.setUserName(user.getName());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reqDTO.getRecordTime());
        int year = calendar.get(Calendar.YEAR);
        userRestHoursLog.setYear(year);
        userRestHoursLog.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        restHoursLogMapper.insert(userRestHoursLog);
    }

    /**
     * 更新2019年10月的请假和加班产生的调休变化
     */
    @Override
    @Transactional
    public void updateLeaveAndEWork(LeaveAndEWorkReqDTO reqDTO) {
        //查询10月份之间  审核通过的请假
        String beginStr = reqDTO.getBeginTime();
        String endStr = reqDTO.getEndTime();
        if (beginStr == null || beginStr.trim().equals("")) {
            throw new ZSYServiceException("请选择开始时间");
        }
        if (endStr == null || endStr.trim().equals("")) {
            throw new ZSYServiceException("请选择截止时间");
        }
        List<UserRestHoursLog> userRestHoursLogList = new ArrayList<>();
        List<UserLeave> leaveList = userLeaveMapper.selectListByTime(beginStr, endStr,ZSYTokenRequestContext.get().getOrgId());
        if (!CollectionUtils.isEmpty(leaveList)) {
            for (UserLeave userLeave : leaveList) {
                User user = userMapper.selectById(userLeave.getUserId());
                UserRestHoursLog restHoursLog = new UserRestHoursLog();
                restHoursLog.setId(snowFlakeIDHelper.nextId());
                restHoursLog.setUserId(userLeave.getUserId());
                restHoursLog.setUserName(user.getName());
                restHoursLog.setLeaveId(userLeave.getId());
                restHoursLog.setRestHours(BigDecimal.ZERO.subtract(userLeave.getHours()));
                restHoursLog.setType(ZSYRestHoursType.LEAVE.getValue());
                restHoursLog.setContent(userLeave.getDescription());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(userLeave.getBeginTime());
                restHoursLog.setYear(calendar.get(Calendar.YEAR));
                restHoursLog.setRecordTime(userLeave.getBeginTime());
                restHoursLog.setCreateTime(new Date());
                userRestHoursLogList.add(restHoursLog);
            }
        }
        //查询10月份之间  审核通过的加班申请
        List<ExtraWork> extraWorkList = extraWorkMapper.selectListByTime(beginStr, endStr,ZSYTokenRequestContext.get().getOrgId());
        if (!CollectionUtils.isEmpty(extraWorkList)) {
            for (ExtraWork extraWork : extraWorkList) {
                User user = userMapper.selectById(extraWork.getUserId());
                UserRestHoursLog restHoursLog = new UserRestHoursLog();
                restHoursLog.setId(snowFlakeIDHelper.nextId());
                restHoursLog.setUserId(extraWork.getUserId());
                restHoursLog.setUserName(user.getName());
                restHoursLog.setEwId(extraWork.getId());
                restHoursLog.setRestHours(BigDecimal.valueOf(extraWork.getWorkHours()));
                restHoursLog.setType(ZSYRestHoursType.EWORK.getValue());
                restHoursLog.setContent(extraWork.getReason());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(extraWork.getBeginTime());
                restHoursLog.setYear(calendar.get(Calendar.YEAR));
                restHoursLog.setRecordTime(extraWork.getBeginTime());
                restHoursLog.setCreateTime(new Date());
                userRestHoursLogList.add(restHoursLog);
            }
        }

        if (!CollectionUtils.isEmpty(userRestHoursLogList)) {
            List<UserRestHoursLog> collect = userRestHoursLogList.stream().map(x -> {
                UserRestHoursLog userRestHoursLog = new UserRestHoursLog();
                BeanUtils.copyProperties(x, userRestHoursLog);
                userRestHoursLog.setOrgId(ZSYTokenRequestContext.get().getOrgId());
                return userRestHoursLog;
            }).collect(Collectors.toList());
            restHoursLogMapper.insertBatch(collect);
        }
    }

    /**
     * 删除调休日志
     *
     * @param logId 日志id
     */
    @Override
    @Transactional
    public void deleteRestHoursLog(Long logId) {
        UserRestHoursLog restHoursLog = restHoursLogMapper.selectById(logId);
        if (restHoursLog == null) {
            throw new ZSYServiceException("当前调休记录不存在,请检查");
        }
        restHoursLogMapper.deleteById(logId);
    }

    /**
     * 编辑调休日志
     *
     * @param reqDTO 参数
     */
    @Override
    @Transactional
    public void editUserRestHoursLog(EditUserRestHourLogReqDTO reqDTO) {
        UserRestHoursLog restHoursLog = restHoursLogMapper.selectById(reqDTO.getLogId());
        if (restHoursLog == null) {
            throw new ZSYServiceException("当前调休记录不存在,请检查");
        }
        restHoursLog.setRestHours(reqDTO.getRestHour());
        restHoursLog.setRecordTime(reqDTO.getRecordTime());
        restHoursLog.setContent(reqDTO.getContent().trim());
        if (restHoursLogMapper.updateById(restHoursLog) == 0) {
            throw new ZSYServiceException("修改调休日志失败");
        }
    }

    /**
     * 分页查看原始考勤记录
     *
     * @param reqDTO 参数
     * @author sch
     */
    @Override
    public PageInfo<SignInOriginResDTO> getSignInOriginPage(SignInReqDTO reqDTO) {
        reqDTO.setOrgId(ZSYTokenRequestContext.get().getOrgId());
        startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), 30);
        Page<SignInOriginBO> signInOriginBOS = signInMapper.selectSignInOriginBOPage(reqDTO);
        Page<SignInOriginResDTO> page = new Page<>();
        BeanUtils.copyProperties(signInOriginBOS, page);
        if (!CollectionUtils.isEmpty(signInOriginBOS)) {
            signInOriginBOS.forEach(signInOriginBO -> {
                SignInOriginResDTO resDTO = new SignInOriginResDTO();
                BeanUtils.copyProperties(signInOriginBO, resDTO);
                resDTO.setTypeName(ZSYSignInType.getName(signInOriginBO.getType()));
                page.add(resDTO);
            });
        }
        return new PageInfo<>(page);
    }

    /**
     * 编辑原始考勤记录
     *
     * @param reqDTO 参数
     * @author sch
     */
    @Override
    @Transactional
    public void editSignIn(EditSignInReqDTO reqDTO) {
        SignIn signIn = signInMapper.selectSignInById(reqDTO.getId());
        if (signIn == null) {
            throw new ZSYServiceException("当前记录不存在");
        }
        signIn.setCheckTime(reqDTO.getCheckTime());
        signInMapper.updateSignIn(signIn);
    }

    /**
     * 删除原始考勤
     *
     * @param id id
     */
    @Override
    @Transactional
    public void deleteSignIn(Long id) {
        SignIn signIn = signInMapper.selectSignInById(id);
        if (signIn == null) {
            throw new ZSYServiceException("当前记录不存在");
        }
        signInMapper.deleteSignInById(id);
    }

    @Override
    public List<SignInUser> getSignInUsersGroup() {


        List<SignInUser> userList = new ArrayList<>();
        if (ZSYTokenRequestContext.get().getUserRole().intValue() == ZSYUserRole.ADMINISTRATOR.getValue()) {
            List<User> users = signInMapper.selectEffectUsers(ZSYTokenRequestContext.get().getOrgId());
            for (User user : users) {
                SignInUser signInUser = new SignInUser();
                signInUser.setUserId(user.getId());
                signInUser.setUserName(user.getName());
                userList.add(signInUser);
            }
            return userList;
        }

        Long userId = ZSYTokenRequestContext.get().getUserId();
        List<WorkGroup> workGroups = workGroupMapper.selectByLeaderId(userId,ZSYTokenRequestContext.get().getOrgId());
        if (!CollectionUtils.isEmpty(workGroups)) {
            List<Long> groupList = workGroups.stream().map(WorkGroup::getId).distinct().collect(Collectors.toList());
            List<UserGroup> userGroups = userGroupMapper.selectByGroupIds(groupList,ZSYTokenRequestContext.get().getOrgId());
            List<Long> userIds = userGroups.stream().map(UserGroup::getUserId).distinct().collect(Collectors.toList());
            List<User> users = signInMapper.selectEffectUsersGroup(userIds);
            for (User user : users) {
                SignInUser signInUser = new SignInUser();
                signInUser.setUserId(user.getId());
                signInUser.setUserName(user.getName());
                userList.add(signInUser);
            }
        }
        return userList;
    }

    /**
     * 导出考勤记录Excel
     *
     * @param dates
     * @param map
     * @return
     */
    private String getSignInExcel(List<Date> dates, Map<Long, List<SignInResDTO>> map, List<User> userList, String yearAndMonth) {
        if (!CollectionUtils.isEmpty(dates) && !CollectionUtils.isEmpty(map.values())) {
            //设置表头
            List<String> headers = new ArrayList<>();
            List<String> weekdays = new ArrayList<>();
            weekdays.add("姓名");
            weekdays.add("上班类型");
            headers.add("求和项");
            headers.add("日期");
            SimpleDateFormat sdf = new SimpleDateFormat(DateHelper.DATE_FORMAT);
            SimpleDateFormat fileNameSdf = new SimpleDateFormat("yyyy年MM月");
            for (Date date : dates) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String weekday = "";
                switch (dayOfWeek) {
                    case 1:
                        weekday = "星期日";
                        break;
                    case 2:
                        weekday = "星期一";
                        break;
                    case 3:
                        weekday = "星期二";
                        break;
                    case 4:
                        weekday = "星期三";
                        break;
                    case 5:
                        weekday = "星期四";
                        break;
                    case 6:
                        weekday = "星期五";
                        break;
                    case 7:
                        weekday = "星期六";
                        break;
                    default:
                        weekday = "";
                        break;
                }
                weekdays.add(weekday);
                headers.add(sdf.format(date));
            }
            headers.add("加班总时长");
            headers.add("加班天数");
            weekdays.add("");
            weekdays.add("");

            //设置文件名
            String fileName = fileNameSdf.format(dates.get(0)) + "考勤记录" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";

            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                 HSSFWorkbook workbook = new HSSFWorkbook()) {
                //创建sheet
                HSSFSheet sheet = workbook.createSheet("考勤记录");
                //设置列宽
                sheet.setDefaultColumnWidth(13);
                //创建行
                HSSFRow row1 = sheet.createRow(0);
                HSSFRow row2 = sheet.createRow(1);
                HSSFRow row3 = sheet.createRow(2);
                HSSFRow row4 = sheet.createRow(3);
                HSSFRow row5;
                //创建样式
                HSSFCellStyle titleStyle = workbook.createCellStyle();
                //设置样式
                titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                titleStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                titleStyle.setAlignment(HorizontalAlignment.LEFT);
                //创建字体
                HSSFFont font = workbook.createFont();
                //设置字体
                font.setFontHeightInPoints((short) 12);
                font.setBold(true);
                font.setFontName("宋体");
                titleStyle.setFont(font);

                //创建样式
                HSSFCellStyle titleStyle2 = workbook.createCellStyle();
                //设置样式
                titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                titleStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                titleStyle.setAlignment(HorizontalAlignment.LEFT);
                //创建字体
                HSSFFont titleFont = workbook.createFont();
                //设置字体
                titleFont.setFontHeightInPoints((short) 12);
                titleFont.setBold(true);
                titleFont.setColor(IndexedColors.RED.index);
                titleFont.setFontName("宋体");
                titleStyle2.setFont(titleFont);

                HSSFCell cell = null;
                HSSFCell cell2 = null;
                //创建标题
                cell = row1.createCell(0);
                cell2 = row2.createCell(0);
                cell.setCellValue("上海互教信息技术中心" + yearAndMonth + "月考勤记录明细");
                cell.setCellStyle(titleStyle);
                cell2.setCellValue("（黄色填充单元格标识代表上/下班未打卡（包括漏打卡，调休/请假等情况以请假单为准），粉色填充单元格标识代表加班（工作时长超过10h（含10h算加班）），绿色填充单元格标识代表有请小时假/半天假）");
                cell2.setCellStyle(titleStyle2);
                CellRangeAddress region = new CellRangeAddress(0, 0, 0, dates.size() + 1);
                CellRangeAddress region2 = new CellRangeAddress(1, 1, 0, dates.size() + 1);
                sheet.addMergedRegion(region);
                sheet.addMergedRegion(region2);
                //创建样式(第二三行样式)
                HSSFCellStyle style1 = workbook.createCellStyle();
                //设置样式
                style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style1.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ROYAL_BLUE.getIndex());
                style1.setAlignment(HorizontalAlignment.RIGHT);
                //创建字体
                HSSFFont font1 = workbook.createFont();
                //设置字体
                font1.setFontHeightInPoints((short) 11);
                font1.setFontName("宋体");
                style1.setFont(font1);


                //创建样式
                HSSFCellStyle style5 = workbook.createCellStyle();
                //设置样式
                style5.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style5.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIME.getIndex());
                style5.setAlignment(HorizontalAlignment.RIGHT);
                //创建字体
                HSSFFont font5 = workbook.createFont();
                //设置字体
                font5.setFontHeightInPoints((short) 11);
                font5.setFontName("宋体");
                style5.setFont(font5);

                for (int i = 0; i < headers.size(); i++) {
                    HSSFCell row3Cell = row3.createCell(i);
                    row3Cell.setCellValue(headers.get(i));
                    row3Cell.setCellStyle(style1);
                    HSSFCell row4Cell = row4.createCell(i);
                    row4Cell.setCellValue(weekdays.get(i));
                    row4Cell.setCellStyle(style1);
                    if (weekdays.get(i).contains("星期六") || weekdays.get(i).contains("星期日")) {
                        row3Cell.setCellStyle(style5);
                        row4Cell.setCellStyle(style5);
                    }
                }
                int num = 0;
                //创建样式
                HSSFCellStyle style2 = workbook.createCellStyle();
                //设置样式
                style2.setFillForegroundColor(HSSFColor.HSSFColorPredefined.PALE_BLUE.getIndex());
                style2.setAlignment(HorizontalAlignment.RIGHT);
                //创建字体
                HSSFFont font2 = workbook.createFont();
                //设置字体
                font2.setFontHeightInPoints((short) 11);
                font2.setFontName("宋体");
                style2.setFont(font2);

                //创建样式
                HSSFCellStyle style3 = workbook.createCellStyle();
                //设置样式
                style3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style3.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex());
                style3.setAlignment(HorizontalAlignment.RIGHT);
                //创建字体
                HSSFFont font3 = workbook.createFont();
                //设置字体
                font3.setFontHeightInPoints((short) 11);
                font3.setFontName("宋体");
                style3.setFont(font3);

                //创建样式
                HSSFCellStyle style4 = workbook.createCellStyle();
                //设置样式
                style4.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style4.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
                style4.setAlignment(HorizontalAlignment.RIGHT);
                //创建字体
                HSSFFont font4 = workbook.createFont();
                //设置字体
                font4.setFontHeightInPoints((short) 11);
                font4.setFontName("宋体");
                style4.setFont(font4);

                //创建样式
                HSSFCellStyle style6 = workbook.createCellStyle();
                //设置样式
                style6.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style6.setFillForegroundColor(HSSFColor.HSSFColorPredefined.CORAL.getIndex());
                style6.setAlignment(HorizontalAlignment.RIGHT);
                //创建字体
                HSSFFont font6 = workbook.createFont();
                //设置字体
                font6.setFontHeightInPoints((short) 11);
                font6.setFontName("宋体");
                style6.setFont(font6);

                //创建样式
                HSSFCellStyle style7 = workbook.createCellStyle();
                //设置样式
                style7.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style7.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                style7.setAlignment(HorizontalAlignment.RIGHT);
                //创建字体
                HSSFFont font7 = workbook.createFont();
                //设置字体
                font7.setFontHeightInPoints((short) 11);
                font7.setFontName("宋体");
                style7.setFont(font7);

                SimpleDateFormat dateFormat = new SimpleDateFormat(DateHelper.DATE_FORMAT);
                SimpleDateFormat timeSDF = new SimpleDateFormat(DateHelper.DATETIME_FORMAT);

                for (User user : userList) {
                    List<SignInResDTO> signInResDTOS = map.get(user.getId());
                    row1 = sheet.createRow(num + 4);
                    row2 = sheet.createRow(num + 5);
                    row3 = sheet.createRow(num + 6);
                    row4 = sheet.createRow(num + 7);
                    row5 = sheet.createRow(num + 8);
                    HSSFCell row1Cell0 = row1.createCell(0);
                    HSSFCell row2Cell0 = row2.createCell(0);
                    HSSFCell row3Cell0 = row3.createCell(0);
                    HSSFCell row4Cell0 = row4.createCell(0);
                    HSSFCell row5Cell0 = row5.createCell(0);
                    row1Cell0.setCellValue(user.getName());
                    row1Cell0.setCellStyle(style2);
                    row2Cell0.setCellValue("");
                    row2Cell0.setCellStyle(style2);
                    row3Cell0.setCellValue(user.getName());
                    row3Cell0.setCellStyle(style3);
                    row4Cell0.setCellValue(user.getName());
                    row4Cell0.setCellStyle(style7);
                    row5Cell0.setCellValue("");
                    row5Cell0.setCellStyle(style7);

                    HSSFCell row1Cell1 = row1.createCell(1);
                    HSSFCell row2Cell1 = row2.createCell(1);
                    HSSFCell row3Cell1 = row3.createCell(1);
                    HSSFCell row4Cell1 = row4.createCell(1);
                    HSSFCell row5Cell1 = row5.createCell(1);
                    row1Cell1.setCellValue("上班");
                    row1Cell1.setCellStyle(style7);
                    row2Cell1.setCellValue("下班");
                    row2Cell1.setCellStyle(style7);
                    row3Cell1.setCellValue("上班时长");
                    row3Cell1.setCellStyle(style3);
                    row4Cell1.setCellValue("加班时长");
                    row4Cell1.setCellStyle(style7);
                    row5Cell1.setCellValue("");
                    row5Cell1.setCellStyle(style7);

                    double totalEWorkHours = 0;
                    int eWorkDays = 0;
                    for (int i = 0; i < dates.size(); i++) {
                        HSSFCell row1Celli = row1.createCell(i + 2);
                        HSSFCell row2Celli = row2.createCell(i + 2);
                        HSSFCell row3Celli = row3.createCell(i + 2);
                        HSSFCell row4Celli = row4.createCell(i + 2);
                        HSSFCell row5Celli = row5.createCell(i + 2);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        Date checkInTime1 = signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckInTime();
                        row1Celli.setCellValue(signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckInTime() == null ? "" : timeFormat.format(signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckInTime()));

                        row1Celli.setCellStyle(style7);
                        if (checkInTime1 != null) {
                            String dateStr = dateFormat.format(checkInTime1);
                            Date today930 = timeSDF.parse(dateStr + " 09:30:00");
                            if (checkInTime1.compareTo(today930) > 0) {
                                row1Celli.setCellStyle(style4);
                            }
                        }
                        Date checkOutTime1 = signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckOutTime();
                        row2Celli.setCellValue(signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckOutTime() == null ? "" : timeFormat.format(signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckOutTime()));
                        row2Celli.setCellStyle(style7);
                        if (checkOutTime1 != null) {
                            String dateStr = dateFormat.format(checkOutTime1);
                            Date today1830 = timeSDF.parse(dateStr + " 18:30:00");
                            if (checkOutTime1.compareTo(today1830) < 0) {
                                row2Celli.setCellStyle(style4);
                            }
                        }
                        row3Celli.setCellValue(signInResDTOS.get(signInResDTOS.size() - 1 - i).getWorkTime() == null ? "" : getTime(signInResDTOS.get(signInResDTOS.size() - 1 - i).getWorkTime()));
                        row3Celli.setCellStyle(style3);
                        Long eWorkTime = signInResDTOS.get(signInResDTOS.size() - 1 - i).getEWorkTime();
                        double eWorkHour = 0;
                        if (eWorkTime != null) {
                            Long hour = eWorkTime / 1000 / 60 / 60;
                            eWorkHour = hour;
                            totalEWorkHours = totalEWorkHours + eWorkHour;
                            row4Celli.setCellStyle(style7);
                            if (eWorkHour >= 1) {
                                eWorkDays += 1;
                                row4Celli.setCellStyle(style6);
                                row3Celli.setCellStyle(style6);
                            }

                            row4Celli.setCellValue(eWorkHour);

                        } else {
                            row4Celli.setCellValue("");
                            row4Celli.setCellStyle(style7);
                        }
                        row5Celli.setCellValue("");
                        row5Celli.setCellStyle(style7);
                        if (signInResDTOS.get(signInResDTOS.size() - 1 - i).getIsWeekend() == 1) {
                            row1Celli.setCellStyle(style5);
                            row2Celli.setCellStyle(style5);
                            row3Celli.setCellStyle(style5);
                            row4Celli.setCellStyle(style5);
                            row5Celli.setCellStyle(style5);
                            Long workTime = 0L;
                            Date checkInTime = signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckInTime();
                            Date checkOutTime = signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckOutTime();
                            if (checkInTime != null && checkOutTime != null) {
                                workTime = checkOutTime.getTime() - checkInTime.getTime();
                                Long hour = workTime / 1000 / 60 / 60;
                                eWorkHour = hour;
                                row4Celli.setCellValue(eWorkHour);
                            } else {
                                row3Celli.setCellValue("00:00:00");
                            }
                        } else {
                            if (signInResDTOS.get(signInResDTOS.size() - 1 - i).getLeaveTime() != null && signInResDTOS.get(signInResDTOS.size() - 1 - i).getLeaveTime() != BigDecimal.ZERO) {
                                row1Celli.setCellStyle(style4);
                                row2Celli.setCellStyle(style4);
                            } else {
                                if (signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckInTime() == null) {
                                    row1Celli.setCellValue("漏打卡");
                                    row1Celli.setCellStyle(style4);
                                }

                                if (signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckOutTime() == null) {
                                    row2Celli.setCellValue("漏打卡");
                                    row2Celli.setCellStyle(style4);
                                }
                            }
                        }
                    }
                    HSSFCell totalEWorkHoursRow = row4.createCell(dates.size() + 2);
                    HSSFCell totalEWorkDaysRow = row4.createCell(dates.size() + 3);
                    totalEWorkHoursRow.setCellValue(totalEWorkHours);
                    totalEWorkDaysRow.setCellValue(eWorkDays);
                    num += 5;
                }
                workbook.write(os);
                return ZSYQinuHelper.uploadToUfile(os.toByteArray(), fileName, "application/vnd.ms-excel", uFileProperties);
            } catch (Exception e) {
                throw new ZSYServiceException("导出表失败" + e);
            }
        }
        return null;
    }

    public String getTime(Long timeMillis) {
        String hours = addZero(timeMillis / 1000 / 60 / 60);
        String mins = addZero(timeMillis / 1000 / 60 % 60);
        String secs = addZero(timeMillis / 1000 % 60);
        return (hours + ":" + mins + ":" + secs);
    }

    private String addZero(long time) {
        if (time < 10) {
            return ("0" + time);
        } else {
            return time + "";
        }
    }

    /**
     * 个人查询补打卡申请
     *
     * @param status
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<ResignInResDTO> getPersonalByStatus(Integer status, Integer pageNum) {
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ResignIn> resignIns = signInMapper.selectPersonalResignInPage(ZSYTokenRequestContext.get().getUserId(), status);
        Page<ResignInResDTO> resignInResDTOS = new Page<>();
        BeanUtils.copyProperties(resignIns, resignInResDTOS);
        if (!CollectionUtils.isEmpty(resignIns)) {
            resignIns.stream().forEach(resignIn -> {
                ResignInResDTO resignInResDTO = new ResignInResDTO();
                BeanUtils.copyProperties(resignIn, resignInResDTO);
                User user = userMapper.selectById(resignIn.getUserId());
                resignInResDTO.setUserName(user.getName());
                resignInResDTO.setAvatarUrl(user.getAvatarUrl());
                resignInResDTOS.add(resignInResDTO);
            });
        }
        return new PageInfo<>(resignInResDTOS);
    }

    /**
     * 管理员查询补打卡申请
     *
     * @param status
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<ResignInResDTO> getResignInByStatus(Integer status, Integer pageNum) {
        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<ResignIn> resignIns = signInMapper.selectResignInPage(status,ZSYTokenRequestContext.get().getOrgId());
        Page<ResignInResDTO> resignInResDTOS = new Page<>();
        BeanUtils.copyProperties(resignIns, resignInResDTOS);
        if (!CollectionUtils.isEmpty(resignIns)) {
            resignIns.stream().forEach(resignIn -> {
                ResignInResDTO resignInResDTO = new ResignInResDTO();
                BeanUtils.copyProperties(resignIn, resignInResDTO);
                User user = userMapper.selectById(resignIn.getUserId());
                resignInResDTO.setUserName(user.getName());
                resignInResDTO.setAvatarUrl(user.getAvatarUrl());
                resignInResDTOS.add(resignInResDTO);
            });
        }
        return new PageInfo<>(resignInResDTOS);
    }


    /**
     * 获取上传文件后缀名
     *
     * @param uploadName
     * @return
     */
    public String getUploadSuffix(String uploadName) {
        return uploadName.substring(uploadName.lastIndexOf(".") + 1);
    }

    //判断是否是excel
    public static boolean isExcel(String url) {
        Pattern p = Pattern.compile("\\.(xls|XLS)");
        Matcher m = p.matcher(url);
        return m.find();
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(@RequestParam MultipartFile file) throws Exception {

        File toFile = null;
        if (file != null && file.getSize() > 0) {
            InputStream ins;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    public static void inputStreamToFile(InputStream ins, File file) {
        try (OutputStream os = new FileOutputStream(file);) {
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

}
