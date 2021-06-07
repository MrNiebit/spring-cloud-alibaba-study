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

## Nacos 持久化配置（MySQL）

修改文件 `conf/application.properties`

```properties
### If use MySQL as datasource:
spring.datasource.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=root
```
将这几行取消注释，然后本地创建数据库名为 nacos_config

然后将 nacos_mysql.sql 导入数据库

最后直接 `./startup.sh -m standalone`

## Nacos 集群搭建

可以参考 官网 架构图 

至少配置3个节点

### 配置自定义端口启动nacos

我们这里修改启动脚本，启动3个端口不一样的节点

```shell
while getopts ":m:f:s:c:p:P:" opt
do
    case $opt in
        m)
            MODE=$OPTARG;;
        f)
            FUNCTION_MODE=$OPTARG;;
        s)
            SERVER=$OPTARG;;
        c)
            MEMBER_LIST=$OPTARG;;
        p)
            EMBEDDED_STORAGE=$OPTARG;;
        P)
            PORT=$OPTARG;;
        ?)
        echo "Unknown parameter"
        exit 1;;
    esac
done
```
这里增加了 `-P` 参数，设置变量PORT
```shell
echo "$JAVA ${JAVA_OPT}" > ${BASE_DIR}/logs/start.out 2>&1 &
nohup $JAVA -Dserver.port=${PORT} ${JAVA_OPT} nacos.nacos >> ${BASE_DIR}/logs/start.out 2>&1 &
echo "nacos is starting，you can check the ${BASE_DIR}/logs/start.out"
```
这里使用前面定义的变量：`-Dserver.port=${PORT}`

这样就可以实现自定义端口启动了

测试一下 `./startup.sh -P 6767 -m standalone`

能够通过6767端口 访问到naco管理页面 就算成功了。

### 配置集群

使用 `nacos-server-2.0.0.tar.gz`

在conf目录下创建 `cluster.conf`，如下配置

```conf
192.168.43.17:6677
192.168.43.17:6678
192.168.43.17:6679
```
不能使用 `127.0.0.1`, 要使用局域网中ip

然后执行启动命令
```shell
./startup.sh -P 6677
./startup.sh -P 6678
./startup.sh -P 6679
```
启动3个节点

6677：占用端口有：5677、6677、7677、7678 均为TCP协议

6678：占用端口有：5678、6678、7678、7679

6679：占用端口有：5679、6679、7679、7680 `这里7679重复，会导致集群启动错误`

> 因为nacos 2.x 新增了两个端口 用来gRPC通信，这两个端口为 主端口分别加上 1000 和 1001 的和
>
> 例如：主端口为 6677，则另外两个端口为：7677、7678
>
> jRaft 选举leader 占用端口为：主端口 - 1000 也就是 5677

所以建议集群端口设置为：
```shell
./startup.sh -P 6666
./startup.sh -P 7777
./startup.sh -P 8888
```

**还有一个问题：nacos默认占用的内存非常大，可以先改下配置**

`vim bin/startup.sh`

```shell
if [[ "${MODE}" == "standalone" ]]; then
    JAVA_OPT="${JAVA_OPT} -Xms512m -Xmx512m -Xmn256m"
    JAVA_OPT="${JAVA_OPT} -Dnacos.standalone=true"
else
    if [[ "${EMBEDDED_STORAGE}" == "embedded" ]]; then
        JAVA_OPT="${JAVA_OPT} -DembeddedStorage=true"
    fi
    JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
    JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/java_heapdump.hprof"
    JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages"

fi
```
else代码块代表集群，默认的配置为：`-Xms2g -Xmx2g -Xmn1g`

可以调整为：`-Xms256m -Xmx256m -Xmn128m`

**经测试，以上配置修改后，将nacos复制成三份，分别以不同的端口启动**

如果使用一个nacos启动三个nacos，两个会启动失败。

成功界面
![](https://cdn.jsdelivr.net/gh/MrNiebit/images@master/gitsilence/1622986247688-1622986247590.png)

服务调用的时候，我们通过Nginx反向代理实现负载均衡
```conf
    upstream nacosserver {
        server  192.168.10.53:6666;
        server  192.168.10.53:7777;
        server  192.168.10.53:8888;
    }

    server {
        listen       81;
        server_name  localhost;
        location / {
            proxy_pass  http://nacosserver;
        }
    }
```

> **使用VIP/nginx请求时，需要配置成TCP转发，不能配置http2转发，否则连接会被nginx断开。**

请参考官网：[https://nacos.io/zh-cn/docs/2.0.0-compatibility.html](https://nacos.io/zh-cn/docs/2.0.0-compatibility.html)

