package com.incode.transformer_project.transformers

sealed interface Transformer {

    fun transform(input: String): String

    fun getId(): String

}