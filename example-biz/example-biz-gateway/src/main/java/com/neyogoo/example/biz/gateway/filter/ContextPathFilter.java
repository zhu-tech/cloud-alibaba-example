package com.neyogoo.example.biz.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(OrderedConstant.CONTEXT_PATH)
@RequiredArgsConstructor
public class ContextPathFilter implements WebFilter {
    private final ServerProperties serverProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String contextPath = serverProperties.getServlet().getContextPath();
        String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
        if (contextPath != null && requestPath.startsWith(contextPath)) {
            requestPath = requestPath.substring(contextPath.length());
        }
        return chain.filter(exchange.mutate().request(
                exchange.getRequest().mutate().path(requestPath).build()
        ).build());
    }
}
