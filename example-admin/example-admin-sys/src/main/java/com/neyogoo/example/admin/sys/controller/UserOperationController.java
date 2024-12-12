package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.service.ext.UserOperationService;
import com.neyogoo.example.admin.sys.vo.request.user.UserSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserUpdatePwdReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserUpdateReqVO;
import com.neyogoo.example.common.boot.annotation.CheckLoginToken;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.log.annotation.SysOptLog;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/v1/user/operation")
@Api(value = "UserOperation", tags = "用户 - 操作")
public class UserOperationController {

    @Autowired
    private UserOperationService userOperationService;

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("新增")
    @PostMapping("/save")
    public R<Long> save(@RequestBody @Validated UserSaveReqVO reqVO) {
        reqVO.setOrgId(ContextUtils.getOrgId());
        return R.success(userOperationService.save(reqVO));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("修改")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody @Validated UserUpdateReqVO reqVO) {
        reqVO.setOrgId(ContextUtils.getOrgId());
        return R.success(userOperationService.update(reqVO));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("启用禁用")
    @PostMapping("/update/usable/byId/{id}/{usable}")
    public R<Boolean> updateUsableById(@PathVariable("id") @NotNull Long id,
                                       @PathVariable("usable") @NotNull Boolean usable) {
        return R.success(userOperationService.updateUsableById(id, usable));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("更新密码")
    @PostMapping("/update/pwd/self")
    public R<Boolean> updateSelfPwd(@RequestBody @Validated UserUpdatePwdReqVO reqVO) {
        return R.success(userOperationService.updateLoginPwdById(ContextUtils.getUserId(), reqVO));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("重置密码")
    @PostMapping("/reset/pwd/byId/{id}")
    public R<Boolean> resetPwdById(@PathVariable("id") @NotNull Long id) {
        return R.success(userOperationService.resetLoginPwdById(id));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("删除")
    @DeleteMapping("/remove/byId/{id}")
    public R<Boolean> removeById(@PathVariable("id") @NotNull Long id) {
        return R.success(userOperationService.removeById(id));
    }
}
