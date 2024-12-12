package com.neyogoo.example.admin.sys.service;

import com.neyogoo.example.admin.sys.model.Menu;
import com.neyogoo.example.admin.sys.vo.request.permission.MenuSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.MenuUpdateReqVO;
import com.neyogoo.example.common.database.mvc.service.SuperService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 菜单
 */
public interface MenuService extends SuperService<Menu> {

    /**
     * 根据编码查询
     */
    Menu getByCode(String roleCode);

    /**
     * 根据上级id查询级联子菜单
     */
    List<Menu> listChildrenByParentId(Long parentId);

    /**
     * 查询可用列表
     */
    List<Menu> listUsable();

    /**
     * 根据id列表查询可用的菜单信息
     */
    List<Menu> listUsableByIds(Collection<Long> ids);

    /**
     * 查询菜单id和名称
     */
    Map<Long, String> findIdNameMap();

    /**
     * 新增
     */
    Menu save(MenuSaveReqVO saveReqVO);

    /**
     * 修改
     */
    Menu update(MenuUpdateReqVO updateReqVO);

    /**
     * 根据id启用禁用
     */
    boolean updateUsableById(Long id, Boolean usableFlag);

    /**
     * 根据id删除（级联删除下级菜单）
     */
    List<Long> removeByIdWithChild(Long id);
}
