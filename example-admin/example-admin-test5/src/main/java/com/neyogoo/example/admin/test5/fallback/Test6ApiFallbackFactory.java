package com.neyogoo.example.admin.test5.fallback;

import com.neyogoo.example.admin.test5.api.Test6Api;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Test6ApiFallbackFactory implements FallbackFactory<Test6Api> {

    @Override
    public Test6Api create(Throwable cause) {

        return new Test6Api() {

            @Override
            public R<Boolean> addUser() {
                log.error("add user error, cause = {}", cause.getMessage());
                return R.fail("add user error");
            }
        };
    }
}
