package com.hedvig.policies.repository

import com.hedvig.policies.model.Policy
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface PolicyRepository : JpaRepository<Policy, Long> {
    @Query("SELECT p FROM Policy p WHERE p.insurance.personalNumber = :personalNumber AND p.startDate <= :date AND (p.endDate IS NULL OR p.endDate >= :date)")
    fun findByPersonalNumberAndDate(personalNumber: String, date: LocalDate): List<Policy>

    @Query("SELECT p FROM Policy p WHERE p.insurance.id = :insuranceId AND p.startDate <= :date AND (p.endDate IS NULL OR p.endDate >= :date)")
    fun findByInsuranceIdAndDate(insuranceId: Long, date: LocalDate): Policy?
}