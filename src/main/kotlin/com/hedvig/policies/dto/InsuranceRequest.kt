package com.hedvig.policies.dto

import java.time.LocalDate

// Insurance Request
data class InsuranceRequest(
    val personalNumber: String,
    val address: String,
    val postalCode: String,
    val startDate: LocalDate
)
