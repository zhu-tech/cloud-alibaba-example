package com.neyogoo.example.biz.toolbox.fallback;

import com.neyogoo.example.biz.toolbox.api.AreaApi;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 地区
 */
@Slf4j
@Component
public class AreaApiFallbackFactory implements FallbackFactory<AreaApi> {

    @Override
    public AreaApi create(Throwable cause) {

        return new AreaApi() {

            @Override
            public R<String> getFullNameByCode(String code) {
                log.error("get area full name by code error, code = {}, cause = {}", code, cause.getMessage());
                return R.fail("根据地区编码查询地区全称失败");
            }
        };
    }
}