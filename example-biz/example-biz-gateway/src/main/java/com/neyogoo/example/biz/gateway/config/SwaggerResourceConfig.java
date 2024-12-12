package com.neyogoo.example.biz.gateway.config;

import cn.hutool.core.collection.CollUtil;
import com.neyogoo.example.common.core.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * 资源配置
 */
@Component
@Primary
@Slf4j
@RequiredArgsConstructor
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    private static final String SWAGGER_RESOURCES_URI = "/swagger-resources";
    private final GatewayProperties gatewayProperties;

    @Resource(name = "lbRestTemplate")
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;
    private ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    /**
     * 遍历网关的路由，并通过 restTemplate 访问后端服务的 swagger 信息，聚合后返回
     */
    @Override
    public List<SwaggerResource> get() {
        return gatewayProperties.getRoutes().stream().flatMap(this::swaggerResources)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Stream<SwaggerResource> swaggerResources(RouteDefinition route) {
        try {
            if (route.getUri().getHost() == null) {
                return Stream.empty();
            }
            // 聚合所有 group
            List<ServiceInstance> instances = discoveryClient.getInstances(route.getUri().getHost());
            if (CollUtil.isEmpty(instances)) {
                return Stream.empty();
            }
            // WebFlux 异步调用
            Future future = executorService.submit(() ->
                    restTemplate.getForObject("http://" + route.getUri().getHost() + SWAGGER_RESOURCES_URI,
                            Object.class)
            );
            String json = JsonUtils.toJson(future.get());
            List<SwaggerResource> srList = JsonUtils.parseArray(json, SwaggerResource.class);
            for (SwaggerResource sr : srList) {
                sr.setName(route.getId() + "-" + sr.getName());
                sr.setUrl("/" + route.getId() + sr.getUrl());
            }
            return srList.stream();
        } catch (Exception e) {
            log.debug("加载 {} 的 swagger 文档信息失败。 请确保该服务成功启动，并注册到了 nacos", route.getUri().getHost(), e);
        }
        return null;
    }
}
