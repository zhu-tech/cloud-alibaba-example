package com.neyogoo.example.admin.sys.controller;

import cn.hutool.core.bean.BeanUtil;
import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.neyogoo.example.admin.sys.vo.request.token.CaptchaCheckReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.CaptchaGetReqVO;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.core.util.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/v1/captcha")
@Api(value = "Captcha", tags = "验证码")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @ApiOperation("获取验证码图片")
    @PostMapping("/anno/get")
    public R<ResponseModel> get(@RequestBody @Validated CaptchaGetReqVO reqVO, HttpServletRequest request) {
        assert request.getRemoteHost() != null;
        CaptchaVO data = BeanUtil.toBean(reqVO, CaptchaVO.class);
        data.setBrowserInfo(WebUtils.getIpAddr(request));
        return R.success(captchaService.get(data));
    }

    @ApiOperation("检查验证码图片")
    @PostMapping("/anno/check")
    public R<ResponseModel> check(@RequestBody @Validated CaptchaCheckReqVO reqVO, HttpServletRequest request) {
        CaptchaVO data = BeanUtil.toBean(reqVO, CaptchaVO.class);
        data.setBrowserInfo(WebUtils.getIpAddr(request));
        try {
            return R.success(captchaService.check(data));
        } catch (Exception e) {
            log.error("check captcha error", e);
            return R.success(new ResponseModel(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR));
        }
    }
}
