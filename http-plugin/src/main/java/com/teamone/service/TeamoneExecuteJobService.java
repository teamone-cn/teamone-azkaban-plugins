package com.teamone.service;

import azkaban.utils.Props;
import com.teamone.exception.TeamoneHttpJobException;
import org.apache.log4j.Logger;

/**
 * Teamone
 * 执行业务服务的接口
 */
public interface TeamoneExecuteJobService {
    /**
     * Teamone
     * 执行具体HttpJob的方法
     *
     * @throws TeamoneHttpJobException
     */
    void executeJob(Props jobProps, Logger logger) throws TeamoneHttpJobException;
}
