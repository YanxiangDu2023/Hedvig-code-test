package com.hedvig.policies.controller

import com.hedvig.policies.model.Insurance
import com.hedvig.policies.model.Policy
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate

class InsuranceQueryControllerTest : BaseInsuranceControllerTest() {

    @Test
    fun `should list all insurances for personal number`() {
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

        `when`(query.getInsurancesByPersonalNumber("19900101-1234"))
            .thenReturn(listOf(insurance))

        mockMvc.perform(
            get("/insurances").param("personalNumber", "19900101-1234")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$[0].personalNumber").value("19900101-1234"))
    }

    @Test
    fun `should get policies by personal number and date`() {
        val policy = Policy(
            id = 1L,
            insurance = Insurance(id = 1L, personalNumber = "19900101-1234"),
            address = "Kungsgatan 16",
            postalCode = "11135",
            startDate = LocalDate.of(2025, 1, 1),
            premium = BigDecimal("100.00")
        )

        `when`(
            query.getPoliciesByPersonalNumberAndDate(
                "19900101-1234",
                LocalDate.of(2025, 3, 1)
            )
        ).thenReturn(listOf(policy))

        mockMvc.perform(
            get("/insurances/policies")
                .param("personalNumber", "19900101-1234")
                .param("date", "2025-03-01")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$[0].address").value("Kungsgatan 16"))
    }

    @Test
    fun `should get policy by insurance id and date`() {
        val policy = Policy(
            id = 1L,
            insurance = Insurance(id = 1L, personalNumber = "19900101-1234"),
            address = "Kungsgatan 16",
            postalCode = "11135",
            startDate = LocalDate.of(2025, 1, 1),
            premium = BigDecimal("100.00")
        )

        `when`(
            query.getPolicyByInsuranceAndDate(
                1L,
                LocalDate.of(2025, 3, 1)
            )
        ).thenReturn(policy)

        mockMvc.perform(
            get("/insurances/1/policy-at").param("date", "2025-03-01")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.address").value("Kungsgatan 16"))
    }
}
