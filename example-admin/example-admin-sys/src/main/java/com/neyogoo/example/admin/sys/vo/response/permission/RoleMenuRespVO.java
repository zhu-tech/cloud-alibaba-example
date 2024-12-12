package com.neyogoo.example.admin.sys.vo.response.permission;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neyogoo.example.admin.sys.model.Menu;
import com.neyogoo.example.admin.sys.model.Resource;
import com.neyogoo.example.common.core.constant.BasicConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "角色关联权限信息")
public class RoleMenuRespVO {

    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty("菜单编码")
    private String menuCode;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty("菜单路径")
    private String menuHref;

    @ApiModelProperty("是否展示")
    private Boolean showFlag;

    @ApiModelProperty("子菜单")
    private List<RoleMenuRespVO> children = new ArrayList<>();

    @ApiModelProperty("是否可用")
    private Boolean usableFlag;

    @ApiModelProperty("资源列表")
    private List<ResourceData> resources = new ArrayList<>();

    @ApiModelProperty("排序")
    @JsonIgnore
    private Integer sort;

    /**
     * 根据菜单列表和资源列表转换
     */
    public static List<RoleMenuRespVO> fromPermission(List<Menu> menus, List<Resource> resources) {
        if (CollUtil.isEmpty(menus)) {
            return Collections.emptyList();
        }
        List<RoleMenuRespVO> topMenus = new ArrayList<>();
        Map<Long, RoleMenuRespVO> menuMap = new HashMap<>();
        for (Menu menu : menus) {
            RoleMenuRespVO menuVO = RoleMenuRespVO.fromModel(menu);
            menuMap.put(menuVO.getMenuId(), menuVO);
            if (BasicConstant.DEFAULT_ID.equals(menu.getParentId())) {
                topMenus.add(menuVO);
            } else {
                RoleMenuRespVO parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(menuVO);
                }
            }
            menuVO.setResources(
                    resources.stream()
                            .filter(o -> o.getMenuId().equals(menu.getId()))
                            .map(RoleMenuRespVO.ResourceData::fromModel)
                            .collect(Collectors.toList())
            );
        }
        topMenus.sort(Comparator.comparing(RoleMenuRespVO::getSort));
        topMenus.forEach(RoleMenuRespVO::sortChildren);
        return topMenus;
    }

    /**
     * 根据菜单移除权限
     */
    public static void removeLikeMenuHref(List<RoleMenuRespVO> list, Set<String> menuHrefs) {
        if (CollUtil.isEmpty(list) || CollUtil.isEmpty(menuHrefs)) {
            return;
        }
        Iterator<RoleMenuRespVO> iterator = list.iterator();
        while (iterator.hasNext()) {
            RoleMenuRespVO next = iterator.next();
            if (menuHrefs.stream().anyMatch(menuHref -> next.menuHref.contains(menuHref))) {
                iterator.remove();
            } else if (CollUtil.isNotEmpty(next.children)) {
                removeLikeMenuHref(next.children, menuHrefs);
            }
        }
    }

    /**
     * 根据菜单转换
     */
    public static RoleMenuRespVO fromModel(Menu menu) {
        RoleMenuRespVO vo = new RoleMenuRespVO();
        BeanUtil.copyProperties(menu, vo);
        vo.setMenuId(menu.getId());
        return vo;
    }

    public List<ResourceData> collectResources() {
        List<ResourceData> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(this.resources)) {
            list.addAll(this.resources);
        }
        if (CollUtil.isNotEmpty(this.children)) {
            for (RoleMenuRespVO child : this.children) {
                list.addAll(child.collectResources());
            }
        }
        return list;
    }

    public void clearResources() {
        this.resources = Collections.emptyList();
        if (CollUtil.isNotEmpty(this.children)) {
            this.children.forEach(RoleMenuRespVO::clearResources);
        }
    }

    public void sortChildren() {
        if (CollUtil.isEmpty(this.children)) {
            return;
        }
        this.children.sort(Comparator.comparing(RoleMenuRespVO::getSort));
        this.children.forEach(RoleMenuRespVO::sortChildren);
    }

    @Data
    public static class ResourceData {

        @ApiModelProperty("资源编码")
        private String resourceCode;

        @ApiModelProperty("资源id")
        private Long resourceId;

        @ApiModelProperty("资源名称")
        private String resourceName;

        @ApiModelProperty("是否可用")
        private Boolean usableFlag;

        public static ResourceData fromModel(Resource resource) {
            ResourceData vo = new ResourceData();
            BeanUtil.copyProperties(resource, vo);
            vo.setResourceId(resource.getId());
            return vo;
        }
    }
}
