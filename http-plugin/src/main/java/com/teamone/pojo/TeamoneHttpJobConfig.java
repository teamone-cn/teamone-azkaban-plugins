package com.teamone.pojo;

import java.util.HashMap;

/**
 * Teamone
 * 用于构建httpJob的config对象
 */
public class TeamoneHttpJobConfig {
    private String requestURL;
    private String requestMethod;
    private String requestContentType;
    private String requestParam;
    private String requestCallbackParamKey;
    private String requestTimeout;
    private String requestCode;

    private String requestNeedToken;
    private String callbackURL;
    private String callbackMethod;
    private String callbackContentType;
    private String callbackParam;
    private String callbackTimeout;
    private String callbackCode;
    private String callbackNeedToken;
    private HashMap<String, Object> requestParamMap;
    private HashMap<String, Object> callBackParamMap;

    public TeamoneHttpJobConfig(String requestURL, String requestMethod, String requestContentType, String requestParam, String requestCallbackParamKey, String requestTimeout, String requestCode, String requestNeedToken, String callbackURL, String callbackMethod, String callbackContentType, String callbackParam, String callbackTimeout, String callbackCode, String callbackNeedToken, HashMap<String, Object> requestParamMap, HashMap<String, Object> callBackParamMap) {
        this.requestURL = requestURL;
        this.requestMethod = requestMethod;
        this.requestContentType = requestContentType;
        this.requestParam = requestParam;
        this.requestCallbackParamKey = requestCallbackParamKey;
        this.requestTimeout = requestTimeout;
        this.requestCode = requestCode;
        this.requestNeedToken = requestNeedToken;
        this.callbackURL = callbackURL;
        this.callbackMethod = callbackMethod;
        this.callbackContentType = callbackContentType;
        this.callbackParam = callbackParam;
        this.callbackTimeout = callbackTimeout;
        this.callbackCode = callbackCode;
        this.callbackNeedToken = callbackNeedToken;
        this.requestParamMap = requestParamMap;
        this.callBackParamMap = callBackParamMap;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getRequestCallbackParamKey() {
        return requestCallbackParamKey;
    }

    public void setRequestCallbackParamKey(String requestCallbackParamKey) {
        this.requestCallbackParamKey = requestCallbackParamKey;
    }

    public String getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(String requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestNeedToken() {
        return requestNeedToken;
    }

    public void setRequestNeedToken(String requestNeedToken) {
        this.requestNeedToken = requestNeedToken;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public String getCallbackMethod() {
        return callbackMethod;
    }

    public void setCallbackMethod(String callbackMethod) {
        this.callbackMethod = callbackMethod;
    }

    public String getCallbackContentType() {
        return callbackContentType;
    }

    public void setCallbackContentType(String callbackContentType) {
        this.callbackContentType = callbackContentType;
    }

    public String getCallbackParam() {
        return callbackParam;
    }

    public void setCallbackParam(String callbackParam) {
        this.callbackParam = callbackParam;
    }

    public String getCallbackTimeout() {
        return callbackTimeout;
    }

    public void setCallbackTimeout(String callbackTimeout) {
        this.callbackTimeout = callbackTimeout;
    }

    public String getCallbackCode() {
        return callbackCode;
    }

    public void setCallbackCode(String callbackCode) {
        this.callbackCode = callbackCode;
    }

    public String getCallbackNeedToken() {
        return callbackNeedToken;
    }

    public void setCallbackNeedToken(String callbackNeedToken) {
        this.callbackNeedToken = callbackNeedToken;
    }

    public HashMap<String, Object> getRequestParamMap() {
        return requestParamMap;
    }

    public void setRequestParamMap(HashMap<String, Object> requestParamMap) {
        this.requestParamMap = requestParamMap;
    }

    public HashMap<String, Object> getCallBackParamMap() {
        return callBackParamMap;
    }

    public void setCallBackParamMap(HashMap<String, Object> callBackParamMap) {
        this.callBackParamMap = callBackParamMap;
    }

    @Override
    public String toString() {
        return "TeamoneHttpJobConfig{" +
                "requestURL='" + requestURL + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestContentType='" + requestContentType + '\'' +
                ", requestParam='" + requestParam + '\'' +
                ", requestCallbackParamKey='" + requestCallbackParamKey + '\'' +
                ", requestTimeout='" + requestTimeout + '\'' +
                ", requestCode='" + requestCode + '\'' +
                ", requestNeedToken='" + requestNeedToken + '\'' +
                ", callbackURL='" + callbackURL + '\'' +
                ", callbackMethod='" + callbackMethod + '\'' +
                ", callbackContentType='" + callbackContentType + '\'' +
                ", callbackParam='" + callbackParam + '\'' +
                ", callbackTimeout='" + callbackTimeout + '\'' +
                ", callbackCode='" + callbackCode + '\'' +
                ", callbackNeedToken='" + callbackNeedToken + '\'' +
                ", requestParamMap=" + requestParamMap +
                ", callBackParamMap=" + callBackParamMap +
                '}';
    }
}
