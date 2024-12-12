package com.neyogoo.example.admin.sys.dao;

import com.neyogoo.example.admin.sys.model.UserOrg;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.database.mvc.dao.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 用户关联机构
 */
@Repository
public interface UserOrgMapper extends SuperMapper<UserOrg> {

    /**
     * 根据用户id查询关联的可用机构id
     */
    List<Long> listUsableOrgIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据机构id查询可用的用户id
     */
    List<Long> listUsableUserIdsByOrgId(@Param("orgId") Long orgId);

    /**
     * 根据机构id查询可用的用户id
     */
    List<Long> listUsableUserIdsByParentOrgId(@Param("orgId") Long orgId);

    /**
     * 根据用户id列表查询关联机构名称
     */
    List<Pair<Long, String>> listOrgNamesByUserIds(@Param("userIds") Collection<Long> userIds);
}
