package com.neyogoo.example.admin.sys.fallback;

import com.neyogoo.example.admin.sys.api.AuthorityApi;
import com.neyogoo.example.common.core.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 权限校验
 */
@Slf4j
@Component
public class AuthorityApiFallbackFactory implements FallbackFactory<AuthorityApi> {

    @Override
    public AuthorityApi create(Throwable cause) {

        return new AuthorityApi() {

            @Override
            public R<Set<String>> getRoleCodes() {
                log.error("get role codes for authority error, cause = {}", cause.getMessage());
                return R.fail("获取当前登录人拥有的角色编码失败");
            }

            @Override
            public R<Set<String>> getPermissionCodes() {
                log.error("get permission codes for authority error, cause = {}", cause.getMessage());
                return R.fail("获取当前登录人拥有的权限编码失败");
            }
        };
    }
}
