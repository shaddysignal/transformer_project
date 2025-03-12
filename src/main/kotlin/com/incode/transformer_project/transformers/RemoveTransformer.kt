package com.incode.transformer_project.transformers

internal class RemoveTransformer(regexPattern: String) : Transformer {

    private val regex = Regex(regexPattern)

    override fun transform(input: String): String {
        return regex.replace(input, "")
    }

    override fun getId(): String = "remove"
}