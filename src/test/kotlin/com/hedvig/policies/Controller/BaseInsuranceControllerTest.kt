package com.hedvig.policies.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hedvig.policies.service.InsuranceCommandService
import com.hedvig.policies.service.InsuranceQueryService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
abstract class BaseInsuranceControllerTest {

    @Mock
    protected lateinit var command: InsuranceCommandService

    @Mock
    protected lateinit var query: InsuranceQueryService

    @InjectMocks
    protected lateinit var controller: InsuranceController

    protected lateinit var mockMvc: MockMvc

    protected val om: ObjectMapper = ObjectMapper().apply { findAndRegisterModules() }

    @BeforeEach
    fun setupBase() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }
}
