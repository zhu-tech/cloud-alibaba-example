package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.UserOrg;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户关联机构
 */
public interface UserOrgService extends SuperService<UserOrg> {

    /**
     * 根据用户id查询关联的可用机构id
     */
    List<Long> listUsableOrgIdsByUserId(Long userId);

    /**
     * 根据机构id查询可用的用户id
     */
    List<Long> listUsableUserIdsByOrgId(Long orgId);

    /**
     * 根据机构id查询可用的用户id（含子级）
     */
    List<Long> listUsableUserIdsByParentOrgId(Long orgId);

    /**
     * 根据用户id列表查询关联机构名称
     *
     * @return key：用户id，value：关联机构名称
     */
    Map<Long, String> mapOrgNamesByUserIds(Collection<Long> userIds);

    /**
     * 查询机构和用户的关联关系是否存在
     */
    boolean queryIsRelationExists(Long orgId, Long userId);

    /**
     * 新增用户关联机构
     */
    boolean saveRelation(Long userId, Long orgId);

    /**
     * 根据用户id删除
     */
    boolean removeByUserId(Long userId);

    /**
     * 根据机构id删除
     */
    boolean removeByOrgId(Long orgId);
}
