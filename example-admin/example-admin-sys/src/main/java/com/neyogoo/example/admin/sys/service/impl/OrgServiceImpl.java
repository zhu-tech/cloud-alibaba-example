package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.neyogoo.example.admin.sys.dao.OrgMapper;
import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.admin.sys.service.OrgService;
import com.neyogoo.example.admin.sys.vo.response.org.OrgSimpleRespVO;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.DateUtils;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
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
public class OrgServiceImpl extends SuperServiceImpl<OrgMapper, Org> implements OrgService {

    /**
     * 根据id查询
     */
    @Override
    public Org getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Org>lbQ()
                        .eq(Org::getId, id)
                        .eq(Org::getDeleteFlag, false)
        );
    }

    /**
     * 根据id查询名称
     */
    @Override
    public String getNameById(Long id) {
        if (id == null) {
            return null;
        }
        Org model = baseMapper.selectOne(
                Wraps.<Org>lbQ()
                        .select(Org::getOrgName)
                        .eq(Org::getId, id)
        );
        return model == null ? null : model.getOrgName();
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
                Wraps.<Org>lbQ()
                        .select(Org::getId, Org::getOrgName)
                        .in(Org::getId, ids)
        ).stream().collect(Collectors.toMap(Org::getId, Org::getOrgName));
    }

    /**
     * 列表
     */
    @Override
    public List<Org> list() {
        return baseMapper.selectList(
                Wraps.<Org>lbQ()
                        .eq(Org::getDeleteFlag, false)
        );
    }

    /**
     * 可用id列表
     */
    @Override
    public List<Long> listUsableIds() {
        return baseMapper.selectList(
                Wraps.<Org>lbQ()
                        .select(Org::getId)
                        .eq(Org::getUsableFlag, true)
                        .eq(Org::getDeleteFlag, false)
        ).stream().map(Org::getId).collect(Collectors.toList());
    }

    /**
     * 直属子机构id
     */
    @Override
    public List<Long> listChildrenIds(Long orgId) {
        if (orgId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Org>lbQ()
                        .select(Org::getId)
                        .like(Org::getParentIds, StrPool.COMMA + orgId + StrPool.COMMA)
                        .eq(Org::getDeleteFlag, false)
        ).stream().map(Org::getId).collect(Collectors.toList());
    }

    /**
     * 可用机构简易信息列表
     */
    @Override
    public List<OrgSimpleRespVO> listUsableSimple(Collection<Long> orgIds) {
        List<Org> models = baseMapper.selectList(
                Wraps.<Org>lbQ()
                        .select(
                                Org::getId,
                                Org::getOrgName,
                                Org::getOrgLevel,
                                Org::getOrgCategory
                        )
                        .in(CollUtil.isNotEmpty(orgIds), Org::getId, orgIds)
                        .eq(Org::getUsableFlag, true)
                        .eq(Org::getDeleteFlag, false)
        );
        return OrgSimpleRespVO.fromModels(models);
    }

    /**
     * 根据机构id查询下面的子机构id（含自身）
     */
    @Override
    public List<Long> listChildrenIdsByParentId(Long parentId) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Org>lbQ()
                        .select(Org::getId)
                        .like(Org::getParentIds, StrPool.COMMA + parentId + StrPool.COMMA)
                        .or()
                        .eq(Org::getId, parentId)
        ).stream().map(Org::getId).collect(Collectors.toList());
    }

    /**
     * 根据机构id查询下面的子机构（含自身）
     */
    @Override
    public Map<Long, String> mapChildrenIdNameByParentId(Long parentId, String orgName) {
        if (parentId == null) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(
                Wraps.<Org>lbQ()
                        .select(Org::getId, Org::getOrgName)
                        .like(StringUtils.isNotBlank(orgName), Org::getOrgName, orgName)
                        .like(Org::getParentId, StrPool.COMMA + parentId + StrPool.COMMA)
                        .or()
                        .eq(Org::getId, parentId)
                        .eq(Org::getDeleteFlag, false)
        ).stream().collect(Collectors.toMap(Org::getId, Org::getOrgName));
    }

    /**
     * 查询机构编码是否已存在
     */
    @Override
    public boolean queryIsOrgCodeExists(String orgCode) {
        if (StringUtils.isBlank(orgCode)) {
            return false;
        }
        return baseMapper.selectOne(
                Wraps.<Org>lbQ()
                        .select(Org::getId)
                        .eq(Org::getOrgCode, orgCode)
                        .last("limit 1")

        ) != null;
    }

    /**
     * 查询机构名称是否已存在
     */
    @Override
    public boolean queryIsOrgNameExists(String orgName, Long excludeOrgId) {
        if (StringUtils.isBlank(orgName)) {
            return false;
        }
        return baseMapper.selectOne(
                Wraps.<Org>lbQ()
                        .select(Org::getId)
                        .ne(Org::getId, excludeOrgId)
                        .eq(Org::getOrgName, orgName)
                        .eq(Org::getDeleteFlag, false)
                        .last("limit 1")

        ) != null;
    }

    /**
     * 查询是否存在启用的下级
     */
    @Override
    public boolean queryIsChildrenUsable(Long parentId) {
        if (parentId == null) {
            return false;
        }
        return baseMapper.selectOne(
                Wraps.<Org>lbQ()
                        .select(Org::getId)
                        .eq(Org::getParentId, parentId)
                        .eq(Org::getUsableFlag, true)
                        .eq(Org::getDeleteFlag, false)
                        .last("limit 1")
        ) != null;
    }

    /**
     * 查询是否存在下级
     */
    @Override
    public boolean queryIsChildrenExists(Long parentId) {
        if (parentId == null) {
            return false;
        }
        return baseMapper.selectOne(
                Wraps.<Org>lbQ()
                        .select(Org::getId)
                        .eq(Org::getParentId, parentId)
                        .eq(Org::getDeleteFlag, false)
                        .last("limit 1")
        ) != null;
    }

    /**
     * 生成机构编码
     */
    @Override
    public String generateOrgCode() {
        long num = count() + 1;
        return DateUtils.format(LocalDate.now(), DateUtils.DEFAULT_YEARMONTHDAY_FORMAT) + String.format("%05d", num);
    }

    /**
     * 根据id启用禁用
     */
    @Override
    public boolean updateUsableById(Long id, Boolean usableFlag) {
        if (id == null || usableFlag == null) {
            return false;
        }
        return update(
                Wraps.<Org>lbU()
                        .set(Org::getUsableFlag, usableFlag)
                        .set(Org::getCreateUserId, ContextUtils.getUserId())
                        .set(Org::getCreateTime, LocalDateTime.now())
                        .eq(Org::getId, id)
                        .eq(Org::getUsableFlag, !usableFlag)
                        .eq(Org::getDeleteFlag, false)
        );
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
                Wraps.<Org>lbU()
                        .set(Org::getDeleteFlag, true)
                        .set(Org::getCreateUserId, ContextUtils.getUserId())
                        .set(Org::getCreateTime, LocalDateTime.now())
                        .eq(Org::getId, id)
                        .eq(Org::getDeleteFlag, false)
        );
    }
}
