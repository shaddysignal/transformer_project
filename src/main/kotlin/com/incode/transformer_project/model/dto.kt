package com.incode.transformer_project.model

import java.time.Instant
import java.time.LocalDate

data class TransformRequest(val input: String, val transformers: List<Pair<String, Map<String, String>>>)
data class TransformResponse(val output: String? = null, val error: String? = null)

data class TransformExecution(
    val transformerId: String,
    val params: Map<String, String>,
    val input: String,
    val result: String,
    val date: Instant
)

fun TransformRecord.toTransformExecution(): TransformExecution =
    TransformExecution(transformerId, params, input, output ?: error!!, date)

data class TransformExecutionListRequest(val from: LocalDate, val to: LocalDate)
data class TransformExecutionListResponse(val executions: List<TransformExecution>)