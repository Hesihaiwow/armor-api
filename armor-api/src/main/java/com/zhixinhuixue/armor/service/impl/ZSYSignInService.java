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
import com.zhixinhuixue.armor.model.bo.SignInBO;
import com.zhixinhuixue.armor.model.dto.request.ResignInReqDTO;
import com.zhixinhuixue.armor.model.dto.request.SignInReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ResignInResDTO;
import com.zhixinhuixue.armor.model.dto.response.SignInLastRecordResDTO;
import com.zhixinhuixue.armor.model.dto.response.SignInResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYSignInService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYSignInType;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
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
            Set<Long> users = new HashSet<>();
            while((str=br.readLine())!=null){//按行读取
                String sort = str.substring(str.lastIndexOf("-")-12,str.lastIndexOf("-")-8);
                String time = str.substring(10, 29);
                String trim = sort.trim();
                User user = signInMapper.selectUserBySort(Integer.valueOf(trim));
                if (user != null){
                    users.add(user.getId());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date checkTime = sdf.parse(time);
                    SignIn existSignIn = signInMapper.selectSignInByUserAndTime(user.getId(),checkTime);
                    if (existSignIn == null){
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
                    for (Long user : users) {
                        SignIn existSignIn = signInMapper.selectSignInByUserAndTimeRange(user,today0,today23);
                        if (existSignIn == null){
                            SignIn signIn = new SignIn();
                            signIn.setId(snowFlakeIDHelper.nextId());
                            signIn.setUserId(user);
                            signIn.setType(2);
                            signIn.setCheckTime(today0);
                            signInList.add(signIn);
                        }
                    }
                }
                System.out.println("时间段长度: "+daysBetweenTwoDate.size());
                System.out.println("signInList的长度: "+signInList.size());
                System.out.println("users的长度: "+users.size());
                signInMapper.insertSignInBatch(signInList);
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
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        if (reqDTO.getBeginTime() == null){
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DATE,-1);
            String prefix = sdf.format(calendar.getTime());
            try {
                Date beginTime = sdf2.parse(prefix + " 00:00:00");
                Date endTime = sdf2.parse(prefix + " 23:59:59");
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
                        if (workTimeMillis > 10*60*60*1000){
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
                    resDTO.setLeaveTime(userLeave.getHours());
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
                ExtraWork extraWork = extraWorkMapper.selectByUserAndTimeRange(user.getId(),today0,today23);
                if (extraWork != null){
                    resDTO.setEWorkHours(BigDecimal.valueOf(extraWork.getWorkHours()));
                }else{
                    resDTO.setEWorkHours(BigDecimal.ZERO);
                }
                List<String> daysBetweenTwoDate = DateHelper.getDaysBetweenTwoDate(new Date(), today0);
                if (daysBetweenTwoDate.size()<=7){
                    resDTO.setCanReCheck(1);
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
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        reqDTO.setUserId(ZSYTokenRequestContext.get().getUserId());
        if (reqDTO.getBeginTime() == null){
            String thisMonthFirstDay = DateHelper.getThisMonthFirstDay();
            String thisMonthLastDay = DateHelper.getThisMonthLastDay();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                reqDTO.setBeginTime(sdf.parse(thisMonthFirstDay));
                reqDTO.setEndTime(sdf.parse(thisMonthLastDay));
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
                        if (workTimeMillis > 10*60*60*1000){
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
                        if (workTimeMillis > 10*60*60*1000){
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
                        if (workTimeMillis > 10*60*60*1000){
                            Long eWorkTime = workTimeMillis - (1000*60*60*10);
                            resDTO.setEWorkTime(eWorkTime);
                        }else {
                            resDTO.setEWorkTime(null);
                        }
                    }
                }
                UserLeave userLeave = userLeaveMapper.selectByUserAndTime(user.getId(), today0, today23);
                if (userLeave != null){
                    resDTO.setLeaveTime(userLeave.getHours());
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
                ExtraWork extraWork = extraWorkMapper.selectByUserAndTimeRange(user.getId(),today0,today23);
                if (extraWork != null){
                    resDTO.setEWorkHours(BigDecimal.valueOf(extraWork.getWorkHours()));
                }else{
                    resDTO.setEWorkHours(BigDecimal.ZERO);
                }
                List<String> daysBetweenTwoDate = DateHelper.getDaysBetweenTwoDate(today0,new Date());
                if (daysBetweenTwoDate.size()<=7){
                    resDTO.setCanReCheck(1);
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
