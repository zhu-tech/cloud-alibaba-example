package com.neyogoo.example.biz.common.enumeration.toolbox;

import com.neyogoo.example.common.core.enumeration.BaseEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("短信模板")
public enum SmsTemplateEnum implements BaseEnum {

    VerificationCode("code", "SMS_00000000", "短信验证码", "短信验证码");

    @ApiModelProperty("短信模板必需的参数，多个key以逗号分隔")
    private String params;

    @ApiModelProperty("短信模板编码")
    private String templateCode;

    @ApiModelProperty("短信标题")
    private String smsTitle;

    @ApiModelProperty("描述")
    private String desc;

    public static SmsTemplateEnum match(String val, SmsTemplateEnum def) {
        return Stream.of(values()).filter((item) -> item.getCode().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static SmsTemplateEnum getByTemplateCode(String val) {
        return Stream.of(values()).filter((item) -> val.equals(item.templateCode)).findAny().orElse(null);
    }

    public static SmsTemplateEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(SmsTemplateEnum val) {
        return val != null && eq(val.getCode());
    }

    @Override
    @ApiModelProperty(value = "编码")
    public String getCode() {
        return this.name();
    }

}
