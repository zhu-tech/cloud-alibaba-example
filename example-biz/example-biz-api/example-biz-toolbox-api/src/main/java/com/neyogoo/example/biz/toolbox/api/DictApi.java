package com.neyogoo.example.biz.toolbox.api;

import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import com.neyogoo.example.biz.toolbox.fallback.DictApiFallbackFactory;
import com.neyogoo.example.biz.toolbox.vo.response.DictRespVO;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 导出记录
 */
@FeignClient(name = "example-biz-toolbox", fallbackFactory = DictApiFallbackFactory.class,
        path = "/v1/dict", qualifiers = "dictApi")
public interface DictApi {

    /**
     * 根据类型查询字典列表
     */
    @GetMapping("/list/byType/{dictType}")
    R<List<DictRespVO>> listByType(@PathVariable("dictType") @NotNull DictTypeEnum dictType);

    /**
     * 根据类型查询字典映射
     */
    @GetMapping("/map/byType/{dictType}")
    R<Map<String, String>> mapByType(@PathVariable("dictType") @NotNull DictTypeEnum dictType);

    /**
     * 根据类型查询字典可用列表
     */
    @PostMapping("/map/usable/byTypes")
    R<Map<String, Map<String, String>>> mapUsableByTypes(@RequestBody List<String> dictTypes);
}
