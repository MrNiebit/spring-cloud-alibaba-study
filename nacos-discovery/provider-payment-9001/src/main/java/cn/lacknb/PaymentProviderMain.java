package cn.lacknb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: gitsilence
 * @description:
 * @date: 2021/6/5 2:36 下午
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentProviderMain {
    public static void main(String[] args) {
        SpringApplication.run(PaymentProviderMain.class, args);
    }

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @RestController
    public class EchoController {
        @GetMapping("/echo/{string}")
        public String echo (@PathVariable String string) {
            return "Hello Nacos Discovery -> " + string + ", I'm " + applicationName
                    + " My Server Port is ---> " + serverPort;
        }
    }
}
