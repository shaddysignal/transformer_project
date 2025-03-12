package com.incode.transformer_project

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

fun LocalDate.toInstant(): Instant =
    this.atStartOfDay().toInstant(ZoneOffset.UTC)