package com.neyogoo.example.admin.sys.constant;

import com.neyogoo.example.admin.common.constant.AdminCacheConstant;

public interface SysCacheConstant {

    /**
     * admin:sys:user_pwd_error:{userType}:{userId} -> 错误次数
     */
    String USER_PWD_ERROR = AdminCacheConstant.SYS_MODULAR_PREFIX + "user_pwd_error:";

    /**
     * admin:sys:block_puzzle_captcha:{验证key}
     */
    String BLOCK_PUZZLE_CAPTCHA = AdminCacheConstant.SYS_MODULAR_PREFIX + "block_puzzle_captcha:";

    /**
     * admin:sys:sms:user_login:{用户类型}:{用户手机号}
     */
    String SMS_CODE_USER_LOGIN = AdminCacheConstant.SYS_MODULAR_PREFIX + "sms:user_login:";
}
