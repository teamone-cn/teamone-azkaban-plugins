package com.teamone;

import azkaban.jobExecutor.AbstractJob;
import azkaban.jobExecutor.AbstractProcessJob;
import azkaban.jobExecutor.Job;
import azkaban.utils.Props;
import azkaban.utils.PropsUtils;
import com.teamone.exception.TeamoneHttpJobException;
import com.teamone.service.TeamoneExecuteJobService;
import com.teamone.service.impl.TeamoneExecuteJobServiceImpl;
import org.apache.log4j.Logger;


/**
 * Teamone
 * 构建定制化的job类型，主要功能是将获取到的
 * reqURL 和 reqParam
 * callBackURL 和 callBackParam
 * 进行拆解后，携带 reqParam 通过调用 reqURL，得到结果后，将结果组装并和 callBackParam 一起组装成参数后请求 callBackURL 进行传递
 */
public class TeamoneHttpJob extends AbstractJob implements Job {
    /**
     * Teamone
     * azkaban系统参数
     */
    private Props sysProps;
    /**
     * Teamone
     * job配置参数
     */
    private Props jobProps;
    /**
     * Teamone
     * 任务调度类
     */
    private TeamoneExecuteJobService teamoneExecuteJobService;

    public TeamoneHttpJob(String jobId, Props sysProps, Props jobProps, Logger logger) {
        super(jobId, logger);

        this.sysProps = sysProps;
        this.jobProps = jobProps;
        this.teamoneExecuteJobService = new TeamoneExecuteJobServiceImpl();
    }

    /**
     * Teamone
     * 执行调度任务
     */
    @Override
    public void run() throws Exception {
        //Teamone 转化任务参数键值对
        try {
            this.resolveProps();
        } catch (Exception var22) {
            this.handleError("[http_job]Bad property definition! " + var22.getMessage(), var22);
        }
        //Teamone 执行任务
        try {

            System.out.println("jobProps======" + this.jobProps.toString());

            teamoneExecuteJobService.executeJob(this.jobProps, this.getLog());
        } catch (TeamoneHttpJobException e) {
            handleError(e.getMessage(), e);
        }

    }

    /**
     * Teamone
     * 处理异常信息
     */
    protected void handleError(String errorMsg, Exception e) throws Exception {
        this.error(errorMsg);
        if (e != null) {
            throw new Exception(errorMsg, e);
        } else {
            throw new Exception(errorMsg);
        }
    }

    /**
     * Teamone
     * 转换参数
     */
    protected void resolveProps() {
        this.jobProps = PropsUtils.resolveProps(this.jobProps);
    }

    public Props getSysProps() {
        return sysProps;
    }

    public void setSysProps(Props sysProps) {
        this.sysProps = sysProps;
    }

    public Props getJobProps() {
        return jobProps;
    }

    public void setJobProps(Props jobProps) {
        this.jobProps = jobProps;
    }

    public TeamoneExecuteJobService getExecuteJobService() {
        return teamoneExecuteJobService;
    }

    public void setExecuteJobService(TeamoneExecuteJobService teamoneExecuteJobService) {
        this.teamoneExecuteJobService = teamoneExecuteJobService;
    }
}
