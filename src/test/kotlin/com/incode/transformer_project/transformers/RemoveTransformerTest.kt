package com.incode.transformer_project.transformers

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RemoveTransformerTest : FunSpec({

    test("transform") {
        val transformer = RemoveTransformer("e")

        transformer.transform("test-test") shouldBe "tst-tst"
        transformer.transform("TEST-TEST") shouldBe "TEST-TEST"
        transformer.transform("TesT-TesT") shouldBe "TsT-TsT"
    }

    test("transform-empty") {
        val transformer = RemoveTransformer("")

        transformer.transform("test") shouldBe "test"
        transformer.transform("TEST") shouldBe "TEST"
        transformer.transform("TesT") shouldBe "TesT"
    }

    test("transform-complex") {
        val transformer = RemoveTransformer("e.*t")

        transformer.transform("test") shouldBe "t"
        transformer.transform("TEST") shouldBe "TEST"
        transformer.transform("TesT") shouldBe "TesT"
    }

})
