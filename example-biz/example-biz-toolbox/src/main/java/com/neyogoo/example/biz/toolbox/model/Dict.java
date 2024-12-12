package com.neyogoo.example.biz.toolbox.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import com.neyogoo.example.common.database.model.UpdatedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 字典
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_biz_dict")
public class Dict extends UpdatedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 类型编码
     */
    private DictTypeEnum dictType;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典描述
     */
    private String dictName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否只读
     */
    private Boolean readonlyFlag;

    /**
     * 是否启用
     */
    private Boolean usableFlag;

    /**
     * 是否删除
     */
    private Boolean deleteFlag;
}
