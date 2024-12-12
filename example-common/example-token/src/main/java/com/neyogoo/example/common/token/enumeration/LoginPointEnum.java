package com.neyogoo.example.common.token.enumeration;

import com.neyogoo.example.common.core.enumeration.BaseEnum;
import com.neyogoo.example.common.core.util.Base64Utils;
import com.neyogoo.example.common.core.util.StrPool;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Getter
@ApiModel("登录点")
@AllArgsConstructor
@NoArgsConstructor
public enum LoginPointEnum implements BaseEnum {

    PC("管理端PC", "seofa"),
    H5("客户端H5", "jiepq"),
    ;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("密钥（随机生成出来的）")
    private String secret;

    public static LoginPointEnum match(String val, LoginPointEnum def) {
        return Stream.of(values()).filter((item) -> item.name().equalsIgnoreCase(val)).findAny().orElse(def);
    }

    public static LoginPointEnum get(String val) {
        return match(val, null);
    }

    public boolean eq(LoginPointEnum val) {
        return val != null && eq(val.name());
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "PC", example = "PC")
    public String getCode() {
        return this.name();
    }

    public String base64Encode() {
        return Base64Utils.encode(this.name() + StrPool.COLON + this.secret);
    }
}
