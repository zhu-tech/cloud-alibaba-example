package com.neyogoo.example.admin.sys.vo.request.dept;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@ApiModel(description = "部门新增入参")
public class DeptSaveReqVO {

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("部门名称")
    @NotBlank(message = "请输入部门名称")
    @Length(max = 20, message = "部门名称不能超过20个字符")
    protected String deptName;

    @ApiModelProperty("部门描述")
    @Length(max = 200, message = "部门名称不能超过200个字符")
    protected String description;

    @ApiModelProperty(value = "机构id", hidden = true)
    @JsonIgnore
    protected Long orgId;

    @ApiModelProperty("主负责人id列表")
    @NotNull(message = "主负责人id列表不能为 null")
    protected Set<Long> majorAdminUserIds;

    @ApiModelProperty("副负责人id列表")
    @NotNull(message = "副负责人id列表不能为 null")
    protected Set<Long> minorAdminUserIds;

    /**
     * 生成默认部门入参
     */
    public static DeptSaveReqVO generalDefault(Long orgId) {
        DeptSaveReqVO reqVO = new DeptSaveReqVO();
        reqVO.deptName = "默认部门";
        reqVO.description = "系统生成默认部门";
        reqVO.orgId = orgId;
        return reqVO;
    }

    /**
     * 转为数据库对象
     */
    public Dept toModel() {
        Long loginUserId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        Dept model = BeanUtil.toBean(this, Dept.class);
        if (model.getParentId() == null) {
            model.setParentId(BasicConstant.DEFAULT_ID);
        }
        // 部门只有2级
        model.setDeptLevel(BasicConstant.DEFAULT_ID.equals(model.getParentId()) ? 1 : 2);
        model.setDeleteFlag(false);
        model.setCreateUserId(loginUserId).setCreateTime(now);
        model.setUpdateUserId(loginUserId).setUpdateTime(now);

        return model;
    }
}
