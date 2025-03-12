package com.incode.transformer_project.repository

import com.incode.transformer_project.model.ReportRecord
import com.incode.transformer_project.model.TransformRecord
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.time.Instant

interface TransformRecordRepository : Repository<TransformRecord, Long> {

    fun save(transformerRecord: TransformRecord): TransformRecord

    fun findByDateBetween(from: Instant, to: Instant): List<TransformRecord>

    fun deleteAll()

    @Query("SELECT NEW com.incode.transformer_project.model.ReportRecord(t.transformerId, 'success', COUNT(t))" +
            " FROM TransformRecord t" +
            " WHERE output IS NOT NULL AND t.date BETWEEN :from AND :to GROUP BY t.transformerId" +
            " UNION " +
            "SELECT NEW com.incode.transformer_project.model.ReportRecord(t.transformerId, 'failure', COUNT(t))" +
            " FROM TransformRecord t" +
            " WHERE output IS NULL AND t.date BETWEEN :from AND :to GROUP BY t.transformerId")
    fun countByDateBetweenGroupByTransformerId(from: Instant, to: Instant): List<ReportRecord>

}

