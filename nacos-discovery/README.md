# Nacos的服务注册

本项目实现了Nacos的服务注册功能，通过RestTemplate
实现服务调用， 为Http请求。

演示的时候，通过Idea实现端口映射 9000

![](https://cdn.jsdelivr.net/gh/MrNiebit/images@master/gitsilence/1622942288415-1622942288385.png)

![](https://cdn.jsdelivr.net/gh/MrNiebit/images@master/gitsilence/1622942335341-1622942335333.png)

也可以通过将 provider模块复制一份，将application.properties文件
中的server.port的值改成9000

![](https://cdn.jsdelivr.net/gh/MrNiebit/images@master/gitsilence/1622942551484-1622942551441.png)

点击详情 可以看到

![](https://cdn.jsdelivr.net/gh/MrNiebit/images@master/gitsilence/1622942621320-1622942621310.png)

## 教程

provider 模块和 consumer 模块引用的依赖是一样的

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
    </dependencies>
```
同样在启动类加上注解 @EnableDiscoveryClient

在配置文件中配置 nacos

### 负载均衡 方式一

```java
    @Bean
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }
        
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/echo")
    public String echo () {
        ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-payment-provider");
        String path = String.format("http://%s:%s/echo/%s", serviceInstance.getHost(), serviceInstance.getPort(), applicationName);
        System.out.println("request Path: " + path);
        return restTemplate.getForObject(path, String.class);
    }

```

### 负载均衡 方式二

```java
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/echo2")
    public String echo2 () {
        String path = serveNameUrl + "/echo/" + applicationName;
        System.out.println(path);
        return restTemplate.getForObject(path, String.class);
    }
```
