package com.neyogoo.example.admin.test4.api;

import com.neyogoo.example.admin.test4.fallback.Test5ApiFallbackFactory;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "example-admin-test5", fallbackFactory = Test5ApiFallbackFactory.class,
        path = "/v1/test5", qualifiers = "test5Api")
public interface Test5Api {

    @GetMapping("/anno/add/user")
    R<Boolean> addUser();
}
