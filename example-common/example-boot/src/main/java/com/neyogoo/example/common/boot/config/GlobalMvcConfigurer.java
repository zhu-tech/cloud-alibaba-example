package com.neyogoo.example.common.boot.config;

import com.neyogoo.example.common.boot.interceptor.HeaderThreadLocalInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 请求拦截配置
 */
@AllArgsConstructor
public class GlobalMvcConfigurer implements WebMvcConfigurer {

    /**
     * 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 请求头中信息封装到 ThreadLocal 中
        registry.addInterceptor(new HeaderThreadLocalInterceptor()).addPathPatterns("/**");
    }

    /**
     * 跨域支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(false).maxAge(3600);
    }

    // 使用阿里 FastJson 作为JSON MessageConverter
    /*@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter =
                new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(stringHttpMessageConverter);

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        converters.add(byteArrayHttpMessageConverter);

        FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
        FastJsonConfig config = new FastJsonConfig();
        // 保留空的字段 String null -> ""
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }*/
}
