package com.neyogoo.example.common.authority.service;

import java.util.Set;

public interface AuthorityQueryService {

    /**
     * 获取拥有的角色编码
     */
    Set<String> getRoleCodes();

    /**
     * 获取拥有的权限编码
     */
    Set<String> getPermissionCodes();
}
