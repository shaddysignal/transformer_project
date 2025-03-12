package com.incode.transformer_project.transformers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LowercaseTransformerTest : FunSpec({

    val transformer = LowercaseTransformer

    test("transform") {
        transformer.transform("test") shouldBe "test"
        transformer.transform("TEST") shouldBe "test"
        transformer.transform("TesT") shouldBe "test"
    }

})
