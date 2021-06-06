
## 问题

查看了好几遍配置，没有问题，一直报错注入 ${nacos.config.value} 注入失败

网上查询当前项目的WARN日志，让重启nacos，重启了，果然又好了。

电脑上的nacos从昨天开始就一直打开着，然后把笔记本合上盖子
今天就直接用了。

难道是 nacos 开的时间太长了？无法找到 新添加的配置？

```log
2021-06-06 11:21:12.893  WARN 27135 --- [           main] c.a.c.n.c.NacosPropertySourceBuilder     : Ignore the empty nacos configuration and get it based on dataId[nacos-config-client-err] & group[DEFAULT_GROUP]
2021-06-06 11:21:12.904  WARN 27135 --- [           main] c.a.c.n.c.NacosPropertySourceBuilder     : Ignore the empty nacos configuration and get it based on dataId[nacos-config-client-err.yaml] & group[DEFAULT_GROUP]
2021-06-06 11:21:12.908  WARN 27135 --- [           main] c.a.c.n.c.NacosPropertySourceBuilder     : Ignore the empty nacos configuration and get it based on dataId[nacos-config-client-err-dev.yaml] & group[DEFAULT_GROUP]
```

这个WARN日志很关键，`Ignore the empty nacos configuration and get it based on dataId[nacos-config-client-err-dev.yaml] `

意思是没找到 配置，根据配置文件的配置，会去nacos上找 `nacos-config-client-err-dev.yaml`

重启nacos之后，就没有这条warn日志了。

## 基本配置

如果只需要实现远程配置，只需要以下依赖。
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```
我们只需要创建 `bootstrap.properties` 和 `application.properties` 两个文件

`boostrap.properties` 只需要配置一下nacos就行了
```properties
spring.cloud.nacos.config.server-addr=127.0.0.1
spring.cloud.nacos.config.file-extension=yaml
```

`application.properties` 配置如下
```properties
spring.application.name=nacos-config-client
server.port=9003
spring.profiles.active=dev
```
则nacos中data id 为 `nacos-config-client-dev.yaml`
