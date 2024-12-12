package com.neyogoo.example.biz.gateway;

import com.neyogoo.example.common.core.constant.BasicConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@EnableDiscoveryClient
@ComponentScan({BasicConstant.COMPONENT_SCAN_LOCATION})
@EnableFeignClients(value = {BasicConstant.COMPONENT_SCAN_LOCATION})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GatewayBootApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(GatewayBootApplication.class, args);
        Environment env = application.getEnvironment();
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "应用 '{}' 运行成功! 访问连接:\n\t"
                        + "Swagger文档: \t\thttp://{}:{}{}{}/doc.html\n\t"
                        + "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path", ""),
                env.getProperty("spring.mvc.servlet.path", "")
        );
    }
}
