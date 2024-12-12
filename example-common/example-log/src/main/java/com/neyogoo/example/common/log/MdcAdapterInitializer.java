package com.neyogoo.example.common.log;

import org.slf4j.CustomMdcAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

/**
 * 初始化 TtlMDCAdapter 实例，并替换 MDC 中的 adapter 对象
 */
public class MdcAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        // 加载 TtlMDCAdapter 实例
        CustomMdcAdapter.getInstance();
    }
}
