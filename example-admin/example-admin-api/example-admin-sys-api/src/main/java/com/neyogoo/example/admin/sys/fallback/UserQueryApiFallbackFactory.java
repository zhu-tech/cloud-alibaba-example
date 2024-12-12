package com.neyogoo.example.admin.sys.fallback;

import com.neyogoo.example.admin.sys.api.UserQueryApi;
import com.neyogoo.example.admin.sys.vo.request.UserListReqVO;
import com.neyogoo.example.admin.sys.vo.response.AdminUserRespVO;
import com.neyogoo.example.admin.sys.vo.response.UserDeptRespVO;
import com.neyogoo.example.admin.sys.vo.response.UserListRespVO;
import com.neyogoo.example.admin.sys.vo.response.UserSimpleRespVO;
import com.neyogoo.example.common.core.model.DataScope;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户查询
 */
@Slf4j
@Component
public class UserQueryApiFallbackFactory implements FallbackFactory<UserQueryApi> {

    @Override
    public UserQueryApi create(Throwable cause) {

        return new UserQueryApi() {

            @Override
            public R<Map<Long, String>> getIdNameMapByIds(Collection<Long> ids) {
                log.error("get user id name map by ids error, ids = {}, cause = {}", ids, cause.getMessage());
                return R.fail("根据用户id列表查询用户名称失败");
            }

            @Override
            public R<Map<Long, String>> getIdDeptNameMapByIds(Collection<Long> ids) {
                log.error("get user id dept name map by ids error, ids = {}, cause = {}", ids, cause.getMessage());
                return R.fail("根据用户id列表查询所属部门名称失败");
            }

            @Override
            public R<DataScope> getSelfDataScope() {
                log.error("get self data scope, cause = {}", cause.getMessage());
                return R.fail("查询当前登录人数据权限失败");
            }

            @Override
            public R<List<UserListRespVO>> listUserWithScope(UserListReqVO reqVO) {
                log.error("list user with scope error, reqVO={}, cause = {}", reqVO, cause.getMessage());
                return R.fail("根据人员范围查询机构人员失败");
            }

            @Override
            public R<List<AdminUserRespVO>> listOrgAdminUser() {
                log.error("list admin user error, cause = {}", cause.getMessage());
                return R.fail("查询平台管理员失败");
            }

            @Override
            public R<List<UserListRespVO>> listByDeptId(Long deptId) {
                log.error("list user by deptId, deptId ={}, cause = {}", deptId, cause.getMessage());
                return R.fail("根据部门id查询人员失败");
            }

            @Override
            public R<Map<Long, List<UserDeptRespVO>>> mapUserByDeptIds(Collection<Long> deptIds) {
                log.error("map user by deptIds, deptIds ={}, cause = {}", deptIds, cause.getMessage());
                return R.fail("根据部门id列表查询人员失败");
            }

            @Override
            public R<List<Long>> listByParentOrgId(Long orgId) {
                log.error("list user by parentOrgId, orgId ={}, cause = {}", orgId, cause.getMessage());
                return R.fail("根据父级机构id查询人员失败");
            }

            @Override
            public R<List<UserSimpleRespVO>> listSimpleByIds(Collection<Long> ids) {
                log.error("list simple by ids, ids ={}, cause = {}", ids, cause.getMessage());
                return R.fail("根据id列表查询用户简易信息失败");
            }
        };
    }
}
