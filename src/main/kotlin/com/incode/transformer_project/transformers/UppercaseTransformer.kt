package com.incode.transformer_project.transformers

internal data object UppercaseTransformer : Transformer {
    override fun transform(input: String): String {
        return input.uppercase()
    }

    override fun getId(): String = "uppercase"
}