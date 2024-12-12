package com.neyogoo.example.admin.test5.api;

import com.neyogoo.example.admin.test5.fallback.Test6ApiFallbackFactory;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "example-admin-test6", fallbackFactory = Test6ApiFallbackFactory.class,
        path = "/v1/test6", qualifiers = "test6Api")
public interface Test6Api {

    @GetMapping("/anno/add/user")
    R<Boolean> addUser();
}
