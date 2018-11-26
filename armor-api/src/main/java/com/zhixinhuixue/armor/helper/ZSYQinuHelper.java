package com.zhixinhuixue.armor.helper;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.source.ZSYQinuOssProperty;

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
     * 获取qiniu服务器文件地址
     *
     * @param key
     * @return
     */
    private static String getFileUrl(String key,ZSYQinuOssProperty ossProperty) {
        return "http://" + ossProperty.getDomain() + '/' + key;
    }


}
