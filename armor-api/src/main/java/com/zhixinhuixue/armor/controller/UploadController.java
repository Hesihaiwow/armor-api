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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
     * @throws IOException
     */
    @RequestMapping(value = "image", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String images(@RequestParam(value = "uploadFile") MultipartFile uploadFile) {
        return null;
    }

    /**
     * 上传文件(image,Word,PDF,Excel)
     * @param uploadFile
     * @return
     */
    @RequestMapping(value = "file",produces = "application/json; charset=utf-8")
    @ResponseBody
    public String words(@RequestParam(value = "uploadFile") MultipartFile uploadFile){
        return null;
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

    @RequestMapping(value = "ucloud/file", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String uploadFileToUcloud(@RequestParam(value = "uploadFile") MultipartFile uploadFile){
        JSONObject result = new JSONObject();
        result.put("url", uploadToUcloud(uploadFile));
        return ZSYResult.success().data(result).build();
    }
}
