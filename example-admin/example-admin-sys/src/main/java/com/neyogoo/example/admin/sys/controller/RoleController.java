package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.admin.sys.service.RoleService;
import com.neyogoo.example.admin.sys.service.ext.PermissionService;
import com.neyogoo.example.admin.sys.vo.request.permission.RoleSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.RoleUpdateReqVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RoleRespVO;
import com.neyogoo.example.admin.sys.vo.response.permission.RoleSimpleRespVO;
import com.neyogoo.example.common.boot.annotation.CheckLoginToken;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.log.annotation.SysOptLog;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@CheckSignature
@CheckLoginToken(requireUserType = UserTypeEnum.SysUser)
@RestController
@RequestMapping("/v1/role")
@Api(value = "Role", tags = "角色")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @ApiOperation("根据id查询")
    @GetMapping("/get/byId/{id}")
    public R<RoleRespVO> getById(@PathVariable("id") @NotNull Long id) {
        return R.success(RoleRespVO.fromModel(roleService.getById(id)));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public R<List<RoleRespVO>> list(@RequestParam(value = "roleName", required = false) String roleName,
                                    @RequestParam(value = "remark", required = false) String remark) {
        List<Role> roles = roleService.list();
        if (StringUtils.isNotBlank(roleName)) {
            roles = roles.stream().filter(o -> o.getRoleName().contains(roleName)).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(remark)) {
            roles = roles.stream().filter(o -> o.getRemark().contains(remark)).collect(Collectors.toList());
        }
        return R.success(RoleRespVO.fromModels(roles));
    }

    @ApiOperation("可用下拉框")
    @GetMapping("/list/usable/simple")
    public R<List<RoleSimpleRespVO>> listUsableSimple() {
        return R.success(RoleSimpleRespVO.fromModels(roleService.listUsable()));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("新增")
    @PostMapping("/save")
    public R<RoleRespVO> save(@RequestBody @Validated RoleSaveReqVO saveReqVO) {
        return R.success(RoleRespVO.fromModel(roleService.save(saveReqVO)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("修改")
    @PostMapping("/update")
    public R<RoleRespVO> update(@RequestBody @Validated RoleUpdateReqVO updateReqVO) {
        return R.success(RoleRespVO.fromModel(roleService.update(updateReqVO)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("启用禁用")
    @PostMapping("/update/usable/byId/{id}/{usable}")
    public R<Boolean> updateUsableById(@PathVariable("id") @NotNull Long id,
                                       @PathVariable("usable") @NotNull Boolean usable) {
        return R.success(roleService.updateUsableById(id, usable));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("删除")
    @DeleteMapping("/remove/byId/{id}")
    public R<Boolean> removeById(@PathVariable("id") @NotNull Long id) {
        return R.success(permissionService.removeRoleByRoleId(id));
    }
}
