package com.neyogoo.example.admin.sys.service.ext;

public interface CaptcheCheckService {

    /**
     * 验证滑动验证码
     */
    void checkBlockPuzzleCaptcha(String captchaVerification);
}
