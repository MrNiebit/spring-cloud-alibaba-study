package cn.lacknb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author: gitsilence
 * @description:
 * @date: 2021/6/5 2:48 下午
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentConsumerMain {

    public static void main(String[] args) {
        SpringApplication.run(PaymentConsumerMain.class, args);
    }

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
//    @LoadBalanced
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }

    @Value("${spring.application.name}")
    private String applicationName;
    
    @RestController
    public class EchoController {

        /**
         * 第一种负载均衡方式
         *   运行此方法，需要去掉上面的 @LoadBalanced 注解
         * @return
         */
        @GetMapping("/echo")
        public String echo () {
            ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-payment-provider");
            String path = String.format("http://%s:%s/echo/%s", serviceInstance.getHost(), serviceInstance.getPort(), applicationName);
            System.out.println("request Path: " + path);
            return restTemplate.getForObject(path, String.class);
        }

        @Value("${cloud.provider.serve.url}")
        private String serveNameUrl;

        /**
         * 第二种负载均衡方式
         * 需要在restTemplate 放入Spring容器的时候
         * 也就是在 @Bean 那 加入 @LoadBalanced注解
         * @return
         */
        @GetMapping("/echo2")
        public String echo2 () {
            String path = serveNameUrl + "/echo/" + applicationName;
            System.out.println(path);
            return restTemplate.getForObject(path, String.class);
        }
    }

}
