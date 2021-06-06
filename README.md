# Spring Cloud Alibaba 的学习

每个知识点的入门Demo

## Nacos 配置中心

1、命名空间（namespace）
2、组（group）
3、data Id

默认情况下
Namespace=public
Group=DEFAULT_GROUP
默认cluster是DEFAULT

Nacos默认的命名空间是public，Namespace主要用来实现隔离

比如说我们现在有三个环境：开发、测试、生产环境，我们就可以创建三个Namespace，
不同的Namespace之间是隔离的。

Group 默认是DEFAULT_GROUP，GROUP可以把不同的微服务划分到同一个分组里面去




