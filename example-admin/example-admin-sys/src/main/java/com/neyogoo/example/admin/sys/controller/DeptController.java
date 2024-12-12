package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.service.DeptService;
import com.neyogoo.example.admin.sys.service.ext.PermissionService;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptUpdateReqVO;
import com.neyogoo.example.admin.sys.vo.response.dept.DeptGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.dept.DeptRespVO;
import com.neyogoo.example.admin.sys.vo.response.dept.DeptTreeRespVO;
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
@RequestMapping("/v1/dept")
@Api(value = "Dept", tags = "部门")
public class DeptController {

    @Autowired
    private DeptService deptService;
    @Autowired
    private PermissionService permissionService;

    @ApiOperation("根据id查询")
    @GetMapping("/get/byId/{id}")
    public R<DeptGetRespVO> getById(@PathVariable("id") @NotNull Long id) {
        return R.success(permissionService.getDeptInfo(id));
    }

    @ApiOperation("列表")
    @GetMapping("/list")
    public R<List<DeptRespVO>> list() {
        return R.success(DeptRespVO.fromModels(deptService.listByOrgId(ContextUtils.getOrgId())));
    }

    @ApiOperation("树")
    @GetMapping("/tree")
    public R<List<DeptTreeRespVO>> tree() {
        return R.success(DeptTreeRespVO.fromModels(deptService.listByOrgId(ContextUtils.getOrgId())));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("新增")
    @PostMapping("/save")
    public R<Long> save(@RequestBody @Validated DeptSaveReqVO reqVO) {
        reqVO.setOrgId(ContextUtils.getOrgId());
        return R.success(permissionService.saveDeptInfo(reqVO));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("修改")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody @Validated DeptUpdateReqVO reqVO) {
        reqVO.setOrgId(ContextUtils.getOrgId());
        return R.success(permissionService.updateDeptInfo(reqVO));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("删除")
    @DeleteMapping("/remove/byId/{id}")
    public R<Boolean> removeById(@PathVariable("id") @NotNull Long id) {
        return R.success(permissionService.removeDeptByDeptId(id));
    }
}
