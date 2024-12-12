package com.neyogoo.example.biz.toolbox.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.neyogoo.example.biz.common.enumeration.toolbox.DictTypeEnum;
import com.neyogoo.example.biz.toolbox.constant.ToolboxCacheConstant;
import com.neyogoo.example.biz.toolbox.dao.DictMapper;
import com.neyogoo.example.biz.toolbox.model.Dict;
import com.neyogoo.example.biz.toolbox.service.DictService;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictMapReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictPageReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictSaveReqVO;
import com.neyogoo.example.biz.toolbox.vo.request.dict.DictUpdateReqVO;
import com.neyogoo.example.biz.toolbox.vo.response.dict.DictMapRespVO;
import com.neyogoo.example.common.cache.model.CacheKey;
import com.neyogoo.example.common.cache.repository.CacheOps;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.model.PageReq;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.EntityUtils;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import com.neyogoo.example.common.database.mybatis.conditions.query.LbQueryWrap;
import com.neyogoo.example.common.database.util.SqlPageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典
 */
@Slf4j
@Service
public class DictServiceImpl extends SuperServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private CacheOps cacheOps;

    /**
     * 根据id查询字典信息
     */
    @Override
    public Dict getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Dict>lbQ()
                        .eq(Dict::getId, id)
                        .eq(Dict::getDeleteFlag, false)
        );
    }

    /**
     * 根据类型查询字典列表
     */
    @Override
    public List<Dict> listByType(DictTypeEnum dictType) {
        if (dictType == null) {
            return Collections.emptyList();
        }
        return listWithCache(dictType);
    }

    /**
     * 根据类型和编码查询字典
     */
    @Override
    public Dict getByTypeAndCode(DictTypeEnum dictType, String dictCode) {
        if (dictType == null || StringUtils.isBlank(dictCode)) {
            return null;
        }
        return listWithCache(dictType).stream().filter(o -> o.getDictCode().equals(dictCode))
                .findFirst().orElse(null);
    }

    /**
     * 根据类型和是否启用查询字典map
     */
    @Override
    public Map<String, List<DictMapRespVO>> mapByTypes(DictMapReqVO reqVO) {
        List<Dict> dicts = baseMapper.selectList(
                Wraps.<Dict>lbQ()
                        .in(CollectionUtil.isNotEmpty(reqVO.getDictTypes()), Dict::getDictType, reqVO.getDictTypes())
                        .eq(reqVO.getUsableFlag() != null, Dict::getUsableFlag, reqVO.getUsableFlag())
                        .eq(Dict::getDeleteFlag, false)
        );
        return EntityUtils.groupingBy(dicts, o -> o.getDictType().getCode(), DictMapRespVO::fromModel);
    }

    /**
     * 根据类型查询启用字典map
     */
    @Override
    public Map<String, Map<String, String>> mapUsableByTypes(List<String> dictTypes) {
        List<Dict> dicts = baseMapper.selectList(
                Wraps.<Dict>lbQ()
                        .in(CollectionUtil.isNotEmpty(dictTypes), Dict::getDictType, dictTypes)
                        .eq(Dict::getUsableFlag, true)
                        .eq(Dict::getDeleteFlag, false)
        );
        return dicts.stream().collect(Collectors.groupingBy(
                o -> o.getDictType().getCode(),
                Collectors.collectingAndThen(
                        Collectors.toList(),
                        l -> l.stream().collect(Collectors.toMap(Dict::getDictCode, Dict::getDictName))
                )
        ));
    }

    /**
     * 分页查询字典信息
     */
    @Override
    public IPage<Dict> findPage(PageReq<DictPageReqVO> pageReq) {
        LbQueryWrap<Dict> wrap = Wraps.lbQ();
        if (pageReq.getModel().getDictType() != null) {
            wrap.eq(Dict::getDictType, pageReq.getModel().getDictType().getCode());
        }
        wrap.eq(Dict::getDeleteFlag, false)
                .orderByAsc(Dict::getDictType, Dict::getSort);
        return baseMapper.selectPage(SqlPageUtils.buildPage(pageReq, Dict.class), wrap);
    }

    /**
     * 新增字典信息
     */
    @Override
    public Dict save(DictSaveReqVO saveReqVO) {
        Dict search = getByTypeAndCode(saveReqVO.getDictType(), saveReqVO.getDictCode());
        ArgumentAssert.isNull(search, "该类型下字典编码已存在");
        Dict model = saveReqVO.toModel();
        baseMapper.insert(model);
        removeFromCache(model.getDictType());
        return model;
    }

    /**
     * 更新字典信息
     */
    @Override
    public Dict update(DictUpdateReqVO updateReqVO) {
        Dict model = getById(updateReqVO.getId());
        ArgumentAssert.notNull(model, "该字典不存在");
        ArgumentAssert.isFalse(model.getReadonlyFlag(), "该字典只读，不可编辑");

        Dict search = getByTypeAndCode(model.getDictType(), updateReqVO.getDictCode());
        if (search != null) {
            ArgumentAssert.equals(search.getId(), updateReqVO.getId(), "该类型下字典编码已存在");
        }
        updateReqVO.toModel(model);
        baseMapper.updateAllById(model);
        removeFromCache(model.getDictType());
        return search;
    }

    /**
     * 根据id启用禁用字典
     */
    @Override
    public boolean updateUsableById(Long id, Boolean usableFlag) {
        if (id == null || usableFlag == null) {
            return false;
        }
        Dict dict = getById(id);
        if (dict.getUsableFlag().equals(usableFlag)) {
            return true;
        }
        ArgumentAssert.notNull(dict, "该字典不存在");
        ArgumentAssert.isFalse(dict.getReadonlyFlag(), "该字典只读，不可编辑");
        dict.setUsableFlag(usableFlag).setUpdateUserId(ContextUtils.getUserId()).setUpdateTime(LocalDateTime.now());
        updateById(dict);
        removeFromCache(dict.getDictType());
        return true;
    }

    /**
     * 根据id删除字典信息
     */
    @Override
    public boolean removeById(Long id) {
        if (id == null) {
            return false;
        }
        Dict dict = getById(id);
        if (dict == null) {
            return true;
        }
        ArgumentAssert.isFalse(dict.getReadonlyFlag(), "该字典只读，不可编辑");
        dict.setDeleteFlag(true).setUpdateUserId(ContextUtils.getUserId()).setUpdateTime(LocalDateTime.now());
        updateById(dict);
        removeFromCache(dict.getDictType());
        return true;
    }

    /**
     * 加载缓存
     */
    private List<Dict> listWithCache(DictTypeEnum dictType) {
        CacheKey cacheKey = new CacheKey(
                ToolboxCacheConstant.DICT_TYPE_LIST + dictType.getCode(),
                Duration.ofHours(12L)
        );
        return cacheOps.get(cacheKey, k -> baseMapper.selectList(
                Wraps.<Dict>lbQ()
                        .eq(Dict::getDictType, dictType)
                        .eq(Dict::getDeleteFlag, false)
        ), false);
    }

    /**
     * 删除缓存
     */
    private void removeFromCache(DictTypeEnum dictType) {
        cacheOps.del(ToolboxCacheConstant.DICT_TYPE_LIST + dictType.getCode());
    }
}
