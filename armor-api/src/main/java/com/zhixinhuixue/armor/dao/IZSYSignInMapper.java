package com.zhixinhuixue.armor.dao;

import com.github.pagehelper.Page;
import com.zhixinhuixue.armor.model.bo.SignInBO;
import com.zhixinhuixue.armor.model.dto.request.ExtraWorkStatsReqDTO;
import com.zhixinhuixue.armor.model.dto.request.SignInReqDTO;
import com.zhixinhuixue.armor.model.pojo.ResignIn;
import com.zhixinhuixue.armor.model.pojo.SignIn;
import com.zhixinhuixue.armor.model.pojo.User;
import com.zhixinhuixue.armor.model.pojo.UserSort;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author SCH
 * @date 2019/2/22 13:28
 */
public interface IZSYSignInMapper {

    /**
     * 清空user_sort表
     * @return
     */
    int deleteUserSort();

    /**
     * 批量新增user_sort
     * @param userSortList
     * @return
     */
    int addUserSortBatch(@Param("userSortList") List<UserSort> userSortList);

    /**
     * 根据序号查用户信息
     * @param sort
     * @return
     */
    User selectUserBySort(@Param("sort") Integer sort);

    /**
     * 批量导入考勤打卡记录
     * @param signIns
     * @return
     */
    int insertSignInBatch(@Param("signIns") List<SignIn> signIns);

    /**
     * 查询全部考勤记录
     * @return
     */
    List<SignIn> selectSignInList();

    /**
     * 分页查询考勤记录
     * @param reqDTO
     * @return
     */
    Page<SignInBO> selectSignInPage(@Param("reqDTO") SignInReqDTO reqDTO);

    /**
     * 查询当前用户第二天7点前是否有打卡记录
     * @param userId
     * @return
     */
    List<SignIn> selectBefore7ByUserId(@Param("userId") Long userId, @Param("seven") Date seven, @Param("zero") Date zero);

    /**
     * 查询漏打卡考勤记录
     * @param userId
     * @return
     */
    List<SignInBO> selectForgetSignInList(@Param("userId") Long userId);

    /**
     * 新增补打卡申请
     * @param resignIn
     * @return
     */
    int addResignIn(@Param("resignIn") ResignIn resignIn);

    /**
     * 根据主键查询补打卡申请
     * @param id
     * @return
     */
    ResignIn selectResignInById(@Param("id") Long id);

    /**
     * 修改补打卡申请
     * @param resignIn
     * @param id
     * @return
     */
    int updateResignIn(@Param("resignIn") ResignIn resignIn, @Param("id") Long id);

    /**
     * 个人查询补打卡申请
     * @param userId
     * @param status
     * @return
     */
    Page<ResignIn> selectPersonalResignInPage(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 管理员查询补打卡申请
     * @param status
     * @return
     */
    Page<ResignIn> selectResignInPage(@Param("status") Integer status);

    /**
     * 删除补打卡申请
     * @param id
     * @return
     */
    int deleteResignInById(@Param("id") Long id);

    /**
     * 新增签到记录
     * @param signIn
     * @return
     */
    int addSignIn(@Param("signIn") SignIn signIn);

    /**
     * 删除指定signIn
     * @param recheckTime
     * @param userId
     * @return
     */
    int deleteSignIn(@Param("recheckTime") Date recheckTime, @Param("userId") Long userId);

    /**
     * 根据userId和checkTime查询考勤记录
     * @param userId
     * @param checkTime
     * @return
     */
    SignIn selectSignInByUserAndTime(@Param("userId") Long userId, @Param("checkTime") Date checkTime);

    /**
     * 根据userId和checkTime时间段查询考勤记录
     * @param userId
     * @param today0
     * @param today23
     * @return
     */
    SignIn selectSignInByUserAndTimeRange(@Param("userId") Long userId, @Param("today0") Date today0, @Param("today23") Date today23);

    /**
     * 删除type=2 的多余记录
     * @return
     */
    int deleteUselessSignIn(@Param("userId") Long userId, @Param("today0") Date today0, @Param("today23") Date today23);


    /**
     * 导入记录的最后一条记录打卡时间
     * @return
     */
    SignIn selectSingInLastRecord();

    /**
     * 导入记录的最先一条记录打卡时间
     * @return
     */
    SignIn selectSingInFirstRecord();

    /**
     * 查询打卡人员列表
     * @return
     */
    List<User> selectCheckInUsers(@Param("yearAndMonth") String yearAndMonth);

    /**
     * 根据用户id和给定年月查询考勤
     * @param userId
     * @param yearAndMonth
     * @return
     */
    List<SignInBO> selectPersonalSignInList(@Param("userId") Long userId, @Param("yearAndMonth") String yearAndMonth);

    /**
     * 根据月份查询当月有记录的最早时间
     * @param month
     * @return
     */
    Date selectMonthFirstTime(@Param("month") Integer month);

    /**
     * 根据月份查询当月有记录的最晚时间
     * @param month
     * @return
     */
    Date selectMonthLastTime(@Param("month") Integer month);

    /**
     * 根据月份查询当月有记录的时间集合
     * @param month
     * @return
     */
    List<Date> selectDateList(@Param("month") Integer month);

    /**
     * 查询有效打卡人员
     * @return
     */
    List<User> selectEffectUsers();

    /**
     * 查询当月所有人的考勤记录
     * @param yearAndMonth
     * @return
     */
    List<SignInBO> selectAllSignInByMonth(@Param("yearAndMonth") String yearAndMonth);

    /**
     * 查询给定年月每天0点到7点之间的考勤记录
     * @return
     */
    List<SignIn> selectAllBetween0And7(@Param("yearAndMonth") String yearAndMonth);

    /**
     * 给定年月  查询打卡日期集合
     * @param yearAndMonth
     * @return
     */
    List<Date> selectDatesByYearAndMonth(@Param("yearAndMonth")String yearAndMonth);

    /**
     * 根据userId和给定月份查询每天0点到7点之间的数据
     * @param yearAndMonth
     * @param userId
     * @return
     */
    List<SignIn> selectAllBetween0And7AndUserId(@Param("yearAndMonth")String yearAndMonth, @Param("userId")Long userId);

    /**
     * 查询签到时间记录
     * @param reqDTO
     * @return
     */
    List<Date> selectCheckTimeByUser(ExtraWorkStatsReqDTO reqDTO);
}
