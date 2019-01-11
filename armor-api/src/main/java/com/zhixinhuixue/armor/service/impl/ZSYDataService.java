package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.dao.IZSYDataMapper;
import com.zhixinhuixue.armor.helper.DateHelper;
import com.zhixinhuixue.armor.model.bo.AnnualFeedbackBO;
import com.zhixinhuixue.armor.model.dto.request.YearReqDTO;
import com.zhixinhuixue.armor.model.dto.response.AnnualFeedbackResDTO;
import com.zhixinhuixue.armor.model.dto.response.AnnualTaskResDTO;
import com.zhixinhuixue.armor.service.IZSYDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author SCH
 * @date 2019/1/10 13:03
 */
@Service
public class ZSYDataService implements IZSYDataService {

    @Autowired
    private IZSYDataMapper dataMapper;

    /**
     * 年度需求总数(学管端,其他)
     * @Author sch
     * @param reqDTO
     * @return
     */
    @Override
    public AnnualFeedbackResDTO getAnnualFeedbackData(YearReqDTO reqDTO) {
        Date beginTime = new Date();
        Date endTime = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (reqDTO.getWhichYear() == null){
            Date firstDate = DateHelper.getCurrYearFirst();
            beginTime = firstDate;
            Date lastDay = DateHelper.getCurrYearFirst();
            String formatDay = sdf1.format(lastDay);
            formatDay = formatDay + " 23:59:59";
            try {
                Date lastDate = sdf2.parse(formatDay);
                endTime = lastDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            Integer year = reqDTO.getWhichYear();
            String firstDay = year + "-01-01 00:00:00";
            String lastDay = year + "-12-31 23:59:59";
            try {
                beginTime = sdf2.parse(firstDay);
                endTime = sdf2.parse(lastDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Integer fromCoachNum = dataMapper.selectAnnualFeedbackFromCoach(beginTime,endTime);
        Integer otherNum = dataMapper.selectAnnualFeedbackOhter(beginTime,endTime);
        Integer totalNum = (fromCoachNum != null ? fromCoachNum : 0) + (otherNum != null ? otherNum : 0);
        AnnualFeedbackResDTO resDTO = new AnnualFeedbackResDTO();
        resDTO.setFromCoachNum(fromCoachNum != null ? fromCoachNum : 0);
        resDTO.setOtherNum(otherNum != null ? otherNum : 0);
        resDTO.setTotalNum(totalNum);
        return resDTO;
    }

    /**
     * 年度任务总数(多人任务,个人任务)
     * @author sch
     * @param reqDTO
     * @return
     */
    @Override
    public AnnualTaskResDTO getAnnualTaskData(YearReqDTO reqDTO) {
        Date beginTime = new Date();
        Date endTime = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (reqDTO.getWhichYear() == null){
            Date firstDate = DateHelper.getCurrYearFirst();
            beginTime = firstDate;
            Date lastDay = DateHelper.getCurrYearFirst();
            String formatDay = sdf1.format(lastDay);
            formatDay = formatDay + " 23:59:59";
            try {
                Date lastDate = sdf2.parse(formatDay);
                endTime = lastDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            Integer year = reqDTO.getWhichYear();
            String firstDay = year + "-01-01 00:00:00";
            String lastDay = year + "-12-31 23:59:59";
            try {
                beginTime = sdf2.parse(firstDay);
                endTime = sdf2.parse(lastDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Integer multiTaskNum = dataMapper.selectAnnualMultiTaskNum(beginTime,endTime);
        Integer singleTaskNum = dataMapper.selectAnnualSingleTaskNum(beginTime,endTime);
        Integer totalNum = (multiTaskNum != null ? multiTaskNum : 0) + (singleTaskNum != null ? singleTaskNum : 0);
        AnnualTaskResDTO annualTaskResDTO = new AnnualTaskResDTO();
        annualTaskResDTO.setMultiTaskNum(multiTaskNum != null ? multiTaskNum : 0);
        annualTaskResDTO.setSingleTaskNum(singleTaskNum != null ? singleTaskNum : 0);
        annualTaskResDTO.setTotalNum(totalNum);
        return annualTaskResDTO;
    }

    private void getTime(YearReqDTO reqDTO,Date beginTime,Date endTime) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (reqDTO.getWhichYear() == null){
            Date firstDate = DateHelper.getCurrYearFirst();
            beginTime = firstDate;
            Date lastDay = DateHelper.getCurrYearFirst();
            String formatDay = sdf1.format(lastDay);
            formatDay = formatDay + " 23:59:59";
            try {
                Date lastDate = sdf2.parse(formatDay);
                endTime = lastDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            Integer year = reqDTO.getWhichYear();
            String firstDay = year + "-01-01 00:00:00";
            String lastDay = year + "-12-31 23:59:59";
            try {
                beginTime = sdf2.parse(firstDay);
                endTime = sdf2.parse(lastDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 给定年度 获取当年第一天
     */
    private Date getFirstDayOfYear(Integer year){
        Calendar calendar = Calendar.getInstance();

        return null;
    }
}
