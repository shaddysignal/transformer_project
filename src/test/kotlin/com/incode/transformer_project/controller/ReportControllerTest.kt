package com.incode.transformer_project.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.incode.transformer_project.IntegrationTest
import com.incode.transformer_project.model.TransformExecutionListRequest
import com.incode.transformer_project.model.TransformRecord
import com.incode.transformer_project.repository.TransformRecordRepository
import com.incode.transformer_project.toInstant
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.time.LocalDate

@AutoConfigureMockMvc
class ReportControllerTest(
    @Autowired mockMvc: MockMvc,
    @Autowired objectMapper: ObjectMapper,
    @Autowired transformRecordRepository: TransformRecordRepository
) : IntegrationTest, FunSpec({

    beforeTest {
        val commonRecord = TransformRecord(
            transformerId = "remove",
            params = mapOf("regexPattern" to "te"),
            input = "test",
            output = "st",
            date = Instant.now()
        )

        transformRecordRepository.save(commonRecord.copy())
        transformRecordRepository.save(commonRecord.copy())
        transformRecordRepository.save(commonRecord.copy(transformerId = "uppercase", params = mapOf()))
        transformRecordRepository.save(commonRecord.copy(transformerId = "lowercase", params = mapOf()))
        transformRecordRepository.save(commonRecord.copy(output = null, error = "something"))
        transformRecordRepository.save(commonRecord.copy(date = LocalDate.now().minusDays(5).toInstant()))
    }

    afterTest {
        transformRecordRepository.deleteAll()
    }

    test("execution list") {
        val request = TransformExecutionListRequest(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))

        val result = mockMvc.perform(get("/api/execution-list").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andReturn()

        result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
    }

    test("csv report") {
        val request = TransformExecutionListRequest(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))

        val result = mockMvc.perform(get("/api/execution-report.csv").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andReturn()

        result.response.contentType shouldBe "text/csv"
        result.response.contentAsString shouldBe
                "transformerId|result|amount\n" +
                "lowercase|success|1\n" +
                "remove|failure|1\n" +
                "remove|success|2\n" +
                "uppercase|success|1\n"
    }

    test("plain report") {
        val request = TransformExecutionListRequest(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))

        val result = mockMvc.perform(get("/api/execution-report").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andReturn()

        result.response.contentType shouldBe "text/plain"
        result.response.contentAsString shouldBe
                "success:\n" +
                "   lowercase: execution amount 1\n" +
                "   remove: execution amount 2\n" +
                "   uppercase: execution amount 1\n" +
                "failure:\n" +
                "   remove: execution amount 1\n"
    }

})