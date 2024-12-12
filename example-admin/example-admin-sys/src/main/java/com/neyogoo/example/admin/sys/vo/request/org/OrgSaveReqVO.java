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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构新增入参")
public class OrgSaveReqVO {

    @ApiModelProperty("机构编码")
    @NotBlank(message = "机构编码不能为空")
    @Pattern(regexp = "^[0-9]{7}$", message = "机构编码不能为空")
    private String orgCode;

    @ApiModelProperty("机构名称")
    @NotBlank(message = "机构名称不能为空")
    @Length(max = 20, message = "机构名称长度不能超过20个字符")
    private String orgName;

    @ApiModelProperty("机构类别（枚举：OrgCategory）")
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

    @ApiModelProperty("管理员信息")
    @NotNull(message = "管理员信息不能为空")
    @Valid
    private AdminUser adminUser;


    /**
     * 转为数据库对象
     */
    public Org toModel() {
        Long loginUserId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        Org model = BeanUtil.toBean(this, Org.class);
        model.setUsableFlag(true).setDeleteFlag(false);
        model.setCreateUserId(loginUserId).setCreateTime(now);
        model.setUpdateUserId(loginUserId).setUpdateTime(now);
        return model;
    }

    @Data
    public static class AdminUser {

        @ApiModelProperty("管理员名称")
        @NotBlank(message = "管理员名称不能为空")
        @Length(max = 10, message = "管理员名称不能超过10个字符")
        private String userName;

        @ApiModelProperty("管理员手机号")
        @NotBlank(message = "管理员手机号不能为空")
        @Pattern(regexp = ValidatorUtils.REGEX_MOBILE, message = "管理员手机号格式不正确")
        private String userMobile;
    }
}
