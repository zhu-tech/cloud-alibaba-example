package com.neyogoo.example.admin.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.neyogoo.example.admin.sys.dao.MenuMapper;
import com.neyogoo.example.admin.sys.model.Menu;
import com.neyogoo.example.admin.sys.service.MenuService;
import com.neyogoo.example.admin.sys.vo.request.permission.MenuSaveReqVO;
import com.neyogoo.example.admin.sys.vo.request.permission.MenuUpdateReqVO;
import com.neyogoo.example.common.core.constant.BasicConstant;
import com.neyogoo.example.common.core.context.ContextUtils;
import com.neyogoo.example.common.core.util.ArgumentAssert;
import com.neyogoo.example.common.core.util.StrPool;
import com.neyogoo.example.common.database.mvc.service.impl.SuperServiceImpl;
import com.neyogoo.example.common.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单
 */
@Slf4j
@Service
public class MenuServiceImpl extends SuperServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
     * 根据id查询
     */
    @Override
    public Menu getById(Serializable id) {
        if (id == null) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Menu>lbQ()
                        .eq(Menu::getId, id)
                        .eq(Menu::getDeleteFlag, false)
        );
    }

    /**
     * 根据编码查询
     */
    @Override
    public Menu getByCode(String roleCode) {
        if (CharSequenceUtil.isBlank(roleCode)) {
            return null;
        }
        return baseMapper.selectOne(
                Wraps.<Menu>lbQ()
                        .eq(Menu::getMenuCode, roleCode)
                        .eq(Menu::getDeleteFlag, false)
        );
    }

    /**
     * 列表
     */
    @Override
    public List<Menu> list() {
        return baseMapper.selectList(
                Wraps.<Menu>lbQ()
                        .eq(Menu::getDeleteFlag, false)
        );
    }

    /**
     * 根据上级id查询级联子类
     */
    @Override
    public List<Menu> listChildrenByParentId(Long parentId) {
        if (parentId == null) {
            return Collections.emptyList();
        }
        String parentIdStr = StrPool.COMMA + parentId + StrPool.COMMA;
        return list().stream().filter(o -> o.getParentIds().contains(parentIdStr))
                .collect(Collectors.toList());
    }

    /**
     * 查询可用列表
     */
    @Override
    public List<Menu> listUsable() {
        return baseMapper.selectList(
                Wraps.<Menu>lbQ()
                        .eq(Menu::getUsableFlag, true)
                        .eq(Menu::getDeleteFlag, false)
                        .orderByAsc(Menu::getLevelValue, Menu::getSort)
        );
    }

    /**
     * 根据id列表查询可用的菜单信息
     */
    @Override
    public List<Menu> listUsableByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return baseMapper.selectList(
                Wraps.<Menu>lbQ()
                        .in(Menu::getId, ids)
                        .eq(Menu::getUsableFlag, true)
                        .eq(Menu::getDeleteFlag, false)
                        .orderByAsc(Menu::getLevelValue, Menu::getSort)
        );
    }

    /**
     * 查询菜单id和名称
     */
    @Override
    public Map<Long, String> findIdNameMap() {
        return baseMapper.selectList(
                Wraps.<Menu>lbQ()
                        .select(Menu::getId, Menu::getMenuName)
                        .eq(Menu::getDeleteFlag, false)
                        .orderByAsc(Menu::getLevelValue, Menu::getSort)
        ).stream().collect(Collectors.toMap(Menu::getId, Menu::getMenuName));
    }

    /**
     * 新增
     */
    @Override
    public Menu save(MenuSaveReqVO saveReqVO) {
        Menu model = getByCode(saveReqVO.getMenuCode());
        ArgumentAssert.isNull(model, "该菜单编码已存在");
        model = saveReqVO.toModel();
        addParentInfo(model);
        baseMapper.insert(model);
        return model;
    }

    /**
     * 修改
     */
    @Override
    public Menu update(MenuUpdateReqVO updateReqVO) {
        Menu search = getByCode(updateReqVO.getMenuCode());
        if (search != null) {
            ArgumentAssert.equals(search.getId(), updateReqVO.getId(), "该菜单编码已存在");
        } else {
            search = getById(updateReqVO.getId());
            ArgumentAssert.notNull(search, "该菜单不存在");
            ArgumentAssert.isFalse(search.getDeleteFlag(), "该菜单不存在");
        }
        updateReqVO.toModel(search);
        addParentInfo(search);
        baseMapper.updateAllById(search);
        return search;
    }

    /**
     * 根据id启用禁用
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean updateUsableById(Long id, Boolean usableFlag) {
        if (id == null || usableFlag == null) {
            return false;
        }
        Menu model = getById(id);
        if (model == null || model.getUsableFlag().equals(usableFlag)) {
            return false;
        }
        Long userId = ContextUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        model.setUsableFlag(usableFlag).setUpdateUserId(userId).setUpdateTime(now);
        boolean flag = updateById(model);

        // 级联禁用下级菜单
        if (flag && !usableFlag) {
            List<Menu> children = listChildrenByParentId(model.getId());
            for (Menu child : children) {
                child.setUsableFlag(false).setUpdateUserId(userId).setUpdateTime(now);
            }
            updateBatchById(children);
        }
        return flag;
    }

    /**
     * 根据id删除（级联删除下级菜单）
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<Long> removeByIdWithChild(Long id) {
        if (id == null) {
            return Collections.emptyList();
        }
        Menu model = getById(id);
        if (model == null) {
            return Collections.emptyList();
        }
        List<Long> menuIds = new ArrayList<>();
        menuIds.add(id);

        List<Menu> children = listChildrenByParentId(id);
        children.forEach(o -> menuIds.add(o.getId()));

        removeByIds(menuIds);
        return menuIds;
    }

    /**
     * 根据id列表删除
     */
    @Override
    public boolean removeByIds(Collection<?> ids) {
        if (CollUtil.isEmpty(ids)) {
            return false;
        }
        return update(
                Wraps.<Menu>lbU()
                        .set(Menu::getDeleteFlag, true)
                        .set(Menu::getUpdateUserId, ContextUtils.getUserId())
                        .set(Menu::getUpdateTime, LocalDateTime.now())
                        .in(Menu::getId, ids)
                        .eq(Menu::getDeleteFlag, false)
        );
    }

    /**
     * 添加上级信息
     */
    private void addParentInfo(Menu menu) {
        if (BasicConstant.DEFAULT_ID.equals(menu.getParentId())) {
            menu.setLevelValue(1).setParentIds(StrPool.COMMA + BasicConstant.DEFAULT_ID_STR + StrPool.COMMA);
            return;
        }
        Menu parent = getById(menu.getParentId());
        ArgumentAssert.notNull(parent, "查询不到上级菜单信息");
        ArgumentAssert.isFalse(parent.getDeleteFlag(), "查询不到上级菜单信息");
        menu.setLevelValue(parent.getLevelValue() + 1)
                .setParentIds(parent.getParentIds() + parent.getId() + StrPool.COMMA);
    }
}
