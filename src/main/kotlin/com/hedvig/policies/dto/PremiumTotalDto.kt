package com.hedvig.policies.dto

import java.math.BigDecimal
import java.time.LocalDate

// PremiumTotal
data class PremiumTotalDto(
    val personalNumber: String,
    val from: LocalDate,
    val to: LocalDate,
    val totalPremium: BigDecimal,
    val currency: String = "SEK"
)
