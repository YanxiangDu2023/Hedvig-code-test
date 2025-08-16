package com.hedvig.policies.service

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

// Verifies that the premium calculation logic based on postal code prefixes works correctly
class PremiumCalculatorTest {
    private val calc = PremiumCalculator()

    @Test
    fun `calculate by postal code prefix`() {
        assertEquals(BigDecimal("100.00"), calc.calculate("11135"))
        assertEquals(BigDecimal("150.00"), calc.calculate("12345"))
    }
}
