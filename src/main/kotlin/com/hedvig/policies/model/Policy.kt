package com.hedvig.policies.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class Policy(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insurance_id")
    @JsonIgnore                       // add
    val insurance: Insurance? = null,

    val address: String,
    val postalCode: String,
    val startDate: LocalDate,
    var endDate: LocalDate? = null,
    val premium: BigDecimal
)
