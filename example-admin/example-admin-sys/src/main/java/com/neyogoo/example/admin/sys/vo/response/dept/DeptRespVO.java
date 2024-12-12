package com.neyogoo.example.admin.sys.vo.response.dept;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.common.core.util.EntityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构信息")
public class DeptRespVO {

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

    /**
     * 从数据库对象转换
     */
    public static List<DeptRespVO> fromModels(Collection<Dept> models) {
        return EntityUtils.toBeanList(models, DeptRespVO.class);
    }

    /**
     * 从数据库对象转换
     */
    public static DeptRespVO fromModel(Dept model) {
        if (model == null) {
            return null;
        }
        return BeanUtil.toBean(model, DeptRespVO.class);
    }
}
