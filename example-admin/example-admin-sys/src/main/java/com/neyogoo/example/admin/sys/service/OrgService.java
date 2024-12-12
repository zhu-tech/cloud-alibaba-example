package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.Org;
import com.neyogoo.example.admin.sys.vo.response.org.OrgSimpleRespVO;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 机构
 */
public interface OrgService extends SuperService<Org> {

    /**
     * 根据id查询名称
     */
    String getNameById(Long id);

    /**
     * 根据id列表查询名称
     */
    Map<Long, String> mapNamesByIds(Collection<Long> ids);

    /**
     * 可用id列表
     */
    List<Long> listUsableIds();

    /**
     * 直属子机构id
     */
    List<Long> listChildrenIds(Long orgId);

    /**
     * 可用机构简易信息列表
     */
    List<OrgSimpleRespVO> listUsableSimple(Collection<Long> orgIds);

    /**
     * 根据机构id查询下面的子机构id（含自身）
     */
    List<Long> listChildrenIdsByParentId(Long parentId);

    /**
     * 根据机构id和机构名称查询下面的子机构（含自身）
     */
    Map<Long, String> mapChildrenIdNameByParentId(Long parentId, String orgName);

    /**
     * 查询机构编码是否已存在
     */
    boolean queryIsOrgCodeExists(String orgCode);

    /**
     * 查询机构名称是否已存在
     */
    boolean queryIsOrgNameExists(String orgName, Long excludeOrgId);

    /**
     * 查询是否存在启用的下级
     */
    boolean queryIsChildrenUsable(Long parentId);

    /**
     * 查询是否存在下级
     */
    boolean queryIsChildrenExists(Long parentId);

    /**
     * 生成机构编码
     */
    String generateOrgCode();

    /**
     * 根据id启用禁用
     */
    boolean updateUsableById(Long id, Boolean usableFlag);
}
