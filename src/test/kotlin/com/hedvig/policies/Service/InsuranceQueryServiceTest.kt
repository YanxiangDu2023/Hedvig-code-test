package com.hedvig.policies.service

import com.hedvig.policies.model.Insurance
import com.hedvig.policies.model.Policy
import com.hedvig.policies.repository.InsuranceRepository
import com.hedvig.policies.repository.PolicyRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockitoExtension::class)
class InsuranceQueryServiceTest {

    @Mock lateinit var insuranceRepository: InsuranceRepository
    @Mock lateinit var policyRepository: PolicyRepository

    @InjectMocks
    lateinit var service: InsuranceQueryService

    @Test
    fun `getPoliciesByPersonalNumberAndDate should return active policies`() {
        val ins = Insurance(id = 1L, personalNumber = "19900101-1234")
        val p1 = Policy(
            id = 1L,
            insurance = ins,
            address = "A",
            postalCode = "11135",
            startDate = LocalDate.of(2025,1,1),
            premium = BigDecimal("100.00")
        )

        val p2 = Policy(
            id = 2L,
            insurance = ins,
            address = "B",
            postalCode = "11151",
            startDate = LocalDate.of(2025,6,1),
            premium = BigDecimal("100.00")
        )

        p1.endDate = LocalDate.of(2025,5,31)
        ins.policies.addAll(listOf(p1, p2))
        `when`(insuranceRepository.findByPersonalNumber("19900101-1234")).thenReturn(listOf(ins))

        val mar = service.getPoliciesByPersonalNumberAndDate("19900101-1234", LocalDate.of(2025,3,1))
        val jun = service.getPoliciesByPersonalNumberAndDate("19900101-1234", LocalDate.of(2025,6,15))

        assertEquals(listOf(p1), mar)
        assertEquals(listOf(p2), jun)
    }

    @Test
    fun `getPolicyByInsuranceAndDate should return policy from repository`() {
        val target = Policy(
            id = 10L, insurance = Insurance(id = 1L, personalNumber = "x"),
            address = "A", postalCode = "11135", startDate = LocalDate.of(2025,1,1), premium = BigDecimal("100.00")
        )
        `when`(policyRepository.findByInsuranceIdAndDate(1L, LocalDate.of(2025,3,1))).thenReturn(target)

        val result = service.getPolicyByInsuranceAndDate(1L, LocalDate.of(2025,3,1))
        assertNotNull(result)
        assertEquals(10L, result.id)
    }

    @Test
    fun `getCurrentPoliciesForPersonalNumber should map to pairs`() {
        val ins = Insurance(id = 1L, personalNumber = "19900101-1234")
        val p1 = Policy(
            id = 1L,
            insurance = ins,
            address = "A",
            postalCode = "11135",
            startDate = LocalDate.of(2025,1,1),
            premium = BigDecimal("100.00")
        )

        val p2 = Policy(
            id = 2L,
            insurance = ins,
            address = "B",
            postalCode = "11151",
            startDate = LocalDate.of(2025,6,1),
            premium = BigDecimal("100.00")
        )

        p1.endDate = LocalDate.of(2025,5,31)
        ins.policies.addAll(listOf(p1, p2))
        `when`(insuranceRepository.findByPersonalNumber("19900101-1234")).thenReturn(listOf(ins))

        val mar = service.getCurrentPoliciesForPersonalNumber("19900101-1234", LocalDate.of(2025,3,1))
        val jun = service.getCurrentPoliciesForPersonalNumber("19900101-1234", LocalDate.of(2025,6,15))

        assertEquals(1, mar.size); assertEquals("A", mar[0].second.address)
        assertEquals(1, jun.size); assertEquals("B", jun[0].second.address)
    }

    @Test
    fun `getTotalPremiumForRange should sum whole months`() {
        val ins = Insurance(id = 1L, personalNumber = "20000101-0000")
        val p1 = Policy(
            id = 1L,
            insurance = ins,
            address = "A",
            postalCode = "11135",
            startDate = LocalDate.of(2025,1,1),
            premium = BigDecimal("100.00")
        )

        val p2 = Policy(
            id = 2L,
            insurance = ins,
            address = "B",
            postalCode = "11151",
            startDate = LocalDate.of(2025,6,1),
            premium = BigDecimal("100.00")
        )

        p1.endDate = LocalDate.of(2025,5,31)
        ins.policies.addAll(listOf(p1, p2))
        `when`(insuranceRepository.findByPersonalNumber("20000101-0000")).thenReturn(listOf(ins))

        val total = service.getTotalPremiumForRange(
            personalNumber = "20000101-0000",
            from = LocalDate.of(2025,1,1),
            to = LocalDate.of(2025,6,30)
        )
        assertEquals(BigDecimal("600.00"), total)
    }
}
