package com.neyogoo.example.biz.gateway.filter;

import org.springframework.core.Ordered;

/**
 * Filter 调用顺序
 */
public interface OrderedConstant {

    /**
     * 链路跟踪id
     */
    int TRACE = Ordered.HIGHEST_PRECEDENCE;
    /**
     * swagger
     */
    int SWAGGER = 1;
    /**
     * 解析 token
     */
    int TOKEN = -1000;
    /**
     * 解决路径前缀
     */
    int CONTEXT_PATH = -1100;
}
