package com.teamone.pojo;

/**
 * 获取到job名称的前后缀，便于对当前job进行分类
 */
public class TeamoneHttpJobInfo {
    private String prefix;
    private String suffix;
    private boolean isGetToken;

    public TeamoneHttpJobInfo(String prefix, String suffix, boolean isGetToken) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.isGetToken = isGetToken;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean getGetToken() {
        return isGetToken;
    }

    public void setGetToken(boolean getToken) {
        isGetToken = getToken;
    }

    @Override
    public String toString() {
        return "TeamoneHttpJobInfo{" +
                "prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", isGetToken=" + isGetToken +
                '}';
    }
}
