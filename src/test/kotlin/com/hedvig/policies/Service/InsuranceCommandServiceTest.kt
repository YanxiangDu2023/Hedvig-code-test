package com.hedvig.policies.service

import com.hedvig.policies.model.Insurance
import com.hedvig.policies.model.Policy
import com.hedvig.policies.repository.InsuranceRepository
import com.hedvig.policies.repository.PolicyRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals

// Unit tests for InsuranceCommandService.
@ExtendWith(MockitoExtension::class)
class InsuranceCommandServiceTest {

    @Mock lateinit var insuranceRepository: InsuranceRepository
    @Mock lateinit var policyRepository: PolicyRepository
    @Mock lateinit var premiumCalculator: PremiumCalculator

    @InjectMocks
    lateinit var service: InsuranceCommandService

    @BeforeEach
    fun setup() {
        `when`(insuranceRepository.save(any(Insurance::class.java))).thenAnswer { it.arguments[0] as Insurance }
        `when`(policyRepository.save(any(Policy::class.java))).thenAnswer { it.arguments[0] as Policy }
    }

    @Test
    fun `createInsurance should persist insurance and policy`() {
        `when`(premiumCalculator.calculate("11135")).thenReturn(BigDecimal("100.00"))

        val result = service.createInsurance(
            "19900101-1234", "Kungsgatan 16", "11135", LocalDate.of(2025, 1, 1)
        )

        assertEquals("19900101-1234", result.personalNumber)
        assertEquals("Kungsgatan 16", result.policies[0].address)
        assertEquals(BigDecimal("100.00"), result.policies[0].premium)

        verify(insuranceRepository, times(1)).save(any(Insurance::class.java))
        verify(policyRepository, times(1)).save(any(Policy::class.java))
        verify(premiumCalculator).calculate("11135")
    }


    @Test
    fun `updatePolicy should end previous policy and create new one`() {
        val insurance = Insurance(id = 1L, personalNumber = "19900101-1234")
        val current = Policy(
            id = 1L, insurance = insurance, address = "Kungsgatan 16", postalCode = "11135",
            startDate = LocalDate.of(2025,1,1), premium = BigDecimal("100.00")
        )
        insurance.policies.add(current)

        `when`(insuranceRepository.findById(1L)).thenReturn(java.util.Optional.of(insurance))
        // effectiveDate = 2025-06-01.minusDays(1) = 2025-05-31
        `when`(policyRepository.findByInsuranceIdAndDate(1L, LocalDate.of(2025,5,31))).thenReturn(current)
        `when`(premiumCalculator.calculate("11151")).thenReturn(BigDecimal("100.00"))

        val result = service.updatePolicy(
            1L, "Drottninggatan 22", "11151", LocalDate.of(2025,6,1)
        )

        assertEquals(LocalDate.of(2025,5,31), current.endDate)
        assertEquals("Drottninggatan 22", result.policies[1].address)
        assertEquals(BigDecimal("100.00"), result.policies[1].premium)

        verify(policyRepository, times(2)).save(any(Policy::class.java)) // old + new
        verify(insuranceRepository, times(1)).save(insurance)
        verify(premiumCalculator).calculate("11151")
    }
}
