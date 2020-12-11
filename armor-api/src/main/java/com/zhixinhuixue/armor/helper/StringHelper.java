package com.zhixinhuixue.armor.helper;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * Created by Akuma on 15/9/16.
 */
public class StringHelper {


    /**
     * 将Object转字符串
     * @param o
     * @return
     */
    public static String toJSONString(Object o){
        return JSONObject.toJSONString(o, (ValueFilter) (o12, s, o1) -> {
            if (o1 instanceof Long){
                if ((Long) o1 >9007199254740992L){
                    return o1.toString();
                }
            }
            return o1;
        });
    }


    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if (null==str||"".equals(str.trim())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断是否是监控url
     * @param str
     * @return
     */
    public static boolean isMonitorUrl(String str){
        if (isEmpty(str)){
            return false;
        }
        if (str.equals("/health")||
                str.equals("/autoconfig")||
                str.contains("/metrics")||
                str.equals("/mappings")||
                str.equals("/trace")||
                str.equals("/info")||
                str.contains("/env")||
                str.contains("/logfile")||
                str.contains("/jolokia")||
                str.equals("/configprops")){
            return true;
        }else{
            return false;
        }
    }

}
