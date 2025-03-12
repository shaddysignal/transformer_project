package com.incode.transformer_project.controllers

import com.incode.transformer_project.model.TransformExecutionListRequest
import com.incode.transformer_project.model.TransformExecutionListResponse
import com.incode.transformer_project.model.toTransformExecution
import com.incode.transformer_project.repository.TransformRecordRepository
import com.incode.transformer_project.service.ReportService
import com.incode.transformer_project.toInstant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/")
class ReportController(
    @Autowired private val reportService: ReportService,
    @Autowired private val transformRecordRepository: TransformRecordRepository
) {

    @GetMapping(path = ["execution-list"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun listExecutionReport(@RequestBody request: TransformExecutionListRequest): ResponseEntity<TransformExecutionListResponse> {
        val executions = transformRecordRepository.findByDateBetween(request.from.toInstant(), request.to.toInstant())

        return ResponseEntity.ok(TransformExecutionListResponse(executions.map { it.toTransformExecution() }))
    }

    @GetMapping(path = ["execution-report{ext:.csv|}"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.TEXT_PLAIN_VALUE, "text/csv"])
    fun exportExecutionReport(@RequestBody request: TransformExecutionListRequest, @PathVariable ext: String): ResponseEntity<Resource> {
        val fileInputStream: Resource
        val fileName: String
        val contentType: String
        if (ext.isEmpty()) {
            fileInputStream = reportService.executionReportPlain(request.from, request.to)
            fileName = "execution_report_${request.from}_${request.to}.txt"
            contentType = "text/plain"
        } else {
            fileInputStream = reportService.executionReportCsv(request.from, request.to)
            fileName = "execution_report_${request.from}_${request.to}.csv"
            contentType = "text/csv"
        }

        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${fileName}")
        headers.set(HttpHeaders.CONTENT_TYPE, contentType)

        return ResponseEntity.ok()
            .headers(headers)
            .body(fileInputStream)
    }

}