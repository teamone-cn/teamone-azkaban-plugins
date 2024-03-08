package com.teamone;

import com.alibaba.fastjson2.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.common.SSLs;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.teamone.exception.TeamoneHttpJobException;
import com.teamone.pojo.TeamoneHttpJobConfig;
import com.teamone.pojo.TeamoneHttpJobInfo;
import com.teamone.service.impl.TeamoneExecuteJobServiceImpl;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import java.util.HashMap;

import static com.arronlong.httpclientutil.common.HttpHeader.*;


public class Main {
    private static Logger logger;
    public static void main(String[] args) throws TeamoneHttpJobException, HttpProcessException {


        HttpClient client = HCB.custom()
                .sslpv(SSLs.SSLProtocolVersion.TLSv1_2)    //可设置ssl版本号，默认SSLv3，用于ssl，也可以调用sslpv("TLSv1.2")
                .ssl()                    //https，支持自定义ssl证书路径和密码，ssl(String keyStorePath, String keyStorepass)
                .retry(5)                    //重试5次
                .build();

        Header[] headers = custom()
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:65.0) Gecko/20100101 Firefox/65.0")
                .contentType("application/json")
                // 增加 token 信息
                .other("token", "bc485a18-5952-49e8-b994-ba2cab8ba331")
                .build();
        HttpConfig httpConfig = HttpConfig.custom()
                .headers(headers)
//                .timeout(Integer.parseInt(teamoneHttpJobConfig.getCallbackTimeout()) * 1000)
                .url("https://dev-svip-api.thwpmanage.com/api/vip/test/verifySign")
                .methodName("post")
                .encoding("utf-8").client(client);


        HashMap<String,Object> requestCallBackParamMap = new HashMap<>();
//        httpConfig.json();
//        requestCallBackParamMap.remove("$ENTITY_JSON$");
        httpConfig.map(requestCallBackParamMap).json(JSONObject.of("sign","123456").toJSONString());

        httpConfig.map().remove("$ENTITY_JSON$");


        System.out.println("json"+httpConfig.json());


        String post = HttpClientUtil.post(httpConfig);
        System.out.println(post);

//
//        HashMap<String, Object> requestParamMap = new HashMap<>();
//        HashMap<String, Object> callBackParamMap = new HashMap<>();
//
//        // Teamone 如果请求参数类型是带有 form 的表单格式给出的话，解析参数并将其放到对应的Map中
//        if ("application/json".contains("form") && "{}".contains("=")) {
//            for (String param : "{}".split("&")) {
//                requestParamMap.put(param.split("=")[0], param.split("=")[1]);
//            }
//        }
//
//        // Teamone 如果回调参数类型是带有 form 的表单格式给出的话，解析参数并将其放到对应的Map中
//        if ("application/x-www-form-urlencoded".contains("form") && "".contains("=")) {
//            for (String param : "".split("&")) {
//                callBackParamMap.put(param.split("=")[0], param.split("=")[1]);
//            }
//        }
//        HttpClient client = HCB.custom()
//                .sslpv(SSLs.SSLProtocolVersion.TLSv1_2)    //可设置ssl版本号，默认SSLv3，用于ssl，也可以调用sslpv("TLSv1.2")
//                .ssl()                    //https，支持自定义ssl证书路径和密码，ssl(String keyStorePath, String keyStorepass)
//                .retry(5)                    //重试5次
//                .build();
//
//        TeamoneHttpJobConfig teamoneHttpJobConfig = new TeamoneHttpJobConfig("https://dev-svip-api.thwpmanage.com/api/vip/test/getSign","post",
//                "application/json","{}","callbackForm","100","301",
//                "https://dev-svip-api.thwpmanage.com/api/vip/test/verifySign","post","application/x-www-form-urlencoded",
//                "{}","100","302",requestParamMap,callBackParamMap);
//        TeamoneHttpJobInfo teamoneHttpJobInfo = new TeamoneHttpJobInfo("svip","request_callback_test",false);
//
//        HashMap<String,String> tokenMap = new HashMap<>();
//        TeamoneExecuteJobServiceImpl teamoneExecuteJobService = new TeamoneExecuteJobServiceImpl();
//        tokenMap.put("svip_callback_token","95c9feb2-e28d-42ec-a981-377aecffa107");
//
//
//        teamoneExecuteJobService.callback(tokenMap,teamoneHttpJobConfig,client,null,"sign=123456",teamoneHttpJobInfo);

    }
}