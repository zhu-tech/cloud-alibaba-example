package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.admin.sys.dao.UserOrgMapper;
import com.neyogoo.example.admin.sys.model.UserOrg;
import com.neyogoo.example.admin.sys.service.UserOrgService;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 用户关联机构
 */
@Slf4j
@Service
public class UserOrgServiceImpl extends SuperServiceImpl<UserOrgMapper, UserOrg> implements UserOrgService {

    /**
     * 根据用户id查询关联的可用机构id
     */
    @Override
    public List<Long> listUsableOrgIdsByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableOrgIdsByUserId(userId);
    }

    /**
     * 根据机构id查询可用的用户id
     */
    @Override
    public List<Long> listUsableUserIdsByOrgId(Long orgId) {
        if (orgId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableUserIdsByOrgId(orgId);
    }

    /**
     * 根据机构id查询可用的用户id（含子级）
     */
    @Override
    public List<Long> listUsableUserIdsByParentOrgId(Long orgId) {
        if (orgId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableUserIdsByParentOrgId(orgId);
    }

    /**
     * 根据用户id列表查询关联机构名称
     *
     * @return key：用户id，value：关联机构名称
     */
    @Override
    public Map<Long, String> mapOrgNamesByUserIds(Collection<Long> userIds) {
        if (CollectionUtil.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        return Pair.toMap(baseMapper.listOrgNamesByUserIds(userIds));
    }

    /**
     * 查询机构和用户的关联关系是否存在
     */
    @Override
    public boolean queryIsRelationExists(Long orgId, Long userId) {
        if (orgId == null || userId == null) {
            return false;
        }
        return baseMapper.selectList(
                Wraps.<UserOrg>lbQ()
                        .select(UserOrg::getId)
                        .eq(UserOrg::getUserId, userId)
                        .eq(UserOrg::getOrgId, orgId)
                        .eq(UserOrg::getDeleteFlag, false)
                        .last("limit 1")
        ) != null;
    }

    /**
     * 新增用户关联机构
     */
    @Override
    public boolean saveRelation(Long userId, Long orgId) {
        if (userId == null || orgId == null) {
            return false;
        }
        Long loginUserId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        UserOrg model = UserOrg.builder()
                .userId(userId).orgId(orgId)
                .deleteFlag(false)
                .createUserId(loginUserId).createTime(now)
                .updateUserId(loginUserId).updateTime(now)
                .build();
        return baseMapper.insert(model) > 0;
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
                Wraps.<UserOrg>lbU()
                        .set(UserOrg::getDeleteFlag, true)
                        .set(UserOrg::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserOrg::getUpdateTime, LocalDateTime.now())
                        .eq(UserOrg::getUserId, userId)
                        .eq(UserOrg::getDeleteFlag, false)
        );
    }

    /**
     * 根据机构id删除
     */
    @Override
    public boolean removeByOrgId(Long orgId) {
        if (orgId == null) {
            return false;
        }
        return update(
                Wraps.<UserOrg>lbU()
                        .set(UserOrg::getDeleteFlag, true)
                        .set(UserOrg::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserOrg::getUpdateTime, LocalDateTime.now())
                        .eq(UserOrg::getOrgId, orgId)
                        .eq(UserOrg::getDeleteFlag, false)
        );
    }
}
