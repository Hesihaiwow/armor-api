package com.zhixinhuixue.armor.helper;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.source.ZSYQinuOssProperty;
import com.zhixinhuixue.armor.source.ZSYUFileProperties;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * Created by Akuma on 2018/6/1.
 */
public class ZSYQinuHelper {

    private ZSYQinuHelper(){}

    /**
     * 上传到七牛
     * @param uploadFile
     * @return
     * @throws IOException
     */
    public static String upload(byte[] uploadFile, String key, String contentType, ZSYQinuOssProperty ossProperty) {
        //构造一个带指定Zone对象的配置类
        try{
            Configuration cfg = new Configuration(Zone.zone2());
            Auth auth = Auth.create(ossProperty.getAccessKey(), ossProperty.getSecretKey());
            String token = auth.uploadToken(ossProperty.getBucketName(),null);
            UploadManager uploadManager = new UploadManager(cfg);
            Response o = uploadManager.put(uploadFile, key,token,null,contentType,false);
            if (o.isOK()) {
                return getFileUrl(key, ossProperty);
            }
            throw new ZSYServiceException(o.error);
        } catch (QiniuException e) {
            throw new ZSYServiceException(e.error());
        } catch (ZSYServiceException e){
            throw new ZSYServiceException(e.getMessage());
        }
    }

    /**
     * 上传到ufile
     * @param uploadFile
     * @param fileName
     * @param contentType
     * @return
     */
    public static String uploadToUfile(byte[] uploadFile, String fileName, String contentType,ZSYUFileProperties uFileProperties) {
        //构造一个带指定Zone对象的配置类
        try{
            String url = getUploadUrl(fileName,uFileProperties);
            RequestBody requestBody = MultipartBody.create(MediaType.get(contentType), uploadFile);
            Request request = new Request.Builder()
                    .header(OKHttpHelper.HTTP_REQUEST_HEAD, authorization(contentType,fileName,uFileProperties))
                    .url(url).put(requestBody).build();
            OKHttpHelper.handleResponse(request, OKHttpHelper.getClient());
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZSYServiceException(e.getMessage());
        }
    }

    /**
     * 获取文件上传路径
     * @param fileName 文件名
     * @return
     */
    public static String getUploadUrl(String fileName,ZSYUFileProperties uFileProperties){
        return String.format("http://%s%s/%s",uFileProperties.getBucketName(),uFileProperties.getUploadProxySuffix(),fileName);
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
     * 获取qiniu服务器文件地址
     *
     * @param key
     * @return
     */
    private static String getFileUrl(String key,ZSYQinuOssProperty ossProperty) {
        return "http://" + ossProperty.getDomain() + '/' + key;
    }


}
