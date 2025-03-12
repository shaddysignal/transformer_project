package com.incode.transformer_project.model

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.Type
import org.hibernate.type.SqlTypes
import java.time.Instant

@Entity
data class TransformRecord(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transform_record_id_seq") val id: Long = 0,
    val transformerId: String,
    @Type(JsonBinaryType::class) @JdbcTypeCode(SqlTypes.JSON) @Column(columnDefinition = "jsonb") val params: Map<String, String>,
    val input: String,
    val output: String? = null,
    val error: String? = null,
    val date: Instant
)

/**
 * Class to collect report data into specified format
 */
data class ReportRecord(val transformerId: String, val result: String, val count: Long)