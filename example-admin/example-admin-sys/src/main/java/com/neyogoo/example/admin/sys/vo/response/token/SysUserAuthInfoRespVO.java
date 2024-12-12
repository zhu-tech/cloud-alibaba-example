package com.neyogoo.example.admin.sys.vo.response.token;

import com.neyogoo.example.admin.sys.vo.response.permission.RoleMenuRespVO;
import com.neyogoo.example.biz.common.enumeration.sys.OrgLevelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@ApiModel(description = "认证信息")
public class SysUserAuthInfoRespVO extends AuthInfoRespVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单列表")
    private List<RoleMenuRespVO> menus = new ArrayList<>();

    @ApiModelProperty("资源列表")
    private List<RoleMenuRespVO.ResourceData> resources = new ArrayList<>();

    @ApiModelProperty("用户扩展信息")
    private SysUserExt sysUserExt = new SysUserExt();

    public void putPermission(List<RoleMenuRespVO> permissions) {
        this.menus = permissions;
        for (RoleMenuRespVO menu : this.menus) {
            this.resources.addAll(menu.collectResources());
        }
        this.menus.forEach(RoleMenuRespVO::clearResources);
    }

    @Data
    public static class Menu {

        @ApiModelProperty("菜单id")
        private Long menuId;

        @ApiModelProperty("菜单编码")
        private String menuCode;

        @ApiModelProperty("菜单名称")
        private String menuName;

        @ApiModelProperty("菜单图标")
        private String menuIcon;

        @ApiModelProperty("菜单路径")
        private String menuHref;

        @ApiModelProperty("是否展示")
        private Boolean showFlag;
    }

    @Data
    public static class Resource {

        @ApiModelProperty("资源id")
        private Long resourceId;

        @ApiModelProperty("资源编码")
        private String resourceCode;

        @ApiModelProperty("资源名称")
        private String resourceName;
    }

    @Data
    public static class SysUserExt {

        @ApiModelProperty("机构等级")
        private OrgLevelEnum orgLevel;

        @ApiModelProperty("角色名称")
        private List<String> roleNames;

        @ApiModelProperty("创建时间")
        private LocalDateTime createTime;
    }
}
