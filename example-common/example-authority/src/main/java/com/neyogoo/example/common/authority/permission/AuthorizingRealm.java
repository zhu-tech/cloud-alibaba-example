package com.neyogoo.example.common.authority.permission;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class AuthorizingRealm {

    /**
     * 验证是否拥有指定权限
     *
     * @param perms         拥有的权限集
     * @param permission    待验证权限
     * @param caseSensitive 是否区分大小写
     */
    public static boolean isPermitted(Collection<WildcardPermission> perms, String permission, boolean caseSensitive) {
        return isPermitted(perms, new WildcardPermission(permission, caseSensitive));
    }

    /**
     * 验证是否拥有指定权限
     *
     * @param perms      拥有的权限集
     * @param permission 待验证权限
     */
    private static boolean isPermitted(Collection<WildcardPermission> perms, WildcardPermission permission) {
        if (perms == null || perms.isEmpty()) {
            return false;
        }
        return perms.parallelStream().anyMatch(perm -> perm.implies(permission));
    }

    /**
     * 验证是否拥有所有指定权限
     *
     * @param allPermissions 拥有的权限集
     * @param permissions    待验证权限
     * @param caseSensitive  是否区分大小写
     */
    public static boolean hasPermission(Collection<String> allPermissions, String[] permissions,
                                        boolean caseSensitive) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        Collection<WildcardPermission> allPerms = allPermissions.stream()
                .map(perm -> new WildcardPermission(perm, caseSensitive))
                .collect(Collectors.toList());
        return Arrays.stream(permissions).allMatch(perm -> isPermitted(allPerms, perm, caseSensitive));
    }

    /**
     * 验证是否拥有任意一个指定权限
     *
     * @param allPermissions 拥有的权限集
     * @param permissions    待验证权限
     * @param caseSensitive  是否区分大小写
     */
    public static boolean hasAnyPermission(Collection<String> allPermissions, String[] permissions,
                                           boolean caseSensitive) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        Collection<WildcardPermission> allPerms = allPermissions.stream()
                .map(perm -> new WildcardPermission(perm, caseSensitive))
                .collect(Collectors.toList());
        return Arrays.stream(permissions).anyMatch(perm -> isPermitted(allPerms, perm, caseSensitive));
    }
}
