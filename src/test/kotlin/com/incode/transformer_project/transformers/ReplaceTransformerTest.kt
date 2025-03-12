package com.incode.transformer_project.transformers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ReplaceTransformerTest : FunSpec({

    test("transform") {
        val transformer = ReplaceTransformer("e", "a")

        transformer.transform("test-test") shouldBe "tast-tast"
        transformer.transform("TEST-TEST") shouldBe "TEST-TEST"
        transformer.transform("TesT-TesT") shouldBe "TasT-TasT"
    }

    test("transform-group") {
        val transformer = ReplaceTransformer("(?<start>[tT]e)", "a\${start}")

        transformer.transform("test") shouldBe "atest"
        transformer.transform("TEST") shouldBe "TEST"
        transformer.transform("TesT") shouldBe "aTesT"
    }

    test("transform-complex") {
        val transformer = ReplaceTransformer("e.*t", "a")

        transformer.transform("test") shouldBe "ta"
        transformer.transform("TEST") shouldBe "TEST"
        transformer.transform("TesT") shouldBe "TesT"
    }

})
