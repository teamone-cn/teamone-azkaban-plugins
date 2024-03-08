package com.teamone.service.impl;

import azkaban.utils.JSONUtils;
import azkaban.utils.Props;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.builder.HCB;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.common.SSLs;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.teamone.constans.TeamoneCommonConstants;
import com.teamone.constans.TeamoneHttpJobPropsKey;
import com.teamone.constans.TeamoneJobPropsKey;
import com.teamone.exception.TeamoneHttpJobException;
import com.teamone.pojo.TeamoneHttpJobConfig;
import com.teamone.pojo.TeamoneHttpJobInfo;
import com.teamone.service.TeamoneExecuteJobService;
import com.teamone.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.teamone.constans.TeamoneCommonConstants.DEFAULT_RETURN_CODE_KEY;

/**
 * Teamone
 * 执行业务服务的实现类
 */
public class TeamoneExecuteJobServiceImpl implements TeamoneExecuteJobService {
    private Logger logger;

    @Override
    public void executeJob(Props jobProps, Logger logger) throws TeamoneHttpJobException {
        this.logger = logger;
        // Teamone 获取job基本信息
        String projectName = jobProps.get(TeamoneJobPropsKey.FLOW_PROJECTNAME.getKey());
        String flowId = jobProps.get(TeamoneJobPropsKey.FLOW_FLOWID.getKey());
        String execId = jobProps.get(TeamoneJobPropsKey.FLOW_EXECID.getKey());
        String jobId = jobProps.get(TeamoneJobPropsKey.JOB_ID.getKey());
        String jobWorkDir = jobProps.getString("working.dir");


        // Teamone 获取到请求的url
        String requestURL = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_URL.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        // Teamone 获取到请求的方法
        String requestMethod = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_METHOD.getKey(), "get").trim();

        // Teamone 获取到请求的参数格式
        String requestContentType = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_CONTENT_TYPE.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        // Teamone 获取到请求的参数
        String requestParam = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_PARAM.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        // Teamone 获取到请求返回内容中用于给回调的参数的json中的key
        String requestCallbackParamKey = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_CALLBACK_PARAM_KEY.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        // Teamone 获取到请求的超时时间，默认 3600秒
        String requestTimeout = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_TIMEOUT.getKey(), "3600").trim();

        // Teamone 获取到请求的响应码，默认 200
        String requestCode = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_CODE.getKey(), "200").trim();

        // Teamone 获取到回调的url
        String callbackURL = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_URL.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        // Teamone 获取到回调的方法
        String callbackMethod = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_METHOD.getKey(), "get").trim();

        // Teamone 获取到回调的参数格式
        String callbackContentType = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_CONTENT_TYPE.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        // Teamone 获取到回调的参数
        String callbackParam = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_PARAM.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        // Teamone 获取到回调的超时时间
        String callbackTimeout = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_TIMEOUT.getKey(), "3600").trim();

        // Teamone 获取到回调的响应码
        String callbackCode = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_CODE.getKey(), "200").trim();

        HashMap<String, Object> requestParamMap = new HashMap<>();
        HashMap<String, Object> callBackParamMap = new HashMap<>();

        // Teamone 如果请求参数类型是带有 form 的表单格式给出的话，解析参数并将其放到对应的Map中
        if (requestContentType.contains("form") && requestParam.contains("=")) {
            for (String param : requestParam.split("&")) {
                requestParamMap.put(param.split("=")[0], param.split("=")[1]);
            }
        }

        // Teamone 如果回调参数类型是带有 form 的表单格式给出的话，解析参数并将其放到对应的Map中
        if (callbackContentType.contains("form") && callbackParam.contains("=")) {
            for (String param : callbackParam.split("&")) {
                callBackParamMap.put(param.split("=")[0], param.split("=")[1]);
            }
        }

        // Teamone 构建配置对象
        TeamoneHttpJobConfig teamoneHttpJobConfig =
                new TeamoneHttpJobConfig(requestURL, requestMethod, requestContentType, requestParam,
                        requestCallbackParamKey, requestTimeout, requestCode, callbackURL,
                        callbackMethod, callbackContentType, callbackParam, callbackTimeout, callbackCode,
                        requestParamMap, callBackParamMap);

        // Teamone 构建判断job分类的对象
        TeamoneHttpJobInfo teamoneHttpJobInfo = build(jobId);

        try {
            // Teamone 获取Http的客户端
            HttpClient client = HCB.custom()
                    .sslpv(SSLs.SSLProtocolVersion.TLSv1_2)    //可设置ssl版本号，默认SSLv3，用于ssl，也可以调用sslpv("TLSv1.2")
                    .ssl()                    //https，支持自定义ssl证书路径和密码，ssl(String keyStorePath, String keyStorepass)
                    .retry(5)                    //重试5次
                    .build();

            if (teamoneHttpJobInfo.getGetToken()) {
                getToken(jobId, teamoneHttpJobConfig, client, jobWorkDir, flowId, teamoneHttpJobInfo);
            } else {
                requestAndCallBack(jobId, teamoneHttpJobConfig, client, jobWorkDir, flowId, teamoneHttpJobInfo);
            }
        } catch (Exception e) {
            throw new TeamoneHttpJobException(e.getMessage(), projectName, flowId, execId, e);
        }


    }

    private TeamoneHttpJobInfo build(String jobId) {
        String prefix = jobId.split("_", 2)[0];
        String suffix = jobId.split("_", 2)[1];

        boolean isGetToken = suffix.equals(TeamoneCommonConstants.DEFAULT_REQUEST_TOKEN_SUFFIX)
                || suffix.equals(TeamoneCommonConstants.DEFAULT_CALLBACK_TOKEN_SUFFIX);

        return new TeamoneHttpJobInfo(prefix, suffix, isGetToken);
    }

    private void requestAndCallBack(String jobId, TeamoneHttpJobConfig teamoneHttpJobConfig, HttpClient client,
                                    String jobWorkDir, String flowId, TeamoneHttpJobInfo teamoneHttpJobInfo)
            throws TeamoneHttpJobException, HttpProcessException {
        JSONObject dataJson = new JSONObject();
        String dataString = "";
        HashMap<String, String> tokenMap = new HashMap<>();

        // Teamone 先对请求的url 进行判空处理，如果为空，那么直接抛异常，不需要执行
        if (StringUtils.isBlank(teamoneHttpJobConfig.getRequestURL())) {
            throw new TeamoneHttpJobException("you must set this param [http_job.request.url] !");
        }

        // Teamone 先读取必然会出现在前面的 token 的数据分别读取和放入 HashMap 中
        readFilesInDirectory(TeamoneCommonConstants.PATH_SPLIT_SYMBOL + "tmp" +
                jobWorkDir + TeamoneCommonConstants.PATH_SPLIT_SYMBOL + flowId, tokenMap);

        // Teamone 流程到这里，必然会进行请求
        String response = request(tokenMap, teamoneHttpJobConfig, client, teamoneHttpJobInfo);

        // Teamone 如果根据param.key拿取到的结果是 json，那么转为json，如果不是，那么直接存储为String
        if (JsonUtil.isJSON(JsonUtil.findValueInJSON(JSON.parseObject(response), teamoneHttpJobConfig.getRequestCallbackParamKey()))) {
            dataJson = JSON.parseObject(JsonUtil.findValueInJSON(JSON.parseObject(response), teamoneHttpJobConfig.getRequestCallbackParamKey()));
        } else {
            dataString = JsonUtil.findValueInJSON(JSON.parseObject(response), teamoneHttpJobConfig.getRequestCallbackParamKey());
        }

        // Teamone 对回调的url 进行判空处理，如果不为空，才需要回调
        if (!StringUtils.isBlank(teamoneHttpJobConfig.getCallbackURL())) {
            callback(tokenMap, teamoneHttpJobConfig, client, dataJson, dataString, teamoneHttpJobInfo);
        }
    }

    public void callback(HashMap<String, String> tokenMap, TeamoneHttpJobConfig teamoneHttpJobConfig, HttpClient client,
                         JSONObject dataJson, String dataString, TeamoneHttpJobInfo teamoneHttpJobInfo)
            throws HttpProcessException, TeamoneHttpJobException {

        System.out.println("dataJson---" + dataJson);
        System.out.println("dataString---" + dataString);

        JSONObject jsonObject = new JSONObject();
        JSONObject paramJson = new JSONObject();
        HashMap<String, Object> requestCallBackParamMap = new HashMap<>();
        // Teamone 先获取到tokenMap里面的callbackToken
        String callbackToken = tokenMap.get(teamoneHttpJobInfo.getPrefix() + "_"
                + TeamoneCommonConstants.DEFAULT_CALLBACK_TOKEN_SUFFIX);

        // Teamone 如果获取不到相对应的token，那么报错
        if (StringUtils.isBlank(callbackToken)) {
            throw new TeamoneHttpJobException("you must get the callback_Token first!");
        }

        // Teamone 构建 headers
        Header[] headers = HttpHeader.custom()
                .userAgent("Apifox/1.0.0")
                .contentType(teamoneHttpJobConfig.getCallbackContentType())
                // 增加 token 信息
                .other("token", callbackToken)
                .build();
        HttpConfig httpConfig = HttpConfig.custom()
                .headers(headers)
                .timeout(Integer.parseInt(teamoneHttpJobConfig.getCallbackTimeout()) * 1000)
                .url(teamoneHttpJobConfig.getCallbackURL())
                .methodName(teamoneHttpJobConfig.getCallbackMethod())
                .encoding("utf-8")
                .inenc("utf-8")
                .client(client);

        // Teamone 对参数进行设置,如果map不为空，那么加上map类型的参数
        if (teamoneHttpJobConfig.getCallbackContentType().contains("form") &&
                !teamoneHttpJobConfig.getCallBackParamMap().isEmpty()) {
//            httpConfig.map(teamoneHttpJobConfig.getCallBackParamMap());
            requestCallBackParamMap.putAll(teamoneHttpJobConfig.getCallBackParamMap());
            System.out.println("这里执行判断000");
        }

        // Teamone 对参数进行设置,如果map为空 且 参数配置不为空 且 为json，那么加上json类型的参数
        if (teamoneHttpJobConfig.getCallbackContentType().contains("json") &&
                teamoneHttpJobConfig.getCallBackParamMap().isEmpty() &&
                !StringUtils.isBlank(teamoneHttpJobConfig.getCallbackParam()) &&
                JsonUtil.isJSON(teamoneHttpJobConfig.getCallbackParam())) {
//            httpConfig.json(teamoneHttpJobConfig.getCallbackParam());
            paramJson = JSONObject.parseObject(teamoneHttpJobConfig.getCallbackParam());
            System.out.println("这里执行判断111");
        }

        // Teamone 如果回调参数类型是带有 form 的表单格式给出的话，解析请求附带回来的string，并且将参数放到对应的Map中
        if (teamoneHttpJobConfig.getCallbackContentType().contains("form") && dataString.contains("=")) {
            for (String param : dataString.split("&")) {
                requestCallBackParamMap.put(param.split("=")[0], param.split("=")[1]);
            }
            System.out.println("requestCallBackParamMap---" + requestCallBackParamMap);
            System.out.println("这里执行判断222");
        }


        // Teamone 解析请求附带回来的json，并且将参数放到参数中
        if (teamoneHttpJobConfig.getCallbackContentType().contains("json")
                && dataJson != null
                && JsonUtil.isJSON(dataJson.toJSONString())) {
            paramJson.putAll(dataJson);
            System.out.println("这里执行判断333");
        }

        httpConfig.json(paramJson.toJSONString());

        if (!teamoneHttpJobConfig.getCallbackContentType().contains("json")) {
            httpConfig.map(requestCallBackParamMap);
            httpConfig.map().remove("$ENTITY_JSON$");
        } else {
            httpConfig.json(paramJson.toJSONString());
        }

        System.out.println("httpConfig.map()---" + httpConfig.map());
        System.out.println("httpConfig.json()---" + httpConfig.json());


        String result = teamoneHttpJobConfig.getCallbackMethod().equals("post") ?
                HttpClientUtil.post(httpConfig) : HttpClientUtil.get(httpConfig);
        this.logger.info("callback的result----" + result);

        // Teamone 获取返回码，并对其进行校验
        String code = JsonUtil.findValueInJSON(JSON.parseObject(result), TeamoneCommonConstants.DEFAULT_RETURN_CODE_KEY);

        if (!code.equals(teamoneHttpJobConfig.getCallbackCode())) {
            throw new TeamoneHttpJobException("The return code is not [" + teamoneHttpJobConfig.getCallbackCode() +
                    "], please check the corresponding interface");
        }

    }


    private String request(HashMap<String, String> tokenMap, TeamoneHttpJobConfig teamoneHttpJobConfig, HttpClient client,
                           TeamoneHttpJobInfo teamoneHttpJobInfo) throws HttpProcessException, TeamoneHttpJobException {

        // Teamone 先获取到tokenMap里面的requestToken
        String requestToken = tokenMap.get(teamoneHttpJobInfo.getPrefix() + "_"
                + TeamoneCommonConstants.DEFAULT_REQUEST_TOKEN_SUFFIX);

        // Teamone 如果获取不到相对应的token，那么报错
        if (StringUtils.isBlank(requestToken)) {
            throw new TeamoneHttpJobException("you must get the request_token first!");
        }

        // Teamone 构建 headers
        Header[] headers = HttpHeader.custom()
                .userAgent("Apifox/1.0.0")
                .contentType(teamoneHttpJobConfig.getRequestContentType())
                // 增加 token 信息
                .other("token", requestToken)
                .build();

        // Teamone 构建请求的客户端
        HttpConfig httpConfig = HttpConfig.custom()
                .headers(headers)
                .timeout(Integer.parseInt(teamoneHttpJobConfig.getRequestTimeout()) * 1000)
                .url(teamoneHttpJobConfig.getRequestURL())
                .methodName(teamoneHttpJobConfig.getRequestMethod())
                .encoding("utf-8")
                .inenc("utf-8")
                .client(client);

        // Teamone 对参数进行设置,如果map不为空，那么加上map类型的参数
        if (!teamoneHttpJobConfig.getRequestParamMap().isEmpty()) {
            httpConfig.map(teamoneHttpJobConfig.getRequestParamMap());
        }

        // Teamone 对参数进行设置,如果map为空 且 参数配置不为空 且 为json，那么加上json类型的参数
        if (teamoneHttpJobConfig.getRequestParamMap().isEmpty() &&
                !StringUtils.isBlank(teamoneHttpJobConfig.getRequestParam()) &&
                JsonUtil.isJSON(teamoneHttpJobConfig.getRequestParam())) {
            httpConfig.json(teamoneHttpJobConfig.getRequestParam());
        }

        String result = teamoneHttpJobConfig.getRequestMethod().equals("post") ?
                HttpClientUtil.post(httpConfig) : HttpClientUtil.get(httpConfig);
        this.logger.info("request的result----" + result);

        // Teamone 获取返回码，并对其进行校验
        String code = JsonUtil.findValueInJSON(JSON.parseObject(result), TeamoneCommonConstants.DEFAULT_RETURN_CODE_KEY);

        if (!code.equals(teamoneHttpJobConfig.getRequestCode())) {
            throw new TeamoneHttpJobException("The return code is not [" + teamoneHttpJobConfig.getRequestCode() +
                    "], please check the corresponding interface");
        }

        return result;

    }

    private void getToken(String jobId, TeamoneHttpJobConfig teamoneHttpJobConfig, HttpClient client, String jobWorkDir,
                          String flowId, TeamoneHttpJobInfo teamoneHttpJobInfo)
            throws TeamoneHttpJobException, HttpProcessException, IOException {
        String result;
        String token;
        Props props = new Props();
        Header[] headers;

        // Teamone 首先判断是不是拿token的job，如果是，那么就走的是 鉴权 并且 拿取对应的 token 并放在配置文件中的逻辑
        if (teamoneHttpJobInfo.getSuffix().contains("request")) {
            // Teamone 先对请求的url 进行判空处理，如果为空，那么直接抛异常，不需要执行
            if (StringUtils.isBlank(teamoneHttpJobConfig.getRequestURL())) {
                throw new TeamoneHttpJobException("you must set this param [http_job.request.url] !");
            }

            // Teamone 鉴权拿token逻辑
            headers = HttpHeader.custom().userAgent("Apifox/1.0.0").contentType(teamoneHttpJobConfig.getRequestContentType()).build();
            HttpConfig httpConfig = HttpConfig.custom()
                    .headers(headers)
                    .url(teamoneHttpJobConfig.getRequestURL())
                    .methodName(teamoneHttpJobConfig.getRequestMethod())
                    .encoding("utf-8")
                    .client(client);

            // Teamone 对参数进行设置,如果map不为空，那么加上map类型的参数
            if (!teamoneHttpJobConfig.getRequestParamMap().isEmpty()) {
                httpConfig.map(teamoneHttpJobConfig.getRequestParamMap());
            }

            // Teamone 对参数进行设置,如果map为空 且 参数配置不为空 且 为json，那么加上json类型的参数
            if (teamoneHttpJobConfig.getRequestParamMap().isEmpty() &&
                    !StringUtils.isBlank(teamoneHttpJobConfig.getRequestParam()) &&
                    JsonUtil.isJSON(teamoneHttpJobConfig.getRequestParam())) {
                httpConfig.json(teamoneHttpJobConfig.getRequestParam());
            }

            result = teamoneHttpJobConfig.getRequestMethod().equals("post") ?
                    HttpClientUtil.post(httpConfig) : HttpClientUtil.get(httpConfig);

            // Teamone 对 result 是否成功拿回进行校验，如果没有成功，那么该任务应该被置为失败
            if (StringUtils.isBlank(result) || !JsonUtil.isJSON(result)) {
                throw new TeamoneHttpJobException("Unable to obtain +" + jobId + "+ result, please check the interface!");
            }

            token = JsonUtil.findValueInJSON(JSON.parseObject(result), "token");

            // Teamone 对 token 是否成功拿回进行校验，如果没有成功，那么该任务应该被置为失败
            if (StringUtils.isBlank(token)) {
                throw new TeamoneHttpJobException("Unable to obtain  +" + jobId + "+  token, please check the interface!");
            }

            props.put(jobId, token);
        }

        if (teamoneHttpJobInfo.getSuffix().contains("callback")) {

            // Teamone 先对回调的url 进行判空处理，如果为空，那么直接抛异常，不需要执行
            if (StringUtils.isBlank(teamoneHttpJobConfig.getCallbackURL())) {
                throw new TeamoneHttpJobException("you must set this param [http_job.callback.url]");
            }

            // Teamone 鉴权拿token逻辑
            headers = HttpHeader.custom().contentType(teamoneHttpJobConfig.getCallbackContentType()).build();

            HttpConfig httpConfig = HttpConfig.custom()
                    .headers(headers)
                    .url(teamoneHttpJobConfig.getCallbackURL())
                    .methodName(teamoneHttpJobConfig.getCallbackMethod())
                    .encoding("utf-8")
                    .client(client);

            // Teamone 对参数进行设置,如果map不为空，那么加上map类型的参数
            if (!teamoneHttpJobConfig.getCallBackParamMap().isEmpty()) {
                httpConfig.map(teamoneHttpJobConfig.getCallBackParamMap());
            }

            // Teamone 对参数进行设置,如果map为空 且 参数配置不为空 且 为json，那么加上json类型的参数
            if (teamoneHttpJobConfig.getCallBackParamMap().isEmpty() &&
                    !StringUtils.isBlank(teamoneHttpJobConfig.getCallbackParam()) &&
                    JsonUtil.isJSON(teamoneHttpJobConfig.getCallbackParam())) {
                httpConfig.json(teamoneHttpJobConfig.getCallbackParam());
            }

            result = teamoneHttpJobConfig.getCallbackMethod().equals("post") ?
                    HttpClientUtil.post(httpConfig) : HttpClientUtil.get(httpConfig);

            // Teamone 对 result 是否成功拿回进行校验，如果没有成功，那么该任务应该被置为失败
            if (StringUtils.isBlank(result) || !JsonUtil.isJSON(result)) {
                throw new TeamoneHttpJobException("Unable to obtain +" + jobId + "+ result, please check the interface!");
            }

            token = JsonUtil.findValueInJSON(JSON.parseObject(result), "token");

            // Teamone 对 token 是否成功拿回进行校验，如果没有成功，那么该任务应该被置为失败
            if (StringUtils.isBlank(token)) {
                throw new TeamoneHttpJobException("Unable to obtain  +" + jobId + "+  token, please check the interface!");
            }

            props.put(jobId, token);
        }
        // Teamone 在当前执行任务id下执行需要用到的request的token信息和callback的token信息
        outputGeneratedProperties(props, jobWorkDir, flowId, teamoneHttpJobInfo);
    }


    private void outputGeneratedProperties(Props outputProperties, String jobWorkDir, String flowId,
                                           TeamoneHttpJobInfo teamoneHttpJobInfo) throws IOException {

        String outputDirPath = TeamoneCommonConstants.PATH_SPLIT_SYMBOL + "tmp" + jobWorkDir + TeamoneCommonConstants.PATH_SPLIT_SYMBOL + flowId;

        String outputFileName = flowId + "_" + teamoneHttpJobInfo.getPrefix() + "_" + teamoneHttpJobInfo.getSuffix() + ".tmp";

        this.logger.info("Outputting generated properties to " + outputDirPath);

        if (outputProperties == null) {
            this.logger.info("  no gend props");
            return;
        }
        for (String key : outputProperties.getKeySet()) {
            this.logger.info("  gend prop " + key + " value:" + outputProperties.get(key));
        }

        Map<String, String> properties = new LinkedHashMap<>();
        for (String key : outputProperties.getKeySet()) {
            properties.put(key, outputProperties.get(key));
        }

        File dir = new File(outputDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, outputFileName);

        BufferedWriter writer = null;
        try {
            this.logger.info("配置写入了文件---" + file.getPath());
            FileWriter fileWriter = new FileWriter(file);
            writer = new BufferedWriter(fileWriter);
            JSONUtils.writePropsNoJarDependency(properties, writer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            assert writer != null;
            writer.close();
        }
    }

    public void readFilesInDirectory(String directoryPath, HashMap<String, String> tokenMap) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) { // 检查是否为文件
                    System.out.println("Reading file: " + file.getAbsolutePath());
                    readContentFromFile(file, tokenMap);
                } else if (file.isDirectory()) { // 若是目录，则递归调用
                    readFilesInDirectory(file.getAbsolutePath(), tokenMap);
                }
            }
        }
    }

    public void readContentFromFile(File file, HashMap<String, String> tokenMap) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            StringBuilder line = new StringBuilder();
            String data = null;
            while ((data = reader.readLine()) != null) {
                line.append(data);
            }

            JSONObject jsonString = JSON.parseObject(line.toString());
            for (String key : jsonString.keySet()) {
                tokenMap.put(key, jsonString.get(key).toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
