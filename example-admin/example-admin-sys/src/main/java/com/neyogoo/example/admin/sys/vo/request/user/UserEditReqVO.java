package com.neyogoo.example.admin.sys.vo.request.user;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.biz.common.enumeration.sys.GenderEnum;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.ValidatorUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@ApiModel(description = "账号编辑入参")
public class UserEditReqVO {

    @ApiModelProperty("用户名称")
    @NotBlank(message = "用户名称不能为空")
    @Length(max = 10, message = "用户名称不能超过10个字符")
    protected String userName;

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = ValidatorUtils.REGEX_MOBILE, message = "手机号格式不正确")
    protected String userMobile;

    @ApiModelProperty("性别")
    @NotNull(message = "性别不能为空")
    protected GenderEnum userGender;

    @ApiModelProperty("出生年月")
    protected LocalDate userBirthday;

    @ApiModelProperty("政治面貌（字典：Politic）")
    protected String politic;

    @ApiModelProperty("学历（字典：UserEducation）")
    protected String education;

    @ApiModelProperty("民族（字典：Nationality）")
    protected String nationality;

    @ApiModelProperty("籍贯")
    protected String nativePlace;

    @ApiModelProperty("邮箱")
    @Length(max = 50, message = "邮箱不能超过50个字符")
    @Pattern(regexp = "(^$)|(" + ValidatorUtils.REGEX_EMAIL + ")", message = "邮箱格式不正确")
    protected String userEmail;

    @ApiModelProperty("部门id列表")
    @NotNull(message = "部门id列表不能为 null")
    protected Set<Long> deptIds;

    @ApiModelProperty("部门角色列表")
    @NotNull(message = "部门角色列表不能为 null")
    @Valid
    protected List<DeptRole> deptRoles;

    @ApiModelProperty(value = "机构id", hidden = true)
    @JsonIgnore
    protected Long orgId;

    @Data
    @AllArgsConstructor
    public static class DeptRole {

        @ApiModelProperty("部门id")
        @NotNull(message = "部门id不能为空")
        private Long deptId;

        @ApiModelProperty("角色id列表")
        @NotNull(message = "角色id列表不能为 null")
        private List<Long> roleIds;
    }

    /**
     * 检查部门和角色数据
     */
    public void checkDeptAndRole(Set<Long> orgDeptIds, Set<Long> roleIds) {
        if (CollectionUtil.isEmpty(this.deptIds)) {
            this.deptRoles = Collections.emptyList();
            return;
        }
        ArgumentAssert.isTrue(orgDeptIds.containsAll(this.deptIds), "部门信息错误");
        if (CollectionUtil.isEmpty(this.deptRoles)) {
            return;
        }
        this.deptRoles = this.deptRoles.stream().filter(o -> this.deptIds.contains(o.getDeptId()))
                .collect(Collectors.toList());
        ArgumentAssert.isTrue(this.deptRoles.stream().allMatch(o -> roleIds.containsAll(o.getRoleIds())),
                "角色信息错误");
    }

    public List<Pair<Long, List<Long>>> toDeptRoleIds() {
        List<Pair<Long, List<Long>>> list = new ArrayList<>();
        for (DeptRole deptRole : deptRoles) {
            list.add(new Pair<>(deptRole.getDeptId(), deptRole.getRoleIds()));
        }
        return list;
    }
}
