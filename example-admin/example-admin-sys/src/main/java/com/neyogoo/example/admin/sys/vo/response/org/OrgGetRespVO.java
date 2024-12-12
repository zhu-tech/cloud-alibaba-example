package com.neyogoo.example.admin.sys.vo.response.org;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.biz.common.enumeration.sys.OrgLevelEnum;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.model.Pair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构信息")
public class OrgGetRespVO {

    @ApiModelProperty("机构id")
    private Long id;

    @ApiModelProperty("机构编码")
    private String orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("机构类别（枚举：OrgCategory，多个间英文逗号分隔）")
    private String orgCategory;

    @ApiModelProperty("机构等级（枚举：OrgLevel）")
    private OrgLevelEnum orgLevel;

    @ApiModelProperty("上级机构（key：上级id，value：上级名称），没有上级时为 null")
    private Pair<Long, String> parent;

    @ApiModelProperty("邮政编码")
    private String postCode;

    @ApiModelProperty("联系电话")
    private String contactNumber;

    @ApiModelProperty("邮箱")
    private String emailAddress;

    @ApiModelProperty("传真")
    private String faxNumber;

    @ApiModelProperty("省编码")
    private String provinceCode;

    @ApiModelProperty("市编码")
    private String cityCode;

    @ApiModelProperty("区编码")
    private String countyCode;

    @ApiModelProperty("地区名称")
    private String areaName;

    @ApiModelProperty("具体地址")
    private String address;

    @ApiModelProperty("机构描述")
    private String description;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    @ApiModelProperty("管理员信息")
    private AdminUser adminUser;

    /**
     * 从数据库对象转换
     */
    public static OrgGetRespVO fromModel(Org model) {
        if (model == null) {
            return null;
        }
        OrgGetRespVO respVO = BeanUtil.toBean(model, OrgGetRespVO.class);
        if (!BasicConstant.DEFAULT_ID.equals(model.getParentId())) {
            respVO.setParent(Pair.of(model.getParentId(), null));
        }
        return respVO;
    }

    @Data
    @AllArgsConstructor
    public static class AdminUser {

        @ApiModelProperty("管理员名称")
        private String userName;

        @ApiModelProperty("管理员手机号")
        private String userMobile;
    }
}
