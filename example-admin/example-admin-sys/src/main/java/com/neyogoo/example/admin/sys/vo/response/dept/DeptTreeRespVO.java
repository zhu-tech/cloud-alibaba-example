package com.neyogoo.example.admin.sys.vo.response.dept;

import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构信息")
public class DeptTreeRespVO {

    @ApiModelProperty("部门id")
    private Long id;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门等级")
    private Integer deptLevel;

    @ApiModelProperty("部门描述")
    private String description;

    @ApiModelProperty("下级部门")
    private List<DeptTreeRespVO> children;

    /**
     * 从数据库对象转换
     */
    public static List<DeptTreeRespVO> fromModels(Collection<Dept> models) {
        List<DeptTreeRespVO> list = EntityUtils.toBeanList(models, DeptTreeRespVO.class);
        Map<Long, List<DeptTreeRespVO>> childMap = EntityUtils.groupingBy(list, DeptTreeRespVO::getParentId);
        for (DeptTreeRespVO vo : list) {
            vo.setChildren(childMap.getOrDefault(vo.getId(), Collections.emptyList()));
        }
        return list.stream().filter(o -> o.getDeptLevel() == 1).collect(Collectors.toList());
    }
}
