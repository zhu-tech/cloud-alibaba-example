package com.neyogoo.example.admin.sys.service.ext;

import com.neyogoo.example.admin.sys.vo.response.org.OrgDeptRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgGetRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgSimpleRespVO;
import com.neyogoo.example.admin.sys.vo.response.org.OrgTreeRespVO;
import com.neyogoo.example.biz.common.enumeration.sys.OrgCategoryEnum;

import java.util.List;

/**
 * 机构查询
 */
public interface OrgQuerySerivice {

    /**
     * 根据id查询
     */
    OrgGetRespVO getById(Long id);

    /**
     * 机构简易列表
     */
    List<OrgSimpleRespVO> listSimple(OrgCategoryEnum orgCategory, Integer orgLevel);

    /**
     * 机构树
     */
    List<OrgTreeRespVO> findTree();

    /**
     * 根据类型查询
     */
    List<OrgSimpleRespVO> listByOrgCategory(OrgCategoryEnum orgCategory);

    /**
     * 查询机构（包含子机构）部门列表
     */
    List<OrgDeptRespVO> listOrgDept(String orgName);
}
