package com.incode.transformer_project

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

private val loggerMap = ConcurrentHashMap<String, KLogger>()

internal inline val <reified T : Logging> T.log: KLogger
    get() {
        val name = this::class.qualifiedName!!
        return loggerMap.getOrPut(name) {
            KotlinLogging.logger(name)
        }
    }

interface Logging