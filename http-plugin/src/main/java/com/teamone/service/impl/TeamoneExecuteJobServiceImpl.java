package com.teamone.service.impl;

import azkaban.utils.JSONUtils;
import azkaban.utils.Props;
import com.alibaba.fastjson2.JSON;
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
import com.teamone.service.TeamoneExecuteJobService;
import com.teamone.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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


        // Teamone 获取到需要请求的 url，参数，和需要回调的 url，参数
        String requestURL = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_URL.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        String requestMethod = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_METHOD.getKey(), "post").trim();

        String requestContentType = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_CONTENT_TYPE.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        String requestParam = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_REQUEST_PARAM.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        String callbackURL = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_URL.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        String callbackMethod = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_METHOD.getKey(), "post").trim();

        String callbackContentType = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_CONTENT_TYPE.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        String callbackParam = jobProps.getString(TeamoneHttpJobPropsKey.HTTP_JOB_CALLBACK_PARAM.getKey(), TeamoneCommonConstants.DEFAULT_VALUE).trim();

        HashMap<String, Object> requestParamMap = new HashMap<>();
        HashMap<String, Object> callBackParamMap = new HashMap<>();

        if (callbackContentType.contains("form")) {
            for (String param : callbackParam.split("&")) {
                callBackParamMap.put(param.split("=")[0], param.split("=")[1]);
            }
        }

        if (requestContentType.contains("form")) {
            for (String param : requestParam.split("&")) {
                requestParamMap.put(param.split("=")[0], param.split("=")[1]);
            }
        }

        TeamoneHttpJobConfig teamoneHttpJobConfig =
                new TeamoneHttpJobConfig(requestURL, requestMethod, requestContentType, requestParam, callbackURL,
                        callbackMethod, callbackContentType, callbackParam, requestParamMap, callBackParamMap);

        try {
            // Teamone 获取Http的客户端
            HttpClient client = HCB.custom()
                    .sslpv(SSLs.SSLProtocolVersion.TLSv1_2)    //可设置ssl版本号，默认SSLv3，用于ssl，也可以调用sslpv("TLSv1.2")
                    .ssl()                    //https，支持自定义ssl证书路径和密码，ssl(String keyStorePath, String keyStorepass)
                    .retry(5)                    //重试5次
                    .build();

            getToken(jobId, teamoneHttpJobConfig, client, jobWorkDir, flowId);

            requestAndCallBack(jobId, teamoneHttpJobConfig, client, jobWorkDir, flowId);

        } catch (Exception e) {
            throw new TeamoneHttpJobException(e.getMessage(), projectName, flowId, execId, e);
        }


    }

    private void requestAndCallBack(String jobId, TeamoneHttpJobConfig teamoneHttpJobConfig, HttpClient client, String jobWorkDir, String flowId) {
        if(!(jobId.equals("login_request") || jobId.equals("login_callback"))){

        }

    }

    private void getToken(String jobId, TeamoneHttpJobConfig teamoneHttpJobConfig, HttpClient client, String jobWorkDir, String flowId)
            throws TeamoneHttpJobException, HttpProcessException, IOException {
        String result;
        String token = "";
        Props props = new Props();
        String propsKey = "";
        // Teamone 首先判断是不是 login 的job，如果是，那么就走的是 鉴权 并且 拿取对应的 token 并放在配置文件中的逻辑
        if (jobId.equals("login_request") || jobId.equals("login_callback")) {
            Header[] headers;

            if (jobId.contains("request")) {
                // Teamone 先对请求的url 进行判空处理，如果为空，那么直接抛异常，不需要执行
                if (StringUtils.isBlank(teamoneHttpJobConfig.getRequestURL())) {
                    throw new TeamoneHttpJobException("you must set this param [http_job.request.url]");
                }
                // Teamone 对请求方法进行验证
                if (!(teamoneHttpJobConfig.getRequestMethod().equals("get") || teamoneHttpJobConfig.getRequestMethod().equals("post"))) {
                    throw new TeamoneHttpJobException("you must set this param [http_job.request.method] in 'get' or 'post' ");
                }

                System.out.println(jobId);
                // Teamone 鉴权拿token逻辑
                headers = HttpHeader.custom().userAgent("Apifox/1.0.0").contentType(teamoneHttpJobConfig.getRequestContentType()).build();
                HttpConfig httpConfig = HttpConfig.custom()
                        .headers(headers)
                        .url(teamoneHttpJobConfig.getRequestURL())
                        .methodName(teamoneHttpJobConfig.getRequestMethod())
                        .encoding("utf-8").client(client);

                // Teamone 对参数进行设置
                httpConfig = httpConfig.map(teamoneHttpJobConfig.getRequestParamMap()).json(teamoneHttpJobConfig.getRequestParam());

                result = teamoneHttpJobConfig.getRequestMethod().equals("post") ?
                        HttpClientUtil.post(httpConfig) : HttpClientUtil.get(httpConfig);

                if (StringUtils.isBlank(result) || !JsonUtil.isJSON(result)) {
                    throw new TeamoneHttpJobException("Unable to obtain [log_request] result, please check the interface!");
                }

                token = JsonUtil.findValueInJSON(JSON.parseObject(result), "token");

                if (StringUtils.isBlank(token)) {
                    throw new TeamoneHttpJobException("Unable to obtain [log_request] token, please check the interface!");
                }
                propsKey = "request";
                props.put("request_token", token);
            }

            if (jobId.contains("callback")) {

                // Teamone 先对请求的url 进行判空处理，如果为空，那么直接抛异常，不需要执行
                if (StringUtils.isBlank(teamoneHttpJobConfig.getCallbackURL())) {
                    throw new TeamoneHttpJobException("you must set this param [http_job.callback.url]");
                }

                // Teamone 对请求方法进行验证
                if (!(teamoneHttpJobConfig.getCallbackMethod().equals("get") || teamoneHttpJobConfig.getCallbackMethod().equals("post"))) {
                    throw new TeamoneHttpJobException("you must set this param [http_job.callback.method] in 'get' or 'post' ");
                }

                System.out.println(jobId);
                // Teamone 鉴权拿token逻辑
                headers = HttpHeader.custom().contentType(teamoneHttpJobConfig.getCallbackContentType()).build();
                // Teamone 对参数进行设置
                HttpConfig httpConfig = HttpConfig.custom()
                        .headers(headers)
                        .url(teamoneHttpJobConfig.getCallbackURL())
                        .methodName(teamoneHttpJobConfig.getCallbackMethod())
                        .encoding("utf-8")
                        .map(teamoneHttpJobConfig.getCallBackParamMap())
                        .json(teamoneHttpJobConfig.getCallbackParam())
                        .client(client);

                result = teamoneHttpJobConfig.getCallbackMethod().equals("post") ? HttpClientUtil.post(httpConfig) : HttpClientUtil.get(httpConfig);
                System.out.println("result----" + result);

                if (StringUtils.isBlank(result) || !JsonUtil.isJSON(result)) {
                    throw new TeamoneHttpJobException("Unable to obtain [log_callback] result, please check the interface!");
                }
                token = JsonUtil.findValueInJSON(JSON.parseObject(result), "token");

                if (StringUtils.isBlank(token)) {
                    throw new TeamoneHttpJobException("Unable to obtain [log_callback] token, please check the interface!");
                }
                propsKey = "callback";
                props.put("callback_token", token);
            }
        }

        outputGeneratedProperties(props, jobWorkDir, flowId, propsKey);
    }


    private void outputGeneratedProperties(Props outputProperties, String jobWorkDir, String flowId, String propsKey) throws IOException {

        String outputDirPath = TeamoneCommonConstants.PATH_SPLIT_SYMBOL + "tmp" + jobWorkDir + TeamoneCommonConstants.PATH_SPLIT_SYMBOL + flowId;

        String outputFileName = flowId + "_" + propsKey + ".tmp";

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
}
