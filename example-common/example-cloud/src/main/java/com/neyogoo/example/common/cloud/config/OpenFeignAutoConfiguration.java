package com.neyogoo.example.common.cloud.config;

import com.neyogoo.example.common.cloud.feign.DateFormatRegister;
import com.neyogoo.example.common.cloud.interceptor.FeignAddHeaderRequestInterceptor;
import feign.Feign;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.CustomFeignClientsRegistrar;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * OpenFeign 配置
 */
@Import(CustomFeignClientsRegistrar.class)
@ConditionalOnClass(Feign.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class OpenFeignAutoConfiguration {

    /**
     * 在feign调用方配置， 解决入参和出参是以下类型.
     * 1. @RequestParam("date") Date date
     * 2. @RequestParam("date") LocalDateTime date
     * 3. @RequestParam("date") LocalDate date
     * 4. @RequestParam("date") LocalTime date
     */
    @Bean
    public DateFormatRegister dateFormatRegister() {
        return new DateFormatRegister();
    }

    /**
     * feign 支持 MultipartFile 上传文件
     */
    @Bean
    public Encoder feignFormEncoder() {
        List<HttpMessageConverter<?>> converters = new RestTemplate().getMessageConverters();
        ObjectFactory<HttpMessageConverters> factory = () -> new HttpMessageConverters(converters);
        return new SpringFormEncoder(new SpringEncoder(factory));
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        return new FeignAddHeaderRequestInterceptor();
    }
}
