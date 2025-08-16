package com.hedvig.policies.service

import com.hedvig.policies.model.Insurance
import com.hedvig.policies.model.Policy
import com.hedvig.policies.repository.InsuranceRepository
import com.hedvig.policies.repository.PolicyRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class InsuranceCommandService(
    private val insuranceRepository: InsuranceRepository,
    private val policyRepository: PolicyRepository,
    private val premiumCalculator: PremiumCalculator
) {

    //Create a new Insurance and its initial Policy.
    //A new Insurance is saved for the given personal number.
    //A first Policy is created starting at startDate with calculated premium.
    // The policy is linked to the insurance and persisted.


    @Transactional
    fun createInsurance(
        personalNumber: String,
        address: String,
        postalCode: String,
        startDate: LocalDate
    ): Insurance {
        val savedInsurance = insuranceRepository.save(Insurance(personalNumber = personalNumber))
        val policy = Policy(
            insurance = savedInsurance,
            address = address,
            postalCode = postalCode,
            startDate = startDate,
            premium = premiumCalculator.calculate(postalCode)
        )
        policyRepository.save(policy)
        savedInsurance.policies.add(policy)
        return savedInsurance
    }


    //Update an existing Insurance by adding a new Policy.
    // Finds the Insurance by ID (throws if not found).
    // Ends the currently active Policy (by setting endDate = startDate - 1).
    // Creates and saves a new Policy with updated details and calculated premium.
    // Links the new Policy to the Insurance and persists the changes.

    @Transactional
    fun updatePolicy(insuranceId: Long, address: String, postalCode: String, startDate: LocalDate): Insurance {
        val insurance = insuranceRepository.findById(insuranceId)
            .orElseThrow { IllegalArgumentException("Insurance not found") }

        val effectiveDate = startDate.minusDays(1)
        policyRepository.findByInsuranceIdAndDate(insuranceId, effectiveDate)?.let { current ->
            current.endDate = effectiveDate
            policyRepository.save(current)
        }

        // Create and link the new policy
        val newPolicy = Policy(
            insurance = insurance,
            address = address,
            postalCode = postalCode,
            startDate = startDate,
            premium = premiumCalculator.calculate(postalCode)
        )
        insurance.policies.add(newPolicy)
        policyRepository.save(newPolicy)


        return insuranceRepository.save(insurance)
    }
}
