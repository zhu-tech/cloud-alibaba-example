package com.neyogoo.example.biz.toolbox.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.neyogoo.example.biz.toolbox.dao.AreaMapper;
import com.neyogoo.example.biz.toolbox.model.Area;
import com.neyogoo.example.biz.toolbox.service.AreaService;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 地区
 */
@Slf4j
@Service
public class AreaServiceImpl extends SuperServiceImpl<AreaMapper, Area> implements AreaService {

    /**
     * 根据编码查询
     */
    @Override
    public Area getByCode(String areaCode) {
        if (StringUtils.isBlank(areaCode)) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Area>lbQ()
                        .eq(Area::getAreaCode, areaCode)
                        .eq(Area::getDeleteFlag, false)
        );
    }

    /**
     * 根据上级id查询子级列表
     */
    @Override
    public List<Area> listByParentId(Long parentId) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Area>lbQ()
                        .eq(Area::getParentId, parentId)
                        .eq(Area::getDeleteFlag, false)
        );
    }

    /**
     * 根据等级查询
     */
    @Override
    public List<Area> listByLevelValue(Integer levelValue) {
        if (levelValue == null) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Area>lbQ()
                        .eq(Area::getLevelValue, levelValue)
                        .eq(Area::getDeleteFlag, false)
        );
    }

    /**
     * 根据编码列表查询编码和名称对应关系
     */
    @Override
    public Map<String, String> getAreaNamesByCodes(Collection<String> areaCodes) {
        if (CollectionUtil.isEmpty(areaCodes)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(
                Wraps.<Area>lbQ()
                        .select(Area::getAreaCode, Area::getAreaName)
                        .in(Area::getAreaCode, areaCodes)
                        .eq(Area::getDeleteFlag, false)
        ).stream().collect(Collectors.toMap(Area::getAreaCode, Area::getAreaName));
    }

    /**
     * 根据编码查询全名
     */
    @Override
    public String getFullNameByCode(String areaCode) {
        if (StringUtils.isBlank(areaCode)) {
            return null;
        }
        Area model = baseMapper.selectOne(
                Wraps.<Area>lbQ()
                        .select(Area::getFullName)
                        .eq(Area::getAreaCode, areaCode)
                        .eq(Area::getDeleteFlag, false)
        );
        return model == null ? null : model.getFullName();
    }

    /**
     * 根据编码列表查询编码和全名对应关系
     */
    @Override
    public Map<String, String> getFullNamesByCodes(Collection<String> areaCodes) {
        if (CollectionUtil.isEmpty(areaCodes)) {
            return Collections.emptyMap();
        }
        return baseMapper.selectList(
                Wraps.<Area>lbQ()
                        .select(Area::getAreaCode, Area::getFullName)
                        .in(Area::getAreaCode, areaCodes)
                        .eq(Area::getDeleteFlag, false)
        ).stream().collect(Collectors.toMap(Area::getAreaCode, Area::getFullName));
    }
}
