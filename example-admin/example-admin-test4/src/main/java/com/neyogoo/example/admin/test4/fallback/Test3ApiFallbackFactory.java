package com.neyogoo.example.admin.test4.fallback;

import com.neyogoo.example.admin.test4.api.Test3Api;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Test3ApiFallbackFactory implements FallbackFactory<Test3Api> {

    @Override
    public Test3Api create(Throwable cause) {

        return new Test3Api() {

            @Override
            public R<Boolean> addOrder(Long userId, Long orderId) {
                log.error("add order error, cause = {}", cause.getMessage());
                return R.fail("add order error");
            }
        };
    }
}
