package com.hedvig.policies.controller

import com.hedvig.policies.dto.PremiumTotalDto
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate

class InsurancePremiumControllerTest : BaseInsuranceControllerTest() {

    @Test
    fun `should calculate total premium`() {
        val from = LocalDate.of(2025, 1, 1)
        val to = LocalDate.of(2025, 3, 31)

        val dto = PremiumTotalDto(
            personalNumber = "19900101-1234",
            from = from,
            to = to,
            totalPremium = BigDecimal("300.00")
        )

        `when`(
            query.getTotalPremiumForRange("19900101-1234", from, to)
        ).thenReturn(dto.totalPremium)

        mockMvc.perform(
            get("/insurances/premium/total")
                .param("personalNumber", "19900101-1234")
                .param("from", "2025-01-01")
                .param("to", "2025-03-31")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.personalNumber").value("19900101-1234"))
            .andExpect(jsonPath("\$.totalPremium").value(300.00))
    }
}
