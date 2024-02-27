package com.teamone;

import azkaban.utils.Props;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.teamone.exception.TeamoneHttpJobException;
import com.teamone.pojo.TeamoneHttpJobConfig;
import com.teamone.service.TeamoneExecuteJobService;
import com.teamone.service.impl.TeamoneExecuteJobServiceImpl;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.HashMap;


public class Main {
    private static Logger logger;
    public static void main(String[] args) throws TeamoneHttpJobException {

        HashMap<String, String> tokenMap = new HashMap<>();

        String line = "{\"request_token\":\"1838d4c5-7eb7-435e-ae20-a6fa7711f722\"}";

        JSONObject jsonString = JSON.parseObject(line);

        for (String key : jsonString.keySet()) {
            System.out.println(key.toString());
            System.out.println(jsonString.get(key).toString());

            tokenMap.put(key.toString(), jsonString.get(key).toString());
        }

        System.out.println(tokenMap.toString());


    }
}