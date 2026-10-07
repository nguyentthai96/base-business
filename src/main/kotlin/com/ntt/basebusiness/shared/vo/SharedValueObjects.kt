package com.ntt.basebusiness.shared.vo

/**
 * Lightweight read-only representation of a menu item.
 * Used by consumer services to build navigation trees without
 * coupling to the sysadmin JPA entity.
 */
data class MenuItemVO(
    val id: Long,
    val parentId: Long?,
    val code: String,
    val name: String,
    val icon: String?,
    val path: String?,
    val routeName: String?,
    val component: String?,
    val menuType: String,
    val sortOrder: Int,
    val level: Int,
    val isVisible: Boolean,
    val isCacheable: Boolean,
    val translateKey: String?,
    val status: String
)

/**
 * Minimal user information for cross-service reads.
 * Does NOT include password or sensitive fields.
 */
data class UserBasicVO(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String,
    val phone: String?,
    val avatarUrl: String?,
    val status: String
)

/**
 * Feature flag state for runtime feature toggling.
 */
data class FeatureFlagVO(
    val id: Long,
    val flagKey: String,
    val enabled: Boolean,
    val rolloutPct: Int,
    val description: String?
)

/**
 * System configuration entry (key-value pair with type).
 */
data class ConfigEntryVO(
    val id: Long,
    val configKey: String,
    val configValue: String,
    val valueType: String,
    val description: String?
)
