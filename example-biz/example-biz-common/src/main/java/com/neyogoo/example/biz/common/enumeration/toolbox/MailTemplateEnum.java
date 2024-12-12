package com.neyogoo.example.biz.common.enumeration.toolbox;

import com.neyogoo.example.biz.common.model.MailHeaderParam;
import com.neyogoo.example.common.core.enumeration.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("站内信模板")
public enum MailTemplateEnum implements BaseEnum {

    Example("time,name", "示例通知",
            "请您于${time}参加活动：${name}",
            null, null, "示例通知");

    /**
     * 站内信模板必需的参数，多个key以逗号分隔
     */
    @ApiModelProperty("模板所需参数")
    private String params;

    @ApiModelProperty("站内信标题")
    private String msgTitle;

    @ApiModelProperty("站内信模板内容")
    private String msgContent;

    @ApiModelProperty("站内信外挂数据表头")
    private List<MailHeaderParam> headers;

    @ApiModelProperty("需操作内容")
    private String needOperation;

    @ApiModelProperty("描述")
    private String desc;


    public static MailTemplateEnum match(String val, MailTemplateEnum def) {
        return Stream.of(values()).filter((item) -> item.getCode().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static MailTemplateEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(MailTemplateEnum val) {
        return val != null && eq(val.getCode());
    }

    @Override
    @ApiModelProperty(value = "编码")
    public String getCode() {
        return this.name();
    }

}
