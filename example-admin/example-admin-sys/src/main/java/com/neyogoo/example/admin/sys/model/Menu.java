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
 * 菜单
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_sys_menu")
public class Menu extends UpdatedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 菜单路径
     */
    private String menuHref;

    /**
     * 直接上级菜单id
     */
    private Long parentId;

    /**
     * 所有上级菜单id
     */
    private String parentIds;

    /**
     * 等级
     */
    private Integer levelValue;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否展示
     */
    private Boolean showFlag;

    /**
     * 是否启用
     */
    private Boolean usableFlag;

    /**
     * 是否删除
     */
    private Boolean deleteFlag;
}
