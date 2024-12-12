package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.service.ext.PermissionService;
import com.neyogoo.example.admin.sys.vo.request.permission.RolePermissionIdReqVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RoleMenuRespVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RolePermissionIdRespVO;
import com.neyogoo.example.common.boot.annotation.CheckLoginToken;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.log.annotation.SysOptLog;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@CheckSignature
@CheckLoginToken(requireUserType = UserTypeEnum.SysUser)
@RestController
@RequestMapping("/v1/permission")
@Api(value = "Permission", tags = "权限")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation("查询菜单资源列表")
    @GetMapping("/list")
    public R<List<RoleMenuRespVO>> list() {
        return R.success(permissionService.listPermission());
    }

    @ApiOperation("查询角色可选权限列表")
    @GetMapping("/list/usable")
    public R<List<RoleMenuRespVO>> listUsable() {
        return R.success(permissionService.listUsablePermission());
    }

    @ApiOperation("根据角色id查询关联的权限id列表")
    @GetMapping("/list/ids/byRoleId/{roleId}")
    public R<RolePermissionIdRespVO> listByIdsByRoleId(@PathVariable("roleId") @NotNull Long roleId) {
        return R.success(permissionService.listPermissionIdsByRoleId(roleId));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("保存角色id关联的权限id")
    @PostMapping("/save/rolePermission")
    public R<Boolean> saveRolePermission(@RequestBody @Validated RolePermissionIdReqVO reqVO) {
        return R.success(permissionService.saveRolePermission(reqVO));
    }
}
