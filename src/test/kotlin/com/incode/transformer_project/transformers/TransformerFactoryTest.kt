package com.incode.transformer_project.transformers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import java.util.regex.PatternSyntaxException

class TransformerFactoryTest : FunSpec({

    val factory = TransformerFactory()

    context("produce") {
        test("nonexistent") {
            shouldThrow<IllegalArgumentException> { factory.produce("nonexistent") }
        }

        test("remove") {
            val removeTransformer = factory.produce("remove", mapOf("regexPattern" to ".*"))
            removeTransformer.shouldBeInstanceOf<RemoveTransformer>()

            shouldThrow<IllegalArgumentException> { factory.produce("remove") }
            shouldThrow<PatternSyntaxException> { factory.produce("remove", mapOf("regexPattern" to "*")) }
        }

        test("replace") {
            val replaceTransformer = factory.produce("replace", mapOf("regexPattern" to ".*", "with" to "stuff"))
            replaceTransformer.shouldBeInstanceOf<ReplaceTransformer>()

            shouldThrow<IllegalArgumentException> { factory.produce("replace") }
            shouldThrow<IllegalArgumentException> { factory.produce("replace", mapOf("regexPattern" to ".*")) }
            shouldThrow<IllegalArgumentException> { factory.produce("replace", mapOf("with" to "stuff")) }
            shouldThrow<PatternSyntaxException> { factory.produce("replace", mapOf("regexPattern" to "*", "with" to "stuff")) }
            shouldThrow<IllegalArgumentException> { factory.produce("replace", mapOf("regexPattern" to "", "with" to "stuff")) }
        }

        test("uppercase") {
            val uppercaseTransformer = factory.produce("uppercase")
            uppercaseTransformer.shouldBeInstanceOf<UppercaseTransformer>()
        }

        test("lowercase") {
            val lowercaseTransformer = factory.produce("lowercase")
            lowercaseTransformer.shouldBeInstanceOf<LowercaseTransformer>()
        }
    }
})
