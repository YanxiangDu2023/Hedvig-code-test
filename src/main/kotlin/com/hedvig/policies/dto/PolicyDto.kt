package com.hedvig.policies.dto

import java.math.BigDecimal
import java.time.LocalDate

// Policy
data class PolicyDto(
    val id: Long?,
    val address: String,
    val postalCode: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val premium: BigDecimal
)
