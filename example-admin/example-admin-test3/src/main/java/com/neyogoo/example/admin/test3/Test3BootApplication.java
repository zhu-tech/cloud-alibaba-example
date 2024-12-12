package com.neyogoo.example.admin.test3;

import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableAsync
@Configuration
@EnableScheduling
@ComponentScan(BasicConstant.COMPONENT_SCAN_LOCATION)
@EnableFeignClients(BasicConstant.COMPONENT_SCAN_LOCATION)
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableDiscoveryClient
@SpringBootApplication
public class Test3BootApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(Test3BootApplication.class, args);
        Environment env = application.getEnvironment();
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "应用 '{}' 启动成功! 访问连接:\n\t"
                        + "Swagger文档: \t\thttp://{}:{}/doc.html\n\t"
                        + "数据库监控: \t\thttp://{}:{}/druid\n"
                        + "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port")
        );
    }
}
