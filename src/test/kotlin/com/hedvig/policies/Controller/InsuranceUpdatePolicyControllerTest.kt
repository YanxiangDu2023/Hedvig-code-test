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

class InsuranceUpdatePolicyControllerTest : BaseInsuranceControllerTest() {

    @Test
    fun `should update policy successfully`() {
        val req = mapOf(
            "address" to "Drottninggatan 22",
            "postalCode" to "11151",
            "startDate" to "2025-06-01"
        )

        val insurance = Insurance(id = 1L, personalNumber = "19900101-1234").apply {
            policies.add(
                Policy(
                    id = 1L,
                    insurance = this,
                    address = "Kungsgatan 16",
                    postalCode = "11135",
                    startDate = LocalDate.of(2025, 1, 1),
                    premium = BigDecimal("100.00"),
                    endDate = LocalDate.of(2025, 5, 31)
                )
            )
            policies.add(
                Policy(
                    id = 2L,
                    insurance = this,
                    address = "Drottninggatan 22",
                    postalCode = "11151",
                    startDate = LocalDate.of(2025, 6, 1),
                    premium = BigDecimal("100.00")
                )
            )
        }

        `when`(
            command.updatePolicy(
                1L,
                "Drottninggatan 22",
                "11151",
                LocalDate.of(2025, 6, 1)
            )
        ).thenReturn(insurance)

        mockMvc.perform(
            post("/insurances/1/policies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.policies[1].address").value("Drottninggatan 22"))
            .andExpect(jsonPath("$.policies[1].premium").value(100.00))
    }
}
