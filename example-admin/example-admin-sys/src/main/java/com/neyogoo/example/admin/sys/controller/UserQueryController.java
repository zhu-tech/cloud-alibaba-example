package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.service.UserDeptService;
import com.neyogoo.example.admin.sys.service.UserOrgService;
import com.neyogoo.example.admin.sys.service.UserService;
import com.neyogoo.example.admin.sys.service.ext.UserQueryService;
import com.neyogoo.example.admin.sys.vo.request.user.UserListReqVO;
import com.neyogoo.example.admin.sys.vo.request.user.UserPageReqVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserDeptRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserListRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserPageRespVO;
import com.neyogoo.example.admin.sys.vo.response.user.UserSimpleRespVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.DataScope;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.model.PageResp;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.core.model.R;
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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user/query")
@Api(value = "UserQuery", tags = "用户 - 查询")
public class UserQueryController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDeptService userDeptService;
    @Autowired
    private UserQueryService userQueryService;
    @Autowired
    private UserOrgService userOrgService;

    @ApiOperation("根据id查询")
    @GetMapping("/get/byId/{id}")
    public R<UserGetRespVO> getById(@PathVariable("id") @NotNull Long id) {
        return R.success(userQueryService.getById(id));
    }

    @ApiOperation("根据id列表查询用户名称")
    @PostMapping("/get/idNameMap/byIds")
    public R<Map<Long, String>> getIdNameMapByIds(@RequestBody @NotEmpty Collection<Long> ids) {
        return R.success(userService.mapNamesByIds(ids));
    }

    @ApiOperation("根据id列表查询用户所属部门名称")
    @PostMapping("/get/idDeptNameMap/byIds")
    public R<Map<Long, String>> getIdDeptNameMapByIds(@RequestBody @NotEmpty Collection<Long> ids) {
        return R.success(userService.mapDeptNamesByIds(ids));
    }

    @ApiOperation("根据id列表查询用户简易信息")
    @PostMapping("/list/simple/byIds")
    public R<List<UserSimpleRespVO>> listSimpleByIds(@RequestBody @NotEmpty Collection<Long> ids) {
        return R.success(UserSimpleRespVO.fromModels(userService.listSimpleInfoByIds(ids)));
    }

    @ApiOperation("查询当前登录人数据权限")
    @GetMapping("/get/dataScope/self")
    public R<DataScope> getSelfDataScope() {
        return R.success(userQueryService.getDataScope());
    }

    @ApiOperation("自动生成用户工号")
    @GetMapping("/generate/userCode")
    public R<String> generateUserCode() {
        return R.success(userQueryService.generateUserCode());
    }

    @ApiOperation("根据机构id查询下拉框")
    @GetMapping("/comboBox/byOrgId/{orgId}")
    public R<List<Pair<Long, String>>> getComboBoxByOrgId(@PathVariable("orgId") @NotNull Long orgId) {
        return R.success(userQueryService.getComboBoxByOrgId(orgId));
    }

    @ApiOperation("分页")
    @PostMapping("/page")
    public R<PageResp<UserPageRespVO>> findPage(@RequestBody @Validated PageReq<UserPageReqVO> pageReq) {
        return R.success(userQueryService.findPage(pageReq));
    }

    @ApiOperation("查询当前机构管理员")
    @GetMapping("/list/orgAdmins")
    public R<List<UserSimpleRespVO>> listOrgAdminUser() {
        return R.success(UserSimpleRespVO.fromModels(userQueryService.listOrgAdminsByOrgId(ContextUtils.getOrgId())));
    }

    @ApiOperation("根据部门id查询人员")
    @GetMapping("/list/simple/byDeptId/{deptId}")
    public R<List<UserSimpleRespVO>> listSimpleByDeptId(@PathVariable("deptId") @NotNull Long deptId) {
        return R.success(UserSimpleRespVO.fromModels(userQueryService.listByDeptId(deptId)));
    }

    @ApiOperation("根据父级机构id查询人员（feign）")
    @GetMapping("/list/byParentOrgId/{orgId}")
    public R<List<Long>> listByParentOrgId(@PathVariable("orgId") @NotNull Long orgId) {
        return R.success(userOrgService.listUsableUserIdsByParentOrgId(orgId));
    }

    @ApiOperation("根据部门id列表查询人员")
    @PostMapping("/map/byDeptIds")
    public R<Map<Long, List<UserDeptRespVO>>> mapUserByDeptIds(@RequestBody @NotNull Collection<Long> deptIds) {
        return R.success(userDeptService.mapByDeptIds(deptIds));
    }

    @ApiOperation("根据人员范围查询机构人员")
    @PostMapping("/list/scope")
    public R<List<UserListRespVO>> listUserWithScope(@RequestBody @Validated UserListReqVO reqVO) {
        return R.success(userService.listUserWithScope(reqVO));
    }

    @ApiOperation("根据部门id查询人员")
    @GetMapping("/list/byDeptId/{deptId}")
    public R<List<UserListRespVO>> listByDeptId(@PathVariable("deptId") @NotNull Long deptId) {
        return R.success(UserListRespVO.sortByRole(userService.listByDeptId(deptId)));
    }

    @ApiOperation("查询是否需要修改密码")
    @GetMapping("/isNeedChangePwd")
    public R<Boolean> queryIsNeedChangePwd() {
        return R.success(userService.queryIsNeedChangePwd(ContextUtils.getUserId()));
    }
}
