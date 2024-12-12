package com.neyogoo.example.biz.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.neyogoo.example.biz.gateway.properties.IgnoreProperties;
import com.neyogoo.example.common.cache.model.CacheKey;
import com.neyogoo.example.common.cache.repository.CacheOps;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.exception.BizException;
import com.neyogoo.example.common.core.exception.UnauthorizedException;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.LoginTypeEnum;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import com.neyogoo.example.common.token.model.LoginTokenCache;
import com.neyogoo.example.common.token.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.neyogoo.example.common.core.constant.ContextConstant.*;
import static com.neyogoo.example.common.core.exception.code.ExceptionCode.UNAUTHORIZED;


/**
 * 过滤器
 */
@Component
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({IgnoreProperties.class})
public class TokenContextFilter implements WebFilter, Ordered {

    private final IgnoreProperties ignoreProperties;
    private final CacheOps cacheOps;
    @Value("${spring.profiles.active:dev}")
    protected String profiles;

    /**
     * 非生产环境，且 token 为指定值
     */
    protected boolean isDev(String token) {
        return !StrPool.PROD.equalsIgnoreCase(profiles) && (StrPool.TEST_TOKEN.equalsIgnoreCase(token)
                || StrPool.TEST.equalsIgnoreCase(token));
    }

    @Override
    public int getOrder() {
        return OrderedConstant.TOKEN;
    }


    protected String getHeader(String headerName, ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String token = StrUtil.EMPTY;
        if (headers == null || headers.isEmpty()) {
            return token;
        }

        token = headers.getFirst(headerName);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }
        return request.getQueryParams().getFirst(headerName);
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest.Builder mutate = request.mutate();

        try {
            // 1. 验证 Authorization
            parseAuthorization(request, mutate);
            // 2. 验证 token
            Mono<Void> mono = parseToken(exchange, chain);
            if (mono != null) {
                return mono;
            }
        } catch (UnauthorizedException e) {
            log.error(e.getMessage(), e);
            return errorResponse(response, e.getMessage(), e.getCode(), HttpStatus.UNAUTHORIZED);
        } catch (BizException e) {
            log.error(e.getMessage(), e);
            return errorResponse(response, e.getMessage(), e.getCode(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return errorResponse(response, "验证 token 出错", R.FAIL_CODE, HttpStatus.BAD_REQUEST);
        }

        ServerHttpRequest build = mutate.build();
        return chain.filter(exchange.mutate().request(build).build());
    }

    private void parseAuthorization(ServerHttpRequest request, ServerHttpRequest.Builder mutate) {
        if (ignoreProperties.isIgnoreAuthorization(request.getMethodValue(), request.getPath().toString())) {
            return;
        }
        String base64Authorization = getHeader(AUTHORIZATION_KEY, request);
        if (StrUtil.isEmpty(base64Authorization)) {
            throw BizException.wrap(UNAUTHORIZED, "解析用户身份错误，请重新登录！");
        }
        LoginPointEnum loginPoint = TokenUtils.extractLoginPoint(base64Authorization);
        ContextUtils.setLoginPoint(loginPoint.name());
        addHeader(mutate, HEADER_LOGIN_POINT, loginPoint.name());
    }

    private Mono<Void> parseToken(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest.Builder mutate = request.mutate();
        // 获取请求头中的 token
        String token = getHeader(HEADER_TOKEN, request);
        addHeader(mutate, HEADER_TOKEN, token);

        // 判断接口是否需要忽略 token 验证
        if (ignoreProperties.isIgnoreToken(request.getMethodValue(), request.getPath().toString())) {
            log.debug("当前接口：{}, 不解析用户 token", request.getPath());
            return chain.filter(exchange);
        }

        LoginTokenCache tokenCache;
        if (isDev(token)) {
            tokenCache = generateTestLoginTokenCache();
        } else {
            // 验证 token 是否有效
            CacheKey cacheKey = new CacheKey(TokenUtils.loginTokenCacheKey(token));
            tokenCache = cacheOps.get(cacheKey);
            if (tokenCache == null) {
                return errorResponse(response, "登录超时，请重新登录！", UNAUTHORIZED.getCode(),
                        HttpStatus.UNAUTHORIZED);
            }
        }

        // 将请求头中的 token 解析出来的用户信息重新封装到请求头，转发到业务服务，业务服务用拦截器将请求头中的用户信息解析到 ThreadLocal 中
        addTokenHeader(tokenCache, mutate);
        return null;
    }

    protected Mono<Void> errorResponse(ServerHttpResponse response, String errMsg, int errCode, HttpStatus httpStatus) {
        R tokenError = R.fail(errCode, errMsg);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatusCode(httpStatus);
        DataBuffer dataBuffer = response.bufferFactory().wrap(tokenError.toString().getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    private void addTokenHeader(LoginTokenCache tokenCache, ServerHttpRequest.Builder mutate) {
        if (tokenCache == null) {
            return;
        }
        ContextUtils.setUserId(tokenCache.getUser().getUserId());
        addHeader(mutate, HEADER_USER_TYPE, tokenCache.getUser().getUserType().name());
        addHeader(mutate, HEADER_USER_ID, tokenCache.getUser().getUserId());
        addHeader(mutate, HEADER_USER_NAME, tokenCache.getUser().getUserName());
        addHeader(mutate, HEADER_USER_ACCOUNT, tokenCache.getUser().getUserAccount());
        addHeader(mutate, HEADER_ORG_ID, tokenCache.getOrg().getOrgId());
        addHeader(mutate, HEADER_FEIGN, false);
        MDC.put(HEADER_USER_ID, String.valueOf(tokenCache.getUser().getUserId()));
        MDC.put(HEADER_ORG_ID, String.valueOf(tokenCache.getOrg().getOrgId()));
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = URLUtil.encode(valueStr);
        mutate.header(name, valueEncode);
    }

    private LoginTokenCache generateTestLoginTokenCache() {
        LoginTokenCache cache = new LoginTokenCache();
        cache.getUser()
                .setUserId(1L)
                .setUserName("测试用户")
                .setUserAccount("12345678901")
                .setUserType(UserTypeEnum.SysUser);
        cache.getOrg()
                .setOrgId(1L)
                .setOrgName("测试机构");
        cache.getLogin()
                .setLoginPoint(LoginPointEnum.PC)
                .setLoginType(LoginTypeEnum.Pwd)
                .setLoginAccount("12345678901")
                .setLoginTime(LocalDateTime.now());
        return cache;
    }
}
