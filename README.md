# Teamone Azkaban Plugins
基于 Azkaban 3.91.0 版本，目前新增了 Http 类型的任务，支持多种参数配置进行调用 Http接口的任务，该任务类型根据配置的url，配置的参数，以Azkaban所在服务器为客户端，对接口进行调用

# 主要特性
1. 支持将 Http 类型的任务添加到 Azkaban 中，其中设置对应的 A_request_token 和 B_callback_token 名称的任务，会拿到 A 和 B 两方的 token 并存储，后续建立依赖于该任务的任务时，调用鉴权即使用该 token
2. 支持将 request 返回的数据合并上 callback 的请求参数，作为新参数去请求 callback 接口
3. Http 类型的任务的配置丰富，有 超时时间，请求参数，是否鉴权，返回编码等，用于自定义对该接口请求和返回的失败的判定 

# 安装方式
执行打包命令
```shell
cd http-plugin
mvn clean package
```

将得到的 http-plugin-jar-with-dependencies.jar 上传到 Azkaban集群的每一台 executor 和 server 的 plugins/jobtypes/http/lib 目录下
然后在 plugins/jobtypes/http 目录下增加文件 private.properties 
内容为 
```text
jobtype.class=com.teamone.TeamoneHttpJob
jobtype.classpath=${plugin.dir}/lib/*

jobtype.lib.dir=${plugin.dir}/lib
```

重启azkaban集群，即可生效