package com.neyogoo.example.common.database.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class SavedEntity extends IdEntity {

    private static final long serialVersionUID = 1L;

    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_TIME_FIELD = "create_time";
    public static final String CREATE_USER_ID = "createUserId";
    public static final String CREATE_USER_ID_FIELD = "create_user_id";

    @ApiModelProperty("创建时间")
    @TableField(value = CREATE_TIME_FIELD, fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @ApiModelProperty("创建人ID")
    @TableField(value = CREATE_USER_ID_FIELD, fill = FieldFill.INSERT)
    protected Long createUserId;
}
