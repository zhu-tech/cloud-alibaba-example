package com.neyogoo.example.admin.sys.controller;

import com.neyogoo.example.admin.sys.service.ext.TokenService;
import com.neyogoo.example.admin.sys.vo.request.token.H5SmsLoginReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.H5SmsSendReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.LogoutReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.RefreshTokenReqVO;
import com.neyogoo.example.admin.sys.vo.request.token.WebPwdLoginReqVO;
import com.neyogoo.example.admin.sys.vo.response.token.AuthInfoRespVO;
import com.neyogoo.example.admin.sys.vo.response.token.CstUserAuthInfoRespVO;
import com.neyogoo.example.admin.sys.vo.response.token.SysUserAuthInfoRespVO;
import com.neyogoo.example.common.boot.annotation.CheckSignature;
import com.neyogoo.example.common.boot.annotation.RepeatSubmit;
import com.neyogoo.example.common.core.model.R;
import com.neyogoo.example.common.log.annotation.SysOptLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CheckSignature
@RestController
@RequestMapping("/v1/token")
@Api(value = "Token", tags = "令牌")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("登录 - 管理端PC - 密码登录")
    @PostMapping("/anno/web/pwd/login")
    public R<SysUserAuthInfoRespVO> webPwdLogin(@RequestBody @Validated WebPwdLoginReqVO reqVO,
                                                HttpServletRequest request) {
        return R.success((SysUserAuthInfoRespVO) tokenService.login(reqVO, request));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("登录 - 用户端H5 - 验证码登录")
    @PostMapping("/anno/h5/sms/login")
    public R<CstUserAuthInfoRespVO> h5SmsLogin(@RequestBody @Validated H5SmsLoginReqVO reqVO,
                                                HttpServletRequest request) {
        return R.success((CstUserAuthInfoRespVO) tokenService.login(reqVO, request));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("登录 - 用户端H5 - 验证码发送")
    @PostMapping("/anno/h5/sms/send")
    public R<Boolean> h5SmsSend(@RequestBody @Validated H5SmsSendReqVO reqVO) {
        return R.success(tokenService.sendSms(reqVO));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("刷新 Token")
    @PostMapping("/anno/refresh")
    public R<AuthInfoRespVO> refresh(@RequestBody @Validated RefreshTokenReqVO reqVO,
                                     HttpServletRequest request) {
        return R.success(tokenService.refresh(reqVO, request));
    }

    @SysOptLog
    @RepeatSubmit
    @ApiOperation("登出")
    @PostMapping("/anno/logout")
    public R<Boolean> logout(@RequestBody @Validated LogoutReqVO reqVO) {
        return R.success(tokenService.logout(reqVO));
    }
}
