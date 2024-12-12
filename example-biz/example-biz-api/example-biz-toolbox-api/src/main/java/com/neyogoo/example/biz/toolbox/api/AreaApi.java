package com.neyogoo.example.biz.toolbox.api;

import com.neyogoo.example.biz.toolbox.fallback.AreaApiFallbackFactory;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.NotBlank;

/**
 * 地区
 */
@FeignClient(name = "example-biz-toolbox", fallbackFactory = AreaApiFallbackFactory.class,
        path = "/v1/area", qualifiers = "areaApi")
public interface AreaApi {

    /**
     * 根据编码查询全称
     */
    @GetMapping("/get/fullName/byCode/{code}")
    R<String> getFullNameByCode(@PathVariable("code") @NotBlank String code);
}
