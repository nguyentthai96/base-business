package com.ntt.basebusiness.cipher.entity

import com.ntt.basecore.autoconfigure.security.cipher.core.CipherAlgorithm
import com.ntt.basecore.autoconfigure.security.cipher.core.CipherType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

/**
 * JPA entity for cipher endpoint policy configuration.
 *
 * Maps to `cipher_endpoint_policy` table.
 * Each row defines the cipher behavior for a specific endpoint pattern.
 */
@Entity
@Table(
    name = "cipher_endpoint_policy",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_cipher_endpoint",
            columnNames = ["endpoint_path", "http_method"]
        )
    ]
)
class CipherEndpointPolicyEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // AntPathMatcher pattern, e.g. /api/v1/payment/
    @Column(name = "endpoint_path", nullable = false, length = 500)
    var endpointPath: String = "",

    // HTTP method or wildcard for all methods
    @Column(name = "http_method", length = 10)
    var httpMethod: String? = "*",

    // Cipher classification
    @Enumerated(EnumType.STRING)
    @Column(name = "cipher_type", nullable = false, length = 20)
    var cipherType: CipherType = CipherType.NON_CIPHER,

    // TRUE = USER KEY (authenticated), FALSE = APP KEY (anonymous)
    @Column(name = "auth_required", nullable = false)
    var authRequired: Boolean = true,

    // Encryption algorithm
    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm", length = 50)
    var algorithm: CipherAlgorithm = CipherAlgorithm.AES_GCM,

    // JSON array for ENCRYPT_PARTIAL field names
    @Column(name = "fields", columnDefinition = "TEXT")
    var fields: String? = null,

    // Higher value = matched first when multiple patterns match
    @Column(name = "priority", nullable = false)
    var priority: Int = 0,

    // Soft enable/disable
    @Column(name = "is_active", nullable = false)
    var isActive: Boolean = true,

    // Creation timestamp (Unix ms)
    @Column(name = "created_at", nullable = false)
    var createdAt: Long = System.currentTimeMillis(),

    // Last update timestamp (Unix ms)
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Long = System.currentTimeMillis()
)
