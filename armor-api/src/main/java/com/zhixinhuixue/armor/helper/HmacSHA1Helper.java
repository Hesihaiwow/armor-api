package com.zhixinhuixue.armor.helper;

import com.google.common.base.Charsets;
import com.zhixinhuixue.armor.exception.ZSYServiceException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by Live.InPast on 2018/11/28.
 */
public class HmacSHA1Helper {

    /**
     * 加密算法
     */
    private static final String ALGORITHM = "HmacSHA1";

    /**
     * 加密
     * @return
     */
    public static String sign(String encryptKey, String encryptText){
        try{
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(encryptKey.getBytes(Charsets.UTF_8), ALGORITHM));
            return toBase64String(mac.doFinal(encryptText.getBytes(Charsets.UTF_8)));
        } catch (Exception e){
          e.printStackTrace();
          throw new ZSYServiceException("加密失败");
        }
    }

    /**
     * 转Base64字符串
     * @return
     */
    private static String toBase64String(byte[] data){
        return new String(Base64.getEncoder().encode(data));
    }
}
