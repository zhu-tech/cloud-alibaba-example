package com.neyogoo.example.biz.toolbox.service;

import com.neyogoo.example.biz.toolbox.model.Area;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 地区
 */
public interface AreaService extends SuperService<Area> {

    /**
     * 根据编码查询
     */
    Area getByCode(String areaCode);

    /**
     * 根据上级id查询子级列表
     */
    List<Area> listByParentId(Long parentId);

    /**
     * 根据等级查询
     */
    List<Area> listByLevelValue(Integer levelValue);

    /**
     * 根据编码列表查询编码和名称对应关系
     */
    Map<String, String> getAreaNamesByCodes(Collection<String> areaCodes);

    /**
     * 根据编码查询全名
     */
    String getFullNameByCode(String areaCode);

    /**
     * 根据编码列表查询编码和全名对应关系
     */
    Map<String, String> getFullNamesByCodes(Collection<String> areaCodes);
}
