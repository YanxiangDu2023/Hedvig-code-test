package com.hedvig.policies.dto


//Current Policy
data class CurrentPolicyDto(
    val insuranceId: Long?,
    val personalNumber: String,
    val policy: PolicyDto
)
