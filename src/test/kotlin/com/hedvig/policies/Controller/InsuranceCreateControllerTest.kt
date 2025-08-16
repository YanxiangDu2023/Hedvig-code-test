package com.hedvig.policies.controller

import com.hedvig.policies.model.Insurance
import com.hedvig.policies.model.Policy
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate

class InsuranceCreateControllerTest : BaseInsuranceControllerTest() {

    @Test
    fun `should create insurance successfully`() {
        val req = mapOf(
            "personalNumber" to "19900101-1234",
            "address" to "Kungsgatan 16",
            "postalCode" to "11135",
            "startDate" to "2025-01-01"
        )

        val insurance = Insurance(id = 1L, personalNumber = "19900101-1234").apply {
            policies.add(
                Policy(
                    id = 1L,
                    insurance = this,
                    address = "Kungsgatan 16",
                    postalCode = "11135",
                    startDate = LocalDate.of(2025, 1, 1),
                    premium = BigDecimal("100.00")
                )
            )
        }

        `when`(
            command.createInsurance(
                "19900101-1234",
                "Kungsgatan 16",
                "11135",
                LocalDate.of(2025, 1, 1)
            )
        ).thenReturn(insurance)

        mockMvc.perform(
            post("/insurances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.personalNumber").value("19900101-1234"))
            .andExpect(jsonPath("$.policies[0].address").value("Kungsgatan 16"))
            .andExpect(jsonPath("$.policies[0].premium").value(100.00))
    }
}
