package com.ntt.basebusiness.shared

import com.ntt.basebusiness.shared.vo.MenuItemVO
import com.ntt.basebusiness.shared.vo.UserBasicVO

/**
 * Reads basic user information from the auth database.
 * Single-Tenant: no domainId parameter.
 */
interface IUserReader {
    fun getUserById(userId: Long): UserBasicVO?
    fun getUserByUsername(username: String): UserBasicVO?
    fun getUsersByIds(userIds: List<Long>): List<UserBasicVO>
}

/**
 * Checks user permissions from the auth RBAC model.
 */
interface IPermissionChecker {
    /**
     * Check if a user has a specific permission code.
     */
    fun hasPermission(userId: Long, permissionCode: String): Boolean

    /**
     * Get all permission codes granted to a user (via roles + groups).
     */
    fun getUserPermissions(userId: Long): Set<String>

    /**
     * Get all role codes assigned to a user.
     */
    fun getUserRoles(userId: Long): Set<String>
}

/**
 * Composite resolver that merges auth roles/permissions with sysadmin menus/feature-flags
 * to produce a permission-filtered menu tree for a specific user.
 *
 * Implementation should be placed at consumer service layer (e.g., account-service)
 * to avoid circular dependency between sysadmin-client and auth-client.
 */
interface IMenuPermissionResolver {
    /**
     * Build the accessible menu tree for a specific user.
     * Merges: menu items + feature flags + user roles + user menu overrides.
     *
     * @param userId the user to resolve menus for
     * @return filtered list of menu items the user can access
     */
    fun resolveMenusForUser(userId: Long): List<MenuItemVO>
}
