package com.neyogoo.example.admin.sys.vo.response.org;

import com.neyogoo.example.admin.sys.model.Dept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@ApiModel(description = "机构部门列表")
public class OrgDeptRespVO {

    @ApiModelProperty("机构id")
    private Long id;

    @ApiModelProperty("机构名称")
    private String labelName;

    @ApiModelProperty("机构部门")
    private List<OrgDept> deptList;

    @Data
    @AllArgsConstructor
    public static class OrgDept {

        @ApiModelProperty("部门id")
        private Long id;

        @ApiModelProperty("部门名称")
        private String labelName;

    }

    public static List<OrgDeptRespVO> transferList(Map<Long, String> orgMap, Map<Long, List<Dept>> orgDeptMap) {
        List<OrgDeptRespVO> list = new ArrayList<>();
        for (Map.Entry<Long, String> entry : orgMap.entrySet()) {
            OrgDeptRespVO result = new OrgDeptRespVO();
            result.setId(entry.getKey());
            result.setLabelName(entry.getValue());
            if (orgDeptMap.containsKey(entry.getKey())) {
                result.setDeptList(orgDeptMap.get(entry.getKey()).stream()
                        .map(dept -> new OrgDept(dept.getId(), dept.getDeptName()))
                        .collect(Collectors.toList()));
            }
            list.add(result);
        }
        return list;
    }
}
