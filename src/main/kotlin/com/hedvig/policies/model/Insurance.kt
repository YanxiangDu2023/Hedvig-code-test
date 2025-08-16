package com.hedvig.policies.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class Insurance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val personalNumber: String,
    @OneToMany(mappedBy = "insurance", cascade = [CascadeType.ALL], orphanRemoval = true)
    val policies: MutableList<Policy> = mutableListOf()
)