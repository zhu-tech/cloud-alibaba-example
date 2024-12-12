package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.service.ext.OrgOperationService;
import com.neyogoo.example.admin.sys.vo.request.org.OrgSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.org.OrgUpdateReqVO;
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
@RequestMapping("/v1/org/operation")
@Api(value = "OrgOperation", tags = "机构 - 操作")
public class OrgOperationController {

    @Autowired
    private OrgOperationService orgOperationService;

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("新增")
    @PostMapping("/save")
    public R<Long> save(@RequestBody @Validated OrgSaveReqVO reqVO) {
        return R.success(orgOperationService.save(reqVO));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("修改")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody @Validated OrgUpdateReqVO reqVO) {
        return R.success(orgOperationService.update(reqVO));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("启用禁用")
    @PostMapping("/update/usable/byId/{id}/{usable}")
    public R<Boolean> updateUsableById(@PathVariable("id") @NotNull Long id,
                                       @PathVariable("usable") @NotNull Boolean usable) {
        return R.success(orgOperationService.updateUsableById(id, usable));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("删除")
    @DeleteMapping("/remove/byId/{id}")
    public R<Boolean> removeById(@PathVariable("id") @NotNull Long id) {
        return R.success(orgOperationService.removeById(id));
    }
}
