package com.incode.transformer_project.repository

import com.incode.transformer_project.IntegrationTest
import com.incode.transformer_project.model.TransformRecord
import com.incode.transformer_project.toInstant
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TransformRecordRepositoryTest(
    @Autowired transformRecordRepository: TransformRecordRepository
) : IntegrationTest, FunSpec({

    afterEach {
        transformRecordRepository.deleteAll()
    }

    test("save and retrieve") {
        val transformerId = "remove"
        val params = mapOf("regexPattern" to ".*")
        val date = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        val saved = transformRecordRepository.save(
            TransformRecord(
                transformerId = transformerId,
                params = params,
                input = "test",
                output = "",
                date = date
            )
        )

        saved.transformerId shouldBe transformerId
        saved.params shouldBe params
        saved.input shouldBe "test"
        saved.date shouldBe date
        saved.error shouldBe null
        saved.id shouldNotBe 0

        val found = transformRecordRepository.findByDateBetween(
            LocalDate.now().minusDays(2).toInstant(),
            LocalDate.now().plusDays(2).toInstant()
        )

        found.size shouldBe 1
        found[0] shouldBe saved
    }

    test("report") {
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

        val report = transformRecordRepository.countByDateBetweenGroupByTransformerId(
            LocalDate.now().minusDays(2).toInstant(),
            LocalDate.now().plusDays(2).toInstant()
        )

        report.size shouldBe 4
        report.filter { it.result == "success" }.map { it.transformerId } shouldContainOnly listOf("remove", "uppercase", "lowercase")
        report.filter { it.result == "failure" }.map { it.transformerId } shouldContainOnly listOf("remove")
        report.filter { it.transformerId == "remove" }.size shouldBe 2
        report.filter { it.transformerId == "remove" }.map { it.result } shouldContainOnly listOf("success", "failure")
        report.filter { it.transformerId == "uppercase" }.size shouldBe 1
        report.filter { it.transformerId == "uppercase" }.map { it.result } shouldContainOnly listOf("success")
        report.filter { it.transformerId == "lowercase" }.size shouldBe 1
        report.filter { it.transformerId == "lowercase" }.map { it.result } shouldContainOnly listOf("success")
    }
})