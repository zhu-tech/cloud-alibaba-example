package com.neyogoo.example.admin.sys.vo.response;

import com.neyogoo.example.biz.common.enumeration.sys.OrgLevelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构树返参")
public class OrgTreeRespVO {

    @ApiModelProperty("机构id")
    private Long id;

    @ApiModelProperty("机构编码")
    private String orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("机构等级（枚举：OrgLevel）")
    private OrgLevelEnum orgLevel;

    @ApiModelProperty("机构类别（枚举：OrgCategory）")
    private String orgCategory;

    @ApiModelProperty("电话")
    private String contactNumber;

    @ApiModelProperty("地址")
    private String fullAddress;

    @ApiModelProperty("地址（-分隔）")
    private String dashAddress;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    @ApiModelProperty("下级机构")
    private List<OrgTreeRespVO> children;
}
