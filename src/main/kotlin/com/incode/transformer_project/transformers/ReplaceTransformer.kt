package com.incode.transformer_project.transformers

internal class ReplaceTransformer(regexPattern: String, private val with: String) : Transformer {

    private val regex = Regex(regexPattern)

    override fun transform(input: String): String {
        return regex.replace(input, with)
    }

    override fun getId(): String = "replace"
}