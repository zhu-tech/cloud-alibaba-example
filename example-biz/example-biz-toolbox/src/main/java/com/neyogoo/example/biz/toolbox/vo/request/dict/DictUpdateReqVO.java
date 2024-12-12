package com.neyogoo.example.biz.toolbox.vo.request.dict;

import cn.hutool.core.bean.BeanUtil;
import com.neyogoo.example.biz.toolbox.model.Dict;
import com.neyogoo.example.common.core.context.ContextUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "字典修改入参")
public class DictUpdateReqVO {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty("字典编码")
    @NotBlank(message = "字典编码不能为空")
    @Length(max = 8, message = "字典编码长度不能超过8位")
    @Pattern(regexp = "^[0-9a-zA-Z]{1,8}$", message = "字典编码格式错误，请输入1至8位的数字字母组合")
    private String dictCode;

    @ApiModelProperty("字典描述")
    @NotBlank(message = "字典描述不能为空")
    @Length(max = 20, message = "字典描述长度不能超过20位")
    private String dictName;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    public void toModel(Dict model) {
        if (model == null) {
            return;
        }
        BeanUtil.copyProperties(this, model);
        model.setUpdateUserId(ContextUtils.getUserId()).setUpdateTime(LocalDateTime.now());
    }
}
