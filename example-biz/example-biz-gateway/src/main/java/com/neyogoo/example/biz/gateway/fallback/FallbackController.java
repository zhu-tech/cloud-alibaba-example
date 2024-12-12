package com.neyogoo.example.biz.gateway.fallback;

import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.neyogoo.example.common.core.exception.code.ExceptionCode.SYSTEM_TIMEOUT;

/**
 * 响应超时熔断处理器
 */
@Slf4j
@RestController
public class FallbackController {

    @RequestMapping("/fallback")
    public Mono<R> fallback(ServerWebExchange exchange) {
        return Mono.just(R.validFail(SYSTEM_TIMEOUT));
    }
}
