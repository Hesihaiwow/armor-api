package com.zhixinhuixue.armor.service;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.ResignInReqDTO;
import com.zhixinhuixue.armor.model.dto.request.SignInReqDTO;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
     * 上传.dat的花名册文件到库
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
    void accessResignIn(Long id);

    /**
     * 个人查看加班总时长
     * @param month
     * @return
     */
    TotalExtraHoursResDTO getPersonalTotalExtraHours(Integer month);

    /**
     * 管理员查看用户的加班总时长
     * @param userId
     * @param month
     * @return
     */
    TotalExtraHoursResDTO getTotalExtraHoursByUserId(Long userId, Integer month);

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
}
