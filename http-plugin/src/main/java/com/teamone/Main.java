package com.teamone;

import azkaban.utils.Props;
import com.teamone.exception.TeamoneHttpJobException;
import com.teamone.pojo.TeamoneHttpJobConfig;
import com.teamone.service.TeamoneExecuteJobService;
import com.teamone.service.impl.TeamoneExecuteJobServiceImpl;
import org.apache.log4j.Logger;


import java.io.IOException;


public class Main {
    private static Logger logger;
    public static void main(String[] args) throws TeamoneHttpJobException {

//        Props props1 = new Props();
//        props1.put("azkaban.job.id","login_request");
//        props1.put("http_job.request.url","https://svip-api.thwpmanage.com/api/vip/user/login");
//        props1.put("http_job.request.method","post");
//        props1.put("http_job.request.content.type","application/x-www-form-urlencoded");
//        props1.put("http_job.request.param","email=neil@tranhom.com&password=Neil758399577");
//
//
//        TeamoneExecuteJobService teamoneExecuteJobService1 = new TeamoneExecuteJobServiceImpl();
//        teamoneExecuteJobService1.executeJob(props1,logger);
//
//        Props props2 = new Props();
//        props2.put("azkaban.job.id","login_callback");
//        props2.put("http_job.callback.url","https://dev-email-marketing.thwpmanage.com/api/system/user/login");
//        props2.put("http_job.callback.method","post");
//        props2.put("http_job.callback.content.type","application/json");
//        props2.put("http_job.callback.param","{\"email\":\"tin@tranhom.com\",\"password\":\"123456\"}");
//
//        TeamoneExecuteJobService teamoneExecuteJobService2 = new TeamoneExecuteJobServiceImpl();
//        teamoneExecuteJobService2.executeJob(props2,logger);





    }
}