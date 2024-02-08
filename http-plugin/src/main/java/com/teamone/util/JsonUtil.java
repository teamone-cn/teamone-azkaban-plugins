package com.teamone.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * Teamone
 * Json工具类
 */
public class JsonUtil {

    public static void main(String[] args) {
        String json = "{\"timestamp\":1704162056355,\"code\":\"00000\",\"msg\":\"Success\",\"data\":{\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Imp1a2l0In0.zX4mgyNORKsDLfghhG-9WE1E8YEWw6LPFg_EpBJs3Gk\"}}";
        System.out.println(findValueInJSON(JSON.parseObject(json),"token"));

    }
    /**
     * Teamone
     * 从 JSONObject 里面获取到指定 key 的 value
     */
    public static String findValueInJSON(JSONObject oriJson, String key) {
        String result = "";
        for (String jsonKey : oriJson.keySet()) {
            if (jsonKey.equals(key)) {
                return oriJson.getString(jsonKey);
            }
            if (oriJson.get(jsonKey) instanceof JSONObject) {
                result = findValueInJSON((JSONObject) oriJson.get(jsonKey), key);
            } else if (oriJson.get(jsonKey) instanceof JSONArray) {
                //todo
                result = findValueInJsonArray((JSONArray) oriJson.get(jsonKey), key);
            }
        }
        return result;
    }

    /**
     * Teamone
     * 从 JSONArray 里面获取到指定 key 的 value
     */
    public static String findValueInJsonArray(JSONArray oriJson, String key) {
        String result = "";

        for (Object jsonObject : oriJson) {
            if (jsonObject instanceof JSONObject) {
                result = findValueInJSON((JSONObject) jsonObject, key);
            } else if (jsonObject instanceof JSONArray) {
                result = findValueInJsonArray((JSONArray) jsonObject, key);
            }
            if (StringUtils.isNotBlank(result)) {
                return result;
            }
        }
        return result;
    }

    /**
     * Teamone
     * 判断一个字符串是不是JSON
     */
    public static boolean isJSON(String str) {
        try {
            JSON.parse(str);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }


}
