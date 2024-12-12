package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.common.authority.service.AuthorityQueryService;
import com.neyogoo.example.common.core.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/v1/authority")
@Api(value = "Authority", tags = "权限验证")
public class AuthorityController {

    @Autowired
    private AuthorityQueryService authorityQueryService;

    @ApiOperation("获取当前登录人拥有的角色编码")
    @GetMapping("/roleCodes")
    public R<Set<String>> getRoleCodes() {
        return R.success(authorityQueryService.getRoleCodes());
    }

    @ApiOperation("获取当前登录人拥有的权限编码")
    @GetMapping("/permissionCodes")
    public R<Set<String>> getPermissionCodes() {
        return R.success(authorityQueryService.getPermissionCodes());
    }
}
