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
public class UpdatedEntity extends SavedEntity {

    private static final long serialVersionUID = 1L;

    public static final String UPDATE_TIME = "updateTime";
    public static final String UPDATE_TIME_FIELD = "update_time";
    public static final String UPDATE_USER_ID = "updateUserId";
    public static final String UPDATE_USER_ID_FIELD = "update_user_id";


    @ApiModelProperty("最后修改时间")
    @TableField(value = UPDATE_TIME_FIELD, fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    @ApiModelProperty("最后修改人ID")
    @TableField(value = UPDATE_USER_ID_FIELD, fill = FieldFill.INSERT_UPDATE)
    protected Long updateUserId;
}
