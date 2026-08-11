package com.ntt.basebusiness.cipher.repository

import com.ntt.basebusiness.cipher.entity.CipherEndpointPolicyEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * JPA repository for cipher endpoint policy entities.
 */
interface CipherEndpointPolicyRepository : JpaRepository<CipherEndpointPolicyEntity, Long> {

    /**
     * Find all active policies ordered by priority descending.
     */
    fun findByIsActiveTrueOrderByPriorityDesc(): List<CipherEndpointPolicyEntity>
}
