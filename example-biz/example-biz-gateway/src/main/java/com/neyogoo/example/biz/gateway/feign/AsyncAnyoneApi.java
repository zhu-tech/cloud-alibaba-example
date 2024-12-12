package com.neyogoo.example.biz.gateway.feign;

import com.neyogoo.example.common.core.model.R;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class AsyncAnyoneApi {

    @Async
    public Future<R<Boolean>> checkApplication(Long applicationId, Long userId) {
        R<Boolean> r = R.success(Boolean.TRUE);
        return new AsyncResult<>(r);
    }

    @Async
    public Future<R<Boolean>> checkUri(Long userId, Long applicationId, String path, String method) {
        R<Boolean> r = R.success(Boolean.TRUE);
        return new AsyncResult<>(r);
    }
}
