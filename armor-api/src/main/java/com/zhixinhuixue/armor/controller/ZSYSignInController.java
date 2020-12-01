package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.service.IZSYSignInService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author SCH
 * @date 2019/2/22 13:25
 */
@RestController
@RequestMapping("/api/sign-in")
public class ZSYSignInController {
    @Resource
    private IZSYSignInService signInService;

    /**
     * 上传 user-sort 文件到库
     * @param uploadFile
     * @return
     */
    @RequestMapping(value = "upload/user-sort/repository",produces = "application/json; charset=utf-8")
    @ResponseBody
    public String addUserSortToMysql(@RequestParam(value = "uploadFile") MultipartFile uploadFile){
        signInService.addUserSortToMysql(uploadFile);
        return ZSYResult.success().build();
    }

    /**
     * 上传用户考勤.dat文件到库
     * @param uploadFile
     * @return
     */
    @RequestMapping(value = "upload/sign-in/repository",produces = "application/json; charset=utf-8")
    @ResponseBody
    public String uploadToMysql(@RequestParam(value = "uploadFile") MultipartFile uploadFile){
        signInService.uploadToMysql(uploadFile);
        return ZSYResult.success().build();
    }

    /**
     * 导入记录的最后一条记录打卡时间
     * @return
     */
    @GetMapping("/last-record")
    public String getSignInLastRecord(){
        SignInLastRecordResDTO resDTO = signInService.getSignInLastRecord();
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 分页查询考勤统计(默认查当天)
     * @param reqDTO
     * @return
     */
    @PostMapping("/page")
    public String getSignInPage(@RequestBody SignInReqDTO reqDTO){
        PageInfo<SignInResDTO> resDTO = signInService.getSignInPage(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 个人分页查询考勤统计
     * @param reqDTO
     * @return
     */
    @PostMapping("/page/personal")
    public String getPersonalSignInPage(@RequestBody SignInReqDTO reqDTO){
        PageInfo<SignInResDTO> resDTO = signInService.getPersonalSignInPage(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 个人漏打卡考勤记录
     * @param pageNum
     * @return
     */
    @GetMapping("/page/personal/forget/{pageNum}")
    public String getForgetSignInPage(@PathVariable("pageNum")Integer pageNum){
        ArmorPageInfo<SignInResDTO> resDTO = signInService.getForgetSignInPage(pageNum);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 补打卡申请
     * @param reqDTO
     * @return
     */
    @PostMapping("/resign-in/add")
    public String addResignIn(@RequestBody ResignInReqDTO reqDTO){
        signInService.addResignIn(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 修改补打卡申请
     * @param reqDTO
     * @param id
     * @return
     */
    @PostMapping("/resign-in/update/{id}")
    public String updateResignIn(@RequestBody ResignInReqDTO reqDTO,@PathVariable("id")Long id){
        signInService.updateResignIn(reqDTO,id);
        return ZSYResult.success().build();
    }

    /**
     * 删除补打卡申请
     * @param id
     * @return
     */
    @DeleteMapping("/resign-in/delete/{id}")
    public String deleteResignIn(@PathVariable("id")Long id){
        signInService.deleteResignIn(id);
        return ZSYResult.success().build();
    }

    /**
     * 审核通过补打卡申请
     * @param reqDTO
     * @param id
     * @return
     */
    @PutMapping("/resign-in/access/{id}")
    public String accessResignIn(@RequestBody ResignInReqDTO reqDTO ,@PathVariable("id")Long id){
        signInService.accessResignIn(reqDTO,id);
        return ZSYResult.success().build();
    }


    /**
     * 个人查询补打卡申请
     * @param status
     * @param pageNum
     * @return
     */
    @GetMapping("/resign-in/page/personal/{status}/{pageNum}")
    public String getPersonalByStatus(@PathVariable("status")Integer status,@PathVariable("pageNum")Integer pageNum){
        PageInfo<ResignInResDTO> resDTO = signInService.getPersonalByStatus(status,pageNum);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 管理员查询补打卡申请
     * @param status
     * @param pageNum
     * @return
     */
    @GetMapping("/resign-in/page/{status}/{pageNum}")
    public String getResignInByStatus(@PathVariable("status")Integer status,@PathVariable("pageNum")Integer pageNum){
        PageInfo<ResignInResDTO> resDTO = signInService.getResignInByStatus(status,pageNum);
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 个人查看加班总时长
     * @param reqDTO
     * @return
     */
    @PostMapping("/extra-hours/total/personal")
    public String getPersonalTotalExtraHours(@RequestBody YearAndMonthReqDTO reqDTO){
        TotalExtraHoursResDTO resDTO = signInService.getPersonalTotalExtraHours(reqDTO.getYearAndMonth());
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 查看指定用户加班总时长
     * @param userId
     * @param reqDTO
     * @return
     */
    @PostMapping("/extra-hours/total/{userId}")
    public String getTotalExtraHoursByUserId(@PathVariable("userId")Long userId,@RequestBody YearAndMonthReqDTO reqDTO){
        TotalExtraHoursResDTO resDTO = signInService.getTotalExtraHoursByUserId(userId,reqDTO.getYearAndMonth());
        return ZSYResult.success().data(resDTO).build();
    }

    /**
     * 按月导出考勤情况Excel
     * @param reqDTO
     * @return
     */
    @PostMapping("/excel")
    public String excelSignInData(@RequestBody YearAndMonthReqDTO reqDTO){
        String url = signInService.excelSignInData(reqDTO.getYearAndMonth());
        return ZSYResult.success().data(url).build();
    }

    /**
     * 考勤人员列表
     * @return
     */
    @GetMapping("/users")
    public String getSignInUsers(){
        return ZSYResult.success().data(signInService.getSignInUsers()).build();
    }

    /**
     * 指定组考勤人员列表
     * @return
     */
    @GetMapping("/users-group")
    public String getSignInUsersGroup(){
        return ZSYResult.success().data(signInService.getSignInUsersGroup()).build();
    }


    /**
     * 个人查看剩余调休
     * @param userId
     * @return
     */
    @GetMapping("/rest-hours/{userId}")
    public String getPersonalRestHours(@PathVariable("userId")Long userId){
        return ZSYResult.success().data(signInService.getPersonalRestHours(userId)).build();
    }

    /**
     * 个人查看调休修改日志
     * @param reqDTO
     * @return
     */
    @PostMapping("/rest-hours-log/personal/page")
    public String getPersonalRestHoursLogPage(@RequestBody QueryRestHoursLogReqDTO reqDTO){
        return ZSYResult.success().data(signInService.getPersonalRestHoursLogPage(reqDTO)).build();
    }

    /**
     * 管理员查看用户调休修改日志
     * @param reqDTO
     * @return
     */
    @PostMapping("/rest-hours-log/page")
    public String getUserRestHoursLogPage(@RequestBody QueryRestHoursLogReqDTO reqDTO){
        return ZSYResult.success().data(signInService.getUserRestHoursLogPage(reqDTO)).build();
    }

    /**
     * 查看所有人调休时间
     * @param reqDTO
     * @return
     */
    @PostMapping("/rest-hours/list")
    public String getUserRestHoursList(@RequestBody QueryUserRestHoursReqDTO reqDTO){
        return ZSYResult.success().data(signInService.getUserRestHoursList(reqDTO)).build();
    }

    /**
     * 手动新增调休
     * @param reqDTO
     * @return
     */
    @PostMapping("/rest-hours-log/add")
    public String addUserRestHoursLog(@Valid @RequestBody AddUserRestHourLogReqDTO reqDTO){
        signInService.addUserRestHoursLog(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑调休日志
     * @param reqDTO
     * @return
     */
    @PutMapping("/rest-hours-log/update")
    public String editUserRestHoursLog(@Valid @RequestBody EditUserRestHourLogReqDTO reqDTO){
        signInService.editUserRestHoursLog(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除调休日志
     * @param logId
     * @return
     */
    @DeleteMapping("/rest-hours-log/delete/{logId}")
    public String deleteRestHoursLog(@PathVariable("logId")Long logId){
        signInService.deleteRestHoursLog(logId);
        return ZSYResult.success().build();
    }

    /**
     * 更新2019年10月的请假和加班产生的调休变化
     * @param reqDTO
     * @return
     */
    @PutMapping("/reset")
    public String updateLeaveAndEWork(@RequestBody LeaveAndEWorkReqDTO reqDTO){
        signInService.updateLeaveAndEWork(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 分页查看原始考勤记录
     * @param reqDTO
     * @return
     */
    @PostMapping("/origin/page")
    public String getSignInOriginPage(@RequestBody SignInReqDTO reqDTO){
        return ZSYResult.success().data(signInService.getSignInOriginPage(reqDTO)).build();
    }

    /**
     * 编辑原始考勤记录
     * @param reqDTO
     * @return
     */
    @PutMapping("/edit")
    public String editSignIn(@RequestBody EditSignInReqDTO reqDTO){
        signInService.editSignIn(reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 删除原始考勤
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public String deleteSignIn(@PathVariable("id")Long id){
        signInService.deleteSignIn(id);
        return ZSYResult.success().build();
    }

}
