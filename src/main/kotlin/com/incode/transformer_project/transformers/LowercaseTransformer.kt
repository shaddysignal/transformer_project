package com.incode.transformer_project.transformers

internal data object LowercaseTransformer : Transformer {
    override fun transform(input: String): String {
        return input.lowercase()
    }

    override fun getId(): String = "lowercase"
}