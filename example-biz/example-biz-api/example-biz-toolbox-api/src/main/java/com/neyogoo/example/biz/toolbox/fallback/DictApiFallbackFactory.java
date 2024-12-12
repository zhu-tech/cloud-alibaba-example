package com.neyogoo.example.biz.toolbox.fallback;

import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import com.neyogoo.example.biz.toolbox.api.DictApi;
import com.neyogoo.example.biz.toolbox.vo.response.DictRespVO;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DictApiFallbackFactory implements FallbackFactory<DictApi> {

    @Override
    public DictApi create(Throwable cause) {

        return new DictApi() {

            @Override
            public R<List<DictRespVO>> listByType(DictTypeEnum dictType) {
                log.error("list dict by type error, dictType = {}, cause = {}", dictType, cause.getMessage());
                return R.fail("根据类型查询字典列表失败");
            }

            @Override
            public R<Map<String, String>> mapByType(DictTypeEnum dictType) {
                log.error("map dict by type error, dictType = {}, cause = {}", dictType, cause.getMessage());
                return R.fail("根据类型查询字典对应关系失败");
            }

            @Override
            public R<Map<String, Map<String, String>>> mapUsableByTypes(List<String> dictTypes) {
                log.error("map usable dict by types error, dictType = {}, cause = {}", dictTypes, cause.getMessage());
                return R.fail("根据类型查询字典可用列表失败");
            }
        };
    }
}
