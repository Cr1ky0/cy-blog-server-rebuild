package com.criiky0.utils;

import java.util.HashMap;
import java.util.Map;

public class QueryHelper {
    /**
     * 将指定options字符串解析为Map
     * @param options (collected:true,sort:likes)
     * @return
     */
    public static Map<String, String> filterOptions(String options) {
        String[] parts = options.split(",");
        HashMap<String, String> map = new HashMap<>();
        for(String single : parts){
            String[] keyValue = single.split(":");
            if(keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }
}
