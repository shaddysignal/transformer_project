package com.incode.transformer_project.transformers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class UppercaseTransformerTest : FunSpec({

    val transformer = UppercaseTransformer

    test("transform") {
        transformer.transform("test") shouldBe "TEST"
        transformer.transform("TEST") shouldBe "TEST"
        transformer.transform("TesT") shouldBe "TEST"
    }

})
