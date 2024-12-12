package com.neyogoo.example.admin.sys.api;

import com.neyogoo.example.admin.sys.fallback.UserQueryApiFallbackFactory;
import com.neyogoo.example.admin.sys.vo.request.UserListReqVO;
import com.neyogoo.example.admin.sys.vo.response.AdminUserRespVO;
import com.neyogoo.example.admin.sys.vo.response.UserDeptRespVO;
import com.neyogoo.example.admin.sys.vo.response.UserListRespVO;
import com.neyogoo.example.admin.sys.vo.response.UserSimpleRespVO;
import com.neyogoo.example.common.core.model.DataScope;
import com.neyogoo.example.common.core.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户查询
 */
@FeignClient(name = "example-admin-sys", fallbackFactory = UserQueryApiFallbackFactory.class,
        path = "/v1/user/query", qualifiers = "userQueryApi")
public interface UserQueryApi {

    /**
     * 根据id列表查询用户名称
     */
    @PostMapping("/get/idNameMap/byIds")
    R<Map<Long, String>> getIdNameMapByIds(@RequestBody @NotEmpty Collection<Long> ids);

    /**
     * 根据id列表查询用户所属部门名称
     */
    @PostMapping("/get/idDeptNameMap/byIds")
    R<Map<Long, String>> getIdDeptNameMapByIds(@RequestBody @NotEmpty Collection<Long> ids);

    /**
     * 查询当前登录人数据权限
     */
    @GetMapping("/get/dataScope/self")
    R<DataScope> getSelfDataScope();

    /**
     * 根据人员范围查询机构人员
     */
    @PostMapping("/list/scope")
    R<List<UserListRespVO>> listUserWithScope(@RequestBody @Validated UserListReqVO reqVO);

    /**
     * 查询机构管理员
     */
    @GetMapping("/list/admin")
    R<List<AdminUserRespVO>> listOrgAdminUser();

    /**
     * 根据部门id查询人员
     */
    @GetMapping("/list/byDeptId/{deptId}")
    R<List<UserListRespVO>> listByDeptId(@PathVariable("deptId") @NotNull Long deptId);

    /**
     * 根据部门id列表查询人员
     */
    @PostMapping("/map/byDeptIds")
    R<Map<Long, List<UserDeptRespVO>>> mapUserByDeptIds(@RequestBody @NotNull Collection<Long> deptIds);

    /**
     * 根据父级机构id查询人员
     */
    @GetMapping("/list/byParentOrgId/{orgId}")
    R<List<Long>> listByParentOrgId(@PathVariable("orgId") @NotNull Long orgId);

    /**
     * 根据id列表查询用户简易信息
     */
    @PostMapping("/list/simple/byIds")
    R<List<UserSimpleRespVO>> listSimpleByIds(@RequestBody @NotEmpty Collection<Long> ids);

}
