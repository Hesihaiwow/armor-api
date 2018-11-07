package com.zhixinhuixue.armor.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Akuma on 16/1/21.
 */
public class ZSYMD5Helper {

    /**
     * md5加密16位
     * @param text
     * @return
     */
    public static String encode16(String text){
        return core(text).substring(8,24);
    }

    /**
     * md5加密32位
     * @param text
     * @return
     */
    public static String encode32(String text){
        return core(text);
    }

    /**
     * MD5加密算法
     * @param str
     * @return
     */
    private static String core(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("utf-8"));
            byte b[] = md.digest();
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0){
                    i += 256;
                }
                if (i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
