package com.neyogoo.example.biz.common.constant;

import com.google.common.collect.ImmutableList;

public final class BizRoleConstant {

    /**
     * 平台管理员
     */
    public static final String SENIOR_ADMIN_ROLE_CODE = "SR001";

    /**
     * 机构管理员
     */
    public static final String ORG_ADMIN_ROLE_CODE = "SR002";

    /**
     * 部门主负责人
     */
    public static final String DEPT_MAJOR_ADMIN_ROLE_CODE = "SR003";

    /**
     * 部门副负责人
     */
    public static final String DEPT_MINOR_ADMIN_ROLE_CODE = "SR004";

    /**
     * 内置角色编码
     */
    public static final ImmutableList<String> BUILD_IN_ROLE_CODES = ImmutableList.of(
            SENIOR_ADMIN_ROLE_CODE,
            ORG_ADMIN_ROLE_CODE,
            DEPT_MAJOR_ADMIN_ROLE_CODE,
            DEPT_MINOR_ADMIN_ROLE_CODE
    );


    public BizRoleConstant() {

    }
}
