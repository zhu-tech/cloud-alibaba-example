package com.neyogoo.example.common.authority.permission;

import cn.hutool.core.collection.CollUtil;
import com.neyogoo.example.common.authority.service.AuthorityQueryService;

import java.util.Set;

/**
 * 权限判断
 */
public class AuthorityFunction {

    /**
     * 是否大小写敏感
     */
    private static final boolean CASE_SENSITIVE = false;
    /**
     * 查询登录用户权限方法
     */
    private final AuthorityQueryService authorityQueryService;

    public AuthorityFunction(AuthorityQueryService authorityQueryService) {
        this.authorityQueryService = authorityQueryService;
    }

    public boolean permit() {
        return true;
    }

    public boolean hasRole(String... role) {
        return CollUtil.containsAll(userRoleCodes(), CollUtil.newHashSet(role));
    }

    public boolean hasAnyRole(String... role) {
        return CollUtil.containsAny(userRoleCodes(), CollUtil.newHashSet(role));
    }

    public boolean hasPermission(String... permit) {
        return AuthorizingRealm.hasPermission(userPermissionCodes(), permit, CASE_SENSITIVE);
    }

    public boolean hasAnyPermission(String... permit) {
        return AuthorizingRealm.hasAnyPermission(userPermissionCodes(), permit, CASE_SENSITIVE);
    }

    /**
     * 查询当前登录的用户拥有的权限编码
     */
    private Set<String> userPermissionCodes() {
        return authorityQueryService.getPermissionCodes();
    }

    /**
     * 查询当前登录的用户拥有的角色编码
     */
    private Set<String> userRoleCodes() {
        return authorityQueryService.getRoleCodes();
    }
}
