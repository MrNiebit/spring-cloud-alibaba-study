package cn.lacknb;

import cn.lacknb.dto.CommonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: gitsilence
 * @description:
 * @date: 2021/6/6 10:09 上午
 **/
@SpringBootApplication
@EnableConfigurationProperties
public class NacosConfigMain {

    public static void main(String[] args) {
        SpringApplication.run(NacosConfigMain.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "config")
    public CommonConfig commonConfig () {
        return new CommonConfig();
    }

}

@RestController
// 通过Spring Cloud原生注解@RefreshScope 实现自动配置更新
@RefreshScope
class EchoController {

    @Autowired
    private CommonConfig commonConfig;

    @GetMapping("/config")
    public CommonConfig getConfig () {
        return commonConfig;
    }

}
