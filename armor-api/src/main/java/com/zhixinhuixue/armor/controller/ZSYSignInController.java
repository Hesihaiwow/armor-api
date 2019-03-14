package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.model.dto.request.ResignInReqDTO;
import com.zhixinhuixue.armor.model.dto.request.SignInReqDTO;
import com.zhixinhuixue.armor.model.dto.request.YearAndMonthReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ResignInResDTO;
import com.zhixinhuixue.armor.model.dto.response.SignInLastRecordResDTO;
import com.zhixinhuixue.armor.model.dto.response.SignInResDTO;
import com.zhixinhuixue.armor.model.dto.response.TotalExtraHoursResDTO;
import com.zhixinhuixue.armor.service.IZSYSignInService;
import com.zhixinhuixue.armor.source.ArmorPageInfo;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author SCH
 * @date 2019/2/22 13:25
 */
@RestController
@RequestMapping("/api/sign-in")
@Api(value = "考勤统计相关接口", description = "任务管理系统考勤统计相关接口", tags = "/sign-in")
public class ZSYSignInController {
    @Autowired
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

    @ApiOperation("导入记录的最后一条记录打卡时间")
    @GetMapping("/last-record")
    public String getSignInLastRecord(){
        SignInLastRecordResDTO resDTO = signInService.getSignInLastRecord();
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("分页查询考勤统计(默认查当天)")
    @PostMapping("/page")
    public String getSignInPage(@RequestBody SignInReqDTO reqDTO){
        PageInfo<SignInResDTO> resDTO = signInService.getSignInPage(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("个人分页查询考勤统计")
    @PostMapping("/page/personal")
    public String getPersonalSignInPage(@RequestBody SignInReqDTO reqDTO){
        PageInfo<SignInResDTO> resDTO = signInService.getPersonalSignInPage(reqDTO);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("个人漏打卡考勤记录")
    @GetMapping("/page/personal/forget/{pageNum}")
    public String getForgetSignInPage(@PathVariable("pageNum")Integer pageNum){
        ArmorPageInfo<SignInResDTO> resDTO = signInService.getForgetSignInPage(pageNum);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("补打卡申请")
    @PostMapping("/resign-in/add")
    public String addResignIn(@RequestBody ResignInReqDTO reqDTO){
        signInService.addResignIn(reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("修改补打卡申请")
    @PostMapping("/resign-in/update/{id}")
    public String updateResignIn(@RequestBody ResignInReqDTO reqDTO,@PathVariable("id")Long id){
        signInService.updateResignIn(reqDTO,id);
        return ZSYResult.success().build();
    }

    @ApiOperation("删除补打卡申请")
    @DeleteMapping("/resign-in/delete/{id}")
    public String deleteResignIn(@PathVariable("id")Long id){
        signInService.deleteResignIn(id);
        return ZSYResult.success().build();
    }

    @ApiOperation("审核通过补打卡申请")
    @PutMapping("/resign-in/access/{id}")
    public String accessResignIn(@PathVariable("id")Long id){
        signInService.accessResignIn(id);
        return ZSYResult.success().build();
    }


    @ApiOperation("个人查询补打卡申请")
    @GetMapping("/resign-in/page/personal/{status}/{pageNum}")
    public String getPersonalByStatus(@PathVariable("status")Integer status,@PathVariable("pageNum")Integer pageNum){
        PageInfo<ResignInResDTO> resDTO = signInService.getPersonalByStatus(status,pageNum);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("管理员查询补打卡申请")
    @GetMapping("/resign-in/page/{status}/{pageNum}")
    public String getResignInByStatus(@PathVariable("status")Integer status,@PathVariable("pageNum")Integer pageNum){
        PageInfo<ResignInResDTO> resDTO = signInService.getResignInByStatus(status,pageNum);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("个人查看加班总时长")
    @GetMapping("/extra-hours/total/personal/{month}")
    public String getPersonalTotalExtraHours(@PathVariable Integer month){
        TotalExtraHoursResDTO resDTO = signInService.getPersonalTotalExtraHours(month);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("查看指定用户加班总时长")
    @GetMapping("/extra-hours/total/{userId}/{month}")
    public String getTotalExtraHoursByUserId(@PathVariable("userId")Long userId,@PathVariable("month")Integer month){
        TotalExtraHoursResDTO resDTO = signInService.getTotalExtraHoursByUserId(userId,month);
        return ZSYResult.success().data(resDTO).build();
    }

    @ApiOperation("按月导出考勤情况Excel")
    @PostMapping("/excel")
    public String excelSignInData(@RequestBody YearAndMonthReqDTO reqDTO){
        String url = signInService.excelSignInData(reqDTO.getYearAndMonth());
        return ZSYResult.success().data(url).build();
    }

    @ApiOperation("考勤人员列表")
    @GetMapping("/users")
    public String getSignInUsers(){
        return ZSYResult.success().data(signInService.getSignInUsers()).build();
    }
}
