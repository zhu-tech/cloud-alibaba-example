package com.neyogoo.example.biz.toolbox.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.common.database.model.UpdatedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 地区
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_biz_area")
public class Area extends UpdatedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 地区编码
     */
    private String areaCode;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 地区全名
     */
    private String fullName;

    /**
     * 拼音首字母
     */
    private String pinyinHeader;

    /**
     * 拼音缩写
     */
    private String abbreviation;

    /**
     * 等级
     */
    private Integer levelValue;

    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 区县编码
     */
    private String countyCode;

    /**
     * 区县名称
     */
    private String countyName;

    /**
     * 是否删除
     */
    private Boolean deleteFlag;
}
