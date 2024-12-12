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
 * 部门
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_sys_dept")
public class Dept extends UpdatedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 机构id
     */
    private Long orgId;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门级别
     */
    private Integer deptLevel;

    /**
     * 部门描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Boolean deleteFlag;
}
