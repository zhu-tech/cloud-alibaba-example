package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.Dept;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.dept.DeptUpdateReqVO;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 部门
 */
public interface DeptService extends SuperService<Dept> {

    /**
     * 根据机构id查询
     */
    List<Dept> listByOrgId(Long orgId);

    /**
     * 根据机构id列表查询（key：机构id，value：部门列表）
     */
    Map<Long, List<Dept>> mapByOrgIds(Collection<Long> orgId);

    /**
     * 根据机构id查询部门id列表
     */
    List<Long> listIdsByOrgId(Long orgId);

    /**
     * 根据id列表查询名称
     */
    Map<Long, String> mapNamesByIds(Collection<Long> ids);

    /**
     * 根据机构id查询名称
     */
    Map<Long, String> mapNamesByOrgId(Long orgId);

    /**
     * 新增
     */
    Long save(DeptSaveReqVO reqVO);

    /**
     * 修改
     */
    boolean update(DeptUpdateReqVO reqVO);
}
