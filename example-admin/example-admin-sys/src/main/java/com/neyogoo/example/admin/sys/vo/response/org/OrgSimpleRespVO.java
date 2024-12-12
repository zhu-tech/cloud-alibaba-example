package com.neyogoo.example.admin.sys.vo.response.org;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.biz.common.enumeration.sys.OrgLevelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构简易信息")
public class OrgSimpleRespVO {

    @ApiModelProperty("机构id")
    private Long id;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("机构等级（枚举：OrgLevel）")
    private OrgLevelEnum orgLevel;

    @ApiModelProperty("机构类别（枚举：OrgCategory，多个间英文逗号分隔）")
    private String orgCategory;

    /**
     * 从数据库对象转换
     */
    public static OrgSimpleRespVO fromModel(Org model) {
        return model == null ? null : BeanUtil.copyProperties(model, OrgSimpleRespVO.class);
    }

    /**
     * 从数据库对象转换
     */
    public static List<OrgSimpleRespVO> fromModels(Collection<Org> models) {
        return models.stream().map(OrgSimpleRespVO::fromModel).collect(Collectors.toList());
    }
}
