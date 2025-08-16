package com.hedvig.policies.controller

import com.hedvig.policies.model.Insurance
import com.hedvig.policies.model.Policy
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate

class InsuranceCurrentControllerTest : BaseInsuranceControllerTest() {

    @Test
    fun `should get current policies`() {
        // Arrange: build domain entities
        val ins = Insurance(id = 1L, personalNumber = "19900101-1234")
        val pol = Policy(
            id = 1L,
            insurance = ins,
            address = "Kungsgatan 16",
            postalCode = "11135",
            startDate = LocalDate.of(2025, 1, 1),
            premium = BigDecimal("100.00")
        )
        ins.policies.add(pol)

        // Stub query service to return entity pairs
        Mockito.`when`(
            query.getCurrentPoliciesForPersonalNumber(
                "19900101-1234",
                LocalDate.of(2025, 3, 1)
            )
        ).thenReturn(listOf(ins to pol))

        // Act + Assert
        mockMvc.perform(
            get("/insurances/current")
                .param("personalNumber", "19900101-1234")
                .param("date", "2025-03-01")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].insuranceId").value(1))
            .andExpect(jsonPath("$[0].personalNumber").value("19900101-1234"))
            .andExpect(jsonPath("$[0].policy.address").value("Kungsgatan 16"))
            .andExpect(jsonPath("$[0].policy.postalCode").value("11135"))
            .andExpect(jsonPath("$[0].policy.premium").value(100.00))
    }
}
