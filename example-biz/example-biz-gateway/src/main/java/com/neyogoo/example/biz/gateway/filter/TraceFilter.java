package com.neyogoo.example.biz.gateway.filter;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.neyogoo.example.common.core.constant.ContextConstant.HEADER_TRACE_ID;

/**
 * 生成日志链路追踪 id，并传入 header 中
 */
@Component
public class TraceFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 链路追踪 id
        String traceId = IdUtil.fastSimpleUUID();
        MDC.put(HEADER_TRACE_ID, traceId);
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .headers(h -> h.add(HEADER_TRACE_ID, traceId))
                .build();
        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return chain.filter(build);
    }

    @Override
    public int getOrder() {
        return OrderedConstant.TRACE;
    }
}
