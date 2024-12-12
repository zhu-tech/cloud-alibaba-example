package com.neyogoo.example.admin.sys.api;

import com.neyogoo.example.admin.sys.fallback.OrgQueryApiFallbackFactory;
import com.neyogoo.example.admin.sys.vo.response.OrgTreeRespVO;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 机构查询
 */
@FeignClient(name = "example-admin-sys", fallbackFactory = OrgQueryApiFallbackFactory.class,
        path = "/v1/org/query", qualifiers = "orgQueryApi")
public interface OrgQueryApi {

    /**
     * 根据id查询名字
     */
    @GetMapping("/get/name/byId/{id}")
    R<String> getNameById(@PathVariable("id") @NotNull Long id);

    /**
     * 根据id列表查询名字
     */
    @PostMapping("/map/names/byIds")
    R<Map<Long, String>> mapNamesByIds(@RequestBody Collection<Long> ids);

    /**
     * 机构树
     */
    @GetMapping("/tree")
    R<List<OrgTreeRespVO>> findTree();
}
