package com.teamone.constans;

import static com.teamone.constans.TeamoneCommonConstants.CUSTOM_PREFIX;

/**
 * Teamone
 * 新增的 HttpJob 的属性的key
 */
public enum TeamoneHttpJobPropsKey {


    HTTP_JOB_REQUEST_URL(String.format("%s.request.url", CUSTOM_PREFIX)),

    HTTP_JOB_REQUEST_METHOD(String.format("%s.request.method", CUSTOM_PREFIX)),

    HTTP_JOB_REQUEST_CONTENT_TYPE(String.format("%s.request.content.type", CUSTOM_PREFIX)),

    HTTP_JOB_REQUEST_PARAM(String.format("%s.request.param", CUSTOM_PREFIX)),

    HTTP_JOB_CALLBACK_URL(String.format("%s.callback.url", CUSTOM_PREFIX)),

    HTTP_JOB_CALLBACK_METHOD(String.format("%s.callback.method", CUSTOM_PREFIX)),

    HTTP_JOB_CALLBACK_CONTENT_TYPE(String.format("%s.callback.content.type", CUSTOM_PREFIX)),

    HTTP_JOB_CALLBACK_PARAM(String.format("%s.callback.param", CUSTOM_PREFIX));

    private final String key;

    TeamoneHttpJobPropsKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}