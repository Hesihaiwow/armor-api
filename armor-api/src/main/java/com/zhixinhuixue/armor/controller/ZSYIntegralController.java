package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.IntegralResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralHistoryPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralPageResDTO;
import com.zhixinhuixue.armor.model.dto.response.IntegralReviewResDTO;
import com.zhixinhuixue.armor.model.dto.response.UserIntegralResDTO;
import com.zhixinhuixue.armor.service.IZSYIntegralService;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.enums.ZSYReviewStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * 积分信息
 * Created by Lang on 2017/8/7 0007.
 */
@RequestMapping("/api/integral")
@RestController
public class ZSYIntegralController extends ZSYController {

    @Resource
    private IZSYIntegralService integralService;


    /**
     * 获取积分列表,按时间区分
     *
     * @return
     */
    @GetMapping("")
    public String getIntegralPage(@PathParam("startTime") String startTime, @PathParam("endTime") String endTime) {
        List<IntegralPageResDTO> integralPageResDTOList = integralService.getIntegralPage(startTime, endTime);
        return ZSYResult.success().data(integralPageResDTOList).build();
    }

    /**
     * 根据时间查看积分列数量
     *
     * @return
     */
    @GetMapping("/count")
    public String getIntegralCount(@PathParam("startTime") String startTime, @PathParam("endTime") String endTime) {
        return ZSYResult.success().data(integralService.getIntegralCount(startTime, endTime)).build();
    }

    /**
     * 用户积分排名信息
     *
     * @return
     */
    @GetMapping("/user")
    public String getUserIntegral() {
        UserIntegralResDTO userIntegralResDTO = integralService.getUserIntegral();
        return ZSYResult.success().data(userIntegralResDTO).build();
    }

    /**
     * 用户积分历史
     *
     * @return
     */
    @GetMapping("/history/{userId}/{pageIndex}")
    public String getIntegralHistory(@PathVariable Long userId, @PathVariable Integer pageIndex, @PathParam("startTime") String startTime, @PathParam("endTime") String endTime) {
        PageInfo<IntegralHistoryPageResDTO> integralHistoryPageResDTOS = integralService.getIntegralHistory(userId, pageIndex, startTime, endTime);
        return ZSYResult.success().data(integralHistoryPageResDTOS).build();
    }

    /**
     * 添加积分记录
     *
     * @param integralResDTO
     * @return
     */
    @PostMapping("/add")
    public String addIntegral(@RequestBody IntegralResDTO integralResDTO) {
        integralService.addIntegral(integralResDTO);
        return ZSYResult.success().build();
    }

    /**
     * 添加积分记录
     *
     * @param integralResDTO
     * @return
     */
    @PostMapping("/addHelp")
    public String addHelpIntegral(@RequestBody IntegralResDTO integralResDTO) {
        integralService.addIntegral(integralResDTO);
        return ZSYResult.success().build();
    }

    /**
     * 编辑积分转移
     *
     * @return
     */
    @PostMapping("/editHelpDetail/{id}")
    public String editHelpDetail(@RequestBody IntegralResDTO integralResDTO, @PathVariable Long id) {
        integralService.updateHelpDetail(integralResDTO, id);
        return ZSYResult.success().build();
    }

    /**
     * 删除积分转移
     *
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public String deleteHelpDetail(@PathVariable Long id) {
        integralService.deleteHelpDetail(id);
        return ZSYResult.success().build();
    }

    /**
     * 获取我的待审核积分转移
     *
     * @return
     */
    @GetMapping("/getMyWaitList/{pageIndex}")
    public String getMyWaitList(@PathVariable Integer pageIndex) {
        PageInfo<IntegralReviewResDTO> integralReviewResDTOPageInfo = integralService.getIntegralByReviewStatus(ZSYReviewStatus.PENDING.getValue(), pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 获取所有待审核积分转移
     *
     * @return
     */
    @GetMapping("/getHelpWaitList/{pageIndex}")
    public String getHelpWaitList(@PathVariable Integer pageIndex) {
        PageInfo<IntegralReviewResDTO> integralReviewResDTOPageInfo = integralService.getAllIntegralByReviewStatus(ZSYReviewStatus.PENDING.getValue(), pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 获取审核打回积分转移
     *
     * @return
     */
    @GetMapping("/getRejectList/{pageIndex}")
    public String getRejectList(@PathVariable Integer pageIndex) {
        PageInfo<IntegralReviewResDTO> integralReviewResDTOPageInfo = integralService.getIntegralByReviewStatus(ZSYReviewStatus.REJECT.getValue(), pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 获取我的审核完成积分转移
     *
     * @return
     */
    @GetMapping("/getMyReviewList/{pageIndex}")
    public String getMyReviewList(@PathVariable Integer pageIndex) {
        PageInfo<IntegralReviewResDTO> integralReviewResDTOPageInfo = integralService.getIntegralByReviewStatus(ZSYReviewStatus.ACCEPT.getValue(), pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 获取所有审核完成积分转移
     *
     * @return
     */
    @GetMapping("/getReviewList/{pageIndex}")
    public String getReviewList(@PathVariable Integer pageIndex) {
        PageInfo<IntegralReviewResDTO> integralReviewResDTOPageInfo = integralService.getAllIntegralByReviewStatus(ZSYReviewStatus.ACCEPT.getValue(), pageIndex);
        return ZSYResult.success().data(integralReviewResDTOPageInfo).build();
    }

    /**
     * 审核通过积分转移
     *
     * @return
     */
    @GetMapping("/passReview/{id}")
    public String passReview(@PathVariable Long id) {
        integralService.passReview(id);
        return ZSYResult.success().build();
    }

    // sch --

    /**
     * 个人积分信息
     *
     * @return
     */
    @GetMapping("/personal")
    public String getPersonalIntegral() {
        return ZSYResult.success().data(integralService.getPersonalIntegral()).build();
    }
    // -- sch

}
