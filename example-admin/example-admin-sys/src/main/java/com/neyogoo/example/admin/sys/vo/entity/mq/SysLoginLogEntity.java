package com.neyogoo.example.admin.sys.vo.entity.mq;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.neyogoo.example.admin.sys.util.IpRegionUtils;
import com.neyogoo.example.admin.sys.vo.response.token.AuthInfoRespVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.WebUtils;
import com.neyogoo.example.common.token.enumeration.LoginPointEnum;
import com.neyogoo.example.common.token.enumeration.LoginTypeEnum;
import com.neyogoo.example.common.token.enumeration.UserTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SysLoginLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 机构id
     */
    private Long orgId;
    /**
     * 登录点
     */
    private LoginPointEnum loginPoint;
    /**
     * 登录方式
     */
    private LoginTypeEnum loginType;
    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
    /**
     * 是否登录成功
     */
    private Boolean successFlag;
    /**
     * 登录描述
     */
    private String description;
    /**
     * 登录IP
     */
    private String requestIp;
    /**
     * 登录地点
     */
    private String location;
    /**
     * 浏览器请求头
     */
    private String ua;
    /**
     * 浏览器名称
     */
    private String browserName;
    /**
     * 浏览器版本
     */
    private String browserVersion;
    /**
     * 操作系统
     */
    private String operatingSystem;
    /**
     * 链路追踪id
     */
    private String traceId;

    public static SysLoginLogEntity success(AuthInfoRespVO authInfo, HttpServletRequest request) {
        SysLoginLogEntity entity = new SysLoginLogEntity();
        BeanUtil.copyProperties(authInfo.getUser(), entity);
        BeanUtil.copyProperties(authInfo.getOrg(), entity);
        BeanUtil.copyProperties(authInfo.getLogin(), entity);
        entity.successFlag = true;
        entity.ua = StrUtil.sub(request.getHeader("user-agent"), 0, 500);
        UserAgent userAgent = UserAgentUtil.parse(entity.ua);
        if (userAgent != null) {
            entity.setBrowserName(userAgent.getBrowser() == null ? null : userAgent.getBrowser().getName());
            entity.setBrowserVersion(userAgent.getVersion());
            entity.setOperatingSystem(userAgent.getOs() == null ? null : userAgent.getOs().getName());
        }
        entity.setDescription("登录成功");
        entity.setRequestIp(WebUtils.getIpAddr(request));
        entity.setLocation(IpRegionUtils.getRegion(entity.getRequestIp()));
        entity.setTraceId(ContextUtils.getXTraceId());
        return entity;
    }
}
