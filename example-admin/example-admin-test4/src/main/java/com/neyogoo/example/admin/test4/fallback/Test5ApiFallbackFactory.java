package com.neyogoo.example.admin.test4.fallback;

import com.neyogoo.example.admin.test4.api.Test5Api;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Test5ApiFallbackFactory implements FallbackFactory<Test5Api> {

    @Override
    public Test5Api create(Throwable cause) {

        return new Test5Api() {

            @Override
            public R<Boolean> addUser() {
                log.error("add user error, cause = {}", cause.getMessage());
                return R.fail("add user error");
            }
        };
    }
}
