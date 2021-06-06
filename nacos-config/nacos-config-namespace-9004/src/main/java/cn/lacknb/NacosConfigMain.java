package cn.lacknb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: gitsilence
 * @description:
 * @date: 2021/6/6 10:09 上午
 **/
@SpringBootApplication
public class NacosConfigMain {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigMain.class, args);
    }

}

@RestController
// 通过Spring Cloud原生注解@RefreshScope 实现自动配置更新
@RefreshScope
class EchoController {

    @Value("${nacos.config.value}")
    private String config;

    @GetMapping("/config")
    public String getConfig () {
        return config;
    }

}
