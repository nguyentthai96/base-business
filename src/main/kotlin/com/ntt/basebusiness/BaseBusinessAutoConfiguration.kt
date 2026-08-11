package com.ntt.basebusiness

import com.fasterxml.jackson.databind.ObjectMapper
import com.ntt.basecore.autoconfigure.security.cipher.core.CipherPolicyService
import com.ntt.basebusiness.cipher.repository.CipherEndpointPolicyRepository
import com.ntt.basebusiness.cipher.service.JpaCipherPolicyService
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * Auto-configuration for base-business module.
 *
 * Provides default JPA implementations for base-core interfaces.
 * Currently includes:
 * - [JpaCipherPolicyService]: Default CipherPolicyService backed by JPA
 *
 * Entity scanning is handled by Spring Boot auto-configuration from the
 * base package of this auto-configuration class.
 */
@AutoConfiguration
@EnableJpaRepositories(basePackages = ["com.ntt.basebusiness"])
class BaseBusinessAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CipherPolicyService::class)
    @ConditionalOnBean(CipherEndpointPolicyRepository::class)
    fun jpaCipherPolicyService(
        repository: CipherEndpointPolicyRepository,
        objectMapper: ObjectMapper
    ): JpaCipherPolicyService {
        return JpaCipherPolicyService(repository, objectMapper)
    }
}
