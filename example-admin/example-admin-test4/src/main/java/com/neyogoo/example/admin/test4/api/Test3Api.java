package com.neyogoo.example.admin.test4.api;

import com.neyogoo.example.admin.test4.fallback.Test3ApiFallbackFactory;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "example-admin-test3", fallbackFactory = Test3ApiFallbackFactory.class,
        path = "/v1/test3", qualifiers = "test3Api")
public interface Test3Api {

    @GetMapping("/anno/add/order")
    R<Boolean> addOrder(@RequestParam("userId") Long userId, @RequestParam("orderId") Long orderId);
}
