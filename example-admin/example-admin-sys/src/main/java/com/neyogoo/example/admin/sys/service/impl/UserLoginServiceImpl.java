package com.neyogoo.example.admin.sys.service.impl;

import com.neyogoo.example.admin.sys.dao.UserLoginMapper;
import com.neyogoo.example.admin.sys.model.UserLogin;
import com.neyogoo.example.admin.sys.service.UserLoginService;
import com.neyogoo.example.admin.sys.util.IpRegionUtils;
import com.neyogoo.example.admin.sys.vo.response.token.AuthInfoRespVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户登录相关记录汇总
 */
@Slf4j
@Service
public class UserLoginServiceImpl extends SuperServiceImpl<UserLoginMapper, UserLogin> implements UserLoginService {

    /**
     * 根据用户类型和id查询登录记录
     */
    @Override
    public UserLogin getByUserTypeAndUserId(UserTypeEnum userType, Long userId) {
        if (userType == null || userId == null) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<UserLogin>lbQ()
                        .eq(UserLogin::getUserId, userId)
                        .eq(UserLogin::getUserType, userType.getCode())
                        .last("limit 1")
        );
    }

    /**
     * 增加新登录记录
     */
    @Override
    public boolean saveNewRecordIfNotExists(UserTypeEnum userType, Long userId) {
        boolean exists = baseMapper.selectOne(
                Wraps.<UserLogin>lbQ()
                        .select(UserLogin::getId)
                        .eq(UserLogin::getUserId, userId)
                        .eq(UserLogin::getUserType, userType.getCode())
                        .last("limit 1")
        ) != null;
        if (exists) {
            return true;
        }
        baseMapper.insert(new UserLogin(userType, userId));
        return true;
    }

    /**
     * 根据登录验证信息更新登录记录
     */
    @Override
    public boolean updateByAuthInfo(AuthInfoRespVO authInfo, String loginIp) {
        UserLogin userLogin = getByUserTypeAndUserId(
                authInfo.getUser().getUserType(), authInfo.getUser().getUserId()
        );
        if (userLogin == null) {
            userLogin = new UserLogin(authInfo.getUser().getUserType(), authInfo.getUser().getUserId());
            baseMapper.insert(userLogin);
        }
        userLogin.setLatestLoginTime(LocalDateTime.now())
                .setLatestLoginIp(loginIp)
                .setLatestLoginLocation(IpRegionUtils.getRegion(loginIp))
                .setLatestLoginPoint(authInfo.getLogin().getLoginPoint())
                .setUpdateTime(LocalDateTime.now())
                .setUpdateUserId(authInfo.getUser().getUserId());
        return baseMapper.updateById(userLogin) > 0;
    }

    /**
     * 更新最近更新密码时间
     */
    @Override
    public boolean updateLatestChangePwdTime(UserTypeEnum userType, Long userId) {
        if (userType == null || userId == null) {
            return false;
        }
        saveNewRecordIfNotExists(userType, userId);
        return update(
                Wraps.<UserLogin>lbU()
                        .set(UserLogin::getLatestChangePwdTime, LocalDateTime.now())
                        .set(UserLogin::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserLogin::getUpdateTime, LocalDateTime.now())
                        .eq(UserLogin::getUserId, userId)
                        .eq(UserLogin::getUserType, userType)
        );
    }
}
