package com.zhixinhuixue.armor.helper;

import com.google.common.base.Strings;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.service.impl.ZSYTaskService;
import com.zhixinhuixue.armor.source.ZSYQinuOssProperty;
import com.zhixinhuixue.armor.source.ZSYResult;
import com.zhixinhuixue.armor.source.ZSYUFileProperties;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by sch on 2019/5/14.
 */
public class UFileHelper {


    private static final Logger logger = LoggerFactory.getLogger(UFileHelper.class);

    private UFileHelper(){}

    private ZSYUFileProperties uFileProperties;

    /**
     * 上传到ufile
     * @param uploadFile
     * @return
     * @throws IOException
     */
    public static String upload(byte[] uploadFile, String fileName, String contentType,ZSYUFileProperties uFileProperties) {
        try {
            fileName = escapeFileName(contentType,fileName);
            String url = getUploadUrl(fileName,uFileProperties);
            RequestBody requestBody = MultipartBody.create(MediaType.get(contentType), uploadFile);
            Request request = new Request.Builder()
                    .header(OKHttpHelper.HTTP_REQUEST_HEAD, authorization(contentType,fileName,uFileProperties))
                    .url(url).put(requestBody).build();
            OKHttpHelper.handleResponse(request, OKHttpHelper.getClient());
            return url;
        } catch (Exception e){
            throw new ZSYServiceException("文件上传失败");
        }
    }

    /**
     * ufile接口权限加密
     * @return
     */
    private static String authorization(String contentType,String fileName,ZSYUFileProperties uFileProperties){
        String httpMethod = "PUT";
        String contentMD5 = "";
        String date = "";
        String resource = "/" + uFileProperties.getBucketName() + "/" + fileName;
        String stringToSign = httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + resource;
        String signature = HmacSHA1Helper.sign(uFileProperties.getPrivateKey(), stringToSign);
        return String.format("UCloud %s:%s",uFileProperties.getPublicKey(),signature);
    }

    /**
     * 获取文件上传路径
     * @param fileName 文件名
     * @return
     */
    private static String getUploadUrl(String fileName,ZSYUFileProperties uFileProperties){
        return String.format("http://%s%s/%s",uFileProperties.getBucketName(),uFileProperties.getUploadProxySuffix(),fileName);
    }

    /**
     * 格式化文件名称
     * @param originalFileName
     * @return
     */
    private static String escapeFileName(String contentType, String originalFileName){
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
    private static boolean fileIsImage(String contentType){
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
     * 获取qiniu服务器文件地址
     *
     * @param key
     * @return
     */
    private static String getFileUrl(String key,ZSYQinuOssProperty ossProperty) {
        return "http://" + ossProperty.getDomain() + '/' + key;
    }


}
