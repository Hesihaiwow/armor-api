package com.zhixinhuixue.armor.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.zhixinhuixue.armor.model.dto.request.*;
import com.zhixinhuixue.armor.model.dto.response.*;
import com.zhixinhuixue.armor.service.IZSYFeedbackPlanService;
import com.zhixinhuixue.armor.service.IZSYFeedbackService;
import com.zhixinhuixue.armor.service.IZSYUserService;
import com.zhixinhuixue.armor.service.impl.ZSYTaskService;
import com.zhixinhuixue.armor.source.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SCH on 2018-11-06
 */
@Api(value = "需求接口",description = "学管端需求相关操作接口",tags = "/feedback")
@RequestMapping("/api/feedback/coach")
@RestController
public class ZSYFeedbackCoachController extends ZSYController {
    @Autowired
    private IZSYFeedbackService feedbackService;

    @Autowired
    private IZSYUserService userService;

    @Autowired
    private IZSYFeedbackPlanService feedbackPlanService;

    @Autowired
    private FastdfsProperty fastdfs;

    private static final Logger logger = LoggerFactory.getLogger(ZSYTaskService.class);

    /**
     * 获取新需求列表
     * @param reqDTO
     * @return
     */
    @ApiOperation("新需求列表")
    @PostMapping(value = "/demand-new/list/{coachId}")
    public ZSYSwaggerResult<ArmorPageInfo<DemandResDTO>> getDemandList(@RequestBody DemandQueryReqDTO reqDTO, @PathVariable("coachId")Integer coachId ){
        return new ZSYSwaggerResult<>(feedbackService.getDemandListByCoach(reqDTO,coachId));
    }

    /**
     * 获取驳回需求列表
     * @param reqDTO
     * @return
     */
    @ApiOperation("驳回需求列表")
    @PostMapping(value = "/demand-rejected/list/{coachId}")
    public ZSYSwaggerResult<ArmorPageInfo<DemandRejectedResDTO>> getDemandRejectedList(@RequestBody DemandQueryReqDTO reqDTO,@PathVariable("coachId")Integer coachId){
        ArmorPageInfo<DemandRejectedResDTO> list = feedbackService.getDemandRejectedListByCoach(reqDTO,coachId);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取排队中需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("排队需求列表")
    @PostMapping(value = "/demand-queued/list/{coachId}")
    public ZSYSwaggerResult<ArmorPageInfo<DemandQueuedResDTO>> getDemandQueuedList(@RequestBody DemandQueryReqDTO reqDTO,@PathVariable("coachId")Integer coachId){
        ArmorPageInfo<DemandQueuedResDTO> list = feedbackService.getDemandQueuedListByCoach(reqDTO,coachId);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取已完成需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("完成需求列表")
    @PostMapping(value = "/demand-completed/list")
    public ZSYSwaggerResult<PageInfo<DemandCompletedResDTO>> getDemandCompletedList(@RequestBody DemandQueryReqDTO reqDTO){
        PageInfo<DemandCompletedResDTO> list = feedbackService.getDemandCompletedListByCoach(reqDTO);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取进行中需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("进行中需求列表")
    @PostMapping(value = "/demand-running/list/{coachId}")
    public ZSYSwaggerResult<ArmorPageInfo<DemandRunningResDTO>> getDemandRunningList(@RequestBody DemandQueryReqDTO reqDTO,@PathVariable("coachId")Integer coachId){
        ArmorPageInfo<DemandRunningResDTO> list = feedbackService.getDemandRunningListByCoach(reqDTO,coachId);
        return new ZSYSwaggerResult<>(list);
    }

    /**
     * 获取需求详情
     * @return
     */
    @ApiOperation("获取需求详情")
    @GetMapping(value = "/demand/detail/{demandId}/{status}")
    public ZSYSwaggerResult<DemandDetailResDTO> getDemandDetail(@PathVariable("demandId")Long id,@PathVariable("status")Integer status){
        return new ZSYSwaggerResult<>(feedbackService.getDemandDetail(id,status));
    }

    /**
     * 获取需求回复详情
     * @param id
     * @return
     */
    @ApiOperation("获取需求回复详情")
    @GetMapping(value = "/demand/reply/{demandId}")
    public ZSYSwaggerResult<List<DemandReplyResDTO>> getDemandReply(@PathVariable("demandId")Long id){
        return new ZSYSwaggerResult<>(feedbackService.getDemandReply(id));
    }

    /**
     * 查看是否已读
     * @param id
     * @return
     */
    @ApiOperation("查看是否已读")
    @GetMapping(value = "/demand/is-read/{demandId}/{coachId}")
    public String isRead(@PathVariable("demandId")Long id,@PathVariable("coachId")Integer coachId){
        return ZSYResult.success().data(feedbackService.isReadByCoach(id,coachId)).build();
    }

    /**
     * 读取需求
     * @param id
     * @return
     */
    @ApiOperation("需求读取")
    @PostMapping(value = "/demand/read/{demandId}")
    public String readDemand(@PathVariable("demandId")Long id, @RequestBody DemandReadReqDTO reqDTO){
        feedbackService.readDemandByCoach(id,reqDTO);
        return ZSYResult.success().build();
    }

    @ApiOperation("需求是否点赞")
    @GetMapping(value = "/demand/is-like/{demandId}/{coachId}")
    public String isLike(@PathVariable("demandId")Long id,@PathVariable("coachId")Integer coachId){
        return ZSYResult.success().data(feedbackService.isLikeByCoach(id,coachId)).build();
    }

    /**
     * 需求点赞
     * @param id
     * @return
     */
    @ApiOperation("需求点赞")
    @PostMapping(value = "/demand/like/{demandId}")
    public String like(@PathVariable("demandId")Long id,@RequestBody DemandLikeReqDTO reqDTO){
        feedbackService.likeDemandByCoach(id,reqDTO);
        return ZSYResult.success().build();
    }

    /**
     * 需求取消点赞
     * @param id
     * @param coachId
     * @return
     */
    @ApiOperation("需求取消点赞")
    @PostMapping(value = "/demand/dislike/{demandId}/{coachId}")
    public String dislike(@PathVariable("demandId")Long id,@PathVariable("coachId")Integer coachId){
        feedbackService.dislikeDemandByCoach(id,coachId);
        return ZSYResult.success().build();
    }

    /**
     * 回复需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("回复需求")
    @PostMapping(value = "/demand/reply/{coachId}")
    public String reply(@RequestBody DemandReplyReqDTO reqDTO,@PathVariable("coachId")Integer coachId){
        feedbackService.replyDemandByCoach(reqDTO,coachId);
        return ZSYResult.success().build();
    }

    /**
     * 新增需求
     * @param reqDTO
     * @return
     */
    @ApiOperation("新增需求")
    @PostMapping(value = "/demand/add/{coachId}")
    public String addDemand(@Valid @RequestBody DemandReqDTO reqDTO,@PathVariable("coachId")Integer coachId){
        feedbackService.addDemandByCoach(reqDTO,coachId);
        return ZSYResult.success().build();
    }

    /**
     * 获取需求附件
     * @param id
     * @return
     */
    @ApiOperation("获取需求附件url")
    @GetMapping(value = "/demand/accessory/{demandId}")
    public String getUrl(@PathVariable("demandId")Long id){
        return ZSYResult.success().data(feedbackService.getUrl(id)).build();
    }

    /**
     * 来源人员列表
     * @return
     */
    @ApiOperation("来源人员列表")
    @GetMapping(value = "/originList")
    public String getOrigin(){
        return ZSYResult.success().data(feedbackService.getOrigin()).build();
    }

    /**
     * 查询提出人员列表
     * @return
     */
    @ApiOperation("提出人员列表")
    @GetMapping(value = "/demand/introducers")
    public String getIntroducerList(){
        return ZSYResult.success().data(feedbackService.getIntroducerList()).build();
    }

    @ApiOperation("负责人列表")
    @GetMapping("/effective")
    public String effectiveUsers() {
        List<EffectUserResDTO> users = userService.getEffectiveUsers();
        return ZSYResult.success().data(users).build();
    }

    /**
     * 获取计划列表
     * @param reqDTO
     * @return
     */
    @ApiOperation("计划列表")
    @PostMapping(value = "/demand-plan/list")
    public String getDemandPlanList(@RequestBody DemandPlanQueryReqDTO reqDTO){
        return ZSYResult.success().data(feedbackPlanService.getDemandPlanListByCoach(reqDTO)).build();
    }

    /**
     * 上传文件(image,Word,PDF,Excel)
     * @param uploadFile
     * @return
     */
    @RequestMapping(value = "file",produces = "application/json; charset=utf-8")
    @ResponseBody
    public String words(@RequestParam(value = "/uploadFile") MultipartFile uploadFile){
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isWord(suffix) && !isImg(suffix) && !isExcel(suffix)){
            return ZSYResult.fail().msg("只能上传图片,Word,PDF和Excel").build();
        }
        try {
            JSONObject result = new JSONObject();
            result.put("url",upload(uploadFile));
            return ZSYResult.success().data(result).build();

        }catch (IOException e){
            e.printStackTrace();
        }catch (MyException e){
            e.printStackTrace();
        }
        return ZSYResult.fail().msg("上传失败").build();
    }

    public static boolean isImg(String url){

        Pattern p=Pattern.compile("\\.(png|PNG|jpg|JPG|jpeg|JPEG|bmp|BMP|gif|GIF|ico|ICO)");
        Matcher m=p.matcher(url);
        if(m.find()){
            return true;
        }
        return false;
    }

    //判断是否是word文档和pdf
    public static boolean isWord(String url){

        Pattern p=Pattern.compile("\\.(doc|docx|PDF|pdf)");
        Matcher m=p.matcher(url);
        if(m.find()){
            return true;
        }
        return false;
    }

    //判断是否是excel
    public static boolean isExcel(String url){

        Pattern p=Pattern.compile("\\.(xls|XLS)");
        Matcher m=p.matcher(url);
        if(m.find()){
            return true;
        }
        return false;
    }

    /**
     * 获取上传文件后缀名
     *
     * @param uploadName
     * @return
     */
    public String getUploadSuffix(String uploadName) {
        return uploadName.substring(uploadName.lastIndexOf(".") + 1);
    }

    /**
     * 上传到 DFS
     *
     * @param uploadFile
     * @return
     * @throws IOException
     * @throws MyException
     */
    private String upload(MultipartFile uploadFile) throws IOException, MyException {
//            ClientGlobal.setG_connect_timeout(fastdfs.getConnectTimeout());
//            ClientGlobal.setG_network_timeout(fastdfs.getNetworkTimeout());
        ClientGlobal.setG_charset(fastdfs.getCharset());
        ClientGlobal.setG_tracker_http_port(fastdfs.getTrackerHttpPort());
        InetSocketAddress[] tracker_servers = new InetSocketAddress[fastdfs.getTrackerServer().length];

        for (int i = 0; i < fastdfs.getTrackerServer().length; ++i) {
            String[] parts = fastdfs.getTrackerServer()[i].split("\\:", 2);
            if (parts.length != 2) {
                throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
            }

            tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        ClientGlobal.setG_tracker_group(new TrackerGroup(tracker_servers));
        ClientGlobal.setG_anti_steal_token(fastdfs.isAntiStealToken());

        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;

        StorageClient storageClient = new StorageClient(trackerServer, storageServer);


        String fileIds[] = storageClient.upload_file(uploadFile.getBytes(), getUploadSuffix(uploadFile.getOriginalFilename()), null);
        logger.info("组名：" + fileIds[0]);
        logger.info("路径: " + fileIds[1]);

        return getUploadPath("/" + fileIds[0] + "/" + fileIds[1]);
    }


    /**
     * 获取上传文件路径
     *
     * @param path
     * @return
     */
    public String getUploadPath(String path) {
        return fastdfs.getDomain() + path;
    }
}
