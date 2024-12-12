package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.admin.sys.dao.DeptMapper;
import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.admin.sys.service.DeptService;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptUpdateReqVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机构
 */
@Slf4j
@Service
public class DeptServiceImpl extends SuperServiceImpl<DeptMapper, Dept> implements DeptService {

    /**
     * 根据id查询
     */
    @Override
    public Dept getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Dept>lbQ()
                        .eq(Dept::getId, id)
                        .eq(Dept::getDeleteFlag, false)
        );
    }

    /**
     * 根据机构id查询
     */
    @Override
    public List<Dept> listByOrgId(Long orgId) {
        if (orgId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Dept>lbQ()
                        .eq(Dept::getOrgId, orgId)
                        .eq(Dept::getDeleteFlag, false)
        );
    }

    /**
     * 根据机构id列表查询（key：机构id，value：部门列表）
     */
    @Override
    public Map<Long, List<Dept>> mapByOrgIds(Collection<Long> orgIds) {
        if (CollectionUtil.isEmpty(orgIds)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(
                Wraps.<Dept>lbQ()
                        .select(Dept::getId, Dept::getOrgId, Dept::getDeptName)
                        .in(Dept::getOrgId, orgIds)
                        .eq(Dept::getDeleteFlag, false)
        ).stream().collect(Collectors.groupingBy(Dept::getOrgId));
    }

    /**
     * 根据机构id查询部门id列表
     */
    @Override
    public List<Long> listIdsByOrgId(Long orgId) {
        if (orgId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Dept>lbQ()
                        .select(Dept::getId)
                        .eq(Dept::getOrgId, orgId)
                        .eq(Dept::getDeleteFlag, false)
        ).stream().map(Dept::getId).collect(Collectors.toList());
    }

    /**
     * 根据id列表查询名称
     */
    @Override
    public Map<Long, String> mapNamesByIds(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(
                Wraps.<Dept>lbQ()
                        .select(Dept::getId, Dept::getDeptName)
                        .in(Dept::getId, ids)
        ).stream().collect(Collectors.toMap(Dept::getId, Dept::getDeptName));
    }

    /**
     * 根据机构id查询名称
     */
    @Override
    public Map<Long, String> mapNamesByOrgId(Long orgId) {
        if (orgId == null) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(
                Wraps.<Dept>lbQ()
                        .select(Dept::getId, Dept::getDeptName)
                        .eq(Dept::getOrgId, orgId)
        ).stream().collect(Collectors.toMap(Dept::getId, Dept::getDeptName));
    }

    /**
     * 新增
     */
    @Override
    public Long save(DeptSaveReqVO reqVO) {
        ArgumentAssert.isFalse(queryIsNameExists(reqVO.getOrgId(), reqVO.getDeptName(), null),
                "该机构名称已存在");
        Dept dept = reqVO.toModel();
        baseMapper.insert(dept);
        return dept.getId();
    }

    /**
     * 修改
     */
    @Override
    public boolean update(DeptUpdateReqVO reqVO) {
        ArgumentAssert.isFalse(queryIsNameExists(reqVO.getOrgId(), reqVO.getDeptName(), reqVO.getId()),
                "该机构名称已存在");
        Dept model = getById(reqVO.getId());
        ArgumentAssert.notNull(model, "未查询到该机构信息");
        reqVO.toModel(model);
        return baseMapper.updateById(model) > 0;
    }

    /**
     * 根据id删除
     */
    @Override
    public boolean removeById(Serializable id) {
        if (id == null) {
            return false;
        }
        return update(
                Wraps.<Dept>lbU()
                        .set(Dept::getDeleteFlag, true)
                        .set(Dept::getCreateUserId, ContextUtils.getUserId())
                        .set(Dept::getCreateTime, LocalDateTime.now())
                        .eq(Dept::getId, id)
                        .eq(Dept::getDeleteFlag, false)
        );
    }

    /**
     * 查询部门名称是否已存在
     */
    private boolean queryIsNameExists(Long orgId, String deptName, Long deptId) {
        if (orgId == null || StringUtils.isBlank(deptName) || deptId == null) {
            return false;
        }
        return baseMapper.selectOne(
                Wraps.<Dept>lbQ()
                        .select(Dept::getDeptName)
                        .eq(Dept::getOrgId, orgId)
                        .eq(Dept::getDeptName, deptName)
                        .ne(Dept::getId, deptId)
                        .eq(Dept::getDeleteFlag, false)
        ) != null;
    }
}
