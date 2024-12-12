package com.neyogoo.example.admin.sys.service.impl.ext;

import com.neyogoo.example.admin.sys.service.UserRoleService;
import com.neyogoo.example.common.authority.service.AuthorityQueryService;
import com.neyogoo.example.common.core.context.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorityQueryServiceImpl implements AuthorityQueryService {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 获取当前登录人拥有的角色编码
     */
    @Override
    public Set<String> getRoleCodes() {
        return new HashSet<>(userRoleService.listUsableRoleCodesByUserId(ContextUtils.getUserId()));
    }

    /**
     * 获取当前登录人拥有的权限编码
     */
    @Override
    public Set<String> getPermissionCodes() {
        return Collections.emptySet();
    }
}
