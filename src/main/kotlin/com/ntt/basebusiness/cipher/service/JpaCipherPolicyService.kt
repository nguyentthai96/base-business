package com.ntt.basebusiness.cipher.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.ntt.basecore.autoconfigure.security.cipher.core.CipherPolicy
import com.ntt.basecore.autoconfigure.security.cipher.core.CipherPolicyService
import com.ntt.basebusiness.cipher.entity.CipherEndpointPolicyEntity
import com.ntt.basebusiness.cipher.repository.CipherEndpointPolicyRepository
import org.slf4j.LoggerFactory
import org.springframework.util.AntPathMatcher
import java.util.concurrent.ConcurrentHashMap

/**
 * Default JPA implementation of [CipherPolicyService].
 *
 * Loads policies from `cipher_endpoint_policy` table and caches in-memory.
 * Uses [AntPathMatcher] for endpoint pattern matching.
 *
 * Pattern follows [ApiThrottleFilter] — DB-driven config with ConcurrentHashMap cache.
 *
 * Consumer services can override via `@ConditionalOnMissingBean(CipherPolicyService::class)`.
 */
class JpaCipherPolicyService(
    private val repository: CipherEndpointPolicyRepository,
    private val objectMapper: ObjectMapper
) : CipherPolicyService {

    private val log = LoggerFactory.getLogger(JpaCipherPolicyService::class.java)
    private val antPathMatcher = AntPathMatcher()

    // In-memory cache of active policies (refreshed on invalidateCache())
    private val policyCache = ConcurrentHashMap<String, CipherPolicy>()
    private var cacheLoaded = false

    override fun resolvePolicy(path: String, method: String): CipherPolicy? {
        ensureCacheLoaded()
        return policyCache.values
            .filter { policy ->
                antPathMatcher.match(policy.endpointPath, path) &&
                    (policy.httpMethod == "*" || policy.httpMethod.equals(method, ignoreCase = true))
            }
            .maxByOrNull { it.priority }
    }

    override fun getAllActivePolicies(): List<CipherPolicy> {
        ensureCacheLoaded()
        return policyCache.values.toList()
    }

    override fun invalidateCache() {
        policyCache.clear()
        cacheLoaded = false
        log.info("Cipher policy cache invalidated")
    }

    private fun ensureCacheLoaded() {
        if (!cacheLoaded) {
            synchronized(this) {
                if (!cacheLoaded) {
                    loadPolicies()
                    cacheLoaded = true
                }
            }
        }
    }

    private fun loadPolicies() {
        try {
            val entities = repository.findByIsActiveTrueOrderByPriorityDesc()
            policyCache.clear()
            entities.forEach { entity ->
                val policy = toCipherPolicy(entity)
                val key = "${entity.httpMethod ?: "*"}:${entity.endpointPath}"
                policyCache[key] = policy
            }
            log.info("Cipher policy cache loaded: {} active policies", entities.size)
        } catch (ex: Exception) {
            log.error("Failed to load cipher policies from database — using stale cache", ex)
        }
    }

    private fun toCipherPolicy(entity: CipherEndpointPolicyEntity): CipherPolicy {
        val fields = entity.fields?.let {
            try {
                objectMapper.readValue(it, object : TypeReference<List<String>>() {})
            } catch (ex: Exception) {
                log.warn("Failed to parse fields JSON for endpoint {}: {}", entity.endpointPath, ex.message)
                null
            }
        }
        return CipherPolicy(
            endpointPath = entity.endpointPath,
            httpMethod = entity.httpMethod,
            cipherType = entity.cipherType,
            authRequired = entity.authRequired,
            algorithm = entity.algorithm,
            fields = fields,
            priority = entity.priority
        )
    }
}
