package com.hedvig.policies.service

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class PremiumCalculator {
    fun calculate(postalCode: String): BigDecimal =
        if (postalCode.startsWith("11")) BigDecimal("100.00") else BigDecimal("150.00")
}
