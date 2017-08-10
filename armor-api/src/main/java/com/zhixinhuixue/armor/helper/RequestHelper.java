package com.zhixinhuixue.armor.helper;

import java.util.Map;

public class RequestHelper {

	
	
	//将getParameterMap转String
	public static String mapToUrl(Map<String,String[]> map){
		
		StringBuffer sb=new StringBuffer();
		
		for (String key : map.keySet()) {  
			if("pageIndex".equals(key)){
				continue;
			}
            String[] values = map.get(key);  
            for (int i = 0; i < values.length; i++) {  
                String value = values[i];  
                sb.append(key + "=" + value + "&");  
            }  
        }  
        // 去掉最后一个空格 
		if(sb.toString().length()==0){
			return "";
		}else{
			return "?"+sb.toString().substring(0, sb.toString().length() - 1);  
		}
		
	}
	
}
