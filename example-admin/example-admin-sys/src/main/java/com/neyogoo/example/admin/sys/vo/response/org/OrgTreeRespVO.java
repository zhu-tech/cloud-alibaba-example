package com.neyogoo.example.admin.sys.vo.response.org;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.anji.captcha.util.StringUtils;
import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.biz.common.enumeration.sys.OrgLevelEnum;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.util.EntityUtils;
import com.neyogoo.example.common.core.util.StrPool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "机构树返参")
public class OrgTreeRespVO {

    @ApiModelProperty("机构id")
    private Long id;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("机构编码")
    private String orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("机构等级（枚举：OrgLevel）")
    private OrgLevelEnum orgLevel;

    @ApiModelProperty("机构类别（枚举：OrgCategory）")
    private String orgCategory;

    @ApiModelProperty("电话")
    private String contactNumber;

    @ApiModelProperty("地址")
    private String fullAddress;

    @ApiModelProperty("地址（-分隔）")
    private String dashAddress;

    @ApiModelProperty("是否启用")
    private Boolean usableFlag;

    @ApiModelProperty("下级机构")
    private List<OrgTreeRespVO> children;

    /**
     * 列表转树
     */
    public static List<OrgTreeRespVO> toTree(Org model) {
        if (model == null) {
            return Collections.emptyList();
        }
        OrgTreeRespVO respVO = OrgTreeRespVO.fromModel(model);
        respVO.setChildren(Collections.emptyList());
        return Collections.singletonList(respVO);
    }

    /**
     * 列表转树
     */
    public static List<OrgTreeRespVO> toTree(Long headId, List<Org> models) {
        if (CollUtil.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<OrgTreeRespVO> respVOs = models.stream().filter(o -> !o.getDeleteFlag())
                .map(OrgTreeRespVO::fromModel).collect(Collectors.toList());
        // key：机构id，value：机构信息
        Map<Long, OrgTreeRespVO> respVOMap = EntityUtils.toMap(respVOs, OrgTreeRespVO::getId);

        OrgTreeRespVO parent;
        for (OrgTreeRespVO respVO : respVOs) {
            if (respVO.children == null) {
                respVO.children = new ArrayList<>();
            }
            if (BasicConstant.DEFAULT_ID.equals(respVO.parentId)) {
                continue;
            }
            parent = respVOMap.get(respVO.getParentId());
            if (parent == null) {
                continue;
            }
            if (parent.children == null) {
                parent.children = new ArrayList<>();
            }
            parent.children.add(respVO);
        }
        if (headId == null) {
            return respVOs.stream().filter(o -> BasicConstant.DEFAULT_ID.equals(o.parentId))
                    .collect(Collectors.toList());
        } else {
            return respVOs.stream().filter(o -> headId.equals(o.id)).collect(Collectors.toList());
        }
    }

    /**
     * 从数据库对象转换
     */
    private static OrgTreeRespVO fromModel(Org model) {
        OrgTreeRespVO respVO = BeanUtil.toBean(model, OrgTreeRespVO.class);
        if (StringUtils.isBlank(model.getAreaName())) {
            respVO.setFullAddress(model.getAddress());
            respVO.setDashAddress(model.getAddress());
        } else {
            String[] split = model.getAreaName().split(StrPool.DASH);
            if (split[0].equals(split[1])) {
                respVO.setFullAddress(split[0] + split[2] + model.getAddress());
                respVO.setDashAddress(split[0] + StrPool.DASH + split[2] + StrPool.DASH + model.getAddress());
            } else {
                respVO.setFullAddress(split[0] + split[1] + split[2] + model.getAddress());
                respVO.setDashAddress(model.getAreaName() + StrPool.DASH + model.getAddress());
            }
        }
        return respVO;
    }
}
