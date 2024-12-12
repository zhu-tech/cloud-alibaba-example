package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.model.Menu;
import com.neyogoo.example.admin.sys.service.MenuService;
import com.neyogoo.example.admin.sys.service.ResourceService;
import com.neyogoo.example.admin.sys.vo.request.permission.ResourceSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.ResourceUpdateReqVO;
import com.neyogoo.example.admin.sys.vo.response.permission.ResourceRespVO;
import com.neyogoo.example.common.boot.annotation.CheckLoginToken;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.log.annotation.SysOptLog;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Validated
@CheckSignature
@CheckLoginToken(requireUserType = UserTypeEnum.SysUser)
@RestController
@RequestMapping("/v1/resource")
@Api(value = "Resource", tags = "资源")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private MenuService menuService;

    @ApiOperation("根据id查询")
    @GetMapping("/get/byId/{id}")
    public R<ResourceRespVO> getById(@PathVariable("id") @NotNull Long id) {
        return R.success(ResourceRespVO.fromModel(resourceService.getById(id)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("新增")
    @PostMapping("/save")
    public R<ResourceRespVO> save(@RequestBody @Validated ResourceSaveReqVO saveReqVO) {
        Menu menu = menuService.getById(saveReqVO.getMenuId());
        ArgumentAssert.notNull(menu, "菜单不存在");
        return R.success(ResourceRespVO.fromModel(resourceService.save(saveReqVO)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("修改")
    @PostMapping("/update")
    public R<ResourceRespVO> update(@RequestBody @Validated ResourceUpdateReqVO updateReqVO) {
        return R.success(ResourceRespVO.fromModel(resourceService.update(updateReqVO)));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("启用禁用")
    @PostMapping("/update/usable/byId/{id}/{usable}")
    public R<Boolean> updateUsableById(@PathVariable("id") @NotNull Long id,
                                       @PathVariable("usable") @NotNull Boolean usable) {
        return R.success(resourceService.updateUsableById(id, usable));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("删除")
    @DeleteMapping("/remove/byId/{id}")
    public R<Boolean> removeById(@PathVariable("id") @NotNull Long id) {
        return R.success(resourceService.removeById(id));
    }
}
