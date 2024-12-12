package com.neyogoo.example.admin.sys.vo.response.token;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.token.model.LoginTokenCache;
import com.neyogoo.example.common.token.model.RefreshTokenCache;
import com.neyogoo.example.common.token.model.UserTokenCache;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@ApiModel(description = "认证信息")
public class AuthInfoRespVO extends LoginTokenCache {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("登录 Token 信息")
    protected Token token = new Token();

    @ApiModelProperty("刷新 Token 信息")
    protected Token refresh = new Token();

    public LoginTokenCache toLoginTokenCache() {
        return BeanUtil.toBean(this, LoginTokenCache.class);
    }

    public RefreshTokenCache toRefreshTokeCache() {
        RefreshTokenCache cache = new RefreshTokenCache();
        BeanUtil.copyProperties(this.user, cache.getUser());
        BeanUtil.copyProperties(this.org, cache.getOrg());
        BeanUtil.copyProperties(this.login, cache.getLogin());
        return cache;
    }

    public UserTokenCache toUserTokeCache() {
        UserTokenCache cache = new UserTokenCache();
        cache.setLoginPoint(this.login.getLoginPoint());
        cache.setToken(this.token.value);
        cache.setCreateTime(LocalDateTime.now());
        cache.setExpireTime(this.token.expire);
        return cache;
    }

    public void putToThreadLocal() {
        ContextUtils.setUserType(this.user.getUserType().getCode());
        ContextUtils.setUserAccount(this.user.getUserAccount());
        ContextUtils.setUserId(this.user.getUserId());
        ContextUtils.setUserName(this.user.getUserName());
        ContextUtils.setOrgId(this.org.getOrgId());
        ContextUtils.setLoginPoint(this.login.getLoginPoint().getCode());
        ContextUtils.setToken(this.token.value);
    }

    @Data
    public static class Token {

        @ApiModelProperty("token值")
        protected String value;

        @ApiModelProperty(value = "到期时间", hidden = true)
        @JsonIgnore
        protected LocalDateTime expire;
    }
}
