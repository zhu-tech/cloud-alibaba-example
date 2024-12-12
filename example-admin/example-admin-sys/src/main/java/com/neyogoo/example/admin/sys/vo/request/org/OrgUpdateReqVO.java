package com.neyogoo.example.admin.sys.vo.request.org;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.ValidatorUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构修改入参")
public class OrgUpdateReqVO {

    @ApiModelProperty("机构id")
    @NotNull(message = "机构id不能为空")
    private Long id;

    @ApiModelProperty("机构名称")
    @NotBlank(message = "机构名称不能为空")
    @Length(max = 20, message = "机构名称长度不能超过20个字符")
    private String orgName;

    @ApiModelProperty("机构类别（枚举：OrgCategory），多个逗号分隔")
    @NotBlank(message = "机构类别不能为空")
    private String orgCategory;

    @ApiModelProperty("邮政编码")
    @Pattern(regexp = "(^$)|(" + ValidatorUtils.REGEX_POST_CODE + ")", message = "邮政编码格式不正确")
    private String postCode;

    @ApiModelProperty("联系电话")
    @Length(max = 20, message = "联系电话长度不能超过20个字符")
    private String contactNumber;

    @ApiModelProperty("邮箱")
    @Pattern(regexp = "(^$)|(" + ValidatorUtils.REGEX_EMAIL + ")", message = "邮箱格式不正确")
    @Length(max = 50, message = "邮箱长度不能超过50个字符")
    private String emailAddress;

    @ApiModelProperty("传真")
    @Length(max = 20, message = "传真长度不能超过20个字符")
    private String faxNumber;

    @ApiModelProperty("省编码")
    private String provinceCode;

    @ApiModelProperty("市编码")
    private String cityCode;

    @ApiModelProperty("区编码")
    private String countyCode;

    @ApiModelProperty("具体地址")
    @Length(max = 30, message = "具体地址长度不能超过30个字符")
    private String address;

    @ApiModelProperty("机构描述")
    @Length(max = 200, message = "机构描述长度不能超过200个字符")
    private String description;

    /**
     * 转为数据库对象
     */
    public void toModel(Org model) {
        BeanUtil.copyProperties(this, model);
        model.setUpdateTime(LocalDateTime.now());
        model.setUpdateUserId(ContextUtils.getUserId());
    }
}
