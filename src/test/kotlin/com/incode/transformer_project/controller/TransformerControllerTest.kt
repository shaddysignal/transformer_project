package com.incode.transformer_project.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.incode.transformer_project.IntegrationTest
import com.incode.transformer_project.model.TransformPair
import com.incode.transformer_project.model.TransformRequest
import com.incode.transformer_project.model.TransformResponse
import com.incode.transformer_project.repository.TransformRecordRepository
import com.incode.transformer_project.toInstant
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@AutoConfigureMockMvc
class TransformerControllerTest(
    @Autowired mockMvc: MockMvc,
    @Autowired objectMapper: ObjectMapper,
    @Autowired transformRecordRepository: TransformRecordRepository
) : IntegrationTest, FunSpec({

    afterTest {
        transformRecordRepository.deleteAll()
    }

    test("transform request") {
        val request =
            TransformRequest("test", listOf(TransformPair("remove", mapOf("regexPattern" to "te")), TransformPair("uppercase")))

        val body = objectMapper.writeValueAsString(request)
        val result = mockMvc.perform(
            get("/api/transform")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
            .andExpect(status().isOk)
            .andReturn()

        result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
        objectMapper.readValue(
            result.response.contentAsByteArray,
            TransformResponse::class.java
        ) shouldBe TransformResponse("ST")

        val records = transformRecordRepository.findByDateBetween(
            LocalDate.now().minusDays(2).toInstant(),
            LocalDate.now().plusDays(2).toInstant()
        )
        records.size shouldBe 2
        records.map { it.transformerId } shouldContainOnly listOf("uppercase", "remove")
    }

    test("transform request error") {
        val request =
            TransformRequest("test", listOf(TransformPair("remove", mapOf("regexPattern" to "*")), TransformPair("uppercase")))

        val result = mockMvc.perform(
            get("/api/transform")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is4xxClientError)
            .andReturn()

        result.response.contentType shouldBe MediaType.APPLICATION_JSON_VALUE
        objectMapper.readValue(
            result.response.contentAsByteArray,
            TransformResponse::class.java
        ).error shouldMatch "remove transformer broke, id: (-|)\\d+"
    }

})