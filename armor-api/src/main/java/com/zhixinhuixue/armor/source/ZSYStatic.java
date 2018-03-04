package com.zhixinhuixue.armor.source;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Akuma on 16/4/18.
 */
public class ZSYStatic {

    /**
     * 判断是否是网页静态文件
     * @return
     */
    public static boolean filterHtml(String url){
        Pattern p=Pattern.compile("\\.(html|htm|jsp|png|jpg|jpeg|bmp|gif|js|css|ico|woff)");
        Matcher m=p.matcher(url);
        if(m.find()){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是图片
     * @return
     */
    public static boolean filterImg(String url){

        Pattern p=Pattern.compile("\\.(png|PNG|jpg|JPG|jpeg|JPEG|bmp|BMP|gif|GIF|ico|ICO)");
        Matcher m=p.matcher(url);
        if(m.find()){
            return true;
        }
        return false;
    }


}
