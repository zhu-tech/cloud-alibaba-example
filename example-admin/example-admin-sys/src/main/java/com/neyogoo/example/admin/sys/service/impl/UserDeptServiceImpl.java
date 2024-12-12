package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Sets;
import com.neyogoo.example.admin.sys.dao.UserDeptMapper;
import com.neyogoo.example.admin.sys.model.UserDept;
import com.neyogoo.example.admin.sys.service.UserDeptService;
import com.neyogoo.example.admin.sys.vo.response.user.UserDeptRespVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.Pair;
import com.neyogoo.example.common.core.util.EntityUtils;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户关联部门
 */
@Slf4j
@Service
public class UserDeptServiceImpl extends SuperServiceImpl<UserDeptMapper, UserDept> implements UserDeptService {

    /**
     * 根据部门id查询可用用户id
     */
    @Override
    public List<Long> listUsableUserIdsByDeptId(Long deptId) {
        if (deptId == null) {
            return Collections.emptyList();
        }
        return baseMapper.listUsableUserIdsByDeptId(deptId);
    }

    /**
     * 根据部门id查询可用用户id
     */
    @Override
    public Map<Long, List<UserDeptRespVO>> mapByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyMap();
        }
        return EntityUtils.groupingBy(baseMapper.listUsableUsersByDeptIds(deptIds),
                UserDeptRespVO::getDeptId);
    }

    /**
     * 根据用户id查询部门id
     */
    @Override
    public List<Long> listDeptIdsByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<UserDept>lbQ()
                        .select(UserDept::getDeptId)
                        .eq(UserDept::getUserId, userId)
                        .eq(UserDept::getDeleteFlag, false)
        ).stream().map(UserDept::getDeptId).distinct().collect(Collectors.toList());
    }

    /**
     * 根据用户id列表查询关联部门名称
     *
     * @return key：用户id，value：关联部门名称
     */
    @Override
    public Map<Long, String> mapDeptNamesByUserIds(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        return Pair.toMap(baseMapper.listDeptNamesByUserIds(userIds));
    }

    /**
     * 保存关联关系
     */
    @Override
    public boolean updateRelations(Long userId, Collection<Long> deptIds) {
        Set<Long> newDeptIds = new HashSet<>(deptIds);
        Set<Long> oldDeptIds = new HashSet<>(listDeptIdsByUserId(userId));
        removeByUserIdAndDeptIds(userId, Sets.difference(oldDeptIds, newDeptIds));
        saveByUserIdAndDeptIds(userId, Sets.difference(newDeptIds, oldDeptIds));
        return true;
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
                Wraps.<UserDept>lbU()
                        .set(UserDept::getDeleteFlag, true)
                        .set(UserDept::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserDept::getUpdateTime, LocalDateTime.now())
                        .eq(UserDept::getUserId, userId)
                        .eq(UserDept::getDeleteFlag, false)
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
                Wraps.<UserDept>lbU()
                        .set(UserDept::getDeleteFlag, true)
                        .set(UserDept::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserDept::getUpdateTime, LocalDateTime.now())
                        .eq(UserDept::getDeptId, deptId)
                        .eq(UserDept::getDeleteFlag, false)
        );
    }

    /**
     * 根据用户id和部门id列表保存
     */
    private boolean saveByUserIdAndDeptIds(Long userId, Collection<Long> deptIds) {
        if (userId == null || CollUtil.isEmpty(deptIds)) {
            return false;
        }
        Long loginUserId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        List<UserDept> relations = deptIds.stream()
                .map(deptId -> UserDept.builder()
                        .userId(userId).deptId(deptId)
                        .deleteFlag(false)
                        .createUserId(loginUserId).createTime(now)
                        .updateUserId(loginUserId).updateTime(now)
                        .build()
                )
                .collect(Collectors.toList());
        return saveBatch(relations);
    }

    /**
     * 根据用户id和部门id列表删除
     */
    private boolean removeByUserIdAndDeptIds(Long userId, Collection<Long> deptIds) {
        if (userId == null || CollUtil.isEmpty(deptIds)) {
            return false;
        }
        return update(
                Wraps.<UserDept>lbU()
                        .set(UserDept::getDeleteFlag, true)
                        .set(UserDept::getUpdateUserId, ContextUtils.getUserId())
                        .set(UserDept::getUpdateTime, LocalDateTime.now())
                        .eq(UserDept::getUserId, userId)
                        .in(UserDept::getDeptId, deptIds)
                        .eq(UserDept::getDeleteFlag, false)
        );
    }
}
