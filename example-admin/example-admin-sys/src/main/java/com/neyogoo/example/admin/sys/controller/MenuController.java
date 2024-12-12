package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.service.MenuService;
import com.neyogoo.example.admin.sys.service.ext.PermissionService;
import com.neyogoo.example.admin.sys.vo.request.permission.MenuSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.MenuUpdateReqVO;
import com.neyogoo.example.admin.sys.vo.response.permission.MenuRespVO;
import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.log.annotation.SysOptLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/v1/menu")
@Api(value = "Menu", tags = "菜单")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private PermissionService permissionService;

    @ApiOperation("根据id查询")
    @GetMapping("/get/byId/{id}")
    public R<MenuRespVO> getById(@PathVariable("id") @NotNull Long id) {
        return R.success(MenuRespVO.fromModel(menuService.getById(id)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("新增")
    @PostMapping("/save")
    public R<MenuRespVO> save(@RequestBody @Validated MenuSaveReqVO saveReqVO) {
        return R.success(MenuRespVO.fromModel(menuService.save(saveReqVO)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("修改")
    @PostMapping("/update")
    public R<MenuRespVO> update(@RequestBody @Validated MenuUpdateReqVO updateReqVO) {
        return R.success(MenuRespVO.fromModel(menuService.update(updateReqVO)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("启用禁用")
    @PostMapping("/update/usable/byId/{id}/{usable}")
    public R<Boolean> updateUsableById(@PathVariable("id") @NotNull Long id,
                                       @PathVariable("usable") @NotNull Boolean usable) {
        return R.success(menuService.updateUsableById(id, usable));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("删除")
    @DeleteMapping("/remove/byId/{id}")
    public R<Boolean> removeById(@PathVariable("id") @NotNull Long id) {
        return R.success(permissionService.removeMenuByMenuId(id));
    }
}
