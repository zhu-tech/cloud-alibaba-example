package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Sets;
import com.neyogoo.example.admin.sys.dao.UserRoleMapper;
import com.neyogoo.example.admin.sys.model.UserRole;
import com.neyogoo.example.admin.sys.service.UserRoleService;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户关联角色
 */
@Slf4j
@Service
public class UserRoleServiceImpl extends SuperServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    /**
     * 根据用户id查询
     */
    @Override
    public List<UserRole> listByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<UserRole>lbQ()
                        .eq(UserRole::getUserId, userId)
                        .eq(UserRole::getDeleteFlag, false)
        );
    }

    /**
     * 根据用户id查询有效的关联关系
     */
    @Override
    public List<UserRole> listUsableByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableByUserId(userId);
    }

    /**
     * 根据用户id查询关联的可用角色id
     */
    @Override
    public List<Long> listUsableRoleIdsByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableRoleIdsByUserId(userId).stream().distinct().collect(Collectors.toList());
    }

    /**
     * 根据用户id查询关联的可用角色编码
     */
    @Override
    public List<String> listUsableRoleCodesByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableRoleCodesByUserId(userId).stream().distinct().collect(Collectors.toList());
    }

    /**
     * 根据部门id和角色编码查询用户id（含已禁用的用户）
     */
    @Override
    public List<Long> listUserIdsByDeptIdAndRoleId(Long deptId, Long roleId) {
        if (deptId == null || roleId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUserIdsByDeptIdAndRoleId(deptId, roleId);
    }

    /**
     * 根据部门id和角色编码查询可用用户id
     */
    @Override
    public List<Long> listUsableUserIdsByDeptIdAndRoleId(Long deptId, Long roleId) {
        if (deptId == null || roleId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableUserIdsByDeptIdAndRoleId(deptId, roleId);
    }

    /**
     * 根据角色id查询关联的用户id列表
     */
    @Override
    public List<Long> listUsableUserIdsByOrgIdAndRoleId(Long orgId, Long roleId) {
        if (orgId == null || roleId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableUserIdsByOrgIdAndRoleId(orgId, roleId);
    }

    /**
     * 根据用户id列表查询关联角色名称
     *
     * @return key：用户id，value：关联角色名称
     */
    @Override
    public Map<Long, String> mapRoleNamesByUserIds(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        return Pair.toMap(baseMapper.listRoleNamesByUserIds(userIds));
    }

    /**
     * 更新指定部门和角色下关联的人员
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean updateDeptRoleUnderUser(Long deptId, Long roleId, Collection<Long> userIds) {
        Set<Long> newUserIds = new HashSet<>(userIds);
        Set<Long> oldUserIds = new HashSet<>(listUserIdsByDeptIdAndRoleId(deptId, roleId));
        saveRelations(deptId, roleId, Sets.difference(newUserIds, oldUserIds));
        removeByDeptIdAndRoleIdAndUserIds(deptId, roleId, Sets.difference(oldUserIds, newUserIds));
        return true;
    }

    /**
     * 更新指定人员关联的部门下角色
     *
     * @param deptRoleIds key：部门id，value：角色id列表
     */
    @Override
    public boolean updateUserUnderDeptRole(Long userId, List<Pair<Long, List<Long>>> deptRoleIds) {
        List<UserRole> oldRelations = listByUserId(userId);
        List<UserRole> needSaveRelations = new ArrayList<>();
        List<UserRole> needRemoveRelations = new ArrayList<>();
        Long loginUserId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        boolean find;
        boolean deptMatch;
        boolean roleMatch;
        // 查找需要删除的数据
        for (UserRole oldRelation : oldRelations) {
            find = false;
            for (Pair<Long, List<Long>> deptRoleId : deptRoleIds) {
                deptMatch = oldRelation.getDeptId().equals(deptRoleId.getKey());
                roleMatch = deptRoleId.getValue().contains(oldRelation.getRoleId());
                if (deptMatch && roleMatch) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                needRemoveRelations.add(oldRelation);
            }
        }
        // 查找需要保存的数据
        for (Pair<Long, List<Long>> deptRoleId : deptRoleIds) {
            for (Long roleId : deptRoleId.getValue()) {
                find = false;
                for (UserRole oldRelation : oldRelations) {
                    deptMatch = oldRelation.getDeptId().equals(deptRoleId.getKey());
                    roleMatch = roleId.equals(oldRelation.getRoleId());
                    if (deptMatch && roleMatch) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    needSaveRelations.add(
                            UserRole.builder()
                                    .userId(userId).deptId(deptRoleId.getKey()).roleId(roleId)
                                    .deleteFlag(false)
                                    .createUserId(loginUserId).createTime(now)
                                    .updateUserId(loginUserId).updateTime(now)
                                    .build()
                    );

                }
            }
        }
        if (CollectionUtil.isNotEmpty(needSaveRelations)) {
            saveBatch(needSaveRelations);
        }
        if (CollectionUtil.isNotEmpty(needRemoveRelations)) {
            removeBatchByIds(needRemoveRelations);
        }
        return true;
    }


    /**
     * 新增用户关联角色
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean saveRelations(Long userId, Collection<Long> roleIds) {
        if (userId == null || CollectionUtil.isEmpty(roleIds)) {
            return false;
        }
        Long loginUserId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        List<UserRole> relations = roleIds.stream()
                .map(roleId -> UserRole.builder()
                        .userId(userId).roleId(roleId)
                        .deleteFlag(false)
                        .createUserId(loginUserId).createTime(now)
                        .updateUserId(loginUserId).updateTime(now)
                        .build()
                )
                .collect(Collectors.toList());
        return saveBatch(relations);
    }

    /**
     * 新增用户关联角色
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean saveRelations(Long deptId, Long roleId, Collection<Long> userIds) {
        if (deptId == null || roleId == null || CollectionUtil.isEmpty(userIds)) {
            return false;
        }
        Long loginUserId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        List<UserRole> relations = userIds.stream()
                .map(userId -> UserRole.builder()
                        .userId(userId).deptId(deptId).roleId(roleId)
                        .deleteFlag(false)
                        .createUserId(loginUserId).createTime(now)
                        .updateUserId(loginUserId).updateTime(now)
                        .build()
                )
                .collect(Collectors.toList());
        return saveBatch(relations);
    }

    /**
     * 根据用户id删除
     */
    @Override
    public boolean removeByUserId(Long userId) {
        if (userId == null) {
            return false;
        }
        return update(
                Wraps.<UserRole>lbU()
                        .set(UserRole::getDeleteFlag, true)
                        .set(UserRole::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserRole::getUpdateTime, LocalDateTime.now())
                        .eq(UserRole::getUserId, userId)
                        .eq(UserRole::getDeleteFlag, false)
        );
    }

    /**
     * 根据部门id删除
     */
    @Override
    public boolean removeByDeptId(Long deptId) {
        if (deptId == null) {
            return false;
        }
        return update(
                Wraps.<UserRole>lbU()
                        .set(UserRole::getDeleteFlag, true)
                        .set(UserRole::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserRole::getUpdateTime, LocalDateTime.now())
                        .eq(UserRole::getDeptId, deptId)
                        .eq(UserRole::getDeleteFlag, false)
        );
    }

    /**
     * 根据角色id删除
     */
    @Override
    public boolean removeByRoleId(Long roleId) {
        if (roleId == null) {
            return false;
        }
        return update(
                Wraps.<UserRole>lbU()
                        .set(UserRole::getDeleteFlag, true)
                        .set(UserRole::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserRole::getUpdateTime, LocalDateTime.now())
                        .eq(UserRole::getRoleId, roleId)
                        .eq(UserRole::getDeleteFlag, false)
        );
    }

    /**
     * 根据用户id和角色id列表删除
     */
    @Override
    public boolean removeByUserIdAndRoleIds(Long userId, Collection<Long> roleIds) {
        if (userId == null || CollectionUtil.isEmpty(roleIds)) {
            return false;
        }
        return update(
                Wraps.<UserRole>lbU()
                        .set(UserRole::getDeleteFlag, true)
                        .set(UserRole::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserRole::getUpdateTime, LocalDateTime.now())
                        .eq(UserRole::getUserId, userId)
                        .in(UserRole::getRoleId, roleIds)
                        .eq(UserRole::getDeleteFlag, false)
        );
    }

    /**
     * 根据部门id和角色id和用户id列表删除
     */
    private boolean removeByDeptIdAndRoleIdAndUserIds(Long deptId, Long roleId, Collection<Long> userIds) {
        if (deptId == null || roleId == null || CollectionUtil.isEmpty(userIds)) {
            return false;
        }
        return update(
                Wraps.<UserRole>lbU()
                        .set(UserRole::getDeleteFlag, true)
                        .set(UserRole::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserRole::getUpdateTime, LocalDateTime.now())
                        .in(UserRole::getUserId, userIds)
                        .eq(UserRole::getDeptId, deptId)
                        .eq(UserRole::getRoleId, roleId)
                        .eq(UserRole::getDeleteFlag, false)
        );
    }
}
