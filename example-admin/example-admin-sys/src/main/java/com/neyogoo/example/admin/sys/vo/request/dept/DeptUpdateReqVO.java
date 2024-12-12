package com.neyogoo.example.admin.sys.vo.request.dept;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "部门更新入参")
public class DeptUpdateReqVO extends DeptSaveReqVO {

    @ApiModelProperty("部门id")
    @NotNull(message = "部门id不能为空")
    private Long id;

    /**
     * 转为数据库对象
     */
    public void toModel(Dept model) {
        BeanUtil.copyProperties(this, model);
        if (model.getParentId() == null) {
            model.setParentId(BasicConstant.DEFAULT_ID);
        }
        // 部门只有2级
        model.setDeptLevel(BasicConstant.DEFAULT_ID.equals(model.getParentId()) ? 1 : 2);
        model.setUpdateUserId(ContextUtils.getUserId())
                .setUpdateTime(LocalDateTime.now());
    }
}
