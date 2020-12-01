package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author SCH
 * @date 2019/2/22 13:27
 */
public interface IZSYSignInService {

    /**
     * 上传 user-sort 文件到库
     * @param uploadFile
     */
    void addUserSortToMysql(MultipartFile uploadFile);

    /**
     * 上传考勤文件到库
     * @param uploadFile
     */
    void uploadToMysql(MultipartFile uploadFile);

    /**
     * 导入记录的最后一条记录打卡时间
     * @return
     */
    SignInLastRecordResDTO getSignInLastRecord();

    /**
     * 分页查询考勤记录
     * @param reqDTO
     * @return
     */
    PageInfo<SignInResDTO> getSignInPage(SignInReqDTO reqDTO);

    /**
     * 个人分页查询考勤统计
     * @param reqDTO
     * @return
     */
    PageInfo<SignInResDTO> getPersonalSignInPage(SignInReqDTO reqDTO);

    /**
     * 个人漏打卡考勤记录
     * @param pageNum
     * @return
     */
    ArmorPageInfo<SignInResDTO> getForgetSignInPage(Integer pageNum);

    /**
     * 补打卡申请
     * @param reqDTO
     */
    void addResignIn(ResignInReqDTO reqDTO);

    /**
     * 修改补打卡申请
     * @param reqDTO
     */
    void updateResignIn(ResignInReqDTO reqDTO, Long id);

    /**
     * 个人查询补打卡申请
     * @param status
     * @param pageNum
     * @return
     */
    PageInfo<ResignInResDTO> getPersonalByStatus(Integer status, Integer pageNum);

    /**
     * 管理员查询补打卡申请
     * @param status
     * @param pageNum
     * @return
     */
    PageInfo<ResignInResDTO> getResignInByStatus(Integer status, Integer pageNum);

    /**
     * 删除补打卡申请
     * @param id
     */
    void deleteResignIn(Long id);

    /**
     * 审核通过补打卡申请
     * @param id
     */
    void accessResignIn(ResignInReqDTO reqDTO,Long id);

    /**
     * 个人查看加班总时长
     * @param yearAndMonth
     * @return
     */
    TotalExtraHoursResDTO getPersonalTotalExtraHours(String yearAndMonth);

    /**
     * 管理员查看用户的加班总时长
     * @param userId
     * @param yearAndMonth
     * @return
     */
    TotalExtraHoursResDTO getTotalExtraHoursByUserId(Long userId, String yearAndMonth);

    /**
     * 按月导出考勤情况Excel
     * @param yearAndMonth
     * @return
     */
    String excelSignInData(String yearAndMonth);

    /**
     * 考勤人员列表
     * @return
     */
    List<SignInUser> getSignInUsers();

    /**
     * 个人查看调休时间
     * @author sch
     * @param userId 用户id
     */
    List<RestHoursResDTO> getPersonalRestHours(Long userId);

    /**
     * 个人查看调休修改日志
     * @author sch
     * @param reqDTO 条件
     */
    PageInfo<RestHoursLogPageResDTO> getPersonalRestHoursLogPage(QueryRestHoursLogReqDTO reqDTO);

    /**
     * 管理员查看用户调休修改日志
     * @author sch
     * @param reqDTO 条件
     */
    PageInfo<RestHoursLogPageResDTO> getUserRestHoursLogPage(QueryRestHoursLogReqDTO reqDTO);

    /**
     * 查看所有人调休时间
     * @author sch
     * @param reqDTO 条件
     */
    List<UserRestHoursListResDTO> getUserRestHoursList(QueryUserRestHoursReqDTO reqDTO);

    /**
     * 手动新增调休
     * @author sch
     * @param reqDTO  参数
     */
    void addUserRestHoursLog(AddUserRestHourLogReqDTO reqDTO);

    /**
     * 更新2019年10月的请假和加班产生的调休变化
     * @author sch
     */
    void updateLeaveAndEWork(LeaveAndEWorkReqDTO reqDTO);

    /**
     * 删除调休日志
     * @author sch
     * @param logId 日志id
     */
    void deleteRestHoursLog(Long logId);

    /**
     * 编辑调休日志
     * @author sch
     * @param reqDTO 参数
     */
    void editUserRestHoursLog(EditUserRestHourLogReqDTO reqDTO);

    /**
     * 分页查看原始考勤记录
     * @author sch
     * @param reqDTO 参数
     */
    PageInfo<SignInOriginResDTO> getSignInOriginPage(SignInReqDTO reqDTO);

    /**
     * 编辑原始考勤记录
     * @author sch
     * @param reqDTO 参数
     */
    void editSignIn(EditSignInReqDTO reqDTO);

    /**
     * 删除原始考勤
     * @param id id
     */
    void deleteSignIn(Long id);

    /**
     *  组内成员
     *
     * @param
     * @return
     * @author hsh
     * @create 2020/11/6-17:22
     */
    List<SignInUser> getSignInUsersGroup();
}
