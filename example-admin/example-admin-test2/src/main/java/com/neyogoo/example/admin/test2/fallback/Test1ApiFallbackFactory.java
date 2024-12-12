package com.neyogoo.example.admin.test2.fallback;

import com.neyogoo.example.admin.test2.api.Test1Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Test1ApiFallbackFactory implements FallbackFactory<Test1Api> {

    @Override
    public Test1Api create(Throwable cause) {

        return new Test1Api() {

            @Override
            public void addOrder(Long userId, Long orderId) {
                log.error("add order error, cause = {}", cause.getMessage());
            }
        };
    }
}
