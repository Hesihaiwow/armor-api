package com.zhixinhuixue.armor.controller;

import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.MyQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.OnlineQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UpdateQuestionReqDTO;
import com.zhixinhuixue.armor.model.dto.response.OnlineQuestionResDTO;
import com.zhixinhuixue.armor.service.IZSYOnlineQuestionService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 线上问题相关接口
 * Created by SCH on 2018-12-27
 */
@RestController
@RequestMapping("/api/question")
public class ZSYOnlineQuestionController {
    @Resource
    private IZSYOnlineQuestionService onlineQuestionService;

    /**
     * 新建线上问题
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestBody OnlineQuestionReqDTO reqDTO) {
        return onlineQuestionService.add(reqDTO).build();
    }

    /**
     * 查看我的线上问题
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/mine")
    public String getMyQuestion(@RequestBody MyQuestionReqDTO reqDTO) {
        PageInfo<OnlineQuestionResDTO> pageInfo = onlineQuestionService.selectMyQuestionPage(reqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * 查看进行中线上问题
     *
     * @return
     */
    @GetMapping("/running")
    public String getRunningQuestion() {
        List<OnlineQuestionResDTO> list = onlineQuestionService.selectRunningQuestion();
        return ZSYResult.success().data(list).build();
    }

    /**
     * 查看已完成线上问题
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/completed/{pageNum}")
    public String getCompletedQuestion(@PathVariable("pageNum") Integer pageNum) {
        PageInfo<OnlineQuestionResDTO> list = onlineQuestionService.selectCompletedQuestion(pageNum);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 查看待审核线上问题
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/wait/{pageNum}")
    public String getWaitQuestion(@PathVariable("pageNum") Integer pageNum) {
        PageInfo<OnlineQuestionResDTO> list = onlineQuestionService.selectWaitQuestion(pageNum);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 查看审核通过线上问题
     *
     * @param pageNum
     * @return
     */
    @GetMapping("/accepted/{pageNum}")
    public String getAcceptedQuestion(@PathVariable("pageNum") Integer pageNum) {
        PageInfo<OnlineQuestionResDTO> list = onlineQuestionService.selectAcceptedQuestion(pageNum);
        return ZSYResult.success().data(list).build();
    }

    /**
     * 查看审核中(审核通过)线上问题
     *
     * @param reqDTO
     * @return
     */
    @PostMapping("/check")
    public String getCheckQuestion(@RequestBody MyQuestionReqDTO reqDTO) {
        PageInfo<OnlineQuestionResDTO> pageInfo = onlineQuestionService.selectCheckQuestionPage(reqDTO);
        return ZSYResult.success().data(pageInfo).build();
    }

    /**
     * 审核线上问题通过
     *
     * @param oqrId
     * @return
     */
    @PutMapping("/auditing/accept/{oqrId}")
    public String acceptQuestion(@PathVariable("oqrId") Long oqrId) {
        return onlineQuestionService.acceptQuestion(oqrId).build();
    }

    /**
     * 删除线上问题
     *
     * @param oqrId
     * @return
     */
    @PutMapping("/delete/{oqrId}")
    public String deleteQuestion(@PathVariable("oqrId") Long oqrId) {
        return onlineQuestionService.deleteQuestion(oqrId).build();
    }

    /**
     * 个人完成线上问题
     *
     * @param oqrId
     * @return
     */
    @PutMapping("/finish/{oqrId}")
    public String finishQuestion(@PathVariable("oqrId") Long oqrId) {
        return onlineQuestionService.finishQuestion(oqrId).build();
    }

    /**
     * 修改线上问题
     *
     * @param reqDTO
     * @return
     */
    @PutMapping("/update")
    public String updateQuestion(@RequestBody UpdateQuestionReqDTO reqDTO) {
        return onlineQuestionService.updateQuestion(reqDTO).build();
    }
}
