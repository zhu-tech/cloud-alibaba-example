package com.neyogoo.example.admin.sys.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.neyogoo.example.biz.common.enumeration.sys.OrgLevelEnum;
import com.neyogoo.example.common.database.model.UpdatedEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * 机构
 */
@Getter
@Setter
@SuperBuilder
@Accessors(chain = true)
@ToString(callSuper = true)
@NoArgsConstructor
@TableName("t_sys_org")
public class Org extends UpdatedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构等级
     */
    private OrgLevelEnum orgLevel;

    /**
     * 机构类别
     *
     * @see com.neyogoo.example.biz.common.enumeration.sys.OrgCategoryEnum
     */
    private String orgCategory;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 上级id列表
     */
    private String parentIds;

    /**
     * 邮政编码
     */
    private String postCode;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 邮箱
     */
    private String emailAddress;

    /**
     * 传真
     */
    private String faxNumber;

    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 区编码
     */
    private String countyCode;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 具体地址
     */
    private String address;

    /**
     * 机构描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean usableFlag;

    /**
     * 是否删除
     */
    private Boolean deleteFlag;
}
