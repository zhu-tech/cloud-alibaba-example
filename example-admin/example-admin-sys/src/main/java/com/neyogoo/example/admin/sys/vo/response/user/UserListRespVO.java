package com.neyogoo.example.admin.sys.vo.response.user;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.common.core.util.StrPool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Data
@ApiModel(description = "用户列表返回实体类")
public class UserListRespVO {

    @ApiModelProperty("人员id")
    private Long id;

    @ApiModelProperty("人员编号")
    private String userCode;

    @ApiModelProperty("人员姓名")
    private String userName;

    @ApiModelProperty("机构id")
    private Long orgId;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编码")
    @JsonIgnore
    private String roleSort;

    public static List<UserListRespVO> sortByRole(List<UserListRespVO> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.sort(Comparator.comparing(UserListRespVO::roleSortValue));
        }
        return list;
    }

    private int roleSortValue() {
        if (StringUtils.isBlank(roleSort)) {
            return 99;
        }
        return Arrays.stream(roleSort.split(StrPool.COMMA)).mapToInt(Integer::valueOf).min().getAsInt();
    }
}
