package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.UserLogin;
import com.neyogoo.example.admin.sys.vo.response.token.AuthInfoRespVO;
import com.neyogoo.example.common.database.mvc.service.SuperService;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;

/**
 * 用户登录相关记录汇总
 */
public interface UserLoginService extends SuperService<UserLogin> {

    /**
     * 根据用户类型和id查询登录记录
     */
    UserLogin getByUserTypeAndUserId(UserTypeEnum userType, Long userId);

    /**
     * 增加新登录记录
     */
    boolean saveNewRecordIfNotExists(UserTypeEnum userType, Long userId);

    /**
     * 根据登录验证信息更新登录记录
     */
    boolean updateByAuthInfo(AuthInfoRespVO authInfo, String loginIp);

    /**
     * 更新最近更新密码时间
     */
    boolean updateLatestChangePwdTime(UserTypeEnum userType, Long userId);
}
