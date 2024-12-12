package com.neyogoo.example.admin.sys.service.impl.ext;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.neyogoo.example.admin.sys.config.properties.LoginProperties;
import com.neyogoo.example.admin.sys.service.ext.CaptcheCheckService;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaptchaCheckServiceImpl implements CaptcheCheckService {

    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private CaptchaService captchaService;

    /**
     * 验证滑动验证码
     */
    @Override
    public void checkBlockPuzzleCaptcha(String captchaVerification) {
        if (!loginProperties.getVerifyCaptcha()) {
            return;
        }
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel response = captchaService.verification(captchaVO);
        ArgumentAssert.isTrue(response.isSuccess(), response.getRepMsg());
    }
}
