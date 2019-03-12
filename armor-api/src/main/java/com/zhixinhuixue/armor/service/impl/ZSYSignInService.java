package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYExtraWorkMapper;
import com.zhixinhuixue.armor.dao.IZSYSignInMapper;
import com.zhixinhuixue.armor.dao.IZSYUserLeaveMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.helper.ZSYQinuHelper;
import com.zhixinhuixue.armor.model.bo.SignInBO;
import com.zhixinhuixue.armor.model.dto.request.ResignInReqDTO;
import com.zhixinhuixue.armor.model.dto.request.SignInReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYSignInService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.ZSYQinuOssProperty;
import com.zhixinhuixue.armor.source.enums.ZSYSignInType;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.CheckedOutputStream;

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

    /**
     * 上传 user-sort 文件到库
     * @param uploadFile
     */
    @Override
    public void addUserSortToMysql(MultipartFile uploadFile) {
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isExcel(suffix)){
            throw new ZSYServiceException("只能上传Excel");
        }
        try {
            File file = multipartFileToFile(uploadFile);
            ZSYSignInService obj = new ZSYSignInService();
            List excelList = obj.readExcel(file);
            List<UserSort> userSortList = new ArrayList<>();
            for (int i = 0; i < excelList.size(); i++) {
                List list = (List) excelList.get(i);
                UserSort userSort = new UserSort();
                userSort.setSort(Integer.valueOf((String) list.get(0)));
                userSort.setUserName((String)list.get(1));
                userSortList.add(userSort);
            }
            signInMapper.deleteUserSort();
            if (signInMapper.addUserSortBatch(userSortList) == 0){
                throw new ZSYServiceException("批量新增 user-sort 失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZSYServiceException("文件解析失败,请检查是否为.xls格式的花名册文件,或者文件是否带密码");
        }
    }

    /**
     * 上传.dat的打卡记录文件到库
     * @param uploadFile
     */
    @Override
    @Transactional
    public void uploadToMysql(MultipartFile uploadFile) {
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!".dat".equals(suffix)){
            throw new ZSYServiceException("文件必须是.dat后缀");
        }
        BufferedReader br = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(uploadFile.getInputStream());
            br = new BufferedReader(inputStreamReader);
            String str;
            List<SignIn> signIns = new ArrayList<>();
            long time1 = System.currentTimeMillis();
            List<User> users = signInMapper.selectCheckInUsers();
            SignIn lastRecord = signInMapper.selectSingInLastRecord();
            SignIn firstRecord = signInMapper.selectSingInFirstRecord();
            //如果有最后一条记录,且当前这天的考勤时间是最后一条之后的
            if (lastRecord != null && firstRecord != null){
                while((str=br.readLine())!=null){//按行读取
                    String sort = str.substring(str.lastIndexOf("-")-12,str.lastIndexOf("-")-8);
                    String time = str.substring(10, 29);
                    String trim = sort.trim();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date checkTime = sdf.parse(time);
                    if (checkTime.after(lastRecord.getCheckTime()) || checkTime.before(firstRecord.getCheckTime())){
                        User user = signInMapper.selectUserBySort(Integer.valueOf(trim));
                        if (user != null){
                            SignIn signIn = new SignIn();
                            signIn.setId(snowFlakeIDHelper.nextId());
                            signIn.setUserId(user.getId());
                            signIn.setCheckTime(checkTime);
                            signIn.setType(ZSYSignInType.NORMAL_SIGN.getValue());
                            signIns.add(signIn);
                        }
                    }
                }
            }
            //如果没有最后一条记录,则全部都是新的
            else {
                while((str=br.readLine())!=null){//按行读取
                    String sort = str.substring(str.lastIndexOf("-")-12,str.lastIndexOf("-")-8);
                    String time = str.substring(10, 29);
                    String trim = sort.trim();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date checkTime = sdf.parse(time);
                    User user = signInMapper.selectUserBySort(Integer.valueOf(trim));
                    if (user != null){
                        SignIn signIn = new SignIn();
                        signIn.setId(snowFlakeIDHelper.nextId());
                        signIn.setUserId(user.getId());
                        signIn.setCheckTime(checkTime);
                        signIn.setType(ZSYSignInType.NORMAL_SIGN.getValue());
                        signIns.add(signIn);
                    }
                }
            }
            System.out.println("signIns的长度: " + signIns.size());
            long time2 = System.currentTimeMillis();
            System.out.println("组装时间: "+ (time2 - time1) + "ms");
            long time3 = System.currentTimeMillis();
            if (!CollectionUtils.isEmpty(signIns) && signInMapper.insertSignInBatch(signIns) == 0){
                throw new ZSYServiceException("批量导入考勤打卡记录失败");
            }
            System.out.println("插入时间: " + (System.currentTimeMillis() - time3) + "ms");
            long time4 = System.currentTimeMillis();
            if (!CollectionUtils.isEmpty(signIns)){
                List<String> daysBetweenTwoDate =
                        DateHelper.getDaysBetweenTwoDate(signIns.get(0).getCheckTime(), signIns.get(signIns.size() - 1).getCheckTime());
                Date firstDate = signIns.get(0).getCheckTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstDate);
                calendar.add(Calendar.DATE,-1);
                List<SignIn> signInList = new ArrayList<>();
                for (int i=0;i < daysBetweenTwoDate.size();i ++){
                    calendar.add(Calendar.DATE,1);
                    String prefix = sdf.format(calendar.getTime());
                    Date today0 = sdf2.parse(prefix + " 00:00:00");
                    Date today23 = sdf2.parse(prefix + " 23:59:59");
                    for (User user : users) {
                        SignIn existSignIn = signInMapper.selectSignInByUserAndTimeRange(user.getId(),today0,today23);
                        if (existSignIn == null){
                            SignIn signIn = new SignIn();
                            signIn.setId(snowFlakeIDHelper.nextId());
                            signIn.setUserId(user.getId());
                            signIn.setType(2);
                            signIn.setCheckTime(today0);
                            signInList.add(signIn);
                        }
                    }
                }
                System.out.println("时间段长度: "+daysBetweenTwoDate.size());
                System.out.println("signInList的长度: "+signInList.size());
                System.out.println("users的长度: "+users.size());
                if (!CollectionUtils.isEmpty(signInList)){
                    signInMapper.insertSignInBatch(signInList);
                }
            }
            System.out.println("插入空数据时间: "+(System.currentTimeMillis()-time4)+"ms");

        } catch (IOException e) {
            throw new ZSYServiceException("上传考勤记录到数据库失败!");
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导入记录的最后一条记录打卡时间
     * @return
     */
    @Override
    public SignInLastRecordResDTO getSignInLastRecord() {
        SignIn signIn = signInMapper.selectSingInLastRecord();
        SignInLastRecordResDTO resDTO = new SignInLastRecordResDTO();
        BeanUtils.copyProperties(signIn,resDTO);
        return resDTO;
    }

    /**
     * 分页查询考勤记录
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<SignInResDTO> getSignInPage(SignInReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), 20);
        if (reqDTO.getBeginTime() == null){
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE,-1);
            String yesterdayPrefix = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE,-6);
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
        Page<SignInBO> signInPage = signInMapper.selectSignInPage(reqDTO);
        Page<SignInResDTO> signInResDTOS = new Page<>();
        BeanUtils.copyProperties(signInPage,signInResDTOS);
        if (!CollectionUtils.isEmpty(signInPage)){
            signInPage.stream().forEach(signIn -> {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signIn,resDTO);
                User user = userMapper.selectById(signIn.getUserId());
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE,1);
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
                    today10 = sdf.parse(prefix + " 10:00:00");
                    today18 = sdf.parse(prefix + " 18:00:00");
                    today23 = sdf.parse(prefix + " 23:59:59");
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 07:00:00");
                    seven = sdf.parse(prefix + " 07:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time-> {
                    try {
                        return time.after(sdf.parse(prefix + " 07:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)){
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(),nextSeven,zero);
                    if (!CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                    }else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                }
                //只有一条打卡记录
                else if (dateList.size() == 1){
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckIn(1);
                            }else {
                               resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)){
                                resDTO.setIsCheckInAfterTen(1);
                            }else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        }else {
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckOut(1);
                            }else {
                                resDTO.setIsRecheckOut(0);
                            }
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            if (resDTO.getCheckOutTime().before(today18)){
                                resDTO.setIsCheckOutBeforeSix(1);
                            }else {
                                resDTO.setIsCheckOutBeforeSix(0);
                            }
                            resDTO.setIsWorkToNextDay(0);
                        }
                    }
                    //第二天有7点前的打卡记录,取最后时间作为下班时间
                    else {
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)){
                                resDTO.setIsCheckInAfterTen(1);
                            }else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckIn(1);
                            }else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setIsForget(0);
                        }else {
                            resDTO.setCheckInTime(null);
                            resDTO.setIsForget(1);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = null;
                        if (resDTO.getIsForget() == 0){
                            workTimeMillis = resDTO.getCheckOutTime().getTime()-resDTO.getCheckInTime().getTime();
                        }
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                //2条或以上
                else {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天早于7点没有打卡记录
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)){
                            resDTO.setIsCheckInAfterTen(1);
                        }else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckIn(1);
                        }else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(dateList.get(dateList.size()-1));
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(),dateList.get(dateList.size()-1) );
                        if (signIn2.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(0);
                        Long workTimeMillis = dateList.get(dateList.size()-1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                    //第二天早于7点有打卡记录
                    else {
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)){
                            resDTO.setIsCheckInAfterTen(1);
                        }else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckIn(1);
                        }else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(),before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (signIn2.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = before7Clock.get(before7Clock.size()-1).getCheckTime().getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                UserLeave userLeave = userLeaveMapper.selectByUserAndTime(user.getId(),today0,today23);
                if (userLeave != null){
                    // 1.先判断请假持续几天
                    List<String> leaveDays = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), userLeave.getEndTime());
                    //此条请假记录只有一天,则请假时长即为当天的请假时长
                    if (leaveDays.size() == 1){
                        resDTO.setLeaveTime(userLeave.getHours());
                    }else {
                        // 2.当请假持续天数超过一天时,再判断今天是第几天
                        List<String> firstDayToToday = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), today23);
                        //当请假开始时间早于10:00时,则第一天请假为8h
                        if (userLeave.getBeginTime().getHours() <= 10){
                            if (firstDayToToday.size() < leaveDays.size()){
                                //当今天不是最后一天,请假时长为8h
                                resDTO.setLeaveTime(BigDecimal.valueOf(8));
                            }else {
                                //当今天是最后一天,请假时长为 总时长减去之前的时长
                                BigDecimal firstLeaveHours = BigDecimal.valueOf(8);
                                BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf(leaveDays.size() - 2));
                                resDTO.setLeaveTime(userLeave.getHours().
                                        subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                            }
                        }else {
                            if (firstDayToToday.size() < leaveDays.size()){
                                //当今天不是最后一天,请假时长为4h
                                if (firstDayToToday.size() == 1){
                                    resDTO.setLeaveTime(BigDecimal.valueOf(5));
                                }else {
                                    resDTO.setLeaveTime(BigDecimal.valueOf(8));
                                }
                            }else {
                                //当今天是最后一天,请假时长为 总时长减去之前的时长
                                BigDecimal firstLeaveHours = BigDecimal.valueOf(5);
                                BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf(leaveDays.size() - 2));
                                resDTO.setLeaveTime(userLeave.getHours().
                                        subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                            }
                        }
                    }
                }else {
                    resDTO.setLeaveTime(BigDecimal.ZERO);
                }
                if (resDTO.getWorkTime() != null){
                    if (resDTO.getWorkTime() > 8.5*60*60*1000){
                        resDTO.setLessThanNine(0);
                    }else {
                        resDTO.setLessThanNine(1);
                    }
                }
                resDTO.setDate(today0);
                calendar.setTime(today0);
                if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
                    resDTO.setIsWeekend(1);
                }else {
                    resDTO.setIsWeekend(0);
                }
                Float eWorkHours = extraWorkMapper.selectEWorkHours(user.getId(),today0,today23);
                resDTO.setEWorkHours(BigDecimal.valueOf(eWorkHours));
                calendar.setTime(today0);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek){
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
                if (resDTO.getIsWeekend() == 1){
                    Date checkInTime = resDTO.getCheckInTime();
                    Date checkOutTime = resDTO.getCheckOutTime();
                    if (checkInTime != null && checkOutTime != null){
                        Long eWorkTime = checkOutTime.getTime() - checkInTime.getTime();
                        resDTO.setLessThanNine(0);
                        resDTO.setEWorkTime(eWorkTime);
                    }
                }
                signInResDTOS.add(resDTO);
            });
        }

        return new PageInfo<>(signInResDTOS);
    }

    /**
     * 个人分页查询考勤统计
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<SignInResDTO> getPersonalSignInPage(SignInReqDTO reqDTO) {
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), 7);
        reqDTO.setUserId(ZSYTokenRequestContext.get().getUserId());
        if (reqDTO.getBeginTime() == null){
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE,-1);
            String yesterdayPrefix = sdf.format(calendar.getTime());
            calendar.add(Calendar.DATE,-6);
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
        Page<SignInBO> signInPage = signInMapper.selectSignInPage(reqDTO);
        Page<SignInResDTO> signInResDTOS = new Page<>();
        BeanUtils.copyProperties(signInPage,signInResDTOS);
        if (!CollectionUtils.isEmpty(signInPage)){
            signInPage.stream().forEach(signIn -> {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signIn,resDTO);
                User user = userMapper.selectById(signIn.getUserId());
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE,1);
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
                    today10 = sdf.parse(prefix + " 10:00:00");
                    today18 = sdf.parse(prefix + " 18:00:00");
                    today23 = sdf.parse(prefix + " 23:59:59");
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 07:00:00");
                    seven = sdf.parse(prefix + " 07:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time-> {
                    try {
                        return time.after(sdf.parse(prefix + " 07:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)){
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(),nextSeven,zero);
                    if (!CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                    }else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                }
                //只有一条打卡记录
                else if (dateList.size() == 1){
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)){
                                resDTO.setIsCheckInAfterTen(1);
                            }else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckIn(1);
                            }else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        }else {
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            if (resDTO.getCheckOutTime().before(today18)){
                                resDTO.setIsCheckOutBeforeSix(1);
                            }else {
                                resDTO.setIsCheckOutBeforeSix(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckOut(1);
                            }else {
                                resDTO.setIsRecheckOut(0);
                            }
                            resDTO.setIsWorkToNextDay(0);
                        }
                    }
                    //第二天有7点前的打卡记录,取最后时间作为下班时间
                    else {
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)){
                                resDTO.setIsCheckInAfterTen(1);
                            }else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckIn(1);
                            }else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setIsForget(0);
                        }else {
                            resDTO.setCheckInTime(null);
                            resDTO.setIsForget(1);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = null;
                        if (resDTO.getIsForget() == 0){
                            workTimeMillis = resDTO.getCheckOutTime().getTime()-resDTO.getCheckInTime().getTime();
                        }
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                //2条或以上
                else {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天早于7点没有打卡记录
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)){
                            resDTO.setIsCheckInAfterTen(1);
                        }else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckIn(1);
                        }else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(dateList.get(dateList.size()-1));
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(dateList.size()-1));
                        if (signIn2.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(0);
                        Long workTimeMillis = dateList.get(dateList.size()-1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                    //第二天早于7点有打卡记录
                    else {
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)){
                            resDTO.setIsCheckInAfterTen(1);
                        }else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckIn(1);
                        }else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (signIn2.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = before7Clock.get(before7Clock.size()-1).getCheckTime().getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                UserLeave userLeave = userLeaveMapper.selectByUserAndTime(user.getId(), today0, today23);
                if (userLeave != null){
                    // 1.先判断请假持续几天
                    List<String> leaveDays = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), userLeave.getEndTime());
                    //此条请假记录只有一天,则请假时长即为当天的请假时长
                    if (leaveDays.size() == 1){
                        resDTO.setLeaveTime(userLeave.getHours());
                    }else {
                        // 2.当请假持续天数超过一天时,再判断今天是第几天
                        List<String> firstDayToToday = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), today23);
                        //当请假开始时间早于10:00时,则第一天请假为8h
                        if (userLeave.getBeginTime().getHours() <= 10){
                            if (firstDayToToday.size() < leaveDays.size()){
                                //当今天不是最后一天,请假时长为8h
                                resDTO.setLeaveTime(BigDecimal.valueOf(8));
                            }else {
                                //当今天是最后一天,请假时长为 总时长减去之前的时长
                                BigDecimal firstLeaveHours = BigDecimal.valueOf(8);
                                BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf(leaveDays.size() - 2));
                                resDTO.setLeaveTime(userLeave.getHours().
                                        subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                            }
                        }else {
                            if (firstDayToToday.size() < leaveDays.size()){
                                //当今天不是最后一天,请假时长为4h
                                if (firstDayToToday.size() == 1){
                                    resDTO.setLeaveTime(BigDecimal.valueOf(5));
                                }else {
                                    resDTO.setLeaveTime(BigDecimal.valueOf(8));
                                }
                            }else {
                                //当今天是最后一天,请假时长为 总时长减去之前的时长
                                BigDecimal firstLeaveHours = BigDecimal.valueOf(5);
                                BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf(leaveDays.size() - 2));
                                resDTO.setLeaveTime(userLeave.getHours().
                                        subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                            }
                        }
                    }
                }else {
                    resDTO.setLeaveTime(BigDecimal.ZERO);
                }
                if (resDTO.getWorkTime() != null){
                    if (resDTO.getWorkTime() > 8.5*60*60*1000){
                        resDTO.setLessThanNine(0);
                    }else {
                        resDTO.setLessThanNine(1);
                    }
                }
                resDTO.setDate(today0);
                calendar.setTime(today0);
                if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
                    resDTO.setIsWeekend(1);
                }else {
                    resDTO.setIsWeekend(0);
                }
                Float eWorkHours = extraWorkMapper.selectEWorkHours(user.getId(),today0,today23);
                resDTO.setEWorkHours(BigDecimal.valueOf(eWorkHours));
                List<String> daysBetweenTwoDate = DateHelper.getDaysBetweenTwoDate(today0,new Date());
                if (daysBetweenTwoDate.size()<=8){
                    resDTO.setCanReCheck(1);
                }
                calendar.setTime(today0);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek){
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
                if (resDTO.getIsWeekend() == 1){
                    Date checkInTime = resDTO.getCheckInTime();
                    Date checkOutTime = resDTO.getCheckOutTime();
                    if (checkInTime != null && checkOutTime != null){
                        Long eWorkTime = checkOutTime.getTime() - checkInTime.getTime();
                        resDTO.setLessThanNine(0);
                        resDTO.setEWorkTime(eWorkTime);
                    }
                }
                signInResDTOS.add(resDTO);
            });
        }
        return new PageInfo<>(signInResDTOS);
    }

    /**
     * 个人漏打卡考勤记录
     * @param pageNum
     * @return
     */
    @Override
    public ArmorPageInfo<SignInResDTO> getForgetSignInPage(Integer pageNum) {

        System.out.println(ZSYTokenRequestContext.get().getUserName());
        List<SignInBO> signInPage = signInMapper.selectForgetSignInList(ZSYTokenRequestContext.get().getUserId());
        List<SignInResDTO> signInResDTOS = new ArrayList<>();
        BeanUtils.copyProperties(signInPage,signInResDTOS);
        if (!CollectionUtils.isEmpty(signInPage)){
            signInPage.stream().forEach(signIn -> {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signIn,resDTO);
                User user = userMapper.selectById(signIn.getUserId());
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE,1);
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
                    nextSeven = sdf.parse(nextPrefix + " 07:00:00");
                    seven = sdf.parse(prefix + " 07:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //看此条打卡记录是否在本周之内
                if (dateList.get(0).after(thisWeekFirstDay) && dateList.get(0).before(thisWeekLastDay)){
                    resDTO.setCanReCheck(1);
                }else {
                    resDTO.setCanReCheck(0);
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time-> {
                    try {
                        return time.after(sdf.parse(prefix + " 07:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)){
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(),nextSeven,zero);
                    if (!CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        resDTO.setIsWorkToNextDay(1);
                    }else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                    signInResDTOS.add(resDTO);
                }
                //只有一条打卡记录
                else if (dateList.size() == 1){
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            resDTO.setCheckInTime(dateList.get(0));
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        }else {
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
        return new ArmorPageInfo<>(current,ZSYConstants.PAGE_SIZE_WAIT,totalSize,list);
    }

    /**
     * 补打卡申请
     * @param reqDTO
     */
    @Override
    @Transactional
    public void addResignIn(ResignInReqDTO reqDTO) {
        if (reqDTO.getRecheckInTime() != null){
            ResignIn resignIn = new ResignIn();
            BeanUtils.copyProperties(reqDTO,resignIn);
            resignIn.setRecheckTime(reqDTO.getRecheckInTime());
            resignIn.setType(0);
            resignIn.setId(snowFlakeIDHelper.nextId());
            resignIn.setReviewStatus(0);
            if (signInMapper.addResignIn(resignIn) == 0){
                throw new ZSYServiceException("新增上班补打卡申请失败");
            }
        }
        if (reqDTO.getRecheckOutTime() != null){
            ResignIn resignIn = new ResignIn();
            BeanUtils.copyProperties(reqDTO,resignIn);
            resignIn.setRecheckTime(reqDTO.getRecheckOutTime());
            resignIn.setType(1);
            resignIn.setId(snowFlakeIDHelper.nextId());
            resignIn.setReviewStatus(0);
            if (signInMapper.addResignIn(resignIn) == 0){
                throw new ZSYServiceException("新增下班补打卡申请失败");
            }
        }

    }

    /**
     * 修改补打卡申请
     * @param reqDTO
     * @param id
     */
    @Override
    @Transactional
    public void updateResignIn(ResignInReqDTO reqDTO,Long id) {
        ResignIn resignIn = signInMapper.selectResignInById(id);
        if (resignIn == null){
            throw new ZSYServiceException("该条补打卡申请不存在");
        }
        BeanUtils.copyProperties(reqDTO,resignIn);
        if (resignIn.getType()==0){
            resignIn.setRecheckTime(reqDTO.getRecheckInTime());
        }else {
            resignIn.setRecheckTime(reqDTO.getRecheckOutTime());
        }
        if (signInMapper.updateResignIn(resignIn,id) == 0){
            throw new ZSYServiceException("修改补打卡申请失败");
        }
    }


    /**
     * 删除补打卡申请
     * @param id
     */
    @Override
    @Transactional
    public void deleteResignIn(Long id) {
        ResignIn resignIn = signInMapper.selectResignInById(id);
        if (resignIn == null){
            throw new ZSYServiceException("该条补打卡申请不存在");
        }
        if (signInMapper.deleteResignInById(id) == 0){
            throw new ZSYServiceException("删除补打卡申请失败");
        }
        //如果删除的是审核通过的  补打卡   还需要删除与之相关的signIn记录
        if (resignIn.getReviewStatus() == 2){
            SignIn signIn = signInMapper.selectSignInByUserAndTime(resignIn.getUserId(), resignIn.getRecheckTime());
            if (signIn != null && signInMapper.deleteSignIn(resignIn.getRecheckTime(),resignIn.getUserId()) == 0 ){
                throw new ZSYServiceException("删除补打卡申请失败");
            }
        }
    }

    /**
     * 审核通过补打卡申请
     * @param id
     */
    @Override
    @Transactional
    public void accessResignIn(Long id) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.ADMINISTRATOR.getValue()) {
            throw new ZSYServiceException("当前账号无权限");
        }
        ResignIn resignIn = signInMapper.selectResignInById(id);
        if (resignIn == null){
            throw new ZSYServiceException("该条补打卡申请不存在");
        }
        resignIn.setReviewStatus(2);
        if (signInMapper.updateResignIn(resignIn,id) == 0){
            throw new ZSYServiceException("审核通过补打卡申请失败");
        }
        SignIn signIn = new SignIn();
        signIn.setId(snowFlakeIDHelper.nextId());
        signIn.setUserId(resignIn.getUserId());
        signIn.setCheckTime(resignIn.getRecheckTime());
        signIn.setType(ZSYSignInType.RE_SIGN.getValue());
        if (signInMapper.addSignIn(signIn) == 0){
            throw new ZSYServiceException("审核通过补打卡申请失败");
        }
        SignIn existSignIn = signInMapper.selectSignInByUserAndTime(signIn.getUserId(), signIn.getCheckTime());
        if (existSignIn != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String prefix = sdf2.format(existSignIn.getCheckTime());
            try {
                Date today0 = sdf2.parse(prefix + " 00:00:00");
                Date today23 = sdf2.parse(prefix + " 23:59:59");
                signInMapper.deleteUselessSignIn(existSignIn.getUserId(),today0,today23);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 个人查看加班总时长
     * @param month
     * @return
     */
    @Override
    public TotalExtraHoursResDTO getPersonalTotalExtraHours(Integer month) {
        TotalExtraHoursResDTO totalExtraHoursResDTO = new TotalExtraHoursResDTO();
        Long extraWorkTime = 0L;
        Date beginTime = null;
        Date endTime = null;
        if (month == 0){
            beginTime = null;
            endTime = null;
        }else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                Date today = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.set(Calendar.MONTH,month-1);
                calendar.set(Calendar.DATE,1);
                String beginTimeStr = sdf.format(calendar.getTime());
                beginTime = sdf.parse(beginTimeStr);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,1);
                String endTimeStr = sdf.format(calendar.getTime());
                endTime = sdf.parse(endTimeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<SignInBO> signInList = signInMapper.selectPersonalSignInList(ZSYTokenRequestContext.get().getUserId(),beginTime,endTime);

        if (!CollectionUtils.isEmpty(signInList)){
            for (SignInBO signIn : signInList) {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signIn,resDTO);
                User user = userMapper.selectById(signIn.getUserId());
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE,1);
                String prefix = sdf2.format(dateList.get(0));
                String nextPrefix = sdf2.format(calendar.getTime());
                Date zero = null;
                Date today10 = null;
                Date today18 = null;
                Date seven = null;
                Date nextSeven = null;
                Date fifteen = null;
                try {
                    today10 = sdf.parse(prefix + " 10:00:00");
                    today18 = sdf.parse(prefix + " 18:00:00");
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 07:00:00");
                    seven = sdf.parse(prefix + " 07:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time-> {
                    try {
                        return time.after(sdf.parse(prefix + " 07:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)){
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(),nextSeven,zero);
                    if (!CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                    }else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                }
                //只有一条打卡记录
                else if (dateList.size() == 1){
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)){
                                resDTO.setIsCheckInAfterTen(1);
                            }else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckIn(1);
                            }else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        }else {
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            if (resDTO.getCheckOutTime().before(today18)){
                                resDTO.setIsCheckOutBeforeSix(1);
                            }else {
                                resDTO.setIsCheckOutBeforeSix(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckOut(1);
                            }else {
                                resDTO.setIsRecheckOut(0);
                            }
                            resDTO.setIsWorkToNextDay(0);
                        }
                    }
                    //第二天有7点前的打卡记录,取最后时间作为下班时间
                    else {
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)){
                                resDTO.setIsCheckInAfterTen(1);
                            }else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckIn(1);
                            }else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setIsForget(0);
                        }else {
                            resDTO.setCheckInTime(null);
                            resDTO.setIsForget(1);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = null;
                        if (resDTO.getIsForget() == 0){
                            workTimeMillis = resDTO.getCheckOutTime().getTime()-resDTO.getCheckInTime().getTime();
                        }
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                //2条或以上
                else {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天早于7点没有打卡记录
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)){
                            resDTO.setIsCheckInAfterTen(1);
                        }else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckIn(1);
                        }else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(dateList.get(dateList.size()-1));
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(dateList.size()-1));
                        if (signIn2.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(0);
                        Long workTimeMillis = dateList.get(dateList.size()-1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                    //第二天早于7点有打卡记录
                    else {
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)){
                            resDTO.setIsCheckInAfterTen(1);
                        }else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckIn(1);
                        }else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (signIn2.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = before7Clock.get(before7Clock.size()-1).getCheckTime().getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                if (resDTO.getEWorkTime() != null){
                    extraWorkTime += resDTO.getEWorkTime();
                }else {
                    extraWorkTime += 0L;
                }
            }
        }
        totalExtraHoursResDTO.setUserId(ZSYTokenRequestContext.get().getUserId());
        totalExtraHoursResDTO.setMonth(month);
        totalExtraHoursResDTO.setExtraTime(extraWorkTime);
        return totalExtraHoursResDTO;
    }

    /**
     * 管理员查看用户的加班总时长
     * @param userId
     * @param month
     * @return
     */
    @Override
    public TotalExtraHoursResDTO getTotalExtraHoursByUserId(Long userId, Integer month) {
        TotalExtraHoursResDTO totalExtraHoursResDTO = new TotalExtraHoursResDTO();
        Long extraWorkTime = 0L;
        Date beginTime = null;
        Date endTime = null;
        if (month == 0){
            beginTime = null;
            endTime = null;
        }else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                Date today = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(today);
                calendar.set(Calendar.MONTH,month-1);
                calendar.set(Calendar.DATE,1);
                String beginTimeStr = sdf.format(calendar.getTime());
                beginTime = sdf.parse(beginTimeStr);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DATE,1);
                String endTimeStr = sdf.format(calendar.getTime());
                endTime = sdf.parse(endTimeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<SignInBO> signInList = signInMapper.selectPersonalSignInList(userId,beginTime,endTime);

        if (!CollectionUtils.isEmpty(signInList)){
            for (SignInBO signIn : signInList) {
                SignInResDTO resDTO = new SignInResDTO();
                BeanUtils.copyProperties(signIn,resDTO);
                User user = userMapper.selectById(signIn.getUserId());
                resDTO.setUserName(user.getName());
                List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<Date> dateList = list.stream().map(str -> {
                    try {
                        return sdf.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    throw new ZSYServiceException("解析当天考勤时间集合失败");
                }).sorted().collect(Collectors.toList());
                resDTO.setCheckTimeList(dateList);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateList.get(0));
                calendar.add(Calendar.DATE,1);
                String prefix = sdf2.format(dateList.get(0));
                String nextPrefix = sdf2.format(calendar.getTime());
                Date zero = null;
                Date today10 = null;
                Date today18 = null;
                Date seven = null;
                Date nextSeven = null;
                Date fifteen = null;
                try {
                    today10 = sdf.parse(prefix + " 10:00:00");
                    today18 = sdf.parse(prefix + " 18:00:00");
                    zero = sdf.parse(nextPrefix + " 00:00:00");
                    nextSeven = sdf.parse(nextPrefix + " 07:00:00");
                    seven = sdf.parse(prefix + " 07:00:00");
                    fifteen = sdf.parse(prefix + " 15:00:00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //过滤当天上午7点之前的打卡记录
                dateList = dateList.stream().filter(time-> {
                    try {
                        return time.after(sdf.parse(prefix + " 07:00:00"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());
                //过滤之后,打卡记录为0,则视为漏打卡
                if (CollectionUtils.isEmpty(dateList)){
                    resDTO.setIsForget(1);
                    resDTO.setWorkTime(null);
                    resDTO.setEWorkTime(null);
                    resDTO.setCheckInTime(null);
                    //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(),nextSeven,zero);
                    if (!CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                    }else {
                        resDTO.setIsWorkToNextDay(0);
                        resDTO.setCheckOutTime(null);
                    }
                }
                //只有一条打卡记录
                else if (dateList.size() == 1){
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天没有7点前的打卡记录,则当天漏打卡
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setIsForget(1);
                        resDTO.setWorkTime(null);
                        resDTO.setEWorkTime(null);
                        //如果时间在7:00--15:00之间,即为上班打卡
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)){
                                resDTO.setIsCheckInAfterTen(1);
                            }else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckIn(1);
                            }else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setCheckOutTime(null);
                            resDTO.setIsWorkToNextDay(0);
                        }else {
                            resDTO.setCheckInTime(null);
                            resDTO.setCheckOutTime(dateList.get(0));
                            if (resDTO.getCheckOutTime().before(today18)){
                                resDTO.setIsCheckOutBeforeSix(1);
                            }else {
                                resDTO.setIsCheckOutBeforeSix(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckOut(1);
                            }else {
                                resDTO.setIsRecheckOut(0);
                            }
                            resDTO.setIsWorkToNextDay(0);
                        }
                    }
                    //第二天有7点前的打卡记录,取最后时间作为下班时间
                    else {
                        if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                            resDTO.setCheckInTime(dateList.get(0));
                            if (resDTO.getCheckInTime().after(today10)){
                                resDTO.setIsCheckInAfterTen(1);
                            }else {
                                resDTO.setIsCheckInAfterTen(0);
                            }
                            SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                            if (signIn1.getType() == 1){
                                resDTO.setIsRecheckIn(1);
                            }else {
                                resDTO.setIsRecheckIn(0);
                            }
                            resDTO.setIsForget(0);
                        }else {
                            resDTO.setCheckInTime(null);
                            resDTO.setIsForget(1);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = null;
                        if (resDTO.getIsForget() == 0){
                            workTimeMillis = resDTO.getCheckOutTime().getTime()-resDTO.getCheckInTime().getTime();
                        }
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                //2条或以上
                else {
                    List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                    //第二天早于7点没有打卡记录
                    if (CollectionUtils.isEmpty(before7Clock)){
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)){
                            resDTO.setIsCheckInAfterTen(1);
                        }else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckIn(1);
                        }else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(dateList.get(dateList.size()-1));
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(dateList.size()-1));
                        if (signIn2.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(0);
                        Long workTimeMillis = dateList.get(dateList.size()-1).getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                    //第二天早于7点有打卡记录
                    else {
                        resDTO.setCheckInTime(dateList.get(0));
                        if (resDTO.getCheckInTime().after(today10)){
                            resDTO.setIsCheckInAfterTen(1);
                        }else {
                            resDTO.setIsCheckInAfterTen(0);
                        }
                        SignIn signIn1 = signInMapper.selectSignInByUserAndTime(user.getId(), dateList.get(0));
                        if (signIn1.getType() == 1){
                            resDTO.setIsRecheckIn(1);
                        }else {
                            resDTO.setIsRecheckIn(0);
                        }
                        resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (resDTO.getCheckOutTime().before(today18)){
                            resDTO.setIsCheckOutBeforeSix(1);
                        }else {
                            resDTO.setIsCheckOutBeforeSix(0);
                        }
                        SignIn signIn2 = signInMapper.selectSignInByUserAndTime(user.getId(), before7Clock.get(before7Clock.size()-1).getCheckTime());
                        if (signIn2.getType() == 1){
                            resDTO.setIsRecheckOut(1);
                        }else {
                            resDTO.setIsRecheckOut(0);
                        }
                        resDTO.setIsForget(0);
                        resDTO.setIsWorkToNextDay(1);
                        Long workTimeMillis = before7Clock.get(before7Clock.size()-1).getCheckTime().getTime()
                                - dateList.get(0).getTime();
                        resDTO.setWorkTime(workTimeMillis);
                        //当工作时长大于10小时,计算为加班时间
                        if (workTimeMillis != null && workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                if (resDTO.getEWorkTime() != null){
                    extraWorkTime += resDTO.getEWorkTime();
                }else {
                    extraWorkTime += 0L;
                }
            }
        }
        User user = userMapper.selectById(userId);
        totalExtraHoursResDTO.setUserId(userId);
        totalExtraHoursResDTO.setUserName(user.getName());
        totalExtraHoursResDTO.setMonth(month);
        totalExtraHoursResDTO.setExtraTime(extraWorkTime);
        return totalExtraHoursResDTO;
    }

    /**
     * 按月导出考勤情况Excel
     * @param month
     * @return
     */
    @Override
    public String excelSignInData(Integer month) {
        Date beginTime = signInMapper.selectMonthFirstTime(month);
        Date endTime = signInMapper.selectMonthLastTime(month);
        List<Date> dates = signInMapper.selectDateList(month);
        List<User> userList = signInMapper.selectCheckInUsers();
        Map<Long,List<SignInResDTO>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(userList)){
            for (User user : userList) {
                List<SignInBO> signInBOS = signInMapper.selectPersonalSignInList(user.getId(), beginTime, endTime);
                List<SignInResDTO> signInResDTOS = new ArrayList<>();
                if (!CollectionUtils.isEmpty(signInBOS)){
                    for (SignInBO signIn : signInBOS) {
                        SignInResDTO resDTO = new SignInResDTO();
                        BeanUtils.copyProperties(signIn,resDTO);
                        resDTO.setUserName(user.getName());
                        List<String> list = Arrays.asList(signIn.getCheckTimeStr().split(","));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        List<Date> dateList = list.stream().map(str -> {
                            try {
                                return sdf.parse(str);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            throw new ZSYServiceException("解析当天考勤时间集合失败");
                        }).sorted().collect(Collectors.toList());
                        resDTO.setCheckTimeList(dateList);
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dateList.get(0));
                        calendar.add(Calendar.DATE,1);
                        String prefix = sdf2.format(dateList.get(0));
                        String nextPrefix = sdf2.format(calendar.getTime());
                        Date zero = null;
                        Date today0 = null;
                        Date today10 = null;
                        Date today23 = null;
                        Date today18 = null;
                        Date seven = null;
                        Date nextSeven = null;
                        Date fifteen = null;
                        try {
                            today0 = sdf.parse(prefix + " 00:00:00");
                            today23 = sdf.parse(prefix + " 23:59:59");
                            today10 = sdf.parse(prefix + " 10:00:00");
                            today18 = sdf.parse(prefix + " 18:00:00");
                            zero = sdf.parse(nextPrefix + " 00:00:00");
                            nextSeven = sdf.parse(nextPrefix + " 07:00:00");
                            seven = sdf.parse(prefix + " 07:00:00");
                            fifteen = sdf.parse(prefix + " 15:00:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //过滤当天上午7点之前的打卡记录
                        dateList = dateList.stream().filter(time-> {
                            try {
                                return time.after(sdf.parse(prefix + " 07:00:00"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }).collect(Collectors.toList());
                        //过滤之后,打卡记录为0,则视为漏打卡
                        if (CollectionUtils.isEmpty(dateList)){
                            resDTO.setIsForget(1);
                            resDTO.setWorkTime(null);
                            resDTO.setEWorkTime(null);
                            resDTO.setCheckInTime(null);
                            //查询当前用户第二天7点前是否有打卡记录,有的话取最后一个作为下班打卡时间
                            List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(),nextSeven,zero);
                            if (!CollectionUtils.isEmpty(before7Clock)){
                                resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                                if (resDTO.getCheckOutTime().before(today18)){
                                    resDTO.setIsCheckOutBeforeSix(1);
                                }else {
                                    resDTO.setIsCheckOutBeforeSix(0);
                                }
                                resDTO.setIsWorkToNextDay(1);
                            }else {
                                resDTO.setIsWorkToNextDay(0);
                                resDTO.setCheckOutTime(null);
                            }
                        }
                        //只有一条打卡记录
                        else if (dateList.size() == 1){
                            List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                            //第二天没有7点前的打卡记录,则当天漏打卡
                            if (CollectionUtils.isEmpty(before7Clock)){
                                resDTO.setIsForget(1);
                                resDTO.setWorkTime(null);
                                resDTO.setEWorkTime(null);
                                //如果时间在7:00--15:00之间,即为上班打卡
                                if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                                    resDTO.setCheckInTime(dateList.get(0));
                                    if (resDTO.getCheckInTime().after(today10)){
                                        resDTO.setIsCheckInAfterTen(1);
                                    }else {
                                        resDTO.setIsCheckInAfterTen(0);
                                    }
                                    resDTO.setCheckOutTime(null);
                                    resDTO.setIsWorkToNextDay(0);
                                }else {
                                    resDTO.setCheckInTime(null);
                                    resDTO.setCheckOutTime(dateList.get(0));
                                    if (resDTO.getCheckOutTime().before(today18)){
                                        resDTO.setIsCheckOutBeforeSix(1);
                                    }else {
                                        resDTO.setIsCheckOutBeforeSix(0);
                                    }
                                    resDTO.setIsWorkToNextDay(0);
                                }
                            }
                            //第二天有7点前的打卡记录,取最后时间作为下班时间
                            else {
                                if (dateList.get(0).after(seven) && dateList.get(0).before(fifteen)){
                                    resDTO.setCheckInTime(dateList.get(0));
                                    if (resDTO.getCheckInTime().after(today10)){
                                        resDTO.setIsCheckInAfterTen(1);
                                    }else {
                                        resDTO.setIsCheckInAfterTen(0);
                                    }
                                    resDTO.setIsForget(0);
                                }else {
                                    resDTO.setCheckInTime(null);
                                    resDTO.setIsForget(1);
                                }
                                resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                                if (resDTO.getCheckOutTime().before(today18)){
                                    resDTO.setIsCheckOutBeforeSix(1);
                                }else {
                                    resDTO.setIsCheckOutBeforeSix(0);
                                }
                                resDTO.setIsWorkToNextDay(1);
                                Long workTimeMillis = null;
                                if (resDTO.getIsForget() == 0){
                                    workTimeMillis = resDTO.getCheckOutTime().getTime()-resDTO.getCheckInTime().getTime();
                                }
                                resDTO.setWorkTime(workTimeMillis);
                                //当工作时长大于10小时,计算为加班时间
                                if (workTimeMillis != null && workTimeMillis > 9.5*60*60*1000){
                                    Long eWorkTime = workTimeMillis - (1000*60*60*10);
                                    resDTO.setEWorkTime(eWorkTime);
                                }else {
                                    resDTO.setEWorkTime(null);
                                }
                            }
                        }
                        //2条或以上
                        else {
                            List<SignIn> before7Clock = signInMapper.selectBefore7ByUserId(user.getId(), nextSeven,zero);
                            //第二天早于7点没有打卡记录
                            if (CollectionUtils.isEmpty(before7Clock)){
                                resDTO.setCheckInTime(dateList.get(0));
                                if (resDTO.getCheckInTime().after(today10)){
                                    resDTO.setIsCheckInAfterTen(1);
                                }else {
                                    resDTO.setIsCheckInAfterTen(0);
                                }
                                resDTO.setCheckOutTime(dateList.get(dateList.size()-1));
                                if (resDTO.getCheckOutTime().before(today18)){
                                    resDTO.setIsCheckOutBeforeSix(1);
                                }else {
                                    resDTO.setIsCheckOutBeforeSix(0);
                                }
                                resDTO.setIsForget(0);
                                resDTO.setIsWorkToNextDay(0);
                                Long workTimeMillis = dateList.get(dateList.size()-1).getTime()
                                        - dateList.get(0).getTime();
                                resDTO.setWorkTime(workTimeMillis);
                                //当工作时长大于10小时,计算为加班时间
                                if (workTimeMillis != null && workTimeMillis > 9.5*60*60*1000){
                                    Long eWorkTime = workTimeMillis - (1000*60*60*10);
                                    resDTO.setEWorkTime(eWorkTime);
                                }else {
                                    resDTO.setEWorkTime(null);
                                }
                            }
                            //第二天早于7点有打卡记录
                            else {
                                resDTO.setCheckInTime(dateList.get(0));
                                if (resDTO.getCheckInTime().after(today10)){
                                    resDTO.setIsCheckInAfterTen(1);
                                }else {
                                    resDTO.setIsCheckInAfterTen(0);
                                }
                                resDTO.setCheckOutTime(before7Clock.get(before7Clock.size()-1).getCheckTime());
                                if (resDTO.getCheckOutTime().before(today18)){
                                    resDTO.setIsCheckOutBeforeSix(1);
                                }else {
                                    resDTO.setIsCheckOutBeforeSix(0);
                                }
                                resDTO.setIsForget(0);
                                resDTO.setIsWorkToNextDay(1);
                                Long workTimeMillis = before7Clock.get(before7Clock.size()-1).getCheckTime().getTime()
                                        - dateList.get(0).getTime();
                                resDTO.setWorkTime(workTimeMillis);
                                //当工作时长大于10小时,计算为加班时间
                                if (workTimeMillis != null && workTimeMillis > 9.5*60*60*1000){
                                    Long eWorkTime = workTimeMillis - (1000*60*60*10);
                                    resDTO.setEWorkTime(eWorkTime);
                                }else {
                                    resDTO.setEWorkTime(null);
                                }
                            }
                        }
                        UserLeave userLeave = userLeaveMapper.selectByUserAndTime(user.getId(), today0, today23);
                        if (userLeave != null){
                            // 1.先判断请假持续几天
                            List<String> leaveDays = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), userLeave.getEndTime());
                            //此条请假记录只有一天,则请假时长即为当天的请假时长
                            if (leaveDays.size() == 1){
                                resDTO.setLeaveTime(userLeave.getHours());
                            }else {
                                // 2.当请假持续天数超过一天时,再判断今天是第几天
                                List<String> firstDayToToday = DateHelper.getDaysBetweenTwoDate(userLeave.getBeginTime(), today23);
                                //当请假开始时间早于10:00时,则第一天请假为8h
                                if (userLeave.getBeginTime().getHours() <= 10){
                                    if (firstDayToToday.size() < leaveDays.size()){
                                        //当今天不是最后一天,请假时长为8h
                                        resDTO.setLeaveTime(BigDecimal.valueOf(8));
                                    }else {
                                        //当今天是最后一天,请假时长为 总时长减去之前的时长
                                        BigDecimal firstLeaveHours = BigDecimal.valueOf(8);
                                        BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf(leaveDays.size() - 2));
                                        resDTO.setLeaveTime(userLeave.getHours().
                                                subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                                    }
                                }else {
                                    if (firstDayToToday.size() < leaveDays.size()){
                                        //当今天不是最后一天,请假时长为4h
                                        if (firstDayToToday.size() == 1){
                                            resDTO.setLeaveTime(BigDecimal.valueOf(5));
                                        }else {
                                            resDTO.setLeaveTime(BigDecimal.valueOf(8));
                                        }
                                    }else {
                                        //当今天是最后一天,请假时长为 总时长减去之前的时长
                                        BigDecimal firstLeaveHours = BigDecimal.valueOf(5);
                                        BigDecimal otherDaysLeaveHours = BigDecimal.valueOf(8).multiply(BigDecimal.valueOf(leaveDays.size() - 2));
                                        resDTO.setLeaveTime(userLeave.getHours().
                                                subtract(firstLeaveHours).subtract(otherDaysLeaveHours));
                                    }
                                }
                            }
                        }else {
                            resDTO.setLeaveTime(BigDecimal.ZERO);
                        }
                        if (resDTO.getWorkTime() != null){
                            if (resDTO.getWorkTime() > 8.5*60*60*1000){
                                resDTO.setLessThanNine(0);
                            }else {
                                resDTO.setLessThanNine(1);
                            }
                        }
                        resDTO.setDate(today0);
                        calendar.setTime(today0);
                        if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
                            resDTO.setIsWeekend(1);
                        }else {
                            resDTO.setIsWeekend(0);
                        }
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        switch (dayOfWeek){
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
                    }
                }
                map.put(user.getId(),signInResDTOS);
            }
        }
        String url = getSignInExcel(dates, map, userList,month);
        return url;
    }

    /**
     * 考勤人员列表
     * @return
     */
    @Override
    public List<SignInUser> getSignInUsers() {
        List<User> userList = signInMapper.selectCheckInUsers();
        List<SignInUser> signInUsers = new ArrayList<>();
        BeanUtils.copyProperties(userList,signInUsers);
        if (!CollectionUtils.isEmpty(userList)){
            userList.stream().forEach(user->{
                SignInUser signInUser = new SignInUser();
                signInUser.setUserId(user.getId());
                signInUser.setUserName(user.getName());
                signInUsers.add(signInUser);
            });
        }
        return signInUsers;
    }

    /**
     * 导出考勤记录Excel
     * @param dates
     * @param map
     * @return
     */
    private String getSignInExcel(List<Date> dates,Map<Long,List<SignInResDTO>> map,List<User> userList,Integer month){
        if (!CollectionUtils.isEmpty(dates) && !CollectionUtils.isEmpty(map.values())){
            //设置表头
            List<String> headers = new ArrayList<>();
            List<String> weekdays = new ArrayList<>();
            weekdays.add("姓名");
            weekdays.add("上班类型");
            headers.add("求和项");
            headers.add("日期");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat fileNameSdf = new SimpleDateFormat("yyyy年MM月");
            for (Date date : dates) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String weekday = "";
                switch (dayOfWeek){
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

            //设置文件名
            String fileName =fileNameSdf.format(dates.get(0))+ "考勤记录" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";

            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                 HSSFWorkbook workbook = new HSSFWorkbook()){
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
                cell.setCellValue("上海互教信息技术中心2019年"+month+"月考勤记录明细");
                cell.setCellStyle(titleStyle);
                cell2.setCellValue("（黄色填充单元格标识代表上/下班未打卡（包括漏打卡，调休/请假等情况以请假单为准），粉色填充单元格标识代表加班（工作时长超过10h（含10h算加班）），绿色填充单元格标识代表有请小时假/半天假）");
                cell2.setCellStyle(titleStyle2);
                CellRangeAddress region = new CellRangeAddress(0, 0, 0, dates.size()+1);
                CellRangeAddress region2 = new CellRangeAddress(1, 1, 0, dates.size()+1);
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
                    if (weekdays.get(i).contains("星期六") || weekdays.get(i).contains("星期日")){
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
                style3.setFillForegroundColor(HSSFColor.HSSFColorPredefined.CORNFLOWER_BLUE.getIndex());
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

                    for (int i = 0;i < dates.size();i ++){
                        HSSFCell row1Celli = row1.createCell(i+2);
                        HSSFCell row2Celli = row2.createCell(i+2);
                        HSSFCell row3Celli = row3.createCell(i+2);
                        HSSFCell row4Celli = row4.createCell(i+2);
                        HSSFCell row5Celli = row5.createCell(i+2);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        row1Celli.setCellValue(signInResDTOS.get(signInResDTOS.size()-1-i).getCheckInTime() == null ? "" : timeFormat.format(signInResDTOS.get(signInResDTOS.size()-1-i).getCheckInTime()));
                        row1Celli.setCellStyle(style7);
                        row2Celli.setCellValue(signInResDTOS.get(signInResDTOS.size()-1-i).getCheckOutTime() == null ? "" : timeFormat.format(signInResDTOS.get(signInResDTOS.size()-1-i).getCheckOutTime()));
                        row2Celli.setCellStyle(style7);
                        row3Celli.setCellValue(signInResDTOS.get(signInResDTOS.size()-1-i).getWorkTime() == null ? "" : getTime(signInResDTOS.get(signInResDTOS.size()-1-i).getWorkTime()));
                        row3Celli.setCellStyle(style3);
                        Long eWorkTime = signInResDTOS.get(signInResDTOS.size() - 1 - i).getEWorkTime();
                        double eWorkHour = 0;
                        if (eWorkTime != null){
                            eWorkTime = eWorkTime + 30*60*1000;
                            Long hour = eWorkTime /1000/60/60;
                            Long mins = eWorkTime/1000/60%60;
                            Double afterPoint = 0.0;
                            if (mins>= 25 && mins < 55){
                                afterPoint = 0.5;
                            }
                            if (mins>=55 && mins <= 60){
                                afterPoint = 1.0;
                            }
                            eWorkHour = hour+afterPoint;
                            row3Celli.setCellStyle(style6);
                            row4Celli.setCellValue(eWorkHour);
                            row4Celli.setCellStyle(style6);
                        }else {
                            row4Celli.setCellValue("");
                            row4Celli.setCellStyle(style7);
                        }
//                        String dateStr = "";
//                        for (Date date : signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckTimeList()) {
//                            dateStr =dateStr+","+ timeFormat.format(date);
//                        }
//                        dateStr = dateStr.substring(1);
                        row5Celli.setCellValue("");
                        row5Celli.setCellStyle(style7);
                        if (signInResDTOS.get(signInResDTOS.size()-1-i).getIsWeekend() == 1){
                            row1Celli.setCellStyle(style5);
                            row2Celli.setCellStyle(style5);
                            row3Celli.setCellStyle(style5);
                            row4Celli.setCellStyle(style5);
                            row5Celli.setCellStyle(style5);
                            Long workTime = 0L;
                            Date checkInTime = signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckInTime();
                            Date checkOutTime = signInResDTOS.get(signInResDTOS.size() - 1 - i).getCheckOutTime();
                            if (checkInTime != null && checkOutTime != null){
                                workTime = checkOutTime.getTime()-checkInTime.getTime();
                                Long hour = workTime /1000/60/60;
                                Long mins = workTime/1000/60%60;
                                Double afterPoint = 0.0;
                                if (mins>= 25 && mins < 55){
                                    afterPoint = 0.5;
                                }
                                if (mins>=55 && mins <= 60){
                                    afterPoint = 1.0;
                                }
                                eWorkHour = hour+afterPoint;
                                row4Celli.setCellValue(eWorkHour);
                            }else {
                                row3Celli.setCellValue("00:00:00");
                            }
                        }else {
                            if (signInResDTOS.get(signInResDTOS.size()-1-i).getLeaveTime() != null && signInResDTOS.get(signInResDTOS.size()-1-i).getLeaveTime() != BigDecimal.ZERO){
                                row1Celli.setCellStyle(style4);
                                row2Celli.setCellStyle(style4);
                            }else {
                                if (signInResDTOS.get(signInResDTOS.size()-1-i).getCheckInTime() == null){
                                    row1Celli.setCellValue("漏打卡");
                                    row1Celli.setCellStyle(style4);
                                }

                                if (signInResDTOS.get(signInResDTOS.size()-1-i).getCheckOutTime() == null){
                                    row2Celli.setCellValue("漏打卡");
                                    row2Celli.setCellStyle(style4);
                                }
                            }
                        }
                    }
                    num += 5;
                }
                workbook.write(os);
                return ZSYQinuHelper.upload(os.toByteArray(), fileName, "application/vnd.ms-excel", qinuOssProperty);
            }catch (Exception e){
                e.printStackTrace();
                throw new ZSYServiceException("导出表失败");
            }
        }
        throw new ZSYServiceException("数据有误,无法导出");
    }

    public String  getTime(Long timeMillis){
        String hours = addZero(timeMillis/1000/60/60);
        String mins = addZero(timeMillis/1000/60%60);
        String secs = addZero(timeMillis/1000%60);
        return (hours+":"+mins+":"+secs);
    }

    private String addZero(long time){
        if (time<10){
            return ("0" + time);
        }else {
            return time+"";
        }
    }

    /**
     * 个人查询补打卡申请
     * @param status
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<ResignInResDTO> getPersonalByStatus(Integer status, Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1),ZSYConstants.PAGE_SIZE_WAIT);
        Page<ResignIn> resignIns = signInMapper.selectPersonalResignInPage(ZSYTokenRequestContext.get().getUserId(),status);
        Page<ResignInResDTO> resignInResDTOS = new Page<>();
        BeanUtils.copyProperties(resignIns,resignInResDTOS);
        if (!CollectionUtils.isEmpty(resignIns)){
            resignIns.stream().forEach(resignIn -> {
                ResignInResDTO resignInResDTO = new ResignInResDTO();
                BeanUtils.copyProperties(resignIn,resignInResDTO);
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
     * @param status
     * @param pageNum
     * @return
     */
    @Override
    public PageInfo<ResignInResDTO> getResignInByStatus(Integer status, Integer pageNum) {
        PageHelper.startPage(Optional.ofNullable(pageNum).orElse(1),ZSYConstants.PAGE_SIZE_WAIT);
        Page<ResignIn> resignIns = signInMapper.selectResignInPage(status);
        Page<ResignInResDTO> resignInResDTOS = new Page<>();
        BeanUtils.copyProperties(resignIns,resignInResDTOS);
        if (!CollectionUtils.isEmpty(resignIns)){
            resignIns.stream().forEach(resignIn -> {
                ResignInResDTO resignInResDTO = new ResignInResDTO();
                BeanUtils.copyProperties(resignIn,resignInResDTO);
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
    public static boolean isExcel(String url){
        Pattern p=Pattern.compile("\\.(xls|XLS)");
        Matcher m=p.matcher(url);
        if(m.find()){
            return true;
        }
        return false;
    }

    /**
     * MultipartFile 转 File
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile( @RequestParam MultipartFile file ) throws Exception {

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

    public static void inputStreamToFile(InputStream ins, File file) {
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

    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public List readExcel(File file) {
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList=new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    List innerList=new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        if(cellinfo.isEmpty()){
                            continue;
                        }
                        innerList.add(cellinfo);
                    }
                    outerList.add(i, innerList);
                }
                return outerList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
