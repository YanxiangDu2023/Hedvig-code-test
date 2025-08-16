package com.hedvig.policies.repository

import com.hedvig.policies.model.Insurance
import org.springframework.data.jpa.repository.JpaRepository

interface InsuranceRepository : JpaRepository<Insurance, Long> {
    fun findByPersonalNumber(personalNumber: String): List<Insurance>
}