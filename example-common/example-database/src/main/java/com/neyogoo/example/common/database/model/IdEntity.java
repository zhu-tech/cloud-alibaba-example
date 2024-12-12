package com.neyogoo.example.common.database.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class IdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ID_FIELD = "id";

    @TableId(value = ID_FIELD, type = IdType.INPUT)
    @ApiModelProperty("主键")
    @NotNull(message = "id不能为空", groups = Update.class)
    protected Long id;


    /**
     * 保存和缺省验证组
     */
    public interface Save extends Default {

    }

    /**
     * 更新和缺省验证组
     */
    public interface Update extends Default {

    }
}
