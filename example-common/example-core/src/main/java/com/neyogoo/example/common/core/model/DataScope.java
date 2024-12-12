package com.neyogoo.example.common.core.model;

import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.enumeration.DataScopeTypeEnum;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 数据权限查询参数
 */
@Data
public class DataScope {

    /**
     * 限制范围为个人时的字段名称
     */
    private String userFieldName = "create_user_id";
    /**
     * 限制范围的字段名称 （除个人外）
     */
    private String deptFieldName = "dept_id";
    /**
     * 限制范围为机构时的字段名称
     */
    private String orgFieldName = "org_id";
    /**
     * 当前用户id
     */
    private Long userId;
    /**
     * 当前机构id
     */
    private Long orgId;
    /**
     * 当前部门id
     */
    private Long deptId;
    /**
     * 可查看的机构id列表
     */
    private List<Long> orgIds;
    /**
     * 可查看的部门id列表
     */
    private List<Long> deptIds;
    /**
     * 数据权限类型
     */
    private DataScopeTypeEnum dsType;

    public static DataScope generalDefault() {
        DataScope dataScope = new DataScope();
        dataScope.setUserId(ContextUtils.getUserId());
        dataScope.setOrgId(ContextUtils.getOrgId());
        dataScope.setOrgIds(Collections.singletonList(dataScope.getOrgId()));
        dataScope.setDeptId(BasicConstant.DEFAULT_ID);
        dataScope.setDeptIds(Collections.emptyList());
        dataScope.setDsType(DataScopeTypeEnum.Self);
        return dataScope;
    }
}
