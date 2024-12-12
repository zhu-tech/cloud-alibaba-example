package com.neyogoo.example.admin.sys.vo.response.dept;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.common.core.model.Pair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构信息")
public class DeptGetRespVO {

    @ApiModelProperty("部门id")
    private Long id;

    @ApiModelProperty("机构id")
    private String orgId;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门等级")
    private Integer deptLevel;

    @ApiModelProperty("部门描述")
    private String description;

    @ApiModelProperty("主负责人（key：用户id，value：用户名称）")
    private List<Pair<Long, String>> majorAdminUsers;

    @ApiModelProperty("副负责人（key：用户id，value：用户名称）")
    private List<Pair<Long, String>> minorAdminUsers;

    /**
     * 从数据库对象转换
     */
    public static DeptGetRespVO fromModel(Dept model) {
        return BeanUtil.toBean(model, DeptGetRespVO.class);
    }
}
