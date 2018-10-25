package com.zhixinhuixue.armor.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhixinhuixue.armor.service.impl.ZSYTaskService;
import com.zhixinhuixue.armor.source.FastdfsProperty;
import com.zhixinhuixue.armor.source.ZSYResult;
import io.swagger.annotations.Api;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tate on 2017/9/14.
 */
@Api(value = "上传文件", description = "上传文件")
@RequestMapping("/api/upload")
@RestController
public class UploadController {

    @Autowired
    private FastdfsProperty fastdfs;

    private static final Logger logger = LoggerFactory.getLogger(ZSYTaskService.class);

    /**
     * 上传图片
     *
     * @param uploadFile
     * @return
     * @throws MyException
     * @throws IOException
     */
    @RequestMapping(value = "image", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String images(@RequestParam(value = "uploadFile") MultipartFile uploadFile) {
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isImg(suffix)) {
            return ZSYResult.fail().msg("只能上传图片格式").build();
        }
        try {
            JSONObject result = new JSONObject();
            result.put("url", upload(uploadFile));
            return ZSYResult.success().data(result).build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return ZSYResult.fail().msg("上传失败").build();
    }

    /**
     * 上传文件(image,Word,PDF,Excel)
     * @param uploadFile
     * @return
     */
    @RequestMapping(value = "file",produces = "application/json; charset=utf-8")
    @ResponseBody
    public String words(@RequestParam(value = "uploadFile") MultipartFile uploadFile){
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isWord(suffix) && !isImg(suffix) && !isExcel(suffix)){
            return ZSYResult.fail().msg("只能上传图片,Word,PDF和Excel").build();
        }
        try {
            JSONObject result = new JSONObject();
            result.put("url",upload(uploadFile));
        }catch (IOException e){
            e.printStackTrace();
        }catch (MyException e){
            e.printStackTrace();
        }
        return ZSYResult.fail().msg("上传失败").build();
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
     * 获取上传文件路径
     *
     * @param path
     * @return
     */
    public String getUploadPath(String path) {
        return fastdfs.getDomain() + path;
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

}
