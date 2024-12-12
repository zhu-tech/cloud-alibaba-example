package com.neyogoo.example.admin.sys.api;

import com.neyogoo.example.admin.sys.fallback.AuthorityApiFallbackFactory;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

/**
 * 权限校验
 */
@FeignClient(name = "example-admin-sys", fallbackFactory = AuthorityApiFallbackFactory.class,
        path = "/v1/authority", qualifiers = "authorityApi")
public interface AuthorityApi {

    /**
     * 获取当前登录人拥有的角色编码
     */
    @GetMapping("/roleCodes")
    R<Set<String>> getRoleCodes();

    /**
     * 获取当前登录人拥有的权限编码
     */
    @GetMapping("/permissionCodes")
    R<Set<String>> getPermissionCodes();
}
