package com.berry.util;


import com.berry.common.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xueancao
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）&拼接成字符
     *
     * @param paramsMap map
     * @return urlStr
     */
    public static String parseUrlParams(StringMap paramsMap) {
        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(paramsMap.entrySet());
        infoIds.sort(Map.Entry.comparingByKey());
        // 构造URL 键值对的格式
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, Object> item : infoIds) {
            if (isNotBlank(item.getKey()) && item.getValue() != null) {
                String key = item.getKey();
                String val = item.getValue().toString();
                buf.append(key).append("=").append(val);
                buf.append("&");
            }
        }
        String buff = buf.toString();
        if (!buff.isEmpty()) {
            buff = buff.substring(0, buff.length() - 1);
        }
        return buff;
    }

    public static byte[] utf8Bytes(String data) {
        return data.getBytes(Constants.UTF_8);
    }

    public static String utf8String(byte[] data) {
        return new String(data, Constants.UTF_8);
    }

}
