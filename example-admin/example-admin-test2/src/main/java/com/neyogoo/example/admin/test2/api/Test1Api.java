package com.neyogoo.example.admin.test2.api;

import com.neyogoo.example.admin.test2.fallback.Test1ApiFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "example-admin-test1", fallbackFactory = Test1ApiFallbackFactory.class,
        path = "/v1/test1", qualifiers = "test1Api")
public interface Test1Api {

    @GetMapping("/anno/addOrder")
    void addOrder(@RequestParam("userId") Long userId, @RequestParam("orderId") Long orderId);
}
