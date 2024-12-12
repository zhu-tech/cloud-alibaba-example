package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.Resource;
import com.neyogoo.example.admin.sys.vo.request.permission.ResourceSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.ResourceUpdateReqVO;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;

/**
 * 资源
 */
public interface ResourceService extends SuperService<Resource> {

    /**
     * 根据编码查询
     */
    Resource getByCode(String resourceCode);

    /**
     * 根据id列表查询可用列表
     */
    List<Resource> listUsableByIds(Collection<Long> ids);

    /**
     * 根据菜单id列表查询可用列表
     */
    List<Resource> listUsableByMenuIds(Collection<Long> menuIds);

    /**
     * 根据菜单id列表查询
     */
    List<Resource> listByMenuIds(Collection<Long> menuIds);

    /**
     * 根据菜单id列表查询资源id列表
     */
    List<Long> listIdsByMenuIds(Collection<Long> menuIds);

    /**
     * 新增
     */
    Resource save(ResourceSaveReqVO saveReqVO);

    /**
     * 修改
     */
    Resource update(ResourceUpdateReqVO updateReqVO);

    /**
     * 启用禁用
     */
    boolean updateUsableById(Long id, Boolean usableFlag);

    /**
     * 根据菜单id列表删除
     */
    List<Long> removeByMenuIds(Collection<Long> menuIds);
}
