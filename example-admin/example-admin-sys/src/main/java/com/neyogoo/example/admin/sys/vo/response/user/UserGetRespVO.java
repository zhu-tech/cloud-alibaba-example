package com.neyogoo.example.admin.sys.vo.response.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.neyogoo.example.admin.sys.model.User;
import com.neyogoo.example.admin.sys.model.UserRole;
import com.neyogoo.example.biz.common.enumeration.sys.GenderEnum;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "账号信息")
public class UserGetRespVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("工号")
    private String userCode;

    @ApiModelProperty("名称")
    private String userName;

    @ApiModelProperty("手机号")
    private String userMobile;

    @ApiModelProperty("性别")
    private GenderEnum userGender;

    @ApiModelProperty("出生日期")
    private LocalDate userBirthday;

    @ApiModelProperty("邮箱")
    private String userEmail;

    @ApiModelProperty("政治面貌（字典：Politic）")
    private String politic;

    @ApiModelProperty("学历（字典：UserEducation）")
    private String education;

    @ApiModelProperty("民族（字典：Nationality）")
    private String nationality;

    @ApiModelProperty("籍贯")
    private String nativePlace;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    @ApiModelProperty("关联部门列表（key：部门id，value：部门名称）")
    private List<Pair<Long, String>> depts;

    @ApiModelProperty("关联角色列表")
    private List<DeptRole> roles;

    @Data
    public static class DeptRole {

        @ApiModelProperty("部门（key：部门id，value：部门名称）")
        private Pair<Long, String> dept;

        @ApiModelProperty("关联角色列表（key：角色id，value：角色名称）")
        private List<Pair<Long, String>> roles;

        public static List<DeptRole> from(List<Long> deptIds, List<UserRole> userRoles, Map<Long, String> deptMap,
                                          Map<Long, String> roleMap) {
            if (CollectionUtil.isEmpty(userRoles)) {
                return Collections.emptyList();
            }
            List<DeptRole> list = Lists.newArrayListWithExpectedSize(deptIds.size());
            Map<Long, List<UserRole>> map = EntityUtils.groupingBy(userRoles, UserRole::getDeptId);
            for (Long deptId : deptIds) {
                DeptRole deptRole = new DeptRole();
                deptRole.setDept(new Pair<>(deptId, deptMap.get(deptId)));
                list.add(deptRole);
                if (!map.containsKey(deptId)) {
                    deptRole.setRoles(Collections.emptyList());
                    continue;
                }
                deptRole.setRoles(
                        map.get(deptId).stream()
                                .map(o -> new Pair<>(o.getRoleId(), roleMap.get(o.getRoleId())))
                                .collect(Collectors.toList())
                );
            }
            return list;
        }
    }

    /**
     * 从数据库对象转换
     */
    public static UserGetRespVO fromModel(User model) {
        return BeanUtil.toBean(model, UserGetRespVO.class);
    }
}
