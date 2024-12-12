package com.neyogoo.example.admin.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.common.database.model.UpdatedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 用户关联机构（一个用户只会关联一个机构）
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("r_user_org")
public class UserOrg extends UpdatedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 机构id
     */
    private Long orgId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否删除
     */
    private Boolean deleteFlag;
}
