package com.ntt.basebusiness.shared

import com.ntt.basebusiness.shared.vo.ConfigEntryVO
import com.ntt.basebusiness.shared.vo.FeatureFlagVO
import com.ntt.basebusiness.shared.vo.MenuItemVO

/**
 * Reads i18n messages from the system-admin database.
 * Single-Tenant: no domainId parameter.
 */
interface II18nReader {
    /**
     * Resolve a message by code and locale.
     * @return the localized message, or null if not found
     */
    fun getMessage(code: String, locale: String): String?

    /**
     * Resolve a message with MessageFormat parameter substitution.
     */
    fun getMessage(code: String, locale: String, args: Array<Any>): String?

    /**
     * Get all active messages for a locale.
     */
    fun getAllMessages(locale: String): Map<String, String>
}

/**
 * Reads system configuration entries.
 */
interface IConfigReader {
    fun getConfig(key: String): ConfigEntryVO?
    fun getConfigValue(key: String): String?
    fun getConfigValue(key: String, defaultValue: String): String
    fun getAllConfigs(): List<ConfigEntryVO>
}

/**
 * Reads menu items for navigation tree building.
 */
interface IMenuReader {
    fun getAllMenuItems(): List<MenuItemVO>
    fun getMenuItemsByParentId(parentId: Long?): List<MenuItemVO>
    fun getMenuItemByCode(code: String): MenuItemVO?
}

/**
 * Reads feature flag state for runtime feature toggling.
 */
interface IFeatureFlagReader {
    fun isEnabled(flagKey: String): Boolean
    fun getFlag(flagKey: String): FeatureFlagVO?
    fun getAllFlags(): List<FeatureFlagVO>
}
