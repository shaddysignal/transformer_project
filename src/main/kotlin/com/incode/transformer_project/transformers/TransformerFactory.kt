package com.incode.transformer_project.transformers

import org.springframework.stereotype.Component

@Component
class TransformerFactory {

    fun produce(transformerId: String, params: Map<String, String> = mapOf()): Transformer {
        return when (transformerId) {
            "remove" -> produceRemoveTransformer(
                params["regexPattern"] ?: throw IllegalArgumentException("regexPattern should be present in params")
            )

            "replace" -> produceReplaceTransformer(
                params["regexPattern"]?.ifEmpty { null } ?: throw IllegalArgumentException("regexPattern should be present and not be empty in params"),
                params["with"] ?: throw IllegalArgumentException("with should be present in params")
            )

            "uppercase" -> produceUppercaseTransformer()
            "lowercase" -> produceLowercaseTransformer()
            else -> throw IllegalArgumentException("transformerId not implemented")
        }
    }

    private fun produceRemoveTransformer(regexPattern: String): Transformer =
        RemoveTransformer(regexPattern)

    private fun produceReplaceTransformer(regexPattern: String, with: String): Transformer =
        ReplaceTransformer(regexPattern, with)

    private fun produceUppercaseTransformer(): Transformer =
        UppercaseTransformer

    private fun produceLowercaseTransformer(): Transformer =
        LowercaseTransformer

}