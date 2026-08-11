plugins {
    id("ntt.spring-library-conventions")
}

description = "Base Business — default JPA implementations for base-core interfaces (cipher policy, etc.)"

dependencies {
    api(project(":"))

    // JPA (required — this module provides JPA implementations)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Security starter (for CipherPolicyService interface)
    compileOnly(project(":starters:base-security-starter"))

    // Cache starter (optional — for cache-aware policy service)
    compileOnly(project(":starters:base-cache-starter"))
}
