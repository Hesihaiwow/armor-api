package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTaskUserMapper;
import com.zhixinhuixue.armor.dao.IZSYUserIntegralMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.IntegralResDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.TaskUser;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.UserIntegral;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYIntegralOrigin;
import com.zhixinhuixue.armor.source.enums.ZSYJobRole;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.zhixinhuixue.armor.helper.DateHelper.getCurrYearFirst;
import static com.zhixinhuixue.armor.helper.DateHelper.getCurrYearLast;


/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYIntegralService implements IZSYIntegralService {


    @Autowired
    private IZSYUserIntegralMapper userIntegralMapper;

    @Autowired
    private IZSYUserMapper userMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    @Autowired
    private IZSYTaskUserMapper taskUserMapper;

    /**
     * 用户积分排名表
     *
     * @param startTime 结束时间
     * @param endTime   页码
     * @return
     */
    @Override
    public List<IntegralPageResDTO> getIntegralPage(String startTime, String endTime) {
        List<UserIntegralInfoBO> userIntegralInfoBOS = userIntegralMapper.getIntegralPage(startTime, endTime,ZSYTokenRequestContext.get().getDepartmentId());
        List<IntegralPageResDTO> integralPageResDTOS = new ArrayList<>();
        BeanUtils.copyProperties(userIntegralInfoBOS, integralPageResDTOS);
        userIntegralInfoBOS.stream().forEach(userIntegralInfoBO -> {
            IntegralPageResDTO integralPageResDTO = new IntegralPageResDTO();
            BeanUtils.copyProperties(userIntegralInfoBO, integralPageResDTO);
            integralPageResDTOS.add(integralPageResDTO);
        });
        return integralPageResDTOS;
    }

    /**
     * 获取积分列数
     * 大于结束时间计数，小于开始时间计数
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Map getIntegralCount(String startTime, String endTime){
        int prev = userIntegralMapper.getIntegralCount(null, startTime,ZSYTokenRequestContext.get().getDepartmentId());
        int next = userIntegralMapper.getIntegralCount(endTime, null,ZSYTokenRequestContext.get().getDepartmentId());
        Map map = new HashMap();
        map.put("prev",prev);
        map.put("next",next);
        return map;
    }

    /**
     * 计算不同时间个人积分排行
     *
     * @return
     */
    @Override
    public UserIntegralResDTO getUserIntegral() {
        Long id = ZSYTokenRequestContext.get().getUserId();
        UserIntegralResDTO userIntegralResDTO = new UserIntegralResDTO();
        userIntegralResDTO.setId(id);
        userIntegralResDTO.setWeek(userIntegralMapper.getUserIntegral(DateHelper.getThisWeekFirstDay(), DateHelper.getThisWeekLastDay(), id));
        userIntegralResDTO.setMonth(userIntegralMapper.getUserIntegral(DateHelper.getThisMonthFirstDay(), DateHelper.getThisMonthLastDay(), id));
        userIntegralResDTO.setYear(userIntegralMapper.getUserIntegral(DateHelper.dateFormatter(getCurrYearFirst(), DateHelper.DATETIME_FORMAT), DateHelper.dateFormatter(getCurrYearLast(), DateHelper.DATETIME_FORMAT), id));
        Integer quarterRank = userIntegralMapper.getRank(DateHelper.getThisQuarterFirstDay(), DateHelper.getThisQuarterLastDay(), id,ZSYTokenRequestContext.get().getDepartmentId());//季度排名为空设为0
        userIntegralResDTO.setQuarterRank(quarterRank != null ? quarterRank : 0);
        Integer yearRank = userIntegralMapper.getRank(DateHelper.dateFormatter(getCurrYearFirst(), DateHelper.DATETIME_FORMAT), DateHelper.dateFormatter(getCurrYearLast(), DateHelper.DATETIME_FORMAT), id, ZSYTokenRequestContext.get().getDepartmentId());//年度排名为空设为0
        userIntegralResDTO.setYearRank(yearRank != null ? yearRank : 0);
        User user = userMapper.selectById(id);
        userIntegralResDTO.setDevelopRole(user.getJobRole() == ZSYJobRole.PROGRAMER.getValue());
        return userIntegralResDTO;
    }

    /**
     * 个人积分记录
     *
     * @param id
     * @param pageIndex
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public PageInfo<IntegralHistoryPageResDTO> getIntegralHistory(Long id, int pageIndex, String startTime, String endTime) {
        PageHelper.startPage(pageIndex, ZSYConstants.PAGE_SIZE);
        Page<UserIntegralHistoryBO> userIntegralHistoryBOS = userIntegralMapper.getIntegralHistory(id, startTime, endTime);
        Page<IntegralHistoryPageResDTO> page = new Page<>();
        BeanUtils.copyProperties(userIntegralHistoryBOS, page);
        userIntegralHistoryBOS.stream().forEach(userIntegralHistoryBO -> {
            IntegralHistoryPageResDTO integralHistoryPageResDTO = new IntegralHistoryPageResDTO();
            BeanUtils.copyProperties(userIntegralHistoryBO, integralHistoryPageResDTO);
            integralHistoryPageResDTO.setCreateTime(userIntegralHistoryBO.getCreateTime());
            page.add(integralHistoryPageResDTO);
        });
        PageInfo<IntegralHistoryPageResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 手动添加积分记录
     *
     * @param integralResDTO
     */
    @Override
    public void addIntegral(IntegralResDTO integralResDTO) {
        User user = userMapper.selectById(integralResDTO.getUserId());
        if(integralResDTO.getTime()!=null){//积分转移求助添加
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setId(snowFlakeIDHelper.nextId());
            userIntegral.setCreateTime(integralResDTO.getTime());
            userIntegral.setIntegral(integralResDTO.getIntegral());
            userIntegral.setUserId(integralResDTO.getUserId());
            userIntegral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            userIntegral.setDescription(integralResDTO.getDescription());
            userIntegral.setOrigin(ZSYIntegralOrigin.TRANSFORM.getValue());//求助转移积分
            userIntegral.setReviewStatus(ZSYReviewStatus.PENDING.getValue());
            userIntegralMapper.insert(userIntegral);
        }else{//积分记录添加
            BigDecimal integral = user.getIntegral().add(integralResDTO.getIntegral());
            user.setIntegral(integral);
            if (userMapper.updateSelectiveById(user) == 0) {
                throw new ZSYServiceException("用户积分更新失败");
            }
            UserIntegral userIntegral = new UserIntegral();
            userIntegral.setId(snowFlakeIDHelper.nextId());
            userIntegral.setCreateTime(new Date());
            userIntegral.setIntegral(integralResDTO.getIntegral());
            userIntegral.setUserId(integralResDTO.getUserId());
            userIntegral.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            userIntegral.setDescription(integralResDTO.getDescription());
            userIntegral.setOrigin(ZSYIntegralOrigin.ARTIFICIAL.getValue());//手动添加
            userIntegralMapper.insert(userIntegral);
        }
    }


    /**
     * 根据审核状态查询求助转移积分
     * @return
     */
    @Override
    public PageInfo<IntegralReviewResDTO> getIntegralByReviewStatus(int status,int pageIndex){
        Long userId = ZSYTokenRequestContext.get().getUserId();
        Long departmentId = ZSYTokenRequestContext.get().getDepartmentId();
        return this.getIntegralByReviewStatus(status,pageIndex,userId,departmentId);
    }

    /**
     * 根据审核状态查询所有求助转移积分
     * @return
     */
    @Override
    public PageInfo<IntegralReviewResDTO> getAllIntegralByReviewStatus(int status,int pageIndex){
        return this.getIntegralByReviewStatus(status,pageIndex,null,ZSYTokenRequestContext.get().getDepartmentId());
    }

    public PageInfo<IntegralReviewResDTO> getIntegralByReviewStatus(int status,int pageIndex,Long userId,Long departmentId){
        PageHelper.startPage(pageIndex, 5);
        Page<UserIntegralInfoBO> userIntegralHistoryBOS= userIntegralMapper.getIntegralByReviewStatus(userId, status, departmentId);
        Page<IntegralReviewResDTO> page = new Page<>();
        BeanUtils.copyProperties(userIntegralHistoryBOS, page);
        userIntegralHistoryBOS.stream().forEach(userIntegralHistoryBO -> {
            IntegralReviewResDTO integralReviewResDTO = new IntegralReviewResDTO();
            BeanUtils.copyProperties(userIntegralHistoryBO, integralReviewResDTO);
            integralReviewResDTO.setTime(userIntegralHistoryBO.getCreateTime());
            page.add(integralReviewResDTO);
        });
        PageInfo<IntegralReviewResDTO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 删除积分求助转移
     * @param id
     */
    @Override
    public void deleteHelpDetail(Long id){
        if(userIntegralMapper.deleteIntegralById(id)==0)
            throw new ZSYServiceException("删除失败");
    }

    /**
     * 编辑积分求助转移
     */
    @Override
    public void updateHelpDetail(IntegralResDTO integralResDTO, Long id){
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setId(id);
        userIntegral.setDescription(integralResDTO.getDescription());
        userIntegral.setUserId(integralResDTO.getUserId());
        userIntegral.setIntegral(integralResDTO.getIntegral());
        userIntegral.setCreateTime(integralResDTO.getTime());
        if( userIntegralMapper.updateIntegralById(userIntegral)==0){
            throw new ZSYServiceException("更新求助信息失败");
        }
    }

    /**
     * 审核通过积分转移
     * @param id
     */
    @Override
    public void passReview(Long id){
        if(userIntegralMapper.updateReview(id)==0){
            throw new ZSYServiceException("更新求助信息失败");
        }
        //求助目标添加积分
        UserIntegral userIntegralFirst = userIntegralMapper.getHelpDetail(id);
        User user = userMapper.selectById(userIntegralFirst.getUserId());
        BigDecimal integral = user.getIntegral().add(userIntegralFirst.getIntegral());
        user.setIntegral(integral);
        userIntegralFirst.setDescription("求助转移积分："+userIntegralFirst.getDescription());
        if (userIntegralMapper.updateIntegralById(userIntegralFirst) == 0) {
            throw new ZSYServiceException("用户积分更新失败");
        }
        if (userMapper.updateSelectiveById(user) == 0) {
            throw new ZSYServiceException("用户积分更新失败");
        }
        //求助者积分扣除
        User userHelp = userMapper.selectById(userIntegralFirst.getUserId());
        userHelp.setIntegral(userHelp.getIntegral().subtract(userIntegralFirst.getIntegral()));
        if (userMapper.updateSelectiveById(userHelp) == 0) {
            throw new ZSYServiceException("用户积分更新失败");
        }
        userIntegralFirst.setId(snowFlakeIDHelper.nextId());
        userIntegralFirst.setIntegral(new BigDecimal(0).subtract(userIntegralFirst.getIntegral()));
        userIntegralFirst.setUserId(userIntegralFirst.getCreateBy());
        userIntegralFirst.setReviewStatus(ZSYReviewStatus.PASS.getValue());
        userIntegralMapper.insert(userIntegralFirst);
    }

    /**
     * 查询用户任务积分
     */
    @Override
    public UserTaskIntegralResDTO getPersonalIntegral() {
        Long userId = ZSYTokenRequestContext.get().getUserId();
        User user = userMapper.selectById(userId);
        Integer userLevel = user.getLevel();
        if (userLevel == null){
            throw new ZSYServiceException("当前用户暂无级别,请联系超管");
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        System.out.println("month = " + month);
        String seasonOneBeginStr = year+"-01-01 00:00:00";
        String seasonOneEndStr = year+"-03-31 23:59:59";
        String seasonTwoBeginStr = year+"-04-01 00:00:00";
        String seasonTwoEndStr = year+"-06-30 23:59:59";
        String seasonThreeBeginStr = year+"-07-01 00:00:00";
        String seasonThreeEndStr = year+"-09-30 23:59:59";
        String seasonFourBeginStr = year+"-10-01 00:00:00";
        String seasonFourEndStr = year+"-12-31 23:59:59";
        Date seasonOneBegin;
        Date seasonOneEnd;
        Date seasonTwoBegin;
        Date seasonTwoEnd;
        Date seasonThreeBegin;
        Date seasonThreeEnd;
        Date seasonFourBegin;
        Date seasonFourEnd;
        Date seasonBegin = null;
        Date seasonEnd = null;

        String thisWeekFirstDayStr = DateHelper.getThisWeekFirstDay();
        String thisWeekLastDayStr = DateHelper.getThisWeekLastDay();
        String thisMonthFirstDayStr = DateHelper.getThisMonthFirstDay();
        String thisMonthLastDayStr = DateHelper.getThisMonthLastDay();
        Date currYearFirst = DateHelper.getCurrYearFirst();
        Date currYearLast = DateHelper.getCurrYearLast();
        SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat timeSDF2 = new SimpleDateFormat("yyyy-MM-dd");
        String currYearLastStr = timeSDF2.format(currYearLast);
        Date weekFirstDay;
        Date weekLastDay;
        Date monthFirstDay;
        Date monthLastDay;
        UserTaskIntegralResDTO resDTO = new UserTaskIntegralResDTO();
        resDTO.setDevelopRole(user.getJobRole() == ZSYJobRole.PROGRAMER.getValue());
        try {
            seasonOneBegin = timeSDF.parse(seasonOneBeginStr);
            seasonOneEnd = timeSDF.parse(seasonOneEndStr);
            seasonTwoBegin = timeSDF.parse(seasonTwoBeginStr);
            seasonTwoEnd = timeSDF.parse(seasonTwoEndStr);
            seasonThreeBegin = timeSDF.parse(seasonThreeBeginStr);
            seasonThreeEnd = timeSDF.parse(seasonThreeEndStr);
            seasonFourBegin = timeSDF.parse(seasonFourBeginStr);
            seasonFourEnd = timeSDF.parse(seasonFourEndStr);
            if (month>=1 && month<=3){
                seasonBegin = seasonOneBegin;
                seasonEnd = seasonOneEnd;
            }else if (month>=4 && month<=6){
                seasonBegin = seasonTwoBegin;
                seasonEnd = seasonTwoEnd;
            }else if (month>=7 && month<=9){
                seasonBegin = seasonThreeBegin;
                seasonEnd = seasonThreeEnd;
            }else if (month>=10 && month<=12){
                seasonBegin = seasonFourBegin;
                seasonEnd = seasonFourEnd;
            }
            currYearLast = timeSDF.parse(currYearLastStr+" 23:59:59");
            calendar.setTime(timeSDF.parse(thisWeekFirstDayStr));
            calendar.add(Calendar.DAY_OF_WEEK,1);
            weekFirstDay = calendar.getTime();

            calendar.setTime(timeSDF.parse(thisWeekLastDayStr));
            calendar.add(Calendar.DAY_OF_WEEK,1);
            weekLastDay = calendar.getTime();

            monthFirstDay = timeSDF.parse(thisMonthFirstDayStr);
            monthLastDay = timeSDF.parse(thisMonthLastDayStr);

            //查询用户已完成且已评价完且含有功能点的任务  功能点的复杂度集合
            List<TaskIntegralBO> monthIntegralList = userIntegralMapper.selectTaskIntegralByUser(userId,monthFirstDay,monthLastDay);
            List<TaskIntegralBO> seasonIntegralList = userIntegralMapper.selectTaskIntegralByUser(userId,seasonBegin,seasonEnd);
            List<TaskIntegralBO> yearIntegralList = userIntegralMapper.selectTaskIntegralByUser(userId,currYearFirst,currYearLast);

            //个人任务
            List<TaskUser> monthTaskUsers = taskUserMapper.selectByUserAndTime(user.getId(),monthFirstDay,monthLastDay);
            List<TaskUser> seasonTaskUsers = taskUserMapper.selectByUserAndTime(user.getId(),seasonBegin,seasonEnd);
            List<TaskUser> yearTaskUsers = taskUserMapper.selectByUserAndTime(user.getId(),currYearFirst,currYearLast);

            //用戶级别系数
            BigDecimal userCoefficient = BigDecimal.ZERO;
            if (userLevel == 1){
                userCoefficient = BigDecimal.valueOf(0.9);
            }else if (userLevel == 2){
                userCoefficient = BigDecimal.valueOf(0.8);
            }else if (userLevel == 3){
                userCoefficient = BigDecimal.valueOf(0.7);
            }else if (userLevel == 4){
                userCoefficient = BigDecimal.valueOf(0.6);
            }else if (userLevel == 5){
                userCoefficient = BigDecimal.valueOf(0.5);
            }else if (userLevel == 6){
                userCoefficient = BigDecimal.valueOf(0.4);
            }else if (userLevel == 7){
                userCoefficient = BigDecimal.valueOf(0.3);
            }else if (userLevel == 8){
                userCoefficient = BigDecimal.valueOf(0.2);
            }else if (userLevel == 9){
                userCoefficient = BigDecimal.valueOf(0.1);
            }
            resDTO.setMonthIntegral(BigDecimal.ZERO);
            resDTO.setSeasonIntegral(BigDecimal.ZERO);
            resDTO.setYearIntegral(BigDecimal.ZERO);

            BigDecimal monthIntegral = BigDecimal.ZERO;
            BigDecimal seasonIntegral = BigDecimal.ZERO;
            BigDecimal yearIntegral = BigDecimal.ZERO;
            BigDecimal monthPrivateIntegral = BigDecimal.ZERO;
            BigDecimal seasonPrivateIntegral = BigDecimal.ZERO;
            BigDecimal yearPrivateIntegral = BigDecimal.ZERO;
            //月多人任务积分
            if (!CollectionUtils.isEmpty(monthIntegralList)){
                monthIntegral = getIntegral(monthIntegralList, userCoefficient);
                resDTO.setMonthIntegral(monthIntegral);
            }
            //季度多人任务积分
            if (!CollectionUtils.isEmpty(seasonIntegralList)){
                seasonIntegral = getIntegral(seasonIntegralList, userCoefficient);
                resDTO.setSeasonIntegral(seasonIntegral);
            }
            //年度多人任务积分
            if (!CollectionUtils.isEmpty(yearIntegralList)){
                yearIntegral = getIntegral(yearIntegralList, userCoefficient);
                resDTO.setYearIntegral(yearIntegral);
            }

            //月度个人任务积分
            BigDecimal privateTaskCoefficient = BigDecimal.valueOf(0.8);
            if (!CollectionUtils.isEmpty(monthTaskUsers)){
                monthPrivateIntegral = getPrivateIntegral(monthTaskUsers, userCoefficient, privateTaskCoefficient);
            }
            if (!CollectionUtils.isEmpty(seasonTaskUsers)){
                seasonPrivateIntegral = getPrivateIntegral(seasonTaskUsers, userCoefficient, privateTaskCoefficient);
            }
            if (!CollectionUtils.isEmpty(yearTaskUsers)){
                yearPrivateIntegral = getPrivateIntegral(yearTaskUsers, userCoefficient, privateTaskCoefficient);
            }
            resDTO.setMonthIntegral(monthIntegral.add(monthPrivateIntegral));
            resDTO.setSeasonIntegral(seasonIntegral.add(seasonPrivateIntegral));
            resDTO.setYearIntegral(yearIntegral.add(yearPrivateIntegral));
            resDTO.setUserId(user.getId());
            resDTO.setUserName(user.getName());
            resDTO.setMonthBegin(monthFirstDay);
            resDTO.setMonthEnd(monthLastDay);
            resDTO.setSeasonBegin(seasonBegin);
            resDTO.setSeasonEnd(seasonEnd);
            resDTO.setYearBegin(currYearFirst);
            resDTO.setYearEnd(currYearLast);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resDTO;
    }

    private BigDecimal getPrivateIntegral(List<TaskUser> monthTaskUsers, BigDecimal userCoefficient, BigDecimal privateTaskCoefficient) {
        Integer totalOriginIntegral = 0;
        for (TaskUser taskUser : monthTaskUsers) {
            //
            Integer privateTaskIntegral = 0;
            Integer taskLevel = taskUser.getTaskLevel();
            if (taskLevel != null){
                if (taskLevel == 1){
                    privateTaskIntegral = 1;
                }else if (taskLevel == 2){
                    privateTaskIntegral = 3;
                }else if (taskLevel == 3){
                    privateTaskIntegral = 8;
                }else if (taskLevel == 4){
                    privateTaskIntegral = 20;
                }else if (taskLevel == 5){
                    privateTaskIntegral = 40;
                }
                totalOriginIntegral += privateTaskIntegral;
            }
        }
        BigDecimal privateTotalIntegral = BigDecimal.valueOf(totalOriginIntegral).multiply(userCoefficient)
                .multiply(privateTaskCoefficient).setScale(2,BigDecimal.ROUND_HALF_UP);
        return privateTotalIntegral;
    }

    private BigDecimal getIntegral(List<TaskIntegralBO> weekIntegralList, BigDecimal userCoefficient) {
        BigDecimal totalIntegral = BigDecimal.ZERO;
        for (TaskIntegralBO weekIntegral : weekIntegralList) {
            //评分系数
            BigDecimal evaluateCoefficient = BigDecimal.ZERO;
            Integer originIntegral = 0;
            List<TaskTempFunctionBO> functions = weekIntegral.getFunctions();
            if (!CollectionUtils.isEmpty(functions)){
                for (TaskTempFunctionBO function : functions) {
                    Integer integral = 0;
                    Integer level = function.getLevel();
                    if (level == 1){
                        integral = 1;
                    }else if (level == 2){
                        integral = 3;
                    }else if (level == 3){
                        integral = 8;
                    }else if (level == 4){
                        integral = 20;
                    }else if (level == 5){
                        integral = 40;
                    }
                    originIntegral += integral;
                }

                TaskEvaluationScoreAndNumBO evaluationScoreAndNumBO = weekIntegral.getEvaluationScoreAndNumBO();
                if (evaluationScoreAndNumBO != null){
                    BigDecimal avgScore = evaluationScoreAndNumBO.getTotalScore().divide(BigDecimal.valueOf(evaluationScoreAndNumBO.getTotalNum()),2, BigDecimal.ROUND_HALF_UP);
                    if (avgScore.compareTo(BigDecimal.valueOf(4.85)) >= 0){
                        evaluateCoefficient = BigDecimal.valueOf(1);
                    }else if (avgScore.compareTo(BigDecimal.valueOf(4.85)) < 0 && avgScore.compareTo(BigDecimal.valueOf(4.6)) >= 0){
                        evaluateCoefficient = BigDecimal.valueOf(0.9);
                    }else if (avgScore.compareTo(BigDecimal.valueOf(4.6)) < 0 && avgScore.compareTo(BigDecimal.valueOf(4.3)) >= 0){
                        evaluateCoefficient = BigDecimal.valueOf(0.8);
                    }else if (avgScore.compareTo(BigDecimal.valueOf(4.3)) < 0 && avgScore.compareTo(BigDecimal.valueOf(4)) >= 0){
                        evaluateCoefficient = BigDecimal.valueOf(0.7);
                    }else if (avgScore.compareTo(BigDecimal.valueOf(4)) < 0){
                        evaluateCoefficient = BigDecimal.valueOf(0.6);
                    }
                }

                BigDecimal resultIntegral = BigDecimal.valueOf(originIntegral).multiply(userCoefficient).multiply(evaluateCoefficient).setScale(2,BigDecimal.ROUND_HALF_UP);
                totalIntegral = totalIntegral.add(resultIntegral);
            }
        }
        return totalIntegral;
    }
}
