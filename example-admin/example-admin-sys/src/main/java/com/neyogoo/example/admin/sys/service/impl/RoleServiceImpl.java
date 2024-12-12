package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.neyogoo.example.admin.sys.dao.RoleMapper;
import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.admin.sys.service.RoleService;
import com.neyogoo.example.admin.sys.vo.request.permission.RoleSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.RoleUpdateReqVO;
import com.neyogoo.example.biz.common.constant.BizRoleConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色
 */
@Slf4j
@Service
public class RoleServiceImpl extends SuperServiceImpl<RoleMapper, Role> implements RoleService {

    /**
     * 根据id查询角色信息
     */
    @Override
    public Role getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Role>lbQ()
                        .eq(Role::getId, id)
                        .eq(Role::getDeleteFlag, false)
        );
    }

    /**
     * 根据编码查询角色信息
     */
    @Override
    public Role getByCode(String roleCode) {
        if (CharSequenceUtil.isBlank(roleCode)) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Role>lbQ()
                        .eq(Role::getRoleCode, roleCode)
                        .eq(Role::getDeleteFlag, false)
        );
    }

    /**
     * 根据id列表查询名称
     */
    @Override
    public Map<Long, String> mapNamesByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(
                Wraps.<Role>lbQ()
                        .select(Role::getId, Role::getRoleName)
                        .in(Role::getId, ids)
                        .eq(Role::getDeleteFlag, false)
        ).stream().collect(Collectors.toMap(Role::getId, Role::getRoleName));
    }

    /**
     * 根据id列表查询编码
     */
    @Override
    public Map<Long, String> mapCodesByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(
                Wraps.<Role>lbQ()
                        .select(Role::getId, Role::getRoleCode)
                        .in(Role::getId, ids)
                        .eq(Role::getDeleteFlag, false)
        ).stream().collect(Collectors.toMap(Role::getId, Role::getRoleCode));
    }

    /**
     * 查询id和名称
     */
    @Override
    public Map<Long, String> mapIdNames() {
        return baseMapper.selectList(
                Wraps.<Role>lbQ()
                        .select(Role::getId, Role::getRoleName)
                        .eq(Role::getDeleteFlag, false)
        ).stream().collect(Collectors.toMap(Role::getId, Role::getRoleName));
    }

    /**
     * 列表
     */
    @Override
    public List<Role> list() {
        return baseMapper.selectList(
                Wraps.<Role>lbQ().eq(Role::getDeleteFlag, false)
        );
    }

    /**
     * 可用id列表
     */
    @Override
    public List<Long> listUsableIds() {
        return baseMapper.selectList(
                Wraps.<Role>lbQ()
                        .eq(Role::getUsableFlag, true)
                        .eq(Role::getDeleteFlag, false)
        ).stream().map(Role::getId).collect(Collectors.toList());
    }

    /**
     * 可用列表
     */
    @Override
    public List<Role> listUsable() {
        return baseMapper.selectList(
                Wraps.<Role>lbQ()
                        .eq(Role::getUsableFlag, true)
                        .eq(Role::getDeleteFlag, false)
        );
    }

    /**
     * 新增
     */
    @Override
    public Role save(RoleSaveReqVO reqVO) {
        Role model = getByCode(reqVO.getRoleCode());
        ArgumentAssert.isNull(model, "该角色编码已存在");
        model = reqVO.toModel();
        baseMapper.insert(model);
        return model;
    }

    /**
     * 修改
     */
    @Override
    public Role update(RoleUpdateReqVO reqVO) {
        Role search = getByCode(reqVO.getRoleCode());
        if (search != null) {
            ArgumentAssert.equals(search.getId(), reqVO.getId(), "该角色编码已存在");
        } else {
            search = getById(reqVO.getId());
            ArgumentAssert.isTrue(search != null && !search.getDeleteFlag(), "该角色不存在");
        }
        reqVO.toModel(search);
        ArgumentAssert.isFalse(BizRoleConstant.BUILD_IN_ROLE_CODES.contains(search.getRoleCode()),
                "内置角色无法编辑");
        baseMapper.updateAllById(search);
        return search;
    }

    /**
     * 启用禁用
     */
    @Override
    public boolean updateUsableById(Long id, Boolean usable) {
        if (id == null || usable == null) {
            return false;
        }
        Role role = getById(id);
        if (role == null || role.getUsableFlag().equals(usable)) {
            return false;
        }
        ArgumentAssert.isFalse(BizRoleConstant.BUILD_IN_ROLE_CODES.contains(role.getRoleCode()),
                "内置角色无法编辑");
        return update(
                Wraps.<Role>lbU()
                        .set(Role::getUsableFlag, usable)
                        .set(Role::getCreateUserId, ContextUtils.getUserId())
                        .set(Role::getCreateTime, LocalDateTime.now())
                        .eq(Role::getId, id)
                        .eq(Role::getUsableFlag, !usable)
                        .eq(Role::getDeleteFlag, false)
        );
    }

    /**
     * 删除
     */
    @Override
    public boolean removeById(Long id) {
        if (id == null) {
            return false;
        }
        return update(
                Wraps.<Role>lbU()
                        .set(Role::getDeleteFlag, true)
                        .set(Role::getCreateUserId, ContextUtils.getUserId())
                        .set(Role::getCreateTime, LocalDateTime.now())
                        .eq(Role::getId, id)
                        .eq(Role::getDeleteFlag, false)
        );
    }
}
