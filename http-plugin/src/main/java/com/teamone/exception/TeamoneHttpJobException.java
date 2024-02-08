package com.teamone.exception;

/**
 * Teamone
 * http-plugin 的job的自定义异常类
 */
public class TeamoneHttpJobException extends Exception {
    /**
     * Teamone
     * 项目名称
     */
    private String projectName;
    /**
     * Teamone
     * 工作流ID
     */
    private String flowId;
    /**
     * Teamone
     * 调度ID
     */
    private String execId;

    public TeamoneHttpJobException(String message, String projectName, String flowId, String execId, Throwable cause) {
        super(message, cause);
        this.projectName = projectName;
        this.flowId = flowId;
        this.execId = execId;
    }

    public TeamoneHttpJobException() {
    }

    public TeamoneHttpJobException(String message) {
        super(message);
    }

    public TeamoneHttpJobException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamoneHttpJobException(Throwable cause) {
        super(cause);
    }

    public TeamoneHttpJobException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getExecId() {
        return execId;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
