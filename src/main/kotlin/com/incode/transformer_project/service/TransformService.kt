package com.incode.transformer_project.service

import com.incode.transformer_project.Logging
import com.incode.transformer_project.log
import com.incode.transformer_project.model.TransformPair
import com.incode.transformer_project.model.TransformRecord
import com.incode.transformer_project.repository.TransformRecordRepository
import com.incode.transformer_project.transformers.TransformerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class TransformService(
    @Autowired private val transformRecordRepository: TransformRecordRepository,
    @Autowired private val transformerFactory: TransformerFactory
) : Logging {

    fun processTransformations(input: String, transformations: List<TransformPair>): String {
        val output = transformations.fold(input) { prev, transformation ->
            val transformRecord = TransformRecord(
                transformerId = transformation.transformerId,
                params = transformation.parameters,
                input = prev,
                date = Instant.now()
            )

            val transformOutput = try {
                val transformer = transformerFactory.produce(transformation.transformerId, transformation.parameters)
                transformer.transform(prev)
            } catch (e: Exception) {
                log.error(e) { "Occurred during transformation request" }
                val saved = transformRecordRepository.save(transformRecord.copy(error = e.message))

                throw RuntimeException("${transformation.transformerId} transformer broke, id: ${saved.id}", e)
            }

            transformRecordRepository.save(transformRecord.copy(output = transformOutput))
            transformOutput
        }

        return output
    }

}