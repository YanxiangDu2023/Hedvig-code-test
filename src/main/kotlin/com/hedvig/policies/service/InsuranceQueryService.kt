package com.hedvig.policies.service

import com.hedvig.policies.model.Insurance
import com.hedvig.policies.model.Policy
import com.hedvig.policies.repository.InsuranceRepository
import com.hedvig.policies.repository.PolicyRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@Service
class InsuranceQueryService(
    private val insuranceRepository: InsuranceRepository,
    private val policyRepository: PolicyRepository
) {
    // Find all Insurance records for a given personal number.
    fun getInsurancesByPersonalNumber(personalNumber: String): List<Insurance> =
        insuranceRepository.findByPersonalNumber(personalNumber)

    // Find all active Policies for a given personal number and date.
    fun getPoliciesByPersonalNumberAndDate(personalNumber: String, date: LocalDate): List<Policy> =
        insuranceRepository.findByPersonalNumber(personalNumber)
            .flatMap { ins -> ins.policies.filter { it.isActiveOn(date) } }

    // Find the Policy for a given insuranceId that is valid on the given date.
    fun getPolicyByInsuranceAndDate(insuranceId: Long, date: LocalDate): Policy =
        policyRepository.findByInsuranceIdAndDate(insuranceId, date)
            ?: throw IllegalArgumentException("No policy found for this date")

    // Find the current active Policy (on the given or default date)
    fun getCurrentPoliciesForPersonalNumber(personalNumber: String, date: LocalDate = LocalDate.now()): List<Pair<Insurance, Policy>> =
        insuranceRepository.findByPersonalNumber(personalNumber).mapNotNull { ins ->
            ins.policies.firstOrNull { it.isActiveOn(date) }?.let { ins to it }
        }

    // Calculate the total premium over a given date range for all Insurances
    // For each policy, calculate the overlap with [from, to].
    // Count overlapping months and multiply by the policy's monthly premium.
    // Sum across all policies.
    fun getTotalPremiumForRange(personalNumber: String, from: LocalDate, to: LocalDate): BigDecimal {
        require(!to.isBefore(from)) { "to must be on or after from" }
        val insList = insuranceRepository.findByPersonalNumber(personalNumber)
        var total = BigDecimal.ZERO
        for (ins in insList) {
            for (p in ins.policies) {
                val overlapStart = maxOf(p.startDate, from)
                val overlapEnd = minOf(p.endDate ?: to, to)
                if (!overlapEnd.isBefore(overlapStart)) {
                    val months = countInclusiveMonths(overlapStart, overlapEnd)
                    total = total.add(p.premium.multiply(BigDecimal.valueOf(months.toLong())))
                }
            }
        }
        return total
    }

    // Check if a policy is active on a given date.
    private fun Policy.isActiveOn(date: LocalDate) =
        !startDate.isAfter(date) && (endDate == null || !endDate!!.isBefore(date))

    // Count the number of whole months between start and end (inclusive).
    private fun countInclusiveMonths(start: LocalDate, end: LocalDate): Int {
        val ymStart = YearMonth.from(start)
        val ymEnd = YearMonth.from(end)
        return (ChronoUnit.MONTHS.between(ymStart, ymEnd) + 1).toInt()
    }
}
