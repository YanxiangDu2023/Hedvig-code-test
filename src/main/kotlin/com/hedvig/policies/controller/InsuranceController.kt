package com.hedvig.policies.controller

import com.hedvig.policies.dto.*
import com.hedvig.policies.model.Insurance
import com.hedvig.policies.model.Policy
import com.hedvig.policies.service.InsuranceCommandService
import com.hedvig.policies.service.InsuranceQueryService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/insurances")
class InsuranceController(
    private val command: InsuranceCommandService,
    private val query: InsuranceQueryService
) {
    //Create a new insurance for a given personal number.
    // Each insurance starts with an initial policy.



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInsurance(@RequestBody request: InsuranceRequest): Insurance =
        command.createInsurance(
            request.personalNumber, request.address, request.postalCode, request.startDate
        )

   //Add or update a policy for an existing insurance.
    //Previous policies are ended and a new one is created

    @PostMapping("/{insuranceId}/policies")
    fun updatePolicy(@PathVariable insuranceId: Long, @RequestBody request: PolicyRequest): Insurance =
        command.updatePolicy(insuranceId, request.address, request.postalCode, request.startDate)

    // Fetch all insurances by personal number.
    @GetMapping
    fun getInsurancesByPersonalNumber(@RequestParam personalNumber: String): List<Insurance> =
        query.getInsurancesByPersonalNumber(personalNumber)

    // Fetch all policies for a personal number that are active at a given date.
    @GetMapping("/policies")
    fun getPoliciesByPersonalNumberAndDate(
        @RequestParam personalNumber: String,
        @RequestParam date: LocalDate
    ): List<Policy> = query.getPoliciesByPersonalNumberAndDate(personalNumber, date)

    // Fetch the policy for a specific insurance that is valid at a given date.
    @GetMapping("/{insuranceId}/policy-at")
    fun getPolicyByInsuranceAndDate(
        @PathVariable insuranceId: Long,
        @RequestParam date: LocalDate
    ): Policy = query.getPolicyByInsuranceAndDate(insuranceId, date)

    //Fetch the current (active) policies for a personal number.
    // If no date is given, use today's date.
    @GetMapping("/current")
    fun getCurrentPolicies(
        @RequestParam personalNumber: String,
        @RequestParam(required = false) date: LocalDate?
    ) = query.getCurrentPoliciesForPersonalNumber(personalNumber, date ?: LocalDate.now())
        .map { (ins, p) ->
            CurrentPolicyDto(
                insuranceId = ins.id,
                personalNumber = ins.personalNumber,
                policy = PolicyDto(p.id, p.address, p.postalCode, p.startDate, p.endDate, p.premium)
            )
        }

    // Calculate the total premium cost for a given personal number
    // over a specified date range.
    @GetMapping("/premium/total")
    fun getPremiumTotal(
        @RequestParam personalNumber: String,
        @RequestParam from: LocalDate,
        @RequestParam to: LocalDate
    ) = PremiumTotalDto(
        personalNumber = personalNumber,
        from = from,
        to = to,
        totalPremium = query.getTotalPremiumForRange(personalNumber, from, to)
    )
}
