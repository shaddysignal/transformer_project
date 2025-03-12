package com.incode.transformer_project.service

import com.incode.transformer_project.repository.TransformRecordRepository
import com.incode.transformer_project.toInstant
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import java.time.LocalDate

@Service
class ReportService(
    @Autowired private val transformRecordRepository: TransformRecordRepository
) {

    fun executionReportPlain(from: LocalDate, to: LocalDate): InputStreamResource {
        val reportData =
            transformRecordRepository.countByDateBetweenGroupByTransformerId(from.toInstant(), to.toInstant())

        val byteArrayOutputStream =
            ByteArrayOutputStream()
                .use { out ->
                    PrintWriter(out)
                        .use { printer ->
                            val grouped = reportData.groupBy { it.result }
                            grouped.keys.forEach { key ->
                                printer.println("$key:")
                                grouped[key]!!.forEach { data ->
                                    printer.println("   ${data.transformerId}: execution amount ${data.count}")
                                }
                            }
                            printer.flush()

                            ByteArrayInputStream(out.toByteArray())
                        }
                }

        return InputStreamResource(byteArrayOutputStream)
    }

    fun executionReportCsv(from: LocalDate, to: LocalDate): InputStreamResource {
        val reportData =
            transformRecordRepository.countByDateBetweenGroupByTransformerId(from.toInstant(), to.toInstant())

        val csvHeader = arrayOf("transformerId", "result", "amount")
        val byteArrayOutputStream =
            ByteArrayOutputStream()
                .use { out ->
                    CSVPrinter(
                        PrintWriter(out),
                        CSVFormat.newFormat('|').builder().setRecordSeparator('\n').setHeader(*csvHeader).get()
                    )
                        .use { csvPrinter ->
                            reportData.map { data ->
                                csvPrinter.printRecord(listOf(data.transformerId, data.result, data.count))
                            }
                            csvPrinter.flush()

                            ByteArrayInputStream(out.toByteArray())
                        }
                }

        return InputStreamResource(byteArrayOutputStream)
    }

}