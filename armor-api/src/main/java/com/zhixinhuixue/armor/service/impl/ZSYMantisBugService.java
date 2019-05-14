package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYBugStatisticsMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.*;
import com.zhixinhuixue.armor.model.dto.request.MantisBugQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.request.MantisBugWeekQueryReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.model.pojo.MantisBugStatistics;
import com.zhixinhuixue.armor.model.pojo.MantisCategory;
import com.zhixinhuixue.armor.model.pojo.MantisUser;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.service.IZSYMantisBugService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.MantisBugSeverity;
import com.zhixinhuixue.armor.source.enums.MantisBugStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sch
 * @DATE 2019/4/17 17:39
 */
@Service
public class ZSYMantisBugService implements IZSYMantisBugService {

    @Autowired
    private IZSYBugStatisticsMapper bugStatisticsMapper;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(ZSYBugService.class);

    @Autowired
    @Qualifier("secondJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    @Autowired
    private IZSYUserMapper userMapper;


    /**
     * 查询mantis中bug信息并导入到任务系统
     * @author sch
     */
    @Override
    @Transactional
    public void importMantisBug(Integer projectId) {
//        List<Integer> bugIdList = bugStatisticsMapper.selectBugIdList();
        List<User> users = userMapper.selectEffectiveUsers(ZSYTokenRequestContext.get().getDepartmentId());
        Map<String,Long> userMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(users)){
            for (User user : users) {
                userMap.put(user.getName(),user.getId());
            }
        }
        Set<String> userNameSet = userMap.keySet();
        String bugSql =
                "SELECT mcfst.value taskId,mbt.id bugId,mbt.reporter_id reporterId,mbt.handler_id handlerId,mut1.realname reporterName,\n" +
                        "mut2.realname handlerName,mbt.severity severity,mbt.priority priority,mbt.status status,mbt.category_id categoryId,\n" +
                        "mct.name categoryName,mbt.date_submitted dateSubmitted,mbt.last_updated lastUpdated\n" +
                        "FROM mantis_custom_field_string_table mcfst\n" +
                        "LEFT JOIN mantis_bug_table mbt on mcfst.`bug_id` = mbt.id\n" +
                        "LEFT JOIN mantis_user_table mut1 on mut1.id = mbt.reporter_id\n" +
                        "LEFT JOIN mantis_user_table mut2 ON mut2.id = mbt.handler_id\n" +
                        "LEFT JOIN mantis_category_table mct ON mct.id = mbt.category_id";
        String bugSql2 =
                "SELECT mcfst.value taskId,mbt.id bugId,mbt.reporter_id reporterId,mbt.handler_id handlerId,mut1.realname reporterName,\n" +
                        "mut2.realname handlerName,mbt.severity severity,mbt.priority priority,mbt.status status,mbt.category_id categoryId,\n" +
                        "mct.name categoryName,mbt.date_submitted dateSubmitted,mbt.last_updated lastUpdated\n" +
                        "FROM mantis_bug_table mbt\n" +
                        "LEFT JOIN mantis_custom_field_string_table mcfst on mcfst.`bug_id` = mbt.id\n" +
                        "LEFT JOIN mantis_user_table mut1 on mut1.id = mbt.reporter_id\n" +
                        "LEFT JOIN mantis_user_table mut2 ON mut2.id = mbt.handler_id\n" +
                        "LEFT JOIN mantis_category_table mct ON mct.id = mbt.category_id\n" +
                        "WHERE mbt.project_id = " + projectId;

        String userSql = "SELECT id,username,realname FROM mantis_user_table";
        String categorySql =
                "SELECT mct.id categoryId,mct.name categoryName,mct.user_id userId,mct.project_id projectId,mut.realName userName FROM mantis_category_table mct \n" +
                "LEFT JOIN mantis_user_table mut on mut.id = mct.user_id\n" +
                "WHERE mct.project_id = 57";
        long time1 = System.currentTimeMillis();
        List<MantisBugStatistics> list = jdbcTemplate.query(bugSql, new MyRowMapper());
        List<MantisBugStatistics> list2 = jdbcTemplate.query(bugSql2, new MyRowMapper());
        List<MantisUser> userList = jdbcTemplate.query(userSql, new RowMapper<MantisUser>() {
            @Override
            public MantisUser mapRow(ResultSet resultSet, int i) throws SQLException {
                String id = resultSet.getString("id");
                String username = resultSet.getString("username");
                String realname = resultSet.getString("realname");
                if (id == null || username == null || realname == null) {
                    throw new ZSYServiceException("请检查mantis_user_table中数据是否有空值");
                }
                MantisUser mantisUser = new MantisUser();
                mantisUser.setId(snowFlakeIDHelper.nextId());
                mantisUser.setUserId(Integer.valueOf(id));
                mantisUser.setUserName(username);
                mantisUser.setRealName(realname);
                return mantisUser;
            }
        });
        List<MantisCategory> categoryList = jdbcTemplate.query(categorySql, new RowMapper<MantisCategory>() {
            @Override
            public MantisCategory mapRow(ResultSet resultSet, int i) throws SQLException {
                String id = resultSet.getString("categoryId");
                String name = resultSet.getString("categoryName");
                String userId = resultSet.getString("userId");
                String projectId1 = resultSet.getString("projectId");
                String userName = resultSet.getString("userName");
                if (id == null || name == null || userId == null || projectId1 == null || userName == null) {
                    throw new ZSYServiceException("请检查mantis_category_table中数据是否有空值");
                }
                MantisCategory mantisCategory = new MantisCategory();
                mantisCategory.setId(snowFlakeIDHelper.nextId());
                mantisCategory.setCategoryId(Integer.valueOf(id));
                mantisCategory.setCategoryName(name);
                mantisCategory.setUserId(Integer.valueOf(userId));
                mantisCategory.setProjectId(Integer.valueOf(projectId1));
                if (userNameSet.contains(userName)){
                    mantisCategory.setSysUserId(userMap.get(userName));
                }else {
                    throw new ZSYServiceException("姓名为: "+userName+" 的用户在积分系统中没有找到,请检查");
                }
                mantisCategory.setUserName(userName);
                return mantisCategory;
            }
        });
        List<MantisBugStatistics> mantisBugStatisticsList = new ArrayList<>();
        mantisBugStatisticsList.addAll(list);
        mantisBugStatisticsList.addAll(list2);
        long time2 = System.currentTimeMillis();
        System.out.println("查询+准备耗时: "+(time2-time1)+"ms");
        logger.info("查询+准备耗时: "+(time2-time1)+"ms");
        if (!CollectionUtils.isEmpty(mantisBugStatisticsList)){

            userList = userList.stream().distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(userList)){
                //清空原来的mantis_user
                bugStatisticsMapper.deleteAllUsers();
                if (bugStatisticsMapper.insertUserBatch(userList) == 0){
                    throw new ZSYServiceException("批量插入用户失败");
                }
            }

            categoryList = categoryList.stream().distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(categoryList)){
                //清空原来的mantis_category
                bugStatisticsMapper.deleteAllCategory();
                if (bugStatisticsMapper.insertCategoryBatch(categoryList) == 0){
                    throw new ZSYServiceException("批量插入分类失败");
                }
            }

            //清空原来的数据
            bugStatisticsMapper.deleteAllBugStats();
            mantisBugStatisticsList = mantisBugStatisticsList.stream().sorted(Comparator.comparing(MantisBugStatistics::getBugId))
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(MantisBugStatistics::getBugId))
                            ),ArrayList::new));
            if (!CollectionUtils.isEmpty(mantisBugStatisticsList)){
                if (bugStatisticsMapper.insertBatch(mantisBugStatisticsList) == 0){
                    throw new ZSYServiceException("导入bug信息失败");
                }
                long time3 = System.currentTimeMillis();
                System.out.println("批量插入耗时: "+(time3-time2)+"ms");
                logger.info("批量插入耗时: "+(time3-time2)+"ms");
            }
        }else {
            throw new ZSYServiceException("暂无数据需要导入,请检查");
        }

    }

    /**
     * 查询mantis中的bug信息导出Excel
     * @author sch
     * @param projectId
     */
    @Override
    public void exportMantisBug(Integer projectId) {

    }

    /**
     * @author sch
     * 查询mantis用户信息
     * @return
     */
    @Override
    public List<MantisBugUserBaseInfoResDTO> getMantisUserList() {
        String sql = "SELECT id userId,realname userName FROM mantis_user_table ORDER BY id";
        List<MantisBugUserBaseInfoResDTO> list = jdbcTemplate.query(sql, new RowMapper<MantisBugUserBaseInfoResDTO>() {
            @Override
            public MantisBugUserBaseInfoResDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                MantisBugUserBaseInfoResDTO mantisBugUserBaseInfoResDTO = new MantisBugUserBaseInfoResDTO();
                mantisBugUserBaseInfoResDTO.setUserId(resultSet.getString("userId") == null ? null : Integer.valueOf(resultSet.getString("userId")));
                mantisBugUserBaseInfoResDTO.setUserName(resultSet.getString("userName"));
                return mantisBugUserBaseInfoResDTO;
            }
        });
        return list;
    }

    /**
     * 查询mantis反馈系统信息
     * @author sch
     * @return
     */
    @Override
    public List<MantisCategoryResDTO> getMantisCategory() {
        String sql = "SELECT id,name FROM mantis_category_table WHERE project_id = 57 ORDER BY id";
        List<MantisCategoryResDTO> list = jdbcTemplate.query(sql, new RowMapper<MantisCategoryResDTO>() {
            @Override
            public MantisCategoryResDTO mapRow(ResultSet resultSet, int i) throws SQLException {
                MantisCategoryResDTO mantisCategoryResDTO = new MantisCategoryResDTO();
                mantisCategoryResDTO.setId(resultSet.getString("id") == null ? null : Integer.valueOf(resultSet.getString("id")));
                mantisCategoryResDTO.setName(resultSet.getString("name"));
                return mantisCategoryResDTO;
            }
        });
        return list;
    }

    /**
     * 查询反馈系统(分类)
     * @author sch
     * @return
     */
    @Override
    public List<MantisCategoryResDTO> getCategoryList() {
        List<MantisCategory> categoryList = bugStatisticsMapper.selectCategoryList();
        List<MantisCategoryResDTO> categoryResDTOList = new ArrayList<>();
        BeanUtils.copyProperties(categoryList,categoryResDTOList);
        if (!CollectionUtils.isEmpty(categoryList)){
            categoryList.stream().forEach(category -> {
                MantisCategoryResDTO mantisCategoryResDTO = new MantisCategoryResDTO();
                mantisCategoryResDTO.setId(category.getCategoryId());
                mantisCategoryResDTO.setName(category.getCategoryName());
                categoryResDTOList.add(mantisCategoryResDTO);
            });
        }
        return categoryResDTOList;
    }

    /**
     * 按年月查询测试人员bug统计情况
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public List<MantisBugStatisticsResDTO> getBugStatsGroupByUser(MantisBugQueryReqDTO reqDTO) {
        Date beginTime = reqDTO.getBeginTime();
        Date endTime = reqDTO.getEndTime();
        Long dateSubmittedBegin = 0l;
        Long dateSubmittedEnd = 0l;
        if (beginTime != null){
            dateSubmittedBegin = beginTime.getTime() / 1000;
        }
        if (endTime != null){
            dateSubmittedEnd = endTime.getTime() / 1000;
        }
        List<MantisBugStatisticsBO> bugStatisticsBOList = bugStatisticsMapper.selectBugStatsGroupByUser(dateSubmittedBegin,dateSubmittedEnd);
        List<MantisBugStatisticsResDTO> bugStatisticsResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(bugStatisticsBOList)){
            bugStatisticsBOList.stream().forEach(bugStatisticsBO ->{
                MantisBugStatisticsResDTO resDTO = new MantisBugStatisticsResDTO();
                resDTO.setUserId(bugStatisticsBO.getUserId());
                resDTO.setRealName(bugStatisticsBO.getUserName());
                resDTO.setMantisBugTotalNum(bugStatisticsBO.getMantisBugTotalNum());
                List<MantisCategory> categoryList = bugStatisticsBO.getCategoryList();
                List<Integer> categoryIds = categoryList.stream().map(MantisCategory::getCategoryId).collect(Collectors.toList());
                List<MantisCategoryResDTO> categoryResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(categoryList)){
                    categoryList.stream().forEach(category ->{
                        MantisCategoryResDTO mantisCategoryResDTO = new MantisCategoryResDTO();
                        mantisCategoryResDTO.setId(category.getCategoryId());
                        mantisCategoryResDTO.setName(category.getCategoryName());
                        categoryResDTOList.add(mantisCategoryResDTO);
                    });
                }

                List<MantisBugSeverityNumBO> bugSeverityNumBOS = bugStatisticsBO.getBugSeverityNumBOS();
                List<MantisBugSeverityNumResDTO> bugSeverityNumResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(bugSeverityNumBOS)){
                    bugSeverityNumBOS.stream().forEach(bugSeverityNumBO ->{
                        MantisBugSeverityNumResDTO mantisBugSeverityNumResDTO = new MantisBugSeverityNumResDTO();
                        mantisBugSeverityNumResDTO.setSeverity(bugSeverityNumBO.getSeverity());
                        mantisBugSeverityNumResDTO.setBugNum(bugSeverityNumBO.getBugNum());
                        mantisBugSeverityNumResDTO.setSeverityName(MantisBugSeverity.getName(bugSeverityNumBO.getSeverity()));
                        bugSeverityNumResDTOList.add(mantisBugSeverityNumResDTO);
                    });
                }
                resDTO.setMantisBugSeverityNumResDTOList(bugSeverityNumResDTOList);

                List<MantisBugStatusNumBO> bugStatusNumBOS = bugStatisticsBO.getBugStatusNumBOS();
                List<MantisBugStatusNumResDTO> bugStatusNumResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(bugStatusNumBOS)){
                    bugStatusNumBOS.stream().forEach(bugStatusNumBO ->{
                        MantisBugStatusNumResDTO mantisBugStatusNumResDTO = new MantisBugStatusNumResDTO();
                        mantisBugStatusNumResDTO.setStatus(bugStatusNumBO.getStatus());
                        mantisBugStatusNumResDTO.setStatusName(MantisBugStatus.getName(bugStatusNumBO.getStatus()));
                        mantisBugStatusNumResDTO.setBugNum(bugStatusNumBO.getBugNum());
                        bugStatusNumResDTOList.add(mantisBugStatusNumResDTO);
                    });
                }
                resDTO.setMantisBugStatusNumResDTOList(bugStatusNumResDTOList);

                List<MantisBugCategoryNumBO> bugCategoryNumBOS = bugStatisticsBO.getBugCategoryNumBOS();
                List<MantisBugCategoryNumResDTO> bugCategoryNumResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(bugCategoryNumBOS)){
                    bugCategoryNumBOS.stream().forEach(bugCategoryNumBO->{
                        MantisBugCategoryNumResDTO mantisBugCategoryNumResDTO = new MantisBugCategoryNumResDTO();
                        mantisBugCategoryNumResDTO.setCategory(bugCategoryNumBO.getCategory());
                        mantisBugCategoryNumResDTO.setCategoryName(bugCategoryNumBO.getCategoryName());
                        mantisBugCategoryNumResDTO.setUserId(bugCategoryNumBO.getUserId());
                        mantisBugCategoryNumResDTO.setRealName(bugCategoryNumBO.getRealName());
                        mantisBugCategoryNumResDTO.setBugNum(bugCategoryNumBO.getBugNum());
                        if (categoryIds.contains(bugCategoryNumBO.getCategory())){
                            mantisBugCategoryNumResDTO.setIsInCharge(1);
                        }else {
                            mantisBugCategoryNumResDTO.setIsInCharge(0);
                        }
                        bugCategoryNumResDTOList.add(mantisBugCategoryNumResDTO);
                    });
                }
                resDTO.setMantisBugCategoryNumResDTOList(bugCategoryNumResDTOList);
                resDTO.setCategoryResDTOList(categoryResDTOList);
                resDTO.setMantisBugTotalNum(bugStatisticsBO.getMantisBugTotalNum());

                List<OnlineBugCategoryNumBO> onlineBugCategoryNumBOS = bugStatisticsMapper.selectBugCategoryNum(bugStatisticsBO.getSysUserId(),beginTime,endTime);
                List<OnlineBugCategoryNumResDTO> onlineBugCategoryNumResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(onlineBugCategoryNumBOS)){
                    onlineBugCategoryNumBOS.stream().forEach(onlineBugCategoryNumBO -> {
                        OnlineBugCategoryNumResDTO onlineBugCategoryNumResDTO = new OnlineBugCategoryNumResDTO();
                        onlineBugCategoryNumResDTO.setUserId(bugStatisticsBO.getUserId());
                        onlineBugCategoryNumResDTO.setRealName(bugStatisticsBO.getUserName());
                        onlineBugCategoryNumResDTO.setDemandSystemId(onlineBugCategoryNumBO.getDemandSystemId());
                        onlineBugCategoryNumResDTO.setDemandSystemName(onlineBugCategoryNumBO.getDemandSystemName());
                        onlineBugCategoryNumResDTO.setBugNum(onlineBugCategoryNumBO.getBugNum());
                        if (categoryIds.contains(onlineBugCategoryNumBO.getDemandSystemId())){
                            onlineBugCategoryNumResDTO.setIsInCharge(1);
                        }else {
                            onlineBugCategoryNumResDTO.setIsInCharge(0);
                        }
                        onlineBugCategoryNumResDTOList.add(onlineBugCategoryNumResDTO);
                    });
                }
                resDTO.setOnlineBugCategoryNumResDTOList(onlineBugCategoryNumResDTOList);
                bugStatisticsResDTOList.add(resDTO);
            });
        }
        return bugStatisticsResDTOList;
    }

    /**
     * 查询测试人员周bug数量图表
     * @author sch
     * @return
     */
    public List<MantisBugUserWeekResDTO> getBugWeekGroupByUser(MantisBugWeekQueryReqDTO reqDTO) {
        SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = new ArrayList<>();
        List<String> weekdayList = new ArrayList<>();
        Date beginTime = reqDTO.getBeginTime();
        Date endTime = reqDTO.getEndTime();
        Long beginTimeInt = 0l;
        Long endTimeInt = 0l;
        if (beginTime != null){
            beginTimeInt = beginTime.getTime() / 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginTime);
            for (int i = 0;i < 7;i ++){
                if (i!=0){
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                }
                String dateStr = dateSDF.format(calendar.getTime());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek){
                    case 1:
                        weekdayList.add("星期日");
                        break;
                    case 2:
                        weekdayList.add("星期一");
                        break;
                    case 3:
                        weekdayList.add("星期二");
                        break;
                    case 4:
                        weekdayList.add("星期三");
                        break;
                    case 5:
                        weekdayList.add("星期四");
                        break;
                    case 6:
                        weekdayList.add("星期五");
                        break;
                    case 7:
                        weekdayList.add("星期六");
                        break;
                    default:
                        weekdayList.add("");
                        break;
                }
                dateList.add(dateStr);
            }
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            for (int i = 0; i < 7; i++) {
                if (i!=0){
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                }
                String dateStr = dateSDF.format(calendar.getTime());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek) {
                    case 1:
                        weekdayList.add("星期日");
                        break;
                    case 2:
                        weekdayList.add("星期一");
                        break;
                    case 3:
                        weekdayList.add("星期二");
                        break;
                    case 4:
                        weekdayList.add("星期三");
                        break;
                    case 5:
                        weekdayList.add("星期四");
                        break;
                    case 6:
                        weekdayList.add("星期五");
                        break;
                    case 7:
                        weekdayList.add("星期六");
                        break;
                    default:
                        weekdayList.add("");
                        break;
                }
                dateList.add(dateStr);
            }
        }
        if (endTime != null){
            endTimeInt = endTime.getTime() / 1000;
        }
        List<MantisBugUserWeekBO> bugUserWeekBOS = bugStatisticsMapper.selectBugWeekGroupByUser(beginTimeInt,endTimeInt);
        List<MantisBugUserWeekResDTO> bugUserWeekResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(bugUserWeekBOS)){
            bugUserWeekBOS.stream().forEach(bugUserWeekBO->{
                MantisBugUserWeekResDTO mantisBugUserWeekResDTO = new MantisBugUserWeekResDTO();
                mantisBugUserWeekResDTO.setUserId(bugUserWeekBO.getUserId());
                mantisBugUserWeekResDTO.setUserName(bugUserWeekBO.getUserName());
                List<MantisBugWeekNumBO> mantisBugWeekNumBOS = bugUserWeekBO.getMantisBugWeekNumBOS();
                Map<String,Integer> dateBugNumMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(mantisBugWeekNumBOS)){
                    for (MantisBugWeekNumBO mantisBugWeekNumBO : mantisBugWeekNumBOS) {
                        dateBugNumMap.put(mantisBugWeekNumBO.getDate(),mantisBugWeekNumBO.getBugNum());
                    }
                }
                Set<String> dateSet = dateBugNumMap.keySet();
                List<Integer> bugNumList = new ArrayList<>();
                for (String dateStr : dateList) {
                    if (dateSet.contains(dateStr)){
                        bugNumList.add(dateBugNumMap.get(dateStr));
                    }else {
                        bugNumList.add(0);
                    }
                }
                mantisBugUserWeekResDTO.setDateStrList(dateList);
                mantisBugUserWeekResDTO.setWeekdayList(weekdayList);
                mantisBugUserWeekResDTO.setBugNumList(bugNumList);
                bugUserWeekResDTOList.add(mantisBugUserWeekResDTO);
            });
        }
        return bugUserWeekResDTOList;
    }


    /**
     * 查询测试人员月bug数量图表
     * @param reqDTO
     * @return
     */
    @Override
    public List<MantisBugUserWeekResDTO> getBugMonthGroupByUser(MantisBugWeekQueryReqDTO reqDTO) {
        SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = new ArrayList<>();
        List<String> weekdayList = new ArrayList<>();
        Date beginTime = reqDTO.getBeginTime();
        Date endTime = reqDTO.getEndTime();
        Long beginTimeInt = 0l;
        Long endTimeInt = 0l;
        Integer range = 0;
        if (beginTime != null && endTime != null){
            List<String> daysBetweenTwoDate = DateHelper.getDaysBetweenTwoDate(beginTime, endTime);
            range = daysBetweenTwoDate.size();
            beginTimeInt = beginTime.getTime() / 1000;
            endTimeInt = endTime.getTime() / 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginTime);
            for (int i = 0;i < range;i ++){
                if (i!=0){
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                }
                String dateStr = dateSDF.format(calendar.getTime());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek){
                    case 1:
                        weekdayList.add("星期日");
                        break;
                    case 2:
                        weekdayList.add("星期一");
                        break;
                    case 3:
                        weekdayList.add("星期二");
                        break;
                    case 4:
                        weekdayList.add("星期三");
                        break;
                    case 5:
                        weekdayList.add("星期四");
                        break;
                    case 6:
                        weekdayList.add("星期五");
                        break;
                    case 7:
                        weekdayList.add("星期六");
                        break;
                    default:
                        weekdayList.add("");
                        break;
                }
                dateList.add(dateStr);
            }
        }else {
            String thisMonthFirstDay = DateHelper.getThisMonthFirstDay();
            String thisMonthLastDay = DateHelper.getThisMonthLastDay();
            Date firstDay = new Date();
            Date lastDay;
            SimpleDateFormat timeSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                firstDay = timeSDF.parse(thisMonthFirstDay);
                lastDay = timeSDF.parse(thisMonthLastDay);
                range = DateHelper.getDaysBetweenTwoDate(firstDay,lastDay).size();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(firstDay);
            for (int i = 0; i < range; i++) {
                if (i!=0){
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                }
                String dateStr = dateSDF.format(calendar.getTime());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                switch (dayOfWeek) {
                    case 1:
                        weekdayList.add("星期日");
                        break;
                    case 2:
                        weekdayList.add("星期一");
                        break;
                    case 3:
                        weekdayList.add("星期二");
                        break;
                    case 4:
                        weekdayList.add("星期三");
                        break;
                    case 5:
                        weekdayList.add("星期四");
                        break;
                    case 6:
                        weekdayList.add("星期五");
                        break;
                    case 7:
                        weekdayList.add("星期六");
                        break;
                    default:
                        weekdayList.add("");
                        break;
                }
                dateList.add(dateStr);
            }
        }

        List<MantisBugUserWeekBO> bugUserWeekBOS = bugStatisticsMapper.selectBugWeekGroupByUser(beginTimeInt,endTimeInt);
        List<MantisBugUserWeekResDTO> bugUserWeekResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(bugUserWeekBOS)){
            bugUserWeekBOS.stream().forEach(bugUserWeekBO->{
                MantisBugUserWeekResDTO mantisBugUserWeekResDTO = new MantisBugUserWeekResDTO();
                mantisBugUserWeekResDTO.setUserId(bugUserWeekBO.getUserId());
                mantisBugUserWeekResDTO.setUserName(bugUserWeekBO.getUserName());
                List<MantisBugWeekNumBO> mantisBugWeekNumBOS = bugUserWeekBO.getMantisBugWeekNumBOS();
                Map<String,Integer> dateBugNumMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(mantisBugWeekNumBOS)){
                    for (MantisBugWeekNumBO mantisBugWeekNumBO : mantisBugWeekNumBOS) {
                        dateBugNumMap.put(mantisBugWeekNumBO.getDate(),mantisBugWeekNumBO.getBugNum());
                    }
                }
                Set<String> dateSet = dateBugNumMap.keySet();
                List<Integer> bugNumList = new ArrayList<>();
                for (String dateStr : dateList) {
                    if (dateSet.contains(dateStr)){
                        bugNumList.add(dateBugNumMap.get(dateStr));
                    }else {
                        bugNumList.add(0);
                    }
                }
                mantisBugUserWeekResDTO.setDateStrList(dateList);
                mantisBugUserWeekResDTO.setWeekdayList(weekdayList);
                mantisBugUserWeekResDTO.setBugNumList(bugNumList);
                bugUserWeekResDTOList.add(mantisBugUserWeekResDTO);
            });
        }
        return bugUserWeekResDTOList;
    }

    /**
     * 查询测试人员线上bug月数量图表
     * @param reqDTO
     * @return
     */
    @Override
    public List<OnlineBugUserMonthResDTO> getOnlineBugGroupByUser(MantisBugWeekQueryReqDTO reqDTO) {
        Date beginTime = reqDTO.getBeginTime();
        Date endTime = reqDTO.getEndTime();
        List<OnlineBugUserMonthBO> onlineBugUserMonthBOS = bugStatisticsMapper.selectBugMonthGroupByUser(beginTime,endTime);
        List<OnlineBugUserMonthResDTO> onlineBugUserMonthResDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(onlineBugUserMonthBOS)){
            onlineBugUserMonthBOS.stream().forEach(onlineBugUserMonthBO -> {
                OnlineBugUserMonthResDTO resDTO = new OnlineBugUserMonthResDTO();
                BeanUtils.copyProperties(onlineBugUserMonthBO,resDTO);
                onlineBugUserMonthResDTOList.add(resDTO);
            });
        }
        return onlineBugUserMonthResDTOList;
    }

    /**
     * 按任务统计bug
     * @param reqDTO
     * @return
     */
    @Override
    public PageInfo<MantisBugGroupByTaskResDTO> getBugStatsGroupByTask(MantisBugQueryReqDTO reqDTO) {
        Date beginTime = reqDTO.getBeginTime();
        Date endTime = reqDTO.getEndTime();
        Long dateSubmittedBegin = 0l;
        Long dateSubmittedEnd = 0l;
        if (beginTime != null){
            dateSubmittedBegin = beginTime.getTime() / 1000;
        }
        if (endTime != null){
            dateSubmittedEnd = endTime.getTime() / 1000;
        }
        PageHelper.startPage(Optional.ofNullable(reqDTO.getPageNum()).orElse(1), ZSYConstants.PAGE_SIZE);
        Page<MantisBugGroupByTaskBO> mantisBugGroupByTaskBOS = bugStatisticsMapper.selectBugStatsGroupByTask(dateSubmittedBegin,dateSubmittedEnd);
        Page<MantisBugGroupByTaskResDTO> mantisBugGroupByTaskResDTOList = new Page<>();
        BeanUtils.copyProperties(mantisBugGroupByTaskBOS,mantisBugGroupByTaskResDTOList);
        if (!CollectionUtils.isEmpty(mantisBugGroupByTaskBOS)){
            mantisBugGroupByTaskBOS.stream().forEach(mantisBugGroupByTaskBO -> {
                MantisBugGroupByTaskResDTO resDTO = new MantisBugGroupByTaskResDTO();
                resDTO.setTaskId(mantisBugGroupByTaskBO.getTaskId());
                resDTO.setTaskName(mantisBugGroupByTaskBO.getTaskName());
                resDTO.setTotalBugNum(mantisBugGroupByTaskBO.getTotalBugNum());

                List<MantisBugTesterNumBO> mantisBugTesterNumBOS = mantisBugGroupByTaskBO.getMantisBugTesterNumBOS();
                List<MantisBugTesterNumResDTO> mantisBugTesterNumResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(mantisBugTesterNumBOS)){
                    mantisBugTesterNumBOS.stream().forEach(mantisBugTesterNumBO -> {
                        MantisBugTesterNumResDTO mantisBugTesterNumResDTO = new MantisBugTesterNumResDTO();
                        BeanUtils.copyProperties(mantisBugTesterNumBO,mantisBugTesterNumResDTO);
                        mantisBugTesterNumResDTOList.add(mantisBugTesterNumResDTO);
                    });
                }
                resDTO.setMantisBugTesterNumResDTOList(mantisBugTesterNumResDTOList);

                List<MantisBugDeveloperNumBO> mantisBugDeveloperNumBOS = mantisBugGroupByTaskBO.getMantisBugDeveloperNumBOS();
                List<MantisBugDeveloperNumResDTO> mantisBugDeveloperNumResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(mantisBugDeveloperNumBOS)){
                    mantisBugDeveloperNumBOS.stream().forEach(mantisBugDeveloperNumBO -> {
                        MantisBugDeveloperNumResDTO mantisBugDeveloperNumResDTO = new MantisBugDeveloperNumResDTO();
                        BeanUtils.copyProperties(mantisBugDeveloperNumBO,mantisBugDeveloperNumResDTO);
                        mantisBugDeveloperNumResDTOList.add(mantisBugDeveloperNumResDTO);
                    });
                }
                resDTO.setMantisBugDeveloperNumResDTOList(mantisBugDeveloperNumResDTOList);

                List<MantisBugSeverityNumBO> mantisBugSeverityNumBOS = mantisBugGroupByTaskBO.getMantisBugSeverityNumBOS();
                List<MantisBugSeverityNumResDTO> mantisBugSeverityNumResDTOList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(mantisBugSeverityNumBOS)){
                    mantisBugSeverityNumBOS.stream().forEach(mantisBugSeverityNumBO -> {
                        MantisBugSeverityNumResDTO mantisBugSeverityNumResDTO = new MantisBugSeverityNumResDTO();
                        mantisBugSeverityNumResDTO.setSeverity(mantisBugSeverityNumBO.getSeverity());
                        mantisBugSeverityNumResDTO.setSeverityName(MantisBugSeverity.getName(mantisBugSeverityNumBO.getSeverity()));
                        mantisBugSeverityNumResDTO.setBugNum(mantisBugSeverityNumBO.getBugNum());
                        mantisBugSeverityNumResDTOList.add(mantisBugSeverityNumResDTO);
                    });
                }
                resDTO.setMantisBugSeverityNumResDTOList(mantisBugSeverityNumResDTOList);
                mantisBugGroupByTaskResDTOList.add(resDTO);
            });
        }
        return new PageInfo<>(mantisBugGroupByTaskResDTOList);
    }

    class MyRowMapper implements RowMapper<MantisBugStatistics> {

        @Override
        public MantisBugStatistics mapRow(ResultSet resultSet, int i) throws SQLException {
            MantisBugStatistics mantisBugStatistics = new MantisBugStatistics();
            mantisBugStatistics.setBsId(snowFlakeIDHelper.nextId());
            mantisBugStatistics.setBugId(Integer.valueOf(resultSet.getString("bugId")));
            mantisBugStatistics.setTaskId(resultSet.getString("taskId") == null ? null : Long.valueOf(resultSet.getString("taskId")));
            mantisBugStatistics.setCategoryId(Integer.valueOf(resultSet.getString("categoryId")));
            mantisBugStatistics.setCategoryName(resultSet.getString("categoryName"));
            mantisBugStatistics.setHandlerId(Integer.valueOf(resultSet.getString("handlerId")));
            mantisBugStatistics.setHandlerName(resultSet.getString("handlerName"));
            mantisBugStatistics.setReporterId(Integer.valueOf(resultSet.getString("reporterId")));
            mantisBugStatistics.setReporterName(resultSet.getString("reporterName"));
            mantisBugStatistics.setStatus(Integer.valueOf(resultSet.getString("status")));
            mantisBugStatistics.setSeverity(Integer.valueOf(resultSet.getString("severity")));
            mantisBugStatistics.setPriority(Integer.valueOf(resultSet.getString("priority")));
            mantisBugStatistics.setDateSubmitted(Integer.valueOf(resultSet.getString("dateSubmitted")));
            mantisBugStatistics.setLastUpdated(Integer.valueOf(resultSet.getString("lastUpdated")));
            mantisBugStatistics.setImportTime(new Date());
            return mantisBugStatistics;
        }
    }
}
