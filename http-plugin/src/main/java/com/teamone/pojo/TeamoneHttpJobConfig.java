package com.teamone.pojo;

import java.util.HashMap;

public class TeamoneHttpJobConfig {
    private String requestURL;
    private String requestMethod;
    private String requestContentType;
    private String requestParam;
    private String callbackURL;
    private String callbackMethod;
    private String callbackContentType;
    private String callbackParam;
    private HashMap<String, Object> requestParamMap;
    private HashMap<String, Object> callBackParamMap;

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

    public TeamoneHttpJobConfig(String requestURL, String requestMethod, String requestContentType, String requestParam, String callbackURL, String callbackMethod, String callbackContentType, String callbackParam, HashMap<String, Object> requestParamMap, HashMap<String, Object> callBackParamMap) {
        this.requestURL = requestURL;
        this.requestMethod = requestMethod;
        this.requestContentType = requestContentType;
        this.requestParam = requestParam;
        this.callbackURL = callbackURL;
        this.callbackMethod = callbackMethod;
        this.callbackContentType = callbackContentType;
        this.callbackParam = callbackParam;
        this.requestParamMap = requestParamMap;
        this.callBackParamMap = callBackParamMap;
    }

    @Override
    public String toString() {
        return "TeamoneHttpJobConfig{" +
                "requestURL='" + requestURL + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestContentType='" + requestContentType + '\'' +
                ", requestParam='" + requestParam + '\'' +
                ", callbackURL='" + callbackURL + '\'' +
                ", callbackMethod='" + callbackMethod + '\'' +
                ", callbackContentType='" + callbackContentType + '\'' +
                ", callbackParam='" + callbackParam + '\'' +
                ", requestParamMap=" + requestParamMap +
                ", callBackParamMap=" + callBackParamMap +
                '}';
    }
}
