package com.zhixinhuixue.armor.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.HmacSHA1Helper;
import com.zhixinhuixue.armor.helper.OKHttpHelper;
import com.zhixinhuixue.armor.service.impl.ZSYTaskService;
import com.zhixinhuixue.armor.source.FastdfsProperty;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.ZSYUFileProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;
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

    @Autowired
    private ZSYUFileProperties uFileProperties;

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
            return ZSYResult.success().data(result).build();

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

    /**
     * 格式化文件名称
     * @param originalFileName
     * @return
     */
    private String escapeFileName(String contentType, String originalFileName){
        String prefix = "zsy-armor-service/";
        if (fileIsImage(contentType)){
            return String.format("%s%s.%s",prefix, UUID.randomUUID().toString(),contentType.split("/")[1]);
        }
        return prefix+originalFileName.trim().replaceAll(" ","");
    }

    /**
     * 判断文件是否是图片
     * @return
     */
    private boolean fileIsImage(String contentType){
        if (Strings.isNullOrEmpty(contentType)){
            throw new ZSYServiceException("未知的文件类型");
        }
        String[] split = contentType.split("/");
        if (split.length!=2){
            throw new ZSYServiceException("未知的文件类型");
        }
        return contentType.split("/")[0].equalsIgnoreCase("image");
    }


    /**
     * 获取文件上传路径
     * @param fileName 文件名
     * @return
     */
    private String getUploadUrl(String fileName){
        return String.format("http://%s%s/%s",uFileProperties.getBucketName(),uFileProperties.getUploadProxySuffix(),fileName);
    }

    /**
     * ufile接口权限加密
     * @return
     */
    private String authorization(String contentType,String fileName){
        String httpMethod = "PUT";
        String contentMD5 = "";
        String date = "";
        String resource = "/" + uFileProperties.getBucketName() + "/" + fileName;
        String stringToSign = httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + resource;
        String signature = HmacSHA1Helper.sign(uFileProperties.getPrivateKey(), stringToSign);
        return String.format("UCloud %s:%s",uFileProperties.getPublicKey(),signature);
    }



    public String uploadToUcloud(MultipartFile uploadFile){
        try {
            if (uploadFile.getBytes().length / (1024 * 1024) > 1) {
                return ZSYResult.fail().msg("上传的文件大小超过最大限制(1MB)").build();
            }
            String contentType = uploadFile.getContentType();
            String fileName = escapeFileName(contentType,uploadFile.getOriginalFilename());
            String url = getUploadUrl(fileName);
            RequestBody requestBody = MultipartBody.create(MediaType.get(contentType), uploadFile.getBytes());
            Request request = new Request.Builder()
                    .header(OKHttpHelper.HTTP_REQUEST_HEAD, authorization(contentType,fileName))
                    .url(url).put(requestBody).build();
            OKHttpHelper.handleResponse(request, OKHttpHelper.getClient());
            return url;
        } catch (Exception e){
            throw new ZSYServiceException("文件上传失败");
        }
    }

    @RequestMapping(value = "ucloud/image", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String uploadImageToUcloud(@RequestParam(value = "uploadFile") MultipartFile uploadFile){
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!isImg(suffix)) {
            return ZSYResult.fail().msg("只能上传图片格式").build();
        }
        JSONObject result = new JSONObject();
        result.put("url", uploadToUcloud(uploadFile));
        return ZSYResult.success().data(result).build();
    }
}
