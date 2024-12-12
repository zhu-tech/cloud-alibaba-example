package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.Role;
import com.neyogoo.example.admin.sys.vo.request.permission.RoleSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.RoleUpdateReqVO;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 角色
 */
public interface RoleService extends SuperService<Role> {

    /**
     * 根据编码查询
     */
    Role getByCode(String roleCode);

    /**
     * 根据id列表查询名称
     */
    Map<Long, String> mapNamesByIds(Collection<Long> ids);

    /**
     * 根据id列表查询编码
     */
    Map<Long, String> mapCodesByIds(Collection<Long> ids);

    /**
     * 查询id和名称
     */
    Map<Long, String> mapIdNames();

    /**
     * 可用id列表
     */
    List<Long> listUsableIds();

    /**
     * 查询可用列表
     */
    List<Role> listUsable();

    /**
     * 新增
     */
    Role save(RoleSaveReqVO reqVO);

    /**
     * 修改
     */
    Role update(RoleUpdateReqVO reqVO);

    /**
     * 启用禁用
     */
    boolean updateUsableById(Long id, Boolean usable);

    /**
     * 删除
     */
    boolean removeById(Long id);
}
