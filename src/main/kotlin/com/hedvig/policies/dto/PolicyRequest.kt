package com.hedvig.policies.dto

import java.time.LocalDate

// Policy Request
data class PolicyRequest(
    val address: String,
    val postalCode: String,
    val startDate: LocalDate
)
