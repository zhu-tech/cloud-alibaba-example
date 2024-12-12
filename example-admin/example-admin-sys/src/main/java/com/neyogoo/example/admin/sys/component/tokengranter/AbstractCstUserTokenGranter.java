package com.neyogoo.example.admin.sys.component.tokengranter;

import com.neyogoo.example.admin.sys.vo.request.token.RefreshTokenReqVO;
import com.neyogoo.example.admin.sys.vo.response.token.CstUserAuthInfoRespVO;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import com.neyogoo.example.common.token.model.RefreshTokenCache;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 家长人员 token 分发器
 */
@Slf4j
public abstract class AbstractCstUserTokenGranter extends AbstractTokenGranter<CstUserAuthInfoRespVO> {

    /**
     * 刷新
     */
    @Override
    public CstUserAuthInfoRespVO refresh(RefreshTokenReqVO reqVO, HttpServletRequest request) {
        RefreshTokenCache cache = reqVO.getCache();

        String userMobile = reqVO.getCache().getUser().getUserId().toString();

        // 生成验证信息
        CstUserAuthInfoRespVO authInfo = generalAuthInfo(userMobile);
        authInfo.getLogin()
                .setLoginPoint(cache.getLogin().getLoginPoint())
                .setLoginType(cache.getLogin().getLoginType())
                .setLoginAccount(cache.getLogin().getLoginAccount());

        // 缓存新 Token 信息
        cacheToken(authInfo);

        // 移除旧刷新 Token
        removeRefreshToken(reqVO.getRefreshToken());

        // 更新登录记录
        updateUserLogin(authInfo, request);

        return authInfo;
    }

    protected CstUserAuthInfoRespVO generalAuthInfo(String userMobile) {
        CstUserAuthInfoRespVO authInfo = new CstUserAuthInfoRespVO();
        // 登录信息
        authInfo.getLogin()
                .setLoginTime(LocalDateTime.now());
        // 用户信息
        authInfo.getUser()
                .setUserType(UserTypeEnum.CstUser)
                .setUserId(Long.valueOf(userMobile))
                .setUserAccount(userMobile)
                .setUserName(userMobile);
        // 租户信息
        authInfo.getOrg()
                .setOrgId(BasicConstant.DEFAULT_NON_ID)
                .setOrgName(StrPool.EMPTY);
        // Token 数据
        generalToken(authInfo);

        return authInfo;
    }
}
